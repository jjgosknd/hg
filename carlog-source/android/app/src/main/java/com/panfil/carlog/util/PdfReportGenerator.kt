package com.panfil.carlog.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.panfil.carlog.domain.CarInfo
import com.panfil.carlog.domain.Expense
import com.panfil.carlog.domain.ExpenseType
import java.io.File
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object PdfReportGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 40f
    private const val LINE_HEIGHT = 18f

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    private val titlePaint = Paint().apply {
        textSize = 20f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }

    private val headerPaint = Paint().apply {
        textSize = 14f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }

    private val bodyPaint = Paint().apply {
        textSize = 11f
        isAntiAlias = true
    }

    private val smallPaint = Paint().apply {
        textSize = 10f
        color = 0xFF666666.toInt()
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = 0xFFCCCCCC.toInt()
        strokeWidth = 0.5f
    }

    fun generateAndShare(
        context: Context,
        expenses: List<Expense>,
        carInfo: CarInfo,
        periodLabel: String,
    ) {
        val file = generate(context, expenses, carInfo, periodLabel)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "CarLog — Отчёт о расходах")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Поделиться отчётом").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun generate(
        context: Context,
        expenses: List<Expense>,
        carInfo: CarInfo,
        periodLabel: String,
    ): File {
        val doc = PdfDocument()
        var pageNum = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create()
        var page = doc.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN + 24f

        fun ensureSpace(needed: Float): Canvas {
            if (y + needed > PAGE_HEIGHT - MARGIN) {
                doc.finishPage(page)
                pageNum++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create()
                page = doc.startPage(pageInfo)
                canvas = page.canvas
                y = MARGIN
            }
            return canvas
        }

        // Title
        canvas.drawText("Отчёт о расходах — CarLog", MARGIN, y, titlePaint)
        y += LINE_HEIGHT * 2

        // Car info
        if (carInfo.brand.isNotEmpty()) {
            canvas.drawText(
                "${carInfo.brand} ${carInfo.model} ${carInfo.generation}",
                MARGIN, y, headerPaint,
            )
            y += LINE_HEIGHT
            canvas.drawText("Пробег: ${formatNumber(carInfo.mileage)} км", MARGIN, y, bodyPaint)
            y += LINE_HEIGHT * 1.5f
        }

        // Period
        canvas.drawText("Период: $periodLabel", MARGIN, y, headerPaint)
        y += LINE_HEIGHT * 1.5f

        // Total
        val total = expenses.sumOf { it.amount }
        canvas.drawText("Общая сумма: ${currencyFormat.format(total)}", MARGIN, y, headerPaint)
        y += LINE_HEIGHT * 2

        // By type
        val byType = expenses
            .groupBy { it.type }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
            .entries.sortedByDescending { it.value }

        if (byType.isNotEmpty()) {
            ensureSpace(LINE_HEIGHT * (byType.size + 2))
            canvas.drawText("Расходы по категориям:", MARGIN, y, headerPaint)
            y += LINE_HEIGHT * 1.2f

            byType.forEach { (type, amount) ->
                ensureSpace(LINE_HEIGHT)
                canvas.drawText(
                    "  ${type.label}: ${currencyFormat.format(amount)}",
                    MARGIN, y, bodyPaint,
                )
                y += LINE_HEIGHT
            }
            y += LINE_HEIGHT
        }

        // Line separator
        ensureSpace(LINE_HEIGHT * 2)
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += LINE_HEIGHT

        // Table header
        canvas.drawText("Дата", MARGIN, y, headerPaint)
        canvas.drawText("Тип", MARGIN + 80, y, headerPaint)
        canvas.drawText("Сумма", MARGIN + 240, y, headerPaint)
        canvas.drawText("Пробег", MARGIN + 360, y, headerPaint)
        y += LINE_HEIGHT * 0.5f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += LINE_HEIGHT * 0.8f

        // Expense rows
        expenses.sortedByDescending { it.date }.forEach { expense ->
            val c = ensureSpace(LINE_HEIGHT * 2)
            val dateStr = Instant.ofEpochMilli(expense.date)
                .atZone(ZoneId.systemDefault())
                .format(dateFormatter)
            c.drawText(dateStr, MARGIN, y, bodyPaint)
            c.drawText(expense.type.label, MARGIN + 80, y, bodyPaint)
            c.drawText(currencyFormat.format(expense.amount), MARGIN + 240, y, bodyPaint)
            if (expense.mileage > 0) {
                c.drawText("${formatNumber(expense.mileage)} км", MARGIN + 360, y, bodyPaint)
            }
            y += LINE_HEIGHT * 0.3f
            if (expense.description.isNotEmpty()) {
                y += LINE_HEIGHT * 0.5f
                c.drawText(expense.description, MARGIN + 10, y, smallPaint)
            }
            y += LINE_HEIGHT
        }

        doc.finishPage(page)

        val dir = File(context.cacheDir, "reports")
        dir.mkdirs()
        val file = File(dir, "carlog_report.pdf")
        file.outputStream().use { doc.writeTo(it) }
        doc.close()
        return file
    }

    private fun formatNumber(n: Int): String =
        NumberFormat.getNumberInstance(Locale("ru", "RU")).format(n)
}
