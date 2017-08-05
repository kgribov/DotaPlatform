package org.dota.steam.service;

import org.dota.model.InventoryItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        DotaInventoryItemsService.class
})
@TestPropertySource(properties = "steam.api.dev_key=test_dev_key")
public class DotaInventoryItemsServiceTest {

    @Autowired
    private DotaInventoryItemsService itemsService;

    @Test
    public void testNoSuchItem() {
        Integer nonExistId = 1_000_000;
        Optional<InventoryItem> actual = itemsService.getItemById(nonExistId);

        Optional<InventoryItem> expected = Optional.empty();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testGetDagonItemById() {
        Integer dagonId = 104;
        Optional<InventoryItem> dagon = itemsService.getItemById(dagonId);

        Optional<InventoryItem> expectedItem = Optional.of(new InventoryItem(
                dagonId,
                "Dagon",
                2720,
                false,
                false,
                false
        ));

        assertThat(dagon, equalTo(expectedItem));
    }

    @Test
    public void testGetAllItems() {
        List<InventoryItem> items = itemsService.getItems();

        Integer expectedNumberOfItems = 259;

        assertThat(items.size(), equalTo(expectedNumberOfItems));
    }
}
