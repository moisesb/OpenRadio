package net.moisesborges.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.moisesborges.R
import net.moisesborges.ui.top.mvvm.TopStationsViewModel
import net.moisesborges.databinding.FragmentTopStationsBinding
import net.moisesborges.ui.top.adapter.TopStationsAdapter
import org.koin.android.ext.android.inject

class TopStationsFragment : Fragment(), LifecycleOwner {

    private lateinit var binding: FragmentTopStationsBinding

    private val viewModel: TopStationsViewModel by inject()
    private val adapter = TopStationsAdapter()
    private val stationsRecyclerView: RecyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.stations_recycler_view) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_stations, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        stationsRecyclerView.setHasFixedSize(true)
        stationsRecyclerView.adapter = adapter

        binding.viewModel = viewModel

        viewModel.stations.observe(this, Observer { stations ->
            adapter.setStations(stations)
        })

        viewModel.loadTopRadios()
    }
}