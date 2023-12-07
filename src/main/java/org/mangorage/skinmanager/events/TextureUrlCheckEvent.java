package org.mangorage.skinmanager.events;

import net.minecraftforge.eventbus.api.Event;

public class TextureUrlCheckEvent extends Event {
    private final String url;
    private boolean allowed = false;

    public TextureUrlCheckEvent(String url) {
        this.url = url;
    }

    public void setAllowed() {
        this.allowed = true;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getUrl() {
        return url;
    }
}
