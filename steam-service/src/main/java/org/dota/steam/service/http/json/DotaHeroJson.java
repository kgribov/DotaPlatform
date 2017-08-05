package org.dota.steam.service.http.json;

import lombok.Value;

@Value
public class DotaHeroJson {

    private final Integer id;
    private final String name;
    private final String localizedName;
}
