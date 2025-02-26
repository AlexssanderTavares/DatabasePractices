package org.example.cahousing.Accessors

import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.Accessors.AddressAccessor
import org.example.cahousing.DataSource.Data.Accessors.AddressAccessorImp
import org.example.cahousing.DataSource.Models.Address
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class AddressAccessorImpTest {

    private var accessor: AddressAccessorImp = AddressAccessor()

    @Before
    fun setup() {
        accessor.turnTestOn()
    }

    @After
    fun tearDown() {
        accessor.turnTestOff()
    }

    @Test
    fun `Should create address and return the number of affected rows`() {
        runBlocking {
            try {
                println("Trying to create a new address...")
                val address: Address = Address("01001000", "Praça da Sé", "Sé", "São Paulo")
                assertEquals(1, accessor.create(address))
                println("Success!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get data by cep and return address`() {
        runBlocking {
            try {
                println("Trying to get address by cep...")
                assertEquals(
                    Address("01.001-000", "Praça da Sé", "Sé", "São Paulo"),
                    accessor.get("01001000")
                )
                println("Success!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should return all addresses`() {
        runBlocking {
            try {
                println("Trying to get all addresses...")
                val list: ArrayList<Address> = accessor.getAll()
                println(list)
                assert(list.size > 0)
                println("Success!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should update and return the number of affected rows`() {
        runBlocking {
            try {
                println("Trying to update an address...")
                val address: Address = Address("01001000", "Praça da Sé", "Sé", "São Paulo")
                val data: Address = Address("01001000", "Praça do Ibirapuera", "Ibirapuera", "São Paulo")
                assertEquals(1, accessor.update(address, data))
                println("Success!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should delete and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to delete an address...")
                val address: Address = Address("01001000", "Praça da Sé", "Sé", "São Paulo")
                assertEquals(1, accessor.delete(address))
                println("Success!")
            } catch (e: Exception){
                fail("Test failed due to: ${e.message}")



            }
        }
    }
}