package com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod.installment;

import androidx.annotation.NonNull;

import com.dlanuara.ejercicioc.models.paymentmethod.installment.Installment;

import cz.msebera.android.httpclient.Header;

public interface InstallmentListener {
    /**
     * Callback ejecutado al recibir correctamente las cuotas disponibles
     * @param installments Cuotas disponibles
     */
    void onReceiveInstallment(@NonNull final Installment[] installments);

    /**
     * Callback ejecutado al no poder recibirse las cuotas disponibles
     * @param statusCode Codigo HTTP que describe el error
     * @param headers Headers de respuesta
     * @param responseBody Cuerpo de la respuesta
     * @param error Excepcion ocurrida
     */
    void onReceiveInstallmentError(final int statusCode, final Header[] headers, final byte[] responseBody, final Throwable error);
}
