package org.dota.model.player;

import org.dota.model.Hero;
import org.dota.model.InventoryItem;
import org.dota.model.LeaverStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Player {

    private final Long accountId;

    private final Boolean direTeam;

    private final Hero hero;

    private final List<InventoryItem> inventoryItems;

    private final PlayerKDA playerKDA;

    private final LeaverStatus leaverStatus;

    private final PlayerEconomy economy;

    private final PlayerFarming farming;

    private final PlayerDamage damage;

    private final Integer healing;

    private final Integer level;

    private final List<PlayerAbilityUpgrade> abilities;
}
