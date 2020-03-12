package com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod.cardissuer;

import androidx.annotation.NonNull;

import com.dlanuara.ejercicioc.models.paymentmethod.cardissuer.CardIssuer;

import cz.msebera.android.httpclient.Header;

public interface CardIssuerListener {
    /**
     * Callback ejecutado al recibir correctamente los bancos disponibles
     * @param cardissuer Bancos disponibles
     */
    void onReceiveCardIssuer(@NonNull final CardIssuer[] cardissuer);

    /**
     * Callback ejecutado al no poder recibirse las bancos disponibles
     * @param statusCode Codigo HTTP que describe el error
     * @param headers Headers de respuesta
     * @param responseBody Cuerpo de la respuesta
     * @param error Excepcion ocurrida
     */
    void onReceiveCardIssuerError(final int statusCode, final Header[] headers, final byte[] responseBody, final Throwable error);
}
