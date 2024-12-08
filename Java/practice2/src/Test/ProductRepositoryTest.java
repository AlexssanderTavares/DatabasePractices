package Test;

import DataSources.Repositories.ProductRepository;
import Models.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class ProductRepositoryTest {

    private static ProductRepository repo;
    private static boolean InsertTestResult;
    private static boolean GetTestResult;
    private static boolean DeleteTestResult;
    private static boolean UpdateNameTestResult;
    private static boolean UpdateQuantityTestResult;
    private static boolean UpdateCategoryTestResult;
    private static boolean GetAllTestResult;

    @Before
    public void setup(){
        repo = new ProductRepository(true);
    }

    @After
    public void report(){
        System.out.println("-------------------------Report------------------------");
        System.out.println("Insertion Test result: " + (InsertTestResult ? "Passed" : "Not Passed"));
        System.out.println("Get Product Register Test result: " + (GetTestResult ? "Passed" : "Not Passed"));
        System.out.println("Delete Test result: " + (DeleteTestResult ? "Passed" : "Not Passed"));
        System.out.println("Update Name Test result: " + (UpdateNameTestResult ? "Passed" : "Not Passed"));
        System.out.println("Update Quantity Test result: " + (UpdateQuantityTestResult ? "Passed" : "Not Passed"));
        System.out.println("Update Category Test result: " + (UpdateCategoryTestResult ? "Passed" : "Not Passed"));
        System.out.println("Get List of All Registers Test result: " + (GetAllTestResult ? "Passed" : "Not Passed"));
        System.out.println("-------------------------Report------------------------");
    }

    @Test
    public void ShouldInsertRegistryInTestDatabaseAndReturnTheNumberOfAffectedRows(){
        Product product = new Product("TestJavaDummy", 10, 2);
        int res = 0;
        try {
            res = repo.insert(product.getName(), product.getQuantity(), product.getCategory());
            assert res == 1;
            InsertTestResult = true;
        } catch (Exception e){
            InsertTestResult = false;
            System.out.println(e.getMessage());
            fail("Failed to insert | Result: " + res);
        }
    }

    @Test
    public void ShouldReturnAProductRegistryFromDatabase(){
        Product product;
        try {
            product = repo.get("TestJavaDummy");
            assert "TestJavaDummy".equals(product.getName());
            GetTestResult = true;
        } catch (Exception e) {
            GetTestResult = false;
            System.out.println(e.getMessage());
            fail("Failed to get data.");
        }
    }

    @Test
    public void ShouldDeleteAProductAndReturnTheNumberOfAffectedRows(){
        int rows = 0;
        try {
            rows = repo.delete("TestJavaDummy");
            assert rows == 1;
            DeleteTestResult = true;
        } catch (Exception e){
            DeleteTestResult = false;
            System.out.println(e.getMessage());
            fail("Something goes wrong when trying to delete | " + rows + " rows affected");
        }
    }

    @Test
    public void ShouldReturnNumberOfAffectedRowsWhenTryingToUpdateName(){
        int rows = 0;
        try {
            rows = repo.updateName("TestDummy3", "JavaTestDummy3");
            assert rows == 1;
            UpdateNameTestResult = true;
        } catch (Exception e){
            UpdateNameTestResult = false;
            System.out.println(e.getMessage());
            fail("Failed to update data, " + rows + " rows affected.");
        }
    }

    @Test
    public void ShouldReturnNumberOfAffectedRowsWhenTryingToUpdateQuantity(){
        int rows = 0;
        try {
            rows = repo.updateQuantity("Alexssander" , 10);
            assert rows == 1;
            UpdateQuantityTestResult = true;
        } catch (Exception e){
            UpdateQuantityTestResult = false;
            System.out.println(e.getMessage());
            fail("Failed to update data, " + rows + " rows affected.");
        }
    }

    @Test
    public void ShouldReturnNumberOfAffectedRowsWhenTryingToUpdateCategory(){
        int rows = 0;
        try {
            rows = repo.updateCategory("Alexssander", 1);
            assert rows == 1;
            UpdateCategoryTestResult = true;
        } catch (Exception e) {
            UpdateCategoryTestResult = false;
            System.out.println(e.getMessage());
            fail("Failed to update data, " + rows + " rows affected.");
        }
    }

    @Test
    public void ShouldReturnListOfAllRegisteredProducts(){
        try {
            ArrayList<Product> list = repo.getAll();
            System.out.println(list.toString());
            assert list.size() == 4;
            GetAllTestResult = true;
        } catch (Exception e) {
            GetAllTestResult = false;
            System.out.println(e.getMessage());
            fail("Failed to get any register in database OR nothing was registered inside it before.");
        }
    }


}
