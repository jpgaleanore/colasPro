package com.yeipi.teoriacolas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenCalculator: () -> Unit,
    onOpenTheory: () -> Unit,
    onOpenAbout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeroSection()

        Text(
            "¿Qué deseas hacer?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        HomeOptionCard(
            badge = "λ",
            title = "Calculadora",
            description = "Ingresa los parámetros del sistema (λ, μ, S) y obtén las " +
                "métricas de desempeño. Genera un reporte PDF con los resultados.",
            actionLabel = "Empezar análisis",
            onClick = onOpenCalculator,
            highlight = true
        )

        HomeOptionCard(
            badge = "📘",
            title = "Teoría",
            description = "Aprende qué es la Teoría de Colas, cómo medir cada parámetro, " +
                "el significado de las métricas y los casos de uso típicos.",
            actionLabel = "Ver teoría",
            onClick = onOpenTheory
        )

        HomeOptionCard(
            badge = "i",
            title = "Acerca de",
            description = "Información de la aplicación, enlace al repositorio en GitHub " +
                "y créditos de los desarrolladores.",
            actionLabel = "Ver detalles",
            onClick = onOpenAbout
        )

        QuickInfoSection()

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun HeroSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "ColasPro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                "Análisis de sistemas de líneas de espera",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Calcula los indicadores de desempeño de tus sistemas usando los " +
                "modelos M/M/1 y M/M/S, comprende la teoría detrás de los cálculos " +
                "y genera reportes profesionales en PDF.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun HomeOptionCard(
    badge: String,
    title: String,
    description: String,
    actionLabel: String,
    onClick: () -> Unit,
    highlight: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = if (highlight)
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        else CardDefaults.cardColors()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (highlight) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        badge,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (highlight) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(description, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                if (highlight) {
                    Button(onClick = onClick) { Text(actionLabel) }
                } else {
                    TextButton(onClick = onClick) { Text("$actionLabel  →") }
                }
            }
        }
    }
}

@Composable
private fun QuickInfoSection() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "¿Sabías que…?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            QuickInfoRow(
                "M/M/1",
                "Un único servidor con llegadas Poisson y servicios exponenciales."
            )
            QuickInfoRow(
                "M/M/S",
                "Varios servidores compartiendo una sola cola FIFO."
            )
            QuickInfoRow(
                "ρ < 1",
                "Condición de estabilidad: la tasa de llegada debe ser menor que la capacidad."
            )
        }
    }
}

@Composable
private fun QuickInfoRow(tag: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                tag,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
    }
}
