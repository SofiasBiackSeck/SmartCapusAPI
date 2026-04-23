/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.exception;



/**
 * Custom exception thrown when a sensor cannot accept new readings.
 * This typically occurs when the sensor is in MAINTENANCE or OFFLINE state.
 */
public class SensorUnavailableException extends RuntimeException {

    // Default constructor
    public SensorUnavailableException() {
        super("Sensor is unavailable");
    }

    // Constructor with custom message
    public SensorUnavailableException(String message) {
        super(message);
    }

    // Constructor with message and cause (optional, but good practice)
    public SensorUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}