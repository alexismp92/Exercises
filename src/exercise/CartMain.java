package src.exercise;


import java.util.ArrayList;
import java.util.List;

/**
 * Cart exercise.
 * This class requires to implement a shopping cart with the following functionalities:
 * 1. Add item to cart
 * 2. Get item from cart
 * 3. Delete item from cart
 * 4. Remove item quantity from cart
 * 5. Get total price of items in cart
 */
class Item{
    private long id;
    private String name;
    private double price;
    private int quantity;

    public Item(long id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }
}

class Cart {
    private List<Item> cartItems = new ArrayList<>();

    public void addItem(Item item){
        System.out.println("Adding item " + item + " to cart.");
        Item existingItem = getItem(item.getId());
        if (existingItem != null) {
            System.out.println("Item with id " + item.getId() + " already exists in cart. Updating quantity.");
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            System.out.println("Updated item " + existingItem + " in cart.");
        }else{
            cartItems.add(item);
        }

    }

    public Item getItem(long id){
        System.out.println("Getting item with id " + id + " from cart.");
        return this.cartItems.stream().filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deleteItem(long id){
        this.cartItems.removeIf(item -> item.getId() == id);
        System.out.println("Item with id " + id + " removed from cart.");
    }

    public void removeItemQuantity(long id, int quantity){
        Item existingItem = getItem(id);
        if(existingItem == null){
            System.out.println("Item with id " + id + " not found in cart.");
            return;
        }

        if(existingItem.getQuantity() < quantity) {
            System.out.println("Quantity to remove is greater than available in cart. Removing all.");
            this.cartItems.remove(existingItem);
            return;
        }

        existingItem.setQuantity(existingItem.getQuantity() - quantity);
        System.out.println("Removed " + quantity + " of item with id " + id + " from cart." +
                " Remaining quantity: " + existingItem.getQuantity());
    }

    public double getTotalPrice(){
        double total = 0;
        for (var item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        System.out.println("Total price: " + total);
        return total;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cart{");
        sb.append("cartItems=").append(cartItems);
        sb.append('}');
        return sb.toString();
    }
}


public class CartMain {

    public static void main(String[] args) {
        Cart cart = new Cart();
        Item item1 = new Item(1, "item1", 10.0, 2);
        Item item2 = new Item(2, "item2", 20.0, 3);
        Item item3 = new Item(3, "item3", 30.0, 4);
        Item item4 = new Item(1, "item1", 10.0, 8);

        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);
        cart.addItem(item4);

        System.out.println(cart.getItem(2));
        cart.deleteItem(2);
        cart.removeItemQuantity(1, 5);

        System.out.println(cart);
        System.out.println(cart.getTotalPrice());
    }
}