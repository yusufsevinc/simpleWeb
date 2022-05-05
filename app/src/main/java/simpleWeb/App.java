/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package simpleWeb;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    private static Logger logger= (Logger) LogManager.getLogger(App.class);

    public static void main(String[] args) {
        
        logger.error("The project is working");
        ArrayList<Product> products = new ArrayList<>();
        int port = Integer.parseInt(System.getenv("PORT"));
        port(port);
        get("/",
                (request,response) -> {

                    Map<String,String> map =new HashMap<String, String>();

                    return new ModelAndView(map, "product.mustache");
                },
                new MustacheTemplateEngine()
        );
        get("/main-page",
                (request,response) -> {

                    Map<String,ArrayList<Product>> map =new HashMap<String, ArrayList<Product>>();
                    map.put("products",products);
                    return new ModelAndView(map, "product.mustache");
                },
                new MustacheTemplateEngine()
        );



        post("/product-add",
                (request,response) -> {
                    String productNameList = request.queryParams("productNameList");
                    String productCountList = request.queryParams("productCountList");
                    int productCountListAsInt = Integer.parseInt(productCountList);

                    App.addProduct(products,productCountListAsInt,productNameList);

                    Map<String,String> map =new HashMap<String, String>();
                    response.redirect("/main-page");
                    return new ModelAndView(map, "product.mustache");
                },
                new MustacheTemplateEngine()
        );

        post("/product-cancel",
                (request,response) -> {
                    String productName = request.queryParams("productName");
                    String productCount = request.queryParams("productCount");
                    int productCountAsInt = Integer.parseInt(productCount);
                    String validation = request.queryParams("validation");
                    boolean validationAsBoolean = Boolean.parseBoolean(validation);

                    boolean result =  App.cancelProduct(products,productCountAsInt,productName,validationAsBoolean);
                    Map<String,Boolean> map =new HashMap<String, Boolean>();
                    map.put("result",result);
                    return new ModelAndView(map,"product.mustache");


                },

                new MustacheTemplateEngine()
        );

    }

    public static boolean cancelProduct(ArrayList<Product> products, int count, String name, boolean validation) {

        if (products == null || products.isEmpty() || name == null
                || name.isEmpty() || count <= 0 || validation == false) {
            logger.error("Make sure you enter the information correctly!");
            return false;
        }
        for (Product productItem : products) {
            if (productItem.getName().equals(name)) {
                if (productItem.getCount() >= count) {
                    productItem.setCount(productItem.getCount() - count);
                    logger.error( count + " " + name +" deleted from the list.");
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean addProduct(ArrayList<Product> products, int count, String name){
        for (Product product:products) {
            if (product.getName().equals(name)) {
                product.setCount(product.getCount() + count);
                logger.error("The number of " + name + " has been increased by " + count);
                return true;
            }
        }
        Product product = new Product(name,count);
        products.add(product);
        logger.error(count + " " +  name + " in the list added!");
        return false;
    }
}
