package com.lk.todolist.About;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lk.todolist.R;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //判斷Statute bar寬高 fitsSystemWindows="true"
        Window window = this.getWindow();
        // Followed by google doc.
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        //window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBar));
        //當API大於棉花糖5.0才能更改status bar顏色
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBar));
        }
        // For not opaque(transparent) color.
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("關於");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
