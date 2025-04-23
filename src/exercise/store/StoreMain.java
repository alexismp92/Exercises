package src.exercise.store;

/*
Some stores operate a BPS (Bulk Product Shipment) system that manually matches requisition orders with available pooling agents based on priority and availability.
You’ve been asked to design a system to simulate the manual pooling process, where requisitions are batched and assigned to available stores/agents based on matching criteria.
Implement a system that:
Accepts a stream of Requisitions and Stores.
Pools (matches) requisitions to stores based on:
Requisition priority (higher first),
Product type compatibility,
Store capacity.
Returns a list of matched pairs: (Requisition ID, Store ID).
*/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

class Requisition implements Comparable<Requisition> {
    private String id;
    private String productType;
    int quantity;
    private int priority; // 1 (lowest) to 10 (highest)

    public Requisition(String id, String productType, int quantity, int priority) {
        this.id = id;
        this.productType = productType;
        this.quantity = quantity;
        this.priority = priority;
    }

    @Override
    public int compareTo(Requisition requisition) {
        return Integer.compare(requisition.priority, this.priority);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Requisition{");
        sb.append("id='").append(id).append('\'');
        sb.append(", productType='").append(productType).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }
}

class Store {
    private String id;
    private Set<String> supportedProductTypes;
    private int availableCapacity;

    public Store(String id, Set<String> supportedProductTypes, int availableCapacity) {
        this.id = id;
        this.supportedProductTypes = supportedProductTypes;
        this.availableCapacity = availableCapacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getSupportedProductTypes() {
        return supportedProductTypes;
    }

    public void setSupportedProductTypes(Set<String> supportedProductTypes) {
        this.supportedProductTypes = supportedProductTypes;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Store{");
        sb.append("id='").append(id).append('\'');
        sb.append(", supportedProductTypes=").append(supportedProductTypes);
        sb.append(", availableCapacity=").append(availableCapacity);
        sb.append('}');
        return sb.toString();
    }
}

class MatchResult {
    private String requisitionId;
    private String storeId;

    public MatchResult(String requisitionId, String storeId) {
        this.requisitionId = requisitionId;
        this.storeId = storeId;
    }

    public String getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(String requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MatchResult{");
        sb.append("requisitionId='").append(requisitionId).append('\'');
        sb.append(", storeId='").append(storeId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

public class StoreMain {

    public static void main(String[] args){

        List<Requisition> requisitions = new ArrayList<>();
        requisitions.add(new Requisition("R1", "Electronics", 10, 8));
        requisitions.add(new Requisition("R2", "Clothing", 5, 9));
        requisitions.add(new Requisition("R3", "Electronics", 7, 8));


        // Sort requisitions by priority (the highest first)
        requisitions.sort(Comparator.naturalOrder());

        System.out.println("Sorted requisitions by priority: " + requisitions);

        List<Store> stores = List.of(
                new Store("S1", Set.of("Electronics"), 15),
                new Store("S2", Set.of("Clothing", "Electronics"), 10)
        );


        StoreMain storeMain = new StoreMain();
        var result = storeMain.poolRequisitions(requisitions, stores);
        System.out.println("Matched results: " + result);
    }


    List<MatchResult> poolRequisitions(List<Requisition> requisitions, List<Store> stores){

        List<MatchResult> matchedResults = new ArrayList<>();

        if(requisitions.isEmpty() || stores.isEmpty()){
            System.out.println("No requisitions or stores available for matching.");
            return matchedResults;
        }


        for (var requisition : requisitions){
            boolean isRequisitionMatched = false;

            for (var store : stores){
                if (store.getSupportedProductTypes().contains(requisition.getProductType()) &&
                    store.getAvailableCapacity() >= requisition.getQuantity()){

                    System.out.println("Matched Requisition: " + requisition.getId() + " in store: " + store.getId());
                    store.setAvailableCapacity(store.getAvailableCapacity() - requisition.getQuantity());
                    matchedResults.add(new MatchResult(requisition.getId(), store.getId()));
                    isRequisitionMatched = true;
                    break;
                }
            }

            if(!isRequisitionMatched){
                System.out.println("No store available for Requisition: " + requisition.getId());
            } else {
                isRequisitionMatched = false;
            }

        }

        return matchedResults;
    }



}
