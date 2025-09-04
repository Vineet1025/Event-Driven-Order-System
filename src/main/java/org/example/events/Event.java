package org.example.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "eventType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderCreatedEvent.class, name = "OrderCreated"),
        @JsonSubTypes.Type(value = PaymentReceivedEvent.class, name = "PaymentReceived"),
        @JsonSubTypes.Type(value = ShippingScheduledEvent.class, name = "ShippingScheduled"),
        @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "OrderCancelled")
})
public abstract class Event {
    public String eventId;
    public String timestamp;
    public EventType eventType;


    public Instant time() {
        try { return Instant.parse(timestamp); } catch (Exception e) { return Instant.now(); }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(#" + eventId + ") @ " + timestamp;
    }

}