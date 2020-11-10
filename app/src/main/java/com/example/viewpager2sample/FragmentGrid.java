package com.example.viewpager2sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentGrid extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private MyRecyclerViewAdapter adapter;
    private GridLayoutManager manager;
    private RecyclerView recyclerView;
    private SeekBar viewSeekBar;

    private int numberOfDisplays = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gridlayout, container, false);

        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.rvNumbers);

        final int numberOfColumns = 2;


        manager = new GridLayoutManager(getActivity(), numberOfColumns) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        setSpanSize();

        viewSeekBar = view.findViewById(R.id.viewSeekBar);
        viewSeekBar.setMax(10);
        viewSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                numberOfDisplays = i;

                if (i > 0) {

                    setSpanSize();

                    adapter.updateViewNumber(numberOfDisplays, recyclerView.getMeasuredHeight());
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
        adapter = new MyRecyclerViewAdapter(getActivity(), 5, recyclerView.getMeasuredHeight());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position, boolean antiBurnMode) {
        if (antiBurnMode){
            ((MainActivity) Objects.requireNonNull(getActivity())).enableAntiScreenBurnMode(false);
        } else {
            maximizeView(position);
        }
    }

    void maximizeView (int position) {
        if (manager.getChildCount() == 1 && numberOfDisplays > 1){
            setSpanSize();
            adapter.maximizeView(false,false, position, recyclerView.getMeasuredHeight());
        } else if (numberOfDisplays > 1) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 2;
                }
            });

            adapter.maximizeView(false,true, position, recyclerView.getMeasuredHeight());
        }
    }

    void updateHeight (/*int viewPagerHeight*/) {
        if (recyclerView != null && getContext() != null) {
            adapter.updateHeight(recyclerView.getMeasuredHeight()/*(int) (viewPagerHeight - (viewSeekBar.getMeasuredHeight() + 2 * (getContext().getResources().getDimension(R.dimen.grid_padding))))*/);
        }
    }

    void updateDataDisplays (ArrayList<String> dataValues) {
        if (adapter != null && dataValues != null) {
            adapter.updateDataDisplays(dataValues);
        }
    }

    void enableAntiBurnMode (boolean antiBurnMode) {
        if (antiBurnMode) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 2;
                }
            });
            adapter.maximizeView(true, true, 0, recyclerView.getMeasuredHeight());
        } else {
            maximizeView(0);
        }
    }

    void setSpanSize () {
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize;
                if (numberOfDisplays == 10) {
                    spanSize = 1;
                } else if (numberOfDisplays == 9) {
                    if (position == 0) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 8) {
                    spanSize = 1;
                } else if (numberOfDisplays == 7) {
                    if (position == 0) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 6) {
                    spanSize = 1;
                } else if (numberOfDisplays == 5) {
                    if (position == 0) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 4) {
                    if (position <= 1) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 3) {
                    if (position <= 2) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 2) {
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
    }

    void editDisplays (final boolean enableEdit) {
        if (enableEdit) {
            viewSeekBar.setVisibility(View.VISIBLE);
        } else {
            viewSeekBar.setVisibility(View.GONE);
        }

        adapter.editDisplays(enableEdit);

        viewSeekBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Log.i("TAG","updating height to " + "Visible = " + visible);
                if (enableEdit ? viewSeekBar.getVisibility() == View.VISIBLE : viewSeekBar.getVisibility() == View.GONE) {
                    updateHeight();
                    viewSeekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }
}
