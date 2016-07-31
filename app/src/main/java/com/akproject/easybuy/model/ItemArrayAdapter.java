package com.akproject.easybuy.model;

/**
 * Created by Allan on 8/1/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akproject.easybuy.AttributeInputActivity;
import com.akproject.easybuy.ItemInputActivity;
import com.akproject.easybuy.R;
import com.akproject.easybuy.TransactionInputActivity;
import com.akproject.easybuy.db.DbFunction;

public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final Item[] values;
    private int attributeId;
    private int type;   // 0: display; 1: edit

    public ItemArrayAdapter(Context context, Item[] values, int attributeId, int type) {
        super(context, R.layout.list_buy_list, values);
        this.context = context;
        this.values = values;
        this.attributeId = attributeId;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_item, parent, false);

        Item item = values[position];
        TextView idTextView = (TextView) rowView.findViewById(R.id.id);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.name);
        idTextView.setText(Integer.toString(item.getItemId()));
        nameTextView.setText(item.getItemName());

        Button button = (Button) rowView.findViewById(R.id.button);
        Button btnRename = (Button) rowView.findViewById(R.id.button_rename);

        if (type == 1) {
            if (item.getItemId() <= 10000) {
                btnRename.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            } else {
                // Edit Mode
                btnRename.setVisibility(View.VISIBLE);
                btnRename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DbFunction dbFunction = new DbFunction();
                        final TextView idTextView = (TextView) rowView.findViewById(R.id.id);
                        final Item item = dbFunction.getItem(context, Integer.parseInt(idTextView.getText().toString()), 1);

                        final EditText txtRename = new EditText(context);
                        txtRename.setText(item.getItemName());

                        new AlertDialog.Builder(context)
                                .setTitle(R.string.title_confirm_rename)
                                .setView(txtRename)
                                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        item.setItemName(txtRename.getText().toString());
                                        dbFunction.updateItem(context, item, 1);
                                        // Redirect Activity
                                        Intent intent = new Intent(context, ItemInputActivity.class);
                                        intent.putExtra(AttributeInputActivity.ATTRIBUTE_ID, Integer.toString(attributeId));
                                        context.startActivity(intent);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });

                button.setVisibility(View.VISIBLE);
                button.setText(R.string.action_remove);
                //button.setBackgroundColor(Color.WHITE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.title_confirm_delete)
                                .setMessage(R.string.alert_confirm_delete)
                                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DbFunction dbFunction = new DbFunction();
                                        TextView idTextView = (TextView) rowView.findViewById(R.id.id);
                                        Item item = dbFunction.getItem(context, Integer.parseInt(idTextView.getText().toString()), 1);
                                        item.setEnable(false);
                                        dbFunction.updateItem(context, item, 1);

                                        // Redirect Activity
                                        Intent intent = new Intent(context, ItemInputActivity.class);
                                        intent.putExtra(AttributeInputActivity.ATTRIBUTE_ID, Integer.toString(attributeId));
                                        context.startActivity(intent);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        } else {    // Display Mode
            btnRename.setVisibility(View.GONE);

            button.setVisibility(View.VISIBLE);
            button.setText(R.string.action_buy);
            //button.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView idTextView = (TextView) rowView.findViewById(R.id.id);

                    // Redirect Activity
                    Intent intent = new Intent(context, TransactionInputActivity.class);
                    intent.putExtra(TransactionInputActivity.ITEM_ID, idTextView.getText().toString());
                    context.startActivity(intent);
                }
            });
        }

        if (position % 2 == 1) {
            //rowView.setBackgroundColor(Color.BLUE);
        } else {
            // rowView.setBackgroundColor(Color.CYAN);
        }

        return rowView;
    }
}