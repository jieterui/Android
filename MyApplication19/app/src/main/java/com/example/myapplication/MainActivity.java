package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_name;
    EditText et_phone;
    Button btn_add,btn_query,btn_update,btn_delete;
    TextView tv_show;
   MyHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name=findViewById(R.id.et_name);
        et_phone=findViewById(R.id.et_phone);
        btn_add=findViewById(R.id.btn_add);
        btn_query=findViewById(R.id.btn_query);
        btn_update=findViewById(R.id.btn_update);
        btn_delete=findViewById(R.id.btn_delete);
        tv_show=findViewById(R.id.tv_show);
        btn_add.setOnClickListener(this);
        btn_query.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        myHelper=new MyHelper(this);


    }

    @Override
    public void onClick(View view) {
        String  name,phone;
        SQLiteDatabase db;
        ContentValues values;
        switch (view.getId()){
            case R.id.btn_add:    //添加数据
                name=et_name.getText().toString();
                phone=et_phone.getText().toString();
                db=myHelper.getWritableDatabase();
                values=new ContentValues();
                values.put("name",name);
                values.put("phone",phone);
                db.insert("information",null,values);
                Toast.makeText(this,"信息已添加",Toast.LENGTH_LONG).show();
                db.close();
                break;
            case R.id.btn_query:
                db = myHelper.getReadableDatabase();
                Cursor cursor = db.query("information", null, null, null, null,
                        null, null);
                if (cursor.getCount() == 0) {
                    tv_show.setText("");
                    Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                } else {
                    cursor.moveToFirst();
                    tv_show.setText("Name :  " + cursor.getString(1) +
                            "  ；Tel :  " + cursor.getString(2));
                }
                while (cursor.moveToNext()) {
                    tv_show.append("\n" + "Name :  " + cursor.getString(1) +
                            "  ；Tel :  " + cursor.getString(2));
                }
                cursor.close();
                db.close();
                break;
            case R.id.btn_update:
                db = myHelper.getWritableDatabase();
                values = new ContentValues();       // 要修改的数据
                values.put("phone", phone = et_phone.getText().toString());
                db.update("information", values, "name=?",
                        new String[]{et_name.getText().toString()}); // 更新并得到行数
                Toast.makeText(this, "信息已修改", Toast.LENGTH_SHORT).show();
                db.close();

                break;
            case R.id.btn_delete:
                db = myHelper.getWritableDatabase();
                db.delete("information", "name=?", new String[]{et_name.getText().toString()});
                Toast.makeText(this, "信息已删除", Toast.LENGTH_SHORT).show();
                tv_show.setText("");
                db.close();
                break;

        }

        //if(view.getId()==R.id.btn_add){   .......}
        //if(view.getId()==R.id.btn_query){.......}
    }


    //写类 创建数据库和表
    class MyHelper extends SQLiteOpenHelper{

        //构造函数创建数据库
        public MyHelper(@Nullable Context context) {
            super(context,"itcast.db", null, 1);
        }
        //创建数据库中的表
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE information(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(100),phone VARCHAR(100) )");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

}
