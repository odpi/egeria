/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface SubjectAreaEntityClient<E extends Node> extends SubjectAreaClient<E> {

    List<Line> getRelationships(String userId,
                                String guid,
                                FindRequest findRequest) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;

    default List<Line> getAllRelationships(String userId, String guid) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        return getRelationships(userId, guid, new FindRequest());
    }
}
