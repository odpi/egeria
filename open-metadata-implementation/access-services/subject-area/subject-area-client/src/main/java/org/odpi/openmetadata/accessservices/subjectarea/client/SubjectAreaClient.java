/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * @param <T> Glossary artifact type for supplied and return.
 * Interface describing common methods to client working with Subject area resources.
 */
public interface SubjectAreaClient<T> {

    FindRequest EMPTY_FIND_REQUEST = new FindRequest();

    /**
     * Get Glossary artifact by guid
     *
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param guid   unique identifier of the Glossary artifact.
     * @return found Glossary artifacts of the T type.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T getByGUID(String userId, String guid) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException;

    /**
     * Create a Glossary artifact. To create, you must pass the created object and specify a unique user identifier.
     *
     * @param userId   unique identifier for requesting user, under which the request is performed.
     * @param supplied Glossary artifact to create.
     * @return created Glossary artifact.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T create(String userId, T supplied) throws InvalidParameterException,
                                               PropertyServerException,
                                               UserNotAuthorizedException;

    /**
     * Request to find all Glossary artifacts of the type T.
     * Be aware that getting all objects may incur a big performance hit when there are many objects.
     *
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @return list all Glossary artifacts of the T type.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    default List<T> findAll(String userId) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException
    {
       return find(userId, EMPTY_FIND_REQUEST);
    }

    /**
     * Request to find Glossary artifacts of the type T.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed.
     * @param findRequest information Glossary artifact for find calls.
     * @return list Glossary artifacts of the T type relevant in the findRequest information.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
   List<T> find(String userId, FindRequest findRequest) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;

    /**
     * Update or replace a Glossary artifact.
     *
     * @param guid      unique identifier of the Glossary artifact.
     * @param userId    unique identifier for requesting user, under which the request is performed.
     * @param supplied  Glossary artifact to be updated or replaced.
     * @param isReplace flag to indicate that this update is a replace.
     * @return updated Glossary artifact.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T update(String userId, String guid, T supplied, boolean isReplace) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;

    /**
     * Replace a Glossary artifact. This means to override all the existing attributes with the supplied attributes.
     *
     * @param guid     unique identifier of the Glossary artifact.
     * @param userId   unique identifier for requesting user, under which the request is performed.
     * @param supplied Glossary artifact to be replaced.
     * @return replaced Glossary artifact.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    default T replace(String userId, String guid, T supplied) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return update(userId, guid, supplied, true);
    }

    /**
     * Update a Glossary artifact. This means to update the object with any non-null attributes from the supplied Glossary artifact.
     *
     * @param guid     unique identifier of the Glossary artifact.
     * @param userId   unique identifier for requesting user, under which the request is performed.
     * @param supplied Glossary artifact to be updated.
     * @return updated Glossary artifact.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    default T update(String userId, String guid, T supplied) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return update(userId, guid, supplied, false);
    }

    /**
     * Delete a Glossary artifact.
     *
     * @param guid    unique identifier of the Glossary artifact.
     * @param userId  unique identifier for requesting user, under which the request is performed.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    void delete(String userId, String guid, boolean isPurge) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;

    /**
     * Purge a Glossary artifact.
     *
     * @param guid    unique identifier of the Glossary artifact.
     * @param userId  unique identifier for requesting user, under which the request is performed.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    default void purge(String userId, String guid) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        delete(userId, guid, true);
    }

    /**
     * Soft delete a Glossary artifact.
     *
     * @param guid    unique identifier of the Glossary artifact.
     * @param userId  unique identifier for requesting user, under which the request is performed.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    default void delete(String userId, String guid) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        delete(userId, guid, false);
    }

    /**
     * Restore of a soft deleted Glossary artifact.
     *
     * @param guid    unique identifier of the Glossary artifact.
     * @param userId  unique identifier for requesting user, under which the request is performed.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T restore(String userId, String guid) throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException;
}