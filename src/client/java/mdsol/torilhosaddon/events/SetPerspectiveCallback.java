package mdsol.torilhosaddon.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.option.Perspective;

public interface SetPerspectiveCallback {

    Event<SetPerspectiveCallback> EVENT = EventFactory.createArrayBacked(SetPerspectiveCallback.class, listeners -> perspective -> {
        var newPerspective = perspective;

        for (var listener : listeners) {
            newPerspective = listener.onSetPerspective(perspective);
        }

        return newPerspective;
    });

    Perspective onSetPerspective(Perspective perspective);
}
