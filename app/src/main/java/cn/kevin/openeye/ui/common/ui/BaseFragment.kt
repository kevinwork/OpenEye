package cn.kevin.openeye.ui.common.ui



import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.kevin.openeye.R
import cn.kevin.openeye.extension.logD
import cn.kevin.openeye.ui.common.callback.RequestLifecycle

open class BaseFragment : Fragment(), RequestLifecycle {

    /**
     * 依附的Activity
     */
    lateinit var activity: Activity

    private var rootView:View? = null

    private var loadErrorView: View? = null

    private var loading: ProgressBar? = null


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

    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    fun onCreateView(view: View): View {
        logD(TAG, "BaseFragment-->onCreateView()")
        rootView = view
        loading = view.findViewById(R.id.loading)
        return view
    }

    override fun startLoading() {
       loading?.visibility = View.VISIBLE
        hideLoadErrorView()
    }

    override fun loadFinished() {
        loading?.visibility = View.GONE
    }

    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
    }

    protected fun showLoadErrorView(tip: String, block: View.()->Unit) {
        if (loadErrorView != null) {
            logD("zwz333","======")
            loadErrorView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.loadErrorView)
            if (viewStub != null) {
                logD("zwz22","======")
                loadErrorView = viewStub.inflate()
                val loadText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
                loadText?.text = tip
                val loadErrorRootView = loadErrorView?.findViewById<View>(R.id.loadErrorRootView)
                loadErrorRootView?.setOnClickListener() {
                    it.block()
                }
            }

        }
    }

    protected fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

}