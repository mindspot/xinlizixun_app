package com.kuangji.paopao.util.excel;


import com.kuangji.paopao.constant.PrefixConstant;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author Lorenzo
 * @location Senzen China
 * @email guiwang007@126.com
 */
public class ImageUtils {
    private static String DEFAULT_PREVFIX = "thumb_";
    private static Boolean DEFAULT_FORCE = false;//建议该值为false
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String getSuffixWithMark(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String genFullFileName(Long userId) {
        return userId + PrefixConstant.SEPARATOR + System.currentTimeMillis();
    }

    /**
     * 使用给定的图片生成指定大小的图片
     */
    private static void generateFixedSizeImage(String source, String destination, int width, int height) {
        try {
            Thumbnails.of(source).size(width, height).toFile(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对原图加水印,然后顺时针旋转90度,最后压缩为80%保存
     */
    private static void generateRotationWatermark(String source, String destination, String watermark) {
        try {
            Thumbnails.of(source).
                    size(160, 160). // 缩放大小
                    rotate(90). // 顺时针旋转90度
                    watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(watermark)), 0.5f). //水印位于右下角,半透明
                    outputQuality(0.8). // 图片压缩80%质量
                    toFile(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换图片格式,将流写入到输出流
     */
    private static void generateOutputstream() {
        try (OutputStream outputStream = new FileOutputStream("data/2016010208_outputstream.png")) { //自动关闭流
            Thumbnails.of("data/2016010208.jpg").
                    size(500, 500).
                    outputFormat("png"). // 转换格式
                    toOutputStream(outputStream); // 写入输出流
        } catch (IOException e) {
            System.out.println("原因: " + e.getMessage());
        }
    }

    /**
     * 按比例缩放图片
     */
    private static void generateScale() {
        try {
            Thumbnails.of("data/2016010208.jpg").
                    //scalingMode(ScalingMode.BICUBIC).
                            scale(0.8). // 图片缩放80%, 不能和size()一起使用
                    outputQuality(0.8). // 图片质量压缩80%
                    toFile("data/2016010208_scale.jpg");
        } catch (IOException e) {
            System.out.println("原因: " + e.getMessage());
        }
    }

    /**
     * 生成缩略图到指定的目录
     */
    private static void generateThumbnail2Directory() {
        try {
            Thumbnails.of("data/2016010208.jpg", "data/meinv.jpg").
                    //scalingMode(ScalingMode.BICUBIC).
                            scale(0.8). // 图片缩放80%, 不能和size()一起使用
                    toFiles(new File("data/new/"), Rename.NO_CHANGE);//指定的目录一定要存在,否则报错
        } catch (IOException e) {
            System.out.println("原因: " + e.getMessage());
        }
    }

    /**
     * 将指定目录下所有图片生成缩略图
     */
    private static void generateDirectoryThumbnail() {
        try {
            Thumbnails.of(new File("data/new").listFiles()).
                    //scalingMode(ScalingMode.BICUBIC).
                            scale(0.8). // 图片缩放80%, 不能和size()一起使用
                    toFiles(new File("data/new/"), Rename.SUFFIX_HYPHEN_THUMBNAIL);//指定的目录一定要存在,否则报错
        } catch (IOException e) {
            System.out.println("原因: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ImageUtils().thumbnailImage("/images/test.jpg", 100, 150, DEFAULT_PREVFIX, DEFAULT_FORCE);
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 依据图片路径生成缩略图 </p>
     *
     * @param imagePath 原图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     * @param prevfix   生成缩略图的前缀
     * @param force     是否强制依照宽高生成缩略图(假设为false，则生成最佳比例缩略图)
     */
    public void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if (imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀所有小写，然后推断后缀是否合法
                if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0) {
                    logger.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return;
                }
                logger.debug("target image's size, width:{}, height:{}.", w, h);
                Image img = null;
                try {
                    img = ImageIO.read(imgFile);
                } catch (IOException e) {

                }
                if (!force) {
                    // 依据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if ((width * 1.0) / w < (height * 1.0) / h) {
                        if (width > w) {
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                            logger.debug("change image's height, width:{}, height:{}.", w, h);
                        }
                    } else {
                        if (height > h) {
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                            logger.debug("change image's width, width:{}, height:{}.", w, h);
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                String p = imgFile.getPath();
                // 将图片保存在原文件夹并加上前缀
                ImageIO.write(bi, suffix, new File(p.substring(0, p.lastIndexOf(File.separator)) + File.separator + prevfix + imgFile.getName()));
                logger.debug("缩略图在原路径下生成成功");
            } catch (IOException e) {
                logger.error("generate thumbnail image failed.", e);
            }
        } else {
            logger.warn("the image is not exist.");
        }
    }
}