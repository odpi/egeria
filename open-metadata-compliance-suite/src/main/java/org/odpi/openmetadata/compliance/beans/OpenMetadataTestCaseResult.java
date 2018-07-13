/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.compliance.OpenMetadataTestCase;


import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestCaseResults is a bean for storing the result of a single test.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTestCaseResult extends OpenMetadataTestCaseSummary
{
    private boolean             isTestSuccessful     = false;

    private String              successMessage       = null;
    private Map<String, Object> discoveredProperties = null;

    private ExceptionBean       complianceException  = null;


    /**
     * Default Constructor
     */
    public OpenMetadataTestCaseResult()
    {
        super();
    }


    public OpenMetadataTestCaseResult(OpenMetadataTestCase   testCase)
    {
        super(testCase);
    }


    public String getSuccessMessage()
    {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage)
    {
        this.successMessage = successMessage;
    }

    public ExceptionBean getComplianceException()
    {
        return complianceException;
    }

    public void setComplianceException(ExceptionBean complianceException)
    {
        this.complianceException = complianceException;
    }

    public Map<String, Object> getDiscoveredProperties()
    {
        return discoveredProperties;
    }

    public void setDiscoveredProperties(Map<String, Object> discoveredProperties)
    {
        this.discoveredProperties = discoveredProperties;
    }
}
