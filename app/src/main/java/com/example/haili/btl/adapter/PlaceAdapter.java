package com.example.haili.btl.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haili.btl.R;
import com.example.haili.btl.model.Place;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends BaseAdapter {

    private Context context;
    private List<Place> placeList;

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }

    public void updateList(List<Place> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_place, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Place place = placeList.get(position);
        viewHolder.txtPlaceName.setText(place.getName());
        viewHolder.txtPlaceAddress.setText(place.getAddress());
        viewHolder.txtPlaceDes.setText(place.getDescription());
        Bitmap placeImage = BitmapFactory.decodeByteArray(place.getImage(), 0, place.getImage().length);
        viewHolder.ivPlaceItem.setImageBitmap(placeImage);

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.iv_place_item)
        ImageView ivPlaceItem;
        @BindView(R.id.txt_place_name)
        TextView txtPlaceName;
        @BindView(R.id.txt_place_address)
        TextView txtPlaceAddress;
        @BindView(R.id.txt_place_des)
        TextView txtPlaceDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
