package com.example.agorjux.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agorjux.moveyourbody.R;

import java.util.List;

/**
 * Created by arthur on 21/08/15.
 */
public class EventsJoinAdapter extends BaseAdapter {
    private Context mContext;
    private List<EventsModel> events;

    private LayoutInflater mInflater;


    public EventsJoinAdapter(Context context, List<EventsModel> aListP) {
        mContext = context;
        events = aListP;
        mInflater = LayoutInflater.from(mContext);
    }


    public int getCount() {
        return events.size();
    }


    public Object getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return events.get(position).id;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.item_list_events, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView title = (TextView)layoutItem.findViewById(R.id.title_event);

        title.setText(events.get(position).title);

        // todo ajouter les couleurs pour tous les sports
        switch(events.get(position).sport){
            case "Tennis":
                title.setBackgroundColor(mContext.getResources().getColor(R.color.google_login_button));
                break;
        }

        return layoutItem;
    }




}
