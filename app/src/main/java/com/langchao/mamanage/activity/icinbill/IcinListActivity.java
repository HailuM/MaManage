package com.langchao.mamanage.activity.icinbill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.order.Pu_order;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_list)
public class IcinListActivity extends AutoLayoutActivity {

    private List<Pu_order> pu_orderList = null;

    @ViewInject((R.id.img_dir_out_list_search))
    private ImageView imgSearch;

    @ViewInject(R.id.et_dir_out_list_search)
    private EditText etSearch;

    @ViewInject(R.id.et_dir_out_list_search_supply)
    private EditText etSearchSupply;

    @ViewInject(R.id.textViewTitle)
    private TextView textViewTitle;

    @ViewInject(R.id.lv_dir_out_order)
    private ListView lvOrder;

    @Event(value = {R.id.back_image}, type = View.OnClickListener.class)
    private void back(View v) {
        this.finish();

    }

    IcinOrderAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        try {
            new MaDAO().deleteTempDirout();
        } catch (DbException e) {
            e.printStackTrace();
        }
        textViewTitle.setText("入库办理");
        lvOrder.setFocusable(true);
        lvOrder.requestFocus();
        try {
            pu_orderList = new MaDAO().queryPuOrderForRk(null,null);

        } catch (DbException e) {
            e.printStackTrace();
        }

        adapter = new IcinOrderAdapter(this,pu_orderList);
        lvOrder.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fresh();
//                String s = editable.toString();
//                try {
//                    pu_orderList = new MaDAO().queryPuOrderForRk(s,null);
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//                if(null == pu_orderList){
//                    pu_orderList = new ArrayList<>();
//                }
//                adapter.pu_orderList = pu_orderList;
//                adapter.notifyDataSetChanged();
            }
        });



        etSearchSupply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fresh();
//                String s = editable.toString();
//                try {
//                    pu_orderList = new MaDAO().queryPuOrder(s, null);
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//                if (null == pu_orderList) {
//                    pu_orderList = new ArrayList<>();
//                }
//                adapter.pu_orderList = pu_orderList;
//                adapter.notifyDataSetChanged();
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
               fresh();
                break;
            default:
                fresh();
                break;
        }
    }

    private void fresh() {
        try {
            String s = etSearch.getText().toString();
            String s2 = etSearchSupply.getText().toString();
            pu_orderList = new MaDAO().queryPuOrderForRk(s,s2);
            adapter.pu_orderList = pu_orderList;
            adapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
