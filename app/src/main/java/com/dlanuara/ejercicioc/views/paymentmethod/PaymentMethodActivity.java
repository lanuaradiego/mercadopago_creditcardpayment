package com.dlanuara.ejercicioc.views.paymentmethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dlanuara.ejercicioc.R;
import com.dlanuara.ejercicioc.controllers.mercadopago.MercadoPagoController;
import com.dlanuara.ejercicioc.controllers.mercadopago.paymentmethod.PaymentMethodListener;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentMethod;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentProcess;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentType;
import com.dlanuara.ejercicioc.utilities.LoadingAlertDialog;
import com.dlanuara.ejercicioc.utilities.WrapperBinder;
import com.dlanuara.ejercicioc.views.main.MainActivity;
import com.dlanuara.ejercicioc.views.paymentmethod.cardissuer.CardIssuerActivity;
import com.dlanuara.ejercicioc.views.paymentmethod.processview.ProcessView;

import cz.msebera.android.httpclient.Header;

public class PaymentMethodActivity extends AppCompatActivity implements PaymentMethodListener, View.OnClickListener {
    private final static String TAG = PaymentMethodActivity.class.getName();
    public final static String BUNDLE_BINDER_PROCESS = "BUNDLE_BINDER_PROCESS";

    private ProcessView processView;

    private RecyclerView rvList;
    private PaymentMethodAdapter adapter;
    private PaymentProcess process;
    private @Nullable LoadingAlertDialog diaWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener monto
        Intent intent = getIntent();
        double amount = intent.getDoubleExtra(MainActivity.EXTRA_AMOUNT, -1d);
        if(amount <= 0){                                                                            //En caso de error
            Toast.makeText(this, R.string.paymentmethod_amount_error, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Wrong amount received: " + amount);
            goBackForError();
        }
        Log.d(TAG, "Amount received: " + amount);
        this.process = new PaymentProcess(amount);
        // Cargar visualmente
        setContentView(R.layout.activity_paymentmethod);
        processView = findViewById(R.id.processView);
        processView.updateProcess(process);
        rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new PaymentMethodAdapter();
        rvList.setAdapter(adapter);
        // Iniciar consultas de medios de pagos
        diaWaiting = LoadingAlertDialog.show(this, R.string.app_waitingdata);               // Mostrar dialog de carga
        MercadoPagoController.getInstance().getAllPaymentMethods(this);                     // Consultar asincronicamente todos los metodos de pago
    }

    @UiThread
    private void goBackForError(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onReceivePaymentMethods(@NonNull PaymentMethod[] methods) {
        Log.d(TAG, "Received " +  methods.length + " payment methods");
        for(int i=0; i < methods.length; ++i){
            PaymentMethod method = methods[i];
            if(method.getPaymentType() == PaymentType.CREDIT_CARD && "active".equals(method.getStatus())){  // Filtrar solo los metodos de pago con tarjetas de credito activos
                Log.d(TAG, "Received: " + method.getName() + ", " + method.getPaymentType().toString());
                adapter.addPaymentMethod(method);
            }
        }
        //Quitar Dialog
        diaWaiting.dismiss();
        diaWaiting = null;
    }

    @Override
    public void onReceivePaymentMethodsError(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.e(TAG, "Error on receive: Code " + statusCode + ", Info '" + (responseBody != null ? new String(responseBody) : "null") + "', Error '" + (error != null ? error.getMessage() : "null") + "'");
        diaWaiting.dismiss();
        diaWaiting = null;
        Toast.makeText(this, R.string.paymentmethod_receive_error, Toast.LENGTH_LONG).show();
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
        PaymentMethod method = adapter.getSelected();
        if(method == null){                                                                         // Si no selecciono ningun metodo
            this.rvList.requestFocus();
            Toast.makeText(this, R.string.paymentmethod_select_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if(process.getAmount() < method.getMinAllowedAmount() || process.getAmount() > method.getMaxAllowedAmount()){
            this.rvList.requestFocus();
            Toast.makeText(this, getString(R.string.paymentmethod_select_error, method.getMinAllowedAmount(), method.getMaxAllowedAmount()), Toast.LENGTH_LONG).show();
            return;
        }
        //Iniciar siguiente actividad
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, CardIssuerActivity.class);
        process.setMethod(method);
        bundle.putBinder(BUNDLE_BINDER_PROCESS, new WrapperBinder<PaymentProcess>(process));
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }
}
