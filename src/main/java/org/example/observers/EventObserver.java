package org.example.observers;


import org.example.events.Event;
import org.example.model.Order;
import org.example.model.OrderStatus;

public interface EventObserver {
    default void onEventProcessed(Event e, Order o) {}
    default void onStatusChanged(Order o, OrderStatus oldStatus, OrderStatus newStatus) {}
}
