package org.dota.steam.service;

import org.dota.steam.service.http.client.SteamClient;
import org.dota.steam.service.http.json.*;
import org.dota.steam.service.rx.OffsetDataPusher;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.dota.model.*;
import org.dota.model.buildings.*;
import org.dota.model.player.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DotaMatchesService {

    @Autowired
    private DotaHeroesService heroesService;

    @Autowired
    private DotaInventoryItemsService itemsService;

    @Autowired
    private SteamClient steamClient;

    @Value("${steam.api.dev_key}")
    private String devKey;

    public Flowable<Match> loadMatchesFromOffset(Long offset) {
        return Flowable.create(
                new OffsetDataPusher<>(
                        this::loadMatchDetailsByOffsetFromClient,
                        this::getLastOffsetOfMatchDetails,
                        offset
                ),
                BackpressureStrategy.BUFFER
        ).map(this::convertToMatchModel);
    }

    public Flowable<ShortMatchInfo> loadMatchesShortInfoFromOffset(Long offset) {
        return Flowable.create(
                new OffsetDataPusher<>(
                        this::loadMatchesByOffsetFromClient,
                        this::getLastOffsetOfMatches,
                        offset
                ),
                BackpressureStrategy.BUFFER
        ).map(this::convertToMatchShortInfo);
    }

    public Match loadMatchById(Long matchId) {
        return convertToMatchModel(steamClient.fetchDotaMatch(devKey, matchId));
    }

    private List<DotaMatchJson> loadMatchDetailsByOffsetFromClient(Long offset) {
        return steamClient.fetchDotaMatches(devKey, offset).getMatches();
    }

    private List<DotaShortMatchInfoJson> loadMatchesByOffsetFromClient(Long offset) {
        return steamClient.fetchDotaMatchShortInfos(devKey, offset).getMatches();
    }

    private Long getLastOffsetOfMatchDetails(List<DotaMatchJson> matches) {
        return matches
                .stream()
                .map(DotaMatchJson::getMatchSeqNum)
                .max(Long::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Unable to get offset from empty match's list"));
    }

    private Long getLastOffsetOfMatches(List<DotaShortMatchInfoJson> shortMatchInfos) {
        return shortMatchInfos
                .stream()
                .map(DotaShortMatchInfoJson::getMatchSeqNum)
                .max(Long::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Unable to get offset from empty shortMatchInfo list"));
    }

    private ShortMatchInfo convertToMatchShortInfo(DotaShortMatchInfoJson matchJson) {
        List<Long> direPlayersIds = filterPlayersByTeam(matchJson.getPlayers(), true);
        List<Long> radiant = filterPlayersByTeam(matchJson.getPlayers(), false);
        return ShortMatchInfo
                .builder()
                .matchId(matchJson.getMatchId())
                .matchOffset(matchJson.getMatchSeqNum())
                .startTime(timeFromEpoch(matchJson.getStartTime()))
                .direPlayers(direPlayersIds)
                .radiantPlayers(radiant)
                .build();
    }

    private Match convertToMatchModel(DotaMatchJson matchJson) {
        return Match
                .builder()
                .matchId(matchJson.getMatchId())
                .matchOffset(matchJson.getMatchSeqNum())
                .radiantWin(matchJson.getRadiantWin())
                .startTime(timeFromEpoch(matchJson.getStartTime()))
                .durationInSeconds(matchJson.getDuration())
                .firstBloodTimeInSeconds(matchJson.getFirstBloodTime())
                .radiantBuildingsStatus(parseBuildingStatus(
                        matchJson.getBarracksStatusRadiant(),
                        matchJson.getTowerStatusRadiant()
                ))
                .direBuildingsStatus(parseBuildingStatus(
                        matchJson.getBarracksStatusDire(),
                        matchJson.getTowerStatusDire()
                ))
                .clusterId(matchJson.getCluster())
                .lobbyType(LobbyType.fromId(matchJson.getLobbyType()))
                .humanPlayers(matchJson.getHumanPlayers())
                .leagueId(matchJson.getLeagueid())
                .gameMode(GameMode.fromId(matchJson.getGameMode()))
                .scores(new MatchScores(
                        matchJson.getRadiant_score(),
                        matchJson.getDire_score())
                )
                .heroesBans(null)
                .heroesPicks(null)
                .players(matchJson
                        .getPlayers()
                        .stream()
                        .map(this::parsePlayer)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private List<Long> filterPlayersByTeam(List<DotaPlayerJson> players, boolean isDire) {
        return players
                .stream()
                .filter(player -> isDire && isDireTeam(player.getPlayerSlot()))
                .map(DotaPlayerJson::getAccountId)
                .collect(Collectors.toList());
    }

    private LocalDateTime timeFromEpoch(Long epochTime) {
        return LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.UTC);
    }


    /**
     * @param playerSlot
     *    ┌─────────────── Team (false if Radiant, true if Dire).
     * │ ┌─┬─┬─┬─────── Not used.
     * │ │ │ │ │ ┌─┬─┬─ The position of a player within their team (0-4).
     * │ │ │ │ │ │ │ │
     * 0 0 0 0 0 0 0 0
     * @return
     */
    private boolean isDireTeam(Integer playerSlot) {
        return checkBitPosition(playerSlot,7);
    }

    private Player parsePlayer(DotaPlayerDetailsJson playerJson) {
        return Player
                .builder()
                .accountId(playerJson.getAccountId())
                .direTeam(isDireTeam(playerJson.getPlayerSlot()))
                .hero(heroesService.getHeroById(playerJson.getHeroId()).get())
                .inventoryItems(playerItems(playerJson)
                        .stream()
                        .filter(item -> item != 0)
                        .map(itemsService::getItemById)
                        // TODO
                        .map(item -> item.get())
                        .collect(Collectors.toList())
                )
                .level(playerJson.getLevel())
                .playerKDA(new PlayerKDA(
                        playerJson.getKills(),
                        playerJson.getDeaths(),
                        playerJson.getAssists()
                ))
                .leaverStatus(LeaverStatus.fromId(playerJson.getLeaverStatus()))
                .economy(new PlayerEconomy(
                        playerJson.getGold(),
                        playerJson.getGoldPerMin(),
                        playerJson.getGoldSpent()
                ))
                .farming(new PlayerFarming(
                        playerJson.getLastHits(),
                        playerJson.getDenies(),
                        playerJson.getXpPerMin()
                ))
                .damage(new PlayerDamage(
                        playerJson.getHeroDamage(),
                        playerJson.getTowerDamage()
                ))
                .healing(playerJson.getHeroHealing())
                .level(playerJson.getLevel())
                .abilities(
                        Optional.ofNullable(playerJson.getAbilityUpgrades())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(this::parseAbility)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private List<Integer> playerItems(DotaPlayerDetailsJson playerJson) {
        return Arrays.asList(
            playerJson.getItem_0(),
            playerJson.getItem_1(),
            playerJson.getItem_2(),
            playerJson.getItem_3(),
            playerJson.getItem_4(),
            playerJson.getItem_5()
        );
    }


    /**
     * @param barracks
     *   ┌─┬───────────── Not used.
     * │ │ ┌─────────── Bottom Ranged
     * │ │ │ ┌───────── Bottom Melee
     * │ │ │ │ ┌─────── Middle Ranged
     * │ │ │ │ │ ┌───── Middle Melee
     * │ │ │ │ │ │ ┌─── Top Ranged
     * │ │ │ │ │ │ │ ┌─ Top Melee
     * │ │ │ │ │ │ │ │
     * 0 0 0 0 0 0 0 0
     *
     *  @param towers
     *     ┌─┬─┬─┬─┬─────────────────────── Not used.
     * │ │ │ │ │ ┌───────────────────── Ancient Bottom
     * │ │ │ │ │ │ ┌─────────────────── Ancient Top
     * │ │ │ │ │ │ │ ┌───────────────── Bottom Tier 3
     * │ │ │ │ │ │ │ │ ┌─────────────── Bottom Tier 2
     * │ │ │ │ │ │ │ │ │ ┌───────────── Bottom Tier 1
     * │ │ │ │ │ │ │ │ │ │ ┌─────────── Middle Tier 3
     * │ │ │ │ │ │ │ │ │ │ │ ┌───────── Middle Tier 2
     * │ │ │ │ │ │ │ │ │ │ │ │ ┌─────── Middle Tier 1
     * │ │ │ │ │ │ │ │ │ │ │ │ │ ┌───── Top Tier 3
     * │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─── Top Tier 2
     * │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─ Top Tier 1
     * │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │
     * 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     *
     * @return BuildingStatus
     */
    private BuildingsStatus parseBuildingStatus(Integer barracks, Integer towers) {
        return new BuildingsStatus(
                new TowersStatus(
                        new TowersTierStatus(
                                checkBitPosition(towers, 6),
                                checkBitPosition(towers, 3),
                                checkBitPosition(towers, 0)
                        ),
                        new TowersTierStatus(
                                checkBitPosition(towers, 7),
                                checkBitPosition(towers, 4),
                                checkBitPosition(towers, 1)
                        ),
                        new TowersTierStatus(
                                checkBitPosition(towers, 8),
                                checkBitPosition(towers, 5),
                                checkBitPosition(towers, 2)
                        )
                ),
                new BarracksStatus(
                        new BarracksSideStatus(
                                checkBitPosition(barracks, 5),
                                checkBitPosition(barracks, 4)
                        ),
                        new BarracksSideStatus(
                                checkBitPosition(barracks, 3),
                                checkBitPosition(barracks, 2)
                        ),
                        new BarracksSideStatus(
                                checkBitPosition(barracks, 1),
                                checkBitPosition(barracks, 0)
                        )
                ),
                new ShrinesStatus(
                        checkBitPosition(towers, 13),
                        checkBitPosition(towers, 14)
                )
        );
    }

    private PlayerAbilityUpgrade parseAbility(DotaAbilityUpgradeJson upgradeJson) {
        return new PlayerAbilityUpgrade(
                upgradeJson.getAbility(),
                upgradeJson.getTime(),
                upgradeJson.getLevel()
        );
    }

    private boolean checkBitPosition(Integer num, Integer bitPos) {
        return BigInteger.valueOf(num).testBit(bitPos);
    }
}
