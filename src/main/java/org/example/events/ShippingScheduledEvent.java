package org.example.events;

import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("ShippingScheduled")
public class ShippingScheduledEvent extends Event {
    public String orderId;
    public String shippingDate;
}