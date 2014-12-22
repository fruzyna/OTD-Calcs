package com.mail929.android.otdcalcs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class OTDActivity extends ActionBarActivity
{
    Button button;
    LinearLayout main;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otd);

        context = this;

        main = (LinearLayout) findViewById(R.id.content);
        button = (Button) findViewById(R.id.button);

        final CheckBox cb = (CheckBox) findViewById(R.id.otdcb);
        if(cb.isChecked())
        {
            findOTD();
        }
        else
        {
            findOTD();
        }
        cb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                main.removeAllViews();
                if(cb.isChecked())
                {
                    findPrice();
                }
                else
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
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences sharedPref = context.getSharedPreferences("otd", Context.MODE_PRIVATE);
                EditText pricev = (EditText) v.findViewById(R.id.price);
                EditText docv = (EditText) v.findViewById(R.id.doc);
                EditText tradev = (EditText) v.findViewById(R.id.trade);
                EditText percentv = (EditText) v.findViewById(R.id.percent);
                TextView subv = (TextView) v.findViewById(R.id.subtotal);
                TextView taxv = (TextView) v.findViewById(R.id.tax);
                TextView otdv = (TextView) v.findViewById(R.id.otd);
                RadioGroup ltv = (RadioGroup) v.findViewById(R.id.lt);
                RadioGroup taxesv = (RadioGroup) v.findViewById(R.id.taxes);

                double price = Double.parseDouble(pricev.getText().toString());
                double doc = Double.parseDouble(docv.getText().toString());
                double trade = Double.parseDouble(tradev.getText().toString());
                double sub = price + doc - trade;
                double tax;
                double lt = 0;
                double otd;

                subv.setText(String.format("%.2f", sub));

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
                        taxpercent = Double.parseDouble(percentv.getText().toString());
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

                otd = sub + tax + lt;
                otdv.setText(String.format("%.2f", otd));
            }
        });
    }

    public void findPrice()
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.calc_price, main);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences sharedPref = context.getSharedPreferences("otd", Context.MODE_PRIVATE);
                EditText otdv = (EditText) v.findViewById(R.id.otd);
                EditText docv = (EditText) v.findViewById(R.id.doc);
                EditText tradev = (EditText) v.findViewById(R.id.trade);
                EditText percentv = (EditText) v.findViewById(R.id.percent);
                TextView subtotalv = (TextView) v.findViewById(R.id.subtotal);
                TextView subv = (TextView) v.findViewById(R.id.sub);
                TextView taxv = (TextView) v.findViewById(R.id.tax);
                TextView pricev = (TextView) v.findViewById(R.id.price);
                RadioGroup ltv = (RadioGroup) v.findViewById(R.id.lt);
                RadioGroup taxesv = (RadioGroup) v.findViewById(R.id.taxes);

                double otd = Double.parseDouble(otdv.getText().toString());
                double doc = Double.parseDouble(docv.getText().toString());
                double trade = Double.parseDouble(tradev.getText().toString());
                double subtotal;
                double sub;
                double tax;
                double lt = 0;
                double price;

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
                subtotal = otd - lt;
                subtotalv.setText(String.format("%.2f", subtotal));

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
                        taxpercent = Double.parseDouble(percentv.getText().toString());
                        break;
                }
                tax = subtotal / ((taxpercent / 100) + 1);
                taxv.setText(String.format("%.2f", tax));

                sub = tax + trade;
                subv.setText(String.format("%.2f", sub));

                price = sub - doc;
                pricev.setText(String.format("%.2f", price));
            }
        });
    }

    /**
     * This class makes the ad request and loads the ad.
     */
    public static class AdFragment extends Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("8F0E678B4BFECC3DF4FDBB0BC93BC803").addTestDevice("ABCDEF012345")
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /** Called when leaving the activity */
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        /** Called when returning to the activity */
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        /** Called before the activity is destroyed */
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }

    }
}
