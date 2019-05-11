package net.moisesborges.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import net.moisesborges.R
import net.moisesborges.databinding.ActivitySearchBinding
import net.moisesborges.extensions.withUnwrapped
import net.moisesborges.ui.base.LifecycleActivity
import net.moisesborges.ui.search.adapter.SearchItemsAdapter
import net.moisesborges.ui.search.mvvm.SearchViewModel
import net.moisesborges.ui.top.adapter.TopStationItemViewModelFactory
import org.koin.android.ext.android.get

class SearchActivity : LifecycleActivity() {

    private val viewModel: SearchViewModel = get()
    private val stationItemViewModelFactory: TopStationItemViewModelFactory = get()
    private val searchItemsAdapter = SearchItemsAdapter(stationItemViewModelFactory)

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.viewModel = viewModel
        viewModel.result.observe(this, Observer { searchItems ->
            searchItemsAdapter.setSearchItems(searchItems)
        })
        viewModel.start()
        setupToolbar()
        setupSearchView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding.searchItemsRecyclerView) {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchItemsAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newQuery: String): Boolean {
                viewModel.search(newQuery)
                return true
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        withUnwrapped(supportActionBar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}

fun createSearchActivityIntent(context: Context) = Intent(context, SearchActivity::class.java)