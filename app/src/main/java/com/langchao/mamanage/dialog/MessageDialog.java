package com.langchao.mamanage.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by miaohl on 2016/6/29.
 */
public class MessageDialog {

    public static void show(Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setMessage(message);

         builder.setTitle("提示");
         builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         });

        AlertDialog dialog = builder.create();
                dialog.show();
    }

}
