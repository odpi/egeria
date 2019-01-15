/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;


import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestResults provides a base class for test result beans.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = OpenMetadataTestWorkbenchResults.class, name = "OpenMetadataTestWorkbenchResults"),
                @JsonSubTypes.Type(value = OpenMetadataTestLabResults.class,       name = "OpenMetadataTestLabResults")
        })
public abstract class OpenMetadataTestResults implements Serializable
{
    private static final long   serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public OpenMetadataTestResults()
    {
    }


    /**
     * Return the number of test cases run.
     *
     * @return int count
     */
    public abstract int getTestCaseCount();


    /**
     * Default setter just needed for JSON transformation.  The value is ignored.
     *
     * @param count number to import
     */
    public void setTestCaseCount(int    count)
    {
        /* do nothing as count is calculated */
    }


    /**
     * Return the number of test cases passed.
     *
     * @return int count
     */
    public abstract int getTestPassCount();


    /**
     * Default setter just needed for JSON transformation.  The value is ignored.
     *
     * @param count number to import
     */
    public void setTestPassCount(int    count)
    {
        /* do nothing as count is calculated */
    }


    /**
     * Return the number of test cases failed.
     *
     * @return int count
     */
    public abstract int getTestFailedCount();


    /**
     * Default setter just needed for JSON transformation.  The value is ignored.
     *
     * @param count number to import
     */
    public void setTestFailedCount(int    count)
    {
        /* do nothing as count is calculated */
    }


    /**
     * Return the number of test cases skipped.
     *
     * @return int count
     */
    public abstract int getTestSkippedCount();


    /**
     * Default setter just needed for JSON transformation.  The value is ignored.
     *
     * @param count number to import
     */
    public void setTestSkippedCount(int    count)
    {
        /* do nothing as count is calculated */
    }

}
