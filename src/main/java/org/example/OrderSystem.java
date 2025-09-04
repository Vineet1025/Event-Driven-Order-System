package org.example;

import org.example.events.Event;
import org.example.observers.AlertObserver;
import org.example.observers.LoggerObserver;
import org.example.processing.EventIngestor;
import org.example.processing.EventProcessor;


public class OrderSystem {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java ... com.example.ordersystem.Application <events.jsonl>\n" +
                    "Each line should be a JSON object with an 'eventType' field.");
            return;
        }


        String eventsFile = args[0];
        EventIngestor ingestor = new EventIngestor(eventsFile);
        EventProcessor processor = new EventProcessor();
        processor.registerObserver(new LoggerObserver());
        processor.registerObserver(new AlertObserver());


        for (Event raw : ingestor) {
            if (raw == null) continue;
            processor.process(raw);
        }


        System.out.println("\n=== FINAL ORDERS STATE ===");
        processor.allOrders().forEach(System.out::println);



    }
}