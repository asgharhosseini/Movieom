package ir.vbile.app.movieom.ui.fragment.splash

import android.os.*
import android.util.*
import android.util.Log.i
import android.view.*
import androidx.fragment.app.*
import androidx.navigation.fragment.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.AndroidEntryPoint
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.other.*

@AndroidEntryPoint
class SplashFragment:Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNav()
        val mainThreadHandler=Handler(Looper.getMainLooper())
        mainThreadHandler.postDelayed({
            try {
                checkSituations()
            }catch (e:Exception){
                Snackbar.make(requireView(),e.message.toString(),Snackbar.LENGTH_LONG).show()
            }
        },2000)

    }
    fun checkSituations() {
        val networkAvailable = NetworkUtils.isNetworkAvailable(requireActivity())
        if (!networkAvailable) {
            Log.i(Constance.TAG,"No Connection")
            // Do somethings
        } else {
            Log.i(Constance.TAG,"Connected to internet ")
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())

            /*if (TextUtils.isEmpty(TokenContainer.token)) {
                DebugHelper.info("Token Invalid")
                findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
            } else {
                DebugHelper.info("Everything is ok, Go to dashboard")
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }*/
        }
    }
}