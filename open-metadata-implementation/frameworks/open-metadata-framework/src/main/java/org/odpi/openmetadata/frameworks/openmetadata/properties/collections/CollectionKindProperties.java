/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDictionaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataSpecProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.CanonicalVocabularyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.NamingStandardsVocabularyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.TaxonomyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EditingCollectionProperties is used to provide the properties for an EditingCollection classification.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CanonicalVocabularyProperties.class, name = "CanonicalVocabularyProperties"),
                @JsonSubTypes.Type(value = DataSharingAgreementProperties.class, name = "DataSharingAgreementProperties"),
                @JsonSubTypes.Type(value = EditingCollectionProperties.class, name = "EditingCollectionProperties"),
                @JsonSubTypes.Type(value = NamingStandardsVocabularyProperties.class, name = "NamingStandardsVocabularyProperties"),
                @JsonSubTypes.Type(value = ScopingCollectionProperties.class, name = "ScopingCollectionProperties"),
                @JsonSubTypes.Type(value = StagingCollectionProperties.class, name = "StagingCollectionProperties"),
                @JsonSubTypes.Type(value = TaxonomyProperties.class, name = "TaxonomyProperties"),
        })
public class CollectionKindProperties extends ClassificationBeanProperties
{
    /**
     * Default constructor
     */
    public CollectionKindProperties()
    {
        super();
        super.typeName = OpenMetadataType.COLLECTION_KIND_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public CollectionKindProperties(CollectionKindProperties template)
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
        return "CollectionKindProperties{} " + super.toString();
    }
}
