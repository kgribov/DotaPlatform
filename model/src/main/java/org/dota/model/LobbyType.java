package org.dota.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum LobbyType {

    INVALID(-1),
    PUBLIC_MATCHMAKING(0),
    PRACTISE(1),
    TOURNAMENT(2),
    TUTORIAL(3),
    CO_OP_WITH_BOTS(4),
    TEAM_MATCH(5),
    SOLO_QUEUE(6),
    RANKED_MATCHMAKING(7),
    SOLO_MID(8);

    private static final Map<Integer, LobbyType> lobbyToId = Arrays
            .stream(LobbyType.values())
            .collect(Collectors.toMap(LobbyType::getId, Function.identity()));

    private final Integer id;

    LobbyType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static LobbyType fromId(Integer id) {
        return lobbyToId.get(id);
    }
}
