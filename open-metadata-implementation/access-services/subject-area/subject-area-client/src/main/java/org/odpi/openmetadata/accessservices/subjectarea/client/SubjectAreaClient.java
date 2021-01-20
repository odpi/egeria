/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * @param <T> Glossary Artifact type for supplied and return.
 * Interface describing common methods to client working with Subject area resources.
 *
 * This is the Subject Area client API, for use by the subject area expert.
 * This API exposes SCRUD (search, create, read ,update, delete) operations that can be performed on a Glossary Artifact.
 */
public interface SubjectAreaClient<T> {

    FindRequest EMPTY_FIND_REQUEST = new FindRequest();

    /**
     * Get Glossary Artifact by guid
     *
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param guid   unique identifier of the Glossary Artifact.
     * @return found Glossary Artifact of the T type.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T getByGUID(String userId, String guid) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException;

    /**
     * Create a Glossary Artifact. To create, you must pass the created object and specify a unique user identifier.
     *
     * @param userId   unique identifier for requesting user, under which the request is performed.
     * @param supplied Glossary Artifact to create.
     * @return created Glossary Artifact.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T create(String userId, T supplied) throws InvalidParameterException,
                                               PropertyServerException,
                                               UserNotAuthorizedException;

    /**
     * Request to find all Glossary Artifacts of the type T.
     * Be aware that getting all objects may incur a big performance hit when there are many objects.
     *
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @return list all Glossary Artifacts of the T type.
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
     * Request to find all Glossary Artifacts of the type T.
     * Be aware that getting all objects may incur a big performance hit when there are many objects.
     *
     * @param userId unique identifier for requesting user, under which the request is performed.
     * @param maximumPageSizeOnRestCall maximum page size that can be used on rest calls, null and 0 mean no limit set.
     * @return list all Glossary Artifacts of the T type.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    default List<T> findAll(String userId, Integer maximumPageSizeOnRestCall) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException
    {
        return find(userId, EMPTY_FIND_REQUEST, maximumPageSizeOnRestCall);
    }

    /**
     * Request to find Glossary Artifacts of the type T.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed.
     * @param findRequest information Glossary Artifact for find calls.
     * @return list Glossary Artifacts of the T type relevant in the findRequest information.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    List<T> find(String userId, FindRequest findRequest) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException;

    /**
     * Request to find Glossary Artifacts of the type T.
     *
     * The downstream server is likely to have a maximum Page Size is will accept, the client can interrogate this value
     * and include it on the maximumPageSizeOnRestCall parameter. This API will ensure will issue multiple rest calls if required
     * to ensure requests only request the supported max page size.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed.
     * @param findRequest information Glossary Artifact for find calls.
     * @param maximumPageSizeOnRestCall maximum page size that can be used on rest calls, null and 0 mean no limit set.
     * @return list Glossary Artifacts of the T type relevant in the findRequest information.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
   List<T> find(String userId, FindRequest findRequest, Integer maximumPageSizeOnRestCall) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;

    /**
     * Update or replace a Glossary Artifact.
     *
     * @param guid      unique identifier of the Glossary Artifact.
     * @param userId    unique identifier for requesting user, under which the request is performed.
     * @param supplied  Glossary Artifact to be updated or replaced.
     * @param isReplace flag to indicate that this update is a replace.
     * @return updated Glossary Artifact.
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T update(String userId, String guid, T supplied, boolean isReplace) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;

    /**
     * Replace a Glossary Artifact. This means to override all the existing attributes with the supplied attributes.
     *
     * @param guid     unique identifier of the Glossary Artifact.
     * @param userId   unique identifier for requesting user, under which the request is performed.
     * @param supplied Glossary Artifact to be replaced.
     * @return replaced Glossary Artifact.
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
     * Update a Glossary Artifact. This means to update the object with any non-null attributes from the supplied Glossary Artifact.
     *
     * @param guid     unique identifier of the Glossary Artifact.
     * @param userId   unique identifier for requesting user, under which the request is performed.
     * @param supplied Glossary Artifact to be updated.
     * @return updated Glossary Artifact.
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
     * Delete a Glossary Artifact.
     *
     * @param guid    unique identifier of the Glossary Artifact.
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
     * Purge a Glossary Artifact.
     *
     * @param guid    unique identifier of the Glossary Artifact.
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
     * Soft delete a Glossary Artifact.
     *
     * @param guid    unique identifier of the Glossary Artifact.
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
     * Restore of a soft deleted Glossary Artifact.
     *
     * @param guid    unique identifier of the Glossary Artifact.
     * @param userId  unique identifier for requesting user, under which the request is performed.
     * @return restored glossary Artifact
     *
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    T restore(String userId, String guid) throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException;
}