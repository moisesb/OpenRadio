package net.moisesborges.ui.recentsearches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.moisesborges.R
import net.moisesborges.ui.base.LifecycleFragment

class RecentSearchesFragment : LifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recent_searches, container, false)
    }
}