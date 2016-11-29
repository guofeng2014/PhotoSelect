package com.photoselect.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.imageloader.ImageLoader;
import com.imageloader.download.ImageDownLoader;
import com.photoselect.R;
import com.photoselect.bean.FolderBean;
import com.photoselect.util.LList;
import com.photoselect.util.Scale;

import java.util.List;

public class GallerySelectDialog implements AdapterView.OnItemClickListener {

    private Context context;
    private List<FolderBean> folderList;
    private AlertDialog dialog;
    private IOnDirectorySelectListener listener;
    private GallerySelectAdapter adapter;
    private LinearLayout.LayoutParams params;

    public GallerySelectDialog(Context context, List<FolderBean> folderList) {
        this.context = context;
        this.folderList = folderList;
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        params.topMargin = Scale.dip2px(context, 200);
    }

    public void setOnDirectorySelectListener(IOnDirectorySelectListener listener) {
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void show() {
        dialog = new AlertDialog.Builder(context, R.style.translate_dialog).create();
        View view = LayoutInflater.from(context).inflate(R.layout.view_gallery_dialog, null);
        dialog.show();
        dialog.setContentView(view);
        ListView lv = (ListView) view.findViewById(R.id.lv);
        lv.setLayoutParams(params);
        if (adapter == null) {
            adapter = new GallerySelectAdapter(context, folderList);
            adapter.setCheckIndex(0);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }


    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lv = (ListView) parent;
        FolderBean item = (FolderBean) lv.getItemAtPosition(position);
        if (item == null) return;
        if (adapter != null) {
            adapter.setCheckIndex(position);
            adapter.notifyDataSetChanged();
        }
        if (listener != null) {
            listener.onDirectorySelectAction(item);
        }
        dismiss();
    }

    private static class GallerySelectAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<FolderBean> data;
        private int checkIndex;

        public GallerySelectAdapter(Context context, List<FolderBean> data) {
            this.data = data;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setCheckIndex(int checkIndex) {
            this.checkIndex = checkIndex;
        }

        @Override
        public int getCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }

        @Override
        public FolderBean getItem(int position) {
            if (data == null || position >= data.size()) {
                return null;
            }
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_dir, null);
                holder.ivDir = (ImageView) convertView.findViewById(R.id.iv_dir);
                holder.ivChecked = (ImageView) convertView.findViewById(R.id.iv_checked);
                holder.tvDirName = (TextView) convertView.findViewById(R.id.tv_dir_name);
                holder.tvDirCount = (TextView) convertView.findViewById(R.id.tv_dir_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            FolderBean item = getItem(position);
            if (item != null) {
                holder.tvDirName.setText(item.folderName);
                holder.tvDirCount.setText(LList.getCount(item.files) + "");
                ImageLoader.getInstance().display(holder.ivDir, ImageDownLoader.HEAD_FILE+item.coverImagePath);
                holder.ivChecked.setVisibility(checkIndex == position ? View.VISIBLE : View.INVISIBLE);
            }
            return convertView;
        }

        static class ViewHolder {
            ImageView ivDir;
            ImageView ivChecked;
            TextView tvDirName;
            TextView tvDirCount;
        }
    }

    public interface IOnDirectorySelectListener {
        void onDirectorySelectAction(FolderBean bean);
    }
}
