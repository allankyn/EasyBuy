package com.akproject.easybuy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.model.BuyListArrayAdapter;
import com.akproject.easybuy.model.PaymentMethod;
import com.akproject.easybuy.model.Transaction;
import com.akproject.easybuy.utility.ConfigManager;
import com.akproject.easybuy.utility.DateManager;
import com.akproject.easybuy.utility.LogManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BuyListActivity extends AppCompatActivity {

    public final static String YEAR = "SpinnerYear";
    public final static String MONTH = "SpinnerMonth";
    public final static String DAY = "SpinnerDay";
    public final static String PAYMENT = "SpinnerPayment";
    public final static String CUTOFF = "CheckCutOff";
    private final int DEFAULT_STORED_VALUE = -1;
    private int storedYear = DEFAULT_STORED_VALUE;
    private int storedMonth = DEFAULT_STORED_VALUE;
    private int storedDay = DEFAULT_STORED_VALUE;
    private int storedPayment = DEFAULT_STORED_VALUE;
    private boolean storedCutOff = false;
    private String[] spinnerArrayYear;
    private String[] spinnerArrayMonth;
    private String[] spinnerArrayDay;
    private String[] spinnerArrayPayment;
    private HashMap<String,Integer> spinnerMapYear = new HashMap<String, Integer>();
    private HashMap<String,Integer> spinnerMapMonth = new HashMap<String, Integer>();
    private HashMap<String,Integer> spinnerMapDay = new HashMap<String, Integer>();
    private HashMap<String,Integer> spinnerMapPayment = new HashMap<String, Integer>();
    private HashMap<Integer,Integer> spinnerMapPaymentCutOff = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_list);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Ads
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (ConfigManager.WITH_AD != 1)
            mAdView.setVisibility(View.GONE);
        mAdView.loadAd(adRequest);

        // Get parameters
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        storedYear = sharedPref.getInt(YEAR, DEFAULT_STORED_VALUE);
        storedMonth = sharedPref.getInt(MONTH, DEFAULT_STORED_VALUE);
        storedDay = sharedPref.getInt(DAY, DEFAULT_STORED_VALUE);
        storedPayment = sharedPref.getInt(PAYMENT, DEFAULT_STORED_VALUE);
        storedCutOff = sharedPref.getBoolean(CUTOFF, false);

        DbFunction dbFunction = new DbFunction();
        PaymentMethod[] paymentMethods = dbFunction.getPaymentMethodList(this, 1);

        // Drop Downs - set options
        LogManager.logSystem(storedYear + " " + storedMonth + " " + storedDay + " " + storedPayment);
        spinnerArrayYear = initSpinner("year", 4, R.string.common_year, R.id.spinner_year, spinnerMapYear, storedYear);
        spinnerArrayMonth = initSpinner("month", 13, R.string.common_month, R.id.spinner_month, spinnerMapMonth, storedMonth);
        spinnerArrayDay = initSpinner("day", 32, R.string.common_day, R.id.spinner_day, spinnerMapDay, storedDay);
        spinnerArrayPayment = initSpinner("payment", paymentMethods.length + 1, R.string.field_payment, R.id.spinner_payment, spinnerMapPayment, storedPayment);

        for (int i = 0; i < paymentMethods.length; i++) {
            spinnerMapPaymentCutOff.put(paymentMethods[i].getPaymentMethodId(), paymentMethods[i].getCutOffDay());
        }
        CheckBox cutOffCheckBox = (CheckBox) findViewById(R.id.checkbox_cutoff);
        if (storedCutOff != false)
            cutOffCheckBox.setChecked(true);

        // Load Data
        LoadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (id == R.id.action_input_category) {
            Intent intent = new Intent(this, AttributeInputActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_input_payment) {
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Spinner spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        Spinner spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
        Spinner spinnerDay = (Spinner) findViewById(R.id.spinner_day);
        Spinner spinnerPayment = (Spinner) findViewById(R.id.spinner_payment);
        CheckBox cutOffCheckBox = (CheckBox) findViewById(R.id.checkbox_cutoff);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (spinnerMapYear.get(spinnerYear.getSelectedItem()) != null)
            editor.putInt(BuyListActivity.YEAR, spinnerMapYear.get(spinnerYear.getSelectedItem()));
        else
            editor.putInt(BuyListActivity.YEAR, DEFAULT_STORED_VALUE);
        if (spinnerMapMonth.get(spinnerMonth.getSelectedItem()) != null)
            editor.putInt(BuyListActivity.MONTH, spinnerMapMonth.get(spinnerMonth.getSelectedItem()));
        else
            editor.putInt(BuyListActivity.MONTH, DEFAULT_STORED_VALUE);
        if (spinnerMapDay.get(spinnerDay.getSelectedItem()) != null)
            editor.putInt(BuyListActivity.DAY, spinnerMapDay.get(spinnerDay.getSelectedItem()));
        else
            editor.putInt(BuyListActivity.DAY, DEFAULT_STORED_VALUE);
        if (spinnerMapPayment.get(spinnerPayment.getSelectedItem()) != null)
            editor.putInt(BuyListActivity.PAYMENT, spinnerMapPayment.get(spinnerPayment.getSelectedItem()));
        else
            editor.putInt(BuyListActivity.PAYMENT, DEFAULT_STORED_VALUE);
        editor.putBoolean(BuyListActivity.CUTOFF, cutOffCheckBox.isChecked());

        editor.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_confirm_exit)
                    .setMessage(R.string.alert_confirm_exit)
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //***Change Here***
                            startActivity(intent);
                            finish();
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void triggerCutOff(View view) {
        LoadData();
    }

    private void LoadData() {
        Spinner yearSpinner = (Spinner) findViewById(R.id.spinner_year);
        Spinner monthSpinner = (Spinner) findViewById(R.id.spinner_month);
        Spinner daySpinner = (Spinner) findViewById(R.id.spinner_day);
        Spinner paymentSpinner = (Spinner) findViewById(R.id.spinner_payment);
        Integer selectedYear = spinnerMapYear.get(yearSpinner.getSelectedItem());
        Integer selectedMonth = spinnerMapMonth.get(monthSpinner.getSelectedItem());
        Integer selectedDay = spinnerMapDay.get(daySpinner.getSelectedItem());
        LogManager.logSystem(yearSpinner.getSelectedItem() + "#");
        LogManager.logSystem(monthSpinner.getSelectedItem() + "#");
        LogManager.logSystem(daySpinner.getSelectedItem() + "#");
        LogManager.logSystem(paymentSpinner.getSelectedItem() + "#");
        Integer selectedPayment = spinnerMapPayment.get(paymentSpinner.getSelectedItem());

        int cutOffDay = DEFAULT_STORED_VALUE;
        CheckBox cutOffCheckBox = (CheckBox) findViewById(R.id.checkbox_cutoff);
        LogManager.logSystem(paymentSpinner.getSelectedItem() + "~~" +  spinnerMapPayment.get(paymentSpinner.getSelectedItem()));
        if (paymentSpinner.getSelectedItem() != null && spinnerMapPayment.get(paymentSpinner.getSelectedItem()) != DEFAULT_STORED_VALUE && cutOffCheckBox.isChecked()) {
            cutOffDay = spinnerMapPaymentCutOff.get(spinnerMapPayment.get(paymentSpinner.getSelectedItem()));
        }

        // Set start and end date
        Date startDate = null;
        Date endDate = null;
        if (selectedYear != null) {
            int intSelectedYear = selectedYear.intValue();
            if (selectedMonth != null && selectedMonth != DEFAULT_STORED_VALUE) {
                int intSelectedMonth = selectedMonth.intValue();
                if (selectedDay != null && selectedDay != DEFAULT_STORED_VALUE) {
                    int intSelectedDay = selectedDay.intValue();
                    startDate = DateManager.getDate(intSelectedYear, intSelectedMonth, intSelectedDay);
                    endDate = DateManager.getDate(intSelectedYear, intSelectedMonth, intSelectedDay + 1);
                } else {
                    if (cutOffDay == DEFAULT_STORED_VALUE) {
                        startDate = DateManager.getDate(intSelectedYear, intSelectedMonth, 1);
                        if (intSelectedMonth == 12) {
                            endDate = DateManager.getDate(intSelectedYear + 1, 1, 1);
                        } else {
                            endDate = DateManager.getDate(intSelectedYear, intSelectedMonth + 1, 1);
                        }
                    } else {
                        Calendar calEndDate = DateManager.getCalendarDate(intSelectedYear, intSelectedMonth, cutOffDay);
                        if (intSelectedMonth != calEndDate.get(Calendar.MONTH) + 1) {
                            calEndDate = DateManager.getCalendarDate(intSelectedYear, intSelectedMonth + 1, 1);
                        } else {
                            calEndDate.add(Calendar.DATE, 1);
                        }
                        endDate = calEndDate.getTime();

                        calEndDate.add(Calendar.MONTH, -1);
                        startDate = calEndDate.getTime();
                    }
                }
            } else {
                startDate = DateManager.getDate(intSelectedYear, 1, 1);
                endDate = DateManager.getDate(intSelectedYear + 1, 1, 1);
            }
        }

        LogManager.logSystem(DateManager.displayDate(startDate) + " " + DateManager.displayDate(endDate) + " " + selectedPayment);
        // List View Content
        DbFunction dbFunction = new DbFunction();
        Transaction[] transactions = dbFunction.getTransactions(this, startDate, endDate, selectedPayment, 1);

        TextView txtGuide = (TextView) findViewById(R.id.txt_guide);
        ListView listBuyList = (ListView) findViewById(R.id.list_buyList);
        TextView txtTotalPrice = (TextView) findViewById(R.id.txt_totalPrice);
        TextView txtPaidPrice = (TextView) findViewById(R.id.txt_paidPrice);

        if (transactions != null && transactions.length > 0) {
            listBuyList.setAdapter(new BuyListArrayAdapter(this, transactions));
            listBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                    TextView idTextView = (TextView) view.findViewById(R.id.id);
                    LogManager.logSystem(String.format("Transaction ID: %s", idTextView.getText()));
                    Spinner spinnerYear = (Spinner) findViewById(R.id.spinner_year);
                    Spinner spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
                    Spinner spinnerDay = (Spinner) findViewById(R.id.spinner_day);
                    Spinner spinnerPayment = (Spinner) findViewById(R.id.spinner_payment);
                    CheckBox cutOffCheckBox = (CheckBox) findViewById(R.id.checkbox_cutoff);

                    Intent intent = new Intent(BuyListActivity.this, TransactionInputActivity.class);
                    intent.putExtra(TransactionInputActivity.TRANSACTION_ID, idTextView.getText());
                    intent.putExtra(BuyListActivity.YEAR, spinnerMapYear.get(spinnerYear.getSelectedItem()));
                    intent.putExtra(BuyListActivity.MONTH, spinnerMapMonth.get(spinnerMonth.getSelectedItem()));
                    intent.putExtra(BuyListActivity.DAY, spinnerMapDay.get(spinnerDay.getSelectedItem()));
                    intent.putExtra(BuyListActivity.PAYMENT, spinnerMapPayment.get(spinnerPayment.getSelectedItem()));
                    intent.putExtra(BuyListActivity.CUTOFF, cutOffCheckBox.isChecked());

                    BuyListActivity.this.startActivity(intent);
                }
            });

            // Total Expense
            float totalExpense = 0;
            float paidExpense = 0;
            for (int i = 0; i < transactions.length; i++) {
                totalExpense += transactions[i].getPrice();
                if (transactions[i].isHasBuy())
                    paidExpense += transactions[i].getPrice();
            }
            txtTotalPrice.setText(String.format(getResources().getString(R.string.total_expense), totalExpense));
            txtPaidPrice.setText(String.format(getResources().getString(R.string.paid_expense), paidExpense));

            txtGuide.setVisibility(View.GONE);
            listBuyList.setVisibility(View.VISIBLE);
            txtTotalPrice.setVisibility(View.VISIBLE);
            txtPaidPrice.setVisibility(View.VISIBLE);
        } else {
            txtGuide.setVisibility(View.VISIBLE);
            listBuyList.setVisibility(View.GONE);
            txtTotalPrice.setVisibility(View.GONE);
            txtPaidPrice.setVisibility(View.GONE);
        }
    }

    private String[] initSpinner(String strType, int size, int stringId, int spinnerId, HashMap<String,Integer> spinnerMap, int storedValue) {
        String[] spinnerArray = new String[size];
        int storedValueArrayIndex = -1;

        // Unselected Option
        spinnerArray[0] = getResources().getString(stringId);
        spinnerMap.put(spinnerArray[0], DEFAULT_STORED_VALUE);
        LogManager.logSystem(strType + " " + spinnerArray[0] +  " " + DEFAULT_STORED_VALUE);

        // Set Options
        DbFunction dbFunction = new DbFunction();
        PaymentMethod[] paymentMethods = dbFunction.getPaymentMethodList(this, 1);
        String[] monthStringArray = getResources().getStringArray(R.array.months_array);
        for (int i = 1; i <= size - 1; i++) {
            int optionValue = 0;
            String optionName = "";
            switch (strType) {
                case "year":
                    int year = 2015 - 1 + i;
                    optionName = Integer.toString(year);
                    optionValue = year;
                    break;
                case "month":
                    optionName = monthStringArray[i - 1];
                    optionValue = i;
                    break;
                case "day":
                    optionName = Integer.toString(i);
                    optionValue = i;
                    break;
                case "payment":
                    optionName = paymentMethods[i - 1].getPaymentMethodName();
                    optionValue = paymentMethods[i - 1].getPaymentMethodId();
                    if (storedValue == optionValue)
                        storedValueArrayIndex = i;
                    break;
            }
            spinnerArray[i] = optionName;
            spinnerMap.put(optionName, optionValue);
        }

        // Set Spinner
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner, spinnerArray);
        adapter.setDropDownViewResource(R.layout.my_spinner);
        spinner.setAdapter(adapter);

        int chosenSelectValue = DEFAULT_STORED_VALUE;
        int defaultSelectValue = DEFAULT_STORED_VALUE;
        switch (strType) {
            case "year":
                chosenSelectValue = storedValue - 2015 + 1;
                defaultSelectValue = Calendar.getInstance().get(Calendar.YEAR) - 2015 + 1;
                break;
            case "month":
                chosenSelectValue = storedValue;
                defaultSelectValue = Calendar.getInstance().get(Calendar.MONTH) + 1;
                break;
            case "day":
                chosenSelectValue = storedValue;
                defaultSelectValue = 0;
                break;
            case "payment":
                chosenSelectValue = storedValueArrayIndex;
                defaultSelectValue = 0;
                break;
        }
        LogManager.logSystem(strType + " " + chosenSelectValue + " " + defaultSelectValue + "******" );
        if (storedValue != DEFAULT_STORED_VALUE)
            spinner.setSelection(chosenSelectValue);
        else
            spinner.setSelection(defaultSelectValue);

        switch (strType) {
            case "year":
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        LoadData();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
                break;
            case "month":
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Spinner spinnerYear = (Spinner) findViewById(R.id.spinner_year);
                        Spinner spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
                        Spinner spinnerDay = (Spinner) findViewById(R.id.spinner_day);
                        Spinner paymentSpinner = (Spinner) findViewById(R.id.spinner_payment);
                        CheckBox cutOffCheckBox = (CheckBox) findViewById(R.id.checkbox_cutoff);

                        if  (spinnerMapPayment.get(paymentSpinner.getSelectedItem()) != DEFAULT_STORED_VALUE &&
                                spinnerMapPaymentCutOff.get(spinnerMapPayment.get(paymentSpinner.getSelectedItem())) != DEFAULT_STORED_VALUE &&
                                spinnerMapMonth.get(spinnerMonth.getSelectedItem()) != 0)
                            cutOffCheckBox.setEnabled(true);
                        else
                            cutOffCheckBox.setEnabled(false);

                        int dayCount = DateManager.getNumberOfDay(spinnerMapYear.get(spinnerYear.getSelectedItem()), spinnerMapMonth.get(spinnerMonth.getSelectedItem()));
                        spinnerArrayDay = new String[dayCount + 1];
                        spinnerArrayDay[0] = getResources().getString(R.string.common_day);
                        for (int i = 1; i <= dayCount; i++) {
                            int day = i;
                            spinnerArrayDay[i] = Integer.toString(day);
                        }
                        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(BuyListActivity.this, R.layout.my_spinner, spinnerArrayDay);
                        adapterDay.setDropDownViewResource(R.layout.my_spinner);
                        adapterDay.notifyDataSetChanged();
                        spinnerDay.setAdapter(adapterDay);

                        if (spinnerMapMonth.get(spinnerMonth.getSelectedItem()) == 0) {
                            spinnerDay.setSelection(0);
                            spinnerDay.setEnabled(false);
                        } else {
                            spinnerDay.setEnabled(true);
                        }

                        LoadData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
                break;
            case "day":
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        LoadData();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
                break;
            case "payment":
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Spinner spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
                        Spinner paymentSpinner = (Spinner) findViewById(R.id.spinner_payment);
                        CheckBox cutOffCheckBox = (CheckBox) findViewById(R.id.checkbox_cutoff);

                        if (spinnerMapPayment.get(paymentSpinner.getSelectedItem()) != DEFAULT_STORED_VALUE &&
                                spinnerMapPaymentCutOff.get(spinnerMapPayment.get(paymentSpinner.getSelectedItem())) != DEFAULT_STORED_VALUE &&
                                spinnerMapMonth.get(spinnerMonth.getSelectedItem()) != 0)
                            cutOffCheckBox.setEnabled(true);
                        else
                            cutOffCheckBox.setEnabled(false);

                        LoadData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
        }
        return spinnerArray;
    }

}
