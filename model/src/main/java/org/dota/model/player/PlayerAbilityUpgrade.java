package org.dota.model.player;

import lombok.Value;

@Value
public class PlayerAbilityUpgrade {

    private final Integer ability;
    private final Integer time;
    private final Integer level;
}
