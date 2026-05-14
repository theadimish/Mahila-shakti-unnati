package com.example.mahilashaktiunnati

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.mahilashaktiunnati.utils.formatCurrency
import com.example.mahilashaktiunnati.viewmodel.MainViewModel
import java.io.File
import java.io.FileOutputStream

class ReportActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private var memberCount = 0
    private var totalCapital = "₹0"
    private var activeLoans = 0
    private var totalDisbursed = "₹0"
    private var pending = 0

    private var primaryColor = "#2D7454"
    private var accentColor = "#7B5E8E"
    private var lightCard = "#EEF7F1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        applySavedTheme()

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

        val savedGroupName =
            prefs.getString("group_name", "Mahila Shakti") ?: "Mahila Shakti"

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val membersTv = findViewById<TextView>(R.id.membersTv)
        val activeLoansTv = findViewById<TextView>(R.id.activeLoansTv)
        val savingsTv = findViewById<TextView>(R.id.savingsTv)
        val disbursedTv = findViewById<TextView>(R.id.disbursedTv)
        val netAssetTv = findViewById<TextView>(R.id.netAssetTv)
        val reportPreviewTv = findViewById<TextView>(R.id.reportPreviewTv)

        fun updatePreview() {

            val report = """
                FINANCIAL SUMMARY: $savedGroupName
                
                Date: 06-05-2026
                
                ----------------------------
                
                Total Members: $memberCount
                Total Savings: $totalCapital
                Active Loans: $activeLoans
                Total Disbursed: $totalDisbursed
                Pending Contributions: $pending
                
                ----------------------------
                
                End of monthly report.
            """.trimIndent()

            reportPreviewTv.text = report
        }

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        viewModel.members.observe(this) { members ->
            memberCount = members.size
            membersTv.text = memberCount.toString()
            updatePreview()
        }

        viewModel.totalGroupCapital.observe(this) { total ->
            totalCapital = formatCurrency(total)
            savingsTv.text = totalCapital
            netAssetTv.text = totalCapital
            updatePreview()
        }

        viewModel.activeLoanCount.observe(this) { count ->
            activeLoans = count
            activeLoansTv.text = count.toString()
            updatePreview()
        }

        viewModel.totalDisbursed.observe(this) { total ->
            totalDisbursed = formatCurrency(total)
            disbursedTv.text = totalDisbursed
            updatePreview()
        }

        viewModel.pendingContributions.observe(this) { count ->
            pending = count
            updatePreview()
        }

        findViewById<TextView>(R.id.downloadPdfBtn).setOnClickListener {
            val pdfFile = createPdf(reportPreviewTv.text.toString())
            sharePdf(pdfFile)
        }

        findViewById<Button>(R.id.whatsappBtn).setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.setPackage("com.whatsapp")
            intent.putExtra(Intent.EXTRA_TEXT, reportPreviewTv.text.toString())

            try {
                startActivity(intent)
            } catch (e: Exception) {

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, reportPreviewTv.text.toString())

                startActivity(Intent.createChooser(shareIntent, "Share Report via"))
            }
        }

        findViewById<Button>(R.id.viewFullBtn).setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, reportPreviewTv.text.toString())

            startActivity(Intent.createChooser(shareIntent, "Share Report via"))
        }
    }

    private fun applySavedTheme() {

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val theme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = theme == "Purple" || theme == "Purple Accent"
        val isGreen = theme == "Green" || theme == "Green Only"

        primaryColor =
            if (isPurple)
                "#7B5E8E"
            else
                "#2D7454"

        accentColor =
            if (isGreen)
                "#2D7454"
            else
                "#7B5E8E"

        lightCard =
            if (isPurple)
                "#F3EDF7"
            else
                "#EEF7F1"

        findViewById<Button>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.exportTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.reportPreviewTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.downloadPdfBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<GridLayout>(R.id.summaryCard)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        findViewById<Button>(R.id.viewFullBtn)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        findViewById<Button>(R.id.whatsappBtn)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.reportPreviewTv)
            .setTextColor(Color.parseColor(accentColor))
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }

    private fun createPdf(reportText: String): File {

        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

        val page = pdfDocument.startPage(pageInfo)

        val canvas: Canvas = page.canvas

        val paint = Paint()
        paint.textSize = 14f

        var y = 50f
        val x = 40f

        reportText.split("\n").forEach { line ->
            canvas.drawText(line, x, y, paint)
            y += 24f
        }

        pdfDocument.finishPage(page)

        val folder = File(cacheDir, "reports")

        if (!folder.exists()) {
            folder.mkdirs()
        }

        val file = File(folder, "mahila_shakti_unnati_report.pdf")

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        ToastHelper.show(this, "PDF created")

        return file
    }

    private fun sharePdf(file: File) {

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(intent, "Download / Share PDF"))
    }
}