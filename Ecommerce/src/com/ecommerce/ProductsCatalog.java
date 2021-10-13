package com.ecommerce;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ProductsCatalog implements Searchable{
    private ArrayList<Product> products;	// add product to productlist
    private ArrayList<Category> categories; // add category to categorylist
    private HashMap<String,ArrayList<Product>> categoryProductMap;  // search by product category
    private HashMap<String,ArrayList<Seller>> productSellerMap;	// adding seller to seller list for a product
    private ConcurrentHashMap<String,ArrayList<Product>> similarProducts;  // search by productname
    
    
    //constructor
    public ProductsCatalog(){
        products = new ArrayList<>();
        categories = new ArrayList<>();
        categoryProductMap = new HashMap<>();
        similarProducts = new ConcurrentHashMap<>();
        similarProducts.put("Dummy Product",new ArrayList<Product>());
        productSellerMap = new HashMap<>();
    }
    
    // add or update productarraylist in similarproducts map for a given product name
    public void updateSimilarProductsMap(Product newProduct){
        String productName = newProduct.getName().toLowerCase();
        Set<String> keySet = similarProducts.keySet();
        boolean isSimilar = false;
        for (String key : keySet){
            if(key.toLowerCase().contains(productName) || productName.contains(key.toLowerCase())) {
                isSimilar = true;
                similarProducts.get(key).add(newProduct);
            }
        }
        if(!isSimilar){
            similarProducts.put(newProduct.getName().toLowerCase(),new ArrayList<Product>(Arrays.asList(newProduct)));
        }
    }
    //initialisation of categoryproductmap
    private void updateCategoryProductMap(){
        for(Category category : categories){
            categoryProductMap.put(category.getName().toLowerCase(), new ArrayList<Product>());
        }
    }
    
    //adding product to categoryproductmap
    private void updateCategoryProductMap(Product product){
        if(categoryProductMap.containsKey(product.getCategory().getName().toLowerCase())){
            categoryProductMap.get(product.getCategory().getName().toLowerCase()).add(product);
        } else{
            categoryProductMap.put(product.getCategory().getName().toLowerCase(),new ArrayList<>(Arrays.asList(product)));
        }

    }
    
    //adding seller to list based on key product name to productSellermap
    private void updateProductSellerMap(Product newProduct){
        if(productSellerMap.containsKey(newProduct.getName().toLowerCase())){
            productSellerMap.get(newProduct.getName().toLowerCase()).add(newProduct.getSeller());
        }else{
            productSellerMap.put(newProduct.getName().toLowerCase(),new ArrayList<>(Arrays.asList(newProduct.getSeller())));
        }
    }
    
    //adding category to category list
    public void addCategory(Category newCategory){
        categories.add(newCategory);
        updateCategoryProductMap();

    }

    // getter method for similarproducts
    public ConcurrentHashMap<String, ArrayList<Product>> getSimilarProducts() {
        return similarProducts;
    }

    //adding product to productslist, updating product seller map, updating categorymap, updating similarproductmap
    public void addProduct(Product product){
        products.add(product);
        updateProductSellerMap(product);
        updateCategoryProductMap(product);
        updateSimilarProductsMap(product);

    }

    @Override
    public String toString() {
        return "ProductsCatalog{" +
                "products=" + products +
                '}';
    }

    @Override
    public ArrayList<Product> searchProduct(String productName) {
        return similarProducts.get(productName.toLowerCase());
    }

    @Override
    public ArrayList<Product> searchCategory(String categoryName) {
        return categoryProductMap.get(categoryName.toLowerCase());
    }
    
    //updating product quantity
    public void updateProductQuantity(Product product, int newQuantity) {
        for(Product prod: products){
            if(prod.getId()==product.getId()){
                prod.setAvailableCount(newQuantity);
            }
        }
    }
    // remove product from products
    public void removeProduct(Product product) {
        products.remove(product);
    }
}