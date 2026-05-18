package com.yeipi.teoriacolas.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

private const val REPO_URL = "https://github.com/jpgaleanore/colasPro"

private val CONTRIBUTORS = listOf(
    "David Gómez",
    "Juan Pablo Galeano",
    "David Sánchez"
)

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Acerca de", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Text(
            "Aplicación Android para el análisis de sistemas de líneas de espera mediante " +
            "los modelos M/M/1 y M/M/S de la Teoría de Colas. Permite registrar los datos " +
            "de campo, calcular los indicadores de desempeño y generar un reporte en PDF.",
            style = MaterialTheme.typography.bodyMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Repositorio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(REPO_URL, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, REPO_URL.toUri())
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Abrir en GitHub") }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Desarrolladores",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Divider()
                CONTRIBUTORS.forEach { name ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("•  ", style = MaterialTheme.typography.bodyMedium)
                        Text(name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        Text(
            "Versión 1.0",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
