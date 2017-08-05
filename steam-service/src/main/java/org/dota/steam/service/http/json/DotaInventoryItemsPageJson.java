package org.dota.steam.service.http.json;

import lombok.Value;

import java.util.List;

@Value
public class DotaInventoryItemsPageJson {

    private List<DotaInventoryItemJson> items;
}
