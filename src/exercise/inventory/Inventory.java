package src.exercise.inventory;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Inventory {

    interface IProductInventory{
        void addProduct(Product product);
        Product getProduct(String sku);
        boolean purchaseProduct(String sku, int quantity);
        void removeProduct(String sku);
    }


    class Product {
        private final String sku;
        private final String name;
        private final AtomicInteger stock;


        public Product(String sku, String name, AtomicInteger stock) {
            this.sku = sku;
            this.name = name;
            this.stock = stock;
        }

        public String getSku() {
            return sku;
        }

        public String getName() {
            return name;
        }

        public AtomicInteger getStock() {
            return stock;
        }

        @Override
        public boolean equals(Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if(o instanceof Product){
                Product product = (Product) o;
                return this.getSku().equals(product.getSku());
            }else{
                return false;
            }
        }

        @Override
        public int hashCode(){
            return Objects.hash(this.sku);
        }

    }

    class ProductInventoryManager implements IProductInventory {
        private final ConcurrentMap<String, Product> inventory = new ConcurrentHashMap<>();

        @Override
        public void addProduct(Product product) {
            if(inventory.containsKey(product.getSku())){
                System.out.println("Product already exists: " + product.getName());
                var stock = inventory.get(product.getSku()).getStock();
                product.getStock().addAndGet(stock.get());
                inventory.put(product.getSku(), product);
                System.out.println("Updated stock for product: " + product.getName() + " to " + product.getStock().get());
            }else{
                System.out.println("Adding product: " + product.getName());
                inventory.put(product.getSku(), product);
            }

        }

        @Override
        public Product getProduct(String sku) {
            return inventory.getOrDefault(sku,null);
        }

        @Override
        public boolean purchaseProduct(String sku, int quantity) {

            boolean isPurchased = false;
            Product product = getProduct(sku);
            if (product == null)
                return false;

            // Synchronize on the product object to ensure thread safety
            synchronized(product){

                var currentStock = product.getStock();
                if (quantity > currentStock.get()) {
                    System.out.println("Not enough stock for product: " + product.getSku());
                }else{
                    System.out.println("Purchased " + quantity + " of product: " + product.getSku());
                    int updated = currentStock.addAndGet(-quantity);
                    if(updated == 0)
                        removeProduct(sku);

                    isPurchased = true;
                }

            }

            return isPurchased;
        }

        @Override
        public void removeProduct(String sku) {
            Product product = getProduct(sku);

            if (product != null) {
                synchronized (product) {
                    inventory.remove(sku);
                    System.out.println("Removed product: " + product.getSku());
                }
            }else {
                System.out.println("Product not found: " + sku);
            }

        }

    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Inventory inventory = new Inventory();
        ProductInventoryManager productInventoryManager = inventory.new ProductInventoryManager();


        Product product1 = inventory.new Product("SKU123", "Product 1", new AtomicInteger(10));
        Product product2 = inventory.new Product("SKU456", "Product 2", new AtomicInteger(5));

        productInventoryManager.addProduct(product1);
        productInventoryManager.addProduct(product2);

        var inventory1 = productInventoryManager.getProduct("SKU123");
        var inventory2 = productInventoryManager.getProduct("SKU456");


        System.out.println("Available stock for SKU123: " + inventory1.getStock().get());
        System.out.println("Available stock for SKU456: " + inventory2.getStock().get());


        Callable<Boolean> task = () -> {
            boolean result = productInventoryManager.purchaseProduct("SKU123", 4);
            System.out.println(Thread.currentThread().getName() + " purchase result: " + result);
            return result;
        };

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<Boolean>> futureList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            futureList.add(executor.submit(task));
        }

        int successfulPurchases = 0;
        for (Future<Boolean> future : futureList) {
            if (future.get()) {
                successfulPurchases++;
            }
        }

        executor.shutdown();

        System.out.println("Total successful purchases: " + successfulPurchases);


    }


}
