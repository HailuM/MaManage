package com.langchao.mamanage.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.langchao.mamanage.R;
import com.langchao.mamanage.activity.MainActivity;

/**
 * Created by miaohl on 2016/6/26.
 */
public class AlertForResult {



    public static  void popUp(double oldnum,Context context,final PopCallBack callBack) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.alert_num);
        TextView text_add = (TextView) window.findViewById(R.id.textView12);
        TextView text_reduce = (TextView) window.findViewById(R.id.textView18);
//        tv_title.setText("详细信息");
        final EditText editText = (EditText) window.findViewById(R.id.editText);
        editText.setText(oldnum+"");
        BootstrapButton no= (BootstrapButton) window.findViewById(R.id.no);
        BootstrapButton yes= (BootstrapButton) window.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();
                double num = 0;
                try{
                      num = Double.parseDouble(result);
                }catch (Exception e){

                }
                callBack.setNum(num);
                alertDialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();
                double num = 0;
                try{
                    num = Double.parseDouble(result);
                    num++;
                }catch (Exception e){

                }
                    editText.setText(num+"");
            }
        });
        text_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();
                double num = 0;
                try{
                    num = Double.parseDouble(result);
                }catch (Exception e){

                }
                if (num!=0){
                    num--;
                    editText.setText(num+"");
                }
            }
        });
    }
}
