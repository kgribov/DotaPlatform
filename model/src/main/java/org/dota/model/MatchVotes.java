package org.dota.model;

import lombok.Value;

@Value
public class MatchVotes {

    private final Integer positiveVotes;
    private final Integer negativeVotes;
}
