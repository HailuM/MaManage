package com.langchao.mamanage.activity.icoutbill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.langchao.mamanage.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_list)
public class OutBillListActivity extends AppCompatActivity {

    @ViewInject((R.id.img_dir_out_list_search))
    private ImageView imgSearch;

    @ViewInject(R.id.et_dir_out_list_search)
    private EditText etSearch;


    @ViewInject(R.id.lv_dir_out_order)
    private ListView lvOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
