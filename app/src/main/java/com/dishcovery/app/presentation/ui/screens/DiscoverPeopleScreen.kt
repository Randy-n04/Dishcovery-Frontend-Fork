package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.Match
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.theme.Cafe
import com.dishcovery.app.presentation.ui.theme.Primary
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.MatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverPeopleScreen(
    paddingValues: PaddingValues,
    categoryIds: List<Long>,
    navController: NavHostController,
    matchViewModel: MatchViewModel,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsState()
    val matches by matchViewModel.matches.collectAsState()
    val isLoading by matchViewModel.isLoading.collectAsState()
    val errorMessage by matchViewModel.errorMessage.collectAsState()
    val generatedPlanId by matchViewModel.generatedPlanId.collectAsState()

    var currentIndex by remember { mutableIntStateOf(0) }

    // Carga inicial de candidatos desde el backend real
    LaunchedEffect(user, categoryIds) {
        user?.let {
            matchViewModel.findMatches(it.id, categoryIds)
        }
    }

    // Navegar al Plan de Intercambio cuando se genera con éxito (Conectar -> Ver Plan)
    LaunchedEffect(generatedPlanId) {
        generatedPlanId?.let { planId ->
            navController.navigate(NavRoutes.ExchangePlan.createRoute(planId = planId))
            matchViewModel.clearGeneratedPlan()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.discover_people_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = Cafe,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                            tint = Cafe
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF9F9F2)
                )
            )
        },
        containerColor = Color(0xFFF9F9F2),
        modifier = Modifier.padding(paddingValues)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading && matches.isEmpty() -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = Primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.searching_chefs_loading),
                            color = Cafe,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                errorMessage != null -> {
                    NoCandidatesState(
                        onBack = { navController.popBackStack() }
                    )
                }
                matches.isEmpty() || currentIndex >= matches.size -> {
                    NoCandidatesState(
                        onBack = { navController.popBackStack() }
                    )
                }
                else -> {
                    val candidate = matches[currentIndex]
                    MatchCandidateCard(
                        candidate = candidate,
                        onNext = { currentIndex++ },
                        onConnect = {
                            user?.let { me ->
                                // Usamos la primera categoría para el plan por defecto
                                val planCategoryId = categoryIds.firstOrNull() ?: 0L
                                matchViewModel.generatePlan(me.id, candidate.profileId, planCategoryId, me.username)
                            }
                        },
                        isLoadingAction = isLoading
                    )
                }
            }
        }
    }
}

@Composable
private fun NoCandidatesState(
    onBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Gray.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_candidates_available),
            style = MaterialTheme.typography.titleMedium,
            color = Cafe,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.back_button_label),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun MatchCandidateCard(
    candidate: Match,
    onNext: () -> Unit,
    onConnect: () -> Unit,
    isLoadingAction: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen del Chef
            Box(modifier = Modifier.fillMaxWidth().height(360.dp)) {
                AsyncImage(
                    model = candidate.photoUrl ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${candidate.publicName}",
                    contentDescription = candidate.publicName,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = candidate.publicName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Cafe
                )

                candidate.location?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Motivo del Match (Reason)
                candidate.reason?.let {
                    Surface(
                        color = Primary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "✨", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = Cafe,
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                CandidateSkillSection(
                    title = stringResource(R.string.can_do_label),
                    skills = candidate.offeredSkills,
                    chipColor = Color(0xFFF1F4E6)
                )

                Spacer(modifier = Modifier.height(20.dp))

                CandidateSkillSection(
                    title = stringResource(R.string.want_to_learn_label),
                    skills = candidate.skillsToLearn,
                    chipColor = Color(0xFFFFF9C4).copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onNext,
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        color = Color(0xFFF5F5F5),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.next_button),
                                tint = Color.Gray,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onConnect,
                        enabled = !isLoadingAction,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            disabledContainerColor = Primary.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(32.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (isLoadingAction) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = stringResource(R.string.connect_button),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CandidateSkillSection(title: String, skills: List<String>, chipColor: Color) {
    Column {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (skills.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_skills_available),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            } else {
                skills.forEach { skill ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(skill, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = chipColor,
                            labelColor = Cafe
                        ),
                        border = null,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    }
}
