package util;

import android.graphics.Bitmap;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by hpzhan on 2019/4/20.
 */

public class ShapeUtils {

    public Page page;

    public ShapeUtils(Page page) {
        this.page = page;
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(page.context()).setShareConfig(config);
    }

    public void shareText(String text, SHARE_MEDIA platform) {
        new ShareAction(page.activity())
                .setPlatform(platform)//传入平台
                .withText(text)//分享内容
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    public void shareImage(String url, SHARE_MEDIA platform) {
        UMImage image = new UMImage(page.context(), url);//网络图片
        new ShareAction(page.activity())
//                .withText(page.activity().getResources().getString(R.string.app_name))
                .withMedia(image)
                .setPlatform(platform)//传入平台
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    public void shareBitmapImage(Bitmap bitmap, SHARE_MEDIA platform) {
        UMImage image = new UMImage(page.context(), bitmap);//网络图片
        new ShareAction(page.activity())
                .withMedia(image)
                .setPlatform(platform)//传入平台
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    public void shareWebUrl(String url, String title, String description, SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        new ShareAction(page.activity())
                .withMedia(web)
                .setPlatform(platform)//传入平台
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    public void shareWebURL(String url, String title, String description) {
        UMWeb web = new UMWeb(url);
        if (StringUtil.isBlank(title)) {
            title = page.context().getResources().getString(R.string.app_name);
        }
        if (StringUtil.isBlank(description)) {
            description = "专注做有效的心理咨询";
        }
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        new ShareAction(page.activity())
                .withText(title).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(web)
                .setCallback(umShareListener)
                .open();
    }

    public void shareWebURL(String url, String title, String description, String imgurl) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (!StringUtil.isBlank(imgurl))
            web.setThumb(new UMImage(page.context(), imgurl));
        new ShareAction(page.activity())
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.MORE)
                .withMedia(web)
                .setCallback(umShareListener)
                .open();
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //MyApplication.loadingDefault(page.activity());
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            //MyApplication.hideLoading();
            page.showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            //MyApplication.hideLoading();
            page.showToast("分享失败\n" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //MyApplication.hideLoading();
            page.showToast("分享取消");
        }
    };


}