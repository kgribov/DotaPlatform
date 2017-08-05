package org.dota.model.buildings;

import lombok.Value;

@Value
public class TowersTierStatus {

    private final Boolean bottomExists;
    private final Boolean middleExists;
    private final Boolean topExists;
}
