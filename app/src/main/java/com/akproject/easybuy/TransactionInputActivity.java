package com.akproject.easybuy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.db.DbTable;
import com.akproject.easybuy.model.Attribute;
import com.akproject.easybuy.model.Item;
import com.akproject.easybuy.model.PaymentMethod;
import com.akproject.easybuy.model.SpaceTokenizer;
import com.akproject.easybuy.model.Transaction;
import com.akproject.easybuy.utility.ConfigManager;
import com.akproject.easybuy.utility.DateManager;
import com.akproject.easybuy.utility.LogManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.HashMap;

public class TransactionInputActivity extends AppCompatActivity {

    public final static String ITEM_ID = "com.akproject.easybuy.ITEM_ID";
    public final static String TRANSACTION_ID = "com.akproject.easybuy.TRANSACTION_ID";

    private DatePicker datePicker;
    private int year, month, day;

    private int itemId = -1;
    private int transactionId = -1;

    private String[] spinnerArrayRepeat;
    private HashMap<String, Integer> spinnerMapRepeat;
    private String[] spinnerArrayPayment;
    private HashMap<String, Integer> spinnerMapPayment;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_input);

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

        // Default date input
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set selected Item ID or Transaction ID
        Intent intent = getIntent();
        String itemIdStr = intent.getStringExtra(TransactionInputActivity.ITEM_ID);
        if (itemIdStr != null)
            itemId = Integer.parseInt(itemIdStr);
        else
            System.out.println("No itemId");
        String transactionIdStr = intent.getStringExtra(TransactionInputActivity.TRANSACTION_ID);
        if (transactionIdStr != null)
            transactionId = Integer.parseInt(transactionIdStr);
        else
            System.out.println("No Transaction Id");

        // Set Repeat Option disable by default
        CheckBox cbxRepeat = (CheckBox) findViewById(R.id.checkbox_repeat);
        Spinner spinnerRepeat = (Spinner) findViewById(R.id.spinner_repeatMode);
        EditText txtRepeat = (EditText) findViewById(R.id.edit_repeatTimes);
        cbxRepeat.setChecked(false);
        spinnerRepeat.setEnabled(false);
        txtRepeat.setEnabled(false);
        cbxRepeat.setVisibility(View.GONE);
        spinnerRepeat.setVisibility(View.GONE);
        txtRepeat.setVisibility(View.GONE);

        Button btnRemove = (Button) findViewById(R.id.button_remove);
        btnRemove.setVisibility(View.VISIBLE);

        DbFunction dbFunction = new DbFunction();

        //Attribute[] attributes = dbFunction.getAttributeList(this, 3, 1);
        PaymentMethod[] paymentMethods = dbFunction.getPaymentMethodList(this, 1);
        spinnerArrayPayment = new String[paymentMethods.length + 1];
        spinnerMapPayment = new HashMap<String, Integer>();
        spinnerArrayPayment[0] = getResources().getString(R.string.field_payment);
        spinnerMapPayment.put(getResources().getString(R.string.field_payment), 0);
        for (int i = 0; i < paymentMethods.length; i++) {
            spinnerArrayPayment[i + 1] = paymentMethods[i].getPaymentMethodName();
            spinnerMapPayment.put(paymentMethods[i].getPaymentMethodName(), paymentMethods[i].getPaymentMethodId());
        }
        Spinner paymentSpinner = (Spinner) findViewById(R.id.spinner_payment);
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayPayment);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(paymentAdapter);

        MultiAutoCompleteTextView shopText = (MultiAutoCompleteTextView) findViewById(R.id.edit_shopName);
        String[] str = dbFunction.getShopName(this, -1);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str);
        shopText.setTokenizer(new SpaceTokenizer());
        shopText.setThreshold(1);
        shopText.setAdapter(adp);

        try {
            if (itemId > 0) {
                Item item = dbFunction.getItem(this, itemId, 1);
                TextView txtItemId = (TextView) findViewById(R.id.txt_itemId);
                txtItemId.setText(Integer.toString(item.getItemId()));
                TextView txtItemName = (TextView) findViewById(R.id.edit_itemName);
                txtItemName.setText(item.getItemName());
                EditText txtDate = (EditText) findViewById(R.id.edit_transactionDate);
                txtDate.setText(DateManager.displayDateTime(year, month + 1, day));

                cbxRepeat.setVisibility(View.VISIBLE);
                spinnerRepeat.setVisibility(View.VISIBLE);
                txtRepeat.setVisibility(View.VISIBLE);
                setRepeatControls();

                btnRemove.setVisibility(View.GONE);
            } else if (transactionId > 0) {
                Transaction transaction = dbFunction.getTransaction(this, transactionId, 1);
                TextView itemIdText = (TextView) findViewById(R.id.txt_itemId);
                itemIdText.setText(Integer.toString(transaction.getItem().getItemId()));
                TextView itemNameText = (TextView) findViewById(R.id.edit_itemName);
                itemNameText.setText(transaction.getItem().getItemName());
                shopText.setText(transaction.getShop());
                EditText priceText = (EditText) findViewById(R.id.edit_itemPrice);
                priceText.setText(Float.toString(transaction.getPrice()));
                EditText dateText = (EditText) findViewById(R.id.edit_transactionDate);
                dateText.setText(DateManager.displayDate(transaction.getDate()));
                CheckBox hasBuyCheckBox = (CheckBox) findViewById(R.id.checkbox_hasBuy);
                hasBuyCheckBox.setChecked(transaction.isHasBuy());

                Calendar cal = Calendar.getInstance();
                cal.setTime(transaction.getDate());
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                for (int i = 0; i < spinnerArrayPayment.length; i++) {
                    int attributeId = spinnerMapPayment.get(spinnerArrayPayment[i]);
                    if (transaction.getPaymentMethodId() == attributeId)
                        paymentSpinner.setSelection(i);
                }
            }
        } catch (Exception e) {
        }

        //http://www.tutorialspoint.com/android/android_auto_complete.htm
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // Save Button
    public void saveTransaction(View view) {

        CheckBox repeatCheckBox = (CheckBox) findViewById(R.id.checkbox_repeat);
        EditText repeatText = (EditText) findViewById(R.id.edit_repeatTimes);
        int repeatTimes = 0;
        if (repeatText.length() > 0)
            repeatTimes = Integer.parseInt(repeatText.getText().toString());

        if (repeatCheckBox.isChecked() && repeatTimes > 10) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_repeat_too_many))
                    .setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            // Get inputs
            int itemId = -1;
            TextView itemIdText = (TextView) findViewById(R.id.txt_itemId);
            if (itemIdText.getText().length() > 0)
                itemId = Integer.parseInt(itemIdText.getText().toString());
            EditText itemPriceText = (EditText) findViewById(R.id.edit_itemPrice);
            float itemPrice = 0;
            try {
                itemPrice = Float.parseFloat(itemPriceText.getText().toString());
            } catch (Exception e) {
            }
            Spinner paymentSpinner = (Spinner) findViewById(R.id.spinner_payment);
            int selectedPayment = spinnerMapPayment.get(paymentSpinner.getSelectedItem());
            int[] attributeIds = null;
            if (selectedPayment > 0) {
                //attributeIds = new int[1];
                //attributeIds[0] = selectedPayment;
            }
            MultiAutoCompleteTextView shopNameText = (MultiAutoCompleteTextView) findViewById(R.id.edit_shopName);
            String shopName = shopNameText.getText().toString();
            CheckBox hasBuyCheckBox = (CheckBox) findViewById(R.id.checkbox_hasBuy);
            boolean hasBuy = hasBuyCheckBox.isChecked();
            EditText transactionDateText = (EditText) findViewById(R.id.edit_transactionDate);
            String transactionDate = transactionDateText.getText().toString();

            // Insert BuyList
            DbFunction dbFunction = new DbFunction();
            if (transactionId > 0) {
                Transaction transaction = dbFunction.getTransaction(this, transactionId, 1);
                transaction.setPrice(itemPrice);
                transaction.setShop(shopName);
                transaction.setDate(DateManager.getDateFromString(transactionDate));
                transaction.setHasBuy(hasBuy);
                transaction.setPaymentMethodId(selectedPayment);

                if (dbFunction.updateTransaction(this, transaction, attributeIds, 1))
                    LogManager.logSystem("Success in update Buy List");
                else
                    LogManager.logSystem("Error in update Buy List");
            } else {
                if (dbFunction.insertTransaction(this, itemId, itemPrice, shopName, transactionDate, hasBuy, selectedPayment, attributeIds, 1) > 0)
                    LogManager.logSystem("Success in insert Buy List");
                else
                    LogManager.logSystem("Error in insert Buy List");

                if (repeatCheckBox.isChecked()) {
                    Spinner repeatSpinner = (Spinner) findViewById(R.id.spinner_repeatMode);
                    int selectedRepeatMode = spinnerMapRepeat.get(repeatSpinner.getSelectedItem());
                    LogManager.logSystem(Integer.toString(selectedRepeatMode));
                    for (int i = 1; i < repeatTimes; i++) {
                        Calendar calendar = DateManager.getCalendarDateFromString(transactionDate);
                        if (selectedRepeatMode == 0) {
                            calendar.add(Calendar.DAY_OF_YEAR, 1 * i);
                        } else if (selectedRepeatMode == 1) {
                            calendar.add(Calendar.DAY_OF_YEAR, 7 * i);
                        } else {
                            calendar.add(Calendar.MONTH, 1 * i);
                        }
                        if (dbFunction.insertTransaction(this, itemId, itemPrice, shopName, DateManager.displayDate(calendar.getTime()), hasBuy, selectedPayment, attributeIds, 1) > 0)
                            LogManager.logSystem("Success in insert Buy List");
                        else
                            LogManager.logSystem("Error in insert Buy List");
                    }
                }
            }

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            if (transactionDate != null) {
                //editor.putInt(BuyListActivity.YEAR, DateManager.getCalendarDateFromString(transactionDate).get(Calendar.YEAR));
                //editor.putInt(BuyListActivity.MONTH, DateManager.getCalendarDateFromString(transactionDate).get(Calendar.MONTH) + 1);
            }
            editor.commit();

            Intent intent = new Intent(this, BuyListActivity.class);
            startActivity(intent);
        }
    }

    public void removeTransaction(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_confirm_delete)
                .setMessage(R.string.alert_confirm_delete)
                .setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DbFunction dbFunction = new DbFunction();
                        if (transactionId > 0) {
                            if (dbFunction.deleteTransaction(TransactionInputActivity.this, transactionId, 1))
                                LogManager.logSystem("Success in delete Buy List");
                            else
                                LogManager.logSystem("Error in delete Buy List");
                        }
                        Intent intent = new Intent(TransactionInputActivity.this, BuyListActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void triggerRepeat(View view) {
        CheckBox repeatCheckBox = (CheckBox) findViewById(R.id.checkbox_repeat);
        Spinner repeatSpinner = (Spinner) findViewById(R.id.spinner_repeatMode);
        EditText repeatText = (EditText) findViewById(R.id.edit_repeatTimes);
        if (repeatCheckBox.isChecked()) {
            repeatSpinner.setEnabled(true);
            repeatText.setEnabled(true);
        } else {
            repeatSpinner.setEnabled(false);
            repeatText.setEnabled(false);
        }
    }

    //  Date Picker
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
            return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            TextView dateView = (EditText) findViewById(R.id.edit_transactionDate);
            dateView.setText(DateManager.displayDateTime(arg1, arg2 + 1, arg3));
            setRepeatControls();
        }
    };

    private void setRepeatControls() {

        String[] weekdayStringArray = getResources().getStringArray(R.array.weekdays_array);
        Spinner repeatSpinner = (Spinner) findViewById(R.id.spinner_repeatMode);
        EditText dateText = (EditText) findViewById(R.id.edit_transactionDate);

        if (dateText.length() > 0) {
            Calendar inputtedDate = DateManager.getCalendarDateFromString(dateText.getText().toString());
            // Drop Downs
            spinnerArrayRepeat = new String[3];
            spinnerMapRepeat = new HashMap<String, Integer>();
            String repeatDaily = String.format(getResources().getString(R.string.repeat_daily));
            String repeatWeekly = String.format(getResources().getString(R.string.repeat_weekly), weekdayStringArray[inputtedDate.get(Calendar.DAY_OF_WEEK) - 1]);
            String repeatMonthly = String.format(getResources().getString(R.string.repeat_monthly), inputtedDate.get(Calendar.DAY_OF_MONTH));
            spinnerArrayRepeat[0] = repeatDaily;
            spinnerArrayRepeat[1] = repeatWeekly;
            spinnerArrayRepeat[2] = repeatMonthly;
            spinnerMapRepeat.put(repeatDaily, 0);
            spinnerMapRepeat.put(repeatWeekly, 1);
            spinnerMapRepeat.put(repeatMonthly, 2);

            ArrayAdapter<String> repeatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayRepeat);
            repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            repeatSpinner.setAdapter(repeatAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TransactionInput Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.akproject.easybuy/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TransactionInput Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.akproject.easybuy/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
