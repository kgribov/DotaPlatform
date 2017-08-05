package org.dota.model.buildings;

import lombok.Value;

@Value
public class BuildingsStatus {

    private final TowersStatus towersStatus;
    private final BarracksStatus barracksStatus;
    private final ShrinesStatus shrinesStatus;
}
