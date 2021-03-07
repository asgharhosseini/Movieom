package ir.vbile.app.movieom.ui.fragment.detail

import android.annotation.*
import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.google.android.material.appbar.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.*
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.data.model.movie.*
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.adapter.*
import ir.vbile.app.movieom.ui.dialog.*
import ir.vbile.app.movieom.ui.fragment.home.*
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.*

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val homeViewModel by viewModels<HomeViewModel>()
    private val vm by viewModels<DetailViewModel>()

    @Inject
    lateinit var moviesSimilarAdapter: MoviesAdapter

    @Inject
    lateinit var galleryAdapter: GalleryImageAdapter

    @Inject
    lateinit var glide: RequestManager
    private lateinit var galleryFullscreenDialog: GalleryFullscreenDialog
    private var flg = false
    private val arg by navArgs<DetailFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getMovie(arg.movieId)
        subscribeToObservers()
        setupRecyclerView()
        setupView()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setupView() {
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_arrow)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        handleCollapsedToolbarTitle()
        moviesSimilarAdapter.setOnItemClickListener { item ->
            val action =
                    DetailFragmentDirections.actionDetailFragmentSelf(
                            item.id,
                    )
            findNavController().navigate(action)
        }
        /*
            if (vm.checkMovieIsFavorite(arg.movieId)) {
            flg = true
            iv_removeFavorite.visibility = View.VISIBLE
        }
        else {
            flg = false
            iv_addFavorite.visibility = View.VISIBLE
        }
        iv_removeFavorite.setOnClickListener {
            compositeDisposable.add(
                vm.deleteItem(arg.movieId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
            iv_addFavorite.visibility = View.VISIBLE
            iv_removeFavorite.visibility = View.GONE
        }
        * */

    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_slide_right
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)

        rv_similar_fragmentDetail.apply {
            adapter = moviesSimilarAdapter
            layoutAnimation = animation
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
        rv_gallery_fragmentDetail.apply {
            adapter = galleryAdapter
            layoutAnimation = animation
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }


    }

    private fun subscribeToObservers() {


        homeViewModel.genresMoviesTop.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        moviesSimilarAdapter.differ.submitList(it.data.toList())
                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    result.message?.let { message ->
                        Log.e(Constance.TAG, message)
                        Snackbar.make(requireView(), "Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        vm.movie.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let { movie ->
                        app_bar_image.apply {
                            transitionName = movie.poster
                        }
                        glide.load(movie.poster).into(app_bar_image)
                        glide.load(movie.poster).into(image)
                        title.text = movie.title
                        rated.text = movie.rated
                        runtime.text = movie.runtime
                        released.text = movie.released
                        val builderGenres = StringBuilder()
                        movie.genres.iterator().forEach {
                            builderGenres.append(" ")
                            builderGenres.append(it)
                        }
                        genres_fragmentDetail.text = builderGenres.toString()
                        plot.text = movie.plot
                        rank.text = movie.imdbRating
                        director.text = movie.director
                        country.text = movie.country
                        var builderWriter = StringBuilder()
                        movie.writer.split(',').iterator().forEach {
                            builderWriter.append(it)
                            builderWriter.append("  ")

                        }
                        writer.text = builderWriter.toString()
                        var builderActors = StringBuilder()
                        movie.actors.split(',').iterator().forEach {
                            builderActors.append(it)
                            builderActors.append("  ")

                        }
                        actors.text = builderActors.toString()
                        var builderAwards = StringBuilder()
                        movie.awards.split('.').iterator().forEach {
                            builderAwards.append(it)
                            builderAwards.append("\n")
                        }
                        awards.text = builderAwards.toString()
                        var builderImages = StringBuilder()
                        if (movie.toString().contains("images") && !movie.images.isNullOrEmpty()) {
                            galleryAdapter.differ.submitList(movie.images)
                            galleryAdapter.setOnItemClickListener { image ->
                                galleryFullscreenDialog =
                                        GalleryFullscreenDialog(movie.images, 0)
                                galleryFullscreenDialog.isCancelable = true
                                galleryFullscreenDialog.show((activity as MainActivity).supportFragmentManager,
                                        null
                                )
                            }

                            movie.images.iterator().forEach {
                                builderImages.append(" , ")
                                builderImages.append(it)
                            }
                        } else {
                            builderImages.append("")
                            galleryTitle.visibility = View.INVISIBLE
                        }


                        var movieInDb = MovieInDb(
                                actors = builderActors.toString(),
                                awards = builderAwards.toString(),
                                country = movie.country,
                                director = movie.director,
                                genres = builderGenres.toString(),
                                id = movie.id,
                                builderImages.toString(),
                                imdbId = movie.imdbId,
                                imdbRating = movie.imdbRating,
                                imdbVotes = movie.imdbVotes,
                                metascore = movie.metascore,
                                poster = movie.poster,
                                plot = movie.plot,
                                rated = movie.rated,
                                released = movie.released,
                                runtime = movie.runtime,
                                title = movie.title,
                                type = movie.type,
                                writer = builderWriter.toString(),
                                year = movie.year
                        )
/*
                            iv_addFavorite.setOnClickListener {
                                compositeDisposable.add(
                                        vm.addFavoriteMovie(movieInDb)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe()
                                )
                                iv_addFavorite.visibility = View.GONE
                                iv_removeFavorite.visibility = View.VISIBLE
                            }
*/


                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                    result.message?.let { message ->
                        Log.e(Constance.TAG, message)
                        Snackbar.make(requireView(), "Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

    private fun handleCollapsedToolbarTitle() {
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                // verify if the toolbar is completely collapsed and set the movie name as the title
                // verify if the toolbar is completely collapsed and set the movie name as the title
                if (scrollRange + verticalOffset == 0) {
                    toolbarTitle.text = title.text.toString()
                    isShow = true
                } else if (isShow) {
                    // display an empty string when toolbar is expanded
                    toolbarTitle.text = " "
                    isShow = false
                }
            }
        })
    }

    private fun hideProgressBar() {
        detailFragment_loading.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        detailFragment_loading.visibility = View.VISIBLE
    }
}