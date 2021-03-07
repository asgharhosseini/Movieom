package ir.vbile.app.movieom.ui.fragment.home

import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import androidx.viewpager2.widget.*
import com.google.android.material.snackbar.*
import com.zhpan.bannerview.constants.*
import com.zhpan.bannerview.utils.*
import com.zhpan.indicator.enums.*
import dagger.hilt.android.*
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.data.model.movies.*
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.other.Constance.Companion.TAG
import ir.vbile.app.movieom.ui.adapter.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.*

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {

    private val vm by viewModels<HomeViewModel>()

    @Inject
    lateinit var genresAdapter: GenreAdapter

    @Inject
    lateinit var moviesTopAdapter: MoviesAdapter

    @Inject
    lateinit var moviesCenterAdapter: MoviesAdapter

    @Inject
    lateinit var moviesDownAdapter: MoviesAdapter

    lateinit var bannerAdapter: BannerAdapter



    private var animationFlag = false



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showBottomNav()
        subscribeToObservers()
        setupRecyclerView()
        setupView()


    }

    fun setupView() {
        genresAdapter.setOnItemClickListener {item->
            val action =
                    HomeFragmentDirections.actionHomeFragmentToGenreFragment(
                            item.id,
                            item.name
                    )
            findNavController().navigate(action)
        }
        moviesTopAdapter.setOnItemClickListener {item->
            val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                            item.id
                    )
            findNavController().navigate(action)
        }
        moviesCenterAdapter.setOnItemClickListener {item->
            val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                            item.id
                    )
            findNavController().navigate(action)
        }
        moviesDownAdapter.setOnItemClickListener {item->
            val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                            item.id
                    )
            findNavController().navigate(action)
        }
        fl_top.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToGenreFragment(
                    vm.top,
                    txt_titleTop_fragmentHome.text.toString().trim()
            )
            findNavController().navigate(action)
        }
        fl_Center.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToGenreFragment(
                    vm.center,
                    txt_titleCenter_fragmentHome.text.toString().trim()
            )
            findNavController().navigate(action)
        }
        fl_down.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToGenreFragment(
                    vm.down,
                    txt_titleDown_fragmentHome.text.toString().trim()
            )
            findNavController().navigate(action)
        }
    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_slide_right
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)

        rv_genres_fragmentHome.apply {
            adapter = genresAdapter
            if (!animationFlag) {
                layoutAnimation = animation
                animationFlag = true
            }
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }

        rv_top_fragmentHome.apply {
            adapter = moviesTopAdapter
            if (!animationFlag) {
                layoutAnimation = animation
                animationFlag = true
            }
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }
        rv_center_fragmentHome.apply {
            adapter = moviesCenterAdapter
            if (!animationFlag) {
                layoutAnimation = animation
                animationFlag = true
            }
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }
        rv_down_fragmentHome.apply {
            adapter = moviesDownAdapter
            if (!animationFlag) {
                layoutAnimation = animation
                animationFlag = true
            }
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }
    }

    private fun subscribeToObservers() {
        vm.genre.observe(viewLifecycleOwner, Observer{ result ->
            when(result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        genresAdapter.differ.submitList(it.toList())
                        txt_titleTop_fragmentHome.text = it[vm.top-1].name
                        txt_titleCenter_fragmentHome.text = it[vm.center-1].name
                        txt_titleDown_fragmentHome.text = it[vm.down-1].name

                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    result.message?.let { message->
                        Log.e(TAG,message)
                        Snackbar.make(requireView(),"Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()

                }
            }
        })
        vm.genresMoviesBanner.observe(viewLifecycleOwner, Observer {
            result ->
            when(result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        bannerAdapter = BannerAdapter {
                            val action =
                                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(this.id)
                            findNavController().navigate(action)
                        }
                        setupBannerViewPager(it.data)
                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    result.message?.let { message->
                        Log.e(TAG,message)
                        Snackbar.make(requireView(),"Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        vm.genresMoviesTop.observe(viewLifecycleOwner, Observer {
            result ->
            when(result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        moviesTopAdapter.differ.submitList(it.data.toList())
                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    result.message?.let { message->
                        Log.e(TAG,message)
                        Snackbar.make(requireView(),"Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        vm.genresMoviesCenter.observe(viewLifecycleOwner, Observer {
            result ->
            when(result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        moviesCenterAdapter.differ.submitList(it.data.toList())
                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    result.message?.let { message->
                        Log.e(TAG,message)
                        Snackbar.make(requireView(),"Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        vm.genresMoviesDown.observe(viewLifecycleOwner, Observer {
            result ->
            when(result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        moviesDownAdapter.differ.submitList(it.data.toList())
                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    result.message?.let { message->
                        Log.e(TAG,message)
                        Snackbar.make(requireView(),"Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }
    private fun setupBannerViewPager(banners: List<ShortMovie>) {
        mViewPager.apply {
            adapter = bannerAdapter
            setAutoPlay(true)
            setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            setIndicatorSlideMode(IndicatorSlideMode.WORM)
            setIndicatorVisibility(View.GONE)
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
        }.create(banners as List<Nothing>?)
    }
    private fun hideProgressBar() {
        homeFragment_loading.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        homeFragment_loading.visibility = View.VISIBLE
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