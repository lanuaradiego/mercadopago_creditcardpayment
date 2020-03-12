package com.dlanuara.ejercicioc.controllers.mercadopago;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({MercadoPagoUri.PAYMENT_METHODS, MercadoPagoUri.CARD_ISSUERS, MercadoPagoUri.INSTALLMENTS})
@Retention(RetentionPolicy.SOURCE)
@interface MercadoPagoUri {
    /**
     * URI para consultar los metodos de pago
     */
    String PAYMENT_METHODS = "payment_methods";
    /**
     * URI para consultar los bancos de un metodo de pago
     */
    String CARD_ISSUERS = "payment_methods/card_issuers";
    /**
     * URI para consultar las cuotas
     */
    String INSTALLMENTS = "payment_methods/installments";
}
