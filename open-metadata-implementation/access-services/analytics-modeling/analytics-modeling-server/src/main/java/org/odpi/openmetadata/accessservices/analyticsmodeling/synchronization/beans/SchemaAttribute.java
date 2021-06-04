/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans;

import java.util.Objects;

/**
 *  SchemaAttribute describes a single attribute within a schema.  The attribute has a name, order within
 *  the parent schema element.
 */
public class SchemaAttribute extends SchemaElement
{
    protected int elementPosition		= 0;
    protected String nativeJavaClass	= null;
    

    /**
     * Default constructor
     */
    public SchemaAttribute()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public SchemaAttribute(SchemaAttribute template)
    {
        super(template);

        if (template != null)
        {
            elementPosition        = template.getElementPosition();
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because schema element
     * is abstract.
     *
     * @return Clone of subclass.
     */
    public SchemaElement cloneSchemaElement()
    {
        return new SchemaAttribute(this);
    }

    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema - 0 means first
     */
    public int getElementPosition() { return elementPosition; }


    /**
     * Set up the position of this schema attribute in its parent schema.
     *
     * @param elementPosition int position in schema - 0 means first
     */
    public void setElementPosition(int elementPosition)
    {
        this.elementPosition = elementPosition;
    }

    /**
	 * @return the nativeJavaClass
	 */
	public String getNativeJavaClass() {
		return nativeJavaClass;
	}


	/**
	 * @param nativeJavaClass the nativeJavaClass to set
	 */
	public void setNativeJavaClass(String nativeJavaClass) {
		this.nativeJavaClass = nativeJavaClass;
	}


	/**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaAttribute{" +
                "elementPosition=" + elementPosition +
                "nativeJavaClass=" + nativeJavaClass +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaAttribute that = (SchemaAttribute) objectToCompare;
        return Objects.equals(elementPosition, that.elementPosition);
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementPosition, nativeJavaClass);
    }
}