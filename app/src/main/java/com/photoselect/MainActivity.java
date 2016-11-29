package com.photoselect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.imageloader.ImageLoader;
import com.imageloader.config.ImageLoaderConfig;

import java.util.ArrayList;

import static com.photoselect.util.Constant.BUNDLE_ARRAY_STRING;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_PHOTO_PATH = 0x1;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setDefaultResourceId(R.mipmap.bg_album_def)
                .setErrorResourceId(R.mipmap.bg_album_def)
                .setThreadCount(3)
                .setImageConfig(Bitmap.Config.RGB_565)
                .setOpenFileCache(true)
                .build();
        com.imageloader.ImageLoader.getInstance().initConfig(config);

        findViewById(R.id.tv_choose_local).setOnClickListener(listener);
        findViewById(R.id.tv_load_net).setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_choose_local:
                    startActivityForResult(new Intent(MainActivity.this, PhotoSelectActivity.class), REQ_PHOTO_PATH);
                    break;
                case R.id.tv_load_net:
                    startActivity(new Intent(MainActivity.this, NetPhotoActivity.class));
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_PHOTO_PATH && data != null) {
            ArrayList<String> selectedData = data.getStringArrayListExtra(BUNDLE_ARRAY_STRING);
            TextView tvContent = (TextView) findViewById(R.id.tv_content);
            tvContent.setText("");
            for (String s : selectedData) {
                tvContent.append(s + "\n");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().release();
    }
}
