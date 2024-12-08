package DataSources.Repositories;

import DataSources.Data.DatabaseConnection;
import Models.Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductRepository {

    private boolean testEnvironment = false;
    private Connection db;

    public ProductRepository(boolean test) {
        this.testEnvironment = test;
        if (test) {
            DatabaseConnection.connect(test);
            this.db = DatabaseConnection.CONNECTION;
        }
    }

    public ProductRepository() {
        DatabaseConnection.connect(false);
        this.db = DatabaseConnection.CONNECTION;
    }

    public int insert(String productName, int quantity, int category) {
        StringBuilder sqlStates = new StringBuilder();
        int affectedRows = 0;
        try {
            if (productName.length() < 120 && category <= 3 || category > 0) {
                PreparedStatement query = this.db.prepareStatement("INSERT INTO Product (str_name, i_quantity, i_category) VALUES ('" + productName + "', '" + quantity + "','" + category + "');");

                query.execute();
                affectedRows += 1;
                System.out.println("Insertion Success! Query ok: " + affectedRows + " row(s) affected.");
                this.db.close();
                return affectedRows;
            } else {
                throw new IllegalArgumentException("Product length must be between 0 to 120 chars and category must be above 0 and below or equals to 3.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Insertion failed or aborted! " + affectedRows + " rows affected.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("You must fulfill the right data requirements in order to register some data.");
            System.out.println("Product name size: " + productName.length() + " | Category: " + category);
        }
        return affectedRows;
    }

    public Product get(String name) {
        Product product;
        try {
            PreparedStatement query = this.db.prepareStatement("SELECT * FROM Product;");

            try {
                ResultSet res = query.executeQuery();
                res.next();

                try {
                    String res2 = res.getString("str_name");
                    while (!res2.equals(name)) {
                        res.next();
                        res2 = res.getString("str_name");
                    }

                    product = new Product(res.getString("str_name"), res.getInt("i_quantity"), res.getInt("i_category"));
                    return product;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    System.out.println("Invalid column OR closed ResultSet");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("The statement doesn't returned anything from DB, this could happen due to connection error or SQL statement error.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Closed connection.");
        }
        product = null;
        return product;
    }

    public int delete(String productName) {
        int affectedRows = 0;
        Product product = this.get(productName);

        if (product != null) {

            try {
                PreparedStatement query = this.db.prepareStatement("DELETE FROM Product WHERE str_name='" + productName + "';");
                query.execute();
                affectedRows += 1;
                System.out.println("Query ok! " + affectedRows + " rows affected.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("Access error OR database is closed!");
            }

        } else {
            System.out.println("Product with such name wasn't found! " + affectedRows + " rows affected.");
            return affectedRows;
        }
        return affectedRows;
    }

    public int updateName(String productName, String newName) {
        int affectedRows = 0;
        Product product = this.get(productName);

        if (product != null) {

            try {
                PreparedStatement query = this.db.prepareStatement("UPDATE Product SET str_name='" + newName + "' WHERE str_name='" + productName + "';");
                query.execute();
                affectedRows += 1;
                System.out.println("Query ok! " + affectedRows + " rows affected.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("Access error OR database is closed.");
            }


        } else {
            System.out.println("Product with such name wasn't found! " + affectedRows + " rows affected.");
            return affectedRows;
        }

        return affectedRows;
    }

    public int updateQuantity(String productName, int newQuantity) {
        int affectedRows = 0;
        Product product = this.get(productName);

        if (product != null) {

            try {
                PreparedStatement query = this.db.prepareStatement("UPDATE Product SET i_quantity='" + newQuantity + "' WHERE str_name='" + productName + "';");
                query.execute();
                affectedRows += 1;
                System.out.println("Query ok! " + affectedRows + " rows affected.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("Access error OR database is closed.");
            }

        }else {
            System.out.println("Product with such name wasn't found! " + affectedRows + " rows affected.");
            return affectedRows;
        }
        return affectedRows;
    }

    public int updateCategory(String productName, int newCategory){
        int affectedRows = 0;
        Product product = this.get(productName);

        if(product != null){

            try{
                PreparedStatement query = this.db.prepareStatement("UPDATE Product SET i_category='" + newCategory + "' WHERE str_name='"+productName+"';");
                query.execute();
                affectedRows += 1;
                System.out.println("Query ok! " + affectedRows + " rows affected.");
            }catch (SQLException e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("Access error OR database is closed.");
            }

        } else{
            System.out.println("Product with such name wasn't found! " + affectedRows + " rows affected;");
            return affectedRows;
        }

        return affectedRows;
    }

    public ArrayList<Product> getAll() {
        ArrayList<Product> productList = new ArrayList<>();

        try {
            PreparedStatement query = this.db.prepareStatement("SELECT * FROM Product;");
            ResultSet res = query.executeQuery();

            while (res.next()){
                Product product = new Product(res.getString("str_name"), res.getInt("i_quantity"), res.getInt("i_category"));
                productList.add(product);
            }
            return productList;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Access error OR database is closed!");
        }
        return productList;
    }


}
