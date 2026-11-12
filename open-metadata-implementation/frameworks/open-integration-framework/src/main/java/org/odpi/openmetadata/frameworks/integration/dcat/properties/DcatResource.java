/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DcatResource holds the properties that are common to the resources described in a DCAT catalog (dcat:Resource):
 * datasets, dataset series, data services and the catalogs themselves.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatResource
{
    private String                    iri = null;
    private String                    nodeId = null;
    private String                    title = null;
    private Map<String, String>       titleTranslations = null;
    private String                    description = null;
    private Map<String, String>       descriptionTranslations = null;
    private List<String>              identifiers = null;
    private String                    issued = null;
    private String                    modified = null;
    private List<String>              languages = null;
    private DcatAgent                 publisher = null;
    private List<DcatAgent>           creators = null;
    private List<DcatContactPoint>    contactPoints = null;
    private List<String>              keywords = null;
    private List<DcatConcept>         themes = null;
    private String                    resourceType = null;
    private DcatConcept               license = null;
    private String                    rights = null;
    private String                    accessRights = null;
    private List<String>              conformsTo = null;
    private String                    landingPage = null;
    private List<String>              documentation = null;
    private String                    version = null;
    private String                    versionNotes = null;
    private String                    status = null;
    private String                    previousVersion = null;
    private Map<String, List<String>> otherProperties = null;


    /**
     * Default constructor
     */
    public DcatResource()
    {
    }


    /**
     * Return the IRI that identifies the resource.  It is null if the resource is a blank node.
     *
     * @return string IRI
     */
    public String getIri()
    {
        return iri;
    }


    /**
     * Set up the IRI that identifies the resource.  It is null if the resource is a blank node.
     *
     * @param iri string IRI
     */
    public void setIri(String iri)
    {
        this.iri = iri;
    }


    /**
     * Return the identifier of the resource within its document.  It is the IRI of the resource, or a blank node identifier if the resource has no IRI, and is the value used when another resource in the document refers to this one.
     *
     * @return string identifier
     */
    public String getNodeId()
    {
        return nodeId;
    }


    /**
     * Set up the identifier of the resource within its document.  It is the IRI of the resource, or a blank node identifier if the resource has no IRI, and is the value used when another resource in the document refers to this one.
     *
     * @param nodeId string identifier
     */
    public void setNodeId(String nodeId)
    {
        this.nodeId = nodeId;
    }


    /**
     * Return the name of the resource (dct:title).
     *
     * @return string name
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the name of the resource (dct:title).
     *
     * @param title string name
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return the names of the resource in other languages, keyed by language tag.
     *
     * @return map of language to name
     */
    public Map<String, String> getTitleTranslations()
    {
        return titleTranslations;
    }


    /**
     * Set up the names of the resource in other languages, keyed by language tag.
     *
     * @param titleTranslations map of language to name
     */
    public void setTitleTranslations(Map<String, String> titleTranslations)
    {
        this.titleTranslations = titleTranslations;
    }


    /**
     * Return the description of the resource (dct:description).
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the resource (dct:description).
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the descriptions of the resource in other languages, keyed by language tag.
     *
     * @return map of language to description
     */
    public Map<String, String> getDescriptionTranslations()
    {
        return descriptionTranslations;
    }


    /**
     * Set up the descriptions of the resource in other languages, keyed by language tag.
     *
     * @param descriptionTranslations map of language to description
     */
    public void setDescriptionTranslations(Map<String, String> descriptionTranslations)
    {
        this.descriptionTranslations = descriptionTranslations;
    }


    /**
     * Return the identifiers of the resource (dct:identifier).
     *
     * @return list of identifiers
     */
    public List<String> getIdentifiers()
    {
        return identifiers;
    }


    /**
     * Set up the identifiers of the resource (dct:identifier).
     *
     * @param identifiers list of identifiers
     */
    public void setIdentifiers(List<String> identifiers)
    {
        this.identifiers = identifiers;
    }


    /**
     * Return the date that the resource was formally issued (dct:issued).
     *
     * @return string date
     */
    public String getIssued()
    {
        return issued;
    }


    /**
     * Set up the date that the resource was formally issued (dct:issued).
     *
     * @param issued string date
     */
    public void setIssued(String issued)
    {
        this.issued = issued;
    }


    /**
     * Return the date that the resource was last changed (dct:modified).
     *
     * @return string date
     */
    public String getModified()
    {
        return modified;
    }


    /**
     * Set up the date that the resource was last changed (dct:modified).
     *
     * @param modified string date
     */
    public void setModified(String modified)
    {
        this.modified = modified;
    }


    /**
     * Return the languages used in the resource (dct:language).
     *
     * @return list of languages
     */
    public List<String> getLanguages()
    {
        return languages;
    }


    /**
     * Set up the languages used in the resource (dct:language).
     *
     * @param languages list of languages
     */
    public void setLanguages(List<String> languages)
    {
        this.languages = languages;
    }


    /**
     * Return the agent responsible for making the resource available (dct:publisher).
     *
     * @return agent
     */
    public DcatAgent getPublisher()
    {
        return publisher;
    }


    /**
     * Set up the agent responsible for making the resource available (dct:publisher).
     *
     * @param publisher agent
     */
    public void setPublisher(DcatAgent publisher)
    {
        this.publisher = publisher;
    }


    /**
     * Return the agents responsible for producing the resource (dct:creator).
     *
     * @return list of agents
     */
    public List<DcatAgent> getCreators()
    {
        return creators;
    }


    /**
     * Set up the agents responsible for producing the resource (dct:creator).
     *
     * @param creators list of agents
     */
    public void setCreators(List<DcatAgent> creators)
    {
        this.creators = creators;
    }


    /**
     * Return who to contact about the resource (dcat:contactPoint).
     *
     * @return list of contact points
     */
    public List<DcatContactPoint> getContactPoints()
    {
        return contactPoints;
    }


    /**
     * Set up who to contact about the resource (dcat:contactPoint).
     *
     * @param contactPoints list of contact points
     */
    public void setContactPoints(List<DcatContactPoint> contactPoints)
    {
        this.contactPoints = contactPoints;
    }


    /**
     * Return the keywords or tags describing the resource (dcat:keyword).
     *
     * @return list of keywords
     */
    public List<String> getKeywords()
    {
        return keywords;
    }


    /**
     * Set up the keywords or tags describing the resource (dcat:keyword).
     *
     * @param keywords list of keywords
     */
    public void setKeywords(List<String> keywords)
    {
        this.keywords = keywords;
    }


    /**
     * Return the themes (categories) of the resource (dcat:theme).
     *
     * @return list of concepts
     */
    public List<DcatConcept> getThemes()
    {
        return themes;
    }


    /**
     * Set up the themes (categories) of the resource (dcat:theme).
     *
     * @param themes list of concepts
     */
    public void setThemes(List<DcatConcept> themes)
    {
        this.themes = themes;
    }


    /**
     * Return the nature or genre of the resource (dct:type).
     *
     * @return string
     */
    public String getResourceType()
    {
        return resourceType;
    }


    /**
     * Set up the nature or genre of the resource (dct:type).
     *
     * @param resourceType string
     */
    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }


    /**
     * Return the licence under which the resource is made available (dct:license).
     *
     * @return concept
     */
    public DcatConcept getLicense()
    {
        return license;
    }


    /**
     * Set up the licence under which the resource is made available (dct:license).
     *
     * @param license concept
     */
    public void setLicense(DcatConcept license)
    {
        this.license = license;
    }


    /**
     * Return the rights statement for the resource (dct:rights).
     *
     * @return string
     */
    public String getRights()
    {
        return rights;
    }


    /**
     * Set up the rights statement for the resource (dct:rights).
     *
     * @param rights string
     */
    public void setRights(String rights)
    {
        this.rights = rights;
    }


    /**
     * Return who can access the resource (dct:accessRights).
     *
     * @return string
     */
    public String getAccessRights()
    {
        return accessRights;
    }


    /**
     * Set up who can access the resource (dct:accessRights).
     *
     * @param accessRights string
     */
    public void setAccessRights(String accessRights)
    {
        this.accessRights = accessRights;
    }


    /**
     * Return the standards that the resource conforms to (dct:conformsTo).
     *
     * @return list of IRIs
     */
    public List<String> getConformsTo()
    {
        return conformsTo;
    }


    /**
     * Set up the standards that the resource conforms to (dct:conformsTo).
     *
     * @param conformsTo list of IRIs
     */
    public void setConformsTo(List<String> conformsTo)
    {
        this.conformsTo = conformsTo;
    }


    /**
     * Return the web page that gives access to the resource (dcat:landingPage).
     *
     * @return string URL
     */
    public String getLandingPage()
    {
        return landingPage;
    }


    /**
     * Set up the web page that gives access to the resource (dcat:landingPage).
     *
     * @param landingPage string URL
     */
    public void setLandingPage(String landingPage)
    {
        this.landingPage = landingPage;
    }


    /**
     * Return the pages that document the resource (foaf:page).
     *
     * @return list of URLs
     */
    public List<String> getDocumentation()
    {
        return documentation;
    }


    /**
     * Set up the pages that document the resource (foaf:page).
     *
     * @param documentation list of URLs
     */
    public void setDocumentation(List<String> documentation)
    {
        this.documentation = documentation;
    }


    /**
     * Return the version of the resource (dcat:version).
     *
     * @return string version
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the resource (dcat:version).
     *
     * @param version string version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the description of this version of the resource (adms:versionNotes).
     *
     * @return string
     */
    public String getVersionNotes()
    {
        return versionNotes;
    }


    /**
     * Set up the description of this version of the resource (adms:versionNotes).
     *
     * @param versionNotes string
     */
    public void setVersionNotes(String versionNotes)
    {
        this.versionNotes = versionNotes;
    }


    /**
     * Return the lifecycle status of the resource (adms:status).
     *
     * @return string
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * Set up the lifecycle status of the resource (adms:status).
     *
     * @param status string
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * Return the IRI of the previous version of the resource (dcat:previousVersion).
     *
     * @return string IRI
     */
    public String getPreviousVersion()
    {
        return previousVersion;
    }


    /**
     * Set up the IRI of the previous version of the resource (dcat:previousVersion).
     *
     * @param previousVersion string IRI
     */
    public void setPreviousVersion(String previousVersion)
    {
        this.previousVersion = previousVersion;
    }


    /**
     * Return the properties of the resource that have no specific field in this bean, keyed by the IRI of the property.
     *
     * @return map of property IRI to values
     */
    public Map<String, List<String>> getOtherProperties()
    {
        return otherProperties;
    }


    /**
     * Set up the properties of the resource that have no specific field in this bean, keyed by the IRI of the property.
     *
     * @param otherProperties map of property IRI to values
     */
    public void setOtherProperties(Map<String, List<String>> otherProperties)
    {
        this.otherProperties = otherProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatResource{" +
                       "iri='" + iri + '\'' +
                       ", nodeId=" + nodeId +
                       ", title=" + title +
                       ", titleTranslations=" + titleTranslations +
                       ", description=" + description +
                       ", descriptionTranslations=" + descriptionTranslations +
                       ", identifiers=" + identifiers +
                       ", issued=" + issued +
                       ", modified=" + modified +
                       ", languages=" + languages +
                       ", publisher=" + publisher +
                       ", creators=" + creators +
                       ", contactPoints=" + contactPoints +
                       ", keywords=" + keywords +
                       ", themes=" + themes +
                       ", resourceType=" + resourceType +
                       ", license=" + license +
                       ", rights=" + rights +
                       ", accessRights=" + accessRights +
                       ", conformsTo=" + conformsTo +
                       ", landingPage=" + landingPage +
                       ", documentation=" + documentation +
                       ", version=" + version +
                       ", versionNotes=" + versionNotes +
                       ", status=" + status +
                       ", previousVersion=" + previousVersion +
                       ", otherProperties=" + otherProperties +
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
        DcatResource that = (DcatResource) objectToCompare;
        return Objects.equals(iri, that.iri) &&
                       Objects.equals(nodeId, that.nodeId) &&
                       Objects.equals(title, that.title) &&
                       Objects.equals(titleTranslations, that.titleTranslations) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(descriptionTranslations, that.descriptionTranslations) &&
                       Objects.equals(identifiers, that.identifiers) &&
                       Objects.equals(issued, that.issued) &&
                       Objects.equals(modified, that.modified) &&
                       Objects.equals(languages, that.languages) &&
                       Objects.equals(publisher, that.publisher) &&
                       Objects.equals(creators, that.creators) &&
                       Objects.equals(contactPoints, that.contactPoints) &&
                       Objects.equals(keywords, that.keywords) &&
                       Objects.equals(themes, that.themes) &&
                       Objects.equals(resourceType, that.resourceType) &&
                       Objects.equals(license, that.license) &&
                       Objects.equals(rights, that.rights) &&
                       Objects.equals(accessRights, that.accessRights) &&
                       Objects.equals(conformsTo, that.conformsTo) &&
                       Objects.equals(landingPage, that.landingPage) &&
                       Objects.equals(documentation, that.documentation) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(versionNotes, that.versionNotes) &&
                       Objects.equals(status, that.status) &&
                       Objects.equals(previousVersion, that.previousVersion) &&
                       Objects.equals(otherProperties, that.otherProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(iri, nodeId, title, titleTranslations, description, descriptionTranslations, identifiers, issued, modified, languages, publisher, creators, contactPoints, keywords, themes, resourceType, license, rights, accessRights, conformsTo, landingPage, documentation, version, versionNotes, status, previousVersion, otherProperties);
    }
}
