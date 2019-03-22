package com.gymkhanachain.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NearGymkAdapter extends RecyclerView.Adapter<NearGymkAdapter.NearGymkItem> {
    private String[] mDataset;

    public static class NearGymkItem extends RecyclerView.ViewHolder {
        public View mView;
        public Context mContext;

        public NearGymkItem(final View view, final Context context) {
            super(view);
            mView = view;
            mContext = context;
        }
    }

    public NearGymkAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public NearGymkItem onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.near_gymk_item,
                viewGroup, false);
        return new NearGymkItem(view, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(final NearGymkItem viewHolder, final int position) {
        final TextView textView = viewHolder.mView.findViewById(R.id.near_gymk_name);
        textView.setText(mDataset[position]);
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Toast toast = Toast.makeText(viewHolder.mContext, mDataset[position], Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        final ImageView imageView = viewHolder.mView.findViewById(R.id.near_gymk_image);
        imageView.setImageResource(R.drawable.beach);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
