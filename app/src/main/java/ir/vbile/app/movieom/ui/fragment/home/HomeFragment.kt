package ir.vbile.app.movieom.ui.fragment.home

import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.AndroidEntryPoint
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.other.Constance.Companion.TAG
import ir.vbile.app.movieom.ui.adapter.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.*
import kotlin.random.*

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {

    private val vm by viewModels<HomeViewModel>()

    private lateinit var genresAdapter: GenreAdapter

    @Inject
     lateinit var moviesTopAdapter: MoviesAdapter


    var top = Random.nextInt(1, 21)
    var center = Random.nextInt(1, 21)
    var down = Random.nextInt(1, 21)
    private var animationFlag = false



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()
        setupView()
        vm.getGenresMovies(top)

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
    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_slide_right
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        genresAdapter= GenreAdapter()
        //moviesTopAdapter= MoviesAdapter()
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
    }

    private fun subscribeToObservers() {
        vm.genre.observe(viewLifecycleOwner, Observer{ result ->
            when(result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        genresAdapter.differ.submitList(it.toList())
                        txt_titleTop_fragmentHome.text = it[top].name
                        txt_titleCenter_fragmentHome.text = it[center].name
                        txt_titleDown_fragmentHome.text = it[down].name

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
        vm.genresMovies.observe(viewLifecycleOwner, Observer {
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

    }
    private fun hideProgressBar() {
        homeFragment_loading.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        homeFragment_loading.visibility = View.VISIBLE
    }

}