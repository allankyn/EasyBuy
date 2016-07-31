package com.akproject.easybuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.model.BuyListArrayAdapter;
import com.akproject.easybuy.model.PaymentMethod;
import com.akproject.easybuy.model.PaymentMethodArrayAdapter;
import com.akproject.easybuy.model.Transaction;
import com.akproject.easybuy.utility.ConfigManager;
import com.akproject.easybuy.utility.LogManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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

        LoadData();
    }

    private void LoadData() {
        DbFunction dbFunction = new DbFunction();
        PaymentMethod[] paymentMethods = dbFunction.getPaymentMethodList(this, 1);

        // List View Content
        if (paymentMethods != null && paymentMethods.length > 0) {
            ListView listPaymentMethod = (ListView) findViewById(R.id.list_paymentList);
            listPaymentMethod.setAdapter(new PaymentMethodArrayAdapter(this, paymentMethods));
            listPaymentMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                    TextView idTextView = (TextView) view.findViewById(R.id.id);
                    LogManager.logSystem(String.format("Payment Method ID: %s", idTextView.getText()));

                    Intent intent = new Intent(PaymentActivity.this, PaymentInputActivity.class);
                    intent.putExtra(PaymentInputActivity.PAYMENT_METHOD_ID, idTextView.getText());
                    PaymentActivity.this.startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_input_add) {
            Intent intent = new Intent(this, PaymentInputActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
