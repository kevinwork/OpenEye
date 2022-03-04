package cn.kevin.openeye.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.kevin.openeye.R
import cn.kevin.openeye.ui.common.ui.BaseViewPagerFragment
import cn.kevin.openeye.ui.community.follow.FollowFragment
import cn.kevin.openeye.ui.community.recommend.RecommendFragment
import cn.kevin.openeye.util.GlobalUtil

class CommunityFragment : BaseViewPagerFragment() {


    override val createTitles = ArrayList<String>().apply {
        add(GlobalUtil.getString(R.string.recommend))
        add(GlobalUtil.getString(R.string.follow))
    }

    override val createFragments: Array<Fragment> = arrayOf(
        RecommendFragment.newInstance(),
        FollowFragment.newInstance()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_main_container, container, false))
    }

}