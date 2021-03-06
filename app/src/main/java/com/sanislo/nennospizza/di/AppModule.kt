package com.sanislo.nennospizza.di

import androidx.room.Room
import com.sanislo.nennospizza.api.checkout.CheckoutService
import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.db.AppDb
import com.sanislo.nennospizza.domain.repository.*
import com.sanislo.nennospizza.domain.usecase.*
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.presentation.PizzaImageLoaderImpl
import com.sanislo.nennospizza.presentation.cart.CartViewModel
import com.sanislo.nennospizza.presentation.details.PizzaDetailsViewModel
import com.sanislo.nennospizza.presentation.drinks.DrinkListViewModel
import com.sanislo.nennospizza.presentation.list.PizzaListViewModel
import kotlinx.coroutines.Dispatchers
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
    single<PizzaRepository> { PizzaRepositoryImpl(get()) }
    single<IngredientsRepository> { IngredientsRepositoryImpl(get()) }
    single<DrinksRepository> { DrinksRepositoryImpl(get()) }
    factory { GetPizzaListUseCase(Dispatchers.IO, get(), get()) }
    factory { AddPizzaToCartUseCase(get()) }
    factory { RemoveFromCartUseCase(get(), get()) }
    factory { CheckoutUseCase(get(), get(), get()) }
    factory { GetDrinksUseCase(get()) }
    factory { AddDrinkToCartUseCase(get(), get()) }
    factory { CartUseCase(get(), get()) }
    factory { GetPizzaDetailsUseCase(get(), get()) }
    factory { GetPizzaPriceChangeUseCase(get()) }
    factory { GetTransitionNameUseCase(androidApplication()) }
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
    return appModule + pizzaListModule + pizzaDetailsModule + drinksModule + cartModule
}

val pizzaListModule = module {
    viewModel { PizzaListViewModel(Dispatchers.IO, get(), get()) }
}

val pizzaDetailsModule = module {
    viewModel { PizzaDetailsViewModel(get(), get(), get()) }
}

val drinksModule = module {
    viewModel { DrinkListViewModel(Dispatchers.IO, get(), get()) }
}

val cartModule = module {
    viewModel { CartViewModel(get(), get(), get()) }
}