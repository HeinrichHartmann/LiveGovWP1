package eu.liveandgov.wp1.pipeline.impl;

import eu.liveandgov.wp1.data.Item;
import eu.liveandgov.wp1.pipeline.Pipeline;
import eu.liveandgov.wp1.serialization.Serialization;

/**
 * Created by Lukas Härtel on 13.02.14.
 */
public class ItemSerializer extends Pipeline<Item, String> {

    @Override
    public void push(Item item) {
        produce(item.toSerializedForm());
    }
}
