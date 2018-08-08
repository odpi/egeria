/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LicenseType defines a license that the organization recognizes and has governance
 * definitions to support it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDefinitionMetric extends GovernanceMetric
{
    private String   rationale = null;


    /**
     * Default constructor
     */
    public GovernanceDefinitionMetric()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDefinitionMetric(GovernanceDefinitionMetric  template)
    {
        super(template);

        if (template != null)
        {
            this.rationale = getRationale();
        }
    }


    /**
     * Return the rationale as to why this metric is a good measure of the activity associated with the
     * governance definition.
     *
     * @return string description
     */
    public String getRationale()
    {
        return rationale;
    }


    /**
     * Set up the rationale as to why this metric is a good measure of the activity associated with the
     * governance definition.
     *
     * @param rationale string description
     */
    public void setRationale(String rationale)
    {
        this.rationale = rationale;
    }



    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */



    /**
     * Test the properties of the DataStrategy to determine if the supplied object is equal to this one.
     *
     * @param objectToCompare object
     * @return boolean evaluation
     */
}
