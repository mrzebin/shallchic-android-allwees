package com.hb.basemodel.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hb.basemodel.R;
import com.hb.basemodel.utils.LoggerUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ImageLoader implements IImageLoader {
    private static ImageLoader sInstance;

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader();
                }
            }
        }
        return sInstance;
    }

    /**
     * 加载图片
     */
    @SuppressLint("CheckResult")
    public void displayImage(ImageView target, String url, int errorRes) {
        final Context context = target.getContext();
        if (!checkActivity(context)) {
            return;
        }
        url = checkUrl(url);
        RequestOptions options = new RequestOptions();
        options.error(errorRes).placeholder(errorRes);
        options.skipMemoryCache(true);   //跳过内存
        options.diskCacheStrategy(DiskCacheStrategy.ALL);  //不缓冲disk硬盘中
        options.dontAnimate();

        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url).apply(options);
        Glide.with(context).load(url).apply(options).into(target);
        requestBuilder.into(target);
    }

    public void displayImage(ImageView target,String url,int errorRes,int type){
        final Context context = target.getContext();
        if (!checkActivity(context)) {
            return;
        }
        url = checkUrl(url);
        RequestOptions options = new RequestOptions();
        options.error(errorRes).placeholder(errorRes);

        Glide.with(context).load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        LoggerUtil.i("图片加载失败:" + e.getMessage());
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        LoggerUtil.i("图片加载成功");
                        return false;
                    }
                })
                .apply(options)
                .into(target);
    }

    @Override
    public void displayImage(ImageView target, String url) {
        displayImage(target, url, null);
    }

    @SuppressLint("CheckResult")
    @Override
    public void displayImage(ImageView target, String url, final ImageConfig config) {
        final Context context = target.getContext();
        if (!checkActivity(context)) {
            return;
        }
        url = checkUrl(url);
        RequestOptions options = new RequestOptions();
//        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).dontAnimate();

        if (config == null) {
            options.error(getError(0)).placeholder(getPlaceholder(0));
        } else {
            options.error(getError(config.errorImage)).placeholder(getPlaceholder(config.placeholderImage));
            int width = config.width;
            int height = config.height;
            if (width != 0 && height != 0) {
                options.override(width, height);
            }
            int corner = config.corner;
            if (corner > 0) {
                if (corner > 180) {
                    options.transform(new CircleCrop());
                } else {
                    options.transform(new RoundedCorners(corner));
                }
            }
        }

        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url).apply(options);
//        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url);

        if (config != null && config.listener != null) {
            final LoadListener listener = config.listener;
            requestBuilder.listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    listener.onLoadFailed(context.getResources().getDrawable(getError(0)));
                    return false;
                }
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Bitmap bitmap = null;
                    if (resource instanceof BitmapDrawable) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                        bitmap = bitmapDrawable.getBitmap();
                    }
                    if (resource instanceof GifDrawable) {
                        GifDrawable gifDrawable = (GifDrawable) resource;
                        bitmap = gifDrawable.getFirstFrame();
                    }
                    listener.onLoadSuccess(bitmap, resource);
                    return false;
                }
            });
        }
        requestBuilder.into(target);
    }


    /**
     * 这个方法 会用的很少 在某些情况下 glide的 回调不会执行 原因尚不明确
     */
    @Override
    public void loadImageData(Context context, String url, final LoadListener loadListener) {
        Glide.with(context).load(checkUrl(url)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = null;
                if (resource instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                    bitmap = bitmapDrawable.getBitmap();
                }
                if (resource instanceof GifDrawable) {
                    GifDrawable gifDrawable = (GifDrawable) resource;
                    bitmap = gifDrawable.getFirstFrame();
                }
                if (loadListener != null) loadListener.onLoadSuccess(bitmap, resource);
            }
        });
    }

    private int getPlaceholder(int placeholder) {
        if (placeholder != 0) {
            return placeholder;
        }
        return R.drawable.allwees_ic_default_goods;
    }

    private int getError(int error) {
        if (error != 0) {
            return error;
        }
        return R.drawable.allwees_ic_default_goods;
    }

    private boolean checkActivity(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !activity.isDestroyed();
        }
        return true;
    }

    private String checkUrl(String url) {
        return url;
    }

    public static void clearMemoryCache(Context context){
        // This method must be called on the main thread.
        System.gc();
        // Glide.get(context).clearMemory();
        Glide.get(context).clearMemory();
    }
}
