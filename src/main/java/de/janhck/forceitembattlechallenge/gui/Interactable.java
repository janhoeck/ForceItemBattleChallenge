package de.janhck.forceitembattlechallenge.gui;

import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;

import java.util.ArrayList;
import java.util.List;

public abstract class Interactable<R> {

    private final List<ClickAction<R>> clickConsumerList = new ArrayList<>();
    private long lastInteractionTimestamp = 0L;

    public void addClickConsumer(ClickAction<R> clickAction) {
        clickConsumerList.add(clickAction);
    }

    public void callClickConsumers(ClickAction.Handler<R> handler) {
        long nowTimestamp = System.currentTimeMillis();
        if(nowTimestamp - lastInteractionTimestamp <= 250) {
            return;
        }

        clickConsumerList.forEach(consumer -> consumer.handleClick(handler));
        lastInteractionTimestamp = nowTimestamp;
    }

}
