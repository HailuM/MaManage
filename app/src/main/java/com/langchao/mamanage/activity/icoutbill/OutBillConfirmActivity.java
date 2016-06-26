package com.langchao.mamanage.activity.icoutbill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.langchao.mamanage.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_confirm)
public class OutBillConfirmActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_dir_out_no)
    TextView tvOrderNo;//订单号
    @ViewInject(R.id.tv_dir_out_supply)
    TextView tvOrderSupply;//供方
    @ViewInject(R.id.tv_dir_out_build)
    TextView tvOrderBuild;//楼栋
    @ViewInject(R.id.tv_dir_out_contact)
    TextView tvOrderContact;//联系人
    @ViewInject(R.id.sp_dir_out_get)
    Spinner spOrderGet;//领用商
    @ViewInject(R.id.lv_dir_out_m)
    ListView lvOrderMaterial;
    @ViewInject(R.id.img_dir_out_choose)
    ImageView imgOrderChoose;
    @ViewInject(R.id.tv_dir_out_choose)
    TextView tvOrderChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
