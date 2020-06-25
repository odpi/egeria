/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface SubjectAreaClient<T> {

    FindRequest EMPTY_FIND_REQUEST = new FindRequest();

    T getByGUID(String userId, String guid) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException;

    T create(String userId, T supplied) throws InvalidParameterException,
                                               PropertyServerException,
                                               UserNotAuthorizedException;

    default List<T> findAll(String userId) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException
    {
       return find(userId, EMPTY_FIND_REQUEST);
    }

   List<T> find(String userId, FindRequest findRequest) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;

    T update(String userId, String guid, T supplied, boolean isReplace) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;

    default T replace(String userId, String guid, T supplied) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return update(userId, guid, supplied, true);
    }

    default T update(String userId, String guid, T supplied) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return update(userId, guid, supplied, false);
    }

    void delete (String userId, String guid, boolean isPurge) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;

    default void purge(String userId, String guid) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        delete(userId, guid, true);
    }

    default void delete(String userId, String guid) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        delete(userId, guid, false);
    }

    T restore(String userId, String guid) throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException;
}