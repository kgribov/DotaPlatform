package org.dota.model;

import lombok.Value;

@Value
public class InventoryItem {

    private final Integer id;
    private final String name;
    private final Integer cost;
    private final Boolean availableInSecretShop;
    private final Boolean availableInSideShop;
    private final Boolean recipe;
}
