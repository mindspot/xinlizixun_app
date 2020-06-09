package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import ui.title.ToolBarTitleView;

public class InputActivity extends BaseActivity {

    @BindView(R.id.input_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.input_activity_edittext)
    EditText edittext;
    @BindView(R.id.input_activity_one_layout)
    LinearLayout oneLayout;
    @BindView(R.id.input_activity_edittext2)
    EditText edittext2;
    @BindView(R.id.input_activity_two_layout)
    LinearLayout twoLayout;
    @BindView(R.id.input_activity_num)
    TextView numText;
    private int mType;
    private String mValue;

    public static void newIntent(Page page, int code, String title, String value) {
        Intent intent = new Intent(page.activity(), InputActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("type", code);
        intent.putExtra("value", value);
        page.startActivityForResult(intent, code);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_input;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            toolbar.setTitle(getIntent().getStringExtra("title"));
            mType = getIntent().getIntExtra("type", 0);
            mValue = getIntent().getStringExtra("value");
        }
        oneLayout.setVisibility(mType == UserInfoActivity.KEY_CODE_SIGN ? View.VISIBLE : View.GONE);
        twoLayout.setVisibility(mType == UserInfoActivity.KEY_CODE_WORK ? View.VISIBLE : View.GONE);
        if (mType == UserInfoActivity.KEY_CODE_SIGN) {
            edittext.setText(mValue);
            edittext.setSelection(mValue.length());
        } else {
            edittext2.setText(mValue);
            edittext2.setSelection(mValue.length());
            numText.setText(5000 - mValue.length() + "");
        }
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if (mType == UserInfoActivity.KEY_CODE_WORK)
                    intent.putExtra("value", edittext2.getText().toString());
                else
                    intent.putExtra("value", edittext.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        edittext2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                numText.setText(5000 - edittext2.getText().length() + "");
            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.input_activity_clear)
    public void onViewClicked() {
        edittext.setText("");
    }
}
