package org.dota.steam.service.http.json;

import lombok.Value;

@Value
public class DotaInventoryItemJson {

    private final Integer id;
    private final String name;
    private final Integer cost;
    private final Integer secretShop;
    private final Integer sideShop;
    private final Integer recipe;
    private final String localizedName;
}
