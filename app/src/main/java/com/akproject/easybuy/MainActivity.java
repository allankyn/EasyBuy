package com.akproject.easybuy;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.model.Transaction;
import com.akproject.easybuy.ods.WebServiceCall;
import com.akproject.easybuy.utility.DateManager;
import com.akproject.easybuy.utility.FileManager;
import com.akproject.easybuy.utility.LogManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.Proxy;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set default User ID before implement user login
        //FileManager.writeSetting(this, getString(R.string.app_setting_user_id), 1);
        //int value = FileManager.readSetting(this, getString(R.string.app_setting_user_id));

        // Default message
        TextView textView = (TextView) findViewById(R.id.message);
        textView.setText("Loading...");
        //textView.setText(Integer.toString(value));

        //  Merge duplicate items
        DbFunction dbFunction = new DbFunction();
        dbFunction.mergeItem(this);
        dbFunction.migratePayment(this);

        // Enable Notification
        SetNotification();

        // Grant Permission
        if (shouldAskPermission()) {
            String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        } else {
            redirect();
        }

        //WebServiceCall task = new WebServiceCall();
        //task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            // Delay for while before switch activity
            new CountDownTimer(100, 100) {
                public void onFinish() {
                    Intent intent = new Intent(MainActivity.this, BuyListActivity.class);
                    startActivity(intent);
                }

                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished    The amount of time until finished.
                }
            }.start();
            */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // save file
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
            redirect();
        }
    }

    private boolean shouldAskPermission(){
        return(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void SetNotification() {
        // Notification
        Calendar lastWeek = DateManager.getCurrentDateCalendar();
        lastWeek.add(Calendar.DAY_OF_YEAR, -7);
        Calendar tomorrow = DateManager.getCurrentDateCalendar();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        //LogManager.logSystem(lastWeek.get(Calendar.YEAR) + " " + lastWeek.get(Calendar.MONTH) + lastWeek.get(Calendar.DAY_OF_MONTH));
        //LogManager.logSystem(tomorrow.get(Calendar.YEAR) + " " + tomorrow.get(Calendar.MONTH) + tomorrow.get(Calendar.DAY_OF_MONTH));

        DbFunction dbFunction = new DbFunction();
        Transaction[] transactions = dbFunction.getTransactions(this,
                DateManager.getDate(lastWeek.get(Calendar.YEAR), lastWeek.get(Calendar.MONTH) + 1, lastWeek.get(Calendar.DAY_OF_MONTH)),
                DateManager.getDate(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH) + 1, tomorrow.get(Calendar.DAY_OF_MONTH)), -1, 1);

        boolean hasUnclear = false;
        if (transactions != null && transactions.length > 0) {
            for (int i=0; i < transactions.length; i++) {
                if (!transactions[i].isHasBuy()) {
                    hasUnclear = true;
                }
            }
        }
        if (false)
            scheduleNotification(getNotification());
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText(getResources().getString(R.string.alert_reminder));
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.buy_32);
        builder.setAutoCancel(true);
        return builder.build();
    }

    private void scheduleNotification(Notification notification) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = DateManager.getCurrentDateCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        LogManager.logSystem(
                calendar.get(Calendar.YEAR) + " " +
                        calendar.get(Calendar.MONTH) + " " +
                        calendar.get(Calendar.DAY_OF_MONTH) + " " +
                        calendar.get(Calendar.HOUR_OF_DAY) + " " +
                        calendar.get(Calendar.MINUTE) + " " +
                        calendar.get(Calendar.SECOND)
        );

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, calendar.get(Calendar.MILLISECOND), 24 * 60 * 60 * 1000, pendingIntent);
    }

    private void redirect() {
        Intent intent = new Intent(MainActivity.this, BuyListActivity.class);
        startActivity(intent);
    }

}
