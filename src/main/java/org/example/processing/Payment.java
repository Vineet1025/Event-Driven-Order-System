package org.example.processing;

import java.util.HashMap;
import java.util.Map;


public class Payment {
    private final Map<String, Double> paid = new HashMap<>();
    public void add(String orderId, double amount) { paid.merge(orderId, amount, Double::sum); }
    public double total(String orderId) { return paid.getOrDefault(orderId, 0.0); }
}