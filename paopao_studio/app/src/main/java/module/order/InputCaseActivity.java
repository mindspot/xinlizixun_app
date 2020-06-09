package module.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;

import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseSelectPhotoActivity;
import butterknife.BindView;
import common.repository.bean.ImageItemBean;
import common.repository.bean.UploadCaseItemBean;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.user.MemberCaseInfoBean;
import common.repository.http.param.FileBean;
import common.repository.http.param.order.EvaluateCouncilorOrderRequestBean;
import common.repository.http.param.order.SubmitUserCaseRequestBean;
import im.IMHolder;
import module.app.MyApplication;
import module.dialog.SelectPhotoDialog;
import ui.MyGridView;
import ui.title.ToolBarTitleView;
import util.ServiceConfig;
import util.TakePhotoHelper;

public class InputCaseActivity extends BaseSelectPhotoActivity {

    @BindView(R.id.input_case_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.input_case_activity_input)
    EditText input;
    @BindView(R.id.input_case_activity_gridView)
    MyGridView gridView;

    private GridViewAddImgesAdpter mAdpter;

    private SelectPhotoDialog selectPhotoDialog;
    private TakePhotoHelper takePhotoHelper;

    private int imageSize = 0;

    private String mOrderNo;

    private MemberCaseInfoBean mBean;

    private boolean isShowImg = false;

    public static void newIntent(Page page, String orderNo) {
        Intent intent = new Intent(page.activity(), InputCaseActivity.class);
        intent.putExtra("orderNo", orderNo);
        page.startActivity(intent);
    }

    public static void newIntent(Page page, String orderNo, MemberCaseInfoBean mBean) {
        Intent intent = new Intent(page.activity(), InputCaseActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("mBean", mBean);
        intent.putExtra("isShowImg", true);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_input_case;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        toolbar.getRight_btn_tv().setTextColor(Color.parseColor("#9DDCAF"));
        mAdpter = new GridViewAddImgesAdpter(this);
        gridView.setAdapter(mAdpter);

        selectPhotoDialog = new SelectPhotoDialog(this);
        takePhotoHelper = new TakePhotoHelper(this, getTakePhoto());
        takePhotoHelper.setShear(false);

        if (getIntent() != null) {
            mOrderNo = getIntent().getStringExtra("orderNo");
            mBean = (MemberCaseInfoBean) getIntent().getSerializableExtra("mBean");
            isShowImg = getIntent().getBooleanExtra("isShowImg", isShowImg);
        }
        gridView.setVisibility(isShowImg ? View.VISIBLE : View.GONE);
        if (isShowImg) {
            input.setHint("可输入文本处方或点击下方“图片”按钮，上传图片处方（例如：来访者咨询时，咨询师的手记笔记等）");
        } else {
            input.setHint("请对本次督导服务做出评价");
        }
        if (mBean != null) {
            if (mBean.getConsultantOrderDiagnosisVO() != null) {
                input.setText(mBean.getConsultantOrderDiagnosisVO().getContent());
                input.setSelection(input.getText().length());
            }
            if (mBean.getConsultantOrderDiagnosisVOs() != null)
                for (MemberCaseInfoBean.ConsultantOrderDiagnosisBean bean : mBean.getConsultantOrderDiagnosisVOs()) {
                    mAdpter.addData(new ImageItemBean(bean.getContent(), bean.getImgeSize()));
                    imageSize++;
                }
        }
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
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
        mAdpter.setOnItemImageListener(new GridViewAddImgesAdpter.OnItemImageListener() {
            @Override
            public void onDelete(int position) {
                imageSize--;
            }

            @Override
            public void onShowBigImg(ImageItemBean bean) {
                IMHolder.getInstance().showBigImage(InputCaseActivity.this, bean.getFile(),
                        bean.getType() == 2 ? bean.getPath().replace(bean.getImgSize(), "") : "");
            }

            @Override
            public void onAddImg() {
                takePhotoHelper.setLimit(3 - imageSize);
                selectPhotoDialog.showDialog();
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        showToast(msg);
    }

    @Override
    public void takeSuccess(final TResult result) {
        super.takeSuccess(result);
        for (File file : takePhotoHelper.getOriginalImageUrls(result.getImages())) {
            mAdpter.addData(new ImageItemBean(file));
            imageSize++;
        }
    }

    public void confirm() {
        String content = input.getText().toString();
        if (!isShowImg && StringUtil.isBlank(content)) {
            showToast("请输入内容");
            return;
        }
        MyApplication.loadingDefault(activity());
        if (isShowImg) {
            imgList = mAdpter.getDatas();
            list.clear();
            uploadImg();
        } else {
            evaluateOrder();
        }
    }

    private List<ImageItemBean> imgList = new ArrayList<>();
    private List<UploadCaseItemBean> list = new ArrayList<>();
    private int uploadImageSize = 0;

    public void uploadImg() {
        if (imageSize == 0) {
            uploadAnli();
            return;
        }
        ImageItemBean imageItemBean = mAdpter.getDatas().get(0);
        if (imageItemBean.getType() == 1) {
            imageSize--;
            imgList.remove(0);
            uploadImg();
            return;
        } else if (imageItemBean.getType() == 2) {
            UploadCaseItemBean bean = new UploadCaseItemBean();
            bean.setContentType(1);
            bean.setContent(imageItemBean.getPath().replace(ServiceConfig.SERVICE_UPLOAD_IMAGE_HOST_URL, "")
                    .replace(imageItemBean.getImgSize(), ""));
            list.add(bean);
            imageSize--;
            imgList.remove(0);
            uploadImg();
            return;
        }
        MyApplication.loadingDefault(activity());
        FileBean fileBean = new FileBean();
        fileBean.setUpLoadKey("file");
        fileBean.setFileSrc(imgList.get(0).getFile().getAbsolutePath());
        fileBean.addExtraParms("output", "json");
        fileBean.addExtraParms("path", "/images");
        fileBean.addExtraParms("scene", "image");
        HttpApi.app().uploadImage(this, fileBean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                uploadImageSize = 0;
                imageSize--;
                imgList.remove(0);
                UploadCaseItemBean bean = new UploadCaseItemBean();
                bean.setContentType(1);
                bean.setContent(data);
                list.add(bean);
                uploadImg();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                uploadImageSize++;
                if (uploadImageSize == 3) {
                    imageSize--;
                    imgList.remove(0);
                    uploadImg();
                }
            }
        });
    }

    public void uploadAnli() {
        UploadCaseItemBean bean = new UploadCaseItemBean();
        bean.setContentType(0);
        bean.setContent(input.getText().toString());
        list.add(bean);
        SubmitUserCaseRequestBean userCaseRequestBean = new SubmitUserCaseRequestBean();
        userCaseRequestBean.setOrderNo(mOrderNo);
        userCaseRequestBean.setList(list);
        HttpApi.app().submitUserCase(this, userCaseRequestBean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    public void evaluateOrder() {
        EvaluateCouncilorOrderRequestBean bean = new EvaluateCouncilorOrderRequestBean();
        bean.setOrderNo(mOrderNo);
        bean.setContent(input.getText().toString());
        HttpApi.app().evaluateCouncilorOrder(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }
}
