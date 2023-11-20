/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import java.util.Objects;

/**
 * RelatedDataField describes a DataFieldLink and the attached DataField.
 */
public class RelatedDataField
{
    private DataFieldLink dataFieldLink     = null;
    private DataField     attachedDataField = null;


    /**
     * Default constructor
     */
    public RelatedDataField()
    {
    }


    /**
     * Copy;clone constructor
     *
     * @param template object to copy
     */
    public RelatedDataField(RelatedDataField template)
    {
        if (template != null)
        {
            this.dataFieldLink = template.getDataFieldLink();
            this.attachedDataField = template.getAttachedDataField();
        }
    }


    /**
     * Return the description of the relationship.
     *
     * @return data field link
     */
    public DataFieldLink getDataFieldLink()
    {
        return dataFieldLink;
    }


    /**
     * Set up the description of the relationship.
     *
     * @param dataFieldLink data field link
     */
    public void setDataFieldLink(DataFieldLink dataFieldLink)
    {
        this.dataFieldLink = dataFieldLink;
    }


    /**
     * Return the description of the attached data field.
     *
     * @return attached data field
     */
    public DataField getAttachedDataField()
    {
        return attachedDataField;
    }


    /**
     * Set up the description of the attached data field.
     *
     * @param attachedDataField attached data field
     */
    public void setAttachedDataField(DataField attachedDataField)
    {
        this.attachedDataField = attachedDataField;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedDataField{" +
                       "dataFieldLink=" + dataFieldLink +
                       ", attachedDataField=" + attachedDataField +
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
        if (! (objectToCompare instanceof RelatedDataField that))
        {
            return false;
        }
        return Objects.equals(dataFieldLink, that.dataFieldLink) && Objects.equals(attachedDataField, that.attachedDataField);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(dataFieldLink, attachedDataField);
    }
}
