/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * SubjectAreaHandler provides the exchange of metadata about subject areas between the repository and
 * the OMAS.
 */
public class SubjectAreaHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the subject area handler with information needed to work with B objects.
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
    public SubjectAreaHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a definition of a subject area.  The qualified name of these subject areas can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param qualifiedName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the subject area
     * @param usage the usage for inclusion in a subject area
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed
     * @param additionalProperties additional properties for a subject area
     * @param extendedProperties  properties for a subject area subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createSubjectArea(String                 userId,
                                    String              externalSourceGUID,
                                    String              externalSourceName,
                                    String              qualifiedName,
                                    String              displayName,
                                    String              description,
                                    String              usage,
                                    String              scope,
                                    int                 domainIdentifier,
                                    Map<String, String> additionalProperties,
                                    String              suppliedTypeName,
                                    Map<String, Object> extendedProperties,
                                    String              methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        String typeName = OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SubjectAreaBuilder builder = new SubjectAreaBuilder(qualifiedName,
                                                            displayName,
                                                            description,
                                                            usage,
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
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Create a definition of a subject area.  The qualified name of these subject areas can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param qualifiedName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the subject area
     * @param criteria the criteria for inclusion in a subject area
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed
     * @param additionalProperties additional properties for a subject area
     * @param extendedProperties  properties for a subject area subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateSubjectArea(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              zoneGUID,
                                  String              zoneGUIDParameterName,
                                  String              qualifiedName,
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
        String typeName = OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SubjectAreaBuilder builder = new SubjectAreaBuilder(qualifiedName,
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
                                    OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_GUID,
                                    OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    methodName);
    }


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     * @param qualifiedNameParameter name of parameter supplying the qualifiedName
     * @param methodName calling method
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public B getSubjectArea(String   userId,
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
                                        OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_GUID,
                                        OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                        methodName);
    }


    /**
     * Return information about a specific subject area's parent.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     * @param zoneGUIDParameter name of parameter supplying the zoneGUID
     * @param methodName calling method
     *
     * @return unique identifier of the parent subject area
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String getSubjectAreaParentGUID(String userId,
                                           String zoneGUID,
                                           String zoneGUIDParameter,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        EntityDetail entity = this.getAttachedEntity(userId,
                                                     zoneGUID,
                                                     zoneGUIDParameter,
                                                     OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                     OpenMetadataAPIMapper.GOVERNED_BY_TYPE_GUID,
                                                     OpenMetadataAPIMapper.GOVERNED_BY_TYPE_NAME,
                                                     OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                                     methodName);

        if (entity != null)
        {
            return entity.getGUID();
        }
        else
        {
            return null;
        }
    }


    /**
     * Return information about a specific subject area's child (nested) zones.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     * @param zoneGUIDParameter name of parameter supplying the zoneGUID
     * @param methodName calling method
     *
     * @return list of unique identifier of the parent subject area
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getSubjectAreaChildrenGUIDs(String userId,
                                                    String zoneGUID,
                                                    String zoneGUIDParameter,
                                                    String methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        List<EntityDetail> entities = this.getAttachedEntities(userId,
                                                               zoneGUID,
                                                               zoneGUIDParameter,
                                                               OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                               OpenMetadataAPIMapper.GOVERNED_BY_TYPE_GUID,
                                                               OpenMetadataAPIMapper.GOVERNED_BY_TYPE_NAME,
                                                               OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                                               0,
                                                               0,
                                                               methodName);

        List<String> results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    results.add(entity.getGUID());
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Return information about the defined subject areas.
     *
     * @param userId calling user
     * @param startFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return an this call
     * @param methodName calling method
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getSubjectAreas(String userId,
                                   int    startFrom,
                                   int    pageSize,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_GUID,
                                   OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                   supportedZones,
                                   OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                   startFrom,
                                   pageSize,
                                   methodName);
    }


    /**
     * Return information about the defined subject areas for a specific domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier of domain - 0 is for all domains
     * @param startFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return an this call
     * @param methodName calling method
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getSubjectAreasByDomain(String userId,
                                           int    domainIdentifier,
                                           int    startFrom,
                                           int    pageSize,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        if (domainIdentifier == 0)
        {
            return this.getSubjectAreas(userId, startFrom, pageSize, methodName);
        }

        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_GUID,
                                                             OpenMetadataAPIMapper.SUBJECT_AREA_TYPE_NAME,
                                                             supportedZones,
                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                             startFrom,
                                                             pageSize,
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
                                                            OpenMetadataAPIMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
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
