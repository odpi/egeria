/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.MeaningConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.MeaningMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ExternalIdentifier;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;


/**
 * MeaningHandler retrieves a Meaning bean from the GlossaryTerm entity.
 */
public class MeaningHandler extends AttachmentHandlerBase
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
    public MeaningHandler(String                  serviceName,
                          String                  serverName,
                          InvalidParameterHandler invalidParameterHandler,
                          RepositoryHandler       repositoryHandler,
                          OMRSRepositoryHelper    repositoryHelper,
                          LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);
    }


    /**
     * Return the meanings attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the identifier is attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<Meaning> getMeanings(String   userId,
                                     String   anchorGUID,
                                     int      startingFrom,
                                     int      pageSize,
                                     String   methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        // todo
        return null;
    }


    /**
     * Retrieve the requested endpoint object.
     *
     * @param userId       calling user
     * @param guid         unique identifier.
     * @return Meaning bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Meaning getMeaning(String userId,
                              String guid) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String methodName        = "getMeaning";
        final String guidParameterName = "guid";

        EntityDetail retrievedEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         guid,
                                                                         guidParameterName,
                                                                         MeaningMapper.MEANING_TYPE_NAME,
                                                                         methodName);


        MeaningConverter converter = new MeaningConverter(retrievedEntity,
                                                          repositoryHelper,
                                                          serviceName);

        return converter.getBean();
    }
}
