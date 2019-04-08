package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gymkhanachain.app.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapFragment extends Fragment implements LocationListener {
    public static final String GYMKHANA_POINTS = "gymkhanaPoints";
    public static final String GIS_POINTS = "gisPoints";
    public static final String NONE = "none";

    private static final String ARG_POINT_TYPE = "pointType";
    private static final String ARG_POINTS = "points";

    @BindView(R.id.map_view)
    MapView mapView;

    @BindView(R.id.fab_search)
    FloatingActionButton fabSearch;

    @BindView(R.id.fab_my_location)
    FloatingActionButton fabMyLocation;

    @BindView(R.id.fab_accesibility)
    FloatingActionButton fabAccesibility;

    private OnMapFragmentInteractionListener listener;
    private Unbinder unbinder;
    private LocationManager locationManager;
    private Location currentLocation = null;
    private GoogleMap map;

    private String pointType;
    private List<MapPoint> points = new ArrayList<>();

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pointType
     *      The point type
     * @param points
     *      The points to show in the map
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String pointType, List<MapPoint> points) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POINT_TYPE, pointType);
        args.putParcelable(ARG_POINTS, Parcels.wrap(points));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pointType = getArguments().getString(ARG_POINT_TYPE);
            points = Parcels.unwrap(getArguments().getParcelable(ARG_POINTS));
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Get current location
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 10, this);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Sets the map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                // Move the camera to Coruña
                LatLng corunna = new LatLng(43.365, -8.410);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(corunna, 12));

                // Set marks in map
                for (MapPoint point: points) {
                    MarkerOptions opts = new MarkerOptions().position(point.getPosition()).
                            title(point.getName()).icon(BitmapDescriptorFactory.
                            defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    Marker marker = map.addMarker(opts);
                    marker.setTag(point);
                }

                // Set on marker clicked
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
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

                // Active configurations
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.
                        ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }

                map.getUiSettings().setMapToolbarEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
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
                if (currentLocation != null) {
                    LatLng latLng = new LatLng(getLatitude(), getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng), 1000, null);
                }
                String position = String.format("Localizado: (%.2f, %.2f)", getLatitude(),
                        getLongitude());
                Toast newToast = Toast.makeText(getContext(), position, Toast.LENGTH_SHORT);
                newToast.show();
            }
        });

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

    public double getLatitude() {
        if (currentLocation != null) {
            return currentLocation.getLatitude();
        }

        return 0.0;
    }

    public double getLongitude() {
        if (currentLocation != null) {
            return currentLocation.getLongitude();
        }

        return 0.0;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
