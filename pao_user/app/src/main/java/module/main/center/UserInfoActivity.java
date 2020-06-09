package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.hyphenate.easeui.sqlite.UserTabUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import org.devio.takephoto.model.TResult;

import java.io.File;

import base.BaseSelectPhotoActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.user.UserInfoResponseBean;
import common.repository.http.param.FileBean;
import common.repository.http.param.user.SaveUserInfoRequestBean;
import common.repository.share_preference.SPApi;
import module.app.MyApplication;
import module.dialog.SelectMarriageDialog;
import module.dialog.SelectPhotoDialog;
import module.dialog.SelectSexDialog;
import ui.title.ToolBarTitleView;
import util.TakePhotoHelper;

public class UserInfoActivity extends BaseSelectPhotoActivity {

    @BindView(R.id.userinfo_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.userinfo_activity_photo)
    ImageView photoImg;
    @BindView(R.id.userinfo_activity_name)
    TextView name;
    @BindView(R.id.userinfo_activity_sign)
    TextView sign;
    @BindView(R.id.userinfo_activity_sex)
    TextView sexText;
    @BindView(R.id.userinfo_activity_hunyin)
    TextView hunyin;
    @BindView(R.id.userinfo_activity_zhiye)
    TextView zhiye;

    public static final int KEY_CODE_NAME = 50001;
    public static final int KEY_CODE_SIGN = 50002;
    public static final int KEY_CODE_WORK = 50003;

    private TakePhotoHelper takePhotoHelper;
    private SelectPhotoDialog selectPhotoDialog;
    private SelectSexDialog selectSexDialog;
    private SelectMarriageDialog marriageDialog;

    private UserInfoResponseBean mData;

    private String mPhotoUrl;

    private boolean isChangePhoto = false;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), UserInfoActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        takePhotoHelper = new TakePhotoHelper(this, getTakePhoto());
        selectPhotoDialog = new SelectPhotoDialog(this);
        selectSexDialog = new SelectSexDialog(this);
        marriageDialog = new SelectMarriageDialog(this);
    }

    @Override
    public void initListener() {
        selectPhotoDialog.setOnOpenClickListener(new SelectPhotoDialog.OnOpenClickListener() {
            @Override
            public void OnOpenCarem() {
                takePhotoHelper.openCamera();
            }

            @Override
            public void OnOpenAlbum() {
                takePhotoHelper.openAlbum();
            }
        });
        selectSexDialog.setOnSexClickListener(new SelectSexDialog.OnSexClickListener() {
            @Override
            public void onClick(int sex) {
                sexText.setText(sex == SelectSexDialog.SEX_MAN ? "男" : (sex == SelectSexDialog.SEX_WOMAN ? "女" : "隐私"));
            }
        });
        marriageDialog.setOnSelectClickListener(new SelectMarriageDialog.OnSelectClickListener() {
            @Override
            public void onClick(int index, String value) {
                hunyin.setText(value);
            }
        });
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    @Override
    public void loadData() {
        getData();
    }

    public void getData() {
        MyApplication.loadingDefault(activity());
        HttpApi.app().getUserInfo(this, new HttpCallback<UserInfoResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UserInfoResponseBean data) {
                MyApplication.hideLoading();
                mData = data;
                setView();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }

    public void setView() {
        if (mData == null) {
            return;
        }
        GlideImageLoader.loadImageCustomCorner(
                this, mData.getHeadImg(), photoImg, ConvertUtil.dip2px(context(), 23));
        name.setText(mData.getRealName());
        sign.setText(mData.getSendWord());
        sexText.setText(mData.getSex() == 0 ? "男" : (mData.getSex() == 1 ? "女" : "隐私"));
        hunyin.setText(mData.getMaritalStatus() == 0 ? "未婚" : (mData.getMaritalStatus() == 1 ? "已婚" : "隐私"));
        zhiye.setText(mData.getOccupation());
        mPhotoUrl = mData.getHeadImg();
        SPApi.user().setLoginRealName(mData.getRealName());
        SPApi.user().setUserPhoto(mData.getHeadImg());
        UserTabUtil.uploadUser(SPApi.user().getUserIMAccount(), mData.getRealName(), mData.getHeadImg(), true);
    }

    public void saveData() {
        MyApplication.loadingDefault(activity());
        SaveUserInfoRequestBean bean = new SaveUserInfoRequestBean();
        final String nameStr = name.getText().toString();
        bean.setRealName(StringUtil.isBlank(nameStr) ? null : nameStr);
        final String signStr = sign.getText().toString();
        bean.setSendWord(StringUtil.isBlank(signStr) ? null : signStr);
        int sexIndex = sexText.getText().toString().equals("男") ? 0 : (sexText.getText().toString().equals("女") ? 1 : 2);
        bean.setSex(sexIndex);
        int hunyinIndex = hunyin.getText().toString().equals("未婚") ? 0
                : (hunyin.getText().toString().equals("已婚") ? 1 : 2);
        bean.setMaritalStatus(hunyinIndex);
        String zhiyeStr = zhiye.getText().toString();
        bean.setOccupation(StringUtil.isBlank(zhiyeStr) ? null : zhiyeStr);
        if (!StringUtil.isBlank(mPhotoUrl) && isChangePhoto) {
            bean.setHeadImg(mPhotoUrl);
            isChangePhoto = false;
        }
        HttpApi.app().saveUserInfo(this, bean, new HttpCallback<UserInfoResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UserInfoResponseBean data) {
                MyApplication.hideLoading();
                SPApi.user().setLoginRealName(nameStr);
                SPApi.user().setSendWork(signStr);
                SPApi.user().setUserPhoto(data.getHeadImg());
                UserTabUtil.uploadUser(SPApi.user().getUserIMAccount(), data.getRealName(), data.getHeadImg(), true);
                if (!StringUtil.isBlank(data.getHeadImg()))
                    SPApi.user().setUserPhoto(data.getHeadImg());
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    @OnClick({R.id.userinfo_activity_photo_layout, R.id.userinfo_activity_name_layout, R.id.userinfo_activity_sign_layout, R.id.userinfo_activity_sex_layout, R.id.userinfo_activity_hunyin_layout, R.id.userinfo_activity_zhiye_lauout})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.userinfo_activity_photo_layout:
                selectPhotoDialog.showDialog();
                break;
            case R.id.userinfo_activity_name_layout:
                InputActivity.newIntent(this, KEY_CODE_NAME, "修改昵称", name.getText().toString());
                break;
            case R.id.userinfo_activity_sign_layout:
                InputActivity.newIntent(this, KEY_CODE_SIGN, "个性签名", sign.getText().toString());
                break;
            case R.id.userinfo_activity_sex_layout:
                selectSexDialog.setData();
                break;
            case R.id.userinfo_activity_hunyin_layout:
                marriageDialog.setData();
                break;
            case R.id.userinfo_activity_zhiye_lauout:
                InputActivity.newIntent(this, KEY_CODE_WORK, "职业填写", zhiye.getText().toString());
                break;
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        showToast(msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        final File photoFile = takePhotoHelper.getCompressImageUrl(result.getImage());
        GlideImageLoader.loadFileImageCustomCorner(this, photoFile, photoImg, ConvertUtil.dip2px(context(), 23));
        uploadPhoto(photoFile);
    }


    public void uploadPhoto(File photoFile) {
        MyApplication.loadingDefault(activity());
        FileBean fileBean = new FileBean();
        fileBean.setUpLoadKey("file");
        fileBean.setFileSrc(photoFile.getAbsolutePath());
        fileBean.addExtraParms("output", "json");
        fileBean.addExtraParms("path", "/images");
        fileBean.addExtraParms("scene", "image");
        HttpApi.app().uploadImage(this, fileBean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                isChangePhoto = true;
                mPhotoUrl = data;
                MyApplication.hideLoading();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == KEY_CODE_NAME) {
                String value = data.getStringExtra("value");
                name.setText(StringUtil.isBlank(value) ? "" : value);
            }
            if (requestCode == KEY_CODE_SIGN) {
                String value = data.getStringExtra("value");
                sign.setText(StringUtil.isBlank(value) ? "" : value);
            }
            if (requestCode == KEY_CODE_WORK) {
                String value = data.getStringExtra("value");
                zhiye.setText(StringUtil.isBlank(value) ? "" : value);
            }
        }
    }
}
