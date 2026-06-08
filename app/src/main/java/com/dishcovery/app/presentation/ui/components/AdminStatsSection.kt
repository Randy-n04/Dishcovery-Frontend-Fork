package com.dishcovery.app.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.StatPoint
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
import com.dishcovery.app.presentation.viewmodel.AdminStatisticsViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries

@Composable
fun AdminStatsSection(
    viewModel: AdminStatisticsViewModel = hiltViewModel()
) {
    val activeUsers by viewModel.activeUsers.collectAsState()
    val matches     by viewModel.matches.collectAsState()
    val isLoading   by viewModel.isLoading.collectAsState()
    val error       by viewModel.error.collectAsState()

    if (isLoading) {
        Box(Modifier.fillMaxWidth().height(200.dp), Alignment.Center) {
            CircularProgressIndicator(color = Rojo)
        }
        return
    }

    if (error != null) {
        Text(error!!, color = Rojo, fontSize = 13.sp)
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StatChart(
            title  = stringResource(R.string.stats_active_users_title),
            points = activeUsers
        )
        StatChart(
            title  = stringResource(R.string.stats_matches_title),
            points = matches
        )
    }
}

@Composable
fun StatChart(
    title : String,
    points: List<StatPoint>
) {
    AdminSection(title) {
        if (points.isEmpty()) {
            Box(Modifier.fillMaxWidth().height(120.dp), Alignment.Center) {
                Text(
                    stringResource(R.string.stats_no_data),
                    color    = TextPrimary.copy(.4f),
                    fontSize = 13.sp
                )
            }
            return@AdminSection
        }

        val modelProducer = remember { CartesianChartModelProducer() }

        LaunchedEffect(points) {
            modelProducer.runTransaction {
                lineSeries { series(points.map { it.count.toFloat() }) }
            }
        }

        val labels = points.map { it.date.takeLast(5) }

        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis  = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom()
            ),
            modelProducer = modelProducer,
            modifier      = Modifier.fillMaxWidth().height(180.dp)
        )
    }
}