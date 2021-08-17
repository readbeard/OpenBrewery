package com.readbeard.openbrewery.app.beerlist.data.model

enum class BreweryFilter(val value: String) {
    NONE(""),
    NAME("by_name"),
    CITY("by_city"),
    POSTALCODE("by_postal"),
    STATE("by_state"),
    DISTANCE("by_distance"),
    TYPE("by_type")
}

fun getAllFilters(): List<BreweryFilter> {
    return listOf(
        BreweryFilter.NONE,
        BreweryFilter.NAME,
        BreweryFilter.CITY,
        BreweryFilter.POSTALCODE,
        BreweryFilter.STATE,
        BreweryFilter.DISTANCE,
        BreweryFilter.TYPE
    )
}

fun getFilter(value: String): BreweryFilter {
    val map = BreweryFilter.values().associateBy(BreweryFilter::value)
    return map[value] ?: BreweryFilter.NONE
}
