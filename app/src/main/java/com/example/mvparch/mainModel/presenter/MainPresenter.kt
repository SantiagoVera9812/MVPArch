package com.example.mvparch.mainModel.presenter

import android.util.Log
import com.example.mvparch.common.EventBus
import com.example.mvparch.common.SportEvent
import com.example.mvparch.mainModel.model.MainRepository
import com.example.mvparch.mainModel.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainPresenter(private val view: MainActivity) {

    private val repository = MainRepository()
    private lateinit var viewScope: CoroutineScope

    fun onCreate(){
        viewScope = CoroutineScope(Dispatchers.IO + Job())
        onEvent()
    }

    fun onDestroy(){
        viewScope.cancel()
    }

    suspend fun refresh(){
        view.clearAdapter()
        getEvents()
        view.showAddUI(true)
    }

    suspend fun getEvents() {
        view.showProgress(true)
        repository.getEvents()
    }

    suspend fun registerAd(){
        repository.registerAdd()
    }

    suspend fun closeAd(){
        repository.closeAd()
    }

    suspend fun saveResult(result: SportEvent.ResultSuccess){
        view.showProgress(true)
        repository.saveResult(result)
    }

    private fun onEvent() {
        viewScope.launch {

            EventBus.instance().subscribe<SportEvent> { event ->
               this.launch {
                   when (event) {
                       is SportEvent.ResultSuccess -> {
                           view.add(event)
                           view.showProgress(false)
                       }

                       is SportEvent.SaveEvent -> {

                           view.showToast("Guardado")
                           view.showProgress(false)
                       }

                       is SportEvent.ResultError -> {

                           view.showSnackbar("Code: ${event.code}, Message: ${event.msg}")
                           view.showProgress(false)
                       }

                       is SportEvent.AdEvent ->
                           view.showToast("Ad click. Send data to server")

                       is SportEvent.CloseEent -> {
                           view.showAddUI(false)
                           Log.i("CursosANTAG", "Ad was closed, Send data to server...")
                       }

                   }
               }
            }
        }

    }


}