package ui.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paopao.paopaouser.R;
import com.socks.library.KLog;

import ui.bar.WebViewProgressBar;
import util.Tool;
import util.ToolBarUtil;

public class ToolBarTitleView extends LinearLayout {
    private LinearLayout parentView_ll;//父部局
    private View toolBar_v;//状态视图
    private RelativeLayout title_rl;
    private LinearLayout left_title_ll;
    private LinearLayout right_title_ll;
    private ImageView left_btn_iv;
    private TextView left_btn_tv;
    private ImageView cloose_btn_iv;
    private TextView title_tv;
    private ImageView right_btn_iv;
    private TextView right_btn_tv;
    private View bottom_line_v;
    private WebViewProgressBar progress_bar_h;

    private ToolBarUtil toolBarUtil;


    private static final int WHITE = 100;//有标题栏 白色
    private static final int IMG = 101;//有标题栏 图片
    private static final int TRAN_WHITE = 102;//有标题栏 白色字体 状态栏透明
    private static final int TRAN_BLCAK = 103;//有标题栏 黑色字体 状态栏透明
    private static final int THEME = 104;//有标题栏 主题色
    private static final int NO_TITLE_WHITE = 105;//无标题 白色字体 状态栏透明
    private static final int NO_TITLE_BLCAK = 106;//无标题 黑色字体 状态栏透明
    private static final int WHITEBG_BLACKTEXT = 107;//有标题栏 白色背景 黑色字体 灰色状态栏
    private static final int COLOR_WHITE = 108;//有标题栏 设置背景颜色 状态栏白色字体
    private static final int COLOR_BLACK = 109;//有标题栏 设置背景颜色 状态栏黑色字体

    private int DEFULT_STYLE = IMG;
    private int DEFUT_IMG_BG;

    private int titleBarBackGround;

    private Context context;

    public ToolBarTitleView(Context context) {
        this(context, null);
    }

