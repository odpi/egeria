/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CohortDescription describes a single cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CohortDescription extends OMRSProperty
{
    private static final long    serialVersionUID = 1L;

    private String                 cohortName = null;
    private CohortConnectionStatus connectionStatus = null;
    private Connection             topicConnection = null;


    /**
     * Default constructor
     */
    public CohortDescription()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CohortDescription(CohortDescription template)
    {
        super(template);

        if (template != null)
        {
            cohortName = template.getCohortName();
        }
    }

    /**
     * Return the name of the cohort.
     *
     * @return string name
     */
    public String getCohortName()
    {
        return cohortName;
    }


    /**
     * Set up the name of the cohort
     *
     * @param cohortName string name
     */
    public void setCohortName(String cohortName)
    {
        this.cohortName = cohortName;
    }


    public CohortConnectionStatus getConnectionStatus()
    {
        return connectionStatus;
    }


    public void setConnectionStatus(CohortConnectionStatus connectionStatus)
    {
        this.connectionStatus = connectionStatus;
    }


    public Connection getTopicConnection()
    {
        return topicConnection;
    }


    public void setTopicConnection(Connection topicConnection)
    {
        this.topicConnection = topicConnection;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CohortDescription{" +
                "cohortName=" + cohortName +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof CohortDescription))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CohortDescription that = (CohortDescription) objectToCompare;
        return Objects.equals(getCohortName(), that.getCohortName());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getCohortName());
    }
}
