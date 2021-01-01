/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TriageGovernanceContext provides access to details of the triage request and associated governance action plus with access to the
 * metadata store.
 *
 * A triage governance service typically assesses the request source elements and makes a choice to
 * call an external triage service, create a to-do for a profile in the open metadata ecosystem,
 * or directly completing the associated governance action with a decision for the completion status and guards that are used to
 * determine the next step to proceed.
 */
public abstract class TriageGovernanceContext extends GovernanceContext
{
    /**
     * Constructor sets up the key parameters for processing the request to the governance service.
     *
     * @param userId calling user
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance service
     * @param requestSourceElements metadata elements associated with the request to the governance service
     * @param actionTargetElements metadata elements that need to be worked on by the governance service
     * @param openMetadataStore client to the metadata store for use by the governance service
     */
    public TriageGovernanceContext(String                     userId,
                                   String                     requestType,
                                   Map<String, String>        requestParameters,
                                   List<RequestSourceElement> requestSourceElements,
                                   List<ActionTargetElement>  actionTargetElements,
                                   OpenMetadataStore          openMetadataStore)
    {
        super(userId, requestType, requestParameters, requestSourceElements, actionTargetElements, openMetadataStore);
    }


    /**
     *
     * @param toDoQualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param assignTo qualified name of the PersonRole element for the recipient
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance service is not authorized to create a to do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public abstract String openToDo(String toDoQualifiedName,
                                    String title,
                                    String instructions,
                                    int    priority,
                                    Date   dueDate,
                                    String assignTo) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;

}
