package com.dlanuara.ejercicioc.models.paymentmethod.installment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dlanuara.ejercicioc.models.paymentmethod.PaymentType;
import com.dlanuara.ejercicioc.models.paymentmethod.cardissuer.Issuer;

import org.json.JSONException;
import org.json.JSONObject;

public final class Installment {
    private final static String TAG = Installment.class.getName();

    private @NonNull String payment_method_id;
    private @NonNull String payment_type_id;
    private @Nullable PaymentType payment_type;
    private @NonNull Issuer issuer;
    private @NonNull String processing_mode;
    private @Nullable String merchant_account_id;
    private @NonNull PayerCost[] payer_costs;
    private @Nullable String agreements;

    public Installment(@NonNull String payment_method_id, @NonNull String payment_type_id,
                       @NonNull Issuer issuer, @NonNull String processing_mode,
                       @Nullable String merchant_account_id, @NonNull PayerCost[] payer_costs,
                       @Nullable String agreements){
        //Asignaciones
        this.payment_method_id = payment_method_id;
        this.payment_type_id = payment_type_id;
        this.payment_type = PaymentType.getById(payment_method_id);
        this.issuer = issuer;
        this.processing_mode = processing_mode;
        this.merchant_account_id = merchant_account_id;
        this.payer_costs = payer_costs;
        this.agreements = agreements;
        //Validaciones
        if(this.payment_type == null)
            Log.e(TAG, "Unknown payment_type_id ["+ payment_type_id +"] on Installment '" + payment_method_id + "|" + issuer.getName() + "'");
    }
    public Installment(@NonNull JSONObject json) throws JSONException {
        this(
                json.getString("payment_method_id"),
                json.getString("payment_type_id"),
                new Issuer(json.getJSONObject("issuer")),
                json.getString("processing_mode"),
                json.isNull("merchant_account_id")? null : json.getString("merchant_account_id"),
                PayerCost.jsonParseArray(json.getJSONArray("payer_costs")),
                json.isNull("agreements")? null : json.getString("agreements")
        );
    }

    public @NonNull String getPaymentMethodDd() {
        return payment_method_id;
    }

    public @NonNull String getPaymentTypeId() {
        return payment_type_id;
    }

    public @Nullable PaymentType getPaymentType(){
        return this.payment_type;
    }

    public @NonNull Issuer getIssuer() {
        return issuer;
    }

    public @NonNull String getProcessingMode() {
        return processing_mode;
    }

    public @Nullable String getMerchantAccountId() {
        return merchant_account_id;
    }

    public int getPayerCostCount(){
        return this.payer_costs.length;
    }
    public PayerCost getPayerCost(int index){
        return this.payer_costs[index];
    }

    public @Nullable String getAgreements() {
        return agreements;
    }
}
