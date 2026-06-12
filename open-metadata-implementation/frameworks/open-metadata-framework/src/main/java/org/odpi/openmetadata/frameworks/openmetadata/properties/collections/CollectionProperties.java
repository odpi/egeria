/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDictionaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataSpecProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

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
                @JsonSubTypes.Type(value = CollectionFolderProperties.class, name = "CollectionFolderProperties"),
                @JsonSubTypes.Type(value = ContextEventCollectionProperties.class, name = "ContextEventCollectionProperties"),
                @JsonSubTypes.Type(value = DataDictionaryProperties.class, name = "DataDictionaryProperties"),
                @JsonSubTypes.Type(value = DataHubProperties.class, name = "DataHubProperties"),
                @JsonSubTypes.Type(value = DataProcessingActionProperties.class, name = "DataProcessingActionProperties"),
                @JsonSubTypes.Type(value = DataProcessingDescriptionProperties.class, name = "DataProcessingDescriptionProperties"),
                @JsonSubTypes.Type(value = DataSpecProperties.class, name = "DataSpecProperties"),
                @JsonSubTypes.Type(value = DesignModelProperties.class, name = "DesignModelProperties"),
                @JsonSubTypes.Type(value = DigitalProductProperties.class, name = "DigitalProductProperties"),
                @JsonSubTypes.Type(value = DigitalProductCatalogProperties.class, name = "DigitalProductCatalogProperties"),
                @JsonSubTypes.Type(value = EventSetProperties.class, name = "EventSetProperties"),
                @JsonSubTypes.Type(value = FolioProperties.class, name = "FolioProperties"),
                @JsonSubTypes.Type(value = GlossaryProperties.class, name = "GlossaryProperties"),
                @JsonSubTypes.Type(value = HomeCollectionProperties.class, name = "HomeCollectionProperties"),
                @JsonSubTypes.Type(value = InformationSupplyChainProperties.class, name = "InformationSupplyChainProperties"),
                @JsonSubTypes.Type(value = NamespaceProperties.class, name = "NamespaceProperties"),
                @JsonSubTypes.Type(value = NamingStandardRuleSetProperties.class, name = "NamingStandardRuleSetProperties"),
                @JsonSubTypes.Type(value = RecentAccessProperties.class, name = "RecentAccessProperties"),
                @JsonSubTypes.Type(value = ReferenceListProperties.class, name = "ReferenceListProperties"),
                @JsonSubTypes.Type(value = ResultsSetProperties.class, name = "ResultsSetProperties"),
                @JsonSubTypes.Type(value = RootCollectionProperties.class, name = "RootCollectionProperties"),
                @JsonSubTypes.Type(value = SecurityListProperties.class, name = "SecurityListProperties"),
                @JsonSubTypes.Type(value = SkillSetProperties.class, name = "SkillSetProperties"),
                @JsonSubTypes.Type(value = SoftwareArchiveProperties.class, name = "SoftwareArchiveProperties"),
                @JsonSubTypes.Type(value = SubjectAreaProperties.class, name = "SubjectAreaProperties"),
                @JsonSubTypes.Type(value = WorkItemListProperties.class, name = "WorkItemListProperties"),
        })
public class CollectionProperties extends AuthoredReferenceableProperties
{
    private String purpose = null;

    /**
     * Default constructor
     */
    public CollectionProperties()
    {
        super();
        super.typeName = OpenMetadataType.COLLECTION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionProperties(CollectionProperties template)
    {
        super(template);

        if (template != null)
        {
            purpose = template.getPurpose();
        }
    }



    /**
     * Return the purpose of the report.
     *
     * @return string
     */
    public String getPurpose()
    {
        return purpose;
    }


    /**
     * Set up the purpose for the report.
     *
     * @param purpose string
     */
    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
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
                "purpose='" + purpose + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        CollectionProperties that = (CollectionProperties) objectToCompare;
        return Objects.equals(purpose, that.purpose);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), purpose);
    }

}
