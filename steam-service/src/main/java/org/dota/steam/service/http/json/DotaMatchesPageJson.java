package org.dota.steam.service.http.json;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Value;

import java.util.List;

@Value
@JsonRootName("result")
public class DotaMatchesPageJson {

    private final Integer status;
    private final List<DotaMatchJson> matches;
}
