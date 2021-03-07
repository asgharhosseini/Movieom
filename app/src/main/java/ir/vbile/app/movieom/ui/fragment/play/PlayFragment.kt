package ir.vbile.app.movieom.ui.fragment.play

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
import kotlinx.android.synthetic.main.fragment_play.*
import javax.inject.*

@AndroidEntryPoint
class PlayFragment : Fragment(R.layout.fragment_play) {
    private val vm by viewModels<PlayViewModel>()

    @Inject
    lateinit var genresAdapter: GenresPlayAdapter

    private var animationFlag = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupRecyclerView()
        setupView()
    }

    fun setupView() {
        genresAdapter.setOnItemClickListener { item ->
            val action =
                    PlayFragmentDirections.actionPlayFragmentToGenreFragment(
                            item.id,
                            item.name
                    )
            findNavController().navigate(action)
        }

    }


    private fun setupRecyclerView() {
        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        rv_fragmentPlay.apply {
            adapter = genresAdapter
            if (!animationFlag) {
                layoutAnimation = animation
                animationFlag = true
            }
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }


    }

    private fun subscribeToObservers() {
        vm.genre.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    result.data?.let {
                        genresAdapter.differ.submitList(it.toList())
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
        playFragment_loading.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        playFragment_loading.visibility = View.VISIBLE
    }

}