package cn.kevin.openeye.ui.common.ui



import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import cn.kevin.openeye.extension.logD

open class BaseFragment : Fragment() {

    /**
     * 依附的Activity
     */
    lateinit var activity: Activity

    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 缓存当前依附的activity
        activity = requireActivity()
        logD(TAG, "BaseFragment-->onAttach()")
    }

}