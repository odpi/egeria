/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.gaf.metadatamanagement.builders.GovernanceZoneBuilder;
import org.odpi.openmetadata.commonservices.gaf.metadatamanagement.converters.GovernanceZoneConverter;
import org.odpi.openmetadata.commonservices.gaf.metadatamanagement.mappers.GovernanceZoneMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceZone;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * GovernanceZoneHandler provides the exchange of metadata about governance zones between the repository and
 * the OMAS.
 */
public class GovernanceZoneHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName  name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public GovernanceZoneHandler(String                  serviceName,
                                 String                  serverName,
                                 InvalidParameterHandler invalidParameterHandler,
                                 RepositoryHandler       repositoryHandler,
                                 OMRSRepositoryHelper    repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }



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
     * @param extendedProperties  properties for a governance zone subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  createGovernanceZone(String              userId,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              criteria,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties,
                                      String              methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        GovernanceZoneBuilder governanceZoneBuilder = new GovernanceZoneBuilder(qualifiedName,
                                                                              displayName,
                                                                              description,
                                                                              criteria,
                                                                              additionalProperties,
                                                                              extendedProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);
        repositoryHandler.createEntity(userId,
                                       GovernanceZoneMapper.ZONE_TYPE_GUID,
                                       GovernanceZoneMapper.ZONE_TYPE_NAME,
                                       governanceZoneBuilder.getInstanceProperties(methodName),
                                       methodName);
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     * @param methodName calling method
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public GovernanceZone getGovernanceZone(String   userId,
                                            String   qualifiedName,
                                            String   methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {

        final String  qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        GovernanceZoneBuilder zoneBuilder = new GovernanceZoneBuilder(qualifiedName,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        EntityDetail zoneEntity = repositoryHandler.getUniqueEntityByName(userId,
                                                                           qualifiedName,
                                                                           qualifiedNameParameter,
                                                                           zoneBuilder.getQualifiedNameInstanceProperties(methodName),
                                                                           GovernanceZoneMapper.ZONE_TYPE_GUID,
                                                                           GovernanceZoneMapper.ZONE_TYPE_NAME,
                                                                           methodName);

        GovernanceZoneConverter zoneConverter = new GovernanceZoneConverter(zoneEntity,
                                                                            repositoryHelper,
                                                                            serviceName);

        return zoneConverter.getZoneBean();
    }


    /**
     * Return information about the defined governance zones.
     *
     * @param userId calling user
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     * @param methodName calling method
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<GovernanceZone> getGovernanceZones(String   userId,
                                                   int      startingFrom,
                                                   int      maximumResults,
                                                   String   methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        List<EntityDetail>  entities = repositoryHandler.getEntitiesForType(userId,
                                                                            GovernanceZoneMapper.ZONE_TYPE_GUID,
                                                                            GovernanceZoneMapper.ZONE_TYPE_NAME,
                                                                            startingFrom,
                                                                            maximumResults,
                                                                            methodName);

        List<GovernanceZone> results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    if (entity.getGUID() != null)
                    {
                        GovernanceZoneConverter zoneConverter = new GovernanceZoneConverter(entity,
                                                                                            repositoryHelper,
                                                                                            serviceName);

                        results.add(zoneConverter.getZoneBean());
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }
}
