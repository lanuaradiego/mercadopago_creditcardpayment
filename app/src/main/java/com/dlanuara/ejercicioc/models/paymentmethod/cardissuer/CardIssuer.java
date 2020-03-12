package com.dlanuara.ejercicioc.models.paymentmethod.cardissuer;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public final class CardIssuer extends Issuer {
    private @NonNull String processing_mode;
    private @Nullable String merchant_account_id;
    private @Nullable Bitmap imgThumbnail;

    public CardIssuer(@NonNull JSONObject json) throws JSONException {
        super(json);
        this.processing_mode = json.getString("processing_mode");
        this.merchant_account_id = json.isNull("merchant_account_id") ? null : json.getString("merchant_account_id");
    }

    public @NonNull String getProcessingMode() {
        return processing_mode;
    }

    public @Nullable String getMerchantAccountId() {
        return merchant_account_id;
    }

    public @Nullable Bitmap getImageThumbnail(){
        return imgThumbnail;
    }
    public void setImageThumbnail(@Nullable Bitmap bmp){
        this.imgThumbnail = bmp;
    }
}
