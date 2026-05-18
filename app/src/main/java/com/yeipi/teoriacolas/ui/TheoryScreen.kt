package com.yeipi.teoriacolas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TheoryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle("¿Qué es la Teoría de Colas?")
        Paragraph(
            "La Teoría de Colas es una rama de la investigación de operaciones que estudia " +
            "matemáticamente las líneas de espera. Permite modelar sistemas en los que " +
            "clientes (personas, llamadas, paquetes, vehículos, etc.) llegan a solicitar un " +
            "servicio y, si el servidor está ocupado, deben esperar en una cola. El objetivo " +
            "es entender el comportamiento del sistema y tomar decisiones que equilibren el " +
            "costo de operación con la calidad del servicio."
        )

        SectionTitle("Modelos implementados en la app")
        Paragraph(
            "Esta aplicación calcula dos modelos clásicos que usan la notación de Kendall " +
            "A/B/C, donde A = distribución de llegadas, B = distribución del servicio y " +
            "C = número de servidores."
        )
        BulletItem(
            "M/M/1",
            "Llegadas tipo Poisson, tiempos de servicio exponenciales y un único servidor. " +
            "Es el modelo más simple y útil cuando hay un solo punto de atención."
        )
        BulletItem(
            "M/M/S",
            "Igual que M/M/1 pero con S servidores en paralelo que comparten una sola cola. " +
            "Útil cuando varios servidores atienden simultáneamente a los clientes."
        )

        SectionTitle("Supuestos del modelo")
        BulletItem("Llegadas independientes con tasa promedio constante λ (Poisson).")
        BulletItem("Tiempos de servicio independientes con tasa promedio μ (exponencial).")
        BulletItem("Disciplina FIFO: el primero en llegar es el primero en ser atendido.")
        BulletItem("Capacidad de la cola y población son infinitas.")
        BulletItem("El sistema está en estado estable, es decir, ρ < 1.")

        SectionTitle("Parámetros de entrada")
        ParamItem(
            "λ (lambda) — Tasa de llegada",
            "Número promedio de clientes que llegan por unidad de tiempo (por ejemplo, " +
            "clientes/hora). Se obtiene contando llegadas durante un periodo de observación " +
            "y dividiendo entre la duración. Ejemplo: si llegan 30 clientes en 1 hora, λ = 30."
        )
        ParamItem(
            "μ (mu) — Tasa de servicio",
            "Número promedio de clientes que un servidor puede atender por unidad de tiempo. " +
            "Si un cajero tarda en promedio 4 minutos por cliente, μ = 60/4 = 15 clientes/hora. " +
            "Debe expresarse en la misma unidad de tiempo que λ."
        )
        ParamItem(
            "S — Número de servidores",
            "Cantidad de servidores en paralelo que atienden desde una misma cola. Solo se " +
            "usa en el modelo M/M/S. Para que el sistema sea estable, S·μ debe ser mayor que λ."
        )

        SectionTitle("Resultados que entrega la app")
        ResultItem(
            "ρ (rho) — Utilización",
            "Fracción del tiempo que los servidores están ocupados. Se calcula como " +
            "ρ = λ/μ (M/M/1) o ρ = λ/(S·μ) (M/M/S). Si ρ ≥ 1 el sistema es inestable."
        )
        ResultItem(
            "P₀ — Probabilidad de sistema vacío",
            "Probabilidad de que no haya ningún cliente en el sistema en un instante dado."
        )
        ResultItem(
            "L — Clientes en el sistema",
            "Número promedio de clientes presentes (en cola más en servicio)."
        )
        ResultItem(
            "Lq — Clientes en cola",
            "Número promedio de clientes esperando a ser atendidos."
        )
        ResultItem(
            "W — Tiempo en el sistema",
            "Tiempo promedio que un cliente permanece en el sistema (cola + servicio)."
        )
        ResultItem(
            "Wq — Tiempo en cola",
            "Tiempo promedio que un cliente espera antes de ser atendido."
        )
        ResultItem(
            "Pw — Probabilidad de espera (solo M/M/S)",
            "Probabilidad de que un cliente que llega tenga que esperar porque todos los " +
            "servidores estén ocupados."
        )

        SectionTitle("Ecuaciones empleadas")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("M/M/1", fontWeight = FontWeight.Bold)
                Text(
                    "ρ = λ/μ\n" +
                    "P₀ = 1 − ρ\n" +
                    "L  = ρ/(1 − ρ)\n" +
                    "Lq = ρ²/(1 − ρ)\n" +
                    "W  = 1/(μ − λ)\n" +
                    "Wq = λ/[μ(μ − λ)]",
                    style = MaterialTheme.typography.bodySmall
                )
                Divider()
                Text("M/M/S", fontWeight = FontWeight.Bold)
                Text(
                    "a  = λ/μ\n" +
                    "ρ  = a/S\n" +
                    "P₀ = 1 / [ Σₙ₌₀ˢ⁻¹ (aⁿ/n!) + (aˢ/S!)·1/(1 − ρ) ]\n" +
                    "Lq = [P₀·aˢ·ρ] / [S!·(1 − ρ)²]\n" +
                    "L  = Lq + a\n" +
                    "Wq = Lq/λ\n" +
                    "W  = Wq + 1/μ\n" +
                    "Pw = (aˢ / [S!·(1 − ρ)]) · P₀",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        SectionTitle("Casos de uso")
        UseCaseItem(
            "Bancos y cajeros",
            "Determinar cuántos cajeros abrir según la afluencia y reducir la espera del cliente."
        )
        UseCaseItem(
            "Supermercados",
            "Planificar el número de cajas registradoras activas en distintas franjas horarias."
        )
        UseCaseItem(
            "Hospitales y clínicas",
            "Dimensionar consultorios, ventanillas de admisión o salas de urgencias."
        )
        UseCaseItem(
            "Call centers y mesas de ayuda",
            "Calcular agentes necesarios para mantener un nivel de servicio objetivo."
        )
        UseCaseItem(
            "Peajes y estaciones de servicio",
            "Definir el número de casetas para evitar congestiones en horas pico."
        )
        UseCaseItem(
            "Líneas de producción y talleres",
            "Analizar cuellos de botella entre estaciones de trabajo."
        )
        UseCaseItem(
            "Redes y servidores",
            "Estimar latencias y tamaños de buffer en sistemas informáticos."
        )

        SectionTitle("¿Para qué sirve esta app?")
        Paragraph(
            "La app permite tomar mediciones de campo (proyecto, analista, ubicación, " +
            "observaciones) y, con la tasa de llegada λ y la tasa de servicio μ, calcular " +
            "todos los indicadores de desempeño del sistema. Con los resultados se puede:"
        )
        BulletItem("Detectar si el sistema actual es estable o está saturado.")
        BulletItem("Estimar tiempos de espera y de servicio promedio.")
        BulletItem("Comparar escenarios cambiando el número de servidores (M/M/S).")
        BulletItem("Justificar decisiones de capacidad ante stakeholders.")
        BulletItem("Generar un reporte PDF con los datos y resultados para archivar o compartir.")

        SectionTitle("Recomendaciones de medición")
        BulletItem(
            "Toma muestras en periodos de comportamiento homogéneo (por ejemplo, no mezcles " +
            "hora pico con hora valle)."
        )
        BulletItem("Cuantos más datos recojas, más confiables serán λ y μ.")
        BulletItem("Asegúrate de usar las mismas unidades de tiempo para λ y μ.")
        BulletItem(
            "Si las llegadas o los servicios no son aleatorios (p. ej. citas programadas), " +
            "los modelos M/M no son apropiados."
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
}

@Composable
private fun Paragraph(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun BulletItem(title: String, body: String? = null) {
    Row(Modifier.fillMaxWidth()) {
        Text("•  ", style = MaterialTheme.typography.bodyMedium)
        Column {
            if (body == null) {
                Text(title, style = MaterialTheme.typography.bodyMedium)
            } else {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(body, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ParamItem(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(body, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ResultItem(title: String, body: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(title, fontWeight = FontWeight.SemiBold)
        Text(body, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun UseCaseItem(title: String, body: String) {
    Column(Modifier.fillMaxWidth()) {
        Text("• $title", fontWeight = FontWeight.SemiBold)
        Text("   $body", style = MaterialTheme.typography.bodySmall)
    }
}
