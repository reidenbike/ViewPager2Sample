package com.example.viewpager2sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.Objects;

import androidx.fragment.app.Fragment;

public class FragmentSettings extends Fragment {

    private ToggleButton buttonEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_frag, container, false);

        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).enterEditMode(b, true);
                }
            }
        });

        return view;
    }

    void setEditButton() {
        buttonEdit.setChecked(false);
    }
}
