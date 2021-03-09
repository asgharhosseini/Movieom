package ir.vbile.app.movieom.ui.fragment.favorite

import android.os.*
import android.view.*
import android.view.animation.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.ui.adapter.*
import ir.vbile.app.movieom.ui.fragment.detail.*
import ir.vbile.app.movieom.ui.fragment.genre.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import javax.inject.*

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private var animationFlag = false
    private val vm: FavoriteViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels()


    private val arg by navArgs<GenreFragmentArgs>()

    @Inject
    lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupRecyclerView()
        setupView()
    }


    fun setupView() {
        favoriteMoviesAdapter.setOnItemClickListener { item ->
            val action =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                            item.id)
            findNavController().navigate(action)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = favoriteMoviesAdapter.differ.currentList[position]
                detailViewModel.deleteItem(movie.id)
                Snackbar.make(requireView(), "article deleted successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        detailViewModel.addFavoriteMovie(movie)
                    }
                    show()
                }
            }


        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rv_fragmentFavorite)
        }
    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        rv_fragmentFavorite.apply {
            adapter = favoriteMoviesAdapter
            if (!animationFlag) {
                layoutAnimation = animation
                animationFlag = true
            }
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun subscribeToObservers() {
        vm.getAllFavorite().observe(viewLifecycleOwner, Observer { result ->
            favoriteMoviesAdapter.differ.submitList(result)
        })
    }


}