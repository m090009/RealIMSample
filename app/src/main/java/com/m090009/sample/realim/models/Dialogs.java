package com.m090009.sample.realim.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.m090009.sample.realim.R;
import com.m090009.sample.realim.interfaces.OnAddUser;

import java.util.ArrayList;

/**
 * Created by tohamy on 5/4/15.
 */
public class Dialogs {

    public static void buildAddUsernameDialog(final Context context,
                                              final OnAddUser callback,
                                              final int position){
//        final TagsRepo tags = tagsrepo;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(
                context.
                        getString(R.string.add_chatroom_user_dialog_title)
        ); //Set Alert dialog title here
        alert.setMessage(
                context.getString(R.string.add_chatroom_user_dialog_message)
        ); //Message here

        // Set an EditText view to get userName
        final EditText userNameInput = new EditText(context);
        userNameInput.setHint(R.string.add_chatroom_user_dialog_editText_hint);
        userNameInput.setGravity(Gravity.CENTER);
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 30, 0);

        layout.addView(userNameInput, params);
        alert.setView(layout);
        alert.setPositiveButton(context.
                getString(R.string.add_chatroom_user_dialog_positiveButton)
                , null);
        alert.setNegativeButton(context.
                getString(R.string.add_chatroom_user_dialog_negativeButton)
                , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = alert.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userName = userNameInput.getEditableText().toString();
                        if(callback.hasUserName(userName, position)){// - returns T/F){
                            Toast.makeText(context, "\"" +
                                    userName +
                                    context.getString(R.string.userName_exists_warning),
                                    Toast.LENGTH_LONG).show();
                            // clearing editText
                            userNameInput.setText("");
                        } else {
                            Toast.makeText(context, "\"" +
                                    userName +
                                    context.getString(R.string.userName_created_message),
                                    Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            callback.onFinishAddUserDialog(userName, position);

                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    public static ProgressDialog buildProgressDialog(Context context, String message){
        ProgressDialog dialog = ProgressDialog.show(context, "",
                message, true);
        return dialog;
    }

    public static AlertDialog buildNoConnectionAlert(Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.noInternetConnection_title));
        alertDialog.setMessage(context.getString(R.string.noInternetConnection_message));
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
        alertDialog.show();
        return alertDialog;
    }
}
