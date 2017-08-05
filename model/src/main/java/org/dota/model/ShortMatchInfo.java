package org.dota.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Value
public class ShortMatchInfo {

    private final Long matchId;
    private final Long matchOffset;
    private final LocalDateTime startTime;
    private final List<Long> direPlayers;
    private final List<Long> radiantPlayers;
}
