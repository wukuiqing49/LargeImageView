package com.wu.large;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.wu.large.databinding.LayoutLargeImageviewBinding;
import com.wu.large.util.LargeUtil;
import com.wu.large.util.ScreenUtils;
import com.wu.large.widget.ImageSource;
import com.wu.large.widget.SubsamplingScaleImageView;

import java.io.File;

/**
 * @author wkq
 * @date 2021年08月31日 16:50
 * @des
 */

public class LargeImageView extends RelativeLayout {
    Context mContext;
    boolean isZoom = true;
    private LayoutLargeImageviewBinding binding;

    public LargeImageView(Context context) {
        this(context, null);
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate();
    }

    private void inflate() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_large_imageview, this, false);
        addView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.large.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        binding.large.setMaxScale(10.0f);
        binding.large.setZoomEnabled(isZoom);
    }


    /**
     * 设置Url
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        if (mContext == null || binding == null || TextUtils.isEmpty(imageUrl)) return;
        try {
            if (imageUrl.startsWith("http")) {
                if (imageUrl.endsWith("gif")) {
                    setGif(imageUrl);
                } else {
                    showPhotoView(imageUrl);
                }

            } else {
                if (new File(imageUrl).exists()) {
                    setFilePath(imageUrl);
                } else {
                    showErr();
                }
            }
        } catch (Exception e) {
            showErr();
        }
    }

    /**
     * 设置Url
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl, String previewUrl) {
        if (mContext == null || binding == null || TextUtils.isEmpty(imageUrl)) return;
        showLoading(true);
        try {
            if (imageUrl.startsWith("http")) {
                showPreviewUrl(previewUrl);
                if (imageUrl.endsWith("gif")) {
                    setGif(imageUrl);
                } else {
                    showPhotoView(imageUrl);
                }
            } else {
                if (new File(imageUrl).exists()) {
                    setFilePath(imageUrl);
                } else {
                    showErr();
                }
            }
        } catch (Exception e) {
            showErr();
        }
    }


    private void showPreviewUrl(String previewUrl) {
        if (mContext == null || binding == null || TextUtils.isEmpty(previewUrl)) return;
        showLarge(false);
        setZoom(false);
        RequestOptions option = RequestOptions.centerCropTransform().encodeQuality(60).dontTransform().dontAnimate();
        Glide.with(mContext).load(previewUrl).apply(option).into(binding.ivImage);

    }

    /**
     * 设置图片
     *
     * @param filePath
     */
    public void setFilePath(String filePath) {
        if (mContext == null || binding == null || TextUtils.isEmpty(filePath)) return;
        if (LargeUtil.isNoSupport(filePath)) {
            setImageFilePath(filePath);
        } else if (LargeUtil.isGif(filePath)) {
            setGif(filePath);
        } else {
            showPhotoView(filePath);
        }
    }

    /**
     * 展示 gif
     *
     * @param filePath
     */
    public void setGif(String filePath) {
        if (mContext == null || binding == null || TextUtils.isEmpty(filePath)) return;
        setZoom(false);
        showLoading(true);
        showLarge(false);
        RequestOptions option = new RequestOptions();
        Glide.with(mContext).asGif().load(filePath).apply(option).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                showErr();
                showLoading(false);
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                showLoading(false);
                return false;
            }
        }).into(binding.ivImage);

    }

    /**
     * 设置 resuse资源
     *
     * @param resource
     */
    public void setResource(int resource) {
        if (mContext == null || binding == null || resource <= -1) return;
        showLoading(true);
        showPhotoView(resource);
    }

    /**
     * 设置Bitmap
     *
     * @param bm
     */
    public void setBitmap(Bitmap bm) {
        if (mContext == null || binding == null || bm == null) return;
        showLoading(true);
        try {
            binding.large.setImage(ImageSource.bitmap(bm));
            binding.large.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
                @Override
                public void onReady() {

                }

                @Override
                public void onImageLoaded() {
                    showLoading(false);
                }

                @Override
                public void onPreviewLoadError(Exception e) {
                    showErr();
                }

                @Override
                public void onImageLoadError(Exception e) {
                    showErr();
                }

                @Override
                public void onTileLoadError(Exception e) {
                    showErr();
                }

                @Override
                public void onPreviewReleased() {

                }
            });
        } catch (Exception e) {
            showErr();
        }

    }

    /**
     * 错误图展示
     */
    private void showErr() {
        if (mContext == null || binding == null) return;
        isZoom = false;
        binding.ivImage.setZoomable(false);
        binding.ivImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        showLarge(false);
        Glide.with(mContext).load(R.drawable.iv_new_media_zw).into(binding.ivImage);
        showLoading(false);

    }

    /**
     * 加载动画
     *
     * @param isShow
     */
    private void showLoading(boolean isShow) {
        if (isShow) {
            binding.lvLoading.setVisibility(View.VISIBLE);
        } else {
            binding.lvLoading.setVisibility(View.INVISIBLE);
        }

    }


    private void setImageFilePath(String filePath) {
        if (mContext == null || binding == null) return;
        try {
            showLoading(true);
            if (LargeUtil.isAndroidQ()) {
                binding.large.setImage(ImageSource.uri(LargeUtil.getImageContentUri(mContext, new File(filePath))));
            } else {
                binding.large.setImage(ImageSource.uri(filePath));
            }
            binding.large.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
                @Override
                public void onReady() {

                }

                @Override
                public void onImageLoaded() {
                    showLoading(false);
                }

                @Override
                public void onPreviewLoadError(Exception e) {

                }

                @Override
                public void onImageLoadError(Exception e) {
                    showPhotoView(filePath);
                }

                @Override
                public void onTileLoadError(Exception e) {
                    showPhotoView(filePath);
                }

                @Override
                public void onPreviewReleased() {

                }
            });
            showLarge(true);
        } catch (Exception e) {
            showErr();
        }

    }

    /**
     * 加载错误换 photoview 加载
     *
     * @param filePath
     */
    private void showPhotoView(String filePath) {

        if (mContext == null || binding == null) return;
        Glide.with(mContext).load(filePath).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                showLoading(false);
                showLarge(false);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                setZoom(true);
                showLarge(false);
                showLoading(false);
                return false;
            }
        }).into(binding.ivImage);
    }

    /**
     * 加载错误换 photoview 加载
     *
     * @param resuse
     */
    private void showPhotoView(int resuse) {

        if (mContext == null || binding == null) return;
        showLarge(false);
        Glide.with(mContext).load(resuse).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                showLoading(false);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                showLoading(false);
                return false;
            }
        }).into(binding.ivImage);

    }

    private void showLarge(boolean isShowLarge) {
        if (mContext == null || binding == null) return;
        if (isShowLarge) {
            binding.large.setVisibility(View.VISIBLE);
            binding.ivImage.setVisibility(View.INVISIBLE);
        } else {
            binding.large.setVisibility(View.INVISIBLE);
            binding.ivImage.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 设置是否支持缩放
     *
     * @param zoom
     */
    public void setZoom(boolean zoom) {
        if (mContext == null || binding == null) return;
        isZoom = zoom;
        binding.ivImage.setZoomable(isZoom);
        binding.large.setZoomEnabled(isZoom);
    }
}
