package org.dota.model.player;

import lombok.Value;

@Value
public class PlayerKDA {

    private final Integer kills;
    private final Integer deaths;
    private final Integer assists;
}
