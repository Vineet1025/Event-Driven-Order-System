package org.example.observers;

import org.example.events.Event;
import org.example.model.Order;
import org.example.model.OrderStatus;

public class LoggerObserver implements EventObserver {
    @Override public void onEventProcessed(Event e, Order o) {
        System.out.println("[Logger] Event processed: " + e + " -> Order status: " + (o == null ? "(new/none)" : o.getStatus()));
    }
    @Override public void onStatusChanged(Order o, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("[Logger] Order " + o.getOrderId() + " status changed: " + oldStatus + " -> " + newStatus);
    }
}