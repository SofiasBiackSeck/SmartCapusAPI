package com.mycompany.smartcampusapi.store;



import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.SensorReading;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryStore {

    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    private InMemoryStore() {
    }

    public static List<SensorReading> getOrCreateReadingList(String sensorId) {
        return readings.computeIfAbsent(sensorId, key -> new CopyOnWriteArrayList<>());
    }
}
