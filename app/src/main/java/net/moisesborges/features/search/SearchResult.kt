package net.moisesborges.features.search

import net.moisesborges.model.Station

data class SearchResult(val isPartial: Boolean, val stations: List<Station>)