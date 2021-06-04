/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * @param <E> inherited from {@link Node} type
 * Interface for Subject Area client node operations
 */
public interface SubjectAreaNodeClient<E extends Node> extends SubjectAreaClient<E> {

    /**
     * Request to search relationships for current node.
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param guid   unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls.
     * @return list relationships relevant in the findRequest information.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    List<Relationship> getRelationships(String userId,
                                        String guid,
                                        FindRequest findRequest) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;
    /**
     * Request to search relationships for current node.
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param guid   unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls.
     * @param maximumPageSizeOnRestCall maximum page size to use on rest calls.
     * @return list relationships relevant in the findRequest information.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    List<Relationship> getRelationships(String userId,
                                        String guid,
                                        FindRequest findRequest,
                                        Integer maximumPageSizeOnRestCall) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;
    /**
     * Request to search all relationships for current node.
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param guid   unique identifier of the object to which the found objects should relate.
     * @return list all relationships.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    default List<Relationship> getAllRelationships(String userId, String guid) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        return getRelationships(userId, guid, new FindRequest());
    }
}
