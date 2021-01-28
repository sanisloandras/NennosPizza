package com.sanislo.nennospizza

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sanislo.nennospizza.db.AppDb
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.DrinkCartItemEntity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class DrinkCartDaoTest {
    private lateinit var dao: DrinkCartDao

    @Before
    fun setup() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDb::class.java).build()
        dao = db.drinkCartDao()
    }

    @Test
    fun test() {
        val d = Date()
        val drinkEntities = mutableListOf(
                DrinkCartItemEntity("1", 1, "Still Water", "$1", d),
                DrinkCartItemEntity("2", 3, "Coke", "$2.5", d),
                DrinkCartItemEntity("3", 2, "Sparkling Water", "$1.5", d),
                DrinkCartItemEntity("4", 2, "Beer", "$3.5", d),
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