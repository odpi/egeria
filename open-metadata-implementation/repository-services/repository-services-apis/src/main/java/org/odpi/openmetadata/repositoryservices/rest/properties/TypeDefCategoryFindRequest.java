/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefCategoryFindRequest extends OMRSAPIRequest
{
    private TypeDefCategory    category = null;


    /**
     * Default constructor
     */
    public TypeDefCategoryFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefCategoryFindRequest(TypeDefCategoryFindRequest template)
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
    public TypeDefCategory getCategory()
    {
        return category;
    }


    /**
     * Set up the category for the request.
     *
     * @param category category enum
     */
    public void setCategory(TypeDefCategory category)
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
        return "TypeDefCategoryFindRequest{" +
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
        if (!(objectToCompare instanceof TypeDefCategoryFindRequest))
        {
            return false;
        }
        TypeDefCategoryFindRequest that = (TypeDefCategoryFindRequest) objectToCompare;
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
