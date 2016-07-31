package com.akproject.easybuy.model;

/**
 * Created by Allan on 8/1/2016.
 */

import android.content.Context;
import android.graphics.Color;
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

public class BuyListArrayAdapter extends ArrayAdapter<Transaction> {
    private final Context context;
    private final Transaction[] values;

    public BuyListArrayAdapter(Context context, Transaction[] values) {
        super(context, R.layout.list_buy_list, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_buy_list, parent, false);

        Transaction transaction = values[position];
        TextView idTextView = (TextView) rowView.findViewById(R.id.id);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.name);
        TextView priceTextView = (TextView) rowView.findViewById(R.id.price);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
        TextView shopTextView = (TextView) rowView.findViewById(R.id.shop);
        TextView hasBuyTextView = (TextView) rowView.findViewById(R.id.has_buy);
        TextView paymentTextView = (TextView) rowView.findViewById(R.id.payment);

        idTextView.setText(Integer.toString(transaction.getId()));
        nameTextView.setText(transaction.getItem().getItemName());
        priceTextView.setText(String.format("$%.2f", transaction.getPrice()));
        dateTextView.setText(DateManager.displayDate(transaction.getDate()));
        shopTextView.setText(transaction.getShop());
        hasBuyTextView.setText(transaction.isHasBuy()? context.getResources().getString(R.string.field_has_buy):"");
        paymentTextView.setText(transaction.getPaymentMethod());
        //if (transaction.getAttributes() != null && transaction.getAttributes().length > 0)
        //    paymentTextView.setText(transaction.getAttributes()[0].getAttributeName());

        if (position % 2 == 1) {
            //rowView.setBackgroundColor(Color.BLUE);
        } else {
            // rowView.setBackgroundColor(Color.CYAN);
        }

        switch (compareToday(transaction.getDate())) {
            case 0:
                rowView.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight));
                break;
            case -1:
                if (!transaction.isHasBuy())
                    dateTextView.setTextColor(context.getResources().getColor(R.color.colorAlert));
                break;
            case -2:
                if (!transaction.isHasBuy()) {
                    nameTextView.setTextColor(context.getResources().getColor(R.color.colorDim));
                    priceTextView.setTextColor(context.getResources().getColor(R.color.colorDim));
                    dateTextView.setTextColor(context.getResources().getColor(R.color.colorDim));
                    shopTextView.setTextColor(context.getResources().getColor(R.color.colorDim));
                    paymentTextView.setTextColor(context.getResources().getColor(R.color.colorDim));
                }
                break;
            default:
                break;
        }
        return rowView;
    }

    /*
     -2 : long past
     -1 : just past
     0 : today
     1 : future
     */
    public static int compareToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);

        if (today.get(Calendar.YEAR) > calDate.get(Calendar.YEAR))
            return -2;
        else if (today.get(Calendar.YEAR) < calDate.get(Calendar.YEAR))
            return 1;
        else {
            if (today.get(Calendar.MONTH) > calDate.get(Calendar.MONTH))
                return -2;
            else if (today.get(Calendar.MONTH) < calDate.get(Calendar.MONTH))
                return 1;
            else {
                if (today.get(Calendar.DAY_OF_MONTH) > calDate.get(Calendar.DAY_OF_MONTH))
                    return -1;
                else if (today.get(Calendar.DAY_OF_MONTH) < calDate.get(Calendar.DAY_OF_MONTH))
                    return 1;
                else
                    return 0;
            }
        }
    }

}