package com.dlanuara.ejercicioc.models.paymentmethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public enum PaymentType {
    CREDIT_CARD("credit_card","Crédito"),
    DEBIT_CARD("debit_card","Débito"),
    TICKET("ticket","Ticket");

    private @NonNull String id;
    private @NonNull String text;

    PaymentType(@NonNull String id, @NonNull String text){
        this.id = id;
        this.text = text;
    }

    public @NonNull String getId(){
        return this.id;
    }

    @Override
    public @NonNull String toString(){
        return this.text;
    }

    public static @Nullable PaymentType getById(@NonNull String id){
        if(CREDIT_CARD.getId().equals(id))
            return CREDIT_CARD;
        if(DEBIT_CARD.getId().equals(id))
            return DEBIT_CARD;
        if(TICKET.getId().equals(id))
            return TICKET;
        return null;
    }
}
