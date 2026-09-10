/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a target entity (dataset or job) and the sources that feed it, from the lineage job facet.  The type field says whether it is a DATASET or a JOB.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLineageEntry
{
    private String                                    type = null;
    private String                                    namespace = null;
    private String                                    name = null;
    private UUID                                      runId = null;
    private List<OpenLineageLineageInput>             inputs = null;
    private Map<String, OpenLineageLineageFieldEntry> fields = null;


    /**
     * Default constructor
     */
    public OpenLineageLineageEntry()
    {
    }


    /**
     * Return the target entity type: DATASET or JOB.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the target entity type: DATASET or JOB.
     *
     * @param type string
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the namespace of the target dataset or job.  For a job it may be omitted, together with name, to refer to the event's own job.
     *
     * @return string
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the namespace of the target dataset or job.  For a job it may be omitted, together with name, to refer to the event's own job.
     *
     * @param namespace string
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the name of the target dataset or job.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the target dataset or job.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the target job run when the relationship is tied to a specific execution (jobs only).
     *
     * @return uuid
     */
    public UUID getRunId()
    {
        return runId;
    }


    /**
     * Set up the target job run when the relationship is tied to a specific execution (jobs only).
     *
     * @param runId uuid
     */
    public void setRunId(UUID runId)
    {
        this.runId = runId;
    }


    /**
     * Return the entity-level inputs feeding this target.  An empty list explicitly means the target has no tracked inputs.
     *
     * @return list
     */
    public List<OpenLineageLineageInput> getInputs()
    {
        return inputs;
    }


    /**
     * Set up the entity-level inputs feeding this target.  An empty list explicitly means the target has no tracked inputs.
     *
     * @param inputs list
     */
    public void setInputs(List<OpenLineageLineageInput> inputs)
    {
        this.inputs = inputs;
    }


    /**
     * Return the field-level lineage: maps target field names to their source inputs (datasets only).
     *
     * @return map
     */
    public Map<String, OpenLineageLineageFieldEntry> getFields()
    {
        return fields;
    }


    /**
     * Set up the field-level lineage: maps target field names to their source inputs (datasets only).
     *
     * @param fields map
     */
    public void setFields(Map<String, OpenLineageLineageFieldEntry> fields)
    {
        this.fields = fields;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageLineageEntry{" +
                       "type='" + type + '\'' +
                       ", namespace='" + namespace + '\'' +
                       ", name='" + name + '\'' +
                       ", runId=" + runId +
                       ", inputs=" + inputs +
                       ", fields=" + fields +
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
        OpenLineageLineageEntry that = (OpenLineageLineageEntry) objectToCompare;
        return Objects.equals(type, that.type) &&
                       Objects.equals(namespace, that.namespace) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(runId, that.runId) &&
                       Objects.equals(inputs, that.inputs) &&
                       Objects.equals(fields, that.fields);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(type, namespace, name, runId, inputs, fields);
    }
}
