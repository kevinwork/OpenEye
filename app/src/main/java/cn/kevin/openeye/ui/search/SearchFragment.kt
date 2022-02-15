package cn.kevin.openeye.ui.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import cn.kevin.openeye.MainActivity
import cn.kevin.openeye.R
import cn.kevin.openeye.databinding.FragmentSearchBinding
import cn.kevin.openeye.extension.*
import cn.kevin.openeye.ui.common.ui.BaseFragment


/**
 *
 */
class SearchFragment : BaseFragment() {

    private lateinit var binding : FragmentSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llSearch.visibleAlphaAnimation(500)
        binding.etQuery.setOnEditorActionListener(EditorActionListener())
        setOnClickListener(binding.tvCancel) {
            hideSoftKeyboard()
            (requireActivity() as MainActivity).getNavController().navigate(R.id.homeFragment)

        }




    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            logD(TAG, "showSoftKeyboard....")
            binding.tvCancel.showSoftKeyboard() },
            2000)
        logD(TAG, "onResume....")
    }

    override fun onStart() {
        super.onStart()
        logD(TAG, "onStart....")
    }

    override fun onPause() {
        super.onPause()
        logD(TAG, "onPause....")
    }

    override fun onStop() {
        super.onStop()
        logD(TAG, "onStop....")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        logD(TAG, "onCreateAnimation....")

        return if (enter) {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                logD(TAG, "showSoftKeyboard....")
                binding.etQuery.showSoftKeyboard() },
                2000)
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_up_in)
        } else {
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_top_out)
        }

    }


    /**
     * 隐藏软键盘
     */
    private fun hideSoftKeyboard() {
        try {
            activity.currentFocus?.run {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            logW(TAG, e.message, e)
        }
    }

    /**
     * 拉起软键盘
     */
    private fun View.showSoftKeyboard() {
        try {
            this.isFocusable = true
            this.isFocusableInTouchMode = true
            this.requestFocus()
            val manager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(this, 0)
        } catch (e: Exception) {
            logW(TAG, e.message, e)
        }
    }

    inner class EditorActionListener : TextView.OnEditorActionListener {

        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etQuery.text.toString().isEmpty()) {
                    R.string.input_keywords_tips.showToast()
                    return false
                }
                R.string.currently_not_supported.showToast()
                return true
            }
            return true
        }

    }
}