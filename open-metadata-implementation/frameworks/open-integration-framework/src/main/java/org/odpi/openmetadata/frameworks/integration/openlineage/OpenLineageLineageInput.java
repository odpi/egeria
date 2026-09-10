/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a source entity (dataset, dataset field or job) feeding a target in the lineage facets.  The type field says whether it is a DATASET or a JOB.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLineageInput
{
    private String                                 type = null;
    private String                                 namespace = null;
    private String                                 name = null;
    private String                                 field = null;
    private UUID                                   runId = null;
    private List<OpenLineageLineageTransformation> transformations = null;


    /**
     * Default constructor
     */
    public OpenLineageLineageInput()
    {
    }


    /**
     * Return the source entity type: DATASET or JOB.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the source entity type: DATASET or JOB.
     *
     * @param type string
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the namespace of the source dataset or job.  For a job it may be omitted, together with name, to refer to the event's own job.
     *
     * @return string
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the namespace of the source dataset or job.  For a job it may be omitted, together with name, to refer to the event's own job.
     *
     * @param namespace string
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the name of the source dataset or job.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the source dataset or job.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the source field (datasets only).  At entity level it represents a dataset-wide dependency; at field level it identifies the source field.
     *
     * @return string
     */
    public String getField()
    {
        return field;
    }


    /**
     * Set up the source field (datasets only).  At entity level it represents a dataset-wide dependency; at field level it identifies the source field.
     *
     * @param field string
     */
    public void setField(String field)
    {
        this.field = field;
    }


    /**
     * Return the source job run when the relationship is tied to a specific execution (jobs only).
     *
     * @return uuid
     */
    public UUID getRunId()
    {
        return runId;
    }


    /**
     * Set up the source job run when the relationship is tied to a specific execution (jobs only).
     *
     * @param runId uuid
     */
    public void setRunId(UUID runId)
    {
        this.runId = runId;
    }


    /**
     * Return the transformations applied to the source data.
     *
     * @return list
     */
    public List<OpenLineageLineageTransformation> getTransformations()
    {
        return transformations;
    }


    /**
     * Set up the transformations applied to the source data.
     *
     * @param transformations list
     */
    public void setTransformations(List<OpenLineageLineageTransformation> transformations)
    {
        this.transformations = transformations;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageLineageInput{" +
                       "type='" + type + '\'' +
                       ", namespace='" + namespace + '\'' +
                       ", name='" + name + '\'' +
                       ", field='" + field + '\'' +
                       ", runId=" + runId +
                       ", transformations=" + transformations +
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
        OpenLineageLineageInput that = (OpenLineageLineageInput) objectToCompare;
        return Objects.equals(type, that.type) &&
                       Objects.equals(namespace, that.namespace) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(field, that.field) &&
                       Objects.equals(runId, that.runId) &&
                       Objects.equals(transformations, that.transformations);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(type, namespace, name, field, runId, transformations);
    }
}
