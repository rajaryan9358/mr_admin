package com.ifstatic.mradmin.view.TransactionDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.databinding.ActivityTransactionDetailBinding;
import com.ifstatic.mradmin.models.OnlineDetailModel;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.utilities.Constants;
import com.ifstatic.mradmin.utilities.PdfGenerator;
import com.ifstatic.mradmin.view.EditTransaction.EditTransactionActivity;
import com.ifstatic.mradmin.view.EditUser.EditUserActivity;
import com.ifstatic.mradmin.view.EditUser.EditUserViewModel;
import com.itextpdf.io.IOException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionDetailActivity extends AppCompatActivity {
    ActivityTransactionDetailBinding transactionDetailBinding;
    RecentTransactionModel transactionModel;
//    OnlineDetailModel onlineDetailModel=new OnlineDetailModel();
    private TransactionDetailViewModel transactionDetailViewModel;
    private Dialog progressDialog;
    PdfGenerator  pdfGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionDetailBinding= DataBindingUtil.setContentView(this,R.layout.activity_transaction_detail);
        setListners();
        getBundles();
        initView();
        changevisibility(); // switching the upi chequw and online layouts

    }

    private void changevisibility() {
        if (transactionModel!=null) {
            switch (transactionModel.getPaymentMode()){
                case "Online":
                    transactionDetailBinding.onlineDetail.setVisibility(View.VISIBLE);
                    transactionDetailBinding.onlinedate.setText(transactionModel.getOnlineDetail().getDate());
                    transactionDetailBinding.onlinereference.setText(transactionModel.getOnlineDetail().getReferenceId());
                    break;
                case "UPI":
                        transactionDetailBinding.Upidetails.setVisibility(View.VISIBLE);
                        transactionDetailBinding.upiid.setText(transactionModel.getUpiDetail().getUpiId());
                        transactionDetailBinding.upidate.setText(transactionModel.getUpiDetail().getDate());
                    break;
                case "Cheque" :
                        transactionDetailBinding.chequqdetails.setVisibility(View.VISIBLE);
                        transactionDetailBinding.chequeNoTextView.setText(transactionModel.getChequeDataModel().getChequeNo());
                        transactionDetailBinding.datePaymentDetailTextView.setText(transactionModel.getChequeDataModel().getDate());
                        transactionDetailBinding.bankNameTextView.setText(transactionModel.getChequeDataModel().getBankName());
                    break;
                case "Case":
                            transactionDetailBinding.paymentDetailsLayout.setVisibility(View.INVISIBLE);
                            transactionDetailBinding.paymentDetailsTextView.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void initView() {
        transactionDetailViewModel=new ViewModelProvider(this).get(TransactionDetailViewModel.class);
        if(transactionModel==null){
            Toast.makeText(this, "No Data Associated", Toast.LENGTH_SHORT).show();
        }
        else if(transactionModel!=null){
            transactionDetailBinding.setTransactionDetail(transactionModel);
        }
    }


    @SuppressLint("SetTextI18n")
    private void setListners() {

        transactionDetailBinding.header.titleTextView.setText("Transaction Details");
        transactionDetailBinding.editTransactionTextView.setOnClickListener(v->{
            Bundle bundle =new Bundle();
            bundle.putParcelable("transaction_data",transactionModel);
            AppBoiler.navigateToActivityWithFinish(TransactionDetailActivity.this, EditTransactionActivity.class,bundle);
        });
        transactionDetailBinding.printTransactionTextView.setOnClickListener(v->{
         if (transactionModel!=null){
//             Toast.makeText(this, transactionModel.getTransactionId()+""+transactionModel?, Toast.LENGTH_SHORT).show();
            generatePDF(transactionModel,"Arun");
         }else{
             Toast.makeText(this, "Failed to generate Pdf", Toast.LENGTH_SHORT).show();
         }
        });


        transactionDetailBinding.DeleteTransactionTextView.setOnClickListener(v->{
            if(AppBoiler.isInternetConnected(this)){
                deleteUser(transactionModel.getTransactionId());
            } else{
                AppBoiler.showSnackBarForInternet(this,transactionDetailBinding.getRoot());
            }

        });
    }


    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            transactionModel = bundle.getParcelable("transaction_data");
        }else if (bundle==null){
            Toast.makeText(this, "No Data Associated", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser(String transactionID){
        progressDialog=AppBoiler.setProgressDialog(this);
        LiveData<String> responsedeleteUser = transactionDetailViewModel.deleteTransactionResponseLiveData(transactionID);
        responsedeleteUser.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();
                if (s.equals(Constants.SUCCESS)){
                    Toast.makeText(TransactionDetailActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    Toast.makeText(TransactionDetailActivity.this, "Failed Deletion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generatePDF(RecentTransactionModel transactionModel, String username) {
        PdfWriter writer = null;
//        Toast.makeText(this, "method called", Toast.LENGTH_SHORT).show();
        try {
            // Get the Downloads directory path
            String downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            // Create a file with a unique name in the Downloads directory
            String fileName = "bill"+transactionModel.getTransactionId()+".pdf";
            File file = new File(downloadsPath, fileName);

            // Create a PDF writer instance
            writer = new PdfWriter(new FileOutputStream(file));

            // Create a PDF document
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A6);

            // Set the font
            PdfFont font = PdfFontFactory.createFont();

            // Add the title paragraph
            Paragraph title = new Paragraph(" Company Name")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add the date paragraph
            Paragraph date = new Paragraph("Date: " + getCurrentDate())
                    .setFont(font)
                    .setFontSize(9)
                    .setMarginBottom(10)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(date);

            // Add the input values paragraphs
            Paragraph input1Paragraph = new Paragraph("Id: " + "asldkfjwerasdfer")
                    .setFont(font)
                    .setFontSize(9)
                    .setMarginTop(-23);
            Paragraph input3Paragraph = new Paragraph("By :" + username)
                    .setFont(font)
                    .setFontSize(10)
                    .setMarginTop(10)
                    ;
            Paragraph input2Paragraph = new Paragraph("To : " + transactionModel.getParty())
                    .setFont(font)
                    .setFontSize(10);
            Paragraph input4Paragraph = new Paragraph("Mode :" +transactionModel.getPaymentMode() )
                    .setFont(font)
                    .setFontSize(10);
            Paragraph input5Paragraph = new Paragraph("address : " +transactionModel.getAddress() )
                    .setFont(font)
                    .setFontSize(10);

            Paragraph paymentDetails = new Paragraph("Payment Details")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold()
                    .setMarginTop(5)
                    .setMarginBottom(5)
                    .setTextAlignment(TextAlignment.CENTER);

            float[] width={100f,100f};
            Table table =new Table(width);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            table.addCell(new Cell().add(new Paragraph("Amount")));
            table.addCell(new Cell().add(new Paragraph("₹ "+transactionModel.getAmount())));

            switch (transactionModel.getPaymentMode()) {
                case "Cheque":
                    table.addCell(new Cell().add(new Paragraph("Cheque No.").setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph(transactionModel.getChequeDataModel().getChequeNo()).setFontSize(10)));

                    table.addCell(new Cell().add(new Paragraph("Bank").setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph(transactionModel.getChequeDataModel().getBankName()).setFontSize(10)));

                    table.addCell(new Cell().add(new Paragraph("Cheque Date").setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph(transactionModel.getChequeDataModel().getDate()).setFontSize(10)));
                    break;
                case "UPI":
                    table.addCell(new Cell().add(new Paragraph("Upi Id").setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph(transactionModel.getUpiDetail().getUpiId()).setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph("Date").setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph(transactionModel.getUpiDetail().getDate()).setFontSize(10)));
                    break;
                case "Online":
                    table.addCell(new Cell().add(new Paragraph("reference Id").setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph(transactionModel.getOnlineDetail().getReferenceId()).setFontSize(10)));

                    table.addCell(new Cell().add(new Paragraph("Date").setFontSize(10)));
                    table.addCell(new Cell().add(new Paragraph(transactionModel.getOnlineDetail().getDate()).setFontSize(10)));
                    break;
            }
            // Add the input values paragraphs to the document
            document.add(input1Paragraph);
            document.add(input3Paragraph);
            document.add(input2Paragraph);
            document.add(input5Paragraph);
            document.add(input4Paragraph);
            document.add(paymentDetails);
            document.add(table);
            Toast.makeText(this, "bill created", Toast.LENGTH_SHORT).show();

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