/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile;

import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDo;

public interface ToDoManagementInterface
{
    String   createToDo(String userId,
                        String personalRoleGUID,
                        String text);

    ToDo getToDo(String userId,
                 String toDoGUID);

    void updateToDo(String userId,
                    ToDo toDo);


    void deleteToDo(String userId,
                    String toDoGUID);


}
