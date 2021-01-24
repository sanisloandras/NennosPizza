package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.sanislo.nennospizza.domain.repository.IngredientsRepositoryImpl
import com.sanislo.nennospizza.domain.repository.PizzaRepositoryImpl
import com.sanislo.nennospizza.domain.usecase.GetPizzaDetailsUseCase
import com.sanislo.nennospizza.presentation.details.PizzaDetails
import com.sanislo.nennospizza.presentation.details.PizzaDetailsInput
import com.sanislo.nennospizza.presentation.details.PizzaDetailsViewModel
import com.sanislo.nennospizza.presentation.details.list.IngredientItem
import com.sanislo.nennospizza.presentation.details.list.PizzaDetailsHeader
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

            val pizzaDetailsInput = PizzaDetailsInput("Boscaiola", "https://cdn.pbrd.co/images/tOhJQ5N3.png", "")
            val ingredients = listOf(
                    IngredientItem(1, "Mozzarella", "$1.0"),
                    IngredientItem(2, "Tomato Sauce", "$0.5"),
                    IngredientItem(3, "Salami", "$1.5"),
                    IngredientItem(4, "Mushrooms", "$2.0"),
                    IngredientItem(5, "Ricci", "$4.0"),
                    IngredientItem(6, "Asparagus", "$2.0"),
                    IngredientItem(7, "Pineapple", "$1.0"),
                    IngredientItem(8, "Speck", "$3.0"),
                    IngredientItem(9, "Bottarga", "$2.5"),
                    IngredientItem(10, "Tuna", "$2.2")
            )

            val expected = PizzaDetailsViewModel.PizzaDetailsState(
                    PizzaDetailsHeader("https://cdn.pbrd.co/images/tOhJQ5N3.png", ""),
                    ingredients,
                    setOf(1,2,7,8),
                    9.5
            )
            val actual = useCase.invoke(pizzaDetailsInput, setOf(1,2,7,8))
            assertEquals(expected, actual)

            Mockito.verify(mockDataService, times(1)).pizzas()
            Mockito.verify(mockDataService, times(1)).ingredients()
        }
    }
}