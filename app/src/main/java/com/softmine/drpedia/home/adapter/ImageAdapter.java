package com.softmine.drpedia.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.softmine.drpedia.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>{

    private ArrayList<String> mImagesList;
    private Context mContext;
    private SparseBooleanArray mSparseBooleanArray;

    public ImageAdapter(Context context, ArrayList<String> imageList) {
        mContext = context;
        mSparseBooleanArray = new SparseBooleanArray();
        mImagesList = new ArrayList<String>();
        this.mImagesList = imageList;

    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for(int i=0;i<mImagesList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(mImagesList.get(i));
            }
        }

        return mTempArry;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
        }
    };

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_multiphoto_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        String imageUrl = mImagesList.get(position);

        Glide.with(mContext)
                .load("file://"+imageUrl)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d("loadedimage","false");
                        holder.loadImage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("loadedimage","readey");
                        holder.loadImage.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
       // holder.imageView.setTag(position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if(mSparseBooleanArray.get(holder.getAdapterPosition()))
                {
                    mSparseBooleanArray.put((Integer) holder.imageView.getTag(),false);
                }
                else
                {
                    mSparseBooleanArray.put((Integer) holder.imageView.getTag(),false);
                }*/
                holder.checkBox.toggle();

            }
        });
        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(mSparseBooleanArray.get(position));
        holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);

    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;
        public ImageView imageView;
        public ImageView loadImage;

        public MyViewHolder(View view) {
            super(view);

            checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
            imageView = (ImageView) view.findViewById(R.id.imageView1);
            loadImage = (ImageView) view.findViewById(R.id.loading);
        }
    }
}
