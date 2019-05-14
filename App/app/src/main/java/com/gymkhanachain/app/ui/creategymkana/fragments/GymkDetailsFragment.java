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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.client.Gymkhana;
import com.gymkhanachain.app.client.GymkhanasClient;
import com.gymkhanachain.app.client.GymkhanasRestService;
import com.gymkhanachain.app.client.Point;
import com.gymkhanachain.app.client.QuizzPoint;
import com.gymkhanachain.app.client.RestServ;
import com.gymkhanachain.app.client.TextPoint;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.model.beans.GymkhanaType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static java.lang.Boolean.FALSE;

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
    private final static String TAG = "GymkDetailsFragment";

    Button buttonActivate;
    Button buttonDelete;

    String mCurrentPhotoPath;
    public static final int PERMISSION_REQUEST_CAMERA_CODE = 200;
    public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 201;
    public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_GALLERY = 202;
    public static final int REQUEST_TAKE_PHOTO = 301;
    public static final int REQUEST_TAKE_PHOTO_FROM_GALLERY = 302;

    private static final String TEMP_IMAGE_NAME = "tempImage";

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
        /* LatLng l = new LatLng(40,40);
        LatLng l2 = new LatLng(0,0);
        List<Gymkhana> prueba = RestServ.getUserGymkanas("CREADORCIO");
        RestServ.deleteGymkhana(43);
        Point p1 = new TextPoint(null,"p1123e","punto1","desc", l, "Holo");
        Point p2 = new QuizzPoint(null,"p1123e","punto1","desc", l, "Holo","","","","",1);
        Point p3 = new TextPoint(null,"p1123e","punto1","desc", l, "Holo");
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        Gymkhana g = new Gymkhana(null,"1111","s","e",l, GymkhanaType.desordenada,FALSE,FALSE,"","",1,1,1, points);
        Gymkhana g2 = new Gymkhana(null,"222","s2","2e",l, GymkhanaType.desordenada,FALSE,FALSE,"","",21,21,12, points);
        List<Gymkhana> prueba = new ArrayList<>();
        prueba.add(g);
        prueba.add(g2);
        RestServ.addGymkhana(prueba);
        LatLng l = new LatLng(40,40);
        Point p1 = new TextPoint(94,"IMAGENONCIA","MINPUTNTO","DESCRIPVIONCAMBIADA", l, "Pruebamod");
        Point p2 = new QuizzPoint(95,"p1123e","punto1","desc", l, "Holo","","","moooood","",1);
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        Gymkhana g = new Gymkhana(41,"1111","s","e",l, GymkhanaType.desordenada,FALSE,FALSE,"","",1,1,1, points);
        RestServ.modGymkhana(g);*/
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
                /*Foto o coger de la galería*/
                Log.d(TAG, "Editar foto");
                requestMode();
                // checkPermissions();

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

    private void requestMode(){
            final CharSequence[] options = {"Sacar foto", "Escoger de la galería", "Cancelar"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle("Opciones");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Sacar foto")) {
                        Log.d(TAG, "Sacar foto");
                        checkPermissions(REQUEST_TAKE_PHOTO);
                    } else if (options[item].equals("Escoger de la galería")) {
                        checkPermissions(REQUEST_TAKE_PHOTO_FROM_GALLERY);
                    } else if (options[item].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
    }


    public void checkPermissions(int mode){
        Log.d(TAG, "Miro permisos");
        switch(mode){
            case REQUEST_TAKE_PHOTO:
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA_CODE);
                    return;
                } else if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                    return;
                }
                break;
            case REQUEST_TAKE_PHOTO_FROM_GALLERY:
                if ( ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    dispatchPickPictureIntent();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_GALLERY);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult " + requestCode);
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    checkPermissions(REQUEST_TAKE_PHOTO);
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Se necesitan permisos de acceso a la cámara", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    checkPermissions(REQUEST_TAKE_PHOTO);
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Se necesitan permisos de escritura en el sistema de ficheros", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_GALLERY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    checkPermissions(REQUEST_TAKE_PHOTO_FROM_GALLERY);
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Se necesitan permisos de escritura en el sistema de ficheros", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void dispatchPickPictureIntent(){
        Intent pickPhoto=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        pickPhoto.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        pickPhoto.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(pickPhoto,REQUEST_TAKE_PHOTO_FROM_GALLERY);
    }

    private void dispatchTakePictureIntent() {

        if(!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
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
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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

    private void setPic() {
        View mImageView = getActivity().findViewById(android.R.id.content);
        int targetW = mImageView.getWidth(); // Get the dimensions of the View
        int targetH = mImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath.replace("file:",""), bmOptions);

        int photoW = bmOptions.outWidth; // Get the dimensions of the bitmap
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        // Decode the image file into a Bitmap sized to fill the View

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath.replace("file:",""),bmOptions);
        ((ImageView) mImageView.findViewById(R.id.imageView_gymk)).setImageBitmap(bitmap);
    }

    private void setPic (Uri imageUri){
        Log.d(TAG, "onActivityResult " + imageUri.toString());
        View mImageView = getActivity().findViewById(android.R.id.content);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
            ((ImageView) mImageView.findViewById(R.id.imageView_gymk)).setImageBitmap(bitmap);
        } catch (FileNotFoundException ex){

        } catch (IOException ex) {
            Toast.makeText(getContext(), "Ha fallado: " + ex.getMessage().toString() , Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    galleryAddPic();
                    setPic();
                }
                break;
            case REQUEST_TAKE_PHOTO_FROM_GALLERY:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    setPic(selectedImage);
                }
                break;
        }
    }


}

