package com.example.viewpager2sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager2 myViewPager2;
    ViewPagerFragmentAdapter myAdapter;
    private ArrayList<Fragment> arrayList = new ArrayList<>();

    Switch switchOne, switchTwo, switchThree;

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
                    myAdapter.addFragment(new FragmentTwo(),2);
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
                    myAdapter.addFragment(new FragmentThree(),3);
                    myAdapter.notifyDataSetChanged();
                } else {
                    myAdapter.removeFragment(3);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
