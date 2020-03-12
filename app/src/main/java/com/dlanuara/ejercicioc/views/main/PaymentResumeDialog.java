package com.dlanuara.ejercicioc.views.main;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dlanuara.ejercicioc.R;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentProcess;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadAsyncTask;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadedListener;

public class PaymentResumeDialog extends AlertDialog implements View.OnClickListener {
    private TextView txtAmount;
    private ImageView imgPaymentMethod;
    private TextView txtPaymentMethod;
    private ImageView imgIssuer;
    private TextView txtIssuer;
    private TextView txtPayerCost;
    private TextView txtPayment;

    protected PaymentResumeDialog(Context context) {
        super(context);
        init();
    }
    protected PaymentResumeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }
    protected PaymentResumeDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }
    private void init(){
        LayoutInflater myLayout = LayoutInflater.from(getContext());
        View view = myLayout.inflate(R.layout.alertdialog_paymentresume,null);
        setView(view);

        txtAmount = view.findViewById(R.id.txtAmount);
        imgPaymentMethod = view.findViewById(R.id.imgPaymentMethod);
        txtPaymentMethod = view.findViewById(R.id.txtPaymentMethod);
        imgIssuer = view.findViewById(R.id.imgIssuer);
        txtIssuer = view.findViewById(R.id.txtIssuer);
        txtPayerCost = view.findViewById(R.id.txtPayerCost);
        txtPayment = view.findViewById(R.id.txtPayment);
        view.findViewById(R.id.btnClose).setOnClickListener(this);
    }

    public void loadedPaymentProcess(@NonNull final PaymentProcess process){
        //Cargar imagen metodo de pago
        Bitmap bmp = process.getMethod().getImageThumbnail();
        txtPaymentMethod.setText(process.getMethod().getName());
        if(bmp == null){
            new ImageDownloadAsyncTask(new ImageDownloadedListener() {
                @Override
                public void onImageDownloaded(@Nullable Bitmap image) {
                    process.getMethod().setImageThumbnail(image);
                    imgPaymentMethod.setImageBitmap(image);
                }
            }).execute(process.getMethod().getThumbnail());
        }
        else
            imgPaymentMethod.setImageBitmap(process.getMethod().getImageThumbnail());
        //Cargar imagen banco
        bmp = process.getCardIssuer().getImageThumbnail();
        txtIssuer.setText(process.getCardIssuer().getName());
        if(bmp == null){
            new ImageDownloadAsyncTask(new ImageDownloadedListener() {
                @Override
                public void onImageDownloaded(@Nullable Bitmap image) {
                    process.getCardIssuer().setImageThumbnail(image);
                    imgIssuer.setImageBitmap(image);
                }
            }).execute(process.getCardIssuer().getThumbnail());
        }
        else
            imgIssuer.setImageBitmap(process.getCardIssuer().getImageThumbnail());
        //Texto
        txtAmount.setText(String.valueOf(process.getAmount()));
        txtPaymentMethod.setText(process.getMethod().getName());
        txtIssuer.setText(process.getCardIssuer().getName());
        txtPayerCost.setText(process.getPayerCost().getRecommendedMessage());
        txtPayment.setText(String.valueOf(process.getPayerCost().getInstallmentAmount()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnClose)
            dismiss();
    }
}
