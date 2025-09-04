package org.example.observers;

import org.example.model.Order;
import org.example.model.OrderStatus;

public class AlertObserver implements EventObserver {
    @Override public void onStatusChanged(Order o, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("[ALERT] Sending alert for Order " + o.getOrderId() + ": Status changed to " + newStatus);
    }
}