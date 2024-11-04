package com.example.mvparch.mainModel.model

import android.util.Log
import com.example.mvparch.common.getResultEventsInRealtime
import com.example.mvparch.common.someTime
import com.example.mvparch.common.EventBus
import com.example.mvparch.common.SportEvent
import kotlinx.coroutines.delay

class MainRepository {

    suspend fun getEvents(){

        val events = getResultEventsInRealtime()
        events.forEach{ event ->
            delay(someTime())
            Log.d("event", event.toString())
            publishEvent(event)
    } }

    suspend fun saveResult(result: SportEvent.ResultSuccess){

        val response = if (result.isWarning)
            SportEvent.ResultError(30, "Error al guardar.")
        else SportEvent.SaveEvent
        publishEvent(response)
    }

    suspend fun registerAdd(){
        val events = getResultEventsInRealtime()
        publishEvent(events.first())
    }

    suspend fun closeAd(){
        publishEvent(SportEvent.CloseEent)
    }

    private suspend fun publishEvent(event: SportEvent){
        EventBus.instance().publish(event)
    }
}