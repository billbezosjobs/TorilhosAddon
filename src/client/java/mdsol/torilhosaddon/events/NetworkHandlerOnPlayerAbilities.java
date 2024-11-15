package mdsol.torilhosaddon.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface NetworkHandlerOnPlayerAbilities {

    Event<NetworkHandlerOnPlayerAbilities> EVENT = EventFactory.createArrayBacked(NetworkHandlerOnPlayerAbilities.class, listeners -> () -> {
        for (var listener : listeners) {
            listener.onPlayerAbilities();
        }
    });

    void onPlayerAbilities();
}
