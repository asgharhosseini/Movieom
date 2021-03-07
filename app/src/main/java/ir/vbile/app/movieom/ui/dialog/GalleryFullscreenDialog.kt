package ir.vbile.app.movieom.ui.dialog

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.viewpager2.widget.*
import com.zhpan.bannerview.constants.*
import com.zhpan.bannerview.utils.*
import com.zhpan.indicator.enums.*
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.ui.adapter.*
import kotlinx.android.synthetic.main.fragment_gallery_fullscreen.*
import kotlinx.android.synthetic.main.fragment_gallery_fullscreen.view.*


class GalleryFullscreenDialog(
        private var imageList: List<String> = ArrayList(),
        private var selectedPosition: Int
) : DialogFragment() {


    private lateinit var bannerAdapter: GalleryBannerAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_fullscreen, container, false)
        if (imageList.isNotEmpty() && imageList != null) {
            bannerAdapter = GalleryBannerAdapter {

            }
            setupBannerViewPager(imageList, view, selectedPosition)
        }


        return view
    }

    private fun setupBannerViewPager(imageList: List<String>, view: View, selectedPosition: Int) {
        view.mViewPager.apply {
            adapter = bannerAdapter
            setAutoPlay(true)
            setCurrentItem(selectedPosition, true)
            verticalScrollbarPosition = selectedPosition
            setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            setIndicatorSlideMode(IndicatorSlideMode.WORM)
            setIndicatorVisibility(View.VISIBLE)
            setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_10))
            removeDefaultPageTransformer()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    BannerUtils.log("position:$position")
                }
            }
            )
            setOnPageClickListener {
            }
        }.create(imageList as List<Nothing>?)
    }

    override fun onPause() {
        super.onPause()
        if (mViewPager != null) {
            mViewPager.stopLoop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mViewPager != null) {
            mViewPager.startLoop()
        }
    }


}