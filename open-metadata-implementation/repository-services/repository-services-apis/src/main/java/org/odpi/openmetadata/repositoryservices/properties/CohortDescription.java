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
    private String                 cohortName                  = null;
    private CohortConnectionStatus connectionStatus            = null;
    private Connection             registrationTopicConnection = null;
    private Connection             typesTopicConnection        = null;
    private Connection             instancesTopicConnection    = null;


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
            connectionStatus = template.getConnectionStatus();
            registrationTopicConnection = template.getRegistrationTopicConnection();
            typesTopicConnection = template.getTypesTopicConnection();
            instancesTopicConnection = template.getInstancesTopicConnection();
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


    /**
     * Return the status of the connection to the cohort.
     *
     * @return status
     */
    public CohortConnectionStatus getConnectionStatus()
    {
        return connectionStatus;
    }


    /**
     * Set up the status of the connector tio the cohort.
     *
     * @param connectionStatus status
     */
    public void setConnectionStatus(CohortConnectionStatus connectionStatus)
    {
        this.connectionStatus = connectionStatus;
    }


    /**
     * Return the connection to the registration cohort topic if it is in use.
     *
     * @return Connection object
     */
    public Connection getRegistrationTopicConnection()
    {
        return registrationTopicConnection;
    }


    /**
     * Set up the connection to the registration cohort topic if it is in use.
     *
     * @param registrationTopicConnection Connection object
     */
    public void setRegistrationTopicConnection(Connection registrationTopicConnection)
    {
        this.registrationTopicConnection = registrationTopicConnection;
    }


    /**
     * Return the connection to the types cohort topic if it is in use.
     *
     * @return Connection object
     */
    public Connection getTypesTopicConnection()
    {
        return typesTopicConnection;
    }


    /**
     * Set up the connection to the types cohort topic if it is in use.
     *
     * @param typesTopicConnection Connection object
     */
    public void setTypesTopicConnection(Connection typesTopicConnection)
    {
        this.typesTopicConnection = typesTopicConnection;
    }


    /**
     * Return the connection to the instances cohort topic if it is in use.
     *
     * @return Connection object
     */
    public Connection getInstancesTopicConnection()
    {
        return instancesTopicConnection;
    }


    /**
     * Set up the connection to the instances cohort topic if it is in use.
     *
     * @param instanceTopicConnection Connection object
     */
    public void setInstancesTopicConnection(Connection instanceTopicConnection)
    {
        this.instancesTopicConnection = instanceTopicConnection;
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
                       "cohortName='" + cohortName + '\'' +
                       ", connectionStatus=" + connectionStatus +
                       ", registrationTopicConnection=" + registrationTopicConnection +
                       ", typesTopicConnection=" + typesTopicConnection +
                       ", instancesTopicConnection=" + instancesTopicConnection +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        CohortDescription that = (CohortDescription) objectToCompare;
        return Objects.equals(cohortName, that.cohortName) &&
                       connectionStatus == that.connectionStatus &&
                       Objects.equals(registrationTopicConnection, that.registrationTopicConnection) &&
                       Objects.equals(typesTopicConnection, that.typesTopicConnection) &&
                       Objects.equals(instancesTopicConnection, that.instancesTopicConnection);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(cohortName, connectionStatus, registrationTopicConnection, typesTopicConnection,
                            instancesTopicConnection);
    }
}
