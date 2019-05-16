package com.gymkhanachain.app.ui.mainscreen.fragments;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.commons.ProxyBitmap;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.commons.GymkhanaCache;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GymkInfoFragment.OnGymkInfoFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GymkInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GymkInfoFragment extends Fragment implements OnClickListener, ProxyBitmap.OnProxyBitmapListener {

    private static final GymkhanaCache gymkCache = GymkhanaCache.getInstance();

    private static final String ARG_GYMKHANA_ID = "GymkhanaId";

    // TODO: añadir interacción del mapa (MapView, onCreateView, etc)
    // private MapView mMapView;

    Button buttonActivate;
    Button buttonSave;
    Button buttonReport;
    TextView textViewNombre;
    TextView textViewDetalles;
    ImageButton imageButton;
    private OnGymkInfoFragmentInteractionListener mListener;
    private Integer gymkhanaId;

    public GymkInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GymkInfoFragment.
     */
    public static GymkInfoFragment newInstance(Integer gymkId) {
        GymkInfoFragment fragment = new GymkInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GYMKHANA_ID, gymkId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gymkhanaId = getArguments().getInt(ARG_GYMKHANA_ID);
        }
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
        GymkhanaBean bean = gymkCache.getGymkhana(gymkhanaId);
        bean.getImage().detach(this);
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
            textViewNombre = getActivity().findViewById(R.id.textView_nombre);
            textViewDetalles = getActivity().findViewById(R.id.textView_Detalles);
        } catch (NullPointerException e) { }

        GymkhanaBean bean = gymkCache.getGymkhana(gymkhanaId);

        textViewNombre.setText(bean.getName());
        textViewDetalles.setText(bean.getDescription());
        buttonActivate.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonReport.setOnClickListener(this);
        imageButton = getActivity().findViewById(R.id.imageButtonGymkInfo);

        if (bean.getImage().getBitmap() != null) {
            imageButton.setImageBitmap(bean.getImage().getBitmap());
        } else {
            bean.getImage().attach(this);
        }

        imageButton.setImageBitmap(bean.getImage().getBitmap());
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave: // Guardar
                Toast.makeText(getContext(), "GymkhanaBean guardada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonReport: // Reportar
                Toast.makeText(getContext(), "GymkhanaBean reportada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonActivate: // Activar
                mListener.onGymkhanaActivate(gymkhanaId);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBitmapChange(ProxyBitmap bitmap) {
        imageButton.setImageBitmap(bitmap.getBitmap());
        bitmap.detach(this);
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
        void onGymkhanaActivate(Integer gymkhanaId);
    }
}
