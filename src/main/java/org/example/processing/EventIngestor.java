package org.example.processing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.events.Event;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class EventIngestor implements Iterable<Event> {
    private final String filePath;
    private final ObjectMapper mapper;


    public EventIngestor(String filePath) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Override
    public Iterator<Event> iterator() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            return new Iterator<>() {
                String nextLine = advance();
                private String advance() {
                    try { return br.readLine(); } catch (IOException e) { return null; }
                }
                @Override public boolean hasNext() { return nextLine != null; }
                @Override public Event next() {
                    String line = nextLine;
                    nextLine = advance();
                    if (line == null) throw new NoSuchElementException();
                    try {
                        return mapper.readValue(line, Event.class);
                    } catch (JsonProcessingException ex) {
                        System.err.println("[WARN] Failed to parse line as Event: " + line + "\n -> " + ex.getMessage());
                        return null; // caller should skip nulls
                    }
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Failed to open events file: " + filePath, e);
        }
    }
}