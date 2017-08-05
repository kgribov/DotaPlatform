package org.dota.steam.service.rx;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class OffsetDataPusher<T> implements FlowableOnSubscribe<T> {

    private final Function<Long, List<T>> loadItemsByOffset;
    private final Function<List<T>, Long> getNextOffset;

    private Long currentOffset;

    private int currentMatchIndex = 0;
    private List<T> currentItems = Collections.emptyList();

    public OffsetDataPusher(Function<Long, List<T>> loadItemsByOffset,
                            Function<List<T>, Long> getNextOffset,
                            Long offset) {

        this.loadItemsByOffset = loadItemsByOffset;
        this.getNextOffset = getNextOffset;
        this.currentOffset = offset;
    }

    @Override
    public void subscribe(@NonNull FlowableEmitter<T> flowableEmitter) throws Exception {
        try {
            while (!flowableEmitter.isCancelled()) {
                if (isItemsAvailable()) {
                    flowableEmitter.onNext(nextItem());
                }
            }
        } catch (Exception ex) {
            log.error("Unable to load data from with currentOffset {}", currentOffset, ex);
            flowableEmitter.onError(ex);
        }
    }

    private Boolean isItemsAvailable() {
        if (currentMatchIndex >= currentItems.size()) {
            currentItems = loadItemsByOffset.apply(currentOffset);
            log.debug("Load {} items", currentItems.size());

            if (!currentItems.isEmpty()) {
                currentOffset = getNextOffset.apply(currentItems);
            }
            currentMatchIndex = 0;
        }

        return !currentItems.isEmpty();
    }

    private T nextItem() {
        return currentItems.get(currentMatchIndex++);
    }
}
