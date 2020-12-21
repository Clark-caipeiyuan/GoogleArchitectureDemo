package google.architecture.coremodel.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import android.util.Log
import com.apkfuns.logutils.LogUtils
import com.google.gson.Gson
import google.architecture.coremodel.datamodel.http.entities.NewsData
import google.architecture.coremodel.datamodel.http.repository.GankDataRepository
import google.architecture.coremodel.util.NetUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by dxx on 2017/11/17.
 */
class NewsViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    //生命周期观察的数据
    val liveObservableData: LiveData<NewsData>

    //UI使用可观察的数据 ObservableField是一个包装类
    var uiObservableData = ObservableField<NewsData>()
    private val mDisposable = CompositeDisposable()

    /**
     * 设置
     * @param product
     */
    fun setUiObservableData(product: NewsData) {
        uiObservableData.set(product)
    }

    override fun onCleared() {
        super.onCleared()
        LogUtils.d("========NewsViewModel--onCleared=========")
        mDisposable.clear()
    }

    companion object {
        private val ABSENT: MutableLiveData<*> = MutableLiveData<Any?>()
    }

    init {
        ABSENT.value = null
    }

    init {
        Log.i("danxx", "GirlsViewModel------>")
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations.switchMap(NetUtils.netConnected(application), Function { isNetConnected ->
            Log.i("danxx", "apply------>")
            if (!isNetConnected!!) {
                ABSENT //网络未连接返回空
            }
            val applyData = MutableLiveData<NewsData>()
            GankDataRepository.getNewsDataRepository("20", "1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<NewsData> {
                        override fun onSubscribe(d: Disposable) {
                            mDisposable.add(d)
                        }

                        override fun onNext(value: NewsData) {
                            Log.d("clark", "getdata success")
                            Log.d("clark", Gson().toJson(value))
                            applyData.setValue(value)
                        }

                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            applyData
        })
    }
}