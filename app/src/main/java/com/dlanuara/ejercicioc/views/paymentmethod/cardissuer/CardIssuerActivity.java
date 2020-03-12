package com.dlanuara.ejercicioc.views.paymentmethod.cardissuer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dlanuara.ejercicioc.R;
import com.dlanuara.ejercicioc.controllers.mercadopago.MercadoPagoController;
import com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod.cardissuer.CardIssuerListener;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentProcess;
import com.dlanuara.ejercicioc.models.paymentmethod.cardissuer.CardIssuer;
import com.dlanuara.ejercicioc.utilities.LoadingAlertDialog;
import com.dlanuara.ejercicioc.utilities.WrapperBinder;
import com.dlanuara.ejercicioc.views.main.MainActivity;
import com.dlanuara.ejercicioc.views.paymentmethod.PaymentMethodActivity;
import com.dlanuara.ejercicioc.views.paymentmethod.PaymentMethodAdapter;
import com.dlanuara.ejercicioc.views.paymentmethod.installment.InstallmentActivity;
import com.dlanuara.ejercicioc.views.paymentmethod.processview.ProcessView;

import cz.msebera.android.httpclient.Header;

public class CardIssuerActivity extends AppCompatActivity implements CardIssuerListener, View.OnClickListener {
    private final static String TAG = CardIssuerActivity.class.getName();

    private PaymentProcess process;

    private ProcessView processView;
    private RecyclerView rvList;
    private CardIssuerAdapter adapter;
    private @Nullable LoadingAlertDialog diaWaiting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Validar proceso de pago
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            IBinder binder = bundle.getBinder(PaymentMethodActivity.BUNDLE_BINDER_PROCESS);
            if(binder instanceof  WrapperBinder){
                process = ((WrapperBinder<PaymentProcess>) binder).getData();
                if(process.getMethod() != null){
                    //Carga correcta del proceso
                    Log.d(TAG, "Received Process, Amount: " + process.getAmount() + ", PaymentMethod: " + process.getMethod().getName() + "[" + process.getMethod().getId() + "]");
                    setContentView(R.layout.activity_card_issuer);
                    processView = findViewById(R.id.processView);
                    processView.updateProcess(process);
                    rvList = findViewById(R.id.rvList);
                    rvList.setLayoutManager(new LinearLayoutManager(this));
                    rvList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    adapter = new CardIssuerAdapter();
                    rvList.setAdapter(adapter);
                    // Iniciar consultas de medios de pagos
                    diaWaiting = LoadingAlertDialog.show(this, R.string.app_waitingdata);                       // Mostrar dialog de carga
                    MercadoPagoController.getInstance().getCardIssuers(process.getMethod().getId() ,this);     // Consultar asincronicamente todos los bancos
                }
            }
        }
        else{                                                                                       // Error al obtener el proceso de pago
            Toast.makeText(this, R.string.cardissuer_error, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Wrong payment process received");
            goBackForError();
        }
    }

    @UiThread
    private void goBackForError(){
        Intent intent = new Intent(getBaseContext(), PaymentMethodActivity.class);
        startActivity(intent);
    }

    @Override
    public void onReceiveCardIssuer(@NonNull CardIssuer[] cardissuer) {
        Log.d(TAG, "Received " +  cardissuer.length + " card issuers");
        for(int i=0; i < cardissuer.length; ++i){
            CardIssuer card = cardissuer[i];
            Log.d(TAG, "Received: " + card.getName());
            adapter.addCardIssuer(card);
        }
        //Quitar Dialog
        diaWaiting.dismiss();
        diaWaiting = null;
    }

    @Override
    public void onReceiveCardIssuerError(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.e(TAG, "Error on receive: Code " + statusCode + ", Info '" + (responseBody != null ? new String(responseBody) : "null") + "', Error '" + (error != null ? error.getMessage() : "null") + "'");
        diaWaiting.dismiss();
        diaWaiting = null;
        Toast.makeText(this, R.string.cardissuer_receive_error, Toast.LENGTH_LONG).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                goBackForError();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnNext)
            onButtonNextClicked();

    }
    private void onButtonNextClicked(){
        //Validacion
        CardIssuer issuer = adapter.getSelected();
        if(issuer == null){                                                                         // Si no selecciono ninguno
            this.rvList.requestFocus();
            Toast.makeText(this, R.string.cardissuer_select_empty, Toast.LENGTH_LONG).show();
            return;
        }
        //Iniciar siguiente actividad
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, InstallmentActivity.class);
        process.setIssuer(issuer);
        bundle.putBinder(PaymentMethodActivity.BUNDLE_BINDER_PROCESS, new WrapperBinder<PaymentProcess>(process));
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }
}
