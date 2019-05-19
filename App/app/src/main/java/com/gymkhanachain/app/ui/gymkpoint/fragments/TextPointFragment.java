package com.gymkhanachain.app.ui.gymkpoint.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.commons.ProxyBitmap;
import com.gymkhanachain.app.model.beans.TextPointBean;
import com.gymkhanachain.app.model.commons.PointCache;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextPointFragment extends Fragment implements ProxyBitmap.OnProxyBitmapListener {

    private static final String ARG_POINT_ID = "PointId";

    private static final PointCache pointCache = PointCache.getInstance();

    Integer pointId;
    OnTextPointFragmentInteraction listener;
    TextPointBean bean;

    @BindView(R.id.pointImage)
    ImageView ivPointImage;

    @BindView(R.id.pointName)
    TextView tvPointName;

    @BindView(R.id.pointDescriptionTextView)
    TextView tvPointDescription;

    @BindView(R.id.pointAccept)
    Button btnAccept;

    public TextPointFragment() {
        // Required empty public constructor
    }

    /**
     * Crea un fragmento de tipo QuizPointFragment
     *
     * @param pointId Id del punto.
     * @return Nueva instancia de QuizPointFragment.
     */
    public static TextPointFragment newInstance(Integer pointId) {
        TextPointFragment fragment = new TextPointFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POINT_ID, pointId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            pointId = getArguments().getInt(ARG_POINT_ID);
            bean = (TextPointBean) pointCache.getPoint(pointId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_point, container, false);
        ButterKnife.bind(this, view);

        if (bean.getImage().getBitmap() != null) {
            ivPointImage.setImageBitmap(bean.getImage().getBitmap());
        } else {
            bean.getImage().attach(this);
        }

        tvPointName.setText(bean.getName());
        tvPointDescription.setText(bean.getLongDescription());

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickAccept();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTextPointFragmentInteraction) {
            listener = (OnTextPointFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bean.getImage().detach(this);
        listener = null;
    }

    @Override
    public void onBitmapChange(ProxyBitmap bitmap) {
        ivPointImage.setImageBitmap(bitmap.getBitmap());
    }

    public interface OnTextPointFragmentInteraction {
        void onClickAccept();
    }
}
