package com.sanislo.nennospizza

import android.app.Application
import com.sanislo.nennospizza.di.getModules
import org.koin.android.java.KoinAndroidApplication
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class PizzaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinApplication.init()
        val koin: KoinApplication = KoinAndroidApplication.create(this)
            .modules(getModules())
        //startKoin(Glo(), koin)
        startKoin(GlobalContext, koin)
    }
}