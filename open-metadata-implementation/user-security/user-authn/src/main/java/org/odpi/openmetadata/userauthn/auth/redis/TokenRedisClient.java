/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.odpi.openmetadata.userauthn.auth.TokenClient;

/**
 * Redis token client
 */
public class TokenRedisClient implements TokenClient
{

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> commands;

    public TokenRedisClient(String host, int port){
        RedisURI redisUri = RedisURI.Builder.redis(host).withPort(port).build();
        redisClient = RedisClient.create(redisUri);
        connection = redisClient.connect();
        commands = connection.sync();
    }

    @Override
    public void shutdownClient(){
        connection.close();
        redisClient.shutdown();
    }

    @Override
    public String set(String key, long seconds, String value){
        return commands.setex(key, seconds, value);
    }

    @Override
    public String set(String key, String value){
        return commands.set(key, value);
    }

    @Override
    public String setKeepTTL(String key, String value){
        return commands.set(key, value, SetArgs.Builder.keepttl());
    }

    @Override
    public String get(String key){
        return commands.get(key);
    }

    @Override
    public Long ttl(String key){
        return commands.ttl(key);
    }

    public Long exists(String... keys){
        return commands.exists(keys);
    }

    @Override
    public void del(String... keys){
        commands.del(keys);
    }


}
