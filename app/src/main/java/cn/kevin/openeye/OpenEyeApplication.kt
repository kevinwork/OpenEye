package cn.kevin.openeye

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import cn.kevin.openeye.ui.common.view.NoStatusFooter
import cn.kevin.openeye.util.GlobalUtil
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpenEyeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context : Context
    }

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        SmartRefreshLayout.setDefaultRefreshInitializer(DefaultRefreshInitializer { context, layout ->
            //全局设置（优先级最低）
            layout.setEnableAutoLoadMore(true);
            layout.setEnableOverScrollDrag(false);
            layout.setEnableOverScrollBounce(true);
            layout.setEnableLoadMoreWhenContentNotFull(true);
            layout.setEnableScrollContentWhenRefreshed(true);
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            layout.setFooterMaxDragRate(4.0F);
            layout.setFooterHeight(45f);
        })

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(DefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(true)
            MaterialHeader(context).setColorSchemeResources(R.color.blue, R.color.blue, R.color.blue)

        })

        // 设置默认 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableFooterTranslationContent(true)
            layout.setFooterHeight(153f)
            layout.setFooterTriggerRate(0.6f)
            // show the end
            NoStatusFooter.REFRESH_FOOTER_NOTHING = GlobalUtil.getString(R.string.footer_not_more)
            NoStatusFooter(context).apply {
                setAccentColorId(R.color.colorTextPrimary)
                setTextTitleSize(16f)
            }
        }
    }


}