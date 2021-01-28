package com.sanislo.nennospizza

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sanislo.nennospizza.db.AppDb
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.db.PizzaCartItemEntity
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class PizzaCartDaoTest {
    private lateinit var dao: PizzaCartDao

    @Before
    fun setup() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDb::class.java).build()
        dao = db.pizzaCartDao()
    }

    @Test
    fun test() {
        val d = Date()
        val drinkEntities = mutableListOf(
                PizzaCartItemEntity("1", "p1", "$1", setOf(1, 2), d),
                PizzaCartItemEntity("2", "p3", "$3", setOf(3, 4, 5), d),
                PizzaCartItemEntity("3", "p2", "$2.5", setOf(2, 7), d),
                PizzaCartItemEntity("4", "p4", "$4", setOf(2, 7, 9), d),
        )
        drinkEntities.forEach {
            dao.insert(it)
        }
        assertTrue(dao.all().size == 4)

        dao.deleteById("3")
        assertTrue(dao.all().size == 3)
        assertEquals(drinkEntities.filter {
            it.id != "3"
        }, dao.all())

        dao.clear()
        assertTrue(dao.all().isEmpty())
    }
}