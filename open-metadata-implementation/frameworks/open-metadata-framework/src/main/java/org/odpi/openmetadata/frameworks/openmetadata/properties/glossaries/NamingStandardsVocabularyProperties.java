/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionKindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NamingStandardsVocabularyProperties is used to classify a glossary that describes the terms used in
 * naming standards.  The terms in this type of glossary are the name parts - such as prime words,
 * modifiers and class words - that are combined to form consistent names.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NamingStandardsVocabularyProperties extends CollectionKindProperties
{
    /**
     * Default constructor
     */
    public NamingStandardsVocabularyProperties()
    {
        super();
        super.typeName = OpenMetadataType.NAMING_STANDARDS_VOCABULARY_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public NamingStandardsVocabularyProperties(NamingStandardsVocabularyProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NamingStandardsVocabularyProperties{} " + super.toString();
    }
}
