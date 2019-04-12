package com.gymkhanachain.app.ui.commons.dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.mainscreen.activity.MainActivity;

public class LocationDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new  AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.location_dialog_title);
        builder.setMessage(R.string.location_dialog_content);
        builder.setPositiveButton(R.string.common_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] myPermissions = new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION
                };

                ActivityCompat.requestPermissions(getActivity(), myPermissions,
                        MainActivity.REQUEST_MY_LOCATION);
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
