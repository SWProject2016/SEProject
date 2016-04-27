package com.example.jeremy_liu.seproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jeremy_Liu on 2016/4/19.
 */
/*public class UserPageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("User Page");
        setContentView(R.layout.user);
        String username=getIntent().getStringExtra("username");
        find_and_modify_text_view(username);
    }


    private void find_and_modify_text_view(String username) {
        TextView text_view = (TextView) findViewById(R.id.textView1);
        CharSequence text_view_old = text_view.getText();
        text_view.setText(text_view_old
                + username);
    }
}*/

public class UserPageActivity extends Activity{
    SQLiteDatabase mDb;
    SQLiteDatabaseDao dao;
    // 存储数据的数组列表
    ArrayList<HashMap<String, Object>> listData;
    // 适配器
    SimpleAdapter listItemAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        dao = new SQLiteDatabaseDao();

        ListView list = (ListView) findViewById(R.id.list_items);
        listItemAdapter = new SimpleAdapter(UserPageActivity.this,
                listData,// 数据源
                R.layout.item,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[] { "image", "username", "birthday" },
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] { R.id.image, R.id.username, R.id.birthday });
        list.setAdapter(listItemAdapter);
        list.setOnCreateContextMenuListener(listviewLongPress);
    }

    // 简单的数据库操作类

    class SQLiteDatabaseDao {

        public SQLiteDatabaseDao() {
            mDb = openOrCreateDatabase("users.db",
                    SQLiteDatabase.CREATE_IF_NECESSARY, null);
            // 初始化创建表
            createTable(mDb, "student");
            // 初始化插入数据
            insert(mDb, "student");
            // 初始化获取所有数据表数据
            getAllData("student");
        }

        // 创建一个数据库
        public void createTable(SQLiteDatabase mDb, String table) {
            try {
                mDb.execSQL("create table if not exists "
                        + table
                        + " (id integer primary key autoincrement, "
                        + "username text not null, birthday text not null,image text);");
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "数据表创建失败",
                        Toast.LENGTH_LONG).show();
            }
        }

        // 插入数据
        public void insert(SQLiteDatabase mDb, String table) {

            // 初始化插入3条数据
            ContentValues values = new ContentValues();
            values.put("username", "LiMei");
            values.put("birthday", "Birthday:6-18");
            values.put("image", R.drawable.ic_launcher);
            mDb.insert(table, null, values);

            values.put("username", "LinQiao");
            values.put("birthday", "Birthday:8-22");
            values.put("image", R.drawable.ic_launcher);
            mDb.insert(table, null, values);

            values.put("username", "WiLee");
            values.put("birthday", "Birthday:9-12");
            values.put("image", R.drawable.ic_launcher);
            mDb.insert(table, null, values);

        }

        // 查询所有数据
        public void getAllData(String table) {
            Cursor c = mDb.rawQuery("select * from " + table, null);
            int columnsSize = c.getColumnCount();
            listData = new ArrayList<HashMap<String, Object>>();
            // 获取表的内容
            while (c.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnsSize; i++) {
                    map.put("id", c.getString(0));
                    map.put("username", c.getString(1));
                    map.put("birthday", c.getString(2));
                    map.put("image", c.getString(3));
                }
                listData.add(map);
            }
        }

        // 删除一条数据
        public boolean delete(SQLiteDatabase mDb, String table, int id) {
            String whereClause = "id=?";
            String[] whereArgs = new String[] { String.valueOf(id) };
            try {
                mDb.delete(table, whereClause, whereArgs);
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "删除数据库失败",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    }

    // 长按事件响应
    View.OnCreateContextMenuListener listviewLongPress = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            // TODO Auto-generated method stub
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            new AlertDialog.Builder(UserPageActivity.this)
                    /* 弹出窗口的最上头文字 */
                    .setTitle("删除当前数据")
                    /* 设置弹出窗口的图式 */
                    .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                    .setMessage("确定删除当前记录")
                    .setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    // 获取位置索引
                                    int mListPos = info.position;
                                    // 获取对应HashMap数据内容
                                    HashMap<String, Object> map = listData
                                            .get(mListPos);
                                    // 获取id
                                    int id = Integer.valueOf((map.get("id")
                                            .toString()));
                                    // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                                    if (dao.delete(mDb, "student", id)) {
                                        // 移除listData的数据
                                        listData.remove(mListPos);
                                        listItemAdapter.notifyDataSetChanged();
                                    }
                                }
                            })
                    .setNegativeButton("否",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    // 什么也没做

                                }
                            }).show();
        }
    };

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        mDb.close();
    }
}
