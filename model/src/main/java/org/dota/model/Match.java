package org.dota.model;

import org.dota.model.buildings.BuildingsStatus;
import org.dota.model.player.Player;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Match {

    private final Long matchId;
    private final Long matchOffset;

    private final Boolean radiantWin;

    private final LocalDateTime startTime;

    private final Integer durationInSeconds;
    private final Integer firstBloodTimeInSeconds;

    private final BuildingsStatus radiantBuildingsStatus;
    private final BuildingsStatus direBuildingsStatus;

    private final Integer clusterId;

    private final LobbyType lobbyType;

    private final Integer humanPlayers;

    private final Integer leagueId;

    private final MatchVotes votes;

    private final GameMode gameMode;

    private final MatchScores scores;

    private final List<Hero> heroesBans;
    private final List<Hero> heroesPicks;

    private final List<Player> players;
}
