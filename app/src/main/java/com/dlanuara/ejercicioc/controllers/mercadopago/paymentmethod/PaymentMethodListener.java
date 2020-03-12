package com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod;

import androidx.annotation.NonNull;

import com.dlanuara.ejercicioc.models.paymentmethod.PaymentMethod;

import cz.msebera.android.httpclient.Header;

public interface PaymentMethodListener {
    /**
     * Callback ejecutado al recibir correctamente los metodos de pagos
     * @param methods Metodos de pagos disponibles
     */
    void onReceivePaymentMethods(@NonNull final PaymentMethod[] methods);

    /**
     * Callback ejecutado al no poder recibirse los metodos de pago
     * @param statusCode Codigo HTTP que describe el error
     * @param headers Headers de respuesta
     * @param responseBody Cuerpo de la respuesta
     * @param error Excepcion ocurrida
     */
    void onReceivePaymentMethodsError(final int statusCode, final Header[] headers, final byte[] responseBody, final Throwable error);
}
