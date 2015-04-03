package com.project.clip;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

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
    private static final String TAG_PAYMENT_REMINDER = "paymentReminder";







    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FinanceDataSource datasource;
        //GraphView variables
        double day1,day2,day3,day4,day5,day6,day7;

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);














        //Payment reminder button
        ImageButton paymentReminderButton = (ImageButton)rootView.findViewById(R.id.button_paybills);
        paymentReminderButton.setOnClickListener(this);



        Spinner spinner = (Spinner) rootView.findViewById(R.id.accountSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.accounts, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);





        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        AdapterView.OnItemSelectedListener account_listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case (1):
                        ShowProgressBar();
                        String url = "https://www.bankofamerica.com";
                        ShowAccountAlert(url);
                        break;

                    case(2):
                        ShowProgressBar();
                        url = "https://www.chase.com";
                        ShowAccountAlert(url);

                        break;

                    case(3):
                        ShowProgressBar();
                        url = "http://www.wellsfargo.com";
                        ShowAccountAlert(url);
                        break;
                    case(4):
                        break;



                }



        //TODO Add listeners for add new account.

            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinner.setOnItemSelectedListener(account_listener);


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


            SetUpGraphView(rootView);

            SetUpFloatingActionButton();



        return rootView;

    }


    @Override
    public void onPause()
    {
            super.onPause();

        actionButton.setVisibility(View.INVISIBLE);
        actionMenu.close(false);
       // datasource.open();
        super.onResume();

    }

    @Override
    public void onStart()
    {
        super.onStart();

        actionButton.setVisibility(View.VISIBLE);

    }



    @Override
    public void onClick(View v) {
            //TODO Set up switch here to avoid NULL pointers


        actionMenu.close(true);
        if(v.getId()==R.id.button_paybills){
            ShowPaymentReminder();

        }

        else if (v.getTag().equals(TAG_ADD_FUNDS)) {

            ShowAddAlert(R.layout.dialog_cash,v);

        }
        else if (v.getTag().equals(TAG_ADD_CREDIT)) {

           ShowAddAlert(R.layout.dialog_credit,v);

        }
        else if (v.getTag().equals(TAG_ADD_ASSETS)) {

           ShowAddAlert(R.layout.dialog_assets,v);
        }

        else if (v.getTag().equals(TAG_ADD_STOCKS)) {

           ShowAddAlert(R.layout.dialog_stocks,v);
        }


        }



void ShowProgressBar()
{
    final ProgressBar progressBar = new ProgressBar(getActivity());
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    final AlertDialog alert = builder.create();
    alert.setView(progressBar);
    alert.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        public void run() {
            alert.dismiss();
        }
    }, 3500);



}

void ShowAccountAlert(String url)
{


    final AlertDialog.Builder Alert = new AlertDialog.Builder(getActivity());
    ProgressBar mProgress = new ProgressBar(getActivity());



   final WebView webView = new WebView(getActivity()) {
        @Override
        public boolean onCheckIsTextEditor() {
            return true;
        }
    };

    webView.loadUrl(url);
    WebSettings BofAwebSettings = webView.getSettings();
    BofAwebSettings.setJavaScriptEnabled(true);





    webView.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url) {
            view.loadUrl(url);
            return true;
        }

    });




    Alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        dialog.dismiss();

        }
    });

    Alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
          dialog.cancel();
        }
    });



    // Execute some code after 3.5 seconds have passed
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        public void run() {
            Alert.setView(webView);
            Alert.show();


        }
    }, 3500);




}


void ShowAddAlert(final int layoutID, final View view)
{



    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
     final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    final SharedPreferences.Editor editor = prefs.edit();


    alert.setView(layoutID);
    alert.setPositiveButton("Update Totals", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {

            switch(layoutID){


                case(R.layout.dialog_cash):

                    Dialog f  = (Dialog) dialog;

                    EditText cashInput = (EditText)f.findViewById(R.id.editText_Cash);
                    String value = "$" + cashInput.getText().toString();
                    // Set pref value
                    editor.putString("moneyString", value);
                    editor.apply();
                    //Update the textView
                    TextView money = (TextView)getActivity().findViewById(R.id.textView_money); //This is working
                    money.setText(value);
                    break;

                case(R.layout.dialog_assets):
                     f  = (Dialog) dialog;
                    EditText assetsInput = (EditText)f.findViewById(R.id.editText_creditValue);
                    value = "$" + assetsInput.getText().toString();


                    // Set pref value
                    editor.putString("assetsString", value);
                    editor.apply();
                    //Update the textView
                    TextView assets = (TextView)getActivity().findViewById(R.id.textView_Assets); //This is working
                    assets.setText(value);
                    break;

                case(R.layout.dialog_credit):
                    f  = (Dialog) dialog;
                    EditText creditBalanceInput = (EditText)f.findViewById(R.id.editText_creditValue);
                    value = "$" + creditBalanceInput.getText().toString();


                    // Set pref value
                    editor.putString("creditString", value);
                    editor.apply();
                    //Update the textView
                    TextView credit = (TextView)getActivity().findViewById(R.id.textView_creditcard); //This is working
                    credit.setText(value);
                    break;


                case(R.layout.dialog_stocks):
                    f  = (Dialog) dialog;
                    EditText brokerageDeposit = (EditText)f.findViewById(R.id.editText_brokerage);
                    value = "$" + brokerageDeposit.getText().toString();


                    // Set pref value
                    editor.putString("creditString", value);
                    editor.apply();
                    //Update the textView
                    TextView brokerage = (TextView)getActivity().findViewById(R.id.textView_stockValue); //This is working
                    brokerage.setText(value);
                    break;



            }

        }
    });

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
        }
    });


    alert.show();
}

void SetUpFloatingActionButton()
{        /*******SET UP THE FLOATING ACTION BUTTON********/
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


}

void SetUpGraphView(View rootView)
{
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

}


//TODO ADD a lot of shit to ShowPaymentReminder()
void ShowPaymentReminder()
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final int i = 3;






        MainActivity.database.createNetwork("name","affiliation","date","times used","comments");

        final List<FinanceData> values = MainActivity.database.getAllComments(DataStrings.TABLE_NETWORK);

       // String[] values = MainActivity.database.getAllNetwork();


        // Set an EditText view to get user input
        final ListView list= new ListView(getActivity());


        //TODO Set up ArrayAdapter for listview
        String reminders[] = new String[i+1];
        reminders[0] = "Electricity   -  Next due: 4/28/15 ";
        reminders[1] = "Master Card   -  Next due: 5/1/15";
        reminders[2] = "Cell Phone    -  Next due: 5/2/15";
        reminders[i] = "Add bill reminder";


        ArrayAdapter<FinanceData> adapter = new ArrayAdapter<FinanceData>(getActivity(),
                android.R.layout.simple_list_item_1, values);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_list_item_1, reminders);

        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case(3):





                }
            }
        });






        alert.setView(list);

        alert.setPositiveButton("Add reminder", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final SharedPreferences.Editor editor = prefs.edit();


                alert.setTitle("Add bill reminder");
                alert.setMessage("Bill name and due date: ");

                // Set an EditText view to get user input
                final EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //TODO Update "Save" function to include new bills and due dates.
                        String value = input.getText().toString();



                        MainActivity.database.createNetwork("name","affiliation","date","times used","comments");



                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });


                alert.show();

            }
        });


        alert.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });


        alert.show();

    }



}

