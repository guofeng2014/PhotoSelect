package com.photoselect;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.photoselect.adapter.AlbumListAdapter;
import com.photoselect.bean.FolderBean;
import com.photoselect.dialog.GallerySelectDialog;
import com.photoselect.util.LList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：guofeng
 * 日期:16/8/9
 * 图像选择器
 */
public class PhotoSelectActivity extends AppCompatActivity implements View.OnClickListener,
        GallerySelectDialog.IOnDirectorySelectListener,
        AdapterView.OnItemClickListener {
    private GridView gv;
    private TextView tvDirName;
    private TextView tvDirCount;
    private List<FolderBean> folderList = new ArrayList<>();
    private ProgressDialog pd;
    private AlbumListAdapter adapter;
    private GallerySelectDialog dialog;
    /**
     * 加载相册数据为空
     */
    private final static int LOAD_EMPTY = 0;
    /**
     * 加载相册有数据
     */
    private final static int LOAD_SUCCEED = 1;
    /**
     * 请求权限
     */
    private static final int REQ_READ_EXTERNAL_PERMISSION = 0X1;
    /**
     * 跳转到图片详情
     */
    private static final int REQ_TYPE_PHOTO_DETAIL = 0x3;


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_photo_select);
        initViews();
        if (hasWriteExternalPermission()) {
            initData();
        }
    }


    private void initViews() {
        gv = (GridView) findViewById(R.id.gv);
        findViewById(R.id.rl_select_bar).setOnClickListener(this);
        tvDirName = (TextView) findViewById(R.id.tv_dir_name);
        tvDirCount = (TextView) findViewById(R.id.tv_dir_count);
    }

    /**
     * 利用ContentProvider扫描本地图片
     */
    private void initData() {
        if (!TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), "未找到可用存储", Toast.LENGTH_SHORT).show();
            return;
        }
        pd = ProgressDialog.show(this, null, "加载中");
        Thread thread = new Thread(runnable);
        thread.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select_bar:
                if (dialog == null) {
                    dialog = new GallerySelectDialog(this, folderList);
                    dialog.setOnDirectorySelectListener(this);
                }
                dialog.show();
                break;
            default:
                break;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(uri,
                    null,
                    MediaStore.Images.Media.MIME_TYPE + " =? or " +
                            MediaStore.Images.Media.MIME_TYPE + " =? or " +
                            MediaStore.Images.Media.MIME_TYPE + " =?",
                    new String[]{"image/jpeg", "image/png", "image/jpg"},
                    MediaStore.Images.Media.DATE_ADDED);
            if (cursor == null) return;
            List<String> totalImage = new ArrayList<>();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                if (path == null || path.length() == 0) continue;
                File file = new File(path);
                File parentFile = file.getParentFile();
                if (parentFile == null || !parentFile.exists() || !file.exists()) continue;
                // 添加全部图片
                totalImage.add(path);
            }
            cursor.close();
            //本地没有图片
            if (totalImage.size() <= 0) {
                handler.sendEmptyMessage(LOAD_EMPTY);
                return;
            }

            //按照拍照日期排序,最近的在最上面
            Collections.reverse(totalImage);

            //所有图片
            FolderBean allBean = new FolderBean();
            List<String> allList = allBean.files;
            folderList.add(allBean);
            allBean.folderName = "全部照片";

            //分组逻辑
            Map<String, FolderBean> group = new HashMap<>();
            for (String s : totalImage) {
                File file = new File(s);
                File parent = file.getParentFile();
                if (!parent.exists()) continue;
                String fonderName = parent.getName();
                //全部图片
                allList.add(s);
                if (!group.containsKey(fonderName)) {
                    FolderBean bean = new FolderBean();
                    bean.coverImagePath = file.toString();
                    bean.folderName = fonderName;
                    bean.files.add(s);
                    group.put(fonderName, bean);
                    folderList.add(bean);
                } else {
                    FolderBean bean = group.get(fonderName);
                    bean.files.add(s);
                }
            }
            int count = allList.size();
            if (count > 0) {
                allBean.coverImagePath = allList.get(0);
                handler.obtainMessage(LOAD_SUCCEED, allBean).sendToTarget();
            }
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            pd.dismiss();
            switch (msg.what) {
                case LOAD_EMPTY:
                    Toast.makeText(getApplicationContext(), "未找到可用的图片", Toast.LENGTH_SHORT).show();
                    break;
                case LOAD_SUCCEED:
                    FolderBean bean = (FolderBean) msg.obj;
                    refreshAdapter(bean);
                    break;
            }
            return true;
        }
    });

    private void refreshAdapter(FolderBean bean) {
        if (adapter == null) {
            adapter = new AlbumListAdapter(this, bean.files);
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(this);
        } else {
            adapter.setData(bean.files);
            adapter.notifyDataSetChanged();
        }
        tvDirName.setText(bean.folderName);
        tvDirCount.setText(LList.getCount(bean.files) + "");
    }

    /**
     * select item callback
     *
     * @param bean
     */
    @Override
    public void onDirectorySelectAction(FolderBean bean) {
        gv.setSelection(0);
        //refresh adapter view
        refreshAdapter(bean);
    }


    /**
     * permission callback
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_READ_EXTERNAL_PERMISSION) {
            //permission allow
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initData();
            }
            //permission deny
            else {
                Toast.makeText(this, "未获取到读取文件的权限", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * check permission
     */
    private boolean hasWriteExternalPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_EXTERNAL_PERMISSION);
            return false;
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = (String) parent.getItemAtPosition(position);
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        //跳转到大图页面
        goToPicDetail(position);
    }

    /**
     * go to photo detail page
     *
     * @param position
     */
    private void goToPicDetail(int position) {
        ArrayList<String> data = adapter.getData();
        if (data != null && data.size() > 0) {
            PhotoDetailActivity.goToPhotoDetail(this, position, data, REQ_TYPE_PHOTO_DETAIL);
        } else {
            Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_TYPE_PHOTO_DETAIL && data != null) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }
}
