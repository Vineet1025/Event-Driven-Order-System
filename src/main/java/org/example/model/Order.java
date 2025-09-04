package org.example.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class Order {
    private String orderId;
    private String customerId;
    private List<OrderItem> items = new ArrayList<>();
    private double totalAmount;
    private OrderStatus status = OrderStatus.PENDING;
    private List<String> eventHistory = new ArrayList<>();


    public Order() {}
    public Order(String orderId, String customerId, List<OrderItem> items, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        if (items != null) this.items.addAll(items);
        this.totalAmount = totalAmount;
    }


    public void addHistory(String note) {
        eventHistory.add(Instant.now().toString() + " : " + note);
    }


    // Getters & Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<String> getEventHistory() { return eventHistory; }
    public void setEventHistory(List<String> eventHistory) { this.eventHistory = eventHistory; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", items=" + items +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", eventHistory=" + eventHistory +
                '}';
    }
}
