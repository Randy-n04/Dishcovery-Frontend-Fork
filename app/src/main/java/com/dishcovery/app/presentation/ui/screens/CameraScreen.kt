package com.dishcovery.app.presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dishcovery.app.R
import com.dishcovery.app.presentation.ui.theme.Beige
import com.dishcovery.app.presentation.ui.theme.Cafe
import com.dishcovery.app.presentation.viewmodel.CameraViewModel
import java.io.File
import java.io.FileOutputStream

private val CafeLight = Color(0xFFFFE8DC)
private val TagBackground = Color(0xFFFFF3F0)
private val TagBorder = Color(0xFFF5C4B3)
private val TagText = Color(0xFFA0432A)

@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val (photoFile, photoUri) = remember {
        val file = File(context.cacheDir, "temp_photo.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        file to uri
    }

    var capturedUri by remember { mutableStateOf<Uri?>(null) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showRetakeDialog by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedUri = photoUri
            viewModel.analyzeImage(photoFile)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            capturedUri = it
            // Copy URI content to temp file for analysis
            try {
                context.contentResolver.openInputStream(it)?.use { input ->
                    FileOutputStream(photoFile).use { output ->
                        input.copyTo(output)
                    }
                }
                viewModel.analyzeImage(photoFile)
            } catch (e: Exception) {
                // Error handling is managed by ViewModel/GlobalError
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) cameraLauncher.launch(photoUri)
    }

    // Intercepta el gesto/botón de sistema atrás
    BackHandler(enabled = uiState is CameraViewModel.CameraUiState.Success) {
        showExitDialog = true
    }

    // Diálogo — salir
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.camera_exit_dialog_title),
                    color = Cafe
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.camera_exit_dialog_body),
                    color = TagText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        viewModel.reset()
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Cafe)
                ) {
                    Text(
                        text = stringResource(R.string.camera_exit_dialog_confirm),
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showExitDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = CafeLight)
                ) {
                    Text(
                        text = stringResource(R.string.camera_exit_dialog_cancel),
                        color = Cafe
                    )
                }
            }
        )
    }

    // Diálogo — tomar otra foto / elegir otra imagen
    if (showRetakeDialog) {
        AlertDialog(
            onDismissRequest = { showRetakeDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.camera_retake_dialog_title),
                    color = Cafe
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.camera_retake_dialog_body),
                    color = TagText
                )
            },
            confirmButton = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            showRetakeDialog = false
                            viewModel.reset()
                            val permission = Manifest.permission.CAMERA
                            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(photoUri)
                            } else {
                                permissionLauncher.launch(permission)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Cafe)
                    ) {
                        Text(
                            text = stringResource(R.string.camera_take_photo_button),
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = {
                            showRetakeDialog = false
                            viewModel.reset()
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CafeLight)
                    ) {
                        Text(
                            text = stringResource(R.string.camera_upload_button),
                            color = Cafe
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRetakeDialog = false }
                ) {
                    Text(
                        text = stringResource(R.string.camera_exit_dialog_cancel),
                        color = Cafe
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige)
    ) {

        // ── Header ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (uiState is CameraViewModel.CameraUiState.Success) {
                        showExitDialog = true
                    } else {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_desc),
                        tint = Cafe
                    )
                }
                Text(
                    text = stringResource(R.string.camera_screen_title),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = Cafe
                )
            }
        }

        HorizontalDivider(color = Color(0xFFE8E8E8), thickness = 1.dp)

        // ── Contenido según estado ────────────────────────────────────────
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {

                is CameraViewModel.CameraUiState.Idle -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            shadowElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.camera_idle_hint),
                                color = Cafe,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    val permission = Manifest.permission.CAMERA
                                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                        cameraLauncher.launch(photoUri)
                                    } else {
                                        permissionLauncher.launch(permission)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Cafe),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.camera_take_photo_button),
                                    color = Color.White
                                )
                            }
                            
                            Button(
                                onClick = { galleryLauncher.launch("image/*") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = CafeLight),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.camera_upload_button),
                                    color = Cafe
                                )
                            }
                        }
                    }
                }

                is CameraViewModel.CameraUiState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = Cafe)
                        Text(
                            text = stringResource(R.string.camera_analyzing),
                            color = Cafe
                        )
                    }
                }

                is CameraViewModel.CameraUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Contenido scrollable
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Foto tomada/seleccionada
                            capturedUri?.let { uri ->
                                Box {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = stringResource(R.string.camera_photo_desc),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Surface(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(10.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        color = Color(0x8C000000)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.camera_analyzed_badge),
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            }

                            // Card de resultados
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(shape = CircleShape, color = CafeLight, modifier = Modifier.size(32.dp)) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Coffee,
                                                    contentDescription = null,
                                                    tint = Cafe,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = stringResource(R.string.camera_result_title),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Cafe,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = stringResource(R.string.camera_tag_count, state.tags.size),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TagText
                                        )
                                    }

                                    HorizontalDivider(color = Color(0xFFE8E8E8))

                                    @OptIn(ExperimentalLayoutApi::class)
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        state.tags.forEach { tag ->
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = TagBackground,
                                                border = androidx.compose.foundation.BorderStroke(
                                                    width = 0.5.dp,
                                                    color = TagBorder
                                                )
                                            ) {
                                                Text(
                                                    text = tag,
                                                    modifier = Modifier.padding(
                                                        horizontal = 12.dp,
                                                        vertical = 5.dp
                                                    ),
                                                    color = TagText,
                                                    style = MaterialTheme.typography.labelMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Botón retomar/nueva — siempre visible al fondo
                            Button(
                                onClick = { showRetakeDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(26.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Cafe)
                            ) {
                                Text(
                                    text = stringResource(R.string.camera_retake_button),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                is CameraViewModel.CameraUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = {
                                viewModel.reset()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Cafe)
                        ) {
                            Text(
                                text = stringResource(R.string.camera_retry_button),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
