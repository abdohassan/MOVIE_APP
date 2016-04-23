package com.example.engab.movie_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.engab.movie_app.R;
import com.example.engab.movie_app.Review;

import java.util.List;

/**
 * Created by engab on 22-Apr-16.
 */
public class ReviewsAdapter extends BaseAdapter {

    private List<Review> reviews;
    private Context context;
    private LayoutInflater inflater;

    public ReviewsAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.review_item, parent, false);
            holder = new ViewHolder();
            holder.revName = (TextView) convertView.findViewById(R.id.revName);
            holder.revContent = (TextView) convertView.findViewById(R.id.revContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Review review = reviews.get(position);
        holder.revName.setText(review.getName());
        holder.revContent.setText(review.getComment());
        return convertView;
    }

    private class ViewHolder {
        TextView revName;
        TextView revContent;
    }
}
