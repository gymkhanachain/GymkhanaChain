package com.gymkhanachain.app.ui.creategymkana.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.mainscreen.activity.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private static final int PERMISSION_REQUEST_CAMERA_CODE = 200;

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
                    + " must implement OnFragmentInteractionListener");
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
                /*Foto o coger de la galería*/
                dispatchTakePictureIntent();

                /*Mostrarla como nueva imagen*/

                /*Mandarla a la base de datos y guardarla allí*/

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


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestCameraPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
        }

    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestCameraPermission() {
        Toast.makeText(getContext(), "Se va a pedir permiso", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CAMERA_CODE);
    }

    private void dispatchTakePictureIntent() {
        if(checkCameraPermission()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try { // Create the File where the photo should go
                    photoFile = createImageFile();
                } catch (IOException ex) { // Error occurred while creating the File

                }
                if (photoFile != null) { // Check if the File was successfully created
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            requestCameraPermission();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }
}
