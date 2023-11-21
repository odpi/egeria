/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

/**
 * TokenClient defines the interface for a stateful web token by using persistence and expiration validation.
 */
public interface TokenClient
{
    /**
     * shut down client connection
     */
    default void shutdownClient() {};

    /**
     *
     * @param token the token
     * @param seconds for absolute timeout
     * @param expiration representation of expiration
     * @return the persistence response
     */
    default String set(String token, long seconds, String expiration){
        return null;
    };

    /**
     *
     * @param token the token
     * @param expiration representation of expiration
     * @return the persistence response
     */
    default String set(String token, String expiration){
        return null;
    };

    /**
     * Used to postpone expiration but keep existing absolute timeout
     * @param token the token
     * @param expiration representation of expiration
     * @return the persistence response
     */
    default String setKeepTTL(String token, String expiration){
        return null;
    };


    /**
     * retrieve expiration from persistence
     * @param token the token
     * @return expiration or null if token doesn't exist
     */
    default String get(String token){
        return null;
    };

    /**
     * retrieve the absolute timeout of token in seconds
     * @param token the token to determine ttl for
     * @return the ttl for the token
     */
    default Long ttl(String token){
        return null;
    };

    /**
     *
     * @param tokens the tokens to be removed from persistence
     */
    default void del(String... tokens) { };
}
