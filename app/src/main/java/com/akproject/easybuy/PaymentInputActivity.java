package com.akproject.easybuy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.model.Attribute;
import com.akproject.easybuy.model.PaymentMethod;
import com.akproject.easybuy.utility.ConfigManager;
import com.akproject.easybuy.utility.LogManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class PaymentInputActivity extends AppCompatActivity {

    public final static String PAYMENT_METHOD_ID = "com.akproject.easybuy.PAYMENT_METHOD_ID";

    private int paymentMethodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_input);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Ads
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (ConfigManager.WITH_AD != 1)
            mAdView.setVisibility(View.GONE);
        mAdView.loadAd(adRequest);

        // Get parameters
        Intent intent = getIntent();
        String paymentMethodIdStr = intent.getStringExtra(PaymentInputActivity.PAYMENT_METHOD_ID);
        LogManager.logSystem(paymentMethodIdStr + " " + "#######");
        if (paymentMethodIdStr != null && paymentMethodIdStr.length() > 0)
            paymentMethodId = Integer.parseInt(paymentMethodIdStr);
        else
            paymentMethodId = -1;

        // List View Content
        Button button = (Button) findViewById(R.id.add_button);
        if (paymentMethodId > 0) {
            button.setText(getString(R.string.action_update));

            DbFunction dbFunction = new DbFunction();
            PaymentMethod paymentMethod = dbFunction.getPaymentMethod(this, paymentMethodId, 1);

            if (paymentMethod != null) {
                EditText paymentNameText = (EditText) findViewById(R.id.edit_paymentName);
                paymentNameText.setText(paymentMethod.getPaymentMethodName());
                if (paymentMethod.getCutOffDay() > 0) {
                    EditText cutOffDayText = (EditText) findViewById(R.id.edit_cutOffDay);
                    cutOffDayText.setText(Integer.toString(paymentMethod.getCutOffDay()));
                }
            }
        }
    }

    // Save Button
    public void addPaymentMethod(View view) {

        // Get inputs
        EditText paymentNameText = (EditText) findViewById(R.id.edit_paymentName);
        String paymentName = paymentNameText.getText().toString();
        EditText cutOffDayText = (EditText) findViewById(R.id.edit_cutOffDay);
        String cutOffDayStr = cutOffDayText.getText().toString();

        if (paymentName == null || paymentName.length() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_no_empty_name))
                    .setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            int cutOffDay;
            if (cutOffDayStr == null || cutOffDayStr.length() == 0)
                cutOffDay = -1;
            else
                cutOffDay = Integer.parseInt(cutOffDayStr);

            // Insert Attribute
            DbFunction dbFunction = new DbFunction();
            if (paymentMethodId > 0) {
                // Update
                PaymentMethod paymentMethod = dbFunction.getPaymentMethod(this, paymentMethodId, 1);
                paymentMethod.setPaymentMethodName(paymentName);
                paymentMethod.setCutOffDay(cutOffDay);
                dbFunction.updatePaymentMethod(this, paymentMethod, 1);
            } else {
                // Insert
                int maxTextId = dbFunction.getMaxTextId(this);
                if  (maxTextId != -1)
                    if (dbFunction.insertText(this, maxTextId + 1, 1, paymentName, 1) > 0) {
                        if (dbFunction.insertPaymentMethod(this, maxTextId + 1, cutOffDay, 1) > 0)
                            LogManager.logSystem("Success in insert Payment Method");
                        else
                            LogManager.logSystem("Error in insert Payment Method");
                    } else
                        LogManager.logSystem("Error in insert Display Text");
            }

            // Redirect Activity
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PaymentInputActivity.PAYMENT_METHOD_ID, Integer.toString(paymentMethodId));
            startActivity(intent);
        }
    }

}
