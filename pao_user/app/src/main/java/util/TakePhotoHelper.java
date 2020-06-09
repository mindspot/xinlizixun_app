package util;

import android.net.Uri;

import com.framework.page.Page;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import module.app.MyApplication;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class TakePhotoHelper {
    private TakePhoto takePhoto;

    //是否剪切
    private boolean isShear = true;
    //是否 使用TakePhoto自带相册
    private boolean isWithOwnGallery = false;
    //纠正拍照的照片旋转角度
    private boolean isCorrectImage = true;
    //数量
    private int limit = 1;
    //剪切宽
    private int shearWidth = 400;
    //剪切高
    private int shearHeight = 400;
    //是否使用Take剪切工具
    private boolean isTakeShearUtil = true;
    //是否是按照宽高比
    private boolean isWidthAndHeight = false;
    //是否压缩
    private boolean isCompress = true;
    /**
     * 是否启用像素压缩
     */
    private boolean enablePixelCompress = true;
    //是否显示进度条
    private boolean isShowProgress = true;
    //最大
    private int maxSize = 102400;

    private Page page;

    public TakePhotoHelper(Page page, TakePhoto takePhoto) {
        this.page = page;
        this.takePhoto = takePhoto;
    }

    public Uri getImageUri() {
        String imgPath = MyApplication.app.imgPath;
        File file = new File(imgPath, System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return Uri.fromFile(file);
    }

    private void configTakePhotoOption() {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //是否使用TakePhoto 自带列表
        builder.setWithOwnGallery(isWithOwnGallery);
        //纠正拍照的照片旋转角度
        builder.setCorrectImage(isCorrectImage);
        takePhoto.setTakePhotoOptions(builder.create());

        configCompress();
    }

    private void configCompress() {
        if (!isCompress) {
            return;
        }
        CompressConfig config = new CompressConfig.Builder().setMaxSize(maxSize)
                .enableReserveRaw(isCompress)
                .enablePixelCompress(enablePixelCompress)
                .create();
        takePhoto.onEnableCompress(config, isShowProgress);
    }


    public void openAlbum() {
        configTakePhotoOption();
        if (limit > 1) {
            if (isShear) {
                takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
            } else {
                takePhoto.onPickMultiple(limit);
            }
            return;
        }
        if (isShear) {
            takePhoto.onPickFromGalleryWithCrop(getImageUri(), getCropOptions());
        } else {
            takePhoto.onPickFromGallery();
        }
    }


    public void openCamera() {
        configTakePhotoOption();
        if (isShear) {
            takePhoto.onPickFromCaptureWithCrop(getImageUri(), getCropOptions());
        } else {
            takePhoto.onPickFromCapture(getImageUri());
        }
    }

    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        if (isWidthAndHeight) {
            builder.setOutputX(shearWidth).setOutputY(shearHeight);
        } else {
            builder.setAspectX(shearWidth).setAspectY(shearHeight);
        }
        builder.setWithOwnCrop(isTakeShearUtil);
        return builder.create();
    }

    public File getOriginalImageUrl(TImage image) {
        return new File(image.getOriginalPath());
    }

    public File getCompressImageUrl(TImage image) {
        return new File(image.getOriginalPath());
    }


    public List<File> getOriginalImageUrls(List<TImage> list) {
        List<File> results = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            results.add(new File(list.get(i).getOriginalPath()));
        }
        return results;
    }

    public List<File> getCompressImageUrls(List<TImage> list) {
        List<File> results = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            results.add(new File(list.get(i).getOriginalPath()));
        }
        return results;
    }

    public void setShear(boolean shear) {
        isShear = shear;
    }

    public void setWithOwnGallery(boolean withOwnGallery) {
        isWithOwnGallery = withOwnGallery;
    }

    public void setCorrectImage(boolean correctImage) {
        isCorrectImage = correctImage;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setShearWidth(int shearWidth) {
        this.shearWidth = shearWidth;
    }

    public void setShearHeight(int shearHeight) {
        this.shearHeight = shearHeight;
    }

    public void setTakeShearUtil(boolean takeShearUtil) {
        isTakeShearUtil = takeShearUtil;
    }

    public void setWidthAndHeight(boolean widthAndHeight) {
        isWidthAndHeight = widthAndHeight;
    }

    public void setCompress(boolean compress) {
        isCompress = compress;
    }

    public void setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setEnablePixelCompress(boolean enablePixelCompress) {
        this.enablePixelCompress = enablePixelCompress;
    }
}
