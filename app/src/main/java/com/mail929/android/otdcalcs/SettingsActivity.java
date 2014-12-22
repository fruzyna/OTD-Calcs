package com.mail929.android.otdcalcs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by mail929 on 12/21/14.
 */
public class SettingsActivity extends ActionBarActivity
{
    EditText newlt;
    EditText transfer;
    EditText out;
    EditText dupage;
    EditText cook;
    EditText chicago;
    EditText doc;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        newlt = (EditText) findViewById(R.id.newlt);
        transfer = (EditText) findViewById(R.id.transfer);
        out = (EditText) findViewById(R.id.out);
        dupage = (EditText) findViewById(R.id.dupage);
        cook = (EditText) findViewById(R.id.cook);
        chicago = (EditText) findViewById(R.id.chicago);
        doc = (EditText) findViewById(R.id.doc);

        sharedPref = getSharedPreferences("otd", Context.MODE_PRIVATE);

        newlt.setText(sharedPref.getString("newlt", "196"));
        transfer.setText(sharedPref.getString("transfer", "122"));
        out.setText(sharedPref.getString("out", "10"));
        dupage.setText(sharedPref.getString("dupage", "7.25"));
        cook.setText(sharedPref.getString("cook", "8.25"));
        chicago.setText(sharedPref.getString("chicago", "9.50"));
        doc.setText(sharedPref.getString("doc", "191"));
    }

    @Override
    public void onBackPressed()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("newlt", newlt.getText().toString());
        editor.putString("transfer", transfer.getText().toString());
        editor.putString("out", out.getText().toString());
        editor.putString("dupage", dupage.getText().toString());
        editor.putString("cook", cook.getText().toString());
        editor.putString("chicago", chicago.getText().toString());
        editor.putString("doc", doc.getText().toString());
        editor.commit();
        super.onBackPressed();
        OTDActivity.sharedPref = sharedPref;
    }
}
