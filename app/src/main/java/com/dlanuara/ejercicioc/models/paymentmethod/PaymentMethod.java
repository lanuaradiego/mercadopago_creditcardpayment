package com.dlanuara.ejercicioc.models.paymentmethod;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public final class PaymentMethod {
    private final static String TAG = PaymentMethod.class.getName();

    private @NonNull String id;
    private @NonNull String name;
    private @NonNull String payment_type_id;
    private @Nullable PaymentType payment_type;
    private @NonNull String status;
    private @NonNull String securethumbnail;
    private @NonNull String thumbnail;
    private @Nullable Bitmap imgThumbnail;
    private @NonNull String deferedcapture;
    private double min_allowed_amount;
    private double max_allowed_amount;

    public PaymentMethod(@NonNull String id, @NonNull String name, @NonNull String payment_type_id,
                         @NonNull String status, @NonNull String securethumbnail,
                         @NonNull String thumbnail, @NonNull String deferedcapture,
                         double min_allowed_amount, double max_allowed_amount){
        //Asignaciones
        this.id = id;
        this.name = name;
        this.payment_type_id = payment_type_id;
        this.payment_type = PaymentType.getById(payment_type_id);
        this.status = status;
        this.securethumbnail = securethumbnail;
        this.thumbnail = thumbnail;
        this.deferedcapture = deferedcapture;
        this.min_allowed_amount =  min_allowed_amount;
        this.max_allowed_amount = max_allowed_amount;
        //Validaciones
        if(this.payment_type == null)
            Log.e(TAG, "Unknown payment_type_id ["+ payment_type_id +"] on PaymentMethod '" + this.name + "' ID [" + id + "]");
    }
    public PaymentMethod(@NonNull JSONObject json) throws JSONException {
        this(
                json.getString("id"),
                json.getString("name"),
                json.getString("payment_type_id"),
                json.getString("status"),
                json.getString("secure_thumbnail"),
                json.getString("thumbnail"),
                json.getString("deferred_capture"),
                json.getDouble("min_allowed_amount"),
                json.getDouble("max_allowed_amount")
        );
    }

    public @NonNull String getId() {
        return id;
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull String getPaymentTypeId() {
        return this.payment_type_id;
    }

    public @Nullable PaymentType getPaymentType() {
        return payment_type;
    }

    public @NonNull String getStatus() {
        return status;
    }

    public @NonNull String getSecurethumbnail() {
        return securethumbnail;
    }

    public @NonNull String getThumbnail() {
        return thumbnail;
    }

    public @NonNull String getDeferedcapture() {
        return deferedcapture;
    }

    public double getMinAllowedAmount() {
        return min_allowed_amount;
    }

    public double getMaxAllowedAmount() {
        return max_allowed_amount;
    }

    public @Nullable Bitmap getImageThumbnail(){
        return this.imgThumbnail;
    }
    public void setImageThumbnail(@Nullable Bitmap image) {
        this.imgThumbnail = image;
    }

    @Override
    public String toString(){
        return this.name;

    }


}
