package org.dota.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum  LeaverStatus {

    NONE(0),
    DISCONNECTED(1),
    DISCONNECTED_TOO_LONG(2),
    ABANDONDED(3),
    AFK(4),
    NEVER_CONNECTED(5),
    NEVER_CONNECTED_TOO_LONG(6);

    private static final Map<Integer, LeaverStatus> leaverStatusToId = Arrays
            .stream(LeaverStatus.values())
            .collect(Collectors.toMap(LeaverStatus::getId, Function.identity()));

    private final Integer id;

    LeaverStatus(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static LeaverStatus fromId(Integer id) {
        return leaverStatusToId.get(id);
    }
}