    public ToolBarTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBarTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        toolBarUtil = new ToolBarUtil((Activity) context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.common_navigation_status_title_layout, this, true);
        initViews();
        hidenAll();
        initStyle(attrs);
        setHeight();
    }

    /**
     * 初始化视图
     */
    public void initViews() {
        parentView_ll = (LinearLayout) findViewById(R.id.parentView_ll);
        toolBar_v = (View) findViewById(R.id.toolBar_v);
        title_rl = (RelativeLayout) findViewById(R.id.title_rl);
        left_title_ll = (LinearLayout) findViewById(R.id.left_title_ll);
        right_title_ll = (LinearLayout) findViewById(R.id.right_title_ll);
        left_btn_iv = (ImageView) findViewById(R.id.left_btn_iv);
        left_btn_tv = (TextView) findViewById(R.id.left_btn_tv);
        cloose_btn_iv = (ImageView) findViewById(R.id.cloose_btn_iv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        right_btn_iv = (ImageView) findViewById(R.id.right_btn_iv);
        right_btn_tv = (TextView) findViewById(R.id.right_btn_tv);
        bottom_line_v = (View) findViewById(R.id.bottom_line_v);
        progress_bar_h = (WebViewProgressBar) findViewById(R.id.progress_bar_h);

    }

    public void initStyle(AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ToolBarTitleView);
        if (attributes != null) {
            //处理titleBar背景色
            titleBarBackGround = attributes.getResourceId(R.styleable.ToolBarTitleView_title_background, -1);

            DEFULT_STYLE = attributes.getInt(R.styleable.ToolBarTitleView_defult_style, DEFULT_STYLE);

            switch (DEFULT_STYLE) {
                case WHITE:
                    initWhiteBg();
                    break;
                case IMG:
                    if (titleBarBackGround == -1) {
                        initImgBg(DEFUT_IMG_BG);
                    } else {
                        initImgBg(titleBarBackGround);
                    }
                    break;
                case TRAN_WHITE:
                    initTransparentWhiteTextBg();
                    break;
                case TRAN_BLCAK:
                    initTransparentBlackTextBg();
                    break;
                case NO_TITLE_WHITE:
                    initNoTitleWhiteTextBg();
                    break;
                case NO_TITLE_BLCAK:
                    initNoTitleBlackTextBg();
                    break;
                case WHITEBG_BLACKTEXT:
                    initWhiteBgWhiteText();
                    break;
                case COLOR_WHITE:
                    initColorBgWhiteText();
                    break;
                case COLOR_BLACK:
                    initColorBgBlackText();
                    break;
                case THEME:
                default:
                    initThemeBg();
                    break;
            }

            //标题
            String title_text = attributes.getString(R.styleable.ToolBarTitleView_title_text);
            if (!TextUtils.isEmpty(title_text)) {
                setTitle(title_text);
            }

            //设置左边按钮的文字
            String leftButtonText = attributes.getString(R.styleable.ToolBarTitleView_left_button_text);
            if (!TextUtils.isEmpty(leftButtonText)) {
                setLeftButtonText(leftButtonText);
                //设置左边按钮文字颜色
                int leftButtonTextColor = attributes.getColor(R.styleable.ToolBarTitleView_left_button_text_color, -1);
                if (leftButtonTextColor != -1) {
                    left_btn_tv.setTextColor(leftButtonTextColor);
                }
            }
            //设置左边图片icon
            int leftButtonDrawable = attributes.getResourceId(R.styleable.ToolBarTitleView_left_button_drawable, -1);
            if (leftButtonDrawable != -1) {
                setLeftButtonImg(leftButtonDrawable);
            }

            //先处理右边按钮
            //设置右边按钮的文字
            String rightButtonText = attributes.getString(R.styleable.ToolBarTitleView_right_button_text);
            if (!TextUtils.isEmpty(rightButtonText)) {
                setRightButtonText(rightButtonText);
                //设置右边按钮文字颜色
                int rightButtonTextColor = attributes.getColor(R.styleable.ToolBarTitleView_right_button_text_color, -1);
                if (rightButtonTextColor != -1) {
                    right_btn_tv.setTextColor(rightButtonTextColor);
                }
            }
            //设置右边图片icon 这里是二选一 要么只能是文字 要么只能是图片
            int rightButtonDrawable = attributes.getResourceId(R.styleable.ToolBarTitleView_right_button_drawable, -1);
            if (rightButtonDrawable != -1) {
                setRightButtonImg(rightButtonDrawable);
            }

            //处理关闭按钮
            boolean close_iv_visible = attributes.getBoolean(R.styleable.ToolBarTitleView_close_iv_visible, false);
            if (close_iv_visible) {
                showCloseImg();
            }

            //显示底部line
            boolean bottom_line_visible = attributes.getBoolean(R.styleable.ToolBarTitleView_bottom_line_visible, false);
            if (bottom_line_visible) {
                showBottomLine();
            } else {
                hideBottomLine();
            }
            attributes.recycle();
        }
    }

    /**
     * 隐藏所有视图
     */

    public void hidenAll() {
        left_btn_iv.setVisibility(View.GONE);
        left_btn_tv.setVisibility(View.GONE);
        cloose_btn_iv.setVisibility(View.GONE);
        title_tv.setVisibility(View.GONE);
        right_btn_tv.setVisibility(View.GONE);
        right_btn_iv.setVisibility(View.GONE);
        bottom_line_v.setVisibility(View.INVISIBLE);
        progress_bar_h.setVisibility(View.GONE);
    }

    /**
     * 设置 透明背景 白色字体
     */
    public void initTransparentWhiteTextBg() {
        setToolBarTitleViewColorBg(R.color.transparent);
        setTextColor(R.color.global_white_color);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(false);
    }

    /**
     * 设置 透明背景 黑色字体
     */
    public void initTransparentBlackTextBg() {
        setToolBarTitleViewColorBg(R.color.transparent);
        setTextColor(R.color.global_black_color);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(true);
    }

    public void initTransparentNoTitleBlackTextBg() {
        title_rl.setVisibility(GONE);
        setToolBarTitleViewColorBg(R.color.global_white_color);
        setTextColor(R.color.global_black_color);
        toolBar_v.setBackgroundColor(getResources().getColor(R.color.global_white_color));
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(true);
        hideBottomLine();
    }

    /**
     * 设置 透明背景 黑色字体
     */
    public void initNoTitleBlackTextBg() {
        title_rl.setVisibility(GONE);
        setToolBarTitleViewColorBg(R.color.transparent);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(true);
    }

    /**
     * 设置 透明背景 白色字体
     */
    public void initNoTitleWhiteTextBg() {
        title_rl.setVisibility(GONE);
        setToolBarTitleViewColorBg(R.color.transparent);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(false);
    }

    /**
     * 设置 主题色背景
     */
    public void initThemeBg() {
        setToolBarTitleViewColorBg(R.color.theme_color);
        setTextColor(R.color.global_white_color);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(false);
    }

    /**
     * 设置 白色背景
     */
    public void initWhiteBg() {
        setToolBarTitleViewColorBg(R.color.global_white_color);
        setTextColor(R.color.theme_color);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(true);
    }

    /**
     * 设置 图片背景
     */
    public void initImgBg(int imgId) {
        setToolBarTitleViewImageBg(imgId);
        setTextColor(R.color.global_white_color);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(false);
    }

    /**
     * 白色背景 黑色文字 灰色Bar
     */
    public void initWhiteBgWhiteText() {
        setToolBarTitleViewColorBg(R.color.global_white_color);
        setTextColor(R.color.global_black_color);
        toolBar_v.setBackgroundColor(getResources().getColor(R.color.titlebar_toolbar_bg_color));
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(false);
        showBottomLine();
    }

    /**
     * 设置颜色背景 白色文字
     */
    public void initColorBgWhiteText() {
        setToolBarTitleViewColorBg(titleBarBackGround);
        setTextColor(R.color.global_white_color);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(false);
    }

    /**
     * 白色背景 黑色文字
     */
    public void initColorBgBlackText() {
        setToolBarTitleViewColorBg(titleBarBackGround);
        setTextColor(R.color.global_black_color);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(true);
    }

    /**
     * 设置状态栏高度
     */
    public void setHeight() {
        title_rl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int statusBarHeight = Tool.getStatusBarHeight(context);
                KLog.d("ToolBar", "Title Height " + title_rl.getHeight() + " Parent Height " + parentView_ll.getHeight() + " ToolBar Height " + toolBar_v.getHeight() + " SYSBAR Height" + statusBarHeight);
                title_rl.getViewTreeObserver().removeOnPreDrawListener(this);
                LayoutParams p_params = (LayoutParams) parentView_ll.getLayoutParams();//获取当前控件的布局对象
                p_params.height = statusBarHeight + title_rl.getHeight();//设置当前控件布局的高度
                parentView_ll.setLayoutParams(p_params);
                LayoutParams tool_params = (LayoutParams) toolBar_v.getLayoutParams();//获取当前控件的布局对象
                tool_params.height = statusBarHeight;//设置当前控件布局的高度
                toolBar_v.setLayoutParams(tool_params);
                KLog.d("ToolBar", "Title Height " + title_rl.getHeight() + " Parent Height " + parentView_ll.getHeight() + " ToolBar Height " + toolBar_v.getHeight() + " SYSBAR Height" + statusBarHeight);
                return true;
            }
        });
    }

    public ToolBarUtil getToolBarUtil() {
        return toolBarUtil;
    }

    /**
     * 返回 状态栏+导航栏高度
     *
     * @return
     */
    public int getToolBarTitleViewHeight() {
        return parentView_ll.getHeight();
    }

    /**
     * 设置 背景色
     *
     * @param bgid
     */
    public void setToolBarTitleViewImageBg(@DrawableRes int bgid) {
        if (parentView_ll != null) {
            parentView_ll.setBackgroundDrawable(getContext().getResources().getDrawable(bgid));
        }
    }

    public void addCustomHeader(View headerView) {
        title_rl.addView(headerView, title_rl.getChildCount());
    }

    /**
     * 设置 背景色
     *
     * @param bgid
     */
    public void setToolBarTitleViewColorBg(@ColorRes int bgid) {
        if (parentView_ll != null) {
            parentView_ll.setBackgroundColor(getContext().getResources().getColor(bgid));
        }
    }

    public void setToolBarTitleViewColor(@ColorInt int color) {
        if (parentView_ll != null) {
            parentView_ll.setBackgroundColor(color);
        }
    }


    public void setBottomViewPaddingTop(View view, boolean isPadding) {
        if (isPadding) {
            view.setPadding(0, getToolBarTitleViewHeight(), 0, 0);
        } else {
            view.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 设置字体颜色
     *
     * @param colorRes
     */
    public void setTextColor(@ColorRes int colorRes) {
        int color = getResources().getColor(colorRes);
        left_btn_tv.setTextColor(color);
        title_tv.setTextColor(color);
        right_btn_tv.setTextColor(color);
    }

    public void setTitle(String text) {
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(text);
    }

    public void setTitle(int textid) {
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(textid);
    }

    public void setLeftButtonText(String text) {
        left_btn_tv.setVisibility(View.VISIBLE);
        left_btn_tv.setText(text);
    }

    public void setLeftButtonText(int textid) {
        left_btn_tv.setVisibility(View.VISIBLE);
        left_btn_tv.setText(textid);
    }

    public void setLeftButtonImg(int imgId) {
        left_btn_iv.setVisibility(View.VISIBLE);
        left_btn_iv.setImageResource(imgId);
    }

    public void setLeftClickListener(OnClickListener listener) {
        left_title_ll.setOnClickListener(listener);
    }

    public void setRightButtonText(String text) {
        right_btn_tv.setVisibility(View.VISIBLE);
        right_btn_tv.setText(text);
    }

    public void setRightButtonText(int textid) {
        right_btn_tv.setVisibility(View.VISIBLE);
        right_btn_tv.setText(textid);
    }

    public void setRightButtonImg(int imgId) {
        right_btn_iv.setVisibility(View.VISIBLE);
        right_btn_iv.setImageResource(imgId);
    }

    public void setRightClickListener(OnClickListener listener) {
        right_title_ll.setOnClickListener(listener);
    }

    public void setRightImageClickListener(OnClickListener listener) {
        right_btn_iv.setOnClickListener(listener);
    }

    /**
     * 隐藏底部线
     */
    public void hideBottomLine() {
        bottom_line_v.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示底部线
     */
    public void showBottomLine() {
        bottom_line_v.setVisibility(View.VISIBLE);
    }


    public void setCloseImgListener(OnClickListener listener) {
        cloose_btn_iv.setOnClickListener(listener);
    }

    public void showCloseImg() {
        cloose_btn_iv.setVisibility(View.VISIBLE);
    }

    public void hideCloseImg() {
        cloose_btn_iv.setVisibility(View.GONE);
    }

    public void showProgress() {
        progress_bar_h.setVisibility(View.VISIBLE);
    }


    public void hideProgress() {
        progress_bar_h.setVisibility(View.GONE);
    }


    public void setAnimProgress(int progress) {
        progress_bar_h.setVisibility(View.VISIBLE);
        progress_bar_h.setAnimProgress2(progress);
    }

    public LinearLayout getParentView_ll() {
        return parentView_ll;
    }

    public View getToolBar_v() {
        return toolBar_v;
    }

    public RelativeLayout getTitle_rl() {
        return title_rl;
    }

    public LinearLayout getLeft_title_ll() {
        return left_title_ll;
    }

    public LinearLayout getRight_title_ll() {
        return right_title_ll;
    }

    public ImageView getLeft_btn_iv() {
        return left_btn_iv;
    }

    public TextView getLeft_btn_tv() {
        return left_btn_tv;
    }

    public ImageView getCloose_btn_iv() {
        return cloose_btn_iv;
    }

    public TextView getTitle_tv() {
        return title_tv;
    }

    public ImageView getRight_btn_iv() {
        return right_btn_iv;
    }

    public TextView getRight_btn_tv() {
        return right_btn_tv;
    }

    public View getBottom_line_v() {
        return bottom_line_v;
    }

    public WebViewProgressBar getProgress_bar_h() {
        return progress_bar_h;
    }
}
