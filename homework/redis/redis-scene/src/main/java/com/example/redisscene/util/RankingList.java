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

package com.example.redisscene.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 分数排名或者排行榜
 *
 * @author lw1243925457
 */
public class RankingList {

    public static void main(String[] args) {
        try (JedisPool jedisPool = new JedisPool(); Jedis jedis = jedisPool.getResource()) {
            String key = "rankingList";
            jedis.del(key);

            for (int i = 0; i < 1000; i++) {
                String user = "user:" + new Random(System.currentTimeMillis()).nextInt(10);
                jedis.zincrby(key, 1, user);

                Set<String> users = jedis.zrevrange(key, 0, 2);
                System.out.print("Top 3:");
                for (String item : users) {
                    System.out.printf("%s -- %f  ", item, jedis.zscore(key, item));
                }
                System.out.println();
            }
        }

        //
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("a", 1);
        hashMap.put("b", 2);
        hashMap.put("f", 5);
        hashMap.put("d", 4);
        hashMap.put("c", 3);
        hashMap.put("b1", 52);
        hashMap.put("ef", 15);
        hashMap.put("n3", 34);
        hashMap.put("ed", 13);

        hashMap.entrySet().forEach(item -> {
            System.out.println(item.getKey() + " : " + item.getValue());
        });
        System.out.println("========================================");
        LinkedHashMap<String, Integer> linkedHashMap = hashMap.entrySet().stream()
                .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        linkedHashMap.entrySet().forEach(item -> {
            System.out.println(item.getKey() + " : " + item.getValue());
        });
    }

}

