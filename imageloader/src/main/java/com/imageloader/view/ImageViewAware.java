package com.imageloader.view;

import android.graphics.Bitmap;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 作者：guofeng
 * 日期:16/11/25
 * <p>
 * Add ImageView to this, this class provides all values,
 * <b>getWidth()<b/>getImageView
 * <b>getHeight()<b/>getImageViewHeight
 * <b>getId()<b/> get imageView hashCode
 */

public class ImageViewAware implements BaseRenderingView {

    private Reference<View> viewReference;

    public ImageViewAware(View view) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        }
        viewReference = new WeakReference<>(view);
    }

    @Override
    public int getWidth() {
        View view = viewReference.get();
        if (view != null) {
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            int width = 0;
            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = view.getWidth();
            }
            if (width <= 0 && params != null) {
                width = params.width;
            }
            if (width <= 0) {
                width = getReflectWidth(view);
            }
            if (width <= 0) {
                width = getScreenWidth(view);
            }
            return width;
        }
        return 0;
    }

    @Override
    public int getHeight() {
        View view = viewReference.get();
        if (view != null) {
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            int height = 0;
            if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = view.getHeight();
            }
            if (height <= 0 && params != null) {
                height = params.height;
            }
            if (height <= 0) {
                height = getReflectHeight(view);
            }
            if (height <= 0) {
                height = getScreenHeight(view);
            }
            return height;
        }
        return 0;
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            View view = getImageView();
            if (view != null && view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(bitmap);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setImageResource(int resource) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            View view = getImageView();
            if (view != null && view instanceof ImageView) {
                ((ImageView) view).setImageResource(resource);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getId() {
        return getImageView() == null ? super.hashCode() : getImageView().hashCode();
    }

    @Override
    public boolean isCollected() {
        return viewReference.get() == null;
    }

    @Override
    public View getImageView() {
        return viewReference.get();
    }

    /**
     * get Reflect width
     *
     * @return
     */
    private int getReflectWidth(View view) {
        return getImageViewFieldValue(view, "mMaxWidth");
    }

    /**
     * get Reflect height
     *
     * @param view
     * @return
     */
    private int getReflectHeight(View view) {
        return getImageViewFieldValue(view, "mMaxHeight");
    }

    /**
     * get Screen width as container width
     *
     * @param view
     * @return
     */
    private int getScreenWidth(View view) {
        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * get Sceen height as container height
     *
     * @param view
     * @return
     */
    private int getScreenHeight(View view) {
        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }


    /**
     * Reflect ImageView to get ImageView width and height
     *
     * @param object
     * @param fieldName
     * @return
     */
    private int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        Field field;
        try {
            field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }
}
