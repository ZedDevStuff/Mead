package dev.zeddevstuff.mead.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Event that calls its listeners once then removes them.
 * @param <T>
 */
public class SingleEvent<T>
{
    private final Object key;
    private final List<IListener<T>> listeners = new ArrayList<>();

    public SingleEvent()
    {
        key = null;
    }
    public SingleEvent(Object key)
    {
        this.key = key;
    }
    public void fire(Object key, T eventArgs)
    {
        if(this.key == null || this.key.equals(key))
            for(var listener : listeners)
                listener.listen(eventArgs);
    }

    public void addListener(IListener<T> listener)
    {
        if(listener != null)
            listeners.add(listener);
    }
    public void removeListener(IListener<T> listener)
    {
        if(listener != null)
            listeners.remove(listener);
    }

    public interface IListener<T>
    {
        void listen(T eventArgs);
    }
}
