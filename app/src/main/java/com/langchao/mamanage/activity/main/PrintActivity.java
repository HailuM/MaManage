package com.langchao.mamanage.activity.main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.activity.icoutbill.IcoutInbillAdapter;
import com.langchao.mamanage.activity.icoutbill.IcoutInbillConfirmActivity;
import com.langchao.mamanage.blueprint.PrintDataActivity;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_b;
import com.langchao.mamanage.db.ic_out.Ic_outbill;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.dialog.MessageDialog;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


@ContentView(R.layout.activity_print_list)
public class PrintActivity extends AppCompatActivity {

    private List<Ic_outbill> ic_outbills = null;

    private List<Ic_diroutbill> ic_diroutbills = null;

    @ViewInject((R.id.img_dir_out_list_search))
    private ImageView imgSearch;

    @ViewInject(R.id.et_dir_out_list_search)
    private EditText etSearch;


    @ViewInject(R.id.textViewTitle)
    private TextView textViewTitle;

    @ViewInject(R.id.lv_dir_out_order)
    private ListView lvOrder;

    PrintAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //textViewTitle.setText("补打");

        try {
            new MaDAO().deleteTempDirout();
            ic_outbills = new MaDAO().queryForPrint();
            ic_diroutbills = new MaDAO().queryDirForPrint();
        } catch (DbException e) {
            e.printStackTrace();
        }
        adapter = new PrintAdapter(this, ic_outbills,ic_diroutbills);
        lvOrder.setAdapter(adapter);


    }


    @Event(value = {R.id.print}, type = View.OnClickListener.class)
    private void print(View v) {
        if(null ==  BluetoothAdapter
                .getDefaultAdapter()){
            MessageDialog.show(this,"没有找到蓝牙适配器");
            return;
        }
        Intent intent = new Intent(this, PrintDataActivity.class);




        this.startActivityForResult(intent,0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:

                break;
            default:
                break;
        }
    }
}
