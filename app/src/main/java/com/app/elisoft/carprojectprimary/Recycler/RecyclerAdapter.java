package com.app.elisoft.carprojectprimary.Recycler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.elisoft.carprojectprimary.R;
import com.app.elisoft.carprojectprimary.Utils.Helper;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static String TAG = "MyRecyclerAdapter";
    private Context context;

    public ArrayList<SignItem> signsList;

    RecyclerCallback recyclerCallback;

    public RecyclerAdapter(Context context, ArrayList<SignItem> list) {
        this.context = context;
        this.signsList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_grid, null);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder()");

        holder.signImage.setImageBitmap(Helper.decodeSampledBitmapFromResource(context.getResources(), signsList.get(position).getImage(), 200, 200));
//        holder.signImage.setImageResource(signsList.get(position).getImage());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onImageItemClick(View view, int position) {
                //send message hare
                recyclerCallback.sendActionToProjector(signsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return signsList.size();
    }

    public void setItems(ArrayList<SignItem> list){
        this.signsList = list;
    }

    public void setRecyclerCallback(RecyclerCallback callback) {
        recyclerCallback = callback;
    }
}
