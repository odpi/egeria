/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;

/**
 * This interface is used to authenticate a user and return the properties known about them that can be included in
 * the resulting JWT token.
 */
public interface OpenMetadataUserSecurity
{
    /**
     * Retrieve information about a specific user
     *
     * @param userId calling user
     * @return security properties about the user
     * @throws UserNotAuthorizedException user not recognized - or supplied an incorrect password
     */
   OpenMetadataUserAccount getUserAccount(String userId) throws UserNotAuthorizedException;
}
