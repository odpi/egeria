/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataDictionaryProperties describes the core properties of a data dictionary.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataDictionaryProperties extends CollectionProperties
{
    /**
     * Default constructor
     */
    public DataDictionaryProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataDictionaryProperties(DataDictionaryProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataDictionaryProperties(CollectionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataDictionaryProperties{} " + super.toString();
    }
}
