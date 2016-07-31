package com.akproject.easybuy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.model.Attribute;
import com.akproject.easybuy.model.Item;
import com.akproject.easybuy.model.ItemArrayAdapter;
import com.akproject.easybuy.utility.ConfigManager;
import com.akproject.easybuy.utility.LogManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ItemInputActivity extends AppCompatActivity {

    private int attributeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_input);

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

        // Set selected Attribute ID
        Intent intent = getIntent();
        String attributeIdStr = intent.getStringExtra(AttributeInputActivity.ATTRIBUTE_ID);
        attributeId = Integer.parseInt(attributeIdStr);

        DbFunction dbFunction = new DbFunction();
        Attribute attribute =  dbFunction.getAttribute(this, attributeId, 1);

        // Attribute Name
        EditText attributeNameText = (EditText) findViewById(R.id.edit_attributeName);
        attributeNameText.setText(attribute.getAttributeName());

        if (attribute.getAttributeId() <= 10000) {
            Button btnRename = (Button) findViewById(R.id.rename_button);
            Button btnRemove = (Button) findViewById(R.id.remove_button);

            attributeNameText.setEnabled(false);
            btnRename.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        }

        // List View Content
        Item[] items = dbFunction.getItemList(this, attributeId, 1);
        ListView listViewItem = (ListView) findViewById(R.id.list_view_item);
        listViewItem.setAdapter(new ItemArrayAdapter(this, items, attributeId, 1));
    }

    public void removeAttribute(View view) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.title_confirm_delete)
            .setMessage(R.string.alert_confirm_delete)
            .setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                    DbFunction dbFunction = new DbFunction();
                    Attribute attribute = dbFunction.getAttribute(ItemInputActivity.this, attributeId, 1);
                    attribute.setEnable(false);
                    dbFunction.updateAttribute(ItemInputActivity.this, attribute, 1);

                    // Redirect Activity
                    Intent intent = new Intent(ItemInputActivity.this, AttributeInputActivity.class);
                    intent.putExtra(AttributeInputActivity.ATTRIBUTE_ID, attributeId);
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

    // Save Button
    public void renameAttribute(View view) {
        DbFunction dbFunction = new DbFunction();
        Attribute attribute = dbFunction.getAttribute(this, attributeId, 1);
        EditText attributeNameText = (EditText) findViewById(R.id.edit_attributeName);
        attribute.setAttributeName(attributeNameText.getText().toString());
        dbFunction.updateAttribute(ItemInputActivity.this, attribute, 1);

        // Redirect Activity
        Intent intent = new Intent(this, ItemInputActivity.class);
        intent.putExtra(AttributeInputActivity.ATTRIBUTE_ID, Integer.toString(attributeId));
        startActivity(intent);
    }

    // Save Button
    public void addItem(View view) {

        // Get inputs
        EditText itemNameText = (EditText) findViewById(R.id.edit_itemName);
        String itemName = itemNameText.getText().toString();

        if (itemName == null || itemName.length() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_no_empty_name))
                    .setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            // Insert Attribute
            DbFunction dbFunction = new DbFunction();
            int[] attributeIds = new int[1];
            attributeIds[0] = attributeId;
            int maxTextId = dbFunction.getMaxTextId(this);
            if  (maxTextId != -1)
                if (dbFunction.insertText(this, maxTextId + 1, 1, itemName, 1) > 0)
                    if (dbFunction.insertItem(this, maxTextId + 1, attributeIds, 1) > 0)
                        LogManager.logSystem("Success in insert Item");
                    else
                        LogManager.logSystem("Error in insert Item");
                else
                    LogManager.logSystem("Error in insert Display Text");

            // Redirect Activity
            Intent intent = new Intent(this, ItemInputActivity.class);
            intent.putExtra(AttributeInputActivity.ATTRIBUTE_ID, Integer.toString(attributeId));
            startActivity(intent);
        }
    }

}
