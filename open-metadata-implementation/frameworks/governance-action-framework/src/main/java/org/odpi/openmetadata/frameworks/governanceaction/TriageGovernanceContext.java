/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TriageGovernanceContext provides access to details of the triage request and associated governance action plus with access to the
 * metadata store.
 *
 * A triage governance action service typically assesses the request source elements and makes a choice to
 * call an external triage service, create a to-do for a profile in the open metadata ecosystem,
 * or directly completing the associated governance action with a decision for the completion status and guards that are used to
 * determine the next step to proceed.
 */
public class TriageGovernanceContext extends GovernanceContext
{
    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
     *
     * @param userId calling user
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param openMetadataStore client to the metadata store for use by the governance action service
     */
    public TriageGovernanceContext(String                     userId,
                                   String                     governanceActionGUID,
                                   String                     requestType,
                                   Map<String, String>        requestParameters,
                                   List<RequestSourceElement> requestSourceElements,
                                   List<ActionTargetElement>  actionTargetElements,
                                   OpenMetadataClient openMetadataStore)
    {
        super(userId, governanceActionGUID, requestType, requestParameters, requestSourceElements, actionTargetElements, openMetadataStore);
    }


    /**
     * Create a To Do request for someone to work on.
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
    public String openToDo(String toDoQualifiedName,
                           String title,
                           String instructions,
                           int    priority,
                           Date   dueDate,
                           String assignTo) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        final String methodName = "openToDo";

        final String todoTypeName             = "ToDo";
        final String personRoleTypeName       = "PersonRole";
        final String actionAssignmentTypeName = "ActionAssignment";

        final String qualifiedNamePropertyName = "qualifiedName";
        final String titlePropertyName         = "name";
        final String instructionsPropertyName  = "description";
        final String priorityPropertyName      = "priority";
        final String dueDatePropertyName       = "dueTime";
        final String statusPropertyName        = "status";
        final String statusPropertyTypeName    = "ToDoStatus";
        final String openEnumPropertyValue     = "Open";

        final String toDoQualifiedNameParameterName = "toDoQualifiedName";
        final String assignToParameterName          = "assignTo";

        propertyHelper.validateMandatoryName(toDoQualifiedName, toDoQualifiedNameParameterName, methodName);
        propertyHelper.validateMandatoryName(assignTo, assignToParameterName, methodName);

        SearchProperties        searchProperties = new SearchProperties();
        List<PropertyCondition> conditions = new ArrayList<>();
        PropertyCondition       condition = new PropertyCondition();
        PrimitivePropertyValue  primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(assignTo);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        condition.setProperty(qualifiedNamePropertyName);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitivePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        /*
         * Validate that there is a person role to assign the "to do" to
         */
        List<OpenMetadataElement> personRoleMatches = openMetadataStore.findMetadataElements(personRoleTypeName,
                                                                                             null,
                                                                                             searchProperties,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             0,
                                                                                             0);

        if ((personRoleMatches == null) || personRoleMatches.isEmpty())
        {
            throw new InvalidParameterException(GAFErrorCode.UNKNOWN_ELEMENT.getMessageDefinition(toDoQualifiedName,
                                                                                                  toDoQualifiedNameParameterName,
                                                                                                  methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                toDoQualifiedNameParameterName);
        }
        else if (personRoleMatches.size() > 1)
        {
            List<String> matchingGUIDs = new ArrayList<>();

            for (OpenMetadataElement element : personRoleMatches)
            {
                if (element != null)
                {
                    matchingGUIDs.add(element.getElementGUID());
                }
            }

            throw new InvalidParameterException(GAFErrorCode.DUPLICATE_ELEMENT.getMessageDefinition(toDoQualifiedName,
                                                                                                    toDoQualifiedNameParameterName,
                                                                                                    methodName,
                                                                                                    matchingGUIDs.toString()),
                                                this.getClass().getName(),
                                                methodName,
                                                toDoQualifiedNameParameterName);
        }

        OpenMetadataElement personRoleElement = personRoleMatches.get(0);
        String personRoleGUID = null;

        if ((personRoleElement != null) && (personRoleElement.getElementGUID() != null))
        {
            personRoleGUID = personRoleElement.getElementGUID();
        }

        /*
         * Create the to do entity
         */
        ElementProperties properties = propertyHelper.addStringProperty(null, qualifiedNamePropertyName, toDoQualifiedName);

        if (title != null)
        {
            properties = propertyHelper.addStringProperty(properties, titlePropertyName, title);
        }

        if (instructions != null)
        {
            properties = propertyHelper.addStringProperty(properties, instructionsPropertyName, instructionsPropertyName);
        }

        if (dueDate != null)
        {
            properties = propertyHelper.addDateProperty(properties, dueDatePropertyName, dueDate);
        }

        properties = propertyHelper.addIntProperty(properties, priorityPropertyName, priority);
        properties = propertyHelper.addEnumProperty(properties, statusPropertyName, statusPropertyTypeName, openEnumPropertyValue);

        String todoGUID = openMetadataStore.createMetadataElementInStore(todoTypeName, ElementStatus.ACTIVE, null, null, properties, null);

        /*
         * Link the "to do" and the person role
         */
        openMetadataStore.createRelatedElementsInStore(actionAssignmentTypeName, personRoleGUID, todoGUID, null, null, null);

        return todoGUID;
    }
}
