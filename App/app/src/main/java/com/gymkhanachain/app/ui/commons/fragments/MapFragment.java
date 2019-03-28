package com.gymkhanachain.app.ui.commons.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gymkhanachain.app.ui.mainscreen.adapters.NearGymkAdapter;
import com.gymkhanachain.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMapFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements NearGymkAdapter.
        NearGymkItem.OnNearGymkItemListener {

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.nearGymkhanas)
    RecyclerView nearGymkanas;

    @BindView(R.id.fabSearch)
    FloatingActionButton fabSearch;

    @BindView(R.id.fabMyLocation)
    FloatingActionButton fabMyLocation;

    @BindView(R.id.fabAccesibility)
    FloatingActionButton fabAccesibility;

    private OnMapFragmentInteractionListener listener;
    private Unbinder unbinder;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Sets the map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Add a marker in Corunna and move the camera
                LatLng corunna = new LatLng(43.365, -8.410);
                googleMap.addMarker(new MarkerOptions().position(corunna).title("A Coruña"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(corunna, 12), 4000, null);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (!marker.isInfoWindowShown()) {
                            marker.showInfoWindow();
                            listener.onMapFragmentInteraction();
                        } else {
                            marker.hideInfoWindow();
                        }

                        return false;
                    }
                });
                googleMap.getUiSettings().setMapToolbarEnabled(false);
            }
        });

        // Sets all fabs
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), "Búsqueda", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        fabAccesibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast newToast = Toast.makeText(getContext(), "Accesibilidad", Toast.LENGTH_SHORT);
                newToast.show();
            }
        });

        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast newToast = Toast.makeText(getContext(), "Localizado", Toast.LENGTH_SHORT);
                newToast.show();
            }
        });

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        nearGymkanas.setHasFixedSize(true);

        nearGymkanas.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        // Specify an adapter (see also next example)
        String[] nearGymkhanas = {"A Coruña - Turismo", "Na procura do tesouro", "Orzán y su bahía",
                "A Coruña Oculta"};
        final NearGymkAdapter adapter = new NearGymkAdapter(nearGymkhanas, this);
        nearGymkanas.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        if (context instanceof OnMapFragmentInteractionListener) {
            listener = (OnMapFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onNearGymkItemClick() {
        listener.onMapFragmentInteraction();
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
    public interface OnMapFragmentInteractionListener {
        void onMapFragmentInteraction();
    }
}
