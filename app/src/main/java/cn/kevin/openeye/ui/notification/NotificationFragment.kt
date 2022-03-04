package cn.kevin.openeye.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.kevin.openeye.R
import cn.kevin.openeye.ui.common.ui.BaseViewPagerFragment
import cn.kevin.openeye.ui.notification.inbox.InboxFragment
import cn.kevin.openeye.ui.notification.interaction.InteractionFragment
import cn.kevin.openeye.ui.notification.push.PushFragment
import cn.kevin.openeye.util.GlobalUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseViewPagerFragment() {

    override val createTitles = ArrayList<String>().apply {
        add(GlobalUtil.getString(R.string.push))
        add(GlobalUtil.getString(R.string.interaction))
        add(GlobalUtil.getString(R.string.inbox))
    }

    override val createFragments: Array<Fragment> = arrayOf(
        PushFragment.newInstance(),
        InteractionFragment.newInstance(),
        InboxFragment.newInstance()
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_main_container, container, false))
    }

    companion object {

        fun newInstance() = NotificationFragment()
    }
}