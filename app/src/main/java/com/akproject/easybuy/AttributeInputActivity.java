package com.akproject.easybuy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.akproject.easybuy.db.DbFunction;
import com.akproject.easybuy.model.Attribute;
import com.akproject.easybuy.model.AttributeArrayAdapter;
import com.akproject.easybuy.model.Item;
import com.akproject.easybuy.model.ItemArrayAdapter;
import com.akproject.easybuy.utility.ConfigManager;
import com.akproject.easybuy.utility.LogManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AttributeInputActivity extends AppCompatActivity {

    public final static String ATTRIBUTE_ID = "com.akproject.easybuy.ATTRIBUTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute_input);

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

        // Get parameters
        Intent intent = getIntent();
        String yearIdStr = intent.getStringExtra(BuyListActivity.YEAR);
        String monthIdStr = intent.getStringExtra(BuyListActivity.MONTH);
        LogManager.logSystem(yearIdStr + " " + monthIdStr + "#######");

        // List View Content
        DbFunction dbFunction = new DbFunction();
        int[] attributeTypes = {1,2};
        Attribute[] attributeList = dbFunction.getAttributeList(this, attributeTypes, 1);
        ListView listViewAttribtue = (ListView) findViewById(R.id.list_view_attribute);
        listViewAttribtue.setAdapter(new AttributeArrayAdapter(this, attributeList));
        listViewAttribtue.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                // Change selected item color
                for (int i = 0; i < parent.getCount(); i++) {
                    View v = (View) parent.getChildAt(i);
                    if (v != null)
                        v.setBackgroundColor(Color.WHITE);
                }
                view.setBackgroundColor(getResources().getColor(R.color.colorHighlight));

                // Get and Set attribute ID
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView attributeIdTextView = (TextView) findViewById(R.id.selected_attribute_id);
                attributeIdTextView.setText(idTextView.getText().toString());
                int attributeId = Integer.parseInt(idTextView.getText().toString());

                // Load item list and visible
                DbFunction dbFunction = new DbFunction();
                ListView listViewItem = (ListView) findViewById(R.id.list_view_item);
                Item[] items = dbFunction.getItemList(AttributeInputActivity.this, attributeId, 1);
                listViewItem.setAdapter(new ItemArrayAdapter(AttributeInputActivity.this, items, attributeId, 0));

                LinearLayout itemListLayout = (LinearLayout) findViewById(R.id.item_list_layout);
                itemListLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    // Save Button
    public void addAttribute(View view) {

        // Get inputs
        EditText attributeNameText = (EditText) findViewById(R.id.edit_attributeName);
        String attributeName = attributeNameText.getText().toString();

        if (attributeName == null || attributeName.length() == 0) {
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
            int maxTextId = dbFunction.getMaxTextId(this);
            if (maxTextId != -1)
                if (dbFunction.insertText(this, maxTextId + 1, 1, attributeName, 1) > 0)
                    if (dbFunction.insertAttribute(this, 1, maxTextId + 1, 1) > 0)
                        LogManager.logSystem("Success in insert Attribute");
                    else
                        LogManager.logSystem("Error in insert Attribute");
                else
                    LogManager.logSystem("Error in insert Display Text");

            // Redirect Activity


            Intent intent2 = new Intent(this, AttributeInputActivity.class);
            startActivity(intent2);
        }
    }

    // Redirect Button
    public void editAttribute(View view) {
        Intent intent = new Intent(this, ItemInputActivity.class);
        TextView attributeIdTextView = (TextView) findViewById(R.id.selected_attribute_id);
        intent.putExtra(ATTRIBUTE_ID, attributeIdTextView.getText().toString());
        startActivity(intent);
    }

}
