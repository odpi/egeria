/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RelatedAssetHandler manages B objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 */
public class RelatedAssetHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler with information needed to work with B objects.
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
    public RelatedAssetHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Return the assets and relationship properties attached to an element by the specified relationship type.
     * If all related assets are required then specify null for the relationship type GUID and name.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the asset that the related assets are attached to
     * @param elementGUIDParameterName parameter passing the elementGUID
     * @param elementTypeName name of the type of the starting element
     * @param relationshipTypeGUID unique identifier for relationship type
     * @param relationshipTypeName unique name for relationship type
     * @param serviceSupportedZones override the default supported zones
     * @param relatedAssetsTypeName expected type of asset at the end of the relationship
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getRelatedAssets(String       userId,
                                     String       elementGUID,
                                     String       elementGUIDParameterName,
                                     String       elementTypeName,
                                     String       relationshipTypeGUID,
                                     String       relationshipTypeName,
                                     String       relatedAssetsTypeName,
                                     List<String> serviceSupportedZones,
                                     int          startingEnd,
                                     int          startFrom,
                                     int          pageSize,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        elementGUID,
                                                                        elementGUIDParameterName,
                                                                        elementTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   startingEntity,
                                                                   elementGUIDParameterName,
                                                                   elementTypeName,
                                                                   relationshipTypeGUID,
                                                                   relationshipTypeName,
                                                                   null,
                                                                   OpenMetadataType.ASSET.typeName,
                                                                   0,
                                                                   null,
                                                                   null,
                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
                                                                   methodName);

        List<B> results = new ArrayList<>();

        final String entityGUIDParameterName = "entityProxy.getGUID()";

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if ((startingEnd == 0)
                                || ((startingEnd == 1) && (startingEntity.getGUID().equals(relationship.getEntityOneProxy().getGUID())))
                                || ((startingEnd == 2) && (startingEntity.getGUID().equals(relationship.getEntityTwoProxy().getGUID()))))
                    {
                        EntityProxy otherEnd = repositoryHandler.getOtherEnd(startingEntity.getGUID(), elementTypeName, relationship, 0, methodName);

                        if ((otherEnd != null) && (otherEnd.getType() != null) && (repositoryHelper.isTypeOf(serviceName,
                                                                                                             otherEnd.getType().getTypeDefName(),
                                                                                                             relatedAssetsTypeName)))
                        {
                            try
                            {
                                EntityDetail entity = this.getEntityFromRepository(userId,
                                                                                   otherEnd.getGUID(),
                                                                                   entityGUIDParameterName,
                                                                                   otherEnd.getType().getTypeDefName(),
                                                                                   null,
                                                                                   null,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   serviceSupportedZones,
                                                                                   effectiveTime,
                                                                                   methodName);

                                if (entity != null)
                                {
                                    B bean = converter.getNewBean(beanClass, entity, relationship, methodName);

                                    if (bean != null)
                                    {
                                        results.add(bean);
                                    }
                                }
                            }
                            catch (UserNotAuthorizedException notVisible)
                            {
                                // skip this one
                            }
                        }
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
