package org.dota.steam.service.http.json;

import lombok.Value;

import java.util.List;

@Value
public class DotaShortMatchInfoJson {

    private final Long matchId;
    private final Long matchSeqNum;
    private final Long startTime;
    private final Integer lobbyType;
    private final Integer radiantTeamId;
    private final Integer direTeamId;
    private final List<DotaPlayerJson> players;
}
