package org.example.events;

import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("OrderCancelled")
public class OrderCancelledEvent extends Event {
    public String orderId;
    public String reason;
}