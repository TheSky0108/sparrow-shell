/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.cache.impl.redis;

import com.sparrow.cache.CacheDataNotFound;
import com.sparrow.cache.CacheSortedSet;
import com.sparrow.constant.cache.KEY;
import com.sparrow.core.TypeConverter;
import com.sparrow.exception.CacheConnectionException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.Tuple;

public class RedisCacheSortedSet extends AbstractCommand implements CacheSortedSet {
    RedisCacheSortedSet(RedisPool redisPool) {
        this.redisPool = redisPool;
    }

    @Override
    public Long getSize(final KEY key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.zcard(key.key());
            }
        }, key);
    }

    @Override
    public <T> Long add(final KEY key, final T value, final double score) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                return jedis.zadd(key.key(), score, typeConverter.convert(value).toString());
            }
        }, key);
    }

    @Override
    public <T> Long remove(final KEY key, final T value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                return jedis.zrem(key.key(), typeConverter.convert(value).toString());
            }
        }, key);
    }

    @Override
    public Long remove(final KEY key, final Long from, final Long to) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.zremrangeByRank(key.key(), from, to);
            }
        }, key);
    }

    @Override
    public <T> Double getScore(final KEY key, final T value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Double>() {
            @Override
            public Double execute(ShardedJedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                return jedis.zscore(key.key(), typeConverter.convert(value).toString());
            }
        }, key);
    }

    @Override
    public <T> Long getRank(final KEY key, final T value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.zrank(key.key(), value.toString());
            }
        }, key);
    }

    @Override
    public Map<String, Double> getAllWithScore(final KEY key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Map<String, Double>>() {
            @Override
            public Map<String, Double> execute(ShardedJedis jedis) {
                Set<Tuple> tuples = jedis.zrevrangeWithScores(key.key(), 0, -1);
                Map<String, Double> scoreMap = new LinkedHashMap<String, Double>(tuples.size());
                for (Tuple tuple : tuples) {
                    scoreMap.put(tuple.getElement(), tuple.getScore());
                }
                return scoreMap;
            }
        }, key);
    }

    @Override
    public <T> Integer putAllWithScore(final KEY key,
        final Map<T, Double> keyScoreMap) throws CacheConnectionException {
        return redisPool.execute(new Executor<Integer>() {
            @Override
            public Integer execute(ShardedJedis jedis) {
                ShardedJedisPipeline pipeline = jedis.pipelined();
                TypeConverter typeConverter = new TypeConverter(String.class);
                for (T value : keyScoreMap.keySet()) {
                    pipeline.zadd(key.key(), keyScoreMap.get(value), typeConverter.convert(value).toString());
                }
                pipeline.sync();
                return keyScoreMap.size();
            }

            ;
        }, key);
    }

    @Override
    public <T> Map<T, Double> getAllWithScore(final KEY key, final Class keyClazz,
        final CacheDataNotFound<Map<T, Double>> hook) {
        try {
            return redisPool.execute(new Executor<Map<T, Double>>() {
                @Override
                public Map<T, Double> execute(ShardedJedis jedis) {
                    Map<T, Double> scoreMap = null;
                    Set<Tuple> tuples = jedis.zrevrangeWithScores(key.key(), 0, -1);
                    if (tuples == null || tuples.size() == 0) {
                        if (redisPool.getCacheMonitor() != null) {
                            redisPool.getCacheMonitor().breakdown(key);
                        }
                        scoreMap = hook.read(key);
                        try {
                            RedisCacheSortedSet.this.putAllWithScore(key, scoreMap);
                        } catch (CacheConnectionException ignore) {
                        }
                        return scoreMap;
                    }
                    scoreMap = new LinkedHashMap<T, Double>(tuples.size());
                    TypeConverter typeConverter = new TypeConverter(keyClazz);
                    for (Tuple tuple : tuples) {
                        scoreMap.put((T) typeConverter.convert(tuple.getElement()), tuple.getScore());
                    }
                    return scoreMap;
                }
            }, key);
        } catch (CacheConnectionException e) {
            if (redisPool.getCacheMonitor() != null) {
                redisPool.getCacheMonitor().breakdown(key);
            }
            return hook.read(key);
        }
    }
}
