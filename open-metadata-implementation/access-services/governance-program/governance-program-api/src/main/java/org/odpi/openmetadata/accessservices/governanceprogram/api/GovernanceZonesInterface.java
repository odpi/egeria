/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneDefinition;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * The GovernanceZonesInterface is used by the governance team to define the zones where the assets
 * can be located.
 */
public interface GovernanceZonesInterface
{
    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones, publishedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition, the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param properties  properties for a governance zone
     *
     * @return unique identifier of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createGovernanceZone(String                   userId,
                                GovernanceZoneProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;

    /**
     * Update the definition of a zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateGovernanceZone(String                   userId,
                              String                   zoneGUID,
                              boolean                  isMergeUpdate,
                              GovernanceZoneProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Remove the definition of a zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteGovernanceZone(String userId,
                              String zoneGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Link two related governance zones together as part of a hierarchy.
     * A zone can only have one parent but many child zones.
     *
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkZonesInHierarchy(String userId,
                              String parentZoneGUID,
                              String childZoneGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Remove the link between two zones in the zone hierarchy.
     *
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkZonesInHierarchy(String userId,
                                String parentZoneGUID,
                                String childZoneGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;

    /**
     * Return information about a specific governance zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    GovernanceZoneElement getGovernanceZoneByGUID(String userId,
                                                  String zoneGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Return information about a specific governance zone and its linked governance definitions.
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
    GovernanceZoneElement getGovernanceZoneByName(String userId,
                                                  String qualifiedName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Return information about the defined governance zones.
     *
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<GovernanceZoneElement> getGovernanceZonesForDomain(String userId,
                                                            int    domainIdentifier,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Return information about a specific governance zone and its linked governance definitions.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone linked to the associated governance definitions
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    GovernanceZoneDefinition getGovernanceZoneDefinitionByGUID(String userId,
                                                               String zoneGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;
}
