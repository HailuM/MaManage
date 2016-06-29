package com.langchao.mamanage.activity.icoutbill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.icin.Ic_inbill_b;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_list)
public class IcoutListActivity extends AppCompatActivity {

    private List<Ic_inbill> ic_inbills = null;

    @ViewInject((R.id.img_dir_out_list_search))
    private ImageView imgSearch;

    @ViewInject(R.id.et_dir_out_list_search)
    private EditText etSearch;


    @ViewInject(R.id.textViewTitle)
    private TextView textViewTitle;

    @ViewInject(R.id.lv_dir_out_order)
    private ListView lvOrder;

    IcoutInbillAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        textViewTitle.setText("出库办理");
        lvOrder.requestFocus();

        try {
            ic_inbills = new MaDAO().queryInbillForCk(null,null);

        } catch (Exception e) {

        }
        if(null == ic_inbills){
            ic_inbills = new ArrayList<>();
        }
        adapter = new IcoutInbillAdapter(this, ic_inbills);
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
                String s = editable.toString();
                try {
                    ic_inbills = new MaDAO().queryInbillForCk(s,null);
                } catch (Exception e) {

                }
                if(null == ic_inbills){
                    ic_inbills = new ArrayList<>();
                }
                adapter.inbills = ic_inbills;
                adapter.notifyDataSetChanged();
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
            ic_inbills = new MaDAO().queryInbillForCk(null,null);
            adapter.inbills = ic_inbills;
            adapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
