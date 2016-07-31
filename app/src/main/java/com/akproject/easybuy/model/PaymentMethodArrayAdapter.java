package com.akproject.easybuy.model;

/**
 * Created by Allan on 8/1/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.akproject.easybuy.R;
import com.akproject.easybuy.utility.DateManager;
import com.akproject.easybuy.utility.LogManager;

import java.util.Calendar;
import java.util.Date;

public class PaymentMethodArrayAdapter extends ArrayAdapter<PaymentMethod> {
    private final Context context;
    private final PaymentMethod[] values;

    public PaymentMethodArrayAdapter(Context context, PaymentMethod[] values) {
        super(context, R.layout.list_buy_list, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_payment_method, parent, false);

        PaymentMethod paymentMethod = values[position];
        TextView idTextView = (TextView) rowView.findViewById(R.id.id);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.name);
        TextView cutOffDayTextView = (TextView) rowView.findViewById(R.id.cut_off_day);

        idTextView.setText(Integer.toString(paymentMethod.getPaymentMethodId()));
        nameTextView.setText(paymentMethod.getPaymentMethodName());
        if (paymentMethod.getCutOffDay() > 0)
            cutOffDayTextView.setText(Integer.toString(paymentMethod.getCutOffDay()));
        else
            cutOffDayTextView.setText("-");

        if (position % 2 == 1) {
            //rowView.setBackgroundColor(Color.BLUE);
        } else {
            // rowView.setBackgroundColor(Color.CYAN);
        }

        return rowView;
    }

}