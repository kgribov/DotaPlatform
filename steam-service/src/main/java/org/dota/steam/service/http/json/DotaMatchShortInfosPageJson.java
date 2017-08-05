package org.dota.steam.service.http.json;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Value;

import java.util.List;

@Value
@JsonRootName("result")
public class DotaMatchShortInfosPageJson {

    private final Integer status;
    private final Integer numResults;
    private final Integer totalResults;
    private final Integer resultsRemaining;
    private final List<DotaShortMatchInfoJson> matches;
}
