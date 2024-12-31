/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * SubjectAreaHandler provides the exchange of metadata about subject areas between the repository and
 * the OMAS.  There is no support for effectivity dating for this element
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
     * @param supportedZones list of subjectAreas that the access service is allowed to serve B instances from.
     * @param defaultZones list of subjectAreas that the access service should set in all new B instances.
     * @param publishZones list of subjectAreas that the access service sets up in published B instances.
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
     * and how they are set up.  In addition, the qualified names of subjectAreas can be added to Asset definitions
     * to indicate which subjectArea(s) they belong to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param qualifiedName unique name for the subject area entity
     * @param subjectAreaName unique name for the subject area - used in other configuration
     * @param displayName short display name for the subjectArea
     * @param description description of the subject area
     * @param usage the usage for inclusion in a subject area
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the subjectArea is managed
     * @param additionalProperties additional properties for a subject area
     * @param suppliedTypeName name of subtype - or null for SubjectAreaDefinition
     * @param extendedProperties  properties for a subject area subtype
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new subjectArea
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createSubjectArea(String              userId,
                                    String              externalSourceGUID,
                                    String              externalSourceName,
                                    String              qualifiedName,
                                    String              subjectAreaName,
                                    String              displayName,
                                    String              description,
                                    String              usage,
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
        String typeName = OpenMetadataType.SUBJECT_AREA_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SubjectAreaBuilder builder = new SubjectAreaBuilder(qualifiedName,
                                                            subjectAreaName,
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
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a definition of a subject area.  The qualified name of these subject areas can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition, the qualified names of subjectAreas can be added to Asset definitions
     * to indicate which subjectArea(s) they belong to.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param subjectAreaGUID unique identifier of subject area
     * @param subjectAreaGUIDParameterName parameter name for subjectAreaGUID
     * @param qualifiedName unique name for the subject area entity
     * @param subjectAreaName unique name for the subject area - used in other configuration
     * @param displayName short display name for the subjectArea
     * @param description description of the subject area
     * @param criteria the criteria for inclusion in a subject area
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the subjectArea is managed
     * @param additionalProperties additional properties for a subject area
     * @param suppliedTypeName subtype name
     * @param extendedProperties  properties for a subject area subtype
     * @param isMergeUpdate should the supplied properties be merged with the existing one or replace them
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateSubjectArea(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              subjectAreaGUID,
                                  String              subjectAreaGUIDParameterName,
                                  String              qualifiedName,
                                  String              subjectAreaName,
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
        String typeName = OpenMetadataType.SUBJECT_AREA_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SubjectAreaBuilder builder = new SubjectAreaBuilder(qualifiedName,
                                                            subjectAreaName,
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
                                    subjectAreaGUID,
                                    subjectAreaGUIDParameterName,
                                    OpenMetadataType.SUBJECT_AREA_TYPE_GUID,
                                    OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                    false,
                                    false,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    new Date(),
                                    methodName);
    }


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param name unique name for the subjectArea
     * @param nameParameter name of parameter supplying the name
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public B getSubjectArea(String   userId,
                            String   name,
                            String   nameParameter,
                            boolean  forLineage,
                            boolean  forDuplicateProcessing,
                            Date     effectiveTime,
                            String   methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataType.SUBJECT_AREA_NAME_PROPERTY_NAME);

        return this.getBeanByValue(userId,
                                   name,
                                   nameParameter,
                                   OpenMetadataType.SUBJECT_AREA_TYPE_GUID,
                                   OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                   specificMatchPropertyNames,
                                   null,
                                   null,
                                   SequencingOrder.CREATION_DATE_RECENT,
                                   null,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Return information about a specific subject area's parent.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subjectArea
     * @param subjectAreaGUIDParameter name of parameter supplying the subjectAreaGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the parent subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String getSubjectAreaParentGUID(String  userId,
                                           String  subjectAreaGUID,
                                           String  subjectAreaGUIDParameter,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        EntityDetail entity = this.getAttachedEntity(userId,
                                                     subjectAreaGUID,
                                                     subjectAreaGUIDParameter,
                                                     OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                                     OpenMetadataType.SUBJECT_AREA_HIERARCHY_TYPE_GUID,
                                                     OpenMetadataType.SUBJECT_AREA_HIERARCHY_TYPE_NAME,
                                                     OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                                     1,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     supportedZones,
                                                     effectiveTime,
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
     * Return information about a specific subject area's child (nested) subjectAreas.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subjectArea
     * @param subjectAreaGUIDParameter name of parameter supplying the subjectAreaGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifier of the parent subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<String> getSubjectAreaChildrenGUIDs(String  userId,
                                                    String  subjectAreaGUID,
                                                    String  subjectAreaGUIDParameter,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        List<EntityDetail> entities = this.getAttachedEntities(userId,
                                                               subjectAreaGUID,
                                                               subjectAreaGUIDParameter,
                                                               OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                                               OpenMetadataType.SUBJECT_AREA_HIERARCHY_TYPE_GUID,
                                                               OpenMetadataType.SUBJECT_AREA_HIERARCHY_TYPE_NAME,
                                                               OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                                               null,
                                                               null,
                                                               0,
                                                               null,
                                                               null,
                                                               SequencingOrder.CREATION_DATE_RECENT,
                                                               null,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               supportedZones,
                                                               0,
                                                               0,
                                                               effectiveTime,
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
     * @param pageSize maximum number of elements to return on this call
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getSubjectAreas(String  userId,
                                   int     startFrom,
                                   int     pageSize,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataType.SUBJECT_AREA_TYPE_GUID,
                                   OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                   null,
                                   null,
                                   SequencingOrder.CREATION_DATE_RECENT,
                                   null,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   startFrom,
                                   pageSize,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Return information about the defined subject areas for a specific domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier of domain - 0 is for all domains
     * @param startFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return on this call
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getSubjectAreasByDomain(String  userId,
                                           int     domainIdentifier,
                                           int     startFrom,
                                           int     pageSize,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        if (domainIdentifier == 0)
        {
            return this.getSubjectAreas(userId, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             OpenMetadataType.SUBJECT_AREA_TYPE_GUID,
                                                             OpenMetadataType.SUBJECT_AREA_TYPE_NAME,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             startFrom,
                                                             pageSize,
                                                             effectiveTime,
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
