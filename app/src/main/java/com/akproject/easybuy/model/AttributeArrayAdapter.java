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

public class AttributeArrayAdapter extends ArrayAdapter<Attribute> {
    private final Context context;
    private final Attribute[] values;

    public AttributeArrayAdapter(Context context, Attribute[] values) {
        super(context, R.layout.list_buy_list, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_attribute, parent, false);

        Attribute attribute = values[position];
        TextView idTextView = (TextView) rowView.findViewById(R.id.id);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.name);
        idTextView.setText(Integer.toString(attribute.getAttributeId()));
        nameTextView.setText(attribute.getAttributeName());

        if (position % 2 == 1) {
            //rowView.setBackgroundColor(Color.BLUE);
        } else {
            // rowView.setBackgroundColor(Color.CYAN);
        }

        return rowView;
    }
}