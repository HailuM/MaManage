package com.langchao.mamanage.activity.icinbill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.order.Pu_order;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_list)
public class IcinListActivity extends AppCompatActivity {

    private List<Pu_order> pu_orderList = null;

    @ViewInject((R.id.img_dir_out_list_search))
    private ImageView imgSearch;

    @ViewInject(R.id.et_dir_out_list_search)
    private EditText etSearch;


    @ViewInject(R.id.textViewTitle)
    private TextView textViewTitle;

    @ViewInject(R.id.lv_dir_out_order)
    private ListView lvOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        textViewTitle.setText("入库办理");

        try {
            pu_orderList = new MaDAO().queryPuOrderForRk(null,null);

        } catch (DbException e) {
            e.printStackTrace();
        }

        lvOrder.setAdapter(new IcinOrderAdapter(this,pu_orderList));


    }
}
