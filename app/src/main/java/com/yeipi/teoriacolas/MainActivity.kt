package com.yeipi.teoriacolas

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yeipi.teoriacolas.domain.QueueingCalculator
import com.yeipi.teoriacolas.domain.QueueingInput
import com.yeipi.teoriacolas.domain.QueueingResult
import com.yeipi.teoriacolas.report.PdfActions
import com.yeipi.teoriacolas.report.PdfReportGenerator
import com.yeipi.teoriacolas.ui.AboutScreen
import com.yeipi.teoriacolas.ui.TheoryScreen
import com.yeipi.teoriacolas.ui.theme.TeoriaColasTheme

private enum class Screen(val title: String, val label: String) {
    Calculator("Teoría de Colas", "Calculadora"),
    Theory("Teoría de Colas — Teoría", "Teoría"),
    About("Teoría de Colas — Acerca de", "Acerca de")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeoriaColasTheme {
                Surface(Modifier.fillMaxSize()) {
                    QueueingApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueingApp() {
    var currentScreen by remember { mutableStateOf(Screen.Calculator) }
    var menuExpanded by remember { mutableStateOf(false) }

    // Estados de los metadatos
    var projectName by remember { mutableStateOf("") }
    var analyst by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }

    // Estados del modelo y cálculos
    var modelType by remember { mutableStateOf("M/M/1") }
    var lambdaText by remember { mutableStateOf("") }
    var muText by remember { mutableStateOf("") }
    var serversText by remember { mutableStateOf("2") }
    var result by remember { mutableStateOf<QueueingResult?>(null) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Estado del PDF generado
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreen.title) },
                actions = {
                    Box {
                        TextButton(onClick = { menuExpanded = true }) { Text("Menú") }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            Screen.entries.forEach { screen ->
                                DropdownMenuItem(
                                    text = { Text(screen.label) },
                                    onClick = {
                                        currentScreen = screen
                                        menuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (currentScreen) {
            Screen.Theory -> {
                TheoryScreen(modifier = Modifier.padding(padding))
                return@Scaffold
            }
            Screen.About -> {
                AboutScreen(modifier = Modifier.padding(padding))
                return@Scaffold
            }
            Screen.Calculator -> { /* renderiza la calculadora abajo */ }
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Datos de campo", style = MaterialTheme.typography.titleMedium)

            // Metadatos del estudio
            OutlinedTextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text("Proyecto") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = analyst,
                onValueChange = { analyst = it },
                label = { Text("Analista") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Ubicación / Sitio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))

            // Selector de modelo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Modelo:", Modifier.weight(1f))
                FilterChip(
                    selected = modelType == "M/M/1",
                    onClick = { modelType = "M/M/1" },
                    label = { Text("M/M/1") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = modelType == "M/M/S",
                    onClick = { modelType = "M/M/S" },
                    label = { Text("M/M/S") }
                )
            }

            // Parámetros numéricos
            OutlinedTextField(
                value = lambdaText,
                onValueChange = { lambdaText = it },
                label = { Text("λ — Tasa de llegada (clientes/hora)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = muText,
                onValueChange = { muText = it },
                label = { Text("μ — Tasa de servicio (clientes/hora)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            if (modelType == "M/M/S") {
                OutlinedTextField(
                    value = serversText,
                    onValueChange = { serversText = it },
                    label = { Text("S — Número de servidores") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Observaciones
            OutlinedTextField(
                value = observations,
                onValueChange = { observations = it },
                label = { Text("Observaciones") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // Botón Calcular
            Button(
                onClick = {
                    errorMsg = null
                    result = null
                    pdfUri = null
                    try {
                        val lambda = lambdaText.toDouble()
                        val mu = muText.toDouble()
                        val s = if (modelType == "M/M/S") serversText.toInt() else 1
                        val input = QueueingInput(
                            lambda = lambda,
                            mu = mu,
                            servers = s,
                            projectName = projectName,
                            analyst = analyst,
                            location = location,
                            observations = observations
                        )
                        result = if (modelType == "M/M/1")
                            QueueingCalculator.calculateMM1(input)
                        else
                            QueueingCalculator.calculateMMs(input)
                    } catch (e: NumberFormatException) {
                        errorMsg = "Verifica que los valores sean números válidos"
                    } catch (e: IllegalArgumentException) {
                        errorMsg = e.message
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Calcular") }

            // Mensaje de error
            errorMsg?.let {
                Card(colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )) {
                    Text(it, Modifier.padding(12.dp))
                }
            }

            // Tarjeta de resultados
            result?.let { ResultsCard(it) }

            // Botón Generar PDF
            result?.let { r ->
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        pdfUri = PdfReportGenerator.generate(context, r)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Generar PDF") }
            }

            // Tarjeta con acciones sobre el PDF generado
            pdfUri?.let { uri ->
                Card {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            "PDF guardado en Descargas ✓",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { PdfActions.openPdf(context, uri) },
                                modifier = Modifier.weight(1f)
                            ) { Text("Abrir") }
                            OutlinedButton(
                                onClick = { PdfActions.sharePdf(context, uri) },
                                modifier = Modifier.weight(1f)
                            ) { Text("Compartir") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsCard(r: QueueingResult) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Resultados — ${r.modelType}", style = MaterialTheme.typography.titleMedium)
            Divider()
            ResultRow("ρ (utilización)", r.rho)
            ResultRow("P₀ (sistema vacío)", r.p0)
            ResultRow("L (clientes en sistema)", r.l)
            ResultRow("Lq (clientes en cola)", r.lq)
            ResultRow("W (tiempo en sistema)", r.w)
            ResultRow("Wq (tiempo en cola)", r.wq)
            r.pw?.let { ResultRow("Pw (prob. de espera)", it) }

            Spacer(Modifier.height(12.dp))
            Text("Ecuaciones empleadas:", style = MaterialTheme.typography.titleSmall)
            Text(
                if (r.modelType == "M/M/1")
                    "ρ=λ/μ · P₀=1-ρ · L=ρ/(1-ρ) · Lq=ρ²/(1-ρ)\n" +
                            "W=1/(μ-λ) · Wq=λ/[μ(μ-λ)]"
                else
                    "a=λ/μ · ρ=a/s\n" +
                            "P₀=1/[Σ(aⁿ/n!) + (aˢ/s!)·1/(1-ρ)]\n" +
                            "Lq=[P₀·aˢ·ρ]/[s!·(1-ρ)²]\n" +
                            "L=Lq+a · Wq=Lq/λ · W=Wq+1/μ",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ResultRow(label: String, value: Double) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text("%.4f".format(value))
    }
}