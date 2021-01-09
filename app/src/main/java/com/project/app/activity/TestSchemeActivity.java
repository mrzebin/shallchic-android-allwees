package com.project.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.project.app.R;
import com.project.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by zb
 * 登录activity
 */
public class TestSchemeActivity extends BaseActivity {
    @BindView(R.id.tv_testScheme)
    TextView tv_testScheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testscheme);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_testScheme})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.tv_testScheme:
                String url = "https://allwees.com/?al_applink_data=%7b%22target_url%22%3a%22https%3a%5c%2f%5c%2fallwees.com%22%2c%22extras%22%3a%7b%22sc_deeplink%22%3a%22allwees%3a%5c%2f%5c%2fmine%22%7d%2c%22referer_app_link%22%3a%7b%22app_name%22%3a%22AllWees%22%7d%7d";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
        }
    }
}
