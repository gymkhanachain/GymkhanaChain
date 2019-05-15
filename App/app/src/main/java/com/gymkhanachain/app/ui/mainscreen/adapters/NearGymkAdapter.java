package com.gymkhanachain.app.ui.mainscreen.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.commons.GymkhanaCache;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NearGymkAdapter extends RecyclerView.Adapter<NearGymkAdapter.NearGymkItem> {
    private static final GymkhanaCache gymkhanas = GymkhanaCache.getInstance();

    private List<Integer> gymkhanaIds;
    private NearGymkItem.OnNearGymkItemListener listener;

    public static class NearGymkItem extends RecyclerView.ViewHolder {
        @BindView(R.id.near_gymk_image)
        ImageView nearGymkImage;

        @BindView(R.id.near_gymk_name)
        TextView nearGymkText;

        private View viewItem;
        private Unbinder unbinder;

        public interface OnNearGymkItemListener {
            void onNearGymkItemClick();
        }

        NearGymkItem(View view) {
            super(view);
            viewItem = view;
            unbinder = ButterKnife.bind(this, view);
        }

        void setText(String text) {
            nearGymkText.setText(text);
        }

        void setImage(Bitmap bitmap) {
            nearGymkImage.setImageBitmap(bitmap);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            unbinder.unbind();
        }
    }

    public NearGymkAdapter(List<Integer> gymkhanaIds, NearGymkItem.OnNearGymkItemListener listener) {
        this.gymkhanaIds = gymkhanaIds;
        this.listener = listener;

    }

    @Override
    public NearGymkItem onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.near_gymk_item, viewGroup, false);
        return new NearGymkItem(view);
    }

    @Override
    public void onBindViewHolder(final NearGymkItem viewHolder, final int position) {
        GymkhanaBean bean = gymkhanas.getGymkhana(gymkhanaIds.get(position));
        viewHolder.setText(bean.getName());
        viewHolder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNearGymkItemClick();
            }
        });
        //viewHolder.setImage(bean.getImage());
    }

    @Override
    public int getItemCount() {
        return gymkhanaIds.size();
    }
}
