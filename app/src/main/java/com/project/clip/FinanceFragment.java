package com.project.clip;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FinanceFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    TextView cashTextView;
    TextView assetsTextView;
    TextView creditTextView;
    TextView stocksTextView;
    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;



    public static ArrayAdapter<String> billReminderAdapter;
    public static ArrayAdapter<String> accountAdapter;
    public static Spinner accountsSpinner;

    //Strings for determining which FAB button was clicked
    private static final String TAG_ADD_FUNDS = "addFunds";
    private static final String TAG_ADD_CREDIT = "addCredit";
    private static final String TAG_ADD_ASSETS = "addAssets";
    private static final String TAG_ADD_STOCKS = "addStocks";


    public static int stockCounter = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int mId = 1;


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);

        //Payment reminder button
        Button paymentReminderButton = (Button) rootView.findViewById(R.id.button_paybills);
        paymentReminderButton.setOnClickListener(this);


        //Use shared preferences to initialize totals table on first run of the device
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = prefs.edit();
        //Initialize total table on first run
        if (!prefs.contains("initDBTotal")) {
            MainActivity.database.createTotal(null, null, null, null, null);
            editor.putBoolean("initDBTotal", true);
            editor.apply();
        }





        //Set CASH TextView
        cashTextView = (TextView) rootView.findViewById(R.id.textView_money);
        String money = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_CASH_TOTAL, "_id", "1");
        cashTextView.setText("$" + money);

        //Set ASSETS TextView
        assetsTextView = (TextView) rootView.findViewById(R.id.textView_Assets);
        String assets = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_ASSET_TOTAL, "_id", "1");
        assetsTextView.setText("$" + assets);

        //Set CREDIT TextView
        creditTextView = (TextView) rootView.findViewById(R.id.textView_creditcard);
        String credit = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_CREDIT_TOTAL, "_id", "1");
        creditTextView.setText("$" + credit);

        //Set STOCK TextView
        stocksTextView = (TextView) rootView.findViewById(R.id.textView_stockValue);
        String stocks = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_STOCK_TOTAL, "_id", "1");
        stocksTextView.setText("$" + stocks);

        //Initialize the graph
        SetUpGraphView(rootView);

        //Initialize the FAB
        SetUpFloatingActionButton();

        AccountSpinner(rootView, false);


        return rootView;


    }


    @Override
    public void onPause() {
        super.onPause();

        actionButton.setVisibility(View.INVISIBLE);
        actionMenu.close(false);

        super.onResume();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();

        actionButton.setVisibility(View.VISIBLE);

    }



    @Override
    public void onClick(View v) {
        //TODO Set up switch here to avoid NULL pointers. Replace else ifs


        actionMenu.close(true);
        if (v.getId() == R.id.button_paybills) {
            ShowPaymentReminder();

        } else if (v.getTag().equals(TAG_ADD_FUNDS)) {

            ShowAddAlert(R.layout.dialog_cash, v);

        } else if (v.getTag().equals(TAG_ADD_CREDIT)) {

            ShowAddAlert(R.layout.dialog_credit, v);

        } else if (v.getTag().equals(TAG_ADD_ASSETS)) {

            ShowAddAlert(R.layout.dialog_assets, v);
        } else if (v.getTag().equals(TAG_ADD_STOCKS)) {

            ShowAddAlert(R.layout.dialog_stocks, v);
        }


    }


    //FUNCTION TO SHOW A PROGRESS BAR DURING LOADING OF WEB PAGES
    void ShowProgressBar() {
        final ProgressBar progressBar = new ProgressBar(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final AlertDialog alert = builder.create();
        alert.setView(progressBar);
        alert.show();
        Handler handler = new Handler();
        //Delays the handler  for 3500ms
        handler.postDelayed(new Runnable() {
            public void run() {
                alert.dismiss();
            }
        }, 3500);

    }

    //HANDLES WEBVIEWS FOR THE ACCOUNTS SPINNER
    void ShowAccountAlert(String url) {


        final AlertDialog.Builder Alert = new AlertDialog.Builder(getActivity());


        final WebView webView = new WebView(getActivity()) {
            @Override
            public boolean onCheckIsTextEditor() {
                return true;
            }
        };

        webView.loadUrl(url);
        WebSettings accountSettings = webView.getSettings();
        accountSettings.setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
        //Prevent the dialog from reshowing on back button press
        Alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //do whatever you want the back key to do
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

    //SHOWS ALL ALERTS GENERATED BY THE FAB - THESE INCLUDE: ADD CASH, ADD CREDIT, ETC
    void ShowAddAlert(final int layoutID, final View view) {


        //MUST USE LayoutInflater HERE TO INFLATE LAYOUT FOR findViewById
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialog_layout = inflater.inflate(layoutID, null);

        //Create the alert dialog
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());


        /***Check that layout is dialog_cash before adding dialog_cash's buttons. Add onclick to continuously add accounts**/
        if (layoutID == R.layout.dialog_cash) {


            //Set up the add cash button
            ImageButton buttonAddCash = (ImageButton) dialog_layout.findViewById(R.id.button_addBrokerage);
            //Set the click listener for add cash
            buttonAddCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText cashInput = (EditText) dialog_layout.findViewById(R.id.editText_Cash);
                    String value = cashInput.getText().toString();
                    if (value.equals("")) {
                        Toast toast = Toast.makeText(getActivity(), "Cash field empty", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        MainActivity.database.createTotal(null, null, null, null, value);
                        Toast toast = Toast.makeText(getActivity(), "Cash added to total", Toast.LENGTH_LONG);
                        toast.show();
                        cashInput.setText(null);
                        AccountSpinner(view, true);
                    }
                }
            });
        }

        /****Check for credit layout before adding credit's buttons/views. Also add Onclick to continuously add accounts***/
        if (layoutID == R.layout.dialog_credit) {
            //Set up the add cash button
            ImageButton buttonAddCredit = (ImageButton) dialog_layout.findViewById(R.id.imageButton_addCredit);
            //Set the click listener for add cash
            buttonAddCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Set up edit text inputs for dialog_credit
                    EditText cardIssuer = (EditText) dialog_layout.findViewById(R.id.editText_cardIssuer);
                    EditText cardBalance = (EditText) dialog_layout.findViewById(R.id.editText_stockValue);
                    EditText cardWebAddr = (EditText) dialog_layout.findViewById(R.id.editText_cardWebSite);

                    //Set strings to edit text inputs
                    String addr = cardWebAddr.getText().toString();
                    String issuer = cardIssuer.getText().toString();
                    String balance = cardBalance.getText().toString();

                    //Validate all fields have some data
                    if (addr.equals("") || issuer.equals("") || balance.equals("")) {
                        Toast toast = Toast.makeText(getActivity(), "Field missing - Account not added", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        //Store new total in database
                        MainActivity.database.createTotal(balance, null, null, null, null);
                        //Add new account to database
                        MainActivity.database.createAccount(issuer, addr, balance);
                        Toast toast = Toast.makeText(getActivity(), "Credit account added", Toast.LENGTH_LONG);
                        toast.show();
                        //Update the Account spinner's adapter to populate the new values without reloading fragment
                        final List<String> accountNames = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS,
                                DataStrings.COLUMN_ACCOUNT_NAME);
                        accountAdapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, accountNames);
                        accountsSpinner.setAdapter(accountAdapter);
                        //Reset the text views for a new account to be added
                        cardIssuer.setText(null);
                        cardBalance.setText(null);
                        cardWebAddr.setText(null);
                        //Update the FinanceFragment credit total text view
                        TextView credit = (TextView) getActivity().findViewById(R.id.textView_creditcard);
                        String creditTotal = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_CREDIT_TOTAL, "_id", "1");
                        credit.setText("$" + creditTotal);


                    }
                }
            });
        }

        /***Check for stocks layout - then add buttons/views. Also add onclick to continuously add stocks****/
        if (layoutID == R.layout.dialog_stocks) {

            //Set up stocks buttons
            ImageButton addStockButton = (ImageButton) dialog_layout.findViewById(R.id.imageButton_AddStock);
            ImageButton brokerageDepositButton = (ImageButton) dialog_layout.findViewById(R.id.button_addBrokerage);

            //Set up onclick listener for add stocks - This listener does everything to add a new stock to the table
            addStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Edit text for SYMBOL and Market Value
                    EditText symbolInput = (EditText) dialog_layout.findViewById(R.id.editText_symbol);
                    EditText marketValueInput = (EditText) dialog_layout.findViewById(R.id.editText_stockValue);

                    //Set up strings for edit text inputs
                    String symbol = symbolInput.getText().toString();
                    String marketValue = marketValueInput.getText().toString();

                    //Validate all fields have some data
                    if (symbol.equals("") || marketValue.equals("")) {
                        Toast toast = Toast.makeText(getActivity(), "Field missing - Stock not added", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        //Store new stock symbol and value into stock table
                        MainActivity.database.createTotal(null, null, marketValue, null, null);
                        //Add new account to database
                        MainActivity.database.createStock(symbol, marketValue);
                        Toast toast = Toast.makeText(getActivity(), "Stock added", Toast.LENGTH_LONG);
                        toast.show();
                        //Reset the text views for a new account to be added
                        symbolInput.setText(null);
                        marketValueInput.setText(null);

                        //Update the FinanceFragment stock total text view
                        TextView stock = (TextView) getActivity().findViewById(R.id.textView_stockValue);
                        String stockTotal = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_STOCK_TOTAL, "_id", "1");
                        stock.setText("$" + stockTotal);

                    }


                }
            });

            //Set up the brokerage account deposit button - this listener does everything to add value to the brokerage account
            brokerageDepositButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set up the edit text for brokerage deposit
                    EditText brokerageDeposit = (EditText) dialog_layout.findViewById(R.id.editText_brokerage);
                    //String to store brokerage deposit amount
                    String depositAmount = brokerageDeposit.getText().toString();

                    //Validate all fields have some data
                    if (depositAmount.equals("")) {
                        Toast toast = Toast.makeText(getActivity(), "Field missing - Amount not deposited", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        //Store new stock symbol and value into stock table
                        MainActivity.database.createTotal(null, null, depositAmount, null, null);
                        //Add new account to database

                        Toast toast = Toast.makeText(getActivity(), "$" + depositAmount + " added", Toast.LENGTH_LONG);
                        toast.show();
                        //Reset the text views for a new account to be added
                        brokerageDeposit.setText(null);


                        //Update the FinanceFragment stock total text view
                        TextView stock = (TextView) getActivity().findViewById(R.id.textView_stockValue);
                        String stockTotal = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_STOCK_TOTAL, "_id", "1");


                        stock.setText("$" + stockTotal);


                    }


                }
            });





        }

        alert.setPositiveButton("Add/Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AccountSpinner(view, true);
                //Switch on all layouts
                switch (layoutID) {


                    case (R.layout.dialog_cash):

                        //Create new dialog to cast the DialogInterface to dialog
                        Dialog dialog_cash = (Dialog) dialog;

                        //Set up bank info edit texts for CASH dialog
                        EditText bankNameInput = (EditText) dialog_cash.findViewById(R.id.editText_cardIssuer);
                        EditText bankBalanceInput = (EditText) dialog_cash.findViewById(R.id.editText_stockValue);
                        EditText bankWebAddrInput = (EditText) dialog_cash.findViewById(R.id.editText_bankwebAddress);

                        //String input from bank info edit texts
                        String name = bankNameInput.getText().toString();
                        String webAddr = bankWebAddrInput.getText().toString();
                        String balance = bankBalanceInput.getText().toString();


                        //Verify fields are not empty
                        if (name.equals("") || webAddr.equals("") || balance.equals("")) {
                            Toast toast = Toast.makeText(getActivity(), "Bank field missing - Account not added", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        //Fields are not empty - Store in database
                        else {
                            MainActivity.database.createAccount(name, webAddr, balance);
                            MainActivity.database.createTotal(null, null, null, null, balance);
                        }
                        //Update the account spinner's adapter by rebuilding the account names and setting it to the spinner
                        final List<String> accountNames = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS,
                                DataStrings.COLUMN_ACCOUNT_NAME);
                        accountAdapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, accountNames);
                        accountsSpinner.setAdapter(accountAdapter);

                        //Update the textView
                        TextView money = (TextView) getActivity().findViewById(R.id.textView_money);
                        String value = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_CASH_TOTAL, "_id", "1");
                        money.setText("$" + value);
                        break;

                    case (R.layout.dialog_assets):
                        Dialog dialog_assets = (Dialog) dialog;

                        //Set up the asset edit text inputs
                        EditText assetValueInput = (EditText) dialog_assets.findViewById(R.id.editText_stockValue);
                        EditText assetNameInput = (EditText) dialog_assets.findViewById(R.id.editText_assetName);
                        String assetName = assetNameInput.getText().toString();
                        String assetValue = assetValueInput.getText().toString();

                        //Store in database
                        MainActivity.database.createAsset(assetName, assetValue);
                        MainActivity.database.createTotal(null, null, null, assetValue, null);

                        //Update the textView
                        TextView assets = (TextView) getActivity().findViewById(R.id.textView_Assets);
                        //Retrieve new total after the new asset value
                        String assetTotal = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_ASSET_TOTAL, "_id", "1");

                        //TODO apply currency formatting to assetTotal
                        assets.setText("$" + assetTotal);

                        break;

                    case (R.layout.dialog_credit):
                        Dialog dialog_credit = (Dialog) dialog;

                        //Set up edit text inputs for dialog_credit
                        EditText cardIssuer = (EditText) dialog_credit.findViewById(R.id.editText_cardIssuer);
                        EditText cardBalance = (EditText) dialog_credit.findViewById(R.id.editText_stockValue);
                        EditText cardWebAddr = (EditText) dialog_credit.findViewById(R.id.editText_cardWebSite);

                        //Set strings to edit text inputs
                        String addr = cardWebAddr.getText().toString();
                        String issuer = cardIssuer.getText().toString();
                        String Balance = cardBalance.getText().toString();

                        //Validate all fields have some data
                        if (addr.equals("") || issuer.equals("") || Balance.equals("")) {
                            Toast toast = Toast.makeText(getActivity(), "Field missing - Account not added", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            //Store new total in database
                            MainActivity.database.createTotal(Balance, null, null, null, null);
                            //Add new account to database
                            MainActivity.database.createAccount(issuer, addr, Balance);
                            Toast toast = Toast.makeText(getActivity(), "Credit account added", Toast.LENGTH_LONG);
                            toast.show();
                            //Update the Account spinner's adapter to populate the new values without reloading fragment
                            final List<String> names = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS,
                                    DataStrings.COLUMN_ACCOUNT_NAME);
                            accountAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_dropdown_item, names);
                            accountsSpinner.setAdapter(accountAdapter);
                            //Reset the text views for a new account to be added
                            cardIssuer.setText(null);
                            cardBalance.setText(null);
                            cardWebAddr.setText(null);
                        }



                        //Update the FinanceFragment credit total text view
                        TextView credit = (TextView) getActivity().findViewById(R.id.textView_creditcard);
                        String creditTotal = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS, DataStrings.COLUMN_CREDIT_TOTAL, "_id", "1");
                        credit.setText("$" + creditTotal);
                        break;


                    case (R.layout.dialog_stocks):
                        Dialog dialog_stocks = (Dialog) dialog;
                        EditText brokerageDeposit = (EditText) dialog_stocks.findViewById(R.id.editText_brokerage);
                        value = brokerageDeposit.getText().toString();
                        if (value.equals("")) {
                            break;
                        }
                        //Update value in database
                        MainActivity.database.createTotal(null, null, value, null, null);
                        //Update the textView
                        TextView brokerage = (TextView) getActivity().findViewById(R.id.textView_stockValue); //This is working
                        brokerage.setText("$" + value);
                        break;

                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.setView(dialog_layout);
        alert.show();
    }

    /**
     * ****SET UP THE FLOATING ACTION BUTTON*******
     */
    void SetUpFloatingActionButton() {
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

    void SetUpGraphView(View rootView) {
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



    void ShowPaymentReminder() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //LIST that contains bill name from bills table
        final List<String> values = MainActivity.database.getAllFromDb(DataStrings.TABLE_BILLS,
                DataStrings.COLUMN_BILL);

        final List<String> dates = MainActivity.database.getAllFromDb(DataStrings.TABLE_BILLS,
                DataStrings.COLUMN_BILL_DUE);


        //Create new listview for the alert dialog layout
        final ListView list = new ListView(getActivity());

        billReminderAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, values);
        list.setAdapter(billReminderAdapter);


        /*******SET THE CLICK LISTENER FOR CHECKBOX******/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                boolean checked;
                int posSwitch = 0;

                //GET ALL VALUES FROM URL COLUMN AND BILLID COLUMN
                final List<String> url = MainActivity.database.getAllFromDb(DataStrings.TABLE_BILLS, DataStrings.COLUMN_BILL_SITE);
                final List<String> billId = MainActivity.database.getAllFromDb(DataStrings.TABLE_BILLS, DataStrings.COLUMN_BILL_ID);


                //TODO: Update the checkbox and close Alert (ListView) after deletion
                //Switch on all positions in the List View (bill reminders)
                switch (posSwitch) {
                    case (0):
                        //PASS URL AND BILLID TO DIALOG - USE ID FOR DELETION OF ROW
                        checked = showBillDetailsDialog(url.get(position), billId.get(position), billReminderAdapter, list);

                        //TODO: Checkbox is not updating
                        list.setItemChecked(1, true);
                        break;
                    default:
                        break;
                }
            }
        });

        alert.setView(list);

        alert.setPositiveButton("Add reminder", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setView(R.layout.dialog_addbill);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Dialog f = (Dialog) dialog;


                        DatePicker datePicker = (DatePicker)f.findViewById(R.id.datePicker);

                        Date dueDate = getDateFromDatePicker(datePicker);




                        String due = dueDate.toString();
                        String newDue = parseDateToddMMyyyy(due);
                        // Set an EditText view to get user input
                        final EditText billName = (EditText) f.findViewById(R.id.editText_billName);
                       // final EditText dueDate = (EditText) f.findViewById(R.id.editText_dueDate);
                        final EditText address = (EditText) f.findViewById(R.id.editText_billAddress);

                        String name = billName.getText().toString();
                        //TODO APPLY CORRECT FORMATTING HERE - RIGHT ALIGN THE PAID ATTRIBUTE
                       // String date = "Due:  " + dueDate.getText().toString() + "     Paid: ";
                        String web = "https://" + address.getText().toString();
                        String date = "Due:  " + newDue+ "   Paid: ";

                       // String alarmDue = dueDate.getText().toString();





                        setAlarm(newDue);


                        //TODO ADD DATE TO DATABASE
                        MainActivity.database.createReminder(name, web, date);

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


    //SHOWS VISIT WEB SITE, DELETE BILL, AND MARK AS PAID DIALOG
    boolean showBillDetailsDialog(final String url, final String pos, final ArrayAdapter adapter, final ListView list) {

        boolean checked;



        //MUST USE LayoutInflater HERE TO INFLATE LAYOUT FOR findViewById
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_bill_detail, null);

        //CREATE NEW ALERT DIALOG BUILDER AND BUTTONS
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        Button visitSite = (Button) dialogView.findViewById(R.id.button_visitSite);
        CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.checkBoxPaid);
        Button delete = (Button) dialogView.findViewById(R.id.button_deleteBill);








        //SET ON CLICK LISTENER TO VISIT URL
        visitSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If no URL was entered at the time of creation notify user
                if (url.equals("https://")) {
                    Toast toast = Toast.makeText(getActivity(), "No address entered", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    ShowProgressBar();
                    ShowAccountAlert(url); //URL = WEBSITE AT ID = POS
                }
            }
        });
        //SET ON CLICK LISTENER TO DELETE BILL
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.database.deleteRow(DataStrings.TABLE_BILLS, "_id", pos);


                Toast toast = Toast.makeText(getActivity(), "Bill reminder deleted", Toast.LENGTH_LONG);
                toast.show();

                final List<String> values = MainActivity.database.getAllFromDb(DataStrings.TABLE_BILLS,
                        DataStrings.COLUMN_BILL);
                billReminderAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_multiple_choice, values);
                list.setAdapter(billReminderAdapter);



            }
        });


        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Log.d("which button", Integer.toString(whichButton));
            if(whichButton==0) {
                dialog.dismiss();
            }
            }
        });





        alert.setView(dialogView);
        alert.show();

        //Set check box
        checkBox.setChecked(!checkBox.isChecked());
        if (checkBox.isChecked()) {
            checked = true;
        } else checked = false;


        //Return checked (bill paid check box)
        return checked;


    }

    //SETS UP THE ACCOUNT SPINNER AND ONCLICK FUNCTIONS
    void AccountSpinner(View rootView, boolean notify) {

        //Populate the list of accounts
        final List<String> accountNames = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS,
                DataStrings.COLUMN_ACCOUNT_NAME);

        accountAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, accountNames);


        if (notify) {
            accountAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, accountNames);
            return;
        }

        //Set the first position of spinner - if no accounts added display. Otherwise show " select account"
        if (accountNames.size() == 0) {
            accountNames.add(0, "                             No accounts added");
        } else {
            accountNames.add(0, "                              Select Account");
        }
        //Create spinner for the accounts spinner
        accountsSpinner = (Spinner) rootView.findViewById(R.id.accountSpinner);

        accountsSpinner.setAdapter(accountAdapter);


        //GET ALL VALUES FROM URL COLUMN AND NAME COLUMN OF ACCOUNTS TABLE
        //NEEDED BECAUSE DELETION FROM TABLE CAUSES UNKNOWN ID - LIST WILL ORDER FROM 0
        final List<String> accountURL = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS, DataStrings.COLUMN_ACCOUNT_WEBSITE);
        final List<String> accountId = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS, DataStrings.COLUMN_ACCOUNT_ID);
        final boolean first = false;


        //Accounts spinner click listener - Opens corresponding webpage based on position in list
        AdapterView.OnItemSelectedListener account_listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    /*This handles 2 things:
                     1. Catch first load to avoid auto selecting and loading page.
                     2. Prevents out of bounds exception on position=0.
                        The 0 position is the spinner hint - no need to select it.
                      */

                if (position == 0) {
                    return;
                }

                ShowProgressBar();
                ShowAccountAlert(accountURL.get(position - 1));




            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        accountsSpinner.setOnItemSelectedListener(account_listener);


    }


    public final void setAlarm(String dateInString) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);


        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM yyyy");


        try {

            Date date = formatter.parse(dateInString);
            long firstTime = date.getTime();
            Calendar c = Calendar.getInstance();

            Date d = new Date();
            long n = d.getTime();
            // Schedule the alarm!
            if (firstTime < n + 86400000) {
                firstTime = n + 2000;
            }
            c.add(Calendar.SECOND, 10);
            
            alarmManager.set(AlarmManager.RTC_WAKEUP, firstTime, pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {




    }

    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);



        return calendar.getTime();
    }
    public static class Palette {
        private String name;
        private String hexValue;
        private int intValue;
        private int position;

        public Palette(String name, String hexValue, int intValue, int position) {
            this.name = name;
            this.hexValue = hexValue;
            this.intValue = intValue;
            this.position = position;
        }


        public String getName() {
            return name;
        }

        public String getHexValue() {
            return hexValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public int getPosition() {
            return position;
        }

    }


    public static class PaletteViewHolder extends RecyclerView.ViewHolder {


        protected TextView titleText;
        protected TextView contentText;
        protected CardView card;
        protected WebView stocksWebView;
        protected boolean isStocks;
        protected int position;

        public PaletteViewHolder(View itemView) {
            super(itemView);


            if (RecyclerAdapterFinance.layout.equals("stocks")) {

                isStocks = true;
            } else {
                isStocks = false;
            }

            card = (CardView) itemView;
            titleText = (TextView) itemView.findViewById(R.id.name);
            contentText = (TextView) itemView.findViewById(R.id.content);
            stocksWebView = (WebView) itemView.findViewById(R.id.webView_stocks);
            titleText.setTypeface(MainActivity.robotoThin);
            contentText.setTypeface(MainActivity.robotoItalic);
        }

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        String outputPattern = "EEE MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}