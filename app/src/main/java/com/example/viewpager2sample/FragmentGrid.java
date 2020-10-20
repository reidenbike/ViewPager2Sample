package com.example.viewpager2sample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentGrid extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gridlayout, container, false);

        // data to populate the RecyclerView with
        final String[] data = {"1", "2", "3", "4", "5"/*, "6"*/};

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rvNumbers);
        int numberOfColumns = 2;

        GridLayoutManager manager = new GridLayoutManager(getActivity(), numberOfColumns);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize;
                if (data.length == 6){
                    spanSize = 1;
                } else if (data.length == 5){
                    if (position == 0){
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (data.length == 4){
                    if (position <= 1){
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (data.length == 3){
                    if (position <= 2){
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (data.length == 2){
                    if (position <= 3){
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else {
                    spanSize = 2;
                }

                return spanSize;
            }
        });

        recyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerViewAdapter(getActivity(), data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }
}
