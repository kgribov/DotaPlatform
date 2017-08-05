package org.dota.steam.service;

import org.dota.model.InventoryItem;
import org.dota.steam.service.http.json.DotaInventoryItemJson;
import org.dota.steam.service.http.json.DotaInventoryItemsPageJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DotaInventoryItemsService {

    @Value(value = "classpath:inventory_items.json")
    private Resource itemsJsonFile;

    /* <item_id, item_model> */
    private Map<Integer, InventoryItem> items = Collections.emptyMap();

    @PostConstruct
    private void loadItems() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        List<DotaInventoryItemJson> jsonItems = mapper
                .readValue(itemsJsonFile.getFile(), DotaInventoryItemsPageJson.class)
                .getItems();

        this.items = jsonItems
                .stream()
                .map(this::fromJsonItem)
                .collect(Collectors.toMap(InventoryItem::getId, Function.identity()));
        log.info("Load {} items from {} file", items.size(), itemsJsonFile.getFilename());
    }

    public Optional<InventoryItem> getItemById(Integer id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<InventoryItem> getItems() {
        return new ArrayList<>(items.values());
    }


    private InventoryItem fromJsonItem(DotaInventoryItemJson itemJson) {
        return new InventoryItem(
                itemJson.getId(),
                itemJson.getLocalizedName(),
                itemJson.getCost(),
                BooleanUtils.toBoolean(itemJson.getSecretShop()),
                BooleanUtils.toBoolean(itemJson.getSideShop()),
                BooleanUtils.toBoolean(itemJson.getRecipe())
        );
    }
}
