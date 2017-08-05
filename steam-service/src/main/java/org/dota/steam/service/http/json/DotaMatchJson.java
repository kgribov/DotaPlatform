package org.dota.steam.service.http.json;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonRootName("result")
public class DotaMatchJson {

    private final Long matchId;
    private final Boolean radiantWin;
    private final Integer duration;
    private final Integer preGameDuration;
    private final Long startTime;
    private final Long matchSeqNum;
    private final Integer towerStatusRadiant;
    private final Integer towerStatusDire;
    private final Integer barracksStatusRadiant;
    private final Integer barracksStatusDire;
    private final Integer cluster;
    private final Integer firstBloodTime;
    private final Integer lobbyType;
    private final Integer humanPlayers;
    private final Integer leagueid;
    private final Integer positiveVotes;
    private final Integer negativeVotes;
    private final Integer gameMode;
    private final Integer flags;
    private final Integer engine;
    private final Integer radiant_score;
    private final Integer dire_score;
    private final List<DotaPlayerDetailsJson> players;
}
