package com.langchao.mamanage.activity.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.langchao.mamanage.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_in_storage)
public class InStorageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
