/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth.model;

/**
 * Login request is a record to provide the user's userId and password in the request body of a token request.
 *
 * @param userId calling user's id
 * @param username calling user's id (deprecated)
 * @param password calling user's password
 */
public record LoginRequest(String userId, String username, String password)
{
}