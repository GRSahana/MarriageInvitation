package com.ubikasoftwares.marriageinvitation.Utils;



import android.app.AlertDialog;
import android.content.Context;

import dmax.dialog.SpotsDialog;

public class ProgressDialogHelper {

    private AlertDialog dialog;


    public void ShowPdialog(Context context){

       dialog = new SpotsDialog.Builder()
       .setCancelable(false)
               .setContext(context)
               .setMessage("loading")
               .build();
            dialog.show();

    }

    public void HidePdialog(){
        if(dialog!=null){
          dialog.dismiss();
            dialog = null;
        }
    }

}
