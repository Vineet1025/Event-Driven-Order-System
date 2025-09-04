package org.example.processing;


import org.example.events.*;
import org.example.model.*;
import org.example.observers.EventObserver;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventProcessor {
    private final Map<String, Order> orders = new HashMap<>();
    private final List<EventObserver> observers = new CopyOnWriteArrayList<>();
    private final Payment payment = new Payment();


    public void registerObserver(EventObserver obs) { observers.add(obs); }
    public Optional<Order> findOrder(String orderId) { return Optional.ofNullable(orders.get(orderId)); }
    public Collection<Order> allOrders() { return orders.values(); }


    public void process(Event e) {
        Order affected = null;
        try {
            if (e instanceof OrderCreatedEvent oce) {
                affected = handle(oce);
            } else if (e instanceof PaymentReceivedEvent pre) {
                affected = handle(pre);
            } else if (e instanceof ShippingScheduledEvent sse) {
                affected = handle(sse);
            } else if (e instanceof OrderCancelledEvent oce2) {
                affected = handle(oce2);
            } else {
                System.err.println("[WARN] Unsupported event type: " + e.eventType);
            }
        } finally {
            for (EventObserver ob : observers) ob.onEventProcessed(e, affected);
        }
    }

    private Order handle(OrderCreatedEvent e) {
        if (orders.containsKey(e.orderId)) {
            System.err.println("[WARN] Order " + e.orderId + " already exists. Ignoring duplicate creation.");
            return orders.get(e.orderId);
        }
        Order o = new Order(e.orderId, e.customerId, e.items, e.totalAmount);
        o.setStatus(OrderStatus.PENDING);
        o.addHistory("Order created (event " + e.eventId + ")");
        orders.put(o.getOrderId(), o);
        return o;
    }


    private Order handle(PaymentReceivedEvent e) {
        Order o = requireOrder(e.orderId);
        if (o == null) return null;
        OrderStatus prev = o.getStatus();
        o.addHistory("Payment received: " + e.amountPaid + " (event " + e.eventId + ")");
        payment.add(e.orderId, e.amountPaid);
        double paid = payment.total(e.orderId);
        if (paid >= o.getTotalAmount()) {
            o.setStatus(OrderStatus.PAID);
        } else if (paid > 0) {
            o.setStatus(OrderStatus.PARTIALLY_PAID);
        }
        if (prev != o.getStatus()) notifyStatusChanged(o, prev, o.getStatus());
        return o;
    }

    private Order handle(ShippingScheduledEvent e) {
        Order o = requireOrder(e.orderId);
        if (o == null) return null;
        OrderStatus prev = o.getStatus();
        o.addHistory("Shipping scheduled on " + e.shippingDate + " (event " + e.eventId + ")");
        o.setStatus(OrderStatus.SHIPPED);
        if (prev != o.getStatus()) notifyStatusChanged(o, prev, o.getStatus());
        return o;
    }


    private Order handle(OrderCancelledEvent e) {
        Order o = requireOrder(e.orderId);
        if (o == null) return null;
        OrderStatus prev = o.getStatus();
        o.addHistory("Order cancelled: " + e.reason + " (event " + e.eventId + ")");
        o.setStatus(OrderStatus.CANCELLED);
        if (prev != o.getStatus()) notifyStatusChanged(o, prev, o.getStatus());
        return o;
    }

    private void notifyStatusChanged(Order o, OrderStatus oldS, OrderStatus newS) {
        for (EventObserver ob : observers) ob.onStatusChanged(o, oldS, newS);
    }


    private Order requireOrder(String orderId) {
        Order o = orders.get(orderId);
        if (o == null) {
            System.err.println("[WARN] Event references unknown orderId " + orderId + "); ignoring.");
        }
        return o;
    }

}

// hatchling (marker as per instructions)
