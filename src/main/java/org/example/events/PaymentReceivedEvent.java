package org.example.events;

import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("PaymentReceived")
public class PaymentReceivedEvent extends Event {
    public String orderId;
    public double amountPaid;
}
