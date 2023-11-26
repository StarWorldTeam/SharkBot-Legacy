package team.starworld.shark.event.bus;

import team.starworld.shark.event.Event;

/**
 * 事件回调函数
 * @param <T> 事件类型
 */
@FunctionalInterface
public interface EventCallback <T extends Event> {

    /**
     * 执行事件回调函数
     * @param event 事件
     * @param callback 当前事件回调函数对象
     */
    void call (T event, EventCallback <T> callback);

}
