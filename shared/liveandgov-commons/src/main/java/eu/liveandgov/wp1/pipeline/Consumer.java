package eu.liveandgov.wp1.pipeline;

/**
 * Created by Lukas Härtel on 08.02.14.
 */
public interface Consumer<Item> {
    /**
     * Consumer that performs no operation on consuming an item
     */
    public static final Consumer<Object> EMPTY_CONSUMER = new Consumer<Object>() {
        @Override
        public void push(Object o) {
        }
    };

    /**
     * Handles a produced item
     *
     * @param item The item to handle
     */
    public void push(Item item);
}
