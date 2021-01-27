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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * RelatedAssetHandler manages B objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 */
public class RelatedAssetHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the asset handler with information needed to work with B objects.
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
     * Return the count of related assets.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the asset that the related assets are attached to
     * @param elementGUIDParameterName parameter passing the elementGUID
     * @param elementTypeName name of the type of the starting element
     * @param relationshipTypeGUID unique identifier for relationship type
     * @param relationshipTypeName unique name for relationship type
     * @param serviceSupportedZones override the default supported zones.
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getRelatedAssetCount(String       userId,
                                    String       elementGUID,
                                    String       elementGUIDParameterName,
                                    String       elementTypeName,
                                    String       relationshipTypeGUID,
                                    String       relationshipTypeName,
                                    List<String> serviceSupportedZones,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        List<B>  relatedAssets = this.getRelatedAssets(userId,
                                                       elementGUID,
                                                       elementGUIDParameterName,
                                                       elementTypeName,
                                                       relationshipTypeGUID,
                                                       relationshipTypeName,
                                                       serviceSupportedZones,
                                                       0,
                                                       invalidParameterHandler.getMaxPagingSize(),
                                                       methodName);

        if (relatedAssets == null)
        {
            return 0;
        }
        else
        {
            return relatedAssets.size();
        }
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
     * @param serviceSupportedZones override the default supported zones.
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
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
                                     List<String> serviceSupportedZones,
                                     int          startFrom,
                                     int          pageSize,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   elementTypeName,
                                                                   relationshipTypeGUID,
                                                                   relationshipTypeName,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        List<B> results = new ArrayList<>();

        final String entityGUIDParameterName = "entityProxy.getGUID()";
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy otherEnd = repositoryHandler.getOtherEnd(elementGUID, elementTypeName, relationship, methodName);

                    if ((otherEnd != null) && (otherEnd.getType() != null))
                    {
                        try
                        {
                            EntityDetail entity = this.getEntityFromRepository(userId,
                                                                               otherEnd.getGUID(),
                                                                               entityGUIDParameterName,
                                                                               otherEnd.getType().getTypeDefName(),
                                                                               null,
                                                                               null,
                                                                               serviceSupportedZones,
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
