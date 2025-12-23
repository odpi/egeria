/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceProperties stores information about a link to an external resource that is relevant to
 * open metadata.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CitedDocumentProperties.class, name = "CitedDocumentProperties"),
                @JsonSubTypes.Type(value = ExternalDataSourceProperties.class, name = "ExternalDataSourceProperties"),
                @JsonSubTypes.Type(value = ExternalModelSourceProperties.class, name = "ExternalModelSourceProperties"),
                @JsonSubTypes.Type(value = ExternalSourceCodeProperties.class, name = "ExternalSourceCodeProperties"),
                @JsonSubTypes.Type(value = RelatedMediaProperties.class, name = "RelatedMediaProperties"),
        })
public class ExternalReferenceProperties extends ReferenceableProperties
{
    private String              referenceTitle    = null;
    private String              referenceAbstract = null;
    private List<String>        authors           = null;
    private String              organization      = null;
    private Map<String, String> sources           = null;
    private String              license           = null;
    private String              copyright         = null;
    private String              attribution       = null;


    /**
     * Default constructor
     */
    public ExternalReferenceProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXTERNAL_REFERENCE.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ExternalReferenceProperties(ExternalReferenceProperties template)
    {
        super(template);

        if (template != null)
        {
            referenceTitle    = template.getReferenceTitle();
            referenceAbstract = template.getReferenceAbstract();
            license           = template.getLicense();
            copyright         = template.getCopyright();
            organization      = template.getOrganization();
            authors           = template.getAuthors();
            sources           = template.getSources();
            attribution       = template.getAttribution();
        }
    }


    /**
     * Return the title of this reference.
     *
     * @return string
     */
    public String getReferenceTitle()
    {
        return referenceTitle;
    }


    /**
     * Set up the title of this reference.
     *
     * @param referenceTitle string
     */
    public void setReferenceTitle(String referenceTitle)
    {
        this.referenceTitle = referenceTitle;
    }


    /**
     * Return the short description of the reference.
     *
     * @return String link description.
     */
    public String getReferenceAbstract() { return referenceAbstract; }


    /**
     * Set up the short description of the reference.
     *
     * @param referenceAbstract String description
     */
    public void setReferenceAbstract(String referenceAbstract)
    {
        this.referenceAbstract = referenceAbstract;
    }


    /**
     * Return the license granted for this external reference.
     *
     * @return string
     */
    public String getLicense() { return license; }


    /**
     * Set up the license granted for this external reference.
     *
     * @param license string
     */
    public void setLicense(String license)
    {
        this.license = license;
    }


    /**
     * Return the copyright statement for this external reference.
     *
     * @return string
     */
    public String getCopyright() { return copyright; }


    /**
     * Set up the copyright statement for this external reference.
     *
     * @param copyright string
     */
    public void setCopyright(String copyright)
    {
        this.copyright = copyright;
    }


    /**
     * Return the name of the organization that owns the resource that this external reference represents.
     *
     * @return String organization name
     */
    public String getOrganization() { return organization; }


    /**
     * Set up the name of the organization that owns the resource that this external reference represents.
     *
     * @param organization String name
     */
    public void setOrganization(String organization)
    {
        this.organization = organization;
    }


    /**
     * Return the list of authors.
     *
     * @return list
     */
    public List<String> getAuthors()
    {
        return authors;
    }


    /**
     * Set up the list of authors.
     *
     * @param authors list
     */
    public void setAuthors(List<String> authors)
    {
        this.authors = authors;
    }


    /**
     * Return the names and addresses of all of the sources of this information.
     *
     * @return map
     */
    public Map<String, String> getSources()
    {
        return sources;
    }


    /**
     * Set up the names and addresses of all of the sources of this information.
     *
     * @param sources map
     */
    public void setSources(Map<String, String> sources)
    {
        this.sources = sources;
    }


    /**
     * Return the attribution statement required when this reference is used.
     *
     * @return string
     */
    public String getAttribution()
    {
        return attribution;
    }


    /**
     * Set up the attribution statement required when this reference is used.
     *
     * @param attribution string
     */
    public void setAttribution(String attribution)
    {
        this.attribution = attribution;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalReferenceProperties{" +
                "referenceTitle='" + referenceTitle + '\'' +
                ", referenceAbstract='" + referenceAbstract + '\'' +
                ", authors=" + authors +
                ", organization='" + organization + '\'' +
                ", sources=" + sources +
                ", license='" + license + '\'' +
                ", copyright='" + copyright + '\'' +
                ", attribution='" + attribution + '\'' +
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
        ExternalReferenceProperties that = (ExternalReferenceProperties) objectToCompare;
        return Objects.equals(referenceTitle, that.referenceTitle) &&
                Objects.equals(referenceAbstract, that.referenceAbstract) &&
                Objects.equals(authors, that.authors) &&
                Objects.equals(organization, that.organization) &&
                Objects.equals(sources, that.sources) &&
                Objects.equals(license, that.license) &&
                Objects.equals(copyright, that.copyright) &&
                Objects.equals(attribution, that.attribution);
    }

    /**
     * Uses the guid to create a hashcode.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), referenceTitle, referenceAbstract, authors, organization, sources, license, copyright, attribution);
    }
}