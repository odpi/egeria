/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;

/**
 * TriageGovernanceContext provides access to details of the triage request and associated governance action plus with access to the
 * metadata store.
 *
 * A triage governance action service typically assesses the request source elements and makes a choice to
 * call an external triage service, create a to-do for a profile in the open metadata ecosystem,
 * or directly completing the associated governance action with a decision for the completion status and guards that are used to
 * determine the next step to proceed.
 */
public interface TriageGovernanceContext extends GovernanceContext
{
    /**
     * Create a "To Do" request for someone to work on.
     *
     * @param toDoQualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param assignTo qualified name of the PersonRole element for the recipient
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a to do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    String openToDo(String toDoQualifiedName,
                    String title,
                    String instructions,
                    int    priority,
                    Date   dueDate,
                    String assignTo) throws InvalidParameterException,
                                            UserNotAuthorizedException,
                                            PropertyServerException;
}
