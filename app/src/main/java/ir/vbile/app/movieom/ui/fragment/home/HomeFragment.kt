package ir.vbile.app.movieom.ui.fragment.home

import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.AndroidEntryPoint
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.other.Constance.Companion.TAG
import ir.vbile.app.movieom.ui.adapter.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.*

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {

    private val vm by viewModels<HomeViewModel>()

    private lateinit var genresAdapter: GenreAdapter

    private var animationFlag = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()
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
    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_slide_right
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        genresAdapter= GenreAdapter()
        rv_genres_fragmentHome.apply {
            adapter = genresAdapter
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