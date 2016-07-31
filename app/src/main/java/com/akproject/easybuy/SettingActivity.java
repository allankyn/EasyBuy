package com.akproject.easybuy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.db.DbTable;
import com.akproject.easybuy.model.Transaction;
import com.akproject.easybuy.utility.ConfigManager;
import com.akproject.easybuy.utility.DateManager;
import com.akproject.easybuy.utility.FileManager;
import com.akproject.easybuy.utility.LogManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private final int PICKFILE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
    }

    // Generate Report Button-
    public void genReport(View view) {
        if (isExternalStorageWritable()) {
            LogManager.logSystem("write");
            File storageFile = FileManager.getStorageFile(String.format("report_%s.csv", DateManager.displayCurrentTimestamp()));

            if (storageFile != null) {
                StringBuffer stringBuilder = new StringBuffer();
                // Header Row
                stringBuilder.append(getResources().getString(R.string.field_transaction_date) + DbTable.EXPORT_SEP);
                stringBuilder.append(getResources().getString(R.string.field_item_name) + DbTable.EXPORT_SEP);
                stringBuilder.append(getResources().getString(R.string.field_shop_name) + DbTable.EXPORT_SEP);
                stringBuilder.append(getResources().getString(R.string.field_price) + DbTable.EXPORT_SEP);
                stringBuilder.append(getResources().getString(R.string.field_payment) + DbTable.EXPORT_SEP);
                stringBuilder.append(getResources().getString(R.string.field_has_buy) + DbTable.EXPORT_SEP);
                stringBuilder.append("\r\n");

                DbFunction dbFunction = new DbFunction();
                Transaction[] transactions = dbFunction.getTransactions(this, 1);
                for (int i=0; i<transactions.length; i++) {
                    stringBuilder.append("\"" + DateManager.displayDate(transactions[i].getDate()) + "\"" + DbTable.EXPORT_SEP);
                    stringBuilder.append("\"" + transactions[i].getItem().getItemName() + "\"" + DbTable.EXPORT_SEP);
                    stringBuilder.append("\"" + transactions[i].getShop() + "\"" + DbTable.EXPORT_SEP);
                    //stringBuilder.append("\"" + transactions[i].getBrand() + "\"" + DbTable.EXPORT_SEP);
                    //stringBuilder.append(String.format("%.2f", transactions[i].getQuantity()) + DbTable.EXPORT_SEP);
                    stringBuilder.append(String.format("$%.2f", transactions[i].getPrice()) + DbTable.EXPORT_SEP);
                    //if (transactions[i].getAttributes() != null && transactions[i].getAttributes().length > 0)
                    //    stringBuilder.append("\"" + transactions[i].getAttributes()[0].getAttributeName() + "\"" + DbTable.EXPORT_SEP);
                    //else
                    //    stringBuilder.append("\"" + "" + "\"" + DbTable.EXPORT_SEP);
                    stringBuilder.append("\"" + transactions[i].getPaymentMethod() + "\"" + DbTable.EXPORT_SEP);
                    stringBuilder.append(transactions[i].isHasBuy()?"Y":"N");
                    stringBuilder.append("\r\n");
                }

                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(storageFile));
                    bufferedWriter.write(stringBuilder.toString());
                    bufferedWriter.close();

                    Toast toast = Toast.makeText(this, storageFile.getAbsolutePath(), Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LogManager.logSystem("storageDir : " + storageFile.getAbsolutePath());
            }
        } else {
            LogManager.logSystem("can't write");
        }
    }

    // Export Button
    public void exportCsv(View view) {
        if (isExternalStorageWritable()) {
            LogManager.logSystem("write");
            File storageFile = FileManager.getStorageFile(String.format("export_%s.csv", DateManager.displayCurrentTimestamp()));

            DbFunction dbFunction = new DbFunction();
            if (storageFile != null) {
                // Export backup
                StringBuffer exportStringBuilder = new StringBuffer();
                exportStringBuilder.append(DbTable.DisplayText.TABLE_NAME);
                exportStringBuilder.append("\r\n");
                exportStringBuilder.append(dbFunction.getExport(this, null, DbTable.DisplayText.SQL_EXPORT));
                exportStringBuilder.append(DbTable.Attribute.TABLE_NAME);
                exportStringBuilder.append("\r\n");
                exportStringBuilder.append(dbFunction.getExport(this, null, DbTable.Attribute.SQL_EXPORT));
                exportStringBuilder.append(DbTable.Item.TABLE_NAME);
                exportStringBuilder.append("\r\n");
                exportStringBuilder.append(dbFunction.getExport(this, null, DbTable.Item.SQL_EXPORT));
                exportStringBuilder.append(DbTable.ItemAttrRel.TABLE_NAME);
                exportStringBuilder.append("\r\n");
                exportStringBuilder.append(dbFunction.getExport(this, null, DbTable.ItemAttrRel.SQL_EXPORT));
                exportStringBuilder.append(DbTable.Transaction.TABLE_NAME);
                exportStringBuilder.append("\r\n");
                exportStringBuilder.append(dbFunction.getExport(this, null, DbTable.Transaction.SQL_EXPORT));
                exportStringBuilder.append(DbTable.TransactionAttrRel.TABLE_NAME);
                exportStringBuilder.append("\r\n");
                exportStringBuilder.append(dbFunction.getExport(this, null, DbTable.TransactionAttrRel.SQL_EXPORT));
                LogManager.logSystem(exportStringBuilder.toString());

                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(storageFile));
                    bufferedWriter.write(exportStringBuilder.toString());
                    bufferedWriter.close();

                    Toast toast = Toast.makeText(this, storageFile.getAbsolutePath(), Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LogManager.logSystem("storageDir : " + storageFile.getAbsolutePath());
            } else {
                LogManager.logSystem("can't load file");
            }
        } else {
            LogManager.logSystem("can't write");
        }
    }

    // Import Button
    public void importCsv(View view) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.action_import)
            .setMessage(R.string.alert_confirm_restore)
            .setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (isExternalStorageReadable()) {
                        LogManager.logSystem("read");
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("file/*");
                        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PICKFILE_REQUEST_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                    LogManager.logSystem(FilePath);

                    Toast toast = Toast.makeText(this, FilePath, Toast.LENGTH_SHORT);
                    toast.show();
                    importFile(FilePath);
                }
                break;
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void importFile(String storageFile) {
        DbFunction dbFunction = new DbFunction();

        // First truncate tables
        dbFunction.trancateTables(this);

        // Initialize default content
        List<String> dbInitScript = ConfigManager.getDbInitScript();
        for (int i=0; i<dbInitScript.size(); i++) {
            dbFunction.execSQL(SettingActivity.this, dbInitScript.get(i));
        }

        // Import
        String strLine;
        try {
            int tableNum = 0;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(storageFile));
            while ((strLine = bufferedReader.readLine()) != null)   {
                if (strLine.equals(DbTable.DisplayText.TABLE_NAME)) {
                    tableNum = 1;
                }else if (strLine.equals(DbTable.Attribute.TABLE_NAME)) {
                    tableNum = 2;
                }else if (strLine.equals(DbTable.Item.TABLE_NAME)) {
                    tableNum = 3;
                }else if (strLine.equals(DbTable.ItemAttrRel.TABLE_NAME)) {
                    tableNum = 4;
                }else if (strLine.equals(DbTable.Transaction.TABLE_NAME)) {
                    tableNum = 5;
                }else if (strLine.equals(DbTable.TransactionAttrRel.TABLE_NAME)) {
                    tableNum = 6;
                }else {
                    String[] strLineParts = strLine.split(DbTable.EXPORT_SEP);
                    if (strLineParts.length > 0 && Integer.parseInt(strLineParts[0]) > 10000 || tableNum == 5 || tableNum == 6) {
                        switch (tableNum) {
                            case 1:
                                if (strLineParts.length == 5) {
                                    dbFunction.execSQL(SettingActivity.this, String.format(DbTable.DisplayText.SQL_INSERT,
                                            Integer.parseInt(strLineParts[0]),
                                            Integer.parseInt(strLineParts[1]),
                                            strLineParts[2].replace("\"", ""),
                                            Integer.parseInt(strLineParts[3]),
                                            strLineParts[4].replace("\"", "")));
                                }
                                break;
                            case 2:
                                if (strLineParts.length == 7) {
                                    dbFunction.execSQL(SettingActivity.this, String.format(DbTable.Attribute.SQL_INSERT,
                                            Integer.parseInt(strLineParts[0]),
                                            Integer.parseInt(strLineParts[1]),
                                            Integer.parseInt(strLineParts[2]),
                                            Integer.parseInt(strLineParts[3]),
                                            Integer.parseInt(strLineParts[4]),
                                            Integer.parseInt(strLineParts[5]),
                                            strLineParts[6].replace("\"", "")));
                                }
                                break;
                            case 3:
                                if (strLineParts.length == 6) {
                                    dbFunction.execSQL(SettingActivity.this, String.format(DbTable.Item.SQL_INSERT,
                                            Integer.parseInt(strLineParts[0]),
                                            Integer.parseInt(strLineParts[1]),
                                            Integer.parseInt(strLineParts[2]),
                                            Integer.parseInt(strLineParts[3]),
                                            Integer.parseInt(strLineParts[4]),
                                            strLineParts[5].replace("\"", "")));
                                }
                                break;
                            case 4:
                                if (strLineParts.length == 4) {
                                    dbFunction.execSQL(SettingActivity.this, String.format(DbTable.ItemAttrRel.SQL_INSERT,
                                            Integer.parseInt(strLineParts[0]),
                                            Integer.parseInt(strLineParts[1]),
                                            Integer.parseInt(strLineParts[2]),
                                            strLineParts[3].replace("\"", "")));
                                }
                                break;
                            case 5:
                                if (strLineParts.length == 10) {
                                    dbFunction.execSQL(SettingActivity.this, String.format(DbTable.Transaction.SQL_INSERT,
                                            Integer.parseInt(strLineParts[0]),
                                            Integer.parseInt(strLineParts[1]),
                                            strLineParts[2].replace("\"", ""),
                                            Float.parseFloat(strLineParts[3]),
                                            Float.parseFloat(strLineParts[4]),
                                            strLineParts[5].replace("\"", ""),
                                            strLineParts[6].replace("\"", ""),
                                            -1,
                                            Integer.parseInt(strLineParts[7]),
                                            Integer.parseInt(strLineParts[8]),
                                            strLineParts[9].replace("\"", "")));
                                } else if (strLineParts.length == 11) {
                                    dbFunction.execSQL(SettingActivity.this, String.format(DbTable.Transaction.SQL_INSERT,
                                            Integer.parseInt(strLineParts[0]),
                                            Integer.parseInt(strLineParts[1]),
                                            strLineParts[2].replace("\"", ""),
                                            Float.parseFloat(strLineParts[3]),
                                            Float.parseFloat(strLineParts[4]),
                                            strLineParts[5].replace("\"", ""),
                                            strLineParts[6].replace("\"", ""),
                                            Integer.parseInt(strLineParts[7]),
                                            Integer.parseInt(strLineParts[8]),
                                            Integer.parseInt(strLineParts[9]),
                                            strLineParts[10].replace("\"", "")));
                                }
                                break;
                            case 6:
                                if (strLineParts.length == 4) {
                                    dbFunction.execSQL(SettingActivity.this, String.format(DbTable.TransactionAttrRel.SQL_INSERT,
                                            Integer.parseInt(strLineParts[0]),
                                            Integer.parseInt(strLineParts[1]),
                                            Integer.parseInt(strLineParts[2]),
                                            strLineParts[3].replace("\"", "")));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
