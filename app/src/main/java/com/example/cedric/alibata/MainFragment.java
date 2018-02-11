package com.example.cedric.alibata;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cedric.alibata.Chapters.Chapterone;
import com.example.cedric.alibata.Chapters.Chapterthree;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    LinearLayout item1,item2,item3,item4,item5;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        item1 = (LinearLayout) getActivity().findViewById(R.id.item1);
        item2 = (LinearLayout) getActivity().findViewById(R.id.item2);
        item3 = (LinearLayout) getActivity().findViewById(R.id.item3);
        item4 = (LinearLayout) getActivity().findViewById(R.id.item4);
        item5 = (LinearLayout) getActivity().findViewById(R.id.item5);

        //introduction
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Introduction.class));
            }
        });
        //chapter1
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Chapterone.class));
            }
        });
        //chapter2
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Chaptertwo.class));
            }
        });
        //chapter3
        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Chapterthree.class));
            }
        });
        //chapter4
        item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
