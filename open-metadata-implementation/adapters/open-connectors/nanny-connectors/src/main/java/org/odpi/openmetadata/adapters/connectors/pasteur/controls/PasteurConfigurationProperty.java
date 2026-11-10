/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.pasteur.controls;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PasteurConfigurationProperty provides definitions for the configuration properties used with the
 * Pasteur Action History Cleanup integration connector.
 */
public enum PasteurConfigurationProperty
{
    /**
     * The minimum number of days after completion before each type of action is deleted.
     */
    DAYS_TO_CLEAN_UP("daysToCleanUp",
                     "A map from the name of a type of action (Action or one of its subtypes) to the minimum number of days after its completion before a finished action of that type is deleted (using the delete method set in the deleteMethod configuration property).  An action uses the entry for its own type or, if there is none, the entry for its nearest supertype.  An action whose type, and whose supertypes, have no entry is never deleted.  Zero means the action is deleted on the first refresh after it finishes.  The completion time is taken from the action's completionTime property, or from the time it was last updated if that is not set.",
                     DataType.MAP_STRING_LONG.getDisplayName(),
                     "{\"EngineAction\": 7, \"GovernanceActionProcessInstance\": 14, \"ToDo\": 90}",
                     false),

    /**
     * The completion statuses that make an action eligible for clean-up.
     */
    COMPLETION_STATUSES("completionStatuses",
                        "A comma-separated list of the activity statuses (the symbolic names of the ActivityStatus enum, such as COMPLETED) that show an action has finished and may be cleaned up once it is old enough.  The default is COMPLETED, INVALID and FAILED.  Actions with any other status - including CANCELLED, IGNORED and ABANDONED unless they are added here - are kept.",
                        DataType.ARRAY_STRING.getDisplayName(),
                        "COMPLETED,INVALID,FAILED",
                        false),

    /**
     * The delete method used to remove the finished actions.
     */
    DELETE_METHOD("deleteMethod",
                  "The delete method used to remove a finished action, along with the elements anchored to it: the symbolic name of one of the values of the DeleteMethod enum.  The default, LOOK_FOR_LINEAGE, archives an action that has lineage relationships, so lineage queries can still find it, and soft-deletes the others.  ARCHIVE archives every action, SOFT_DELETE soft-deletes every action and PURGE removes them permanently, so they cannot be restored.",
                  DataType.STRING.getDisplayName(),
                  "LOOK_FOR_LINEAGE",
                  false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isPlaceholder;


    /**
     * The completion statuses used when the completionStatuses configuration property is not set.
     */
    public static final List<String> DEFAULT_COMPLETION_STATUSES = List.of(ActivityStatus.COMPLETED.name(),
                                                                           ActivityStatus.INVALID.name(),
                                                                           ActivityStatus.FAILED.name());


    /**
     * Return the days to clean up set by the core content pack.  The engine actions are the bulk of the action
     * history, and each has served its purpose once the steward has seen that it has finished, so they go after a
     * week.  A governance action process instance anchors the engine actions it ran, and deleting it removes
     * them too, so it is kept for a little longer to give a complete picture of each recent process.  To dos
     * are created and finished by people, so they are kept for a quarter.  The other types of action, such as
     * meetings, notifications and reviews, are kept until someone adds them.
     *
     * @return map of type name to days
     */
    public static Map<String, Long> getDefaultDaysToCleanUp()
    {
        Map<String, Long> daysToCleanUp = new HashMap<>();

        daysToCleanUp.put(OpenMetadataType.ENGINE_ACTION.typeName, 7L);
        daysToCleanUp.put(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName, 14L);
        daysToCleanUp.put(OpenMetadataType.TO_DO.typeName, 90L);

        return daysToCleanUp;
    }


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the configuration property
     * @param description description of the configuration property
     * @param dataType type of value of the configuration property
     * @param example example of the configuration property
     * @param isPlaceholder is this also used as a placeholder property?
     */
    PasteurConfigurationProperty(String  name,
                                 String  description,
                                 String  dataType,
                                 String  example,
                                 boolean isPlaceholder)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.example       = example;
        this.isPlaceholder = isPlaceholder;
    }


    /**
     * Return the name of the configuration property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the configuration property.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the configuration property.
     *
     * @return string data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property to help users understand how to set it up.
     *
     * @return string example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Return whether this value is also used as a placeholder property.
     *
     * @return boolean
     */
    public boolean isPlaceholder()
    {
        return isPlaceholder;
    }


    /**
     * Retrieve the defined configuration properties as a list of the type used in the connector's specification.
     *
     * @return list
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (PasteurConfigurationProperty configurationProperty : PasteurConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in the connector's specification.
     *
     * @return configuration property type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

        configurationPropertyType.setName(name);
        configurationPropertyType.setDescription(description);
        configurationPropertyType.setDataType(dataType);
        configurationPropertyType.setExample(example);
        configurationPropertyType.setRequired(isPlaceholder);

        return configurationPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PasteurConfigurationProperty{ name=" + name + "}";
    }
}
