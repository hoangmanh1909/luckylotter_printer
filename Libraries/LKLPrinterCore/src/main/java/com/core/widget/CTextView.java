package com.core.widget;

import com.core.common.R;
import com.core.utils.StringUtils;
import com.core.utils.Typefaces;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Custom Text view
 * Created by neo on 3/24/2016.
 */
public class CTextView extends TextView {
  public CTextView(Context context) {
    super(context);
    initFont(null);
  }

  public CTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initFont(attrs);
  }

  public CTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initFont(attrs);
  }

  /**
   * init font text view
   */
  public void initFont(AttributeSet attrs) {
    if (attrs == null) {
      return;
    }

    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CTextView);
    String font = a.getString(R.styleable.CTextView_typeFaceFont);
    String style = a.getString(R.styleable.CTextView_typeFaceStyle);

    if (font != null) {
      if (!StringUtils.isEmpty(style)) {
        font = font + "-" + style;
      }
      this.setTypeface(Typefaces.get(getContext(), font, "ttf"));
    }
    a.recycle();
  }

}