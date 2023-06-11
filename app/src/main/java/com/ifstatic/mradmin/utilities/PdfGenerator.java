package com.ifstatic.mradmin.utilities;

import android.content.Context;
import android.os.Environment;

import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.itextpdf.io.IOException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PdfGenerator {


    public void generatePDF(Context context, RecentTransactionModel transactionModel,String username) {
        PdfWriter writer = null;

        try {
            // Get the Downloads directory path
            String downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            // Create a file with a unique name in the Downloads directory
            String fileName = "bill.pdf";
            File file = new File(downloadsPath, fileName);

            // Create a PDF writer instance
            writer = new PdfWriter(new FileOutputStream(file));

            // Create a PDF document
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4);

            // Set the font
            PdfFont font = PdfFontFactory.createFont();

            // Add the title paragraph
            Paragraph title = new Paragraph(" Company Name")
                    .setFont(font)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add the date paragraph
            Paragraph date = new Paragraph("Date: " + getCurrentDate())
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(date);

            // Add the input values paragraphs
            Paragraph input1Paragraph = new Paragraph("Transaction id: " + username)
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginTop(10)
                    .setMarginBottom(10);
            Paragraph input3Paragraph = new Paragraph("Input 3: " + username)
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginTop(-10)
                    .setMarginBottom(10);
            Paragraph input2Paragraph = new Paragraph("User Name: " + username)
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginTop(10)
                    .setMarginBottom(10);
            Paragraph input4Paragraph = new Paragraph("address : " +transactionModel.getAmount() )
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginTop(10)
                    .setMarginBottom(10);
            Paragraph input5Paragraph = new Paragraph("address : " +transactionModel.getAddress() )
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginTop(10)
                    .setMarginBottom(10);
            // Add the input values paragraphs to the document
            document.add(input1Paragraph);
            document.add(input3Paragraph);
            document.add(input2Paragraph);
            document.add(input4Paragraph);
            document.add(input5Paragraph);

            // Close the document
            document.close();

            // Show a toast or perform any other actions to indicate successful PDF generation

            // Scan the saved file to make it visible in other apps
//            MediaScannerHelper.scanFile(context, file.getAbsolutePath());
        } catch (IOException | java.io.IOException e) {
            e.printStackTrace();
            // Show a toast or perform any other actions to indicate an error occurred during PDF generation
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
