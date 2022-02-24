package cn.kevin.openeye.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import cn.kevin.openeye.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Glide加载图片，可以指定圆角弧度。
 *
 * @param url 图片地址
 * @param round 圆角，单位dp
 * @param cornerType 圆角角度
 */
fun ImageView.load(url: String, round: Float = 0f, cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL) {
    if (round == 0f) {
        Glide.with(this.context).load(url).into(this)
    } else {
        val option = RequestOptions.bitmapTransform(RoundedCornersTransformation(dp2px(round), 0, cornerType)).placeholder(
            R.drawable.shape_album_loading_bg)
        Glide.with(this.context).load(url).apply(option).into(this)
    }
}

/**
 * Glide加载图片，可以定义配置参数。
 *
 * @param url 图片地址
 * @param options 配置参数
 */
fun ImageView.load(url: String, options: RequestOptions.() -> RequestOptions) {
    Glide.with(this.context).load(url).apply(RequestOptions().options()).into(this)
}