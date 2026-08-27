/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Governance action process describes a governance action that is a sequence of steps.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class", defaultImpl = GovernanceActionProcessProperties.class)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AnalyticalActionProcessProperties.class, name = "AnalyticalActionProcessProperties"),
                @JsonSubTypes.Type(value = CataloguingActionProcessProperties.class, name = "CataloguingActionProcessProperties"),
                @JsonSubTypes.Type(value = DeletingActionProcessProperties.class, name = "DeletingActionProcessProperties"),
                @JsonSubTypes.Type(value = ExploringActionProcessProperties.class, name = "ExploringActionProcessProperties"),
                @JsonSubTypes.Type(value = ProvisioningActionProcessProperties.class, name = "ProvisioningActionProcessProperties"),
                @JsonSubTypes.Type(value = SubscribingActionProcessProperties.class, name = "SubscribingActionProcessProperties"),
                @JsonSubTypes.Type(value = SurveyingActionProcessProperties.class, name = "SurveyingActionProcessProperties"),
        })
public class GovernanceActionProcessProperties extends GovernanceActionProperties
{

    /**
     * Default constructor
     */
    public GovernanceActionProcessProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessProperties(GovernanceActionProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProcessProperties{" +
                "} " + super.toString();
    }
}
