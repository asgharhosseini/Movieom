package ir.vbile.app.movieom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ir.vbile.app.movieom.databinding.ActivityMainBinding
import ir.vbile.app.movieom.other.Constance.Companion.TAG
import ir.vbile.app.movieom.other.NetworkUtils
import ir.vbile.app.movieom.other.setupWithNavController
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null

    lateinit var binding: ActivityMainBinding
    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkInternetConnection()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(broadcastReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
       // setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView =
                findViewById<BottomNavigationView>(binding.bnvActivityMainMain.id)
        bottomNavigationView.selectedItemId = R.id.home

        val navGraphIds = listOf(
                R.navigation.home,
                R.navigation.play,
                R.navigation.favorite,
                R.navigation.search,
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = binding.fcvActivityMainMain.id,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, { navController ->
            //  setupNavigationView(controller.value!!)
//      setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }


    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }


    fun showBottomNav() {
        binding.bnvActivityMainMain.visibility = View.VISIBLE
//        ViewHelper.setMargins(binding.fcvActivityMainMain, 0, 0, 0, 56)
    }

    fun hideBottomNav() {
        binding.bnvActivityMainMain.visibility = View.GONE
//        ViewHelper.setMargins(binding.fcvActivityMainMain, 0, 0, 0, 0)
    }


    private fun checkInternetConnection() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            try {
                findNavController(R.id.fcv_activityMain_main).navigate(R.id.networkFailedFragment)
            } catch (e: IllegalArgumentException) {
                Log.i(TAG, "Multiple navigation attempts handled. ${e.message} ")
            }
        } else {
            try {
                findNavController(R.id.fcv_activityMain_main).navigate(R.id.splashFragment)
            } catch (e: IllegalArgumentException) {

                Log.i(TAG, "Multiple navigation attempts handled. ${e.message} ")

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
