package com.gymkhanachain.app.ui.gymkpoint.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.model.beans.PointBean;
import com.gymkhanachain.app.model.commons.PointCache;

public class TextPointFragment extends Fragment {

    private static final String ARG_POINT_ID = "PointId";

    private static final PointCache pointCache = PointCache.getInstance();

    Integer pointId;
    OnTextPointFragmentInteraction listener;
    PointBean bean;

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
            bean = pointCache.getPoint(pointId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_point, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
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
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTextPointFragmentInteraction {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
