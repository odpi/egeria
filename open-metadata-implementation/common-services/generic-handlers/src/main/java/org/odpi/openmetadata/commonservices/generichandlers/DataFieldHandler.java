/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Handler for data fields which are part of discovery reports.
 *
 * @param <B> class of bean
 */
public class DataFieldHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
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
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public DataFieldHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Return the list of data fields from previous runs of the discovery service.
     * These data fields are the data fields are the accepted data fields linked to the asset's schema.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param startingFrom starting position in the list.
     * @param pageSize maximum number of elements that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    public List<B>  getPreviousDataFieldsForAsset(String  userId,
                                                  String  discoveryReportGUID,
                                                  int     startingFrom,
                                                  int     pageSize,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing,
                                                  Date    effectiveTime,
                                                  String  methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String discoveryReportGUIDParameterName = "discoveryReportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, discoveryReportGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        // todo not sure what this means - need more info

        return null;
    }


    /**
     * Return the current list of data fields for this discovery run.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param startingFrom starting position in the list.
     * @param pageSize maximum number of elements that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    public List<B> getNewDataFieldsForAsset(String  userId,
                                            String  discoveryReportGUID,
                                            int     startingFrom,
                                            int     pageSize,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String   discoveryReportGUIDParameterName = "discoveryReportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, discoveryReportGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<Relationship> annotationRelationships = super.getAttachmentLinks(userId,
                                                                              discoveryReportGUID,
                                                                              discoveryReportGUIDParameterName,
                                                                              OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                                              OpenMetadataAPIMapper.REPORT_TO_ANNOTATIONS_TYPE_GUID,
                                                                              OpenMetadataAPIMapper.REPORT_TO_ANNOTATIONS_TYPE_NAME,
                                                                              null,
                                                                              OpenMetadataAPIMapper.SCHEMA_ANALYSIS_ANNOTATION_TYPE_NAME,
                                                                              0,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              supportedZones,
                                                                              0,
                                                                              invalidParameterHandler.getMaxPagingSize(),
                                                                              effectiveTime,
                                                                              methodName);

        if ((annotationRelationships != null) && (! annotationRelationships.isEmpty()))
        {
            for (Relationship relationship : annotationRelationships)
            {
                if (relationship != null)
                {
                    EntityProxy annotationEnd = relationship.getEntityOneProxy();

                    if (annotationEnd != null)
                    {
                        final String annotationEndParameterName = "annotationEnd.getGUID()";
                        List<String> entityGUIDs = super.getAttachedElementGUIDs(userId,
                                                                                 annotationEnd.getGUID(),
                                                                                 annotationEndParameterName,
                                                                                 OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                                 OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_GUID,
                                                                                 OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_NAME,
                                                                                 OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 startingFrom,
                                                                                 pageSize,
                                                                                 effectiveTime,
                                                                                 methodName);

                        return this.getDataFields(userId,
                                                  entityGUIDs,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);
                    }
                }
            }
        }

        return null;
    }


    /**
     * Return any data fields attached to this data field.
     *
     * @param userId identifier of calling user
     * @param parentDataFieldGUID parent data field identifier
     * @param startingFrom starting position in the list
     * @param pageSize maximum number of data fields that can be returned.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B>  getNestedDataFields(String  userId,
                                        String  parentDataFieldGUID,
                                        int     startingFrom,
                                        int     pageSize,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String dataFieldGUIDParameterName = "parentDataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataFieldGUID, dataFieldGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<String> entityGUIDs = super.getAttachedElementGUIDs(userId,
                                                                 parentDataFieldGUID,
                                                                 dataFieldGUIDParameterName,
                                                                 OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                 OpenMetadataAPIMapper.DISCOVERED_NESTED_DATA_FIELD_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.DISCOVERED_NESTED_DATA_FIELD_TYPE_NAME,
                                                                 OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 startingFrom,
                                                                 pageSize,
                                                                 effectiveTime,
                                                                 methodName);

        return this.getDataFields(userId,
                                  entityGUIDs,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return any peer data fields attached to this data field.
     *
     * @param userId identifier of calling user
     * @param dataFieldGUID starting data field identifier
     * @param startingFrom starting position in the list
     * @param pageSize maximum number of data fields that can be returned.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B>  getLinkedDataFields(String  userId,
                                        String  dataFieldGUID,
                                        int     startingFrom,
                                        int     pageSize,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String dataFieldGUIDParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, dataFieldGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<Relationship> relationships = super.getAttachmentLinks(userId,
                                                                    dataFieldGUID,
                                                                    dataFieldGUIDParameterName,
                                                                    OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                    OpenMetadataAPIMapper.DISCOVERED_LINKED_DATA_FIELD_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.DISCOVERED_LINKED_DATA_FIELD_TYPE_NAME,
                                                                    null,
                                                                    OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                    0,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);

        if (relationships != null)
        {
            final String attachedEntityGUIDParameterName = "relationship.getEntityProxyXXX().getGUID()";

            List<B> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, dataFieldGUID, relationship);
                    EntityDetail entityDetail = super.getEntityFromRepository(userId,
                                                                              entityProxy.getGUID(),
                                                                              attachedEntityGUIDParameterName,
                                                                              OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                              null,
                                                                              null,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              effectiveTime,
                                                                              methodName);

                    if (entityDetail != null)
                    {
                        B bean = converter.getNewBean(beanClass, entityDetail, relationship, methodName);

                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Return a list of data field beans from a list of entity GUIDs.
     *
     * @param userId calling user
     * @param dataFieldGUIDs list of guids for entities that represent data fields
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of data field beans
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    private List<B> getDataFields(String       userId,
                                  List<String> dataFieldGUIDs,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        List<B> results = new ArrayList<>();

        if (dataFieldGUIDs != null)
        {
            for (String entityGUID : dataFieldGUIDs)
            {
                if (entityGUID != null)
                {
                    B bean = this.getDataField(userId,
                                               entityGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);

                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }



    /**
     * Return a specific data field stored in the metadata repositories.  This includes information from the
     * entity
     *
     * @param userId calling user
     * @param dataFieldGUID unique identifier of the data field
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return data field object
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the data field from the annotation store.
     */
    public B  getDataField(String  userId,
                           String  dataFieldGUID,
                           boolean forLineage,
                           boolean forDuplicateProcessing,
                           Date    effectiveTime,
                           String  methodName) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        final String   dataFieldGUIDParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, dataFieldGUIDParameterName, methodName);

        EntityDetail entity = super.getEntityFromRepository(userId,
                                                            dataFieldGUID,
                                                            dataFieldGUIDParameterName,
                                                            OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                            null,
                                                            null,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            supportedZones,
                                                            effectiveTime,
                                                            methodName);

        List<Relationship> relationships = super.getAllAttachmentLinks(userId,
                                                                       dataFieldGUID,
                                                                       dataFieldGUIDParameterName,
                                                                       OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);

        return converter.getNewComplexBean(beanClass, entity, relationships, methodName);
    }



    /**
     * Save a new data field as an entity.  The calling method will link it to its asset via the
     * discovery analysis report.
     *
     * @param userId identifier of calling user
     * @param externalSourceGUID unique identifier of the external source (null for local)
     * @param externalSourceName unique name of the external source (null for local)
     * @param parentEntityGUID unique identifier of the entity that the new data field will be attached to
     * @param parentEntityParameterName name of parameter supplying parentEntityGUID
     * @param parentEntityType type of entity that the new data field will be attached to
     * @param dataFieldName the name of the data field
     * @param dataFieldType the type of the data field
     * @param dataFieldDescription a description of the data field
     * @param dataFieldAliases any aliases associated with the data field
     * @param dataFieldSortOrder any sort order
     * @param defaultValue default value of the field
     * @param additionalProperties any additional properties
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of the data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    private String addDataField(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              parentEntityGUID,
                                String              parentEntityParameterName,
                                String              parentEntityType,
                                String              relationshipTypeGUID,
                                String              relationshipTypeName,
                                int                 dataFieldPosition,
                                String              dataFieldName,
                                String              dataFieldType,
                                String              dataFieldDescription,
                                List<String>        dataFieldAliases,
                                int                 dataFieldSortOrder,
                                String              defaultValue,
                                Map<String, String> additionalProperties,
                                boolean             forLineage,
                                boolean             forDuplicateProcessing,
                                Date                effectiveTime,
                                String              methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String dataFieldNameParameterName = "dataFieldName";
        final String dataFieldGUIDParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentEntityGUID, parentEntityParameterName, methodName);
        invalidParameterHandler.validateName(dataFieldName, dataFieldNameParameterName, methodName);

        String       assetGUID    = null;
        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              parentEntityGUID,
                                                              parentEntityParameterName,
                                                              parentEntityType,
                                                              true,
                                                              false,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              supportedZones,
                                                              effectiveTime,
                                                              methodName);

        if (anchorEntity != null)
        {
            assetGUID = anchorEntity.getGUID();
        }

        DataFieldBuilder builder = new DataFieldBuilder(dataFieldName,
                                                        dataFieldType,
                                                        dataFieldDescription,
                                                        dataFieldAliases,
                                                        dataFieldSortOrder,
                                                        defaultValue,
                                                        additionalProperties,
                                                        OpenMetadataAPIMapper.DATA_FIELD_TYPE_GUID,
                                                        OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                        null,
                                                        repositoryHelper,
                                                        serviceName,
                                                        serverName);

        if (assetGUID != null)
        {
            builder.setAnchors(userId, assetGUID, methodName);
        }

        String dataFieldGUID = this.createBeanInRepository(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           OpenMetadataAPIMapper.DATA_FIELD_TYPE_GUID,
                                                           OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                           builder,
                                                           effectiveTime,
                                                           methodName);

        if (dataFieldGUID != null)
        {
            InstanceProperties relationshipProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                                  null,
                                                                                                  OpenMetadataAPIMapper.DATA_FIELD_POSITION_PROPERTY_NAME,
                                                                                                  dataFieldPosition,
                                                                                                  methodName);
            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               parentEntityGUID,
                                               parentEntityParameterName,
                                               parentEntityType,
                                               dataFieldGUID,
                                               dataFieldGUIDParameterName,
                                               OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               relationshipTypeGUID,
                                               relationshipTypeName,
                                               relationshipProperties,
                                               effectiveTime,
                                               methodName);
        }

        return dataFieldGUID;
    }


    /**
     * Add a new data field to the Annotation store linked off of an annotation (typically SchemaAnalysisAnnotation).
     *
     * @param userId identifier of calling user
     * @param externalSourceGUID unique identifier of the external source (null for local)
     * @param externalSourceName unique name of the external source (null for local)
     * @param annotationGUID unique identifier of the annotation that the data field is to be linked to
     * @param dataFieldPosition the position of the data field in the schema (if there is ordering of fields)
     * @param dataFieldName the name of the data field
     * @param dataFieldType the type of the data field
     * @param dataFieldDescription a description of the data field
     * @param dataFieldAliases any aliases associated with the data field
     * @param dataFieldSortOrder any sort order
     * @param defaultValue default value of the field
     * @param additionalProperties any additional properties
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    public String  addDataFieldToDiscoveryReport(String              userId,
                                                 String              externalSourceGUID,
                                                 String              externalSourceName,
                                                 String              annotationGUID,
                                                 int                 dataFieldPosition,
                                                 String              dataFieldName,
                                                 String              dataFieldType,
                                                 String              dataFieldDescription,
                                                 List<String>        dataFieldAliases,
                                                 int                 dataFieldSortOrder,
                                                 String              defaultValue,
                                                 Map<String, String> additionalProperties,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime,
                                                 String              methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String annotationGUIDParameterName = "annotationGUID";

        return this.addDataField(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 annotationGUID,
                                 annotationGUIDParameterName,
                                 OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME,
                                 OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_GUID,
                                 OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_NAME,
                                 dataFieldPosition,
                                 dataFieldName,
                                 dataFieldType,
                                 dataFieldDescription,
                                 dataFieldAliases,
                                 dataFieldSortOrder,
                                 defaultValue,
                                 additionalProperties,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);
    }


    /**
     * Add a new data field and link it to an existing data field.
     *
     * @param userId identifier of calling user
     * @param externalSourceGUID unique identifier of the external source (null for local)
     * @param externalSourceName unique name of the external source (null for local)
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param dataFieldPosition the position of the data field in the schema (if there is ordering of fields)
     * @param dataFieldName the name of the data field
     * @param dataFieldType the type of the data field
     * @param dataFieldDescription a description of the data field
     * @param dataFieldAliases any aliases associated with the data field
     * @param dataFieldSortOrder any sort order
     * @param defaultValue default value of the field
     * @param additionalProperties any additional properties
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of new data field
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    public String  addDataFieldToDataField(String              userId,
                                           String              externalSourceGUID,
                                           String              externalSourceName,
                                           String              parentDataFieldGUID,
                                           int                 dataFieldPosition,
                                           String              dataFieldName,
                                           String              dataFieldType,
                                           String              dataFieldDescription,
                                           List<String>        dataFieldAliases,
                                           int                 dataFieldSortOrder,
                                           String              defaultValue,
                                           Map<String, String> additionalProperties,
                                           boolean             forLineage,
                                           boolean             forDuplicateProcessing,
                                           Date                effectiveTime,
                                           String              methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String   parentDataFieldGUIDParameterName = "parentDataFieldGUID";

        return this.addDataField(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 parentDataFieldGUID,
                                 parentDataFieldGUIDParameterName,
                                 OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                 OpenMetadataAPIMapper.DISCOVERED_NESTED_DATA_FIELD_TYPE_GUID,
                                 OpenMetadataAPIMapper.DISCOVERED_NESTED_DATA_FIELD_TYPE_NAME,
                                 dataFieldPosition,
                                 dataFieldName,
                                 dataFieldType,
                                 dataFieldDescription,
                                 dataFieldAliases,
                                 dataFieldSortOrder,
                                 defaultValue,
                                 additionalProperties,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);
    }




    /**
     * Add a new data field and link it to an existing data field.
     *
     * @param userId identifier of calling user
     * @param externalSourceGUID unique identifier of the external source (null for local)
     * @param externalSourceName unique name of the external source (null for local)
     * @param linkFromDataFieldGUID unique identifier of the data field that is at end 1 of the relationship
     * @param relationshipEnd the logical end of the relationship.  Use 0 if this does not make sense.
     * @param relationshipTypeName the name of this relationship between data fields.
     * @param additionalProperties any additional properties
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    public void    linkDataFields(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              linkFromDataFieldGUID,
                                  String              linkToDataFieldGUID,
                                  int                 relationshipEnd,
                                  String              relationshipTypeName,
                                  Map<String, String> additionalProperties,
                                  boolean             forLineage,
                                  boolean             forDuplicateProcessing,
                                  Date                effectiveTime,
                                  String              methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   linkFromDataFieldGUIDParameterName = "linkFromDataFieldGUID";
        final String   linkToDataFieldGUIDParameterName = "linkToDataFieldGUID";

        InstanceProperties relationshipProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                              null,
                                                                                              OpenMetadataAPIMapper.RELATIONSHIP_END_PROPERTY_NAME,
                                                                                              relationshipEnd,
                                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              relationshipProperties,
                                                                              OpenMetadataAPIMapper.RELATIONSHIP_TYPE_NAME_PROPERTY_NAME,
                                                                              relationshipTypeName,
                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                 relationshipProperties,
                                                                                 OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                 additionalProperties,
                                                                                 methodName);
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  linkFromDataFieldGUID,
                                  linkFromDataFieldGUIDParameterName,
                                  OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                  linkToDataFieldGUID,
                                  linkToDataFieldGUIDParameterName,
                                  OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.DISCOVERED_LINKED_DATA_FIELD_TYPE_GUID,
                                  OpenMetadataAPIMapper.DISCOVERED_LINKED_DATA_FIELD_TYPE_NAME,
                                  relationshipProperties,
                                  null,
                                  null,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Replace the current properties of a data field.
     *
     * @param userId identifier of calling user
     * @param externalSourceGUID unique identifier of the external source (null for local)
     * @param externalSourceName unique name of the external source (null for local)
     * @param dataFieldGUID unique identifier of the data field
     * @param isMergeUpdate flag to indicate whether all properties should be updated, or just the properties specified.
     * @param dataFieldName the name of the data field
     * @param dataFieldType the type of the data field
     * @param dataFieldDescription a description of the data field
     * @param dataFieldAliases any aliases associated with the data field
     * @param dataFieldSortOrder any sort order
     * @param defaultValue default value of the field
     * @param additionalProperties any additional properties
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the data field in the annotation store.
     */
    public void  updateDataField(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              dataFieldGUID,
                                 boolean             isMergeUpdate,
                                 String              dataFieldName,
                                 String              dataFieldType,
                                 String              dataFieldDescription,
                                 List<String>        dataFieldAliases,
                                 int                 dataFieldSortOrder,
                                 String              defaultValue,
                                 Map<String, String> additionalProperties,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 boolean             forLineage,
                                 boolean             forDuplicateProcessing,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   dataFieldGUIDParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);

        DataFieldBuilder builder = new DataFieldBuilder(dataFieldName,
                                                        dataFieldType,
                                                        dataFieldDescription,
                                                        dataFieldAliases,
                                                        dataFieldSortOrder,
                                                        defaultValue,
                                                        additionalProperties,
                                                        OpenMetadataAPIMapper.DATA_FIELD_TYPE_GUID,
                                                        OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                        null,
                                                        repositoryHelper,
                                                        serviceName,
                                                        serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        super.updateBeanInRepository(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     dataFieldGUID,
                                     dataFieldGUIDParameterName,
                                     OpenMetadataAPIMapper.DATA_FIELD_TYPE_GUID,
                                     OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                     forLineage,
                                     forDuplicateProcessing,
                                     supportedZones,
                                     builder.getInstanceProperties(methodName),
                                     isMergeUpdate,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Remove a data field from the metadata repositories.
     *
     * @param userId identifier of calling user
     * @param externalSourceGUID unique identifier of the external source (null for local)
     * @param externalSourceName unique name of the external source (null for local)
     * @param dataFieldGUID unique identifier of the data field
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the data field from the annotation store.
     */
    public void  deleteDataField(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  dataFieldGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String dataFieldGUIDParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, dataFieldGUIDParameterName, methodName);

        super.deleteBeanInRepository(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     dataFieldGUID,
                                     dataFieldGUIDParameterName,
                                     OpenMetadataAPIMapper.DATA_FIELD_TYPE_GUID,
                                     OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                     null,
                                     null,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }
}
