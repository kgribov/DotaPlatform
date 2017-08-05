package org.dota.model.buildings;

import lombok.Value;

@Value
public class TowersStatus {

    private final TowersTierStatus tier1;
    private final TowersTierStatus tier2;
    private final TowersTierStatus tier3;
}
