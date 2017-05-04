package pattern.ch09.listener;

import java.util.EventListener;

public interface DoorListener extends EventListener {
    public void doorEvent(DoorEvent event);
}
