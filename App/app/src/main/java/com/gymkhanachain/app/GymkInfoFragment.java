package com.gymkhanachain.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GymkInfoFragment.OnGymkInfoFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GymkInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GymkInfoFragment extends Fragment implements OnClickListener {

    // TODO: añadir interacción del mapa (MapView, onCreateView, etc)
    // private MapView mMapView;

    Button buttonActivate;
    Button buttonSave;
    Button buttonReport;
    private OnGymkInfoFragmentInteractionListener mListener;

    public GymkInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GymkInfoFragment.
     */
    public static GymkInfoFragment newInstance() {
        GymkInfoFragment fragment = new GymkInfoFragment();
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
        return inflater.inflate(R.layout.fragment_gymk_info, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGymkInfoFragmentInteractionListener) {
            mListener = (OnGymkInfoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGymkInfoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        // mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        // mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        // mMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            buttonActivate = getActivity().findViewById(R.id.buttonActivate);
            buttonSave = getActivity().findViewById(R.id.buttonSave);
            buttonReport = getActivity().findViewById(R.id.buttonReport);
        } catch (NullPointerException e){

        }
        buttonActivate.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonReport.setOnClickListener(this);
        ImageButton imageButton = getActivity().findViewById(R.id.imageButtonGymkInfo);
        imageButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonSave: // Guardar
                Toast.makeText(getContext(), "Gymkhana guardada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonReport: // Reportar
                Toast.makeText(getContext(), "Gymkhana reportada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonActivate: // Activar
                Toast.makeText(getContext(), "Gymkhana activada", Toast.LENGTH_SHORT).show();
                break;
        }
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
    public interface OnGymkInfoFragmentInteractionListener {
        // TODO: Update argument type and name
        void onGymkInfoFragmentInteraction(Uri uri);
    }
}
