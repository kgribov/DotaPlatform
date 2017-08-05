package org.dota.steam.service.http.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@Value
@JsonIgnoreProperties({
        "additional_units",
        "scaled_hero_damage",
        "scaled_tower_damage",
        "scaled_hero_healing"
})
public class DotaPlayerDetailsJson {

    private final Long accountId;
    private final Integer playerSlot;
    private final Integer heroId;

    private final Integer kills;
    private final Integer deaths;
    private final Integer assists;

    private final Integer leaverStatus;

    private final Integer lastHits;
    private final Integer denies;

    private final Integer gold;
    private final Integer goldSpent;
    private final Integer goldPerMin;
    private final Integer xpPerMin;

    private final Integer level;

    private final Integer item_0;
    private final Integer item_1;
    private final Integer item_2;
    private final Integer item_3;
    private final Integer item_4;
    private final Integer item_5;

    private final Integer backpack_0;
    private final Integer backpack_1;
    private final Integer backpack_2;

    private final Integer heroDamage;
    private final Integer heroHealing;
    private final Integer towerDamage;

    private final List<DotaAbilityUpgradeJson> abilityUpgrades;
}
