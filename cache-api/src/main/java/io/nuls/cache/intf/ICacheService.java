package io.nuls.cache.intf;

import io.nuls.cache.entity.CacheElement;

import java.util.List;
import java.util.Map;

/**
 * Created by Niels on 2017/10/18.
 * nuls.io
 */
public interface ICacheService<T> {

    /**
     * create a cache named title
     * @param title
     */
    void createCache(String title);


    /**
     * create a cache named title by configurations
     * @param title
     * @param initParams
     */
    void createCache(String title,Map<String,Object> initParams);

    /**
     * remove a cache by title
     * @param title
     */
    void removeCache(String title);

    /**
     * put data to a cache
     * @param cacheTitle
     * @param key
     * @param value
     */
    void putElement(String cacheTitle,String key,T value);

    /**
     * put data to a cache
     * @param cacheTitle
     * @param element
     */
    void putElement(String cacheTitle,CacheElement element);

    /**
     * get data from the cache named cacheTitle
     * @param cacheTitle
     * @param key
     * @return
     */
    T getElement(String cacheTitle,String key);

    /**
     * remove an element from the cache named cacheTitle
     * @param cacheTitle
     * @param key
     */
    void removeElement(String cacheTitle,String key);

    /**
     * Batch addition
     * @param cacheTitle
     * @param elementList
     */
    void putElements(String cacheTitle,List<CacheElement<T>> elementList);

    /**
     * @param Title
     */
    void clearCache(String Title);

    /**
     * Add an operating listener
     * add a oprateion
     * @param title
     * @param listenner
     */
    void addCacheListenner(String title,ICacheEventListenner listenner);

}
