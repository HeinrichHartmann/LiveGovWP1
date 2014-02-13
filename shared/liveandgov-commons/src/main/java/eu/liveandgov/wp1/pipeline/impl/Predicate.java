package eu.liveandgov.wp1.pipeline.impl;

import com.google.common.base.Function;
import eu.liveandgov.wp1.pipeline.Pipeline;

/**
 * <p>The predicate element uses a given function to filter elements</p>
 * Created by Lukas Härtel on 10.02.14.
 */
public class Predicate<Item> extends Pipeline<Item, Item> {
    public final Function<? super Item, Boolean> predicate;

    public Predicate(Function<? super Item, Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void push(Item sourceItem) {
        if (predicate.apply(sourceItem)) {
            produce(sourceItem);
        }
    }
}
