package ir.vbile.app.movieom.ui.adapter

import android.view.*
import android.widget.*
import com.bumptech.glide.*
import com.bumptech.glide.load.engine.*
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.request.*
import com.zhpan.bannerview.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.other.*


class GalleryBannerAdapter(
        private val onItemCLicked: MethodBlock<String>) :
        BaseBannerAdapter<String, GalleryBannerAdapter.BannerViewHolder>() {


    inner class BannerViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        private var imageIv: ImageView

        init {

            imageIv = findView(R.id.iv_itemBanner_image)

        }

        override fun bindData(banner: String?, position: Int, pageSize: Int) {
            //  titleTv.text = banner?.title

            Glide.with(itemView.context).setDefaultRequestOptions(
                    RequestOptions()
                            .placeholder(R.drawable.ic_image)
                            .error(R.drawable.ic_image)
                            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
            ).load(banner).into(imageIv)
            itemView.setOnClickListener {
                onItemCLicked(banner!!)
            }
        }

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_gallery_banner
    }

    override fun createViewHolder(itemView: View?, viewType: Int): BannerViewHolder {
        return BannerViewHolder(itemView!!)
    }

    override fun onBind(holder: BannerViewHolder?, data: String?, position: Int, pageSize: Int) {
        holder?.bindData(data, position, pageSize)
    }


}