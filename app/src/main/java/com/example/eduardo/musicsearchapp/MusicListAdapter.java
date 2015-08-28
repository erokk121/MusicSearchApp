package com.example.eduardo.musicsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Eduardo on 8/27/2015.
 *
 * This is the adapter used to populate the list view, once the retrieval of the data from the
 * iTunes API is complete.
 */
public class MusicListAdapter extends BaseAdapter {

    Context context;
    ArrayList<MusicItem> list;
    LayoutInflater inflater;

    public MusicListAdapter(Context context, ArrayList<MusicItem> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MusicItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    //It is important to follow best practices when implementing the functionality of the list view.
    //In particular getView() needs to be conservative with view inflations by utilizing convertView,
    //and also needs to use the ViewHolder pattern to prevent needless findViewById() calls.

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.track = (TextView) convertView.findViewById(R.id.track);
            holder.artist = (TextView) convertView.findViewById(R.id.artist);
            holder.album = (TextView) convertView.findViewById(R.id.album);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.track.setText(list.get(position).track);
        holder.artist.setText(list.get(position).artist);
        holder.album.setText(list.get(position).album);

        //Using the Picasso library makes loading the images much easier

        Picasso.with(context).load(list.get(position).imageUrl).into(holder.image);

        return convertView;
    }
    private static class ViewHolder {
        public ImageView image;
        public TextView track;
        public TextView artist;
        public TextView album;
    }
}
