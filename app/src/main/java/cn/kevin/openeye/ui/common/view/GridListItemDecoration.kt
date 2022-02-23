package cn.kevin.openeye.ui.common.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.kevin.openeye.extension.dp2px

/**
 * 网格列表，实现相等内间距空隙，四周不留空隙。
 *
 * @author vipyinzhiwei
 * @since  2020/5/11
 */
class GridListItemDecoration(private val spanCount: Int = 2, private val space: Float = 2f) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val count = parent.adapter?.itemCount //item count
        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        val lastRowFirstItemPostion = count?.minus(spanCount)   //最后一行,第一个item索引
        val space = dp2px(space)

        when {
            position < spanCount -> {
                outRect.bottom = space
            }
            position < lastRowFirstItemPostion!! -> {
                outRect.top = space
                outRect.bottom = space
            }
            else -> {
                outRect.top = space
            }
        }
        when (spanIndex) {
            0 -> {
                outRect.right = space
            }
            spanCount - 1 -> {
                outRect.left = space
            }
            else -> {
                outRect.left = space
                outRect.right = space
            }
        }
    }
}