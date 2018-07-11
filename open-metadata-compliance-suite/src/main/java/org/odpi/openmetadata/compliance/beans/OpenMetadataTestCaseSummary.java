/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.beans;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.compliance.OpenMetadataTestCase;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestCaseSummary provides basic properties of a test case for use in reporting.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
                @JsonSubTypes.Type(value = OpenMetadataTestCaseResult.class, name = "OpenMetadataTestCaseResult")
              })
public class OpenMetadataTestCaseSummary implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private String  testCaseId = null;
    private String  testCaseName = null;
    private String  testCaseDescription = null;

    /**
     * Default constructor
     */
    public OpenMetadataTestCaseSummary()
    {
    }


    /**
     * Constructor to create a test case summary from a test case.
     *
     * @param testCase test case object.
     */
    public OpenMetadataTestCaseSummary(OpenMetadataTestCase testCase)
    {
        this.testCaseId = testCase.getTestCaseId();
        this.testCaseName = testCase.getTestCaseName();
        this.testCaseDescription = testCase.getTestCaseDescription();
    }

    public String getTestCaseId()
    {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId)
    {
        this.testCaseId = testCaseId;
    }

    public String getTestCaseName()
    {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName)
    {
        this.testCaseName = testCaseName;
    }

    public String getTestCaseDescription()
    {
        return testCaseDescription;
    }

    public void setTestCaseDescription(String testCaseDescription)
    {
        this.testCaseDescription = testCaseDescription;
    }
}
