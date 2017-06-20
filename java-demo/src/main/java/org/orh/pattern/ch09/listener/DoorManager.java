package org.orh.pattern.ch09.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class DoorManager {

    private Collection<DoorListener> listeners;

    public void addDoorListener(DoorListener listener) {
        if (listeners == null) {
            listeners = new HashSet<DoorListener>();
        }
        listeners.add(listener);
    }

    public void removeDoorListener(DoorListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    protected void triggerWorkspaceOpened() {
        if (listeners == null) {
            return;
        }
        DoorEvent event = new DoorEvent(this, "open");
        notifyListeners(event);
    }

    protected void triggerWorkspaceClosed() {
        if (listeners == null) {
            return;
        }
        DoorEvent event = new DoorEvent(this, "close");
        notifyListeners(event);

    }

    private void notifyListeners(DoorEvent event) {
        Iterator<DoorListener> iter = listeners.iterator();
        while (iter.hasNext()) {
            iter.next().doorEvent(event);
        }
    }

}
