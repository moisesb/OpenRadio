/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.moisesborges.ui.search

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.Window
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
import net.moisesborges.ui.search.adapter.StationSearchItemViewModelFactory
import org.koin.android.ext.android.get

class SearchActivity : LifecycleActivity() {

    private val viewModel: SearchViewModel = get()
    private val stationItemViewModelFactory: StationSearchItemViewModelFactory = get()
    private val searchItemsAdapter = SearchItemsAdapter(stationItemViewModelFactory)

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
                enterTransition = Fade()
                exitTransition = Fade()
            }
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.viewModel = viewModel
        viewModel.result.observe(this, Observer { searchItems ->
            searchItemsAdapter.setSearchItems(searchItems)
        })
        setupToolbar()
        setupSearchView()
        setupRecyclerView()
        viewModel.start()
    }

    private fun setupRecyclerView() {
        with(binding.searchItemsRecyclerView) {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchItemsAdapter
        }
    }

    private fun setupSearchView() {
        with(binding.searchView) {
            isIconified = false
            setQuery("", true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newQuery: String): Boolean {
                    viewModel.search(newQuery)
                    return true
                }
            })

            requestFocus()
            requestFocusFromTouch()
        }
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