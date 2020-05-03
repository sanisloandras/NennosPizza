package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.sanislo.nennospizza.domain.repository.IngredientsRepositoryImpl
import com.sanislo.nennospizza.domain.repository.PizzaRepositoryImpl
import com.sanislo.nennospizza.domain.usecase.GetPizzaDetailsUseCase
import com.sanislo.nennospizza.presentation.details.IngredientListItem
import com.sanislo.nennospizza.presentation.details.PizzaDetails
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class GetPizzaDetailsUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test() {
        runBlocking{
            val mockDataService = MockDataSerivce.create()
            val pizzaRepository = PizzaRepositoryImpl(mockDataService)
            val ingredientsRepository = IngredientsRepositoryImpl(mockDataService)
            val useCase = GetPizzaDetailsUseCase(pizzaRepository, ingredientsRepository)
            val pizzaListItem = PizzaListItem("Boscaiola",
                "1,2,3,4",
                "mock",
                "https://cdn.pbrd.co/images/tOhJQ5N3.png",
                listOf(1,2,3,4))
            val actual = useCase.invoke(pizzaListItem)
            Mockito.verify(mockDataService, times(1)).pizzas()
            Mockito.verify(mockDataService, times(1)).ingredients()
            assertEquals(expected(), actual)
        }
    }

    private fun expected(): PizzaDetails {
        val ingredientsListItems = listOf(
            IngredientListItem(1, "Mozzarella", "$1.0"),
            IngredientListItem(2, "Tomato Sauce", "$0.5"),
            IngredientListItem(3, "Salami", "$1.5"),
            IngredientListItem(4, "Mushrooms", "$2.0"),
            IngredientListItem(5, "Ricci", "$4.0"),
            IngredientListItem(6, "Asparagus", "$2.0"),
            IngredientListItem(7, "Pineapple", "$1.0"),
            IngredientListItem(8, "Speck", "$3.0"),
            IngredientListItem(9, "Bottarga", "$2.5"),
            IngredientListItem(10, "Tuna", "$2.2")
        )
        return PizzaDetails(
            "Boscaiola",
            "https://cdn.pbrd.co/images/tOhJQ5N3.png",
            ingredientsListItems,
            mapOf(1 to true, 2 to true, 3 to true, 4 to true),
            9.0
        )
    }
}