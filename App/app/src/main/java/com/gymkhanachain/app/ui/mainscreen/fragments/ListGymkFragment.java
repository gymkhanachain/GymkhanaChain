package com.gymkhanachain.app.ui.mainscreen.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gymkhanachain.app.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListGymkFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListGymkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListGymkFragment extends Fragment {

    private OnListGymkFragmentInteractionListener mListener;

    public ListGymkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListGymkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListGymkFragment newInstance() {
        ListGymkFragment fragment = new ListGymkFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_gymk, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCreateGymkInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListGymkFragmentInteractionListener) {
            mListener = (OnListGymkFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListGymkFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_create_gymk);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCreateGymkInteraction();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListGymkFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateGymkInteraction();
    }
}
