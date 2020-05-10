package com.sanislo.nennospizza.presentation

import android.os.Bundle
import android.transition.Fade
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.cart.CartFragment
import com.sanislo.nennospizza.presentation.list.PizzaListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) addPizzaListFragment()
        observeNavigateToCart()
        observeErrors()
        temp()
    }

    private fun temp() {
        val fade = Fade()
        fade.excludeTarget(resources.getIdentifier("action_bar_container", "id", "android"), true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = fade
        window.exitTransition = fade
    }

    private fun observeErrors() {
        viewModel.errors.observe(this, EventObserver { exception ->
            Toast.makeText(
                this,
                exception.message ?: getString(R.string.unexpected_error),
                Toast.LENGTH_LONG
            ).show()
        })
    }

    private fun addPizzaListFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fl_fragment_container, PizzaListFragment())
                .commit()
    }

    private fun observeNavigateToCart() {
        viewModel.navigateToCartEvent.observe(this, EventObserver {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, CartFragment())
                .addToBackStack("cart")
                .commit()
        })
    }
}
