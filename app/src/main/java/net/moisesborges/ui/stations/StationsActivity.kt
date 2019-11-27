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

package net.moisesborges.ui.stations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import net.moisesborges.R
import net.moisesborges.databinding.ActivityStationsBinding
import net.moisesborges.model.Genre
import net.moisesborges.ui.base.LifecycleActivity
import net.moisesborges.ui.home.adapter.StationItemViewModelFactory
import net.moisesborges.ui.stations.adapter.StationsAdapter
import net.moisesborges.ui.stations.mvvm.StationsViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.lang.IllegalStateException

private const val GENRE_NAME_ARG = "StationsActivity.genreName"

class StationsActivity : LifecycleActivity() {

    private val viewModel: StationsViewModel by inject { parametersOf(filter) }
    private lateinit var binding: ActivityStationsBinding

    private val viewModelFactory: StationItemViewModelFactory by inject()
    private val adapter = StationsAdapter(viewModelFactory)

    private val filter: StationsViewModel.Filter by lazy {
        val genreName = intent.genreName
        when {
            genreName != null -> StationsViewModel.Filter.Genre(genreName)
            else -> throw IllegalStateException("Not supported")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stations)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.title = viewModel.title
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.stationsRecyclerView.adapter = adapter

        viewModel.stations.observe(this, Observer { stations ->
            adapter.stations = stations
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.backButtonSelected()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

private var Intent.genreName: String?
    get() = getStringExtra(GENRE_NAME_ARG)
    set(value) {
        putExtra(GENRE_NAME_ARG, value)
    }

fun createStationsActivityByGenre(context: Context, genre: Genre): Intent =
    Intent(context, StationsActivity::class.java).apply {
        genreName = genre.name
    }