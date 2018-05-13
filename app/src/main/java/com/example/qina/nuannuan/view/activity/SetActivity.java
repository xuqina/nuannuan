package com.example.qina.nuannuan.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qina.nuannuan.R;
import com.example.qina.nuannuan.model.entity.Notify;
import com.example.qina.nuannuan.presenter.impl.NotifyPresenterImpl;
import com.example.qina.nuannuan.presenter.presenters.NotifyPresenter;
import com.example.qina.nuannuan.view.views.NotifyView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by weijia on 18-4-16.
 */
public class SetActivity extends AppCompatActivity implements NotifyView {
    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 10000;
    private ListView listView;
    private Button new_Button;
    private String[] items;
    private String[] items1;
    private String[] items3;
    private Activity activity;
    private List<Notify> notifyList;
    private Notify newNofify = null;
    private ListAdapter listAdapter = null;
    NotifyPresenter notifyPresenter = new NotifyPresenterImpl(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notifyList = findAllNotify();
        setContentView(R.layout.set);
        listView = (ListView)findViewById(R.id.setList);
        listAdapter = new ListAdapter(this);
        listView.setAdapter(listAdapter);
        new_Button = (Button)findViewById(R.id.newItem);
        items = new String[] {"提醒我", "提醒好友"};
        items1 = new String[] {"日期提醒", "天气提醒"};
        items3 = new String[]{"高温","升温","降温","下雨","下雪"};
        activity = this;
        new_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNofify = new Notify();
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setTitle("选择提醒对象")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //数据库处理，插入对象，设置提醒对象
                                if (which == 0) {
                                    newNofify.setNotifyid(String.valueOf(System.currentTimeMillis()));
                                    newNofify.setIsNotifyFri("0");
                                    newNofify.setFriendName("我");
                                    newNofify.setFriendPhone("");

                                } else if (which == 1) {
                                    newNofify.setNotifyid(String.valueOf(System.currentTimeMillis()));
                                    newNofify.setIsNotifyFri("1");
                                    checkContactsPermission();//检查通讯录权限，之后弹出通讯录，选择之后会给newNofify赋值
                                }
                                //弹出城市输入框
                                final EditText cityInput = new EditText(activity);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                cityInput.setLayoutParams(lp);
                                AlertDialog dialog2 = new AlertDialog.Builder(activity)
                                        .setTitle("输入城市")
                                        .setView(cityInput)
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                newNofify.setCity(cityInput.getText().toString());


                                                AlertDialog dialog1 = new AlertDialog.Builder(activity)
                                                        .setTitle("选择提醒类型")
                                                        .setItems(items1, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //设置提醒类型为“日期”
                                                                if (which == 0) {
                                                                    newNofify.setNotifyType("0");
                                                                    showDatePickerDialog();
                                                                    //增加日期选择
                                                                    //设置日期
                                                                }
                                                                if (which == 1) {
                                                                    //设置提醒类型为“天气”
                                                                    newNofify.setNotifyType("1");
                                                                    AlertDialog dialog3 = new AlertDialog.Builder(activity).setTitle("选择天气类型")
                                                                            .setItems(items3, new DialogInterface.OnClickListener() {

                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    //多选对话框数据处理。。。
                                                                                    Log.i("qian", "which:" + which);
                                                                                    newNofify.setKeyword(items3[which]);
                                                                                    insertNotify(newNofify);
                                                                                    notifyList = findAllNotify();
                                                                                    listAdapter.notifyDataSetChanged();
                                                                                }
                                                                            })
                                                                            .create();
                                                                    dialog3.show();
                                                                }

                                                            }
                                                        })
                                                        .create();
                                                dialog1.show();

                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .create();
                                dialog2.show();
                            }
                        }).create();
                dialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setTitle("删除提醒")
                        .setMessage("确认删除吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Notify notify = notifyList.get(pos);
                                String notifyid = notify.getNotifyid();
                                deleteNotify(notifyid);
                                notifyList = findAllNotify();
                                listAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                return true;
            }
        });

    }

    @Override
    public void deleteNotify(String notifyid) {
        notifyPresenter.deleteNotify(notifyid);
    }

    @Override
    public List<Notify> findAllNotify() {
        return notifyPresenter.findAllNotify();
    }

    @Override
    public void insertNotify(Notify notify) {
        notifyPresenter.insertNotify(notify);
    }

    /**
     * 展示日期选择对话框
     */
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(SetActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                Log.i("qina",date);
                newNofify.setDate(date);
                insertNotify(newNofify);
                notifyList = findAllNotify();
                listAdapter.notifyDataSetChanged();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    class ListAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;

        public ListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return notifyList.size();
        }

        @Override
        public Notify getItem(int position) {
            return notifyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Notify notify = notifyList.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.set_item,null);
                viewHolder.personTextView = (TextView)convertView.findViewById(R.id.person);
                viewHolder.contentTextView = (TextView)convertView.findViewById(R.id.content_set);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.personTextView.setText(notify.getFriendName());
            String content = "";
            if (notify.getNotifyType().equals("0")) {
                content = "日期："+notify.getDate();
            } else {
                content = "天气："+notify.getKeyword();
            }
            viewHolder.contentTextView.setText(content);
            return convertView;
        }
    }

    class ViewHolder {
        private TextView personTextView;
        private TextView contentTextView;
    }


    public void checkContactsPermission() {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[] {
                        Manifest.permission.READ_CONTACTS}
        );
        // 如果这个权限已拥有, 则直接执行代码
        if (isAllGranted) {
            callContacts();
        } else {
            /**
             * 第 2 步: 请求权限
             */
            // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.READ_CONTACTS
                    },
                    CONTACTS_PERMISSION_REQUEST_CODE
            );
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }


    public void callContacts() {
        startActivityForResult(new Intent(
                Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver reContentResolverol = getContentResolver();
            Uri contactData = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            newNofify.setFriendName(username);
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phone.moveToNext()) {
                String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                newNofify.setFriendPhone(usernumber);
            }

        }
    }
}
