package com.example.cedric.alibata.Chapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cedric.alibata.R;

import java.util.ArrayList;

/**
 * Created by Cedric on 21 Jan 2018.
 */

public class CustomAdapter extends ArrayAdapter<Announce> {



    int groupid;

    ArrayList<Announce> records;

    Context context;



    public CustomAdapter(Context context, int vg, int id, ArrayList<Announce>
            records) {

        super(context, vg, id, records);

        this.context = context;

        groupid = vg;

        this.records = records;



    }



    public View getView(int position, View convertView, ViewGroup parent) {



        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(groupid, parent, false);

       // TextView textanid = (TextView) itemView.findViewById(R.id.anid);

        //textanid.setText(records.get(position).getpSubject());

        TextView textprofname = (TextView) itemView.findViewById(R.id.profname);

        textprofname.setText(records.get(position).getpProf());

        TextView textsub = (TextView) itemView.findViewById(R.id.subj);

        textsub.setText(records.get(position).getpSubject());

        TextView textmessage = (TextView) itemView.findViewById(R.id.message);

        textmessage.setText(records.get(position).getpMessage());

        TextView textdate = (TextView) itemView.findViewById(R.id.date);

        textdate.setText(records.get(position).getpDate());

        return itemView;

    }

}