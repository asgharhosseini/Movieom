package ir.vbile.app.movieom.ui.adapter

import android.view.View
import android.widget.*
import com.bumptech.glide.*
import com.bumptech.glide.load.engine.*
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.request.*

import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.data.model.movies.*
import ir.vbile.app.movieom.other.*
import javax.inject.*


class BannerAdapter  (
        private val onItemCLicked: MethodBlock<ShortMovie>) :
    BaseBannerAdapter<ShortMovie, BannerAdapter.BannerViewHolder>() {



    inner class BannerViewHolder(itemView: View) : BaseViewHolder<ShortMovie>(itemView) {
        // private var titleTv: TextView
        private var imageIv: ImageView

        init {
            //  titleTv = findView(R.id.tv_itemBanner_title)
            imageIv = findView(R.id.iv_itemBanner_image)

        }

        override fun bindData(banner: ShortMovie?, position: Int, pageSize: Int) {
            //  titleTv.text = banner?.title

            Glide.with(itemView.context).setDefaultRequestOptions(
                    RequestOptions()
                            .placeholder(R.drawable.ic_image)
                            .error(R.drawable.ic_image)
                            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
            ).load(banner?.poster).into(imageIv)
            itemView.setOnClickListener {
                onItemCLicked(banner!!)
            }
        }

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_banner
    }

    override fun createViewHolder(itemView: View?, viewType: Int): BannerViewHolder {
        return BannerViewHolder(itemView!!)
    }

    override fun onBind(holder: BannerViewHolder?, data: ShortMovie?, position: Int, pageSize: Int) {
        holder?.bindData(data, position, pageSize)
    }


}