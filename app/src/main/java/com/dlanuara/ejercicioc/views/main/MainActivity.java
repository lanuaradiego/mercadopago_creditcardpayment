package com.dlanuara.ejercicioc.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dlanuara.ejercicioc.R;
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentProcess;
import com.dlanuara.ejercicioc.utilities.DecimalDigitsInputFilter;
import com.dlanuara.ejercicioc.utilities.WrapperBinder;
import com.dlanuara.ejercicioc.views.paymentmethod.PaymentMethodActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String TAG = MainActivity.class.getName();
    public final static String ACTION_SHOWRESULT = "ACTION_SHOWRESULT";
    public final static String EXTRA_AMOUNT = "EXTRA_AMOUNT";

    public final static int REQUESTCODE_PAYMENT_PROCESS = 1303;

    private final static int AMOUNT_DECIMAL_COUNT = 2;
    private final static int AMOUNT_INTEGER_COUNT = 6;

    private EditText txtAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAmount = findViewById(R.id.txtAmount);
        txtAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(AMOUNT_INTEGER_COUNT, AMOUNT_DECIMAL_COUNT)});
    }

//    @Override
//    protected void onStart(){
//        super.onStart();
//        //Evaluar caso en el que se realizo el proceso de pago
//        Intent intent = getIntent();
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUESTCODE_PAYMENT_PROCESS && resultCode == RESULT_OK){
            //Validar proceso de pago
            Bundle bundle = data.getExtras();
            if(bundle != null) {
                IBinder binder = bundle.getBinder(PaymentMethodActivity.BUNDLE_BINDER_PROCESS);
                if(binder instanceof WrapperBinder){
                    PaymentProcess process = ((WrapperBinder<PaymentProcess>) binder).getData();
                    if(process.getMethod() != null && process.getCardIssuer() != null && process.getPayerCost() != null){
                        //Motrar proceso de pago
                        Log.d(TAG, "Received Process, Amount: " + process.getAmount() + ", PaymentMethod: " + process.getMethod().getName() + "[" + process.getMethod().getId() + "], Issuer: " + process.getCardIssuer().getName() + " [" + process.getCardIssuer().getId()  +"], PayerCost: " + process.getPayerCost().getRecommendedMessage());
                        PaymentResumeDialog dialog = new PaymentResumeDialog(this,false, null);
                        dialog.loadedPaymentProcess(process);
                        dialog.show();
                        txtAmount.setText("");
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnNext)
            onButtonNextClicked();
    }
    private void onButtonNextClicked(){
        if(validateAmount()){
            //Iniciar nueva actividad
            Intent intent = new Intent(getBaseContext(), PaymentMethodActivity.class);
            intent.putExtra(EXTRA_AMOUNT, Double.parseDouble(txtAmount.getText().toString()));
            try {
                startActivityForResult(intent, REQUESTCODE_PAYMENT_PROCESS);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private boolean validateAmount(){
        String val = txtAmount.getText().toString();
        double amount;
        double maxamount = Math.pow(10, AMOUNT_INTEGER_COUNT);                                      // Validacion segun la cantidad de numeros enteros utilizados
        if(val.isEmpty() || ".".equals(val) || ",".equals(val)){
            txtAmount.requestFocus();
            Toast.makeText(this, R.string.main_amount_empty, Toast.LENGTH_LONG).show();
            return false;
        }
        amount = Double.parseDouble(val);
        if(Double.isInfinite(amount) || Double.isNaN(amount) || amount <= 0 || amount >= maxamount){
            txtAmount.requestFocus();
            Toast.makeText(this, R.string.main_amount_error, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
