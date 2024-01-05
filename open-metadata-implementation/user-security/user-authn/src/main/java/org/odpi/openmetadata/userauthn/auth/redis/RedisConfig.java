/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration values for using Redis as a token store.
 */
@Configuration
public class RedisConfig
{

    @Value("${redis.hostName:localhost}")
    private String hostName;

    @Value("${redis.port:6379}")
    private int port;


    /**
     * Initialise redis if configured.
     *
     * @return client
     */
    @Bean
    @ConditionalOnProperty(value = "authentication.mode", havingValue = "redis")
    public TokenRedisClient tokenRedisClient()
    {
        return new TokenRedisClient(hostName, port);
    }

}