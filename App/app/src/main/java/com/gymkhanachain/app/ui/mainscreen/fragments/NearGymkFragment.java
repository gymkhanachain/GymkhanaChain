package com.gymkhanachain.app.ui.mainscreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.commons.GymkhanaCache;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.mainscreen.adapters.NearGymkAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NearGymkFragment extends Fragment implements NearGymkAdapter.
        NearGymkItem.OnNearGymkItemListener {
    private static final String ARG_NEAR_GYMKHANAS = "nearGymks";

    private static final GymkhanaCache gymkhanas = GymkhanaCache.getInstance();

    @BindView(R.id.near_gymkhanas)
    RecyclerView nearGymkhanasView;

    private List<Integer> nearGymkhanas;
    private Unbinder unbinder;
    private OnNearGymkFragmentInteractionListener listener;

    public NearGymkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nearGymkhanasId
     *      The near gymkhanas Id.
     * @return A new instance of fragment NearGymkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NearGymkFragment newInstance(List<Integer> nearGymkhanasId) {
        NearGymkFragment fragment = new NearGymkFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEAR_GYMKHANAS, Parcels.wrap(nearGymkhanasId));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nearGymkhanas = Parcels.unwrap(getArguments().getParcelable(ARG_NEAR_GYMKHANAS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near_gymk, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        nearGymkhanasView.setHasFixedSize(true);

        nearGymkhanasView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        // Specify an adapter (see also next example)
        final NearGymkAdapter adapter = new NearGymkAdapter(nearGymkhanas, this);
        nearGymkhanasView.setAdapter(adapter);

        // Load all points
        List<MapPoint> points = new ArrayList<>();
        for (Integer id : nearGymkhanas) {
            GymkhanaBean bean = gymkhanas.getGymkhana(id);
            MapPoint point = new MapPoint(id, bean.getPosition(), bean.getName());
            points.add(point);
        }

        // Create map fragment
        final MapFragment map = MapFragment.newInstance(MapFragment.GYMKHANA_POINTS, points);
        getFragmentManager().beginTransaction().add(R.id.map_placeholder, map).commit();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNearGymkFragmentInteractionListener) {
            listener = (OnNearGymkFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNearGymkFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onNearGymkItemClick() {
        listener.onNearGymkFragmentInteraction();
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
    public interface OnNearGymkFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNearGymkFragmentInteraction();
    }
}
