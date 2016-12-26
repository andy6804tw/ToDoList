package com.lk.todolist;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class modify extends AppCompatActivity {
    DBAccess access;
    EditText edtDate,edtTime,edtTitle,edtCategory,edtDesc,edtStatue;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        edtDate=(EditText)findViewById(R.id.edtDate);
        edtTime=(EditText)findViewById(R.id.edtTime);
        edtTitle=(EditText)findViewById(R.id.edtTitle);
        edtCategory=(EditText)findViewById(R.id.edtCategory);
        edtDesc=(EditText)findViewById(R.id.edtDesc);
        edtStatue=(EditText)findViewById(R.id.edtStatue);

        access=new DBAccess(this, "schedule", null, 1);
        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("id","0");
        Cursor c=access.getData(DBAccess.ID_FIELD+" ="+id, null);//資料查詢，條件為_id等於上一個活動視窗傳遞過來的資料
        c.moveToFirst();//將指標一道第一筆

        edtTitle.setText(c.getString(1));//得到ID欄位的資料1
        edtDate.setText(c.getString(2));
        edtTime.setText(c.getString(3));
        edtCategory.setText(c.getString(4));
        edtDesc.setText(c.getString(5));
        edtStatue.setText(c.getString(6));

    }

    public void modify(View view) {
        access.update(edtTitle.getText()+"", edtDate.getText()+"",edtTime.getText()+"",edtCategory.getText()+"",edtDesc.getText()+"",edtStatue.getText()+"",DBAccess.ID_FIELD+" ="+id);
        finish();

    }

    public void delete(View view) {
        access.delete(id);
        finish();
    }
}
