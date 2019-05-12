package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.request.DirectionDestinationRequest;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gymkhanachain.app.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";

    private static final String ARG_PARAMS = "Params";
    private static final String ARG_POINTS = "Points";

    @BindView(R.id.map_view)
    MapView mapView;

    @BindView(R.id.fab_search)
    FloatingActionButton fabSearch;

    @BindView(R.id.fab_my_location)
    FloatingActionButton fabMyLocation;

    @BindView(R.id.fab_accesibility)
    FloatingActionButton fabAccesibility;

    private MapFragmentParams params;
    private OnMapFragmentInteractionListener listener;
    private Unbinder unbinder;
    private Location currentLocation = null;
    private FusedLocationProviderClient fusedLocationClient = null;
    private LocationRequest locationRequest = null;
    private LocationCallback locationCallback = null;
    private GoogleMap map;
    private AtomicBoolean isDragPoint = new AtomicBoolean(false);
    private List<MapPoint> points = new ArrayList<>();

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Crea un nuevo MapFragment
     *
     * @param params
     *      Un MapFragmentParams para configurar el fragmento
     * @param points
     *      Los puntos que se mostrarán en el mapa
     */
    public static MapFragment newInstance(MapFragmentParams params, List<MapPoint> points) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAMS, Parcels.wrap(params));
        args.putParcelable(ARG_POINTS, Parcels.wrap(points));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            params = Parcels.unwrap(getArguments().getParcelable(ARG_PARAMS));
            points = Parcels.unwrap(getArguments().getParcelable(ARG_POINTS));
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Sets the map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Sets all fabs listener
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
                Toast newToast = Toast.makeText(getContext(), "Accesibilidad",
                        Toast.LENGTH_SHORT);
                newToast.show();
            }
        });

        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng position;

                if ((params.getMapMode() == MapMode.EDIT_MODE) && !points.isEmpty()) {
                    position = points.get(0).position;
                } else {
                    position = new LatLng(getLatitude(), getLongitude());
                }

                map.animateCamera(CameraUpdateFactory.newLatLng(position), 1000,
                        null);
                Toast toast = Toast.makeText(getContext(), "Location: (" + position.latitude + ", " + position.longitude + ")", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return view;
    }

    /**
     * Añade un nuevo punto al mapa
     * @param point Punto para añadir
     */
    public void addPoint(MapPoint point) {
        points.add(point);
        drawMark(point);
    }

    /**
     * Elimina un punto del mapa
     * @param point Punto a eliminar
     */
    public void removePoint(MapPoint point) {
        points.remove(point);
        // Actualizamos el mapa
        drawMap();
    }

    /**
     * Establece el intervalo de actualización del mapa
     * @param locationInterval Intervalo de actualización del mapa en segundos
     */
    public void setLocationInterval(float locationInterval) {
        if (params != null) {
            params.setLocationInterval(locationInterval);
        }

        // Reiniciamos el servicio
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

        startLocationUpdates();
    }

    /**
     * Establece el estilo del mapa
     * @param style Un objeto del tipo MapStyleOptions
     */
    public void setMapStyle(MapStyleOptions style) {
        if (params != null) {
            params.setStyle(style);
        }

        if (map != null) {
            map.setMapStyle(style);
        }
    }

    /**
     * Establece el tipo del mapa
     * @param mapType Una constante del tipo GoogleMaps
     */
    public void setMapType(int mapType) {
        if (params != null) {
            params.setMapType(mapType);
        }

        if (map != null) {
            map.setMapType(mapType);
        }
    }

    private void drawPath(Direction direction) {
        @ColorInt int color = getResources().getColor(R.color.colorRoute);

        Route route = direction.getRouteList().get(0);

        for (Leg leg : route.getLegList()) {
            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(),
                    directionPositionList, 5, color);
            map.addPolyline(polylineOptions);
        }
    }

    private void drawMark(MapPoint point) {
        // Get background
        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.ic_map_marker);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background
                .getIntrinsicHeight());

        // Get logo
        Drawable logo = ContextCompat.getDrawable(getContext(), R.drawable.ic_logo_white);
        int size = Math.round(background.getIntrinsicWidth()*340.0f/512.0f);
        int xPos = Math.round(background.getIntrinsicWidth()*86.0f/512.0f);
        int yPos = Math.round(background.getIntrinsicWidth()*40.0f/512.0f);
        logo.setBounds(xPos, yPos, xPos+size, yPos+size);

        // Draw marker
        Drawable wrapped = null;

        // Get color of background
        if (params.getTypePoints() == PointType.GYMKHANA_POINTS) {
            @ColorInt int color = getResources().getColor(R.color.colorGymkhana);
            wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, color);
        } else {
            @ColorInt int color = getResources().getColor(R.color.colorGis);
            wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, color);
        }

        // If drawable exists, draw marker
        if (wrapped != null) {
            // Create icon mark
            Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background
                    .getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            wrapped.draw(canvas);
            logo.draw(canvas);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);

            // Set all marker options
            MarkerOptions opts = new MarkerOptions()
                    .position(point.getPosition())
                    .icon(icon);

            if (params.getMapMode() == MapMode.EDIT_MODE) {
                opts.draggable(true);
            }

            Marker marker = map.addMarker(opts);
            marker.setTag(point);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        // Comprobamos los permisos de localización
        if (isLocationRequestPermission() && (params.getMapMode() != MapMode.EDIT_MODE)) {
            // Creamos el callback de localización
            if (locationCallback == null) {
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // Si se está arrastrando un punto, no actualizamos la posición actual
                        if (!isDragPoint.get()) {
                            if (locationResult == null) {
                                return;
                            }

                            currentLocation = locationResult.getLastLocation();
                            Log.i(TAG, "Current location: " + currentLocation);

                            if (map != null) {
                                drawRoute();
                            }
                        }
                    }
                };
            }

            // Creamos el criterio de localización
            if (locationRequest == null) {
                locationRequest = LocationRequest.create().setFastestInterval((long) Math
                        .round(params.getLocationInterval()*1000));
            }

            // Obtenemos un cliente de localización
            if (fusedLocationClient == null) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            }

            // Definimos la localización
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                    null);

            if (map != null) {
                setCurrentLocation();
            }
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void setCurrentLocation() {
        if (isLocationRequestPermission()) {
            if (fusedLocationClient == null) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            }

            // Obtiene la última posición marcada
            fusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            currentLocation = location;
                            Log.i(TAG, "Last location: " + currentLocation);

                            // Mueve la cámara a la posición inicial del jugador
                            CameraPosition cameraFrom = new CameraPosition.Builder()
                                    .target(new LatLng(getLatitude(), getLongitude()))
                                    .build();
                            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraFrom));
                            CameraPosition cameraTo = new CameraPosition.Builder()
                                    .target(new LatLng(getLatitude(), getLongitude()))
                                    .zoom(15.0f)
                                    .build();
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraTo));
                        }
                    });

            // Enable on my location listener
            map.setMyLocationEnabled(true);
        }
    }

    private void setFirstPointLocation() {
        if (!points.isEmpty()) {
            LatLng position = points.get(0).position;

            // Mueve la cámara al primer punto
            CameraPosition cameraFrom = new CameraPosition.Builder()
                    .target(position)
                    .build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraFrom));
            CameraPosition cameraTo = new CameraPosition.Builder()
                    .target(position)
                    .zoom(15.0f)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraTo));
        }
    }

    private List<LatLng> getPoints() {
        List<LatLng> points = new ArrayList<>();

        // Si estamos en modo de jugar, se añade la posición actual del jugador
        if (params.getMapMode() == MapMode.PLAY_MODE) {
            points.add(new LatLng(getLatitude(), getLongitude()));
        }

        for (MapPoint point : this.points) {
            points.add(point.getPosition());
        }

        return points;
    }

    private void getRoute(LatLng firstPoint, LatLng lastPoint, List<LatLng> wayPoints,
                          final AtomicBoolean clearMap, final AtomicInteger completeRoutes,
                          final int maxRoutes) {
        // Obtenemos la ruta
        DirectionDestinationRequest request = GoogleDirection.withServerKey(getString(R.string.maps_key))
                .from(firstPoint);

        if (wayPoints != null) {
            request = request.and(wayPoints);
        }

        request.to(lastPoint)
                .transportMode(TransportMode.WALKING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            // Si es el primer thread, tendrá que limpiar el mapa
                            if (clearMap.compareAndSet(true, false)) {
                                map.clear();
                            }

                            // Dibuja el camino
                            drawPath(direction);
                        } else {
                            Log.e("MapFragment", "Error getting direction: "
                                    + direction.getStatus());
                        }

                        // Si es el último thread, dibuja los marcadores
                        if (completeRoutes.get() == maxRoutes-1) {
                            drawMarkers();
                        }

                        completeRoutes.getAndAdd(1);
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        throw new RuntimeException("Error getting route", t);
                    }
                });
    }

    private void drawRoute() {
        // Obtenemos una lista de punto para trazar la ruta
        List<LatLng> points = getPoints();

        // Dibujamos los puntos de ruta
        if (params.getOrderPoints() == PointOrder.ROUTE_ORDER) {
            LatLng fromPoint = points.get(0);
            LatLng toPoint = points.get(points.size()-1);
            List<LatLng> wayPoints = null;

            for (int i = 1; i < points.size()-1; i++) {
                if (wayPoints == null) {
                    wayPoints = new ArrayList<>();
                }

                wayPoints.add(points.get(i));
            }

            getRoute(fromPoint, toPoint, wayPoints, new AtomicBoolean(true),
                    new AtomicInteger(0), 1);
        }

        // Dibujamos los puntos aleatorios
        if (params.getOrderPoints() == PointOrder.NONE_ORDER) {
            AtomicBoolean clearMap = new AtomicBoolean(true);
            AtomicInteger completeRoutes = new AtomicInteger(0);
            LatLng fromPoint = points.get(0);

            for (int i = 1; i < points.size(); i++) {
                getRoute(fromPoint, points.get(i), null, clearMap, completeRoutes,
                        points.size()-1);
            }
        }
    }

    private void drawMarkers() {
        for (final MapPoint point: points) {
            drawMark(point);
        }
    }

    private void drawMap() {
        if (params.getTypePoints() == PointType.GIS_POINTS && (points.size() > 1)) {
            drawRoute();
        } else {
            drawMarkers();
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        // Comprueba que el contexto ha implementado el listener
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

        if (isLocationRequestPermission()) {
            startLocationUpdates();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onPause() {
        super.onPause();
        mapView.onPause();

        // Deshabilita el seguimiento
        if (isLocationRequestPermission()) {
            map.setMyLocationEnabled(false);

            if (fusedLocationClient != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Se define un zoom mínimo y máximo
        map.setMinZoomPreference(10.0f);
        map.setMaxZoomPreference(20.0f);

        // Centra el mapa en la posición actual o en el punto de inicio
        if (params.getMapMode() != MapMode.EDIT_MODE) {
            setCurrentLocation();
        } else {
            setFirstPointLocation();
        }


        // Dibuja el mapa
        drawMap();

        // Callback para cuando se mantiene presionado el mapa
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                listener.onMapLongClickListener(latLng);
            }
        });

        // Callback cuando se pulsa en un marcador
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (params.isShowInfoWindow()) {
                    if (!marker.isInfoWindowShown()) {
                        marker.showInfoWindow();
                    } else {
                        marker.hideInfoWindow();
                    }
                }

                // Se llama al callback
                listener.onMapPointClickListener((MapPoint) marker.getTag());
                return false;
            }
        });

        // Si estamos en el modo editar, se añade el listener de de D&D
        if (params.getMapMode() == MapMode.EDIT_MODE) {
            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    isDragPoint.set(true);
                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    MapPoint newPoint = (MapPoint) marker.getTag();

                    // Se actualiza el marcador
                    for (int i = 0; i < points.size(); i++) {
                        if (points.get(i).getId().equals(newPoint.getId())) {
                            points.get(i).setPosition(marker.getPosition());
                            break;
                        }
                    }

                    // Se llama al callback
                    listener.onMapPointEditListener(newPoint);

                    // Dibuja el mapa
                    isDragPoint.set(false);
                    drawMap();
                }
            });
        }

        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);
    }

    private double getLatitude() {
        if (currentLocation != null) {
            return currentLocation.getLatitude();
        }

        return 0.0;
    }

    private double getLongitude() {
        if (currentLocation != null) {
            return currentLocation.getLongitude();
        }

        return 0.0;
    }

    private boolean isLocationRequestPermission() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context
                .LOCATION_SERVICE);
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public interface OnMapFragmentInteractionListener {
        /**
         * Callback que se llama cuando se ha hecho una pulsación larga en el mapa
         * @param point Posición donde se ha pulsado
         */
        void onMapLongClickListener(LatLng point);

        /**
         * Callback que se llama cuando se ha pulsado uno de los marcadores del mapa
         * @param point Marcador que se ha pulsado
         */
        void onMapPointClickListener(MapPoint point);

        /**
         * Callback que se llama cuando se ha hecho drag-and-drop en un marcador
         * @param point Marcador que se ha movido
         */
        void onMapPointMoveListener(MapPoint point);

        /**
         * Callback que se llama cuando se quiere editar un marcador
         * @param point Marcador que se quiere editar
         */
        void onMapPointEditListener(MapPoint point);

        /**
         * Callback que se llama cuando se quiere borrar un marcador
         * @param point Marcador que se quiere borrar
         */
        void onMapPointRemoveListener(MapPoint point);
    }
}
