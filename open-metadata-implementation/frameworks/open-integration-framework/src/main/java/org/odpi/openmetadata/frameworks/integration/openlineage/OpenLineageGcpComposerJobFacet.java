/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the gcp_composer_job job facet registered by Google Cloud Composer.  It identifies the Composer environment, DAG and task that the job belongs to.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/GcpComposerJobFacet.json#/$defs/GcpComposerJobFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageGcpComposerJobFacet extends OpenLineageJobFacet
{
    private String environmentName = null;
    private String dagId = null;
    private String operator = null;
    private String taskId = null;
    private String airflowVersion = null;
    private String composerVersion = null;


    /**
     * Default constructor
     */
    public OpenLineageGcpComposerJobFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/GcpComposerJobFacet.json#/$defs/GcpComposerJobFacet"));
    }


    /**
     * Return the Cloud Composer environment name.
     *
     * @return string
     */
    public String getEnvironmentName()
    {
        return environmentName;
    }


    /**
     * Set up the Cloud Composer environment name.
     *
     * @param environmentName string
     */
    public void setEnvironmentName(String environmentName)
    {
        this.environmentName = environmentName;
    }


    /**
     * Return the id of the DAG.
     *
     * @return string
     */
    public String getDagId()
    {
        return dagId;
    }


    /**
     * Set up the id of the DAG.
     *
     * @param dagId string
     */
    public void setDagId(String dagId)
    {
        this.dagId = dagId;
    }


    /**
     * Return the operator class name.  Only present for tasks, not for DAGs, for example PythonOperator.
     *
     * @return string
     */
    public String getOperator()
    {
        return operator;
    }


    /**
     * Set up the operator class name.  Only present for tasks, not for DAGs, for example PythonOperator.
     *
     * @param operator string
     */
    public void setOperator(String operator)
    {
        this.operator = operator;
    }


    /**
     * Return the id of the task.  Only present for tasks, not for DAGs.
     *
     * @return string
     */
    public String getTaskId()
    {
        return taskId;
    }


    /**
     * Set up the id of the task.  Only present for tasks, not for DAGs.
     *
     * @param taskId string
     */
    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }


    /**
     * Return the version of Airflow, suffixed by +composer.
     *
     * @return string
     */
    public String getAirflowVersion()
    {
        return airflowVersion;
    }


    /**
     * Set up the version of Airflow, suffixed by +composer.
     *
     * @param airflowVersion string
     */
    public void setAirflowVersion(String airflowVersion)
    {
        this.airflowVersion = airflowVersion;
    }


    /**
     * Return the version of the Cloud Composer environment.
     *
     * @return string
     */
    public String getComposerVersion()
    {
        return composerVersion;
    }


    /**
     * Set up the version of the Cloud Composer environment.
     *
     * @param composerVersion string
     */
    public void setComposerVersion(String composerVersion)
    {
        this.composerVersion = composerVersion;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageGcpComposerJobFacet{" +
                       "environmentName='" + environmentName + '\'' +
                       ", dagId='" + dagId + '\'' +
                       ", operator='" + operator + '\'' +
                       ", taskId='" + taskId + '\'' +
                       ", airflowVersion='" + airflowVersion + '\'' +
                       ", composerVersion='" + composerVersion + '\'' +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", _deleted=" + get_deleted() +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenLineageGcpComposerJobFacet that = (OpenLineageGcpComposerJobFacet) objectToCompare;
        return Objects.equals(environmentName, that.environmentName) &&
                       Objects.equals(dagId, that.dagId) &&
                       Objects.equals(operator, that.operator) &&
                       Objects.equals(taskId, that.taskId) &&
                       Objects.equals(airflowVersion, that.airflowVersion) &&
                       Objects.equals(composerVersion, that.composerVersion);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), environmentName, dagId, operator, taskId, airflowVersion, composerVersion);
    }
}
