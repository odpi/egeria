/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaTypeChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaTypeChoiceProperties carries the specialized parameters for creating or updating a choice of schema types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaTypeChoiceProperties extends SchemaTypeProperties
{
    private static final long     serialVersionUID = 1L;

    private List<SchemaTypeProperties> schemaOptions = null;


    /**
     * Default constructor
     */
    public SchemaTypeChoiceProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param template template object to copy.
     */
    public SchemaTypeChoiceProperties(SchemaTypeChoiceProperties template)
    {
        super(template);

        if (template != null)
        {
            schemaOptions = template.getSchemaOptions();
        }
    }


    /**
     * Copy/clone operator.
     *
     * @param objectToFill schema attribute object
     * @return filled object
     */
    public SchemaTypeChoice cloneProperties(SchemaTypeChoice objectToFill)
    {
        SchemaTypeChoice clone = objectToFill;

        if (clone == null)
        {
            clone = new SchemaTypeChoice();
        }

        if (schemaOptions != null)
        {
            List<SchemaType> schemaTypes = new ArrayList<>();

            for (SchemaTypeProperties properties : schemaOptions)
            {
                if (properties != null)
                {
                    schemaTypes.add(properties.cloneProperties(null));
                }
            }

            if (! schemaTypes.isEmpty())
            {
                clone.setSchemaOptions(schemaTypes);
            }
        }

        super.cloneProperties(clone);

        return clone;
    }

    /**
     * Return the list of alternative schema types that this attribute or asset may use.
     *
     * @return list of schema types
     */
    public List<SchemaTypeProperties> getSchemaOptions()
    {
        if (schemaOptions == null)
        {
            return null;
        }
        else if (schemaOptions.isEmpty())
        {
            return null;
        }

        return schemaOptions;
    }


    /**
     * Set up the list of alternative schema types that this attribute or asset may use.
     *
     * @param schemaOptions list of schema types
     */
    public void setSchemaOptions(List<SchemaTypeProperties> schemaOptions)
    {
        this.schemaOptions = schemaOptions;
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
        SchemaTypeChoiceProperties that = (SchemaTypeChoiceProperties) objectToCompare;
        return Objects.equals(getSchemaOptions(), that.getSchemaOptions());
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaTypeChoiceProperties{" +
                "versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", namespace='" + getNamespace() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}