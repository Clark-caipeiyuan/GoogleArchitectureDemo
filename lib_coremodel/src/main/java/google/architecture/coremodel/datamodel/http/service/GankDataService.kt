package google.architecture.coremodel.datamodel.http.service

import google.architecture.coremodel.datamodel.http.entities.GirlsData
import google.architecture.coremodel.datamodel.http.entities.NewsData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by dxx on 2017/11/8.
 */
interface GankDataService {
    @GET("api/data/福利/{size}/{index}")
    fun getFuliData(@Path("size") size: String?, @Path("index") index: String?): Observable<GirlsData?>?

    @GET("api/data/Android/{size}/{index}")
    fun getAndroidData(@Path("size") size: String?, @Path("index") index: String?): Observable<NewsData?>?
}