package com.communication.lib_core;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

public class TitleBar extends Toolbar {
    private TextView mLeft;
    private TextView mMiddle;
    private TextView mTying;
    private TextView mRight;
    private OnBackClickListener mOnBackClickListener;
    private OnRightIconClickListener mOnRightIconClickListener;
    private Context mContext;
    private Drawable drawable;
    private ImageView search;

    public TitleBar(Context context) {
        super(context);
        this.mContext = context;
        this.init((AttributeSet)null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.init(attrs);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.init(attrs);
    }

    public void setOnBackClickListener(OnBackClickListener l) {
        this.mOnBackClickListener = l;
    }

    public void setOnRightIconClickListener(OnRightIconClickListener l) {
        this.mOnRightIconClickListener = l;
    }

    private void init(AttributeSet attrs) {
        this.setContentInsetsRelative(0, 0);
        TypedArray typedArray = this.mContext.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        String title = typedArray.getString(R.styleable.TitleBar_title);
        String rightText = typedArray.getString(R.styleable.TitleBar_right_text);
        int rightColor = typedArray.getColor(R.styleable.TitleBar_right_text_color, -16777216);
        int leftColor = typedArray.getColor(R.styleable.TitleBar_left_text_color, -16777216);
        String leftText = typedArray.getString(R.styleable.TitleBar_left_text);
        boolean isBackIconShow = typedArray.getBoolean(R.styleable.TitleBar_show_back_icon, true);
        boolean isShowMiddle = typedArray.getBoolean(R.styleable.TitleBar_show_middle, true);
        int rightIconResourceId = typedArray.getResourceId(R.styleable.TitleBar_right_icon, -1);
        typedArray.recycle();
        this.addView(LayoutInflater.from(this.mContext).inflate(R.layout.rc_title_bar, this, false));
        if (rightIconResourceId > 0) {
            this.drawable = this.getResources().getDrawable(rightIconResourceId);
            this.drawable.setBounds(0, 0, this.drawable.getMinimumWidth(), this.drawable.getMinimumHeight());
            TextView rightView = this.getRightView();
            rightView.setVisibility(View.VISIBLE);
            rightView.setCompoundDrawables((Drawable)null, (Drawable)null, this.drawable, (Drawable)null);
        }

        if (null != title) {
            this.setTitle(title);
        }

        if (null != rightText) {
            this.setRightText(rightText);
        }

        this.setRightTextColor(rightColor);
        this.getLeftView().setTextColor(leftColor);
        if (!TextUtils.isEmpty(leftText)) {
            this.setLeftText(leftText);
        }

        if (!isBackIconShow) {
            this.dismissBackIcon();
        }

        if (!isShowMiddle) {
            this.dismissMiddle();
        }

        this.getLeftView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TitleBar.this.mOnBackClickListener != null) {
                    TitleBar.this.mOnBackClickListener.onBackClick();
                } else {
                    if (TitleBar.this.mContext instanceof Activity) {
                        ((Activity)TitleBar.this.mContext).onBackPressed();
                    }

                }
            }
        });
        this.getRightView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TitleBar.this.mOnRightIconClickListener != null) {
                    TitleBar.this.mOnRightIconClickListener.onRightIconClick(v);
                }

            }
        });
    }

    private void dismissMiddle() {
        this.getMiddleView().setVisibility(View.GONE);
    }

    public void setRightTextColor(int color) {
        this.getRightView().setTextColor(color);
    }

    public TextView getLeftView() {
        if (this.mLeft == null) {
            this.mLeft = (TextView)this.findViewById(R.id.tool_bar_left);
        }

        return this.mLeft;
    }

    public void setRightVisible(boolean visible) {
        if (visible) {
            this.getRightView().setVisibility(View.VISIBLE);
        } else {
            this.getRightView().setVisibility(View.GONE);
        }

    }

    public TextView getMiddleView() {
        if (this.mMiddle == null) {
            this.mMiddle = (TextView)this.findViewById(R.id.tool_bar_middle);
        }

        return this.mMiddle;
    }

    public TextView getTypingView() {
        if (this.mTying == null) {
            this.mTying = (TextView)this.findViewById(R.id.tool_bar_middle_typing);
        }

        return this.mTying;
    }

    public void setTyping(@StringRes int typing) {
        this.getTypingView().setText(typing);
    }

    public TextView getRightView() {
        if (this.mRight == null) {
            this.mRight = (TextView)this.findViewById(R.id.tool_bar_right);
        }

        return this.mRight;
    }

    public void setRightText(CharSequence charSequence) {
        TextView rightView = this.getRightView();
        if (rightView.getVisibility() != View.VISIBLE) {
            rightView.setVisibility(View.VISIBLE);
        }

        rightView.setText(charSequence);
    }

    public void setLeftText(CharSequence charSequence) {
        TextView leftText = this.getLeftView();
        leftText.setText(charSequence);
    }

    public void setTitle(String title) {
        super.setTitle(title);
        this.getMiddleView().setText(title);
    }

    public void setTitle(@StringRes int resId) {
        super.setTitle(resId);
        this.getMiddleView().setText(resId);
    }

    public void dismissBackIcon() {
        this.getLeftView().setVisibility(View.GONE);
    }

    public void setRightIconDrawableVisibility(boolean visible) {
        if (visible) {
            if (this.drawable == null) {
                return;
            }

            this.getRightView().setCompoundDrawables((Drawable)null, (Drawable)null, this.drawable, (Drawable)null);
        } else {
            this.getRightView().setCompoundDrawables((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
        }

    }

    public ImageView getSearchView() {
        if (this.search == null) {
            this.search = (ImageView)this.findViewById(R.id.rc_search);
        }

        return this.search;
    }

    public void setSearchViewVisibility(boolean visibility) {
        this.getSearchView().setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public interface OnRightIconClickListener {
        void onRightIconClick(View v);
    }

    public interface OnBackClickListener {
        void onBackClick();
    }
}
