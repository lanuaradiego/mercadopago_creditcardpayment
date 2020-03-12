package com.dlanuara.ejercicioc.controllers.mercadopago;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod.PaymentMethodListener;
import com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod.cardissuer.CardIssuerListener;
import com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod.installment.InstallmentListener;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentMethod;
import com.dlanuara.ejercicioc.models.paymentmethod.cardissuer.CardIssuer;
import com.dlanuara.ejercicioc.models.paymentmethod.installment.Installment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public final class MercadoPagoController {
    private final static String TAG = MercadoPagoController.class.getName();

    private final static String BASE_URL = "https://api.mercadopago.com/v1/";
    private final static String PUBLIC_KEY = "444a9ef5-8a6b-429f-abdf-587639155d88";
    private final static int TIMEOUT = 10000;

    private static @Nullable MercadoPagoController instance;
    public synchronized static @NonNull MercadoPagoController getInstance(){
        if(instance == null)
            instance = new MercadoPagoController();
        return instance;
    }

    private @NonNull AsyncHttpClient client;

    private MercadoPagoController(){
        client = new AsyncHttpClient();
        client.setTimeout(TIMEOUT);
        client.setConnectTimeout(TIMEOUT);
        client.setResponseTimeout(TIMEOUT);
        client.setMaxRetriesAndTimeout(3,TIMEOUT);
    }

    /**
     * Metodo pedido por el ejercicio: "Modularizar la llamada para recibir base_url, uri y query_params".
     * Inicia una peticion GET a la URL definida
     * @param base_url URL a la cual consultar
     * @param uri URI de la consulta a realizar
     * @param query_params Parametros a consultar (sin incluir la public_key)
     * @param responseHandler Eventos asincronicos que se ejecutaran al obtener o no una respuesta del API REST
     */
    private void get(final @NonNull String base_url, final @NonNull @MercadoPagoUri String uri, final @NonNull RequestParams query_params, final @NonNull AsyncHttpResponseHandler responseHandler) {
        String url = base_url + uri;
        query_params.put("public_key", PUBLIC_KEY);                                                 // Agregar siempre la public_key
        client.get(url, query_params, responseHandler);
    }

    public void getAllPaymentMethods(@NonNull final PaymentMethodListener callback){
        get(BASE_URL, MercadoPagoUri.PAYMENT_METHODS, new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                if ("null".equals(str))                                                             //Validacion
                    callback.onReceivePaymentMethodsError(statusCode, headers, responseBody, null);
                else {
                    //Parseo
                    try {
                        JSONArray obj = new JSONArray(str);
                        PaymentMethod[] methods = new PaymentMethod[obj.length()];
                        for (int i = 0; i < methods.length; ++i)
                            methods[i] = new PaymentMethod(obj.getJSONObject(i));
                        callback.onReceivePaymentMethods(methods);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error on parsing all payment methods", e);
                        callback.onReceivePaymentMethodsError(statusCode, headers, responseBody, null);
                    } catch (Exception e) {
                        Log.e(TAG, "Error on receive and parsing all payment methods", e);
                        callback.onReceivePaymentMethodsError(statusCode, headers, responseBody, null);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onReceivePaymentMethodsError(statusCode,headers,responseBody,error);
            }
        });
    }

    public void getCardIssuers(@NonNull final String payment_method_id, @NonNull final CardIssuerListener callback){
        //Parmetros
        RequestParams query_params = new RequestParams();
        query_params.put("payment_method_id", payment_method_id);
        //Iniciar consulta
        get(BASE_URL, MercadoPagoUri.CARD_ISSUERS, query_params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                if ("null".equals(str))                                                             //Validacion
                    callback.onReceiveCardIssuerError(statusCode, headers, responseBody, null);
                else {
                    //Parseo
                    try {
                        JSONArray obj = new JSONArray(str);
                        CardIssuer[] issuers = new CardIssuer[obj.length()];
                        for (int i = 0; i < issuers.length; ++i)
                            issuers[i] = new CardIssuer(obj.getJSONObject(i));
                        callback.onReceiveCardIssuer(issuers);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error on parsing all card issuers", e);
                        callback.onReceiveCardIssuerError(statusCode, headers, responseBody, null);
                    } catch (Exception e) {
                        Log.e(TAG, "Error on receive and parsing all card issuers", e);
                        callback.onReceiveCardIssuerError(statusCode, headers, responseBody, null);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onReceiveCardIssuerError(statusCode,headers,responseBody,error);
            }
        });
    }

    public void getInstallments(@NonNull final double amount, @NonNull final String payment_method_id, @NonNull final String issuer_id,@NonNull final InstallmentListener callback){
        //Parmetros
        RequestParams query_params = new RequestParams();
        query_params.put("amount", amount);
        query_params.put("payment_method_id", payment_method_id);
        query_params.put("issuer.id", issuer_id);
        //Iniciar consulta
        get(BASE_URL, MercadoPagoUri.INSTALLMENTS, query_params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                if ("null".equals(str))                                                             //Validacion
                    callback.onReceiveInstallmentError(statusCode, headers, responseBody, null);
                else {
                    //Parseo
                    try {
                        JSONArray obj = new JSONArray(str);
                        Installment[] installment = new Installment[obj.length()];
                        for (int i = 0; i < installment.length; ++i)
                            installment[i] = new Installment(obj.getJSONObject(i));
                        callback.onReceiveInstallment(installment);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error on parsing all installments", e);
                        callback.onReceiveInstallmentError(statusCode, headers, responseBody, null);
                    } catch (Exception e) {
                        Log.e(TAG, "Error on receive and parsing all installments", e);
                        callback.onReceiveInstallmentError(statusCode, headers, responseBody, null);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onReceiveInstallmentError(statusCode,headers,responseBody,error);
            }
        });
    }
}
