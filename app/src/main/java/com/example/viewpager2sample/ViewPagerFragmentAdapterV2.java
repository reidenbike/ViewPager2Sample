package com.example.viewpager2sample;

import android.util.Log;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapterV2 extends FragmentStateAdapter {

    private int idGen = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Integer> mFragmentIDs = new ArrayList<>();
    private ArrayList<Integer> mFragmentOrderNumber = new ArrayList<>();


    ViewPagerFragmentAdapterV2(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
        /*switch (position) {
            case 1:
                return new FragmentTwo();
            case 2:
                return new FragmentThree();
            default:
                return new FragmentOne();

        }*/
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

    @Override
    public long getItemId(int position) {
        return (long)mFragmentIDs.get(position);
    }

    @Override
    public boolean containsItem(long itemId) {
        return mFragmentOrderNumber.contains((int)itemId);
    }

    void addFragment (Fragment fragment, int fragNumber) {
        int insertPosition = mFragmentOrderNumber.size();
        for (Integer orderNumber : mFragmentOrderNumber){
            //Log.i("OrderNum","To add: " + fragNumber + ", In list: "+orderNumber);
            if (orderNumber > fragNumber){
                insertPosition = mFragmentOrderNumber.indexOf(orderNumber);
                break;
            }
        }

        //Log.i("OrderNum","Insert Position: " + insertPosition);

        mFragments.add(insertPosition,fragment);
        mFragmentOrderNumber.add(insertPosition,fragNumber);
        idGen++;
        mFragmentIDs.add(insertPosition,idGen);
    }

    void removeFragment (int fragNumber){
        if (mFragmentOrderNumber.contains(fragNumber)){
            int index = mFragmentOrderNumber.indexOf(fragNumber);
            mFragments.remove(index);
            mFragmentIDs.remove(index);
            mFragmentOrderNumber.remove(index);
        }
    }

    int getNumberDisplayFragment () {
        int number = 0;
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentGrid) {
                number++;
            }
        }
        return number;
    }

    boolean isEditFragPresent () {
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentAdd) {
                return true;
            }
        }
        return false;
    }

    void updateHeight () {
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentGrid) {
                ((FragmentGrid) fragment).updateHeight();
            }
        }
    }

    void updateDataDisplays (ArrayList<String> dataValues) {
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentGrid) {
                ((FragmentGrid) fragment).updateDataDisplays(dataValues);
            }
        }
    }

    void updateSpecificMetricDisplays (int metric, String dataValue) {
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentGrid) {
                ((FragmentGrid) fragment).updateSpecificMetricDisplays(metric,dataValue);
            }
        }
    }

    void enableAntiBurnMode (boolean antiBurnMode) {
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentGrid) {
                ((FragmentGrid) fragment).enableAntiBurnMode(antiBurnMode);
            }
        }
    }

    void editDisplays(boolean enableEdit, boolean save){
        int fragNum = 0;
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentGrid) {
                fragNum++;
                ((FragmentGrid) fragment).editDisplays(enableEdit, save, fragNum);
            }
        }
    }

    void setEditButton() {
        for (Fragment fragment : mFragments){
            if (fragment instanceof FragmentSettings){
                ((FragmentSettings) fragment).setEditButton();
                break;
            }
        }
    }
}
