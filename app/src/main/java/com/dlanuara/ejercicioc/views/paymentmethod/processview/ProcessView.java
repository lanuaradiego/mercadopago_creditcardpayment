package com.dlanuara.ejercicioc.views.paymentmethod.processview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dlanuara.ejercicioc.R;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentProcess;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadAsyncTask;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadedListener;

public class ProcessView extends LinearLayout {
    private @NonNull LinearLayout layAmount;
    private @NonNull TextView txtAmount;

    private @NonNull LinearLayout layPaymentMethod;
    private @NonNull ImageView imgPaymentMethod;
    private @NonNull TextView txtPaymentMethod;

    private @NonNull LinearLayout layIssuer;
    private @NonNull ImageView imgIssuer;
    private @NonNull TextView txtIssuer;

    public ProcessView(Context context) {
        super(context);
        init(context);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final @NonNull Context context){
        inflate(context, R.layout.customview_process, this);
        layAmount = findViewById(R.id.layAmount);
        txtAmount = findViewById(R.id.txtAmount);

        layPaymentMethod = findViewById(R.id.layPaymentMethod);
        imgPaymentMethod = findViewById(R.id.imgPaymentMethod);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);

        layIssuer = findViewById(R.id.layIssuer);
        imgIssuer = findViewById(R.id.imgIssuer);
        txtIssuer = findViewById(R.id.txtIssuer);
    }
    public void updateProcess(final @Nullable PaymentProcess process){
        if(process == null){
            layAmount.setVisibility(GONE);
            layIssuer.setVisibility(GONE);
            layPaymentMethod.setVisibility(GONE);
        }
        else{
            layAmount.setVisibility(VISIBLE);
            txtAmount.setText(String.valueOf(process.getAmount()));
            if(process.getMethod() != null){
                Bitmap bmp = process.getMethod().getImageThumbnail();
                layPaymentMethod.setVisibility(VISIBLE);
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
            }
            else
                layPaymentMethod.setVisibility(GONE);
            if(process.getCardIssuer() != null){
                Bitmap bmp = process.getCardIssuer().getImageThumbnail();
                layIssuer.setVisibility(VISIBLE);
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
            }
            else
                layIssuer.setVisibility(GONE);
        }
    }
}
