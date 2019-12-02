/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.CertificationConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.CertificationMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Certification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * CertificationHandler manages Certification objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Certification entities through the OMRSRepositoryConnector.
 */
public class CertificationHandler extends AttachmentHandlerBase
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public CertificationHandler(String                  serviceName,
                                String                  serverName,
                                InvalidParameterHandler invalidParameterHandler,
                                RepositoryHandler       repositoryHandler,
                                OMRSRepositoryHelper    repositoryHelper,
                                LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);
    }


    /**
     * Count the number of Certifications attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countCertifications(String   userId,
                                   String   anchorGUID,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      anchorGUID,
                                      ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                      CertificationMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID,
                                      CertificationMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                      methodName);
    }


    /**
     * Return the Certifications attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the feedback is attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<Certification>  getCertifications(String   userId,
                                                  String   anchorGUID,
                                                  int      startingFrom,
                                                  int      pageSize,
                                                  String   methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String guidParameterName      = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<Relationship>  relationships = repositoryHandler.getPagedRelationshipsByType(userId,
                                                                                          anchorGUID,
                                                                                          ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                          CertificationMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID,
                                                                                          CertificationMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                                                                          startingFrom,
                                                                                          queryPageSize,
                                                                                          methodName);
        if (relationships != null)
        {
            List<Certification>  results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = relationship.getEntityTwoProxy();

                    if (entityProxy != null)
                    {
                        final String  entityParameterName = "entityProxyTwo.getGUID";

                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                entityProxy.getGUID(),
                                                                                entityParameterName,
                                                                                CertificationMapper.CERTIFICATION_TYPE_TYPE_NAME,
                                                                                methodName);

                        CertificationConverter converter = new CertificationConverter(entity,
                                                                                      relationship,
                                                                                      repositoryHelper,
                                                                                      serviceName);
                        results.add(converter.getBean());
                        // TODO external reference link see setLink();
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

        return null;
    }

}
