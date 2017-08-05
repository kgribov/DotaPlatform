package org.dota.steam.service.http.json;

import lombok.Value;

import java.util.List;

@Value
public class DotaHeroesPageJson {

    private List<DotaHeroJson> heroes;
}
