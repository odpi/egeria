/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class AttributeTypeDefCategoryFindRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private AttributeTypeDefCategory category = null;


    /**
     * Default constructor
     */
    public AttributeTypeDefCategoryFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AttributeTypeDefCategoryFindRequest(AttributeTypeDefCategoryFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.category = template.getCategory();
        }
    }


    /**
     * Return the category for the request.
     *
     * @return category enum
     */
    public AttributeTypeDefCategory getCategory()
    {
        return category;
    }


    /**
     * Set up the category for the request.
     *
     * @param category category enum
     */
    public void setCategory(AttributeTypeDefCategory category)
    {
        this.category = category;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AttributeTypeDefCategoryFindRequest{" +
                "category=" + category +
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
        if (!(objectToCompare instanceof AttributeTypeDefCategoryFindRequest))
        {
            return false;
        }
        AttributeTypeDefCategoryFindRequest
                that = (AttributeTypeDefCategoryFindRequest) objectToCompare;
        return getCategory() == that.getCategory();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getCategory());
    }
}
