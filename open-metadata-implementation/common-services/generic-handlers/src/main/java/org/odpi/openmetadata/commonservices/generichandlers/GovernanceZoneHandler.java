/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * GovernanceZoneHandler provides the exchange of metadata about governance zones between the repository and
 * the OMAS.  There is no support for effectivity dating because just too difficult to manage the zone look up for Assets.
 * They are also supposed to be long-lived.
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
     * and how they are set up.  In addition, the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param qualifiedName unique name for the zone entity
     * @param zoneName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the governance zone
     * @param criteria the criteria for inclusion in a governance zone
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed
     * @param additionalProperties additional properties for a governance zone
     * @param suppliedTypeName type name of the zone definition (null for GovernanceZone)
     * @param extendedProperties  properties for a governance zone subtype
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGovernanceZone(String              userId,
                                       String              externalSourceGUID,
                                       String              externalSourceName,
                                       String              qualifiedName,
                                       String              zoneName,
                                       String              displayName,
                                       String              description,
                                       String              criteria,
                                       String              scope,
                                       int                 domainIdentifier,
                                       Map<String, String> additionalProperties,
                                       String              suppliedTypeName,
                                       Map<String, Object> extendedProperties,
                                       Date                effectiveTime,
                                       String              methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        String typeName = OpenMetadataType.ZONE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.ZONE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceZoneBuilder builder = new GovernanceZoneBuilder(qualifiedName,
                                                                  zoneName,
                                                                  displayName,
                                                                  description,
                                                                  criteria,
                                                                  scope,
                                                                  domainIdentifier,
                                                                  additionalProperties,
                                                                  typeGUID,
                                                                  typeName,
                                                                  extendedProperties,
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition, the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param zoneGUID unique identifier for the zone to change
     * @param zoneGUIDParameterName parameter supplying the zoneGUID
     * @param qualifiedName unique name for the zone entity
     * @param zoneName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the governance zone
     * @param criteria the criteria for inclusion in a governance zone
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed
     * @param additionalProperties additional properties for a governance zone
     * @param suppliedTypeName type name of the zone definition (null for GovernanceZone)
     * @param extendedProperties  properties for a governance zone subtype
     * @param isMergeUpdate should the supplied properties be merged with or replace existing properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateGovernanceZone(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              zoneGUID,
                                     String              zoneGUIDParameterName,
                                     String              qualifiedName,
                                     String              zoneName,
                                     String              displayName,
                                     String              description,
                                     String              criteria,
                                     String              scope,
                                     int                 domainIdentifier,
                                     Map<String, String> additionalProperties,
                                     String              suppliedTypeName,
                                     Map<String, Object> extendedProperties,
                                     boolean             isMergeUpdate,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        String typeName = OpenMetadataType.ZONE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.ZONE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceZoneBuilder builder = new GovernanceZoneBuilder(qualifiedName,
                                                                  zoneName,
                                                                  displayName,
                                                                  description,
                                                                  criteria,
                                                                  scope,
                                                                  domainIdentifier,
                                                                  additionalProperties,
                                                                  typeGUID,
                                                                  typeName,
                                                                  extendedProperties,
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    zoneGUID,
                                    zoneGUIDParameterName,
                                    OpenMetadataType.ZONE_TYPE_GUID,
                                    OpenMetadataType.ZONE_TYPE_NAME,
                                    false,
                                    false,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    new Date(),
                                    methodName);
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param userId calling user
     * @param name unique name for the zone
     * @param nameParameter name of parameter supplying the name
     * @param methodName calling method
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public B getGovernanceZone(String   userId,
                               String   name,
                               String   nameParameter,
                               String   methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataType.ZONE_NAME_PROPERTY_NAME);

        return this.getBeanByValue(userId,
                                   name,
                                   nameParameter,
                                   OpenMetadataType.ZONE_TYPE_GUID,
                                   OpenMetadataType.ZONE_TYPE_NAME,
                                   specificMatchPropertyNames,
                                   false,
                                   false,
                                   new Date(),
                                   methodName);
    }


    /**
     * Return information about a specific governance zone's parent.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     * @param zoneGUIDParameter name of parameter supplying the zoneGUID
     * @param methodName calling method
     *
     * @return Relationship to the parent
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public Relationship getGovernanceZoneParentGUID(String userId,
                                                    String zoneGUID,
                                                    String zoneGUIDParameter,
                                                    String methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return this.getUniqueAttachmentLink(userId,
                                            zoneGUID,
                                            zoneGUIDParameter,
                                            OpenMetadataType.ZONE_TYPE_NAME,
                                            OpenMetadataType.ZONE_HIERARCHY_TYPE_GUID,
                                            OpenMetadataType.ZONE_HIERARCHY_TYPE_NAME,
                                            null,
                                            OpenMetadataType.ZONE_TYPE_NAME,
                                            1,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
    }


    /**
     * Return information about a specific governance zone's child (nested) zones.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     * @param zoneGUIDParameter name of parameter supplying the zoneGUID
     * @param methodName calling method
     *
     * @return list of relationships of the child governance zones
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<Relationship> getGovernanceZoneChildrenGUIDs(String userId,
                                                             String zoneGUID,
                                                             String zoneGUIDParameter,
                                                             String methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return this.getAttachmentLinks(userId,
                                       zoneGUID,
                                       zoneGUIDParameter,
                                       OpenMetadataType.ZONE_TYPE_NAME,
                                       OpenMetadataType.ZONE_HIERARCHY_TYPE_GUID,
                                       OpenMetadataType.ZONE_HIERARCHY_TYPE_NAME,
                                       null,
                                       OpenMetadataType.ZONE_TYPE_NAME,
                                       2,
                                       false,
                                       false,
                                       supportedZones,
                                       0,
                                       0,
                                       new Date(),
                                       methodName);
    }


    /**
     * Return information about the defined governance zones.
     *
     * @param userId calling user
     * @param startFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return on this call
     * @param methodName calling method
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getGovernanceZones(String userId,
                                      int    startFrom,
                                      int    pageSize,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataType.ZONE_TYPE_GUID,
                                   OpenMetadataType.ZONE_TYPE_NAME,
                                   null,
                                   false,
                                   false,
                                   supportedZones,
                                   startFrom,
                                   pageSize,
                                   new Date(),
                                   methodName);
    }


    /**
     * Return information about the defined governance zones for a specific domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier of domain - 0 is for all domains
     * @param startFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return on this call
     * @param methodName calling method
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getGovernanceZonesByDomain(String userId,
                                              int    domainIdentifier,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        if (domainIdentifier == 0)
        {
            return this.getGovernanceZones(userId, startFrom, pageSize, methodName);
        }

        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             OpenMetadataType.ZONE_TYPE_GUID,
                                                             OpenMetadataType.ZONE_TYPE_NAME,
                                                             null,
                                                             false,
                                                             false,
                                                             supportedZones,
                                                             startFrom,
                                                             pageSize,
                                                             new Date(),
                                                             methodName);

        List<B> results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    if (entity.getProperties() != null)
                    {
                        if (repositoryHelper.getIntProperty(serviceName,
                                                            OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                            entity.getProperties(),
                                                            methodName) == domainIdentifier)
                        {
                            B bean = converter.getNewBean(beanClass, entity, methodName);

                            results.add(bean);
                        }
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }
}
