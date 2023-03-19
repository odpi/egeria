/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessCallProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * ProcessCallElement contains the properties and header for a process call relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessCallElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader         processCallHeader     = null;
    private ProcessCallProperties processCallProperties = null;
    private ElementHeader         caller                = null;
    private ElementHeader         called                = null;

    /**
     * Default constructor
     */
    public ProcessCallElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProcessCallElement(ProcessCallElement template)
    {
        if (template != null)
        {
            processCallHeader = template.getProcessCallHeader();
            processCallProperties = template.getProcessCallProperties();
            caller = template.getCaller();
            called = template.getCalled();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getProcessCallHeader()
    {
        return processCallHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param processCallHeader element header object
     */
    public void setProcessCallHeader(ElementHeader processCallHeader)
    {
        this.processCallHeader = processCallHeader;
    }


    /**
     * Return details of the relationship
     *
     * @return relationship properties
     */
    public ProcessCallProperties getProcessCallProperties()
    {
        return processCallProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param processCallProperties relationship properties
     */
    public void setProcessCallProperties(ProcessCallProperties processCallProperties)
    {
        this.processCallProperties = processCallProperties;
    }


    /**
     * Return the element header associated with end 1 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getCaller()
    {
        return caller;
    }


    /**
     * Set up the element header associated with end 1 of the relationship.
     *
     * @param caller element header object
     */
    public void setCaller(ElementHeader caller)
    {
        this.caller = caller;
    }



    /**
     * Return the element header associated with end 2 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getCalled()
    {
        return called;
    }


    /**
     * Set up the element header associated with end 2 of the relationship.
     *
     * @param called element header object
     */
    public void setCalled(ElementHeader called)
    {
        this.called = called;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProcessCallElement{" +
                       "processCallHeader=" + processCallHeader +
                       ", processCallProperties=" + processCallProperties +
                       ", caller=" + caller +
                       ", called=" + called +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ProcessCallElement that = (ProcessCallElement) objectToCompare;
        return Objects.equals(getProcessCallHeader(), that.getProcessCallHeader()) &&
                       Objects.equals(getProcessCallProperties(), that.getProcessCallProperties()) &&
                       Objects.equals(getCaller(), that.getCaller()) &&
                       Objects.equals(getCalled(), that.getCalled());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), processCallHeader, processCallProperties, caller, called);
    }
}
