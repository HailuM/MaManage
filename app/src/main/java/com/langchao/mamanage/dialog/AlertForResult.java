package com.langchao.mamanage.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Created by miaohl on 2016/6/26.
 */
public class AlertForResult {



    public static  void popUp(double oldnum,Context context,final PopCallBack callBack) {
        final EditText inputServer = new EditText(context);
        inputServer.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        inputServer.setText(oldnum+"");
        AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(context);
        alertbBuilder.setView(inputServer);
        alertbBuilder.setTitle("请输入数量").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = inputServer.getText().toString();
                double num = 0;
                try{
                      num = Double.parseDouble(result);
                }catch (Exception e){

                }
                callBack.setNum(num);

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        }).create();
        alertbBuilder.show();
    }
}
