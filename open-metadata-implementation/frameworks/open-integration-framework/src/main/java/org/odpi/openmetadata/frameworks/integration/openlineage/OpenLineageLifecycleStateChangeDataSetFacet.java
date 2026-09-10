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
 * This class represents the lifecycleStateChange dataset facet.  It records a change to the lifecycle of the dataset, for example CREATE, DROP or RENAME.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/LifecycleStateChangeDatasetFacet.json#/$defs/LifecycleStateChangeDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLifecycleStateChangeDataSetFacet extends OpenLineageDataSetFacet
{
    private String                                                        lifecycleStateChange = null;
    private OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier previousIdentifier = null;


    /**
     * Default constructor
     */
    public OpenLineageLifecycleStateChangeDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/LifecycleStateChangeDatasetFacet.json#/$defs/LifecycleStateChangeDatasetFacet"));
    }


    /**
     * Return the lifecycle state change: ALTER, CREATE, DROP, OVERWRITE, RENAME or TRUNCATE.
     *
     * @return string
     */
    public String getLifecycleStateChange()
    {
        return lifecycleStateChange;
    }


    /**
     * Set up the lifecycle state change: ALTER, CREATE, DROP, OVERWRITE, RENAME or TRUNCATE.
     *
     * @param lifecycleStateChange string
     */
    public void setLifecycleStateChange(String lifecycleStateChange)
    {
        this.lifecycleStateChange = lifecycleStateChange;
    }


    /**
     * Return the previous name of the dataset in the case of a rename.
     *
     * @return bean
     */
    public OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier getPreviousIdentifier()
    {
        return previousIdentifier;
    }


    /**
     * Set up the previous name of the dataset in the case of a rename.
     *
     * @param previousIdentifier bean
     */
    public void setPreviousIdentifier(OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier previousIdentifier)
    {
        this.previousIdentifier = previousIdentifier;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageLifecycleStateChangeDataSetFacet{" +
                       "lifecycleStateChange='" + lifecycleStateChange + '\'' +
                       ", previousIdentifier=" + previousIdentifier +
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
        OpenLineageLifecycleStateChangeDataSetFacet that = (OpenLineageLifecycleStateChangeDataSetFacet) objectToCompare;
        return Objects.equals(lifecycleStateChange, that.lifecycleStateChange) &&
                       Objects.equals(previousIdentifier, that.previousIdentifier);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), lifecycleStateChange, previousIdentifier);
    }
}
