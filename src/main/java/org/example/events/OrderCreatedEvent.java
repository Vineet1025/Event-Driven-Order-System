package org.example.events;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.example.model.OrderItem;
import java.util.List;


@JsonTypeName("OrderCreated")
public class OrderCreatedEvent extends Event {
    public String orderId;
    public String customerId;
    public List<OrderItem> items;
    public double totalAmount;
}