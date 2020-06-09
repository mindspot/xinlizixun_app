package com.lib.image.loader.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;

import java.io.File;
import java.util.Map;

/**
 * 文件名:   GlideImageLoader
 * 描述：    Glide封装
 * <p>
 * http://blog.csdn.net/qq_26787115/article/details/52877997
 * <p>
 * Glide特点
 * 使用简单
 * 可配置度高，自适应程度高
 * 支持常见图片格式 Jpg png gif webp
 * 支持多种数据源  网络、本地、资源、Assets 等
 * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
 * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
 * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
 * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
 */
public class GlideImageLoader {

    private static Map<String, String> httpHeaders;

    public static void init(Map<String, String> headers) {
        GlideImageLoader.httpHeaders = headers;
    }

    private static RequestBuilder loadWithHeaders(Page page, String url) {
        return loadAllWithHeaders(page, url, false, false);
    }

    private static RequestBuilder loadGifWithHeaders(Page page, String url) {
        return loadAllWithHeaders(page, url, true, false);
    }

    private static RequestBuilder loadBitmapWithHeaders(Page page, String url) {
        return loadAllWithHeaders(page, url, false, true);
    }

    private static RequestBuilder loadAllWithHeaders(Page page, String url, boolean asGif, boolean asBitmap) {
        RequestManager requestManager = with(page);
        if (url == null || url.isEmpty() || url.trim().isEmpty() || url.startsWith("file://")) {
            if (asGif)
                requestManager.asGif();
            if (asBitmap)
                requestManager.asBitmap();
            return requestManager.load(url);
        }

        LazyHeaders.Builder headersBuilder = new LazyHeaders.Builder();
        for (Map.Entry<String, String> header : httpHeaders.entrySet()) {
            try {
                headersBuilder.setHeader(header.getKey(), header.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (asGif)
            requestManager.asGif();
        if (asBitmap)
            requestManager.asBitmap();
        return requestManager.load(new GlideUrl(url, headersBuilder.build()));
    }


    private static RequestManager with(Page page) {
        if (page instanceof FragmentActivity) {
            return Glide.with((FragmentActivity) page);
        }
        if (page instanceof Activity) {
            return Glide.with((Activity) page);
        }
        if (page instanceof android.app.Fragment) {
            return Glide.with((android.app.Fragment) page);
        }
        if (page instanceof Fragment) {
            return Glide.with((Fragment) page);
        }
        return Glide.with(page.context());
    }

    //默认加载
    public static void loadImageView(Page page, String path, ImageView mImageView) {
        loadWithHeaders(page, path).into(mImageView);
    }

    //默认加载
    public static void preloadImage(Page page, final String path) {
        loadWithHeaders(page, path).apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                Log.d("preloadImage", "预加载失败：" + path);
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                Log.d("preloadImage", "预加载成功:" + path);
                return false;
            }
        }).preload();
    }


    public static RequestOptions getRequestOptions() {
        return new RequestOptions();
    }

    //加载失败图
    public static void loadImageViewAndError(Page page, String path, ImageView mImageView, int errorImageView) {
        RequestOptions requestOptions = new RequestOptions();
        loadWithHeaders(page, path).apply(getRequestOptions().error(errorImageView)).into(mImageView);
    }

