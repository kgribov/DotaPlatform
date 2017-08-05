package org.dota.model.player;

import lombok.Value;

@Value
public class PlayerFarming {

    private final Integer lastHits;
    private final Integer denies;
    private final Integer xpPerMin;
}
