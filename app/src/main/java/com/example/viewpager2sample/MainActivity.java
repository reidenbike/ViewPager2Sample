package com.example.viewpager2sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    ViewPager2 myViewPager2;
    ViewPagerFragmentAdapter myAdapter;
    TextView txtGpsStatus;
    int numberFragments;
    int maxFragNumber = 5;

    //Switch switchOne, switchTwo, switchThree;
    private LinearLayout switchLayout;
    private ImageButton buttonDone;
    private ImageButton buttonCancel;

    //Simulate GPS callback / ride calculations:
    private int mInterval = 2000;
    private int mIntervalIndependent = 1000;
    private Handler mHandler;
    private Handler mHandlerIndependent;

    //Screen burn timer
    private CountDownTimer antiScreenBurnTimer;
    private boolean antiBurnMode;

    private boolean metric = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mPrefs = getSharedPreferences(Metrics.DISPLAY_PREFS,MODE_PRIVATE);
        numberFragments = mPrefs.getInt(Metrics.NUMBER_FRAGS,2);

        myViewPager2 = findViewById(R.id.viewpager);

        // add Fragments in your ViewPagerFragmentAdapter class

        myAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        myViewPager2.setAdapter(myAdapter);

        myViewPager2.setOffscreenPageLimit(8);

        myAdapter.addFragment(new FragmentSettings(),0);
        addFragment(numberFragments,false);

        myViewPager2.setCurrentItem(1,false);

        switchLayout = findViewById(R.id.switchLayout);

        buttonDone = findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterEditMode(false,true);
            }
        });

        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterEditMode(false,false);
            }
        });

/*        switchOne = findViewById(R.id.switchOne);
        switchOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //myAdapter.addFragment(new FragmentGrid(),1);
                    myAdapter.editDisplays(true);
                    if (myAdapter.getItemCount() < 5) {
                        myAdapter.addFragment(new FragmentAdd(), 100);
                    }
                    myAdapter.notifyDataSetChanged();
                } else {
                    //myAdapter.removeFragment(1);
                    myAdapter.editDisplays(false);
                    myAdapter.removeFragment(100);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        switchTwo = findViewById(R.id.switchTwo);
        switchTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    myAdapter.addFragment(new FragmentGrid(),2);
                    myAdapter.notifyDataSetChanged();
                } else {
                    myAdapter.removeFragment(2);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        switchThree = findViewById(R.id.switchThree);
        switchThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    *//*myAdapter.addFragment(new FragmentGrid(),3);
                    myAdapter.notifyDataSetChanged();*//*

                    setAlertVisibility(true);
                } else {
                    *//*myAdapter.removeFragment(3);
                    myAdapter.notifyDataSetChanged();*//*

                    setAlertVisibility(false);
                }
            }
        });*/

        txtGpsStatus = findViewById(R.id.txtGpsStatus);

        //Simulate GPS callbacks:
        mHandler = new Handler();
        mHandlerIndependent = new Handler();
        startRepeatingTask();

        boolean screenOnStatus = true;
        long screenBurnTimerLength = (screenOnStatus) ? 60000 : 120000;

        antiScreenBurnTimer = new CountDownTimer(screenBurnTimerLength,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                //Log.i("Screen","Timer Finished");
                try {
                    enableAntiScreenBurnMode(true);
                } catch (Exception e){
                    //Log.i("Exception", "Anti Screen Burn Mode Timer onFinish failure: " + e);
                }

            }
        };
        antiScreenBurnTimer.start();
    }

    public void enableAntiScreenBurnMode(boolean antiBurnMode){
        this.antiBurnMode = antiBurnMode;
        myAdapter.enableAntiBurnMode(antiBurnMode);
    }

    @Override
    public void onUserInteraction(){
        antiScreenBurnTimer.cancel();
        antiScreenBurnTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateDataDisplays(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    Runnable independentDataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                updateSpecificMetricDisplays(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandlerIndependent.postDelayed(independentDataRunnable, mIntervalIndependent);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
        independentDataRunnable.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
        mHandlerIndependent.removeCallbacks(independentDataRunnable);
    }

    void setAlertVisibility (final boolean visible) {
        if (visible) {
            txtGpsStatus.setVisibility(View.VISIBLE);
        } else {
            txtGpsStatus.setVisibility(View.GONE);
        }
        txtGpsStatus.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Log.i("TAG","updating height to " + "Visible = " + visible);
                if (visible ? txtGpsStatus.getVisibility() == View.VISIBLE : txtGpsStatus.getVisibility() == View.GONE) {
                    myAdapter.updateHeight();
                    txtGpsStatus.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    void updateDataDisplays () {
        ArrayList<String> dataValues = new ArrayList<>();

        for (int i = 0; i <= 20; i++){
            dataValues.add(String.valueOf(new Random().nextInt(10) + (i*10)));
        }

        if (dataValues.size() > 0) {
            myAdapter.updateDataDisplays(dataValues);
        }
    }

    void updateSpecificMetricDisplays () {
        myAdapter.updateSpecificMetricDisplays(7,String.valueOf(new Random().nextInt(10) + (7*10))); //Metric 7 == Power value
    }

    void enterEditMode (boolean edit, boolean save) {
        if (edit){
            switchLayout.setVisibility(View.VISIBLE);
            if (antiBurnMode) {
                enableAntiScreenBurnMode(false);
            }
            myAdapter.editDisplays(true, save);
            if (myAdapter.getItemCount() < 6) {
                myAdapter.addFragment(new FragmentAdd(), 100);
            }
            myAdapter.notifyDataSetChanged();
            myViewPager2.setCurrentItem(1,true);
        } else {
            switchLayout.setVisibility(View.GONE);
            myAdapter.setEditButton();
            myAdapter.editDisplays(false, save);
            myAdapter.removeFragment(100);

            if (save){
                saveFragNumber();
            } else {
                int numberDisplayFrags = myAdapter.getNumberDisplayFragment();
                if (numberFragments > numberDisplayFrags){
                    addFragment(numberFragments - numberDisplayFrags, false);
                } else if (numberFragments < numberDisplayFrags){
                    do {
                        myAdapter.removeFragment(myAdapter.getItemCount());
                    } while (numberFragments < myAdapter.getNumberDisplayFragment());
                }
            }

            myAdapter.notifyDataSetChanged();
        }
    }

    void addFragment (int numberFragmentsToAdd, boolean editMode) {
        for (int i = 0; i < numberFragmentsToAdd; i++) {
            int numberDisplayFragments = myAdapter.getNumberDisplayFragment();
            if (numberDisplayFragments < maxFragNumber) {
                myAdapter.addFragment(FragmentGrid.newInstance(myAdapter.getItemCount(),editMode,metric), myAdapter.getItemCount());

                if (numberDisplayFragments >= maxFragNumber - 1) {
                    myAdapter.removeFragment(100);
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    void deleteFragment (int fragNumber) {
        myAdapter.removeFragment(fragNumber);
        if (!myAdapter.isEditFragPresent()) {
            myAdapter.addFragment(new FragmentAdd(), 100);
        }
        myAdapter.notifyDataSetChanged();
    }

    void saveFragNumber () {
        numberFragments = myAdapter.getNumberDisplayFragment();

        SharedPreferences mPrefs = getSharedPreferences(Metrics.DISPLAY_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putInt(Metrics.NUMBER_FRAGS, numberFragments);
        prefsEditor.apply();
    }
}
