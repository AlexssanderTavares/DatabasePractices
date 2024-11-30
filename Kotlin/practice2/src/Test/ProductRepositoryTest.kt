package Test

import DataSources.Data.DatabaseConnection
import DataSources.Repositories.ProductRepository
import org.junit.After
import org.junit.Before
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




}