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
import com.sanislo.nennospizza.presentation.details.PizzaDetailsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    factory { checkoutService() }
    factory {
        get<Retrofit>(Retrofit::class.java, named("data")).create(DataService::class.java)
    }
    factory(named("data")) {
        Retrofit.Builder()
            .baseUrl("https://doclerlabs.github.io/mobile-native-challenge/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    factory { Room
        .databaseBuilder(androidApplication(), AppDb::class.java, "appdb.db")
        .fallbackToDestructiveMigration()
        .build()
    }
    single { get<AppDb>().pizzaCartDao() }
    single { get<AppDb>().drinkCartDao() }
    single<PizzaImageLoader> { PizzaImageLoaderImpl(androidApplication()) }
    factory<PizzaRepository> { PizzaRepositoryImpl(get()) }
    factory<IngredientsRepository> { IngredientsRepositoryImpl(get()) }
    factory<DrinksRepository> { DrinksRepositoryImpl(get()) }
    factory { GetPizzaListUseCase(get(), get()) }
    factory { GetPizzaDetailsUseCase(get()) }
    factory { GetPizzaDetailsByNameUseCase(get(), get()) }
    factory { AddPizzaToCartUseCase(get()) }
    factory { RemoveFromCartUseCase(get(), get()) }
    factory { CheckoutUseCase(get(), get(), get()) }
    factory { GetDrinksUseCase(get()) }
    factory { AddDrinkToCartUseCase(get(), get()) }
    factory { CartUseCase(get(), get()) }
    factory { GetPizzaPriceUseCase(get(), get()) }
}

fun checkoutService(): CheckoutService {
    return checkoutRetrofit().create(CheckoutService::class.java)
}

fun checkoutRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://httpbin.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getModules(): List<Module> {
    return appModule + mainModule + pizzaDetailsModule
}

val mainModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get()) }
}

val pizzaDetailsModule = module {
    viewModel { PizzaDetailsViewModel(get(), get(), get()) }
}