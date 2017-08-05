package org.dota.model.buildings;

import lombok.Value;

@Value
public class BarracksSideStatus {

    private final Boolean rangedExists;
    private final Boolean meleeExists;
}
