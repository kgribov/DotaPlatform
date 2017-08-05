package org.dota.model.player;

import lombok.Value;

@Value
public class PlayerEconomy {

    private final Integer gold;
    private final Integer goldPerMin;
    private final Integer goldSpent;
}
