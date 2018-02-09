package com.example.cedric.alibata.Chapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cedric.alibata.R;

/**
 * Created by Cedric on 19 Dec 2017.
 */

public class CustomListView extends ArrayAdapter<String> {
    public String[] chapter;
    public String[] description;
    public Integer[] imgid;
    public Activity context;



    public CustomListView(Activity context, String [] chapter, String [] description, Integer [] imgid) {
        super(context,R.layout.lview,chapter);
        this.context=context;
        this.chapter=chapter;
        this.description=description;
        this.imgid=imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        ViewHolder viewHolder = null;
        if(r==null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.lview,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);

        }
        else
        {
            viewHolder=(ViewHolder)r.getTag();

        }
        viewHolder.tv1.setText(chapter[position]);
        viewHolder.tv2.setText(description[position]);
        viewHolder.iv1.setImageResource(imgid[position]);
        return r;

    }
    class ViewHolder{
        TextView tv1;
        TextView tv2;
        ImageView iv1;
        ViewHolder(View v)
        {
            tv1= (TextView)v.findViewById(R.id.tvchapter);
            tv2=(TextView)v.findViewById(R.id.tvdescription);
            iv1=(ImageView)v.findViewById(R.id.imageview);
        }
    }
}
