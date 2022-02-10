package cn.kevin.openeye.ui.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import cn.kevin.openeye.R
import cn.kevin.openeye.util.TypefaceUtil

/**
 * 带有自定义字体TextView。
 */
class TypefaceTextView : AppCompatTextView {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typedArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = getTypeface(typefaceType)
            typedArray.recycle()
        }
    }

    companion object {

        /**
         * 根据字体类型，获取自定义字体。
         */
        fun getTypeface(typefaceType: Int?) = when (typefaceType) {
            TypefaceUtil.FZLL_TYPEFACE -> TypefaceUtil.getFzlLTypeface()
            TypefaceUtil.FZDB1_TYPEFACE -> TypefaceUtil.getFzdb1Typeface()
            TypefaceUtil.FUTURA_TYPEFACE -> TypefaceUtil.getFuturaTypeface()
            TypefaceUtil.DIN_TYPEFACE -> TypefaceUtil.getDinTypeface()
            TypefaceUtil.LOBSTER_TYPEFACE -> TypefaceUtil.getLobsterTypeface()
            else -> Typeface.DEFAULT
        }

    }
}