package org.orh.pattern.ch09.listener;

public class DoorMain {

    public static void main(String[] args) {
        DoorManager manager = new DoorManager();
        manager.addDoorListener(new DoorListener1()); //给门1添加监听器
        manager.addDoorListener(new DoorListener2()); //给门2添加监听器
        
        manager.triggerWorkspaceOpened();
        System.out.println("我已经进来了");
        manager.triggerWorkspaceClosed();
    }
}
