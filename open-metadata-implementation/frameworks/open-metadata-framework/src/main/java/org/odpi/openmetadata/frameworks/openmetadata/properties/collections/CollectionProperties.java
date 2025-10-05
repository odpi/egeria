/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDictionaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataSpecProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductCatalogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionProperties describes the core properties of a collection.  The collection is a managed list of elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AgreementProperties.class, name = "AgreementProperties"),
                @JsonSubTypes.Type(value = BusinessCapabilityProperties.class, name = "BusinessCapabilityProperties"),
                @JsonSubTypes.Type(value = DataDictionaryProperties.class, name = "DataDictionaryProperties"),
                @JsonSubTypes.Type(value = DataSpecProperties.class, name = "DataSpecProperties"),
                @JsonSubTypes.Type(value = DesignModelProperties.class, name = "DesignModelProperties"),
                @JsonSubTypes.Type(value = DesignModelFolderProperties.class, name = "DesignModelFolderProperties"),
                @JsonSubTypes.Type(value = DigitalProductProperties.class, name = "DigitalProductProperties"),
                @JsonSubTypes.Type(value = DigitalProductCatalogProperties.class, name = "DigitalProductCatalogProperties"),
                @JsonSubTypes.Type(value = EventSetProperties.class, name = "EventSetProperties"),
                @JsonSubTypes.Type(value = GlossaryProperties.class, name = "GlossaryProperties"),
                @JsonSubTypes.Type(value = InformationSupplyChainProperties.class, name = "InformationSupplyChainProperties"),
                @JsonSubTypes.Type(value = NamingStandardRuleSetProperties.class, name = "NamingStandardRuleSetProperties"),
        })
public class CollectionProperties extends ReferenceableProperties
{
    /**
     * Default constructor
     */
    public CollectionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.COLLECTION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionProperties(ReferenceableProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CollectionProperties{" +
                "} " + super.toString();
    }
}
