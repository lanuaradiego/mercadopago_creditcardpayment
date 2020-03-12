package com.dlanuara.ejercicioc.models.paymentmethod;

import androidx.annotation.Nullable;

import com.dlanuara.ejercicioc.models.paymentmethod.cardissuer.CardIssuer;
import com.dlanuara.ejercicioc.models.paymentmethod.installment.Installment;
import com.dlanuara.ejercicioc.models.paymentmethod.installment.PayerCost;

/**
 * Clase que representa el proceso de pago con tarjeta de credito
 */
public class PaymentProcess {
    private final double amount;
    private @Nullable PaymentMethod method;
    private @Nullable CardIssuer issuer;
    private @Nullable PayerCost payercost;

    public PaymentProcess(double amount){
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public @Nullable PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(@Nullable PaymentMethod method) {
        this.method = method;
    }

    public @Nullable CardIssuer getCardIssuer() {
        return issuer;
    }

    public void setIssuer(@Nullable CardIssuer issuer) {
        this.issuer = issuer;
    }

    public @Nullable PayerCost getPayerCost() {
        return payercost;
    }

    public void setPayerCost(@Nullable PayerCost payercost) {
        this.payercost = payercost;
    }
}
