package com.app.elisoft.carprojectprimary.Recycler;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.elisoft.carprojectprimary.R;


public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "ViewHolder";

    ImageView signImage;

    ItemClickListener itemClickListener;

    public ViewHolder(View itemView) {
        super(itemView);
        signImage = (ImageView) itemView.findViewById(R.id.sign_image);
        signImage.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.sign_image) {
            Log.d(TAG, "Image clicked :)");
            itemClickListener.onImageItemClick(view, getLayoutPosition());
        }

    }


}
