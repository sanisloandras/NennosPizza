package com.sanislo.nennospizza.presentation.details

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionInflater
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.presentation.details.list.PizzaDetailsAdapter
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_pizza_details.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PizzaDetailsFragment : Fragment(R.layout.fragment_pizza_details) {
    private val viewModel: PizzaDetailsViewModel by viewModel()
    private val pizzaImageLoader: PizzaImageLoader by inject()

    private lateinit var pizzaDetailsInput: PizzaDetailsInput

    private val adapter = PizzaDetailsAdapter(object : PizzaDetailsAdapter.ClickHandler {
        override fun onSelectionChanged(id: Int, isSelected: Boolean) {
            viewModel.onSelectionChanged(id, isSelected)
        }
    }, object : PizzaDetailsAdapter.PizzaImageLoadedCallback {
        override fun pizzaImageLoaded() {
            d(TAG, "startPostponedEnterTransition")
            lifecycleScope.launch {
                startPostponedEnterTransition()
            }
        }

    }, pizzaImageLoader)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        pizzaDetailsInput = arguments?.getParcelable(EXTRA_INPUT)!!
        if (savedInstanceState == null) viewModel.pizzaDetailsInput.value = pizzaDetailsInput
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d(TAG, "onViewCreated")
        setupToolbarForBack()
        rv.adapter = adapter
        viewModel.pizzaName.observe(viewLifecycleOwner, Observer {
            (activity as? AppCompatActivity)?.supportActionBar?.title = it
        })
        viewModel.pizzaDetailsState.observe(viewLifecycleOwner, Observer {
            adapter.selection = it.selection.toMutableSet()
            adapter.submitList(it.list)
        })
        observeCartState()
        add_to_cart.setOnClickListener { viewModel.addPizzaToCart(adapter.selection) }
    }

    private fun observeCartState() {
        viewModel.addToCartState.observe(viewLifecycleOwner, Observer {
            if (!it.isLoaded) {
                add_to_cart.isEnabled = false
                tv_add_to_cart.text = getString(R.string.add_to_cart, it.price)
                return@Observer
            }
            add_to_cart.isEnabled = it.isEnabled
            iv_cart.visibility = if (it.isEnabled) View.VISIBLE else View.GONE
            tv_add_to_cart.text = if (it.isEnabled) getString(R.string.add_to_cart, it.price)
            else getString(R.string.added_to_cart)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntArray(EXTRA_SELECTION, adapter.selection.toIntArray())
        super.onSaveInstanceState(outState)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            val pizzaName = savedInstanceState.getString(EXTRA_NAME)
            val selection = savedInstanceState.getIntArray(EXTRA_SELECTION)?.toSet() ?: emptySet()
            viewModel.restoreState(pizzaName, selection)
        }
    }*/

    companion object {
        const val EXTRA_INPUT = "EXTRA_INPUT"
        const val EXTRA_SELECTION = "EXTRA_SELECTION"

        val TAG = PizzaDetailsFragment::class.java.simpleName

        fun newInstance(pizzaDetailsInput: PizzaDetailsInput): PizzaDetailsFragment {
            return PizzaDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_INPUT, pizzaDetailsInput)
                }
            }
        }
    }
}