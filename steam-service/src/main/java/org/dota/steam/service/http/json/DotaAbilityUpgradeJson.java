package org.dota.steam.service.http.json;

import lombok.Value;

@Value
public class DotaAbilityUpgradeJson {

    private final Integer ability;
    private final Integer time;
    private final Integer level;
}
