package com.gymkhanachain.app.ui.creategymkana.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.mainscreen.activity.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


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

    String mCurrentPhotoPath;
    private static final int PERMISSION_REQUEST_CAMERA_CODE = 200;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 201;

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


    private boolean checkPermission(String p) {
        if (ContextCompat.checkSelfPermission(getActivity(), p)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission(String p, int code) {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{p},code);
    }

    private void dispatchTakePictureIntent() {

        if(!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if(!checkPermission(Manifest.permission.CAMERA)) {
            requestPermission(Manifest.permission.CAMERA, PERMISSION_REQUEST_CAMERA_CODE);
        }

        if(!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try { // Create the File where the photo should go
                photoFile = createImageFile();
            } catch (IOException ex) { // Error occurred while creating the File
                Toast.makeText(getContext(), "Ha fallado: " + ex.getMessage().toString() , Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) { // Check if the File was successfully created
                try {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.gymkhanachain.app.ui.providers.GenericFileProvider", createImageFile());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, PERMISSION_REQUEST_CAMERA_CODE);
                } catch (IOException e) {

                }
            }
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PERMISSION_REQUEST_CAMERA_CODE && resultCode == RESULT_OK) {
                galleryAddPic();
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                // Glide.with(this).load(imageFilePath).into(mImageView);
                /*Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                View inflatedView = getActivity().findViewById(android.R.id.content);
                ((ImageView) inflatedView.findViewById(R.id.imageView_gymk)).setImageBitmap(bitmap);
            }
        }
    }
}

