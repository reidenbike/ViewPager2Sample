package com.example.viewpager2sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentGrid extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private MyRecyclerViewAdapter adapter;
    private String[] data = {"1", "2", "3", "4", "5", "6"};
    private String[] actualData;
    private GridLayoutManager manager;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gridlayout, container, false);

        // data to populate the RecyclerView with
        actualData = data;

        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.rvNumbers);
        int numberOfColumns = 2;

        manager = new GridLayoutManager(getActivity(), numberOfColumns);
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

        SeekBar viewSeekBar = view.findViewById(R.id.viewSeekBar);
        viewSeekBar.setMax(6);
        viewSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (i > 0) {

                    actualData = Arrays.copyOfRange(data, 0, i);

                    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            int spanSize;
                            if (actualData.length == 6) {
                                spanSize = 1;
                            } else if (actualData.length == 5) {
                                if (position == 0) {
                                    spanSize = 2;
                                } else {
                                    spanSize = 1;
                                }
                            } else if (actualData.length == 4) {
                                if (position <= 1) {
                                    spanSize = 2;
                                } else {
                                    spanSize = 1;
                                }
                            } else if (actualData.length == 3) {
                                if (position <= 2) {
                                    spanSize = 2;
                                } else {
                                    spanSize = 1;
                                }
                            } else if (actualData.length == 2) {
                                if (position <= 3) {
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

                    adapter.updateViewNumber(actualData, recyclerView.getMeasuredHeight());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        recyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerViewAdapter(getActivity(), Arrays.copyOfRange(data,0,6), recyclerView.getMeasuredHeight());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {

        if (manager.getChildCount() == 1 && actualData.length > 1){
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize;
                    if (actualData.length == 6) {
                        spanSize = 1;
                    } else if (actualData.length == 5) {
                        if (position == 0) {
                            spanSize = 2;
                        } else {
                            spanSize = 1;
                        }
                    } else if (actualData.length == 4) {
                        if (position <= 1) {
                            spanSize = 2;
                        } else {
                            spanSize = 1;
                        }
                    } else if (actualData.length == 3) {
                        if (position <= 2) {
                            spanSize = 2;
                        } else {
                            spanSize = 1;
                        }
                    } else if (actualData.length == 2) {
                        if (position <= 3) {
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

            adapter.updateViewNumber(actualData, recyclerView.getMeasuredHeight());
        } else if (actualData.length > 1) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 2;
                }
            });

            adapter.updateViewNumber(Arrays.copyOfRange(actualData, position, position + 1), recyclerView.getMeasuredHeight());
        }
    }
}
