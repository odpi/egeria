/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceZone;

import java.util.List;
import java.util.Map;

/**
 * The GovernanceZoneManagerInterface is used by the governance team to define the zones where the assets
 * can be located.
 */
public interface GovernanceZoneManagerInterface
{
    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the governance zone
     * @param criteria the criteria for inclusion in a governance zone
     * @param additionalProperties additional properties for a governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  createGovernanceZone(String               userId,
                               String               qualifiedName,
                               String               displayName,
                               String               description,
                               String               criteria,
                               Map<String, String>  additionalProperties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;;


    /**
     * Return information about a specific governance zone.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    GovernanceZone getGovernanceZone(String   userId,
                                     String   qualifiedName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Return information about the defined governance zones.
     *
     * @param userId calling user
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<GovernanceZone> getGovernanceZones(String   userId,
                                            int      startingFrom,
                                            int      maximumResults) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;
}
