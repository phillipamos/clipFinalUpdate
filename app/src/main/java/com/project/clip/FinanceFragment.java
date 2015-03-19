package com.project.clip;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class FinanceFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView cashTextView;
    TextView assetsTextView;
    TextView creditTextView;
    TextView stocksTextView;



    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;

    //Strings for determining which FAB button was clicked
    private static final String TAG_ADD_FUNDS = "addFunds";
    private static final String TAG_ADD_CREDIT = "addCredit";
    private static final String TAG_ADD_ASSETS = "addAssets";
    private static final String TAG_ADD_STOCKS = "addStocks";




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //GraphView variables
        double day1,day2,day3,day4,day5,day6,day7;

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);


        View spin = inflater.inflate(R.layout.dialog_credit,null);
        Spinner spinner = (Spinner) spin.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(spin.getContext(),
                R.array.credit_cards, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);





        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());






        //Set CASH TextView
        cashTextView = (TextView) rootView.findViewById(R.id.textView_money);
        String money = prefs.getString("moneyString", "0");
        cashTextView.setText(money.toString());
        //Set ASSETS TextView
        assetsTextView = (TextView) rootView.findViewById(R.id.textView_Assets);
        String assets = prefs.getString("assetsString", "0");
        assetsTextView.setText(assets.toString());
        //Set CREDIT TextView
        creditTextView = (TextView) rootView.findViewById(R.id.textView_creditcard);
        String credit = prefs.getString("creditString", "0");
        creditTextView.setText(credit.toString());
        //Set STOCK TextView
        stocksTextView = (TextView) rootView.findViewById(R.id.textView_stockValue);
        String stocks = prefs.getString("stocksString", "0");
        stocksTextView.setText(stocks.toString());


        /*******SET UP THE GRAPH VIEW********/

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        graph.setTitle("Financial History");


        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 4344),
                new DataPoint(1, 5887),
                new DataPoint(2, 3656),
                new DataPoint(3, 2565),
                new DataPoint(4, 6545),
                new DataPoint(5, 1454),
                new DataPoint(6, 9323)
        });
        graph.addSeries(series);



          /*******SET UP THE FLOATING ACTION BUTTON********/
            ImageView icon = new ImageView(getActivity()); // Create an icon
            icon.setImageResource(R.drawable.ic_action_new);


            actionButton = new FloatingActionButton.Builder(getActivity())
                    .setContentView(icon)
                    .setBackgroundDrawable(R.drawable.selector_button)
                    .build();


            //Set up ADD icons FAB
            ImageView iconAddFunds = new ImageView(getActivity());
            iconAddFunds.setImageResource(R.drawable.bill_add);
            ImageView iconAddAssets = new ImageView(getActivity());
            iconAddAssets.setImageResource(R.drawable.home_add);
            ImageView iconAddStocks = new ImageView(getActivity());
            iconAddStocks.setImageResource(R.drawable.stock_market_add);
            ImageView iconAddCredit = new ImageView(getActivity());
            iconAddCredit.setImageResource(R.drawable.mastercard_add);

            //Sub Action Builder - Sets up the sub action buttons
            SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());


            //Extra buttons to be placed on the FAB
            SubActionButton buttonAddFunds = itemBuilder.setContentView(iconAddFunds).build();
            SubActionButton buttonAddAssets = itemBuilder.setContentView(iconAddAssets).build();
            SubActionButton buttonAddStocks = itemBuilder.setContentView(iconAddStocks).build();
            SubActionButton buttonAddCredit = itemBuilder.setContentView(iconAddCredit).build();

            buttonAddFunds.setTag(TAG_ADD_FUNDS);
            buttonAddAssets.setTag(TAG_ADD_ASSETS);
            buttonAddCredit.setTag(TAG_ADD_CREDIT);
            buttonAddStocks.setTag(TAG_ADD_STOCKS);

             actionMenu = new FloatingActionMenu.Builder(getActivity())
                    .addSubActionView(buttonAddFunds)
                    .addSubActionView(buttonAddAssets)
                    .addSubActionView(buttonAddStocks)
                    .addSubActionView(buttonAddCredit)
                    .attachTo(actionButton)
                    .build();


            //Set the sub action buttons
            buttonAddAssets.setOnClickListener(this);
            buttonAddFunds.setOnClickListener(this);
            buttonAddStocks.setOnClickListener(this);
            buttonAddCredit.setOnClickListener(this);




        //TODO: Add cardview animations here


        return rootView;

    }

    @Override
    public void onPause()
    {
            super.onPause();

        actionButton.setVisibility(View.INVISIBLE);
        actionMenu.close(false);


    }

    @Override
    public void onStart()
    {
        super.onStart();

        actionButton.setVisibility(View.VISIBLE);

    }



    @Override
    public void onClick(View v) {

        actionMenu.close(true);

        if (v.getTag().equals(TAG_ADD_FUNDS)) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final SharedPreferences.Editor editor = prefs.edit();




            // Set an EditText view to get user input
            final EditText input = (EditText)getView().findViewById(R.id.editText_cash);
            //alert.setView(input);
            alert.setView(R.layout.dialog_cash);
            alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = "$" + input.getText().toString();
                    // Set pref value
                    editor.putString("moneyString", value);
                    editor.apply();

                    //Update the textView
                    TextView money = (TextView) getView().findViewById(R.id.textView_money);
                    money.setText(value);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });


            alert.show();

        }
        if (v.getTag().equals(TAG_ADD_CREDIT)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());


            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final SharedPreferences.Editor editor = prefs.edit();

            // Set an EditText view to get user input
            final EditText input = new EditText(getActivity());
            alert.setView(R.layout.dialog_credit);






            alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = "$" + input.getText().toString();

                    editor.putString("creditString", value);
                    editor.apply();

                    //Update the textView
                    TextView credit = (TextView) getView().findViewById(R.id.textView_creditcard);
                    credit.setText(value);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();

        }
        if (v.getTag().equals(TAG_ADD_ASSETS)) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            alert.setTitle("Add Assests");
            alert.setMessage("Total Assests: ");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final SharedPreferences.Editor editor = prefs.edit();


            // Set an EditText view to get user input
            final EditText input = new EditText(getActivity());
            alert.setView(input);

            alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = "$" + input.getText().toString();
                    // Do something with value!
                    editor.putString("assetsString", value);
                    editor.apply();

                    //Update the textView
                    TextView assets = (TextView) getView().findViewById(R.id.textView_Assets);
                    assets.setText(value);


                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }

        if (v.getTag().equals(TAG_ADD_STOCKS)) {
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Update Market Value");
                alert.setMessage("Total Value: ");

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final SharedPreferences.Editor editor = prefs.edit();

                // Set an EditText view to get user input
                final EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = "$" + input.getText().toString();

                        editor.putString("stocksString", value);
                        editor.apply();

                        //Update the textView
                        TextView stocks = (TextView) getView().findViewById(R.id.textView_stockValue);
                        stocks.setText(value);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();


            }


        }

    }




}