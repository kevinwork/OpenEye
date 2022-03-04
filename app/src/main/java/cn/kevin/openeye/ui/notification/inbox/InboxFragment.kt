package cn.kevin.openeye.ui.notification.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.kevin.openeye.databinding.FragmentNotificationLoginTipsBinding
import cn.kevin.openeye.ui.common.ui.BaseFragment
import cn.kevin.openeye.ui.login.LoginActivity

class InboxFragment : BaseFragment() {

    lateinit var binding: FragmentNotificationLoginTipsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationLoginTipsBinding.inflate(inflater, container, false)
        return super.onCreateView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLogin.setOnClickListener { LoginActivity.start(activity) }
    }

    companion object {
        fun newInstance() = InboxFragment()
    }
}