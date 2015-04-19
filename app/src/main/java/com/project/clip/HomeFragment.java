package com.project.clip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class HomeFragment extends Fragment {

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        WebView wv = (WebView)rootView.findViewById(R.id.stockTicker_WebView);

        /*Disable Scrolling in WebView*/

        wv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });



        String url = "http://www.finance.yahoo.com/";
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url) {
                view.loadUrl(url);
                return true;
            }
        });



        /*************************ADD CONTACT***********************/
        RelativeLayout rl = (RelativeLayout)rootView.findViewById(R.id.add_contact);

        RelativeLayout addStocks = (RelativeLayout)rootView.findViewById(R.id.layout_addstocks);

        addStocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //MUST USE LayoutInflater HERE TO INFLATE LAYOUT FOR findViewById
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialog_layout = inflater.inflate(R.layout.dialog_stocks, null);

                //Create the alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());


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




                        }


                    }
                });
                alert.setPositiveButton("Update Totals", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                                Dialog dialog_stocks = (Dialog) dialog;
                                EditText brokerageDeposit = (EditText) dialog_stocks.findViewById(R.id.editText_brokerage);
                                String value = brokerageDeposit.getText().toString();
                                if(value.equals("")){

                                }
                           else {
                                    //Update value in database
                                    MainActivity.database.createTotal(null, null, value, null, null);

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
        });



        rl.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


                // Creates a new Intent to insert a contact
                Intent intent = new Intent(Contacts.Intents.Insert.ACTION);
                // Sets the MIME type to match the Contacts Provider
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                 /* Sends the Intent */
                startActivity(intent);

            }
        });






        return rootView;






    }
}
    //TODO: Add click listeners to Stocks,Calories.