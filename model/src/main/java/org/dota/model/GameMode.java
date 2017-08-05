package org.dota.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum  GameMode {

    NONE(0),
    ALL_PICK(1),
    CAPTAINS_MODE(2),
    RANDOM_DRAFT(3),
    SINGLE_DRAFT(4),
    ALL_RANDOM(5),
    INTRO(6),
    DIRETIDE(7),
    REVERSE_CAPTAINS_MODE(8),
    THE_GREEVILING(9),
    TUTORIAL(10),
    MID_ONLY(11),
    LEAST_PLAYED(12),
    NEW_PLAYER_POOL(13),
    COMPENDIUM_MATCHMAKING(14),
    CO_OP_VS_BOTS(15),
    CAPTAINS_DRAFT(16),
    ABILITY_DRAFT(17),
    ALL_RANDOM_DEATHMATCH(18),
    ONE_VS_ONE_MID_ONLY(19),
    RANKED_MATCHMAKING(20);

    private static final Map<Integer, GameMode> gameModeToId = Arrays
            .stream(GameMode.values())
            .collect(Collectors.toMap(GameMode::getId, Function.identity()));

    private final Integer id;

    GameMode(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static GameMode fromId(Integer id) {
        return gameModeToId.get(id);
    }
}
