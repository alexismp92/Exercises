package src.exercise.trade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

enum OrderType { BUY, SELL }

class Order{
    private String id;
    private OrderType type;
    private double price;
    private int quantity;

public Order(String id, OrderType type, double price, int quantity) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
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
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id='").append(id).append('\'');
        sb.append(", type=").append(type);
        sb.append(", price=").append(price);
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }

}

class Trade {
    private String buyOrderId;
    private String sellOrderId;
    private double price;
    private int quantity;

    public Trade(String buyOrderId, String sellOrderId, double price, int quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(String buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(String sellOrderId) {
        this.sellOrderId = sellOrderId;
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
        final StringBuilder sb = new StringBuilder("Trade{");
        sb.append("buyOrderId='").append(buyOrderId).append('\'');
        sb.append(", sellOrderId='").append(sellOrderId).append('\'');
        sb.append(", price=").append(price);
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }
}

public class TradeMain {
    
    private static final List<Trade> tradeHistory = new ArrayList<>();
    // Highest BUY price should come first
    private static final PriorityQueue<Order> buyQueue = new PriorityQueue<>((a, b) -> Double.compare(b.getPrice(), a.getPrice()));

    // Lowest SELL price should come first (default is fine)
    private static final PriorityQueue<Order> sellQueue = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));
    
    public static void main(String[] args) {
        /*
            1. BUY  - ID: B1, Price: 100.0, Quantity: 10
            2. BUY  - ID: B2, Price: 101.0, Quantity: 5
            3. SELL - ID: S1, Price: 99.0,  Quantity: 7
            4. SELL - ID: S2, Price: 101.0, Quantity: 10
        */

        Order b1 = new Order("B1", OrderType.BUY, 100.0, 10);
        Order b2 = new Order("B2", OrderType.BUY, 101.0, 5);
        Order s1 = new Order("S1", OrderType.SELL, 99.0, 7);
        Order s2 = new Order("S2", OrderType.SELL, 101.0, 10);

        TradeMain tradeMain = new TradeMain();
        tradeMain.placeOrder(b1);
        tradeMain.placeOrder(b2);
        tradeMain.placeOrder(s1);
        tradeMain.placeOrder(s2);
        System.out.println(tradeMain.getTradeHistory());

    }

    public void placeOrder(Order order){

        switch (order.getType()) {
            case BUY:
                if(sellQueue.isEmpty()){
                    System.out.println("Buy order added to queue: " + order.getId());
                    buyQueue.add(order);

                }else{

                    while (!sellQueue.isEmpty() && order.getQuantity() > 0){

                        var sellOrder = sellQueue.peek();

                        // BUY WHEN SELL PRICE IS LOWER
                        if(sellOrder.getPrice() <= order.getPrice()){
                            System.out.println("Buying order: " + order);
                            int quantity;

                            boolean isOrderRemaining = false;

                            if(sellOrder.getQuantity() < order.getQuantity()){
                                System.out.println("There are not enough shares to buy. Buying partial shares");
                                quantity = sellOrder.getQuantity();
                            }else{
                                //RETURN THE MIN VALUE
                                quantity = Math.min(sellOrder.getQuantity(), order.getQuantity());
                                isOrderRemaining = true;
                            }

                            var history = new Trade(order.getId(), sellOrder.getId(), sellOrder.getPrice(), quantity);
                            System.out.println("buy trade executed: " + history);
                            tradeHistory.add(history);

                            if(isOrderRemaining){
                                sellOrder.setQuantity(sellOrder.getQuantity() - quantity);
                                order.setQuantity(order.getQuantity() - quantity);

                                if(order.getQuantity() == 0){
                                    System.out.println("Buy order completed");
                                    break;
                                }
                                System.out.println("order quantity updated. Looking for next order in queue...");

                            }else{
                                sellQueue.poll();
                                System.out.println("sell order removed from queue");
                            }

                        }
                    }

                    if(order.getQuantity() > 0) {
                        System.out.println("Buy order added to queue: " + order.getId());
                        buyQueue.add(order);
                    }

                }
                break;
            case SELL:
                if(buyQueue.isEmpty()){
                    System.out.println("Sell order added to queue: " + order.getId());
                    sellQueue.add(order);
                }else{

                    while (!buyQueue.isEmpty() && order.getQuantity() > 0){
                        var buyOrder = buyQueue.peek();

                        // SELL WHEN BUY PRICE IS HIGHER
                        if(buyOrder.getPrice() >= order.getPrice()){
                            System.out.println("Selling order: " + order);
                            int quantity;

                            boolean isOrderRemaining = false;

                            if(buyOrder.getQuantity() < order.getQuantity()){
                                System.out.println("There are not enough shares to sell. Selling partial shares");
                                quantity = buyOrder.getQuantity();
                            }else{
                                quantity = Math.min(buyOrder.getQuantity(), order.getQuantity());
                                isOrderRemaining = true;
                            }
                            var history = new Trade(buyOrder.getId(), order.getId(), buyOrder.getPrice(), quantity);
                            System.out.println("sell trade executed: " + history);

                            tradeHistory.add(history);

                            if(isOrderRemaining){
                                order.setQuantity(order.getQuantity() - quantity);
                                buyOrder.setQuantity(buyOrder.getQuantity() - quantity);

                                if(order.getQuantity() == 0 ){
                                    break;
                                }

                                System.out.println("order quantity updated. Looking for next order in queue...");
                            }
                        }else{
                                buyQueue.poll();
                                System.out.println("buy order removed from queue");
                            }

                    }

                    if(order.getQuantity() > 0) {
                        System.out.println("Sell order added to queue: " + order.getId());
                        sellQueue.add(order);
                    }
                }
                break;
        }
        
    }

    List<Trade> getTradeHistory() {
        return this.tradeHistory;
    }



}