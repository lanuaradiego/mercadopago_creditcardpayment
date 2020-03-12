package com.dlanuara.ejercicioc.models.paymentmethod.installment;

import android.util.JsonReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dlanuara.ejercicioc.utilities.JsonUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class PayerCost {
    private int installments;
    private double installments_rate;
    private double discount_rate;
    private @Nullable Double reimbursement_rate;
    private @NonNull String[] labels;
    private @NonNull String[] installment_rate_collector;
    private double min_allowed_amount;
    private double max_allowed_amount;
    private @NonNull String recommended_message;
    private double installment_amount;
    private double total_amount;
    private @NonNull String payment_method_option_id;

    public PayerCost(int installments, double installments_rate, double discount_rate, @Nullable Double reimbursement_rate,
                     @NonNull String[] labels, @NonNull String[] installment_rate_collector, double min_allowed_amount,
                     double max_allowed_amount, @NonNull String recommended_message, double installment_amount,
                     double total_amount, @NonNull String payment_method_option_id){
        this.installments = installments;
        this.installments_rate = installments_rate;
        this.discount_rate = discount_rate;
        this.reimbursement_rate = reimbursement_rate;
        this.labels = labels;
        this.installment_rate_collector = installment_rate_collector;
        this.min_allowed_amount = min_allowed_amount;
        this.max_allowed_amount= max_allowed_amount;
        this.recommended_message = recommended_message;
        this.installment_amount = installment_amount;
        this.total_amount = total_amount;
        this.payment_method_option_id = payment_method_option_id;
    }

    public PayerCost(@NonNull JSONObject json) throws JSONException {
        this(
                json.getInt("installments"),
                json.getDouble("installment_rate"),
                json.getDouble("discount_rate"),
                json.isNull("reimbursement_rate") ? null : json.getDouble("reimbursement_rate"),
                JsonUtility.jsonArrayParse(json.getJSONArray("labels")),
                JsonUtility.jsonArrayParse(json.getJSONArray("installment_rate_collector")),
                json.getDouble("min_allowed_amount"),
                json.getDouble("max_allowed_amount"),
                json.getString("recommended_message"),
                json.getDouble("installment_amount"),
                json.getDouble("total_amount"),
                json.getString("payment_method_option_id")
        );
    }

    public static @NonNull PayerCost[] jsonParseArray(@NonNull final JSONArray array) throws JSONException {
        PayerCost[] payercosts = new PayerCost[array.length()];
        for(int i = 0; i < payercosts.length; ++i)
            payercosts[i] = new PayerCost(array.getJSONObject(i));
        return payercosts;
    }

    public int getInstallments() {
        return installments;
    }

    public double getInstallmentsRate() {
        return installments_rate;
    }

    public double getDiscountRate() {
        return discount_rate;
    }

    public @Nullable Double getReimbursementRate() {
        return reimbursement_rate;
    }


    public @NonNull String getLabel(int index) {
        return labels[index];
    }
    public int getLabelsCount(){
        return this.labels.length;
    }

    public @NonNull String getInstallmentRateCollector(int index) {
        return installment_rate_collector[index];
    }
    public int getInstallmentRateCollectors(){
        return this.installment_rate_collector.length;
    }

    public double getMinAllowedAmount() {
        return min_allowed_amount;
    }

    public double getMaxAllowedAmount() {
        return max_allowed_amount;
    }

    public @NonNull String getRecommendedMessage() {
        return recommended_message;
    }

    public double getInstallmentAmount() {
        return installment_amount;
    }

    public double getTotalAmount() {
        return total_amount;
    }

    public @NonNull String getPaymentMethodOptionId() {
        return payment_method_option_id;
    }
}
