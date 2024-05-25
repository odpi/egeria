/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConfidenceGovernanceClassification defines the level of confidence that should be placed in the accuracy of related data items.
 * This limits the scope that the data can be used in.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConfidenceGovernanceClassification extends GovernanceClassificationBase
{
    /**
     * Default constructor
     */
    public ConfidenceGovernanceClassification()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ConfidenceGovernanceClassification(ConfidenceGovernanceClassification template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConfidenceGovernanceClassification{" +
                       "classificationOrigin=" + getClassificationOrigin() +
                       ", classificationOriginGUID='" + getClassificationOriginGUID() + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", governanceStatus=" + getGovernanceStatus() +
                       ", confidence=" + getConfidence() +
                       ", steward='" + getSteward() + '\'' +
                       ", stewardTypeName='" + getStewardTypeName() + '\'' +
                       ", stewardPropertyName='" + getStewardPropertyName() + '\'' +
                       ", source='" + getSource() + '\'' +
                       ", notes='" + getNotes() + '\'' +
                       ", levelIdentifier=" + getLevelIdentifier() +
                       ", headerVersion=" + getHeaderVersion() +
                       '}';
    }
}
