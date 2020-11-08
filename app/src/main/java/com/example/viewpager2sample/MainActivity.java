package com.example.viewpager2sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ViewPager2 myViewPager2;
    ViewPagerFragmentAdapter myAdapter;
    private ArrayList<Fragment> arrayList = new ArrayList<>();
    TextView txtGpsStatus;

    Switch switchOne, switchTwo, switchThree;

    //Simulate GPS callback / ride calculations:
    private int mInterval = 1000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myViewPager2 = findViewById(R.id.viewpager);

        // add Fragments in your ViewPagerFragmentAdapter class

        myAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        myViewPager2.setAdapter(myAdapter);

        switchOne = findViewById(R.id.switchOne);
        switchOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    myAdapter.addFragment(new FragmentGrid(),1);
                    myAdapter.notifyDataSetChanged();
                } else {
                    myAdapter.removeFragment(1);
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
                    /*myAdapter.addFragment(new FragmentGrid(),3);
                    myAdapter.notifyDataSetChanged();*/

                    setAlertVisibility(true);
                } else {
                    /*myAdapter.removeFragment(3);
                    myAdapter.notifyDataSetChanged();*/

                    setAlertVisibility(false);
                }
            }
        });

        txtGpsStatus = findViewById(R.id.txtGpsStatus);

        //Simulate GPS callbacks:
        mHandler = new Handler();
        startRepeatingTask();
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

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
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
                    myAdapter.updateHeight(myViewPager2.getMeasuredHeight());
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
}
