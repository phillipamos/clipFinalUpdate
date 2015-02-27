package com.project.clip;

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
import android.widget.RelativeLayout;


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