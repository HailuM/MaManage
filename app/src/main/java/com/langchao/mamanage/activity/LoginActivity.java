package com.langchao.mamanage.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.langchao.mamanage.R;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.dialog.MessageDialog;
import com.langchao.mamanage.manet.MaCallback;
import com.langchao.mamanage.manet.NetUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author fei
 * @Description:登录页
 * @date 2016/6/22 22:54
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends AutoLayoutActivity {

    @ViewInject(R.id.logIn)
    private BootstrapButton logIn;

    @ViewInject(R.id.editText1)
    private EditText user;

    @ViewInject(R.id.editText2)
    private EditText password;

    @ViewInject(R.id.checkBoxAutoLogin)
    private CheckBox autoLoginBox;

    Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        checkAutoLogin(readAutoLogin());

        if(null == readIp() || readIp().trim().length() == 0){
            saveIp(MaConstants.DEAULT_IP);
        }
    }

    private void checkAutoLogin(boolean auto) {
        if(auto) {
            String userId = readUserId();
            String userName = readUserName();
            if(!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userName)){
                LoginActivity.this.finish();
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }


    }

    //点击事件
    @Event(value = {R.id.service,R.id.outStorage,R.id.logIn,R.id.textViewForgot}, type = View.OnClickListener.class)
    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.service:
                intent.setClass(this,ServiceActivity.class);
                startActivity(intent);
                break;

            case R.id.logIn:

                this.doLogin();
                break;
            case R.id.textViewForgot:

                MessageDialog.show(this,"手机端暂不支持此功能，请联系系统管理员!");
                break;
        }
    }

    private void doLogin() {
        boolean cancel = false;
        View focusView = null;
        final String userName =user.getText().toString();

        String pwd = password.getText().toString();


        // Check for a valid email address.
        if (TextUtils.isEmpty(pwd)) {
            password.setError("密码不能为空");
            focusView = password;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName)  ) {
            user.setError("用户名不能为空");
            focusView = user;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            NetUtils.ToLogin(userName, pwd, new MaCallback.ToLoginCallBack() {
                @Override
                public void onSuccess(String userId) {
                    saveUserInfo(userName,userId,autoLoginBox.isChecked());
                    LoginActivity.this.finish();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(Throwable ex) {
                    Toast.makeText(x.app(),  ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }



    }

    private void saveUserInfo(String userName,String userId,boolean autoLogin){
        SharedPreferences.Editor editor = getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).edit();
        // 存入键值对
        editor.putString(MaConstants.PARA_USERNAME, userName);
        editor.putString(MaConstants.PARA_USERID, userId);
        editor.putBoolean(MaConstants.PARA_AUTOLOGIN,autoLogin);

        // 将内存中的数据写到XML文件中去
        editor.commit();
    }

    private String readUserId(){
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_USERID,"");

    }

    private String readUserName(){
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_USERNAME,"");

    }


    private boolean readAutoLogin(){
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getBoolean(MaConstants.PARA_AUTOLOGIN,false);

    }

    private String readIp(){
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_IP,"");

    }

    private void saveIp(String ip){
        SharedPreferences.Editor editor = getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).edit();
        // 存入键值对
        editor.putString("ip", ip);
        // 将内存中的数据写到XML文件中去
        editor.commit();
    }
}
