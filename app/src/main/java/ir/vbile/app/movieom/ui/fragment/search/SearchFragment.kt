package ir.vbile.app.movieom.ui.fragment.search

import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import androidx.appcompat.widget.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.adapter.*
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val vm: SearchViewModel by viewModels()

    @Inject
    lateinit var searchMoviesAdapter: GenreMoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupView()
    }

    fun setupView() {
        searchMoviesAdapter.setOnItemClickListener { item ->
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(item.id)
            findNavController().navigate(action)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                txt_searchHelper_fragmentSearch.visibility = View.GONE
                lottieAnimationView.visibility = View.GONE
                if (newText.length == 0) {
                    txt_searchHelper_fragmentSearch.visibility = View.VISIBLE
                    lottieAnimationView.visibility = View.VISIBLE
                    searchMoviesAdapter.differ.submitList(listOf())

                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                vm.getGenresMovies(query)
                txt_searchHelper_fragmentSearch.visibility = View.GONE
                lottieAnimationView.visibility = View.GONE
                subscribeToObservers()
                return false
            }
        })
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                searchMoviesAdapter.differ.submitList(listOf())
                txt_searchHelper_fragmentSearch.visibility = View.VISIBLE
                lottieAnimationView.visibility = View.VISIBLE
                return false
            }
        })

    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        rv_search_fragmentSearch.apply {
            adapter = searchMoviesAdapter
            layoutAnimation = animation
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun subscribeToObservers() {
        vm.SearchMovie.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {

                    result.data?.let {
                        searchMoviesAdapter.differ.submitList(it.data.toList())
                    }
                }
                is Resource.Error -> {

                    result.message?.let { message ->
                        Log.e(Constance.TAG, message)
                        Snackbar.make(requireView(), "Error: $message ${result.code}", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }


}