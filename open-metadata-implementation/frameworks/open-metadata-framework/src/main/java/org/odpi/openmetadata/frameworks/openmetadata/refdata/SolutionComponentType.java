/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * SolutionComponentType describes suggested values to use in the solutionComponentType property found in the
 * SolutionComponent entity.
 */
public enum SolutionComponentType
{
    /**
     * An automated process that is implemented by some form of software.
     */
    AUTOMATED_PROCESS("Automated Process",
                      "An automated process that is implemented by some form of software."),


    /**
     * A process that is implemented by a third party and is opaque to the solution.
     */
    THIRD_PARTY_PROCESS("Third Party Process",
                      "A process that is implemented by a third party and is opaque to the solution."),


    /**
     * A set of tasks that need to be preformed by individuals or teams of people.
     */
    MANUAL_PROCESS("Manual Process",
                   "A set of tasks that need to be preformed by individuals or teams of people."),


    /**
     * The storage of data for later retrieval by automated processes.
     */
    DATA_STORAGE("Data Storage",
                 "The storage of data for later retrieval by automated processes."),


    /**
     * A distribution of data to a variety of automated processes.
     */
    DATA_DISTRIBUTION("Data Distribution",
                      "A distribution of data to a variety of automated processes."),


    /**
     * The publishing of information (typically documents) to third parties.
     */
    DOCUMENT_PUBLISHING("Publishing",
                        "The publishing of information (typically documents) to third parties."),


    /**
     * A trained model using analytical or AI techniques to generate insight from data.
     */
    INSIGHT_MODEL("Insight Model",
                  "A trained model using analytical or AI techniques to generate insight from data."),


    ;

    /**
     * Property value.
     */
    private final String solutionComponentType;


    /**
     * Property value description.
     */
    private final String description;


    /**
     * Constructor for individual enum value.
     *
     * @param solutionComponentType the property value to use in solutionComponentType
     * @param description description of the property value
     */
    SolutionComponentType(String      solutionComponentType,
                          String      description)
    {
        this.solutionComponentType = solutionComponentType;
        this.description           = description;
    }


    /**
     * Return the value of this property.
     *
     * @return string
     */
    public String getSolutionComponentType()
    {
        return solutionComponentType;
    }


    /**
     * Return the description for this value.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }



    /**
     * Return the qualified name for this resourceUse value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.SOLUTION_COMPONENT_TYPE.name,
                                                null,
                                                solutionComponentType);
    }


    /**
     * Return the namespace for this solutionComponentType value.
     *
     * @return string
     */
    public String getNamespace()
    {
        return constructValidValueNamespace(null,
                                            OpenMetadataProperty.SOLUTION_COMPONENT_TYPE.name,
                                            null);
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionComponentType{" + solutionComponentType + '}';
    }
}
