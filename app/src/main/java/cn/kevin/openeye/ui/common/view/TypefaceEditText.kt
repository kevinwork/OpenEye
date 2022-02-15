package cn.kevin.openeye.ui.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import cn.kevin.openeye.R

/**
 * 带有自定义字体EditText。
 */
class TypefaceEditText : AppCompatEditText {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("CustomViewStyleable")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typedArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = TypefaceTextView.getTypeface(typefaceType)
            typedArray.recycle()
        }
    }
}