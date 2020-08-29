package com.example.doda.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doda.activities.DrawingDetailsAvtivity;
import com.example.doda.R;
import com.example.doda.model.Drawing;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawingAdapter extends RecyclerView.Adapter<DrawingAdapter.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Drawing> drawingList;

    public DrawingAdapter(Context context, List<Drawing> drawingList) {
        this.layoutInflater = layoutInflater.from(context);
        this.drawingList = drawingList;
    }

    @NonNull
    @Override
    public DrawingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_drawing_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(layoutInflater.getContext()).asBitmap().load(drawingList.get(position).getImgUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.drawingImg.setImageBitmap(resource);
            }
        });
        holder.drawingTitle.setText(drawingList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return drawingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.newsImg)
        ImageView drawingImg;
        @BindView(R.id.newsTitle)
        TextView drawingTitle;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(layoutInflater.getContext(), DrawingDetailsAvtivity.class);
                intent.putExtra("imhUrl", drawingList.get(getAdapterPosition()).getImgUrl());
                intent.putExtra("title", drawingList.get(getAdapterPosition()).getName());
                intent.putExtra("id", drawingList.get(getAdapterPosition()).getId());
                intent.putExtra("date", drawingList.get(getAdapterPosition()).getDate());
                intent.putExtra("time", drawingList.get(getAdapterPosition()).getTime());
                intent.putExtra("pinCount", drawingList.get(getAdapterPosition()).getTotalMarkers());
                layoutInflater.getContext().startActivity(intent);
            });
        }
    }
}
