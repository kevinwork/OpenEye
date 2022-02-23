package cn.kevin.openeye.ui.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * 处理嵌套ViewPager时，横向滑动冲突。
 * 判断滑动事件：纵向，父控件控制；横向，自己控制，不给父控件控制权限
 */
class HorizontalRecyclerView : RecyclerView {

    private var lastX = 0f

    private var lastY = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 在 HorizontalRecyclerView 上按下，拦截事件
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY
                // 当纵向移动的距离大于横向移动的距离，判断为上下滑动，那事件应该归父容器管，不拦截
                if (abs(deltaX) < abs(deltaY)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 松开，后续事件继续交给父容器
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {
            }
        }
        lastX = x
        lastY = y
        return super.dispatchTouchEvent(ev)
    }
}
