package org.dota.model.buildings;

import lombok.Value;

@Value
public class BarracksStatus {

    private final BarracksSideStatus bottom;
    private final BarracksSideStatus middle;
    private final BarracksSideStatus top;
}
