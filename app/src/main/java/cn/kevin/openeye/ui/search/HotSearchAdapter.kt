package cn.kevin.openeye.ui.search

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.kevin.openeye.BuildConfig
import cn.kevin.openeye.Const
import cn.kevin.openeye.R
import cn.kevin.openeye.extension.gone
import cn.kevin.openeye.extension.inflate
import cn.kevin.openeye.extension.showToast
import cn.kevin.openeye.util.GlobalUtil

/**
 * 热搜关键词Adapter
 */
class HotSearchAdapter(var dataList: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int) = when (position) {
        0 -> Const.ItemViewType.CUSTOM_HEADER
        else -> HOT_SEARCH_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Const.ItemViewType.CUSTOM_HEADER -> HeaderViewHolder(R.layout.item_search_header.inflate(parent))
        else -> HotSearchViewHolder(R.layout.item_search.inflate(parent))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.tvTitle.text = GlobalUtil.getString(R.string.hot_keywords)
            }
            is HotSearchViewHolder -> {
                val item = dataList[position]
                holder.tvKeywords.text = item
                holder.itemView.setOnClickListener {
                    "${item},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast()
                }
            }
            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
    }

    inner class HotSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKeywords = view.findViewById<TextView>(R.id.tvKeywords)
    }

    companion object {
        const val TAG = "HotSearchAdapter"
        const val HOT_SEARCH_TYPE = Const.ItemViewType.MAX
    }
}