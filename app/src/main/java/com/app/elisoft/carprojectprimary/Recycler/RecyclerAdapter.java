package com.app.elisoft.carprojectprimary.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.elisoft.carprojectprimary.R;

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

//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) >= reqHeight
//                    && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
//
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder()");

//        holder.signImage.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), signsList.get(position).getImage(), R.dimen.signs_grid_size, R.dimen.signs_grid_size));
        holder.signImage.setImageResource(signsList.get(position).getImage());

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
