package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.sanislo.nennospizza.domain.repository.IngredientsRepositoryImpl
import com.sanislo.nennospizza.presentation.details.PizzaDetails
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

//todo this will be removed
class IngredientCheckUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    /*@Test
    fun test() {
        runBlocking{
            val mockDataService = MockDataSerivce.create()
            val ingredientsRepository = IngredientsRepositoryImpl(mockDataService)
            val useCase = IngredientCheckUseCase(ingredientsRepository)
            val ingredientsListItems = listOf(
                    IngredientItemItem(1, "Mozzarella", "$1.0"),
                    IngredientItemItem(2, "Tomato Sauce", "$0.5"),
                    IngredientItemItem(3, "Salami", "$1.5"),
                    IngredientItemItem(4, "Mushrooms", "$2.0"),
                    IngredientItemItem(5, "Ricci", "$4.0"),
                    IngredientItemItem(6, "Asparagus", "$2.0"),
                    IngredientItemItem(7, "Pineapple", "$1.0"),
                    IngredientItemItem(8, "Speck", "$3.0"),
                    IngredientItemItem(9, "Bottarga", "$2.5"),
                    IngredientItemItem(10, "Tuna", "$2.2")
            )
            val pizzaDetails = PizzaDetails("Boscaiola",
                "https://cdn.pbrd.co/images/tOhJQ5N3.png",
                ingredientsListItems,
                mapOf(1 to true, 2 to true, 3 to true, 4 to true),
                9.0,
                true
            )
            val actual = useCase.invoke(pizzaDetails, 9, true)
            val expected = expected()
            assertEquals(expected, actual)
            val actual2 = useCase.invoke(actual, 9, true)
            assertEquals(expected, actual2)
            Mockito.verify(mockDataService, times(1)).ingredients()
        }
    }

    private fun expected(): PizzaDetails {
        val ingredientsListItems = listOf(
                IngredientItemItem(1, "Mozzarella", "$1.0"),
                IngredientItemItem(2, "Tomato Sauce", "$0.5"),
                IngredientItemItem(3, "Salami", "$1.5"),
                IngredientItemItem(4, "Mushrooms", "$2.0"),
                IngredientItemItem(5, "Ricci", "$4.0"),
                IngredientItemItem(6, "Asparagus", "$2.0"),
                IngredientItemItem(7, "Pineapple", "$1.0"),
                IngredientItemItem(8, "Speck", "$3.0"),
                IngredientItemItem(9, "Bottarga", "$2.5"),
                IngredientItemItem(10, "Tuna", "$2.2")
        )
        return PizzaDetails(
            "Boscaiola",
            "https://cdn.pbrd.co/images/tOhJQ5N3.png",
            ingredientsListItems,
            mapOf(1 to true, 2 to true, 3 to true, 4 to true, 9 to true),
            11.5
        )
    }*/
}