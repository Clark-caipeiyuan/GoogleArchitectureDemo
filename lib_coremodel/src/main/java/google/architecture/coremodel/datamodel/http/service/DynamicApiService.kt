package google.architecture.coremodel.datamodel.http.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by dxx on 2017/11/20.
 */
interface DynamicApiService<T> {
    @GET
    fun getDynamicData(@Url fullUrl: String?): Observable<ResponseBody?>?
}