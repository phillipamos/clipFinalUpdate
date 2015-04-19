package com.project.clip;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.project.clip.R.layout.fragment_pager_list;


public class ActivityRegister extends FragmentActivity {

    static final int NUM_ITEMS = 5;

    MyAdapter mAdapter;

    public static ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_register);

        FragmentManager fm = this.getSupportFragmentManager();
        mAdapter = new MyAdapter(fm);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_pager_list,null);




        MainActivity.robotoThin =  Typeface.createFromAsset(this.getAssets(), "Fonts/Roboto-Thin.ttf");
        MainActivity.robotoMedium = Typeface.createFromAsset(this.getAssets(),"Fonts/Roboto-Medium.ttf");
        MainActivity.robotoItalic = Typeface.createFromAsset(this.getAssets(),"Fonts/Roboto-Italic.ttf");
        MainActivity.robotoRegular = Typeface.createFromAsset(this.getAssets(),"Fonts/Roboto-Regular.ttf");


        // Watch for button clicks.
        Button button = (Button)view.findViewById(R.id.button_express);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               mPager.setCurrentItem(0);
           }
       });
         button = (Button)view.findViewById(R.id.button_custom);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(NUM_ITEMS-1);
            }
        });
    }
       public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {




            return ArrayListFragment.newInstance(position);

        }
    }

    public static class ArrayListFragment extends Fragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;


        }


        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View v = inflater.inflate(fragment_pager_list, container, false);

            switch(mNum) {

               case (0):
                    v = inflater.inflate(fragment_pager_list, container, false);
                   // View tv = v.findViewById(R.id.text);
                   TextView welcome = (TextView) v.findViewById(R.id.textView_welcome);
                   welcome.setText("Welcome");
                   welcome.setTypeface(MainActivity.robotoThin);
                   TextView setUp = (TextView) v.findViewById(R.id.textView_setup);
                   setUp.setText("Let's get you set up");
                   setUp.setTypeface(MainActivity.robotoItalic);
                   TextView express = (TextView) v.findViewById(R.id.textView_noAccounts);
                   express.setTypeface(MainActivity.robotoRegular);
                   TextView custom = (TextView) v.findViewById(R.id.textView_customAccounts);
                   custom.setTypeface(MainActivity.robotoRegular);

                   Button expressButton = (Button) v.findViewById(R.id.button_express);
                   Button customButtom = (Button) v.findViewById(R.id.button_custom);

                   expressButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                        mPager.setCurrentItem(1);
                       }
                   });


                   return v;

               case(1):
                   v = inflater.inflate(R.layout.express_setup, container,false);
                   TextView expressSetup = (TextView)v.findViewById(R.id.textView_ExpressSetup);
                   expressSetup.setTypeface(MainActivity.robotoThin);
                   TextView create = (TextView)v.findViewById(R.id.textView_createAccount);
                   create.setTypeface(MainActivity.robotoItalic);
                   final EditText userName = (EditText)v.findViewById(R.id.editText_userName);
                   final EditText password = (EditText)v.findViewById(R.id.editText_Password);
                   final EditText rePassword = (EditText)v.findViewById(R.id.editText_reenter);
                   final EditText secuiryQ = (EditText)v.findViewById(R.id.editText_securitQ);

                   Button register = (Button)v.findViewById(R.id.button_cancelPass);
                   Button cancel = (Button)v.findViewById(R.id.button_getPass);

                   Spinner spinnerSecurityQ = (Spinner)v.findViewById(R.id.spinner_securityQ);
                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                           android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.security));

                   SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                   final SharedPreferences.Editor editor = prefs.edit();
                   spinnerSecurityQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           if (position == 0) {
                               editor.putString("forgotPassQ", "What is your paternal grandmother's first name?");
                               editor.apply();
                           }
                           if (position == 1) {
                               editor.putString("forgotPassQ", "What is your first pet's name?");
                               editor.apply();
                           }
                       }

                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {

                       }
                   });
                   spinnerSecurityQ.setAdapter(adapter);

                   cancel.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           mPager.setCurrentItem(0);
                       }
                   });

                   register.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           final String reEnter = rePassword.getText().toString();
                           final String user = userName.getText().toString();
                           final String pass = password.getText().toString();
                           final String secuirtyQ = secuiryQ.getText().toString();


                           if(reEnter.equals(pass)&&user.length()>=4){


                               editor.putString("UserPass",user+":"+pass);
                               editor.putString("SecurityQ",secuirtyQ);
                               editor.apply();

                               mPager.setCurrentItem(5);

                           }
                           else if(!reEnter.equals(pass))
                           {
                               Toast toast = Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_LONG);
                               toast.show();
                           }
                       }
                   });
                return v;

                case(4):
                    v = inflater.inflate(R.layout.layout_loading_account,null);
                    Handler handler = new Handler();
                    //Delays the handler  for 3500ms
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                        }
                    }, 3500);
                    return v;




            }

    return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        //@Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }



    void ShowProgressBar() {
        final ProgressBar progressBar = new ProgressBar(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final AlertDialog alert = builder.create();
        alert.setView(progressBar);
        alert.show();
        Handler handler = new Handler();
        //Delays the handler  for 3500ms
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 3500);

    }

    }
