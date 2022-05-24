package com.darkshandev.freshcam.presentation

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.ActivityMainBinding
import com.darkshandev.freshcam.presentation.classifier.viewmodels.ClassifierViewmodel
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val fruitsViewmodel:FruitsViewmodel by viewModels()
    private val classifierViewmodel: ClassifierViewmodel by viewModels( )
    private  val  binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Create your custom animation.
            ObjectAnimator.ofFloat(splashScreenView, "alpha", 0f).apply {
                duration = 1000
                interpolator = AnticipateInterpolator()
                doOnEnd { splashScreenView.remove() }
                start()
            }
        }
        setContentView(binding.root)
        setupNavigation()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun setupNavigation() {
        //setup NavigationUi with bottomNavigationView
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController,)
        binding.bottomNavigationView.itemIconTintList = null
        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_homeFruitsFragment_to_scanFruitsFragment)
        }
navController.addOnDestinationChangedListener { _, destination, _ ->
    when (destination.id) {
        R.id.homeFruitsFragment -> {
            binding.fab.visibility = View.VISIBLE
            binding.bottomAppBar.visibility =View.VISIBLE   
            binding.bottomNavigationView.visibility = View.VISIBLE
            binding.fab.setOnClickListener {
                navController.navigate(R.id.action_homeFruitsFragment_to_scanFruitsFragment)
            }
        }
        R.id.settingFragment -> {
            binding.fab.visibility = View.VISIBLE
            binding.bottomAppBar.visibility =View.VISIBLE
            binding.bottomNavigationView.visibility = View.VISIBLE
            binding.fab.setOnClickListener {
                navController.navigate(R.id.action_settingFragment_to_scanFruitsFragment)
            }
        }
        R.id.scanFruitsFragment -> {
            binding.fab.visibility = View.GONE
            binding.bottomNavigationView.visibility = View.GONE
            binding.bottomAppBar.visibility =View.GONE

        }
    }

    }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
        )
        private const val REQUEST_CODE_PERMISSIONS = 10

    }
}