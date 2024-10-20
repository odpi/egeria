/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Properties of a model version.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelVersionProperties extends StoredDataProperties
{
    private String source = null;
    private String run_id = null;
    private String model_name = null;

    /**
     * Constructor
     */
    public ModelVersionProperties()
    {
    }


    /**
     * Return the type of source.
     *
     * @return name
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the type of source.
     *
     * @param source name
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the run id for the model version
     *
     * @return string
     */
    public String getRun_id()
    {
        return run_id;
    }


    /**
     * Set up the run id for the model version
     *
     * @param run_id string
     */
    public void setRun_id(String run_id)
    {
        this.run_id = run_id;
    }


    /**
     * Return the name of the registered model that this is a part of.
     *
     * @return name
     */
    public String getModel_name()
    {
        return model_name;
    }


    /**
     * Set up the name of the registered model that this is a part of.
     *
     * @param model_name name
     */
    public void setModel_name(String model_name)
    {
        this.model_name = model_name;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ModelVersionProperties{" +
                "source='" + source + '\'' +
                ", run_id='" + run_id + '\'' +
                ", model_name='" + model_name + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ModelVersionProperties that = (ModelVersionProperties) objectToCompare;
        return Objects.equals(source, that.source) &&
         Objects.equals(run_id, that.run_id) &&
         Objects.equals(model_name, that.model_name);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), source, run_id, model_name);
    }
}
