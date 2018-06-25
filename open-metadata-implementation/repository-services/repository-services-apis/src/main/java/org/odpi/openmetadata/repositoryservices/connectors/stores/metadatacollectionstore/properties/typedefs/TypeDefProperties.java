/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The TypeDefProperties class provides support for arbitrary properties that belong to a TypeDef object.
 * It is used for searching the TypeDefs.
 * It wraps a java.util.Map map object built around HashMap.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefProperties extends TypeDefElementHeader
{
    private List<String> typeDefProperties = new ArrayList<>();


    /**
     * Typical constructor
     */
    public TypeDefProperties()
    {
        /*
         * Nothing to do
         */
    }


    /**
     * Copy/clone Constructor.
     *
     * @param templateProperties template object to copy.
     */
    public TypeDefProperties(TypeDefProperties templateProperties)
    {
        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            this.setTypeDefProperties(templateProperties.getTypeDefProperties());
        }
    }


    /**
     * Return the list of property names
     *
     * @return List of String property names
     */
    public List<String> getTypeDefProperties()
    {
        if (typeDefProperties == null)
        {
            return null;
        }
        else
        {
            return new ArrayList<>(typeDefProperties);
        }
    }


    /**
     * Set up the list of property names.
     *
     * @param typeDefProperties list of property names
     */
    public void setTypeDefProperties(List<String> typeDefProperties)
    {
        if (typeDefProperties == null)
        {
            this.typeDefProperties = null;
        }
        else
        {
            this.typeDefProperties = new ArrayList<>(typeDefProperties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "TypeDefProperties{" +
                "typeDefProperties=" + typeDefProperties +
                '}';
    }
}
