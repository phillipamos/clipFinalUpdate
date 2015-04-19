package com.project.clip;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class MainActivity extends ActionBarActivity

        implements NavigationDrawerFragment.NavigationDrawerCallbacks

{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static final String TAG_HOME_FRAGMENT = "homeFragment";


    public static FinanceDataSource database;
    public static Typeface robotoMedium;
    public static Typeface robotoThin;
    public static Typeface robotoItalic;
    public static Typeface robotoRegular;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Set up all the fonts
        robotoThin =  Typeface.createFromAsset(this.getAssets(),"Fonts/Roboto-Thin.ttf");
        robotoMedium = Typeface.createFromAsset(this.getAssets(),"Fonts/Roboto-Medium.ttf");
        robotoItalic = Typeface.createFromAsset(this.getAssets(),"Fonts/Roboto-Italic.ttf");
        robotoRegular = Typeface.createFromAsset(this.getAssets(),"Fonts/Roboto-Regular.ttf");

        database = new FinanceDataSource(this);
        database.open();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));





    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments


        android.support.v4.app.Fragment fragment = null;


        switch (position) {

            case 0:
                fragment = new HomeFragment();
                mTitle = "CLIP";


                break;
            case 1:
                fragment = new EducationFragment();
                mTitle = getString(R.string.nav_drawer_education);
                break;

            case 2:
                fragment = new FinanceFragment();
                mTitle = getString(R.string.nav_drawer_finance);
                break;

            case 3:
                fragment = new CareerFragment();
                mTitle = getString(R.string.nav_drawer_career);
                break;

            case 4:
                fragment = new HealthFragment();
                mTitle = getString(R.string.nav_drawer_health);
                break;

            default:

                break;
        }




        /*Create new fragments for each subsection*/
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();


        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");


        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "CLIP";
            case 2:
                mTitle = getString(R.string.title_section1);
                break;
            case 3:
                mTitle = getString(R.string.title_section2);
                break;
            case 4:
                mTitle = getString(R.string.title_section3);
            case 5:
                mTitle = "Health";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            return rootView;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /**
     * *********ADD FUNDS BUTTON*********************
     */
    public void addFunds(View view) {
        android.support.v4.app.Fragment fragment = new Fragment_finance_recycler();
        Bundle bundle = new Bundle();

        bundle.putString("LayoutId","Account");
        fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();

    }

    /**
     * *********ADD ASSETS BUTTON*******************
     */
    public void addAssets(View view) {
        android.support.v4.app.Fragment fragment = new Fragment_finance_recycler();
        Bundle bundle = new Bundle();

        bundle.putString("LayoutId", "Assets");

        fragment.setArguments(bundle);


            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();


    }

    /**
     * *********ADD CREDIT BUTTON*******************
     */
    public void addCredit(View view) {
        android.support.v4.app.Fragment fragment = new Fragment_finance_recycler();
        Bundle bundle = new Bundle();

        bundle.putString("LayoutId", "Account");

        fragment.setArguments(bundle);


            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();




    }

    /**
     * *********ADD STOCKS BUTTON*******************
     */
    public void addStocks(View view) {
        android.support.v4.app.Fragment fragment = new Fragment_finance_recycler();
        Bundle bundle = new Bundle();

        bundle.putString("LayoutId", "Stocks");

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {


        if(this.findViewById(R.id.recyclerView_finance)==null){
            super.onBackPressed();

        }
        //TODO ADD LAYOUTS OF THE OTHER FRAGMENTS
        else {
            android.support.v4.app.Fragment fragment = null;
            fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }
    }

}