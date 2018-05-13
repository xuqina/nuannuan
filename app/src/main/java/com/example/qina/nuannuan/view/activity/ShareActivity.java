package com.example.qina.nuannuan.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qina.nuannuan.R;
import com.example.qina.nuannuan.utils.MessageUtil;

import java.util.List;

/**
 * Created by weijia on 18-4-16.
 */
public class ShareActivity extends AppCompatActivity {

    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 10000;
    private static final int SMS_PERMISSION_REQUEST_CODE = 10001;
    ImageButton imageButton = null;
    Button sendButton = null;
    EditText phoneNumber = null;
    EditText contentEdit = null;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        phoneNumber = (EditText)findViewById(R.id.phone);

        contentEdit = (EditText)findViewById(R.id.content);
        contentEdit.setText(getIntent().getStringExtra("content"));

        Log.i("qina",getIntent().getStringExtra("content"));

        imageButton = (ImageButton)findViewById(R.id.contract_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 第 1 步: 检查是否有相应的权限
                 */
                boolean isAllGranted = checkPermissionAllGranted(activity,
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
        });

        sendButton = (Button)findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.getText().toString().length()==0) {
                    Toast.makeText(activity,"手机号码不能为空",Toast.LENGTH_LONG).show();
                } else if (contentEdit.getText().toString().length()==0) {
                    Toast.makeText(activity,"分享内容不能为空",Toast.LENGTH_LONG).show();
                } else {
                    /**
                     * 第 1 步: 检查是否有相应的权限
                     */
                    boolean isAllGranted = checkPermissionAllGranted(activity,
                            new String[] {
                                    Manifest.permission.SEND_SMS,
                                    Manifest.permission.READ_PHONE_STATE}
                    );
                    // 如果这个权限已拥有, 则直接执行代码
                    if (isAllGranted) {
                        MessageUtil.sendSMS(activity, phoneNumber.getText().toString(), contentEdit.getText().toString());
                    } else {
                        /**
                         * 第 2 步: 请求权限
                         */
                        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                        ActivityCompat.requestPermissions(
                                activity,
                                new String[]{
                                        Manifest.permission.SEND_SMS,
                                        Manifest.permission.READ_PHONE_STATE
                                },
                                SMS_PERMISSION_REQUEST_CODE
                        );
                    }


                }
            }
        });

    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Context context,String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行代码
                callContacts();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        } else if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行代码
                MessageUtil.sendSMS(activity, phoneNumber.getText().toString(), contentEdit.getText().toString());

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }


    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("需要访问 “通讯录” 和 “发送短信”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
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
            //username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phone.moveToNext()) {
                String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber.setText(usernumber);
            }

        }
    }


}
