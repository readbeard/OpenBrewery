package com.readbeard.openbrewery.app.beerlist.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepositoryImpl
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class BrewerySource @Inject constructor(private val breweryRepositoryImpl: BreweryRepositoryImpl) :
    PagingSource<Int, Brewery>() {

    override fun getRefreshKey(state: PagingState<Int, Brewery>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Brewery> {
        return try {
            val nextPage = params.key ?: 1
            val breweryListResponse = breweryRepositoryImpl.loadBreweriesAtPage(nextPage)

            when(breweryListResponse) {
                is CustomResult.Success -> {
                    LoadResult.Page(
                        data = breweryListResponse.value,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = nextPage.plus(1)
                    )
                }
                else -> {
                    LoadResult.Error(Exception("An error occurred while retrieving paged data"))
                }
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
