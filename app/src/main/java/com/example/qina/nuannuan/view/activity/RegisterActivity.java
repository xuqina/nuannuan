package com.example.qina.nuannuan.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qina.nuannuan.R;
import com.example.qina.nuannuan.model.entity.User;
import com.example.qina.nuannuan.presenter.impl.UserPresenterImpl;
import com.example.qina.nuannuan.presenter.presenters.UserPresenter;
import com.example.qina.nuannuan.view.views.UserView;

/**
 * Created by qina on 18-1-29.
 */
public class RegisterActivity extends AppCompatActivity implements UserView {

    TextInputLayout usernameText = null;
    Button btn_reg = null;
    Activity activity = this;
    UserPresenter userPresenter = new UserPresenterImpl(this,this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        usernameText = (TextInputLayout) findViewById(R.id.more_username);
        btn_reg = (Button) findViewById(R.id.button_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getEditText().getText().toString();
                Log.i("qina", "username:" + username);
                if (username.trim().length()==0) {
                    Toast.makeText(activity,"昵称不能为空，请重新输入",Toast.LENGTH_LONG).show();
                } else {
                    User user = new User(username);
                    insertUser(user);
                    Intent wea = new Intent();
                    wea.setClass(RegisterActivity.this, WeatherActivity.class);
                    startActivity(wea);
                }
            }
        });
    }

    @Override
    public int getUserCount() {
        return 0;
    }

    @Override
    public void insertUser(User user) {
        userPresenter.insertUser(user);
    }
}