    //加载指定大小
    public static void loadImageViewSize(Page page, String path, int width, int height, ImageView mImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().override(width, height)).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageViewWithLoadingAndFitCenter(Page page, String path, ImageView mImageView, int lodingImage) {
        loadWithHeaders(page, path).apply(getRequestOptions().placeholder(lodingImage).fitCenter()).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageViewWithLoadingAndError(Page page, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().placeholder(lodingImage).error(errorImageView)).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageViewLodingSize(Page page, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().override(width, height).placeholder(lodingImage).error(errorImageView)).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Page page, String path, ImageView mImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().skipMemoryCache(true)).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Page page, String path, ImageView mImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().priority(Priority.NORMAL)).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Page page, String path, ImageView mImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Page page, String path, ImageView mImageView) {
        loadWithHeaders(page, path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public static void loadImageViewCrop(Page page, String path, ImageView mImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().centerCrop()).into(mImageView);
    }

    public static void loadImageViewFitCenter(Page page, String path, ImageView mImageView) {
        loadWithHeaders(page, path).apply(getRequestOptions().fitCenter()).into(mImageView);
    }

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(Page page, String path, ImageView mImageView) {
        loadGifWithHeaders(page, path).into(mImageView);
    }

    //设置静态GIF加载方式
    public static void loadImageViewStaticGif(Page page, String path, ImageView mImageView) {
        loadBitmapWithHeaders(page, path).apply(getRequestOptions().fitCenter()).into(mImageView);
    }

    public static void loadImageViewBitmapAndFitCenter(Page page, String path, SimpleTarget<Bitmap> simpleTarget) {
        loadBitmapWithHeaders(page, path).apply(getRequestOptions().fitCenter()).into(simpleTarget);
    }

    //清理磁盘缓存
    public static void clearDiskCache(Context context) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(context).clearDiskCache();
    }

    //清理内存缓存
    public static void clearMemory(Context context) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(context).clearMemory();
    }

    public static void loadImageLisenter(Page page, String imageUrl, ImageView imageView, RequestListener requestListener) {
        if (Utils.isGifUrl(imageUrl)) {
            loadGifWithHeaders(page, imageUrl).apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .listener(requestListener)
                    .into(imageView);
        } else {
            loadWithHeaders(page, imageUrl).apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter())
                    .listener(requestListener)
                    .into(imageView);
        }
    }


    public static void loadImageCorner(Page page, String imageUrl, ImageView imageView) {
        if (Utils.isGifUrl(imageUrl)) {
            loadGifWithHeaders(page, imageUrl).apply(getRequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(imageView);
        } else {
            loadWithHeaders(page, imageUrl)
                    .apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter())
                    .into(imageView);
        }
    }

    public static void loadImageCorner(Page page, String imageUrl, ImageView imageView, int resID) {
        if (Utils.isGifUrl(imageUrl)) {
            loadGifWithHeaders(page, imageUrl).apply(getRequestOptions().placeholder(resID)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(imageView);
        } else {
            loadWithHeaders(page, imageUrl)
                    .apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter()
                            .placeholder(resID))
                    .into(imageView);
        }
    }

    public static void loadFileImageCustomCorner(Page page, File imageUrl, ImageView imageView, int dp) {
        Glide.with(page.context()).asBitmap().load(imageUrl)
                .apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter()
                        .transform(new GlideRoundedCornersTransform(ConvertUtil.dip2px(page.context(), dp), GlideRoundedCornersTransform.CornerType.ALL)))
                .into(imageView);
    }


    public static void loadGlideImageCustomCorner(Context context, String imageUrl, ImageView imageView, int dp) {
        Glide.with(context).asBitmap().load(imageUrl)
                .apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter()
                        .transform(new GlideRoundedCornersTransform(ConvertUtil.dip2px(context, dp), GlideRoundedCornersTransform.CornerType.ALL)))
                .into(imageView);
    }


    public static void loadImageCustomCorner(Page page, String imageUrl, ImageView imageView, int dp) {
        if (Utils.isGifUrl(imageUrl)) {
            loadGifWithHeaders(page, imageUrl).apply(getRequestOptions().transform(new GlideRoundedCornersTransform(dip2px(page.context(), dp), GlideRoundedCornersTransform.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(imageView);
        } else {
            loadWithHeaders(page, imageUrl)
                    .apply(getRequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter()
                            .transform(new GlideRoundedCornersTransform(dip2px(page.context(), dp), GlideRoundedCornersTransform.CornerType.ALL)))
                    .into(imageView);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}