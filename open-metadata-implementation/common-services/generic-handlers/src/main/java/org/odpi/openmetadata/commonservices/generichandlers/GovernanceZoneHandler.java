/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;


/**
 * GovernanceZoneHandler provides the exchange of metadata about governance zones between the repository and
 * the OMAS.
 */
public class GovernanceZoneHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the governance zone handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public GovernanceZoneHandler(OpenMetadataAPIGenericConverter<B> converter,
                                 Class<B>                           beanClass,
                                 String                             serviceName,
                                 String                             serverName,
                                 InvalidParameterHandler            invalidParameterHandler,
                                 RepositoryHandler                  repositoryHandler,
                                 OMRSRepositoryHelper               repositoryHelper,
                                 String                             localServerUserId,
                                 OpenMetadataServerSecurityVerifier securityVerifier,
                                 List<String>                       supportedZones,
                                 List<String>                       defaultZones,
                                 List<String>                       publishZones,
                                 AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param qualifiedName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the governance zone
     * @param criteria the criteria for inclusion in a governance zone
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed
     * @param additionalProperties additional properties for a governance zone
     * @param extendedProperties  properties for a governance zone subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  createGovernanceZone(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              criteria,
                                      String              scope,
                                      int                 domainIdentifier,
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
                                                                              scope,
                                                                              domainIdentifier,
                                                                              additionalProperties,
                                                                              extendedProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        this.createBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    OpenMetadataAPIMapper.ZONE_TYPE_GUID,
                                    OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                    qualifiedName,
                                    OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                    governanceZoneBuilder,
                                    methodName);
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     * @param qualifiedNameParameter name of parameter supplying the qualifiedName
     * @param methodName calling method
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public B getGovernanceZone(String   userId,
                               String   qualifiedName,
                               String   qualifiedNameParameter,
                               String   methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return this.getBeanByUniqueName(userId,
                                        qualifiedName,
                                        qualifiedNameParameter,
                                        OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                        OpenMetadataAPIMapper.ZONE_TYPE_GUID,
                                        OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                        methodName);
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
    public List<B> getGovernanceZones(String   userId,
                                      int      startingFrom,
                                      int      maximumResults,
                                      String   methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataAPIMapper.ZONE_TYPE_GUID,
                                   OpenMetadataAPIMapper.ZONE_TYPE_NAME,
                                   supportedZones,
                                   startingFrom,
                                   maximumResults,
                                   methodName);
    }
}
