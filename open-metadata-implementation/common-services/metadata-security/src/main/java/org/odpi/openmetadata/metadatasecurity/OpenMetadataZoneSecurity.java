/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * OpenMetadataZoneSecurity validates if a user is allowed access to Assets within a specific Governance Zone.
 */
public interface OpenMetadataZoneSecurity extends OpenMetadataServerSecurity
{
    /**
     * Tests for whether a specific user should have access to assets within a zone.
     *
     * @param userId identifier of user
     * @param zoneName name of the zones
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    void  validateUserForZone(String     userId,
                              String     zoneName) throws UserNotAuthorizedException;
}
