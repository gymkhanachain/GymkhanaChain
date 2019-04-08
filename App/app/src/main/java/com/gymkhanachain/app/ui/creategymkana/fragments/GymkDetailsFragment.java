package com.gymkhanachain.app.ui.creategymkana.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gymkhanachain.app.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GymkDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GymkDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GymkDetailsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    Button buttonActivate;
    Button buttonDelete;

    public GymkDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GymkDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GymkDetailsFragment newInstance() {
        return new GymkDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gymk_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            buttonActivate = getActivity().findViewById(R.id.button_start_gymk);
            buttonDelete = getActivity().findViewById(R.id.button_delete_gymk);
        } catch (NullPointerException e){

        }
        buttonActivate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        ImageButton imageButtonEditGymkImg = getActivity().findViewById(R.id.imageButton_edit_gymk_img);
        imageButtonEditGymkImg.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNearGymkFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        EditText etDesc = getActivity().findViewById(R.id.edittext_gymk_desc);
        EditText etName = getActivity().findViewById(R.id.edittext_gymk_name);

        switch (v.getId()) {
            case R.id.button_start_gymk:
                if (etName.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Por favor, introduce el nombre", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (etDesc.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Por favor, introduce la descripción", Toast.LENGTH_SHORT).show();
                    break;
                }
                Toast.makeText(getContext(), "La gymkhana ha sido activada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_delete_gymk:
                etName.setText("");
                etDesc.setText("");
                Toast.makeText(getContext(), "La gymkhana ha sido eliminada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton_edit_gymk_img:
                Toast.makeText(getContext(), "Se ha cambiado la imagen", Toast.LENGTH_SHORT).show();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
