package com.photoselect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.photoselect.adapter.PhotoDetailAdapter;

import java.util.ArrayList;

import static com.photoselect.util.Constant.BUNDLE_ARRAY_STRING;
import static com.photoselect.util.Constant.BUNDLE_INTEGER;

/**
 * 作者：guofeng
 * 日期:16/11/22
 */

public class PhotoDetailActivity extends AppCompatActivity {

    /**
     * go to photo detail page
     *
     * @param activity current activity
     * @param position current item position
     * @param data     source imageData
     * @param code     request code
     */
    public static void goToPhotoDetail(Activity activity, int position, ArrayList<String> data, int code) {
        Intent intent = new Intent(activity, PhotoDetailActivity.class);
        intent.putExtra(BUNDLE_INTEGER, position);
        intent.putStringArrayListExtra(BUNDLE_ARRAY_STRING, data);
        activity.startActivityForResult(intent, code);
    }


    private TextView.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_complete:
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(BUNDLE_ARRAY_STRING, checkedData);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case R.id.tv_select:
                    int curPage = viewPager.getCurrentItem();
                    String path = imageData.get(curPage);
                    int selectSize = checkedData.size();
                    // remove data from selected list
                    if (checkedData.contains(path)) {
                        ivStatus.setImageResource(R.mipmap.ic_album_uncheck);
                        checkedData.remove(path);
                    }
                    // add path to selected list
                    else if (selectSize < MAX_SELECT_PHOTO) {
                        ivStatus.setImageResource(R.mipmap.ic_album_check);
                        checkedData.add(path);
                    }
                    selectSize = checkedData.size();
                    if (selectSize > 0) {
                        btnComplete.setText("完成" + checkedData.size() + "/" + MAX_SELECT_PHOTO);
                    } else {
                        btnComplete.setText("完成");
                    }
                    break;
            }
        }
    };

    private ViewPager.OnPageChangeListener pagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tvIndicate.setText(position + "/" + imageData.size());
            String path = imageData.get(position);
            boolean isSelected = checkedData.contains(path);
            //change imageView status to checked
            if (isSelected) {
                ivStatus.setImageResource(R.mipmap.ic_album_check);
            }
            //change imageView status to unchecked
            else {
                ivStatus.setImageResource(R.mipmap.ic_album_uncheck);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private TextView tvIndicate;
    private ImageView ivStatus;
    private ViewPager viewPager;
    private Button btnComplete;
    private PhotoDetailAdapter adapter;
    private int defaultIndex;
    private static final int MAX_SELECT_PHOTO = 9;

    private ArrayList<String> checkedData = new ArrayList<>();
    private ArrayList<String> imageData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_photo_detail);
        getIntentParams();
        initView();
        refreshAdapter(imageData);
        viewPager.setCurrentItem(defaultIndex);
    }


    private void getIntentParams() {
        Intent intent = getIntent();
        defaultIndex = intent.getIntExtra(BUNDLE_INTEGER, 0);
        imageData = intent.getStringArrayListExtra(BUNDLE_ARRAY_STRING);
    }

    private void initView() {
        tvIndicate = (TextView) findViewById(R.id.tv_indicate);
        ivStatus = (ImageView) findViewById(R.id.iv_status);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(clickListener);
        findViewById(R.id.tv_select).setOnClickListener(clickListener);
        tvIndicate.setText(defaultIndex + "/" + imageData.size());
    }


    private void refreshAdapter(ArrayList<String> data) {
        if (adapter == null) {
            adapter = new PhotoDetailAdapter(this);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(pagerChangeListener);
        }
        adapter.addData(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.clear();
        }
    }
}
