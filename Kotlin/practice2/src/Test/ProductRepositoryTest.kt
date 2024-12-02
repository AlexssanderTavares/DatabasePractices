package Test

import DataSources.Data.DatabaseConnection
import DataSources.Repositories.ProductRepository
import Models.Product
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.fail
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.sql.Connection

@RunWith(JUnit4::class)
class ProductRepositoryTest {

    private lateinit var repo: ProductRepository

    @Before
    fun setup(){
        this.repo = ProductRepository(testEnvironment = true)
    }

    @After
    fun report(){

    }

    @Test
    fun ShouldInsertRegistryInTestDatabaseAndReturnTheNumberOfAffectedRows(){
        val product: Product = Product("TestDummy3", 5, 3)
        var res: Int = 0
        try {
            res = this.repo.insert(product.name, product.quantity, product.category)
            assertEquals(1, res)
        } catch (e: Exception) {
            fail("Failed to insert, Result: $res", e)
        }
    }

    @Test
    fun ShouldReturnAProductRegistryFromDatabase(){
        var product: Product? = null
        try {
            product = this.repo.get("TestDummy3") ?: Product("Junk", 1, 1)
            assertEquals("TestDummy3", product?.name)
        } catch (e: Exception){
            fail("Failed to get requested data, returned: ${product?.name}")
        }
    }

    @Test
    fun ShouldDeleteAProductAndReturnTheNumberOfAffectedRows(){
        var rows: Int = 0
        try {
            rows = this.repo.delete("TestDummy3")
            assertEquals(1, rows)
        } catch (e: Exception) {
            fail("Something goes wrong when trying to delete! Rows affected: ${rows}")
        }
    }

    @Test
    fun ShouldReturnNumberOfAffectedRowsWhenTryingToUpdateName(){
        var rows: Int = 0
        try {
            rows = this.repo.updateName("TestDummy", "Alexssander")
            assertEquals(1, rows)
        } catch (e: Exception) {
            fail("Failed to update data, returned: ${rows}")
        }
    }

    @Test
    fun ShouldReturnNumberOfAffectedRowsWhenTryingToUpdateQuantity(){
        var rows: Int = 0
        try {
            rows = this.repo.updateQuantity("Alexssander", 5)
            assertEquals(1, rows)
        } catch (e: Exception) {
            fail("Failed to update data, returned: ${rows}")
        }
    }

    @Test
    fun ShouldReturnNumberOfAffectedRowsWhenTryingToUpdateCategory(){
        var rows: Int = 0
        try {
            rows = this.repo.updateCategory("Alexssander", 2)
            assertEquals(1, rows)
        } catch (e: Exception) {
            fail("Failed to update data, returned: ${rows}")
        }
    }




}