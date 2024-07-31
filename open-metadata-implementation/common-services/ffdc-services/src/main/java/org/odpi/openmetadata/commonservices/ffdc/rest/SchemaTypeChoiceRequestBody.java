/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeChoiceProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SchemaTypeChoiceRequestBody describes the properties of the schema type plus the optional identifiers for an
 * owning software server capability and the list of guids for the schema types that represent the choices.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaTypeChoiceRequestBody extends SchemaTypeChoiceProperties
{
    private String       externalSourceGUID    = null;
    private String       externalSourceName    = null;
    private List<String> schemaTypeOptionGUIDs = null;


    /**
     * Default constructor
     */
    public SchemaTypeChoiceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SchemaTypeChoiceRequestBody(SchemaTypeChoiceRequestBody template)
    {
        super(template);

        if (template != null)
        {
            externalSourceGUID = template.getExternalSourceGUID();
            externalSourceName = template.getExternalSourceName();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SchemaTypeChoiceRequestBody(SchemaTypeChoiceProperties template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @return string guid
     */
    public String getExternalSourceGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Set up the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @param externalSourceGUID string guid
     */
    public void setExternalSourceGUID(String externalSourceGUID)
    {
        this.externalSourceGUID = externalSourceGUID;
    }


    /**
     * Return the unique name of the software server capability entity that represented the external source.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /**
     * Set up the unique name of the software server capability entity that represented the external source.
     *
     * @param externalSourceName string name
     */
    public void setExternalSourceName(String externalSourceName)
    {
        this.externalSourceName = externalSourceName;
    }


    /**
     * Return the list of schema types that describe the options.
     *
     * @return list of guids
     */
    public List<String> getSchemaTypeOptionGUIDs()
    {
        return schemaTypeOptionGUIDs;
    }


    /**
     * Set up the list of schema types that describe the options.
     *
     * @param schemaTypeOptionGUIDs list of guids
     */
    public void setSchemaTypeOptionGUIDs(List<String> schemaTypeOptionGUIDs)
    {
        this.schemaTypeOptionGUIDs = schemaTypeOptionGUIDs;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SchemaTypeChoiceRequestBody{" +
                "externalSourceGUID='" + externalSourceGUID + '\'' +
                ", externalSourceName='" + externalSourceName + '\'' +
                ", schemaTypeOptionGUIDs=" + schemaTypeOptionGUIDs +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaTypeChoiceRequestBody that = (SchemaTypeChoiceRequestBody) objectToCompare;
        return Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                       Objects.equals(externalSourceName, that.externalSourceName) &&
                       Objects.equals(schemaTypeOptionGUIDs, that.schemaTypeOptionGUIDs);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSourceGUID, externalSourceName, schemaTypeOptionGUIDs);
    }
}
