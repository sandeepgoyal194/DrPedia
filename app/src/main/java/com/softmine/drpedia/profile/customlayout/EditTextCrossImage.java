package com.softmine.drpedia.profile.customlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.softmine.drpedia.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class EditTextCrossImage extends RelativeLayout {
    @BindView(R.id.edt_txt)
    EditText mEditText;
    @BindView(R.id.btn_clear)
    Button mButtonCross;

    ColorStateList mTextOldColor;

    public EditTextCrossImage(Context context) {
        super(context);
        init(context);
    }

    public EditTextCrossImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextCrossImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EditTextCrossImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.profile_edit_text, this, true);
        ButterKnife.bind(this, view);
    }

    @OnFocusChange(R.id.edt_txt)
    void onFocusChanged(boolean focus) {
        if (!focus) {
            mButtonCross.setVisibility(GONE);
            mEditText.setTextColor(mTextOldColor);
        } else {
            mTextOldColor = mEditText.getTextColors();
            mEditText.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @OnTextChanged(R.id.edt_txt)
    void onBeforeTextChanged(CharSequence text) {
        mTextOldColor = mEditText.getTextColors();
    }

    @OnTextChanged(R.id.edt_txt)
    void onTextChanged(CharSequence text) {
        if (!text.toString().isEmpty()) {
            mButtonCross.setVisibility(VISIBLE);
        } else {
            mButtonCross.setVisibility(GONE);
        }
    }

    @OnClick(R.id.btn_clear)
    void onClick() {
        mEditText.setText("");
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public void setText(String text) {
        mEditText.setText(text);
    }
}