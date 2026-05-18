package com.yeipi.teoriacolas.report

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.yeipi.teoriacolas.domain.QueueingResult
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    // A4 a 72 dpi
    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 50f

    fun generate(context: Context, result: QueueingResult): Uri? {
        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = pdf.startPage(pageInfo)
        drawContent(page.canvas, result)
        pdf.finishPage(page)

        val fileName = "Informe_Colas_${
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        }.pdf"

        val uri = savePdf(context, pdf, fileName)
        pdf.close()
        return uri
    }

    private fun drawContent(canvas: Canvas, r: QueueingResult) {
        val titlePaint = Paint().apply {
            color = Color.rgb(25, 55, 109)
            textSize = 20f
            isFakeBoldText = true
            isAntiAlias = true
        }
        val sectionPaint = Paint().apply {
            color = Color.rgb(25, 55, 109)
            textSize = 14f
            isFakeBoldText = true
            isAntiAlias = true
        }
        val bodyPaint = Paint().apply {
            color = Color.BLACK
            textSize = 11f
            isAntiAlias = true
        }
        val mutedPaint = Paint().apply {
            color = Color.GRAY
            textSize = 9f
            isAntiAlias = true
        }
        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 0.8f
        }

        var y = MARGIN + 10f

        // Encabezado
        canvas.drawText("Informe de Análisis de Colas", MARGIN, y, titlePaint)
        y += 8f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 18f

        val fmtDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        canvas.drawText("Generado: ${fmtDate.format(Date(r.timestamp))}", MARGIN, y, mutedPaint)
        y += 25f

        // Información del estudio
        canvas.drawText("1. Información del estudio", MARGIN, y, sectionPaint)
        y += 18f
        y = drawField(canvas, "Proyecto:", r.projectName.ifBlank { "—" }, MARGIN, y, bodyPaint)
        y = drawField(canvas, "Analista:", r.analyst.ifBlank { "—" }, MARGIN, y, bodyPaint)
        y = drawField(canvas, "Ubicación:", r.location.ifBlank { "—" }, MARGIN, y, bodyPaint)
        y += 10f

        // Modelo
        canvas.drawText("2. Modelo y datos de entrada", MARGIN, y, sectionPaint)
        y += 18f
        y = drawField(canvas, "Modelo seleccionado:", r.modelType, MARGIN, y, bodyPaint)
        y = drawField(canvas, "λ (tasa llegada):", "${"%.4f".format(r.lambda)} clientes/h", MARGIN, y, bodyPaint)
        y = drawField(canvas, "μ (tasa servicio):", "${"%.4f".format(r.mu)} clientes/h", MARGIN, y, bodyPaint)
        if (r.servers > 1) {
            y = drawField(canvas, "S (servidores):", r.servers.toString(), MARGIN, y, bodyPaint)
        }
        y += 10f

        // Ecuaciones
        canvas.drawText("3. Ecuaciones empleadas", MARGIN, y, sectionPaint)
        y += 18f
        val equations = if (r.modelType == "M/M/1") listOf(
            "ρ = λ / μ",
            "P₀ = 1 - ρ",
            "L = ρ / (1 - ρ)",
            "Lq = ρ² / (1 - ρ)",
            "W = 1 / (μ - λ)",
            "Wq = λ / [μ (μ - λ)]"
        ) else listOf(
            "a = λ / μ ;  ρ = a / s",
            "P₀ = 1 / [ Σₙ₌₀^(s-1)(aⁿ/n!) + (aˢ/s!)·1/(1-ρ) ]",
            "Lq = [P₀ · aˢ · ρ] / [s! · (1 - ρ)²]",
            "L = Lq + a",
            "Wq = Lq / λ",
            "W = Wq + 1/μ",
            "Pw = (aˢ / [s! · (1-ρ)]) · P₀"
        )
        equations.forEach {
            canvas.drawText("• $it", MARGIN + 8f, y, bodyPaint)
            y += 16f
        }
        y += 6f

        // Resultados
        canvas.drawText("4. Resultados", MARGIN, y, sectionPaint)
        y += 18f
        y = drawField(canvas, "ρ — Utilización:", "%.4f".format(r.rho), MARGIN, y, bodyPaint)
        y = drawField(canvas, "P₀ — Prob. sistema vacío:", "%.4f".format(r.p0), MARGIN, y, bodyPaint)
        y = drawField(canvas, "L — Clientes en sistema:", "%.4f".format(r.l), MARGIN, y, bodyPaint)
        y = drawField(canvas, "Lq — Clientes en cola:", "%.4f".format(r.lq), MARGIN, y, bodyPaint)
        y = drawField(canvas, "W — Tiempo en sistema:", "${"%.4f".format(r.w)} h", MARGIN, y, bodyPaint)
        y = drawField(canvas, "Wq — Tiempo en cola:", "${"%.4f".format(r.wq)} h", MARGIN, y, bodyPaint)
        r.pw?.let {
            y = drawField(canvas, "Pw — Prob. de espera:", "%.4f".format(it), MARGIN, y, bodyPaint)
        }
        y += 10f

        // Observaciones
        if (r.observations.isNotBlank()) {
            canvas.drawText("5. Observaciones", MARGIN, y, sectionPaint)
            y += 18f
            r.observations.chunked(85).forEach { line ->
                canvas.drawText(line, MARGIN, y, bodyPaint)
                y += 14f
            }
        }

        // Pie de página
        canvas.drawLine(MARGIN, PAGE_HEIGHT - 40f, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 40f, linePaint)
        canvas.drawText(
            "Generado automáticamente — App de Teoría de Colas",
            MARGIN, PAGE_HEIGHT - 25f, mutedPaint
        )
    }

    private fun drawField(canvas: Canvas, label: String, value: String, x: Float, y: Float, paint: Paint): Float {
        val labelPaint = Paint(paint).apply { isFakeBoldText = true }
        canvas.drawText(label, x, y, labelPaint)
        canvas.drawText(value, x + 160f, y, paint)
        return y + 16f
    }

    /**
     * Guarda el PDF en la carpeta Downloads usando MediaStore (Android 10+)
     * o el método legacy en versiones anteriores.
     */
    private fun savePdf(context: Context, pdf: PdfDocument, fileName: String): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                resolver.openOutputStream(it)?.use { os -> pdf.writeTo(os) }
            }
            uri
        } else {
            // Android 9 y anteriores: necesita permiso WRITE_EXTERNAL_STORAGE
            val downloads = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            if (!downloads.exists()) downloads.mkdirs()
            val file = File(downloads, fileName)
            FileOutputStream(file).use { pdf.writeTo(it) }
            Uri.fromFile(file)
        }
    }
}