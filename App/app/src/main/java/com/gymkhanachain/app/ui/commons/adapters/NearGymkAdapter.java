package com.gymkhanachain.app.ui.commons.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gymkhanachain.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearGymkAdapter extends RecyclerView.Adapter<NearGymkAdapter.NearGymkItem> {
    private String[] mDataset;
    private NearGymkItem.OnNearGymkItemListener mListener;

    public static class NearGymkItem extends RecyclerView.ViewHolder {
        @BindView(R.id.near_gymk_image)
        ImageView nearGymkImage;

        @BindView(R.id.near_gymk_name)
        TextView nearGymkText;

        View viewItem;

        public interface OnNearGymkItemListener {
            void onNearGymkItemClick();
        }

        public NearGymkItem(View view) {
            super(view);
            viewItem = view;
            ButterKnife.bind(this, view);
        }

        public void setText(String text) {
            nearGymkText.setText(text);
        }

        public void setImage(Integer resource) {
            nearGymkImage.setImageResource(resource);
        }
    }

    public NearGymkAdapter(String[] myDataset, NearGymkItem.OnNearGymkItemListener myListener) {
        mDataset = myDataset;
        mListener = myListener;
    }

    @Override
    public NearGymkItem onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.near_gymk_item, viewGroup, false);
        return new NearGymkItem(view);
    }

    @Override
    public void onBindViewHolder(final NearGymkItem viewHolder, final int position) {
        viewHolder.setText(mDataset[position]);
        viewHolder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNearGymkItemClick();
            }
        });
        viewHolder.setImage(R.drawable.beach);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
