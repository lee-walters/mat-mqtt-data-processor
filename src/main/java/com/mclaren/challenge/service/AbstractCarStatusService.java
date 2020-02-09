package com.mclaren.challenge.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class AbstractCarStatusService {

    LinkedHashMap<Integer, Double> distanceTravelledTracker;

    public AbstractCarStatusService() {
        this.distanceTravelledTracker = new LinkedHashMap<>();
    }
}
