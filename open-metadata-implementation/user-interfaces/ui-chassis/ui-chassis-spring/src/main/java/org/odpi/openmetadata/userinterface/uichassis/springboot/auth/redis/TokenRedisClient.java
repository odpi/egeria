/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class TokenRedisClient {

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> commands;

    public TokenRedisClient(String host, int port){
        RedisURI redisUri = RedisURI.Builder.redis(host).withPort(port).build();
        redisClient = RedisClient.create(redisUri);
        connection = redisClient.connect();
        commands = connection.sync();
    }

    public void shutdownRedisClient(){
        connection.close();
        redisClient.shutdown();
    }

    public String set(String key, long seconds, String value){
        return commands.setex(key, seconds, value);
    }

    public String set(String key, String value){
        return commands.set(key, value);
    }

    public Boolean expire(String key, long seconds){
        return commands.expire(key, seconds);
    }

    public String get(String key){
        return commands.get(key);
    }

    public Long ttl(String key){
        return commands.ttl(key);
    }

    public Long exists(String... keys){
        return commands.exists(keys);
    }

    public Long del(String... keys){
        return commands.del(keys);
    }


}
