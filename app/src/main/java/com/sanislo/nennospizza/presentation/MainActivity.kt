package com.sanislo.nennospizza.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.list.PizzaListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) addPizzaListFragment()
    }

    private fun addPizzaListFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fl_fragment_container, PizzaListFragment())
                .commit()
    }

}
