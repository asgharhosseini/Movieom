package ir.vbile.app.movieom.ui.fragment.genre

import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.adapter.*
import kotlinx.android.synthetic.main.fragment_genre.*
import javax.inject.*

@AndroidEntryPoint
class GenreFragment : Fragment(R.layout.fragment_genre) {
    private var animationFlag = false
    private val vm: GenreViewModel by viewModels()

    private val arg by navArgs<GenreFragmentArgs>()

    @Inject
    lateinit var genreMoviesAdapter: GenreMoviesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getGenresMovies(arg.genreId)
        subscribeToObservers()
        setupRecyclerView()
        setupView()
    }


    fun setupView() {
        genreMoviesAdapter.setOnItemClickListener { item ->
            val action =
                    GenreFragmentDirections.actionGenreFragmentToDetailFragment(
                            item.id)
            findNavController().navigate(action)
        }
        tv_title_fragmentGenre.text = arg.genreName
        iv_fragmentGenre_backBottom.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        rv_fragmentGenre.apply {
            adapter = genreMoviesAdapter
            if (!animationFlag) {
                layoutAnimation = animation
                animationFlag = true
            }
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun subscribeToObservers() {
        vm.genreMovie.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        genreMoviesAdapter.differ.submitList(it.data.toList())
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

    private fun hideProgressBar() {
        genreFragment_loading.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        genreFragment_loading.visibility = View.VISIBLE
    }

}