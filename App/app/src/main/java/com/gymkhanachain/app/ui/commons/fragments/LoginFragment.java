package com.gymkhanachain.app.ui.commons.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.gymkhanachain.app.R;

import static android.support.constraint.Constraints.TAG;


public class LoginFragment extends DialogFragment implements OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    private String mParam2;

     SignInButton signInButton;

    public static final int RC_SIGN_IN = 9001;

    public LoginFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        signInButton = view.findViewById(R.id.button_signin);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
               // .enableAutoManage(getActivity(), this ,OnConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        try {
            //signInButton = getActivity().findViewById(R.id.signInButton);// NUllpointer aqui: no debe estar bien inicializado
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setOnClickListener(this);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Login w/google", Toast.LENGTH_SHORT).show();
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }

            });

        } catch (NullPointerException e){
        }


    }

    @Override
    public void onClick(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

        } catch (ApiException e) {

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }



/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new  AlertDialog.Builder(getActivity());
        builder.setTitle("Login required");
        builder.setMessage("Quieres iniciar sesion con tu cuenta de Google?");
        builder.setPositiveButton(R.string.common_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 {
                     //signIn();
                    //String[] myPermissions = new String[] {
                     //                        Manifest.permission.ACCESS_FINE_LOCATION
                     //                };
                };
            }
        })
        .setNegativeButton(R.string.common_reject, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //close app
            }
        });
        return builder.create();
    }
  */

}
