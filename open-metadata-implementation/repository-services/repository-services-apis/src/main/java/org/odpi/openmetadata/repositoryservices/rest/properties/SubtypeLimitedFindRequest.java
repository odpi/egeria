/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubtypeLimitedFindRequest extends the type limited find request to allow the caller to request that only a restricted
 * list of subtypes of the overall type are included in the results.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = InstanceFindRequest.class, name = "InstanceFindRequest")
        })
public class SubtypeLimitedFindRequest extends TypeLimitedFindRequest
{
    private static final long    serialVersionUID = 1L;

    private List<String>             subtypeGUIDs = null;


    /**
     * Default constructor
     */
    public SubtypeLimitedFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SubtypeLimitedFindRequest(SubtypeLimitedFindRequest template)
    {
        super(template);

        if (template != null)
        {
            List<String> subtypes = template.getSubtypeGUIDs();
            if (subtypes != null && !subtypes.isEmpty())
            {
                this.subtypeGUIDs = new ArrayList<>(subtypes);
            }
        }
    }


    /**
     * Return the subtype guids to limit the results of the find request.
     *
     * @return {@code List<String>} guids
     */
    public List<String> getSubtypeGUIDs()
    {
        return subtypeGUIDs;
    }


    /**
     * Set up the subtype guids to limit the results of the find request.
     *
     * @param subtypeGUIDs {@code List<String>} guids
     */
    public void setSubtypeGUIDs(List<String> subtypeGUIDs)
    {
        this.subtypeGUIDs = subtypeGUIDs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SubtypeLimitedFindRequest{" +
                "typeGUID='" + getTypeGUID() + '\'' +
                ", subtypeGUIDs='" + getSubtypeGUIDs() + '\'' +
                ", sequencingProperty='" + getSequencingProperty() + '\'' +
                ", sequencingOrder=" + getSequencingOrder() +
                ", offset=" + getOffset() +
                ", pageSize=" + getPageSize() +
                ", limitResultsByStatus=" + getLimitResultsByStatus() +
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
        if (!(objectToCompare instanceof TypeLimitedFindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SubtypeLimitedFindRequest
                that = (SubtypeLimitedFindRequest) objectToCompare;
        return Objects.equals(getSubtypeGUIDs(), that.getSubtypeGUIDs());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSubtypeGUIDs());
    }

}
