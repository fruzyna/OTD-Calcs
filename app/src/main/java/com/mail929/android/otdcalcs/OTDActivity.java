package com.mail929.android.otdcalcs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class OTDActivity extends ActionBarActivity
{
    LinearLayout main;

    Context context;

    static SharedPreferences sharedPref;

    double sub;
    double subtotal;
    double end;

    EditText pricev;
    EditText docv;
    EditText tradev;
    EditText percentv;
    TextView subv;
    TextView subtotalv;
    TextView taxv;
    TextView otdv;
    RadioGroup ltv;
    RadioGroup taxesv;
    TextView pricevp;
    EditText otdvp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otd);

        context = this;

        main = (LinearLayout) findViewById(R.id.content);

        sharedPref = getSharedPreferences("otd", Context.MODE_PRIVATE);

        final CheckBox cb = (CheckBox) findViewById(R.id.otdcb);
        if (cb.isChecked())
        {
            findOTD();
        } else
        {
            findOTD();
        }
        cb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                main.removeAllViews();
                if (cb.isChecked())
                {
                    findPrice();
                } else
                {
                    findOTD();
                }
            }
        });
    }

    public void findOTD()
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.calc_otd, main);
        setup(v);

        pricev = (EditText) v.findViewById(R.id.price);
        docv = (EditText) v.findViewById(R.id.doc);
        tradev = (EditText) v.findViewById(R.id.trade);
        percentv = (EditText) v.findViewById(R.id.percent);
        subv = (TextView) v.findViewById(R.id.subtotal);
        taxv = (TextView) v.findViewById(R.id.tax);
        otdv = (TextView) v.findViewById(R.id.otd);
        ltv = (RadioGroup) v.findViewById(R.id.lt);
        taxesv = (RadioGroup) v.findViewById(R.id.taxes);

        pricev.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateSubOTD();
                updateEndOTD();
                updateOTD();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });
        docv.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateSubOTD();
                updateEndOTD();
                updateOTD();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });
        tradev.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateSubOTD();
                updateEndOTD();
                updateOTD();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });
        percentv.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateSubOTD();
                updateEndOTD();
                updateOTD();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });


        taxesv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup r, int id)
            {
                updateEndOTD();
                updateOTD();
            }
        });
        ltv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup r, int id)
            {
                updateEndOTD();
                updateOTD();
            }
        });
    }

    public void updateOTD()
    {
        if (updateSubOTD())
        {
            double otd = sub + end;
            otdv.setText(String.format("%.2f", otd));
        }
    }

    public void updateEndOTD()
    {
        double tax;
        double lt = 0;

        if (updateSubOTD())
        {
            double taxpercent = 0;
            switch (taxesv.getCheckedRadioButtonId())
            {
                case R.id.dupage:
                    taxpercent = Double.parseDouble(sharedPref.getString("dupage", "7.25"));
                    break;
                case R.id.chicago:
                    taxpercent = Double.parseDouble(sharedPref.getString("chicago", "9.50"));
                    break;
                case R.id.cook:
                    taxpercent = Double.parseDouble(sharedPref.getString("cook", "8.25"));
                    break;
                case R.id.other:
                    if (!percentv.getText().toString().equals(""))
                    {
                        taxpercent = Double.parseDouble(percentv.getText().toString());
                    }
                    break;
            }
            tax = sub * (taxpercent / 100);
            taxv.setText(String.format("%.2f", tax));
            switch (ltv.getCheckedRadioButtonId())
            {
                case R.id.newlt:
                    lt = Double.parseDouble(sharedPref.getString("newlt", "196"));
                    break;
                case R.id.transfer:
                    lt = Double.parseDouble(sharedPref.getString("transfer", "122"));
                    break;
                case R.id.out:
                    lt = Double.parseDouble(sharedPref.getString("out", "10"));
                    break;
            }

            end = tax + lt;
        }

    }

    public boolean updateSubOTD()
    {
        if (!pricev.getText().toString().equals("") && !docv.getText().toString().equals("") && !tradev.getText().toString().equals(""))
        {
            double price = Double.parseDouble(pricev.getText().toString());
            double doc = Double.parseDouble(docv.getText().toString());
            double trade = Double.parseDouble(tradev.getText().toString());
            sub = price + doc - trade;
            subv.setText(String.format("%.2f", sub));
            return true;
        }
        return false;
    }


    public void findPrice()
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.calc_price, main);
        setup(v);

        otdvp = (EditText) v.findViewById(R.id.otd);
        docv = (EditText) v.findViewById(R.id.doc);
        tradev = (EditText) v.findViewById(R.id.trade);
        percentv = (EditText) v.findViewById(R.id.percent);
        subtotalv = (TextView) v.findViewById(R.id.subtotal);
        subv = (TextView) v.findViewById(R.id.sub);
        taxv = (TextView) v.findViewById(R.id.tax);
        pricevp = (TextView) v.findViewById(R.id.price);
        ltv = (RadioGroup) v.findViewById(R.id.lt);
        taxesv = (RadioGroup) v.findViewById(R.id.taxes);

        otdvp.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateFirstSub();
                updateSecondSub();
                updateEndPrice();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });
        docv.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateFirstSub();
                updateSecondSub();
                updateEndPrice();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });
        tradev.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateFirstSub();
                updateSecondSub();
                updateEndPrice();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });
        percentv.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                updateFirstSub();
                updateSecondSub();
                updateEndPrice();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        });


        taxesv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup r, int id)
            {
                updateFirstSub();
                updateSecondSub();
                updateEndPrice();
            }
        });
        ltv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup r, int id)
            {
                updateFirstSub();
                updateSecondSub();
                updateEndPrice();
            }
        });
    }

    public boolean updateFirstSub()
    {
        if (!otdvp.getText().toString().equals(""))
        {
            double lt = 0;
            switch (ltv.getCheckedRadioButtonId())
            {
                case R.id.newlt:
                    lt = Double.parseDouble(sharedPref.getString("newlt", "196"));
                    break;
                case R.id.transfer:
                    lt = Double.parseDouble(sharedPref.getString("transfer", "122"));
                    break;
                case R.id.out:
                    lt = Double.parseDouble(sharedPref.getString("out", "10"));
                    break;
            }
            subtotal = Double.parseDouble(otdvp.getText().toString()) - lt;
            subtotalv.setText(String.format("%.2f", subtotal));
            return true;
        }
        return false;
    }

    public boolean updateSecondSub()
    {
        if (!tradev.getText().toString().equals("") && updateFirstSub())
        {
            double taxpercent = 0;
            switch (taxesv.getCheckedRadioButtonId())
            {
                case R.id.dupage:
                    taxpercent = Double.parseDouble(sharedPref.getString("dupage", "7.25"));
                    break;
                case R.id.chicago:
                    taxpercent = Double.parseDouble(sharedPref.getString("chicago", "9.50"));
                    break;
                case R.id.cook:
                    taxpercent = Double.parseDouble(sharedPref.getString("cook", "8.25"));
                    break;
                case R.id.other:
                    if (!percentv.getText().toString().equals(""))
                    {
                        taxpercent = Double.parseDouble(percentv.getText().toString());
                    }
                    break;
            }
            double tax = subtotal / ((taxpercent / 100) + 1);
            taxv.setText(String.format("%.2f", tax));

            sub = tax + Double.parseDouble(tradev.getText().toString());
            subv.setText(String.format("%.2f", sub));
            return true;
        }
        return false;
    }

    public void updateEndPrice()
    {
        if (!docv.getText().toString().equals("") && updateSecondSub())
        {
            end = sub - Double.parseDouble(docv.getText().toString());
            pricevp.setText(String.format("%.2f", end));
        }
    }


    public void setup(View v)
    {
        EditText docv = (EditText) v.findViewById(R.id.doc);
        RadioButton dupage = (RadioButton) v.findViewById(R.id.dupage);
        RadioButton cook = (RadioButton) v.findViewById(R.id.cook);
        RadioButton chicago = (RadioButton) v.findViewById(R.id.chicago);
        RadioButton newlt = (RadioButton) v.findViewById(R.id.newlt);
        RadioButton transfer = (RadioButton) v.findViewById(R.id.transfer);
        RadioButton out = (RadioButton) v.findViewById(R.id.out);
        dupage.setText("DuPage - " + sharedPref.getString("dupage", "7.25") + "%");
        cook.setText("Cook - " + sharedPref.getString("cook", "8.25") + "%");
        chicago.setText("Chicago - " + sharedPref.getString("chicago", "9.50") + "%");
        newlt.setText("New - $" + sharedPref.getString("new", "196"));
        transfer.setText("Transfer - $" + sharedPref.getString("transfer", "122"));
        out.setText("Out - $" + sharedPref.getString("out", "10"));
        docv.setText(sharedPref.getString("doc", "191"));
    }

    /**
     * This class makes the ad request and loads the ad.
     */
    public static class AdFragment extends Fragment
    {

        private AdView mAdView;

        public AdFragment()
        {
        }

        @Override
        public void onActivityCreated(Bundle bundle)
        {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    /*.addTestDevice("8F0E678B4BFECC3DF4FDBB0BC93BC803")*/.addTestDevice("ABCDEF012345")
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /**
         * Called when leaving the activity
         */
        @Override
        public void onPause()
        {
            if (mAdView != null)
            {
                mAdView.pause();
            }
            super.onPause();
        }

        /**
         * Called when returning to the activity
         */
        @Override
        public void onResume()
        {
            super.onResume();
            if (mAdView != null)
            {
                mAdView.resume();
            }
        }

        /**
         * Called before the activity is destroyed
         */
        @Override
        public void onDestroy()
        {
            if (mAdView != null)
            {
                mAdView.destroy();
            }
            super.onDestroy();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_otd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent goSettings = new Intent(this, SettingsActivity.class);
            this.startActivity(goSettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
