package com.framework.component.ui;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.framework.R;
import com.framework.utils.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

public class RightClearEditText extends EditText {
    // 图标的偏移位置
    private int x = 0;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;
    /**
     * 是否弹出自定义键盘，如果是的话，那么touch事件需要改动
     * <br/>true的话在重写touch事件的时候可以return false
     */
    private boolean isBindToCustomerKb;

    public RightClearEditText(Context context) {
        this(context, null);
        addTextViewEvent();
    }

    public RightClearEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
        addTextViewEvent();
    }

    public RightClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        addTextViewEvent();
    }

    private void init(Context context) {
        // 设置清除图标
        mClearDrawable = getResources().getDrawable(R.drawable.icon_edittext_clear);
        // 设置图标位置
        x = ConvertUtil.dip2px(context, 8);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
//		setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
//		addTextChangedListener(this);
        // 默认没绑定
        isBindToCustomerKb = false;
    }


    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - x - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - x - getPaddingRight())));
                if (touchable)
                    this.setText("");
            }
        }

        if (isBindToCustomerKb)
            return true;

        return super.onTouchEvent(event);
    }

//	@Override
//	public void onFocusChange(View v, boolean hasFocus) {
//		this.hasFoucs = hasFocus;
//        if (hasFocus) {
//            setClearIconVisible(getText().length() > 0);
//        } else {
//            setClearIconVisible(false);
//        }
//	}


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    private void addTextViewEvent() {
        this.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (hasFoucs)
                    setClearIconVisible(s.length() > 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        super.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (focusListeners != null) {
                    for (int i = 0; i < focusListeners.size(); i++) {
                        focusListeners.get(i).onFocusChange(v, hasFocus);
                    }
                }

                RightClearEditText.this.hasFoucs = hasFocus;
                if (hasFocus) {
                    setClearIconVisible(getText().length() > 0);
                } else {
                    setClearIconVisible(false);
                }
            }
        });
    }


    List<OnFocusChangeListener> focusListeners;

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        if (focusListeners == null)
            focusListeners = new ArrayList<OnFocusChangeListener>();
        focusListeners.add(l);
    }


//    /** 当输入框里面内容发生变化的时候回调的方法  */  
//    @Override   
//    public void onTextChanged(CharSequence s, int start, int count, int after) {   
//    	if(hasFoucs)
//    		setClearIconVisible(s.length() > 0);  
//    } 
//	
//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//	@Override
//	public void afterTextChanged(Editable s) {}

    public boolean isBindToCustomerKb() {
        return isBindToCustomerKb;
    }

    public void setBindToCustomerKb(boolean isBindToCustomerKb) {
        this.isBindToCustomerKb = isBindToCustomerKb;
    }
}

