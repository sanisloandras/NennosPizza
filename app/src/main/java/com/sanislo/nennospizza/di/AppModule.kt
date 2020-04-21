package com.sanislo.nennospizza.di

import androidx.room.Room
import com.sanislo.nennospizza.api.checkout.CheckoutService
import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.db.AppDb
import com.sanislo.nennospizza.domain.repository.*
import com.sanislo.nennospizza.domain.usecase.*
import com.sanislo.nennospizza.presentation.MainViewModel
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.presentation.PizzaImageLoaderImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    factory { checkoutService() }
    factory { dataService() }
    factory { Room
        .databaseBuilder(androidApplication(), AppDb::class.java, "appdb.db")
        .fallbackToDestructiveMigration()
        .build()
    }
    single { get<AppDb>().pizzaCartDao() }
    single { get<AppDb>().drinkCartDao() }
    single<PizzaImageLoader> { PizzaImageLoaderImpl(androidApplication()) }
    factory<PizzaRepository> {
        PizzaRepositoryImpl(
            get()
        )
    }
    factory<IngredientsRepository> {
        IngredientsRepositoryImpl(
            get()
        )
    }
    factory<DrinksRepository> {
        DrinksRepositoryImpl(get())
    }
    factory {
        GetPizzaListUseCase(
            get(),
            get()
        )
    }
    factory {
        GetPizzaDetailsUseCase(
            get(),
            get()
        )
    }
    factory { IngredientCheckUseCase(get()) }
    factory { AddPizzaToCartUseCase(get()) }
    factory { RemoveFromCartUseCase(get(), get()) }
    factory { CheckoutUseCase(get(), get(), get()) }
    factory { GetDrinksUseCase(get()) }
    factory { AddDrinkToCartUseCase(get(), get()) }
    factory { CartUseCase(get(), get()) }
}

fun dataService(): DataService {
    return Retrofit.Builder()
        .baseUrl("https://doclerlabs.github.io/mobile-native-challenge/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DataService::class.java)
}

fun checkoutService(): CheckoutService {
    return Retrofit.Builder()
        .baseUrl("http://httpbin.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CheckoutService::class.java)
}

fun getModules(): List<Module> {
    return appModule + mainModule
}

val mainModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}