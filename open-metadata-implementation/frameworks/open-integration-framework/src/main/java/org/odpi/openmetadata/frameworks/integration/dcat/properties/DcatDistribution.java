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
 * DcatDistribution describes a specific representation of a dataset (dcat:Distribution) such as a downloadable file
 * or an access point.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatDistribution
{
    private String                    iri = null;
    private String                    title = null;
    private String                    description = null;
    private String                    accessURL = null;
    private String                    downloadURL = null;
    private String                    mediaType = null;
    private String                    format = null;
    private Long                      byteSize = null;
    private String                    compressFormat = null;
    private String                    packageFormat = null;
    private String                    issued = null;
    private String                    modified = null;
    private DcatConcept               license = null;
    private String                    rights = null;
    private String                    accessRights = null;
    private List<String>              conformsTo = null;
    private List<String>              languages = null;
    private String                    checksumAlgorithm = null;
    private String                    checksumValue = null;
    private List<String>              accessServices = null;
    private Map<String, List<String>> otherProperties = null;


    /**
     * Default constructor
     */
    public DcatDistribution()
    {
    }


    /**
     * Return the IRI that identifies the distribution.
     *
     * @return string IRI
     */
    public String getIri()
    {
        return iri;
    }


    /**
     * Set up the IRI that identifies the distribution.
     *
     * @param iri string IRI
     */
    public void setIri(String iri)
    {
        this.iri = iri;
    }


    /**
     * Return the name of the distribution (dct:title).
     *
     * @return string name
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the name of the distribution (dct:title).
     *
     * @param title string name
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return the description of the distribution (dct:description).
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the distribution (dct:description).
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the URL of a resource that gives access to the distribution (dcat:accessURL).
     *
     * @return string URL
     */
    public String getAccessURL()
    {
        return accessURL;
    }


    /**
     * Set up the URL of a resource that gives access to the distribution (dcat:accessURL).
     *
     * @param accessURL string URL
     */
    public void setAccessURL(String accessURL)
    {
        this.accessURL = accessURL;
    }


    /**
     * Return the URL of the downloadable file (dcat:downloadURL).
     *
     * @return string URL
     */
    public String getDownloadURL()
    {
        return downloadURL;
    }


    /**
     * Set up the URL of the downloadable file (dcat:downloadURL).
     *
     * @param downloadURL string URL
     */
    public void setDownloadURL(String downloadURL)
    {
        this.downloadURL = downloadURL;
    }


    /**
     * Return the IANA media type of the distribution (dcat:mediaType).
     *
     * @return string media type
     */
    public String getMediaType()
    {
        return mediaType;
    }


    /**
     * Set up the IANA media type of the distribution (dcat:mediaType).
     *
     * @param mediaType string media type
     */
    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }


    /**
     * Return the file format of the distribution (dct:format).
     *
     * @return string format
     */
    public String getFormat()
    {
        return format;
    }


    /**
     * Set up the file format of the distribution (dct:format).
     *
     * @param format string format
     */
    public void setFormat(String format)
    {
        this.format = format;
    }


    /**
     * Return the size of the distribution in bytes (dcat:byteSize).
     *
     * @return long
     */
    public Long getByteSize()
    {
        return byteSize;
    }


    /**
     * Set up the size of the distribution in bytes (dcat:byteSize).
     *
     * @param byteSize long
     */
    public void setByteSize(Long byteSize)
    {
        this.byteSize = byteSize;
    }


    /**
     * Return the compression format of the distribution (dcat:compressFormat).
     *
     * @return string format
     */
    public String getCompressFormat()
    {
        return compressFormat;
    }


    /**
     * Set up the compression format of the distribution (dcat:compressFormat).
     *
     * @param compressFormat string format
     */
    public void setCompressFormat(String compressFormat)
    {
        this.compressFormat = compressFormat;
    }


    /**
     * Return the packaging format of the distribution (dcat:packageFormat).
     *
     * @return string format
     */
    public String getPackageFormat()
    {
        return packageFormat;
    }


    /**
     * Set up the packaging format of the distribution (dcat:packageFormat).
     *
     * @param packageFormat string format
     */
    public void setPackageFormat(String packageFormat)
    {
        this.packageFormat = packageFormat;
    }


    /**
     * Return the date that the distribution was formally issued (dct:issued).
     *
     * @return string date
     */
    public String getIssued()
    {
        return issued;
    }


    /**
     * Set up the date that the distribution was formally issued (dct:issued).
     *
     * @param issued string date
     */
    public void setIssued(String issued)
    {
        this.issued = issued;
    }


    /**
     * Return the date that the distribution was last changed (dct:modified).
     *
     * @return string date
     */
    public String getModified()
    {
        return modified;
    }


    /**
     * Set up the date that the distribution was last changed (dct:modified).
     *
     * @param modified string date
     */
    public void setModified(String modified)
    {
        this.modified = modified;
    }


    /**
     * Return the licence under which the distribution is made available (dct:license).
     *
     * @return concept
     */
    public DcatConcept getLicense()
    {
        return license;
    }


    /**
     * Set up the licence under which the distribution is made available (dct:license).
     *
     * @param license concept
     */
    public void setLicense(DcatConcept license)
    {
        this.license = license;
    }


    /**
     * Return the rights statement for the distribution (dct:rights).
     *
     * @return string
     */
    public String getRights()
    {
        return rights;
    }


    /**
     * Set up the rights statement for the distribution (dct:rights).
     *
     * @param rights string
     */
    public void setRights(String rights)
    {
        this.rights = rights;
    }


    /**
     * Return who can access the distribution (dct:accessRights).
     *
     * @return string
     */
    public String getAccessRights()
    {
        return accessRights;
    }


    /**
     * Set up who can access the distribution (dct:accessRights).
     *
     * @param accessRights string
     */
    public void setAccessRights(String accessRights)
    {
        this.accessRights = accessRights;
    }


    /**
     * Return the standards that the distribution conforms to (dct:conformsTo).
     *
     * @return list of IRIs
     */
    public List<String> getConformsTo()
    {
        return conformsTo;
    }


    /**
     * Set up the standards that the distribution conforms to (dct:conformsTo).
     *
     * @param conformsTo list of IRIs
     */
    public void setConformsTo(List<String> conformsTo)
    {
        this.conformsTo = conformsTo;
    }


    /**
     * Return the languages used in the distribution (dct:language).
     *
     * @return list of languages
     */
    public List<String> getLanguages()
    {
        return languages;
    }


    /**
     * Set up the languages used in the distribution (dct:language).
     *
     * @param languages list of languages
     */
    public void setLanguages(List<String> languages)
    {
        this.languages = languages;
    }


    /**
     * Return the algorithm used to calculate the checksum (spdx:algorithm).
     *
     * @return string algorithm
     */
    public String getChecksumAlgorithm()
    {
        return checksumAlgorithm;
    }


    /**
     * Set up the algorithm used to calculate the checksum (spdx:algorithm).
     *
     * @param checksumAlgorithm string algorithm
     */
    public void setChecksumAlgorithm(String checksumAlgorithm)
    {
        this.checksumAlgorithm = checksumAlgorithm;
    }


    /**
     * Return the checksum of the downloadable file (spdx:checksumValue).
     *
     * @return string checksum
     */
    public String getChecksumValue()
    {
        return checksumValue;
    }


    /**
     * Set up the checksum of the downloadable file (spdx:checksumValue).
     *
     * @param checksumValue string checksum
     */
    public void setChecksumValue(String checksumValue)
    {
        this.checksumValue = checksumValue;
    }


    /**
     * Return the IRIs of the data services that give access to the distribution (dcat:accessService).
     *
     * @return list of IRIs
     */
    public List<String> getAccessServices()
    {
        return accessServices;
    }


    /**
     * Set up the IRIs of the data services that give access to the distribution (dcat:accessService).
     *
     * @param accessServices list of IRIs
     */
    public void setAccessServices(List<String> accessServices)
    {
        this.accessServices = accessServices;
    }


    /**
     * Return the properties of the distribution that have no specific field in this bean, keyed by the IRI of the property.
     *
     * @return map of property IRI to values
     */
    public Map<String, List<String>> getOtherProperties()
    {
        return otherProperties;
    }


    /**
     * Set up the properties of the distribution that have no specific field in this bean, keyed by the IRI of the property.
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
        return "DcatDistribution{" +
                       "iri='" + iri + '\'' +
                       ", title=" + title +
                       ", description=" + description +
                       ", accessURL=" + accessURL +
                       ", downloadURL=" + downloadURL +
                       ", mediaType=" + mediaType +
                       ", format=" + format +
                       ", byteSize=" + byteSize +
                       ", compressFormat=" + compressFormat +
                       ", packageFormat=" + packageFormat +
                       ", issued=" + issued +
                       ", modified=" + modified +
                       ", license=" + license +
                       ", rights=" + rights +
                       ", accessRights=" + accessRights +
                       ", conformsTo=" + conformsTo +
                       ", languages=" + languages +
                       ", checksumAlgorithm=" + checksumAlgorithm +
                       ", checksumValue=" + checksumValue +
                       ", accessServices=" + accessServices +
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
        DcatDistribution that = (DcatDistribution) objectToCompare;
        return Objects.equals(iri, that.iri) &&
                       Objects.equals(title, that.title) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(accessURL, that.accessURL) &&
                       Objects.equals(downloadURL, that.downloadURL) &&
                       Objects.equals(mediaType, that.mediaType) &&
                       Objects.equals(format, that.format) &&
                       Objects.equals(byteSize, that.byteSize) &&
                       Objects.equals(compressFormat, that.compressFormat) &&
                       Objects.equals(packageFormat, that.packageFormat) &&
                       Objects.equals(issued, that.issued) &&
                       Objects.equals(modified, that.modified) &&
                       Objects.equals(license, that.license) &&
                       Objects.equals(rights, that.rights) &&
                       Objects.equals(accessRights, that.accessRights) &&
                       Objects.equals(conformsTo, that.conformsTo) &&
                       Objects.equals(languages, that.languages) &&
                       Objects.equals(checksumAlgorithm, that.checksumAlgorithm) &&
                       Objects.equals(checksumValue, that.checksumValue) &&
                       Objects.equals(accessServices, that.accessServices) &&
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
        return Objects.hash(iri, title, description, accessURL, downloadURL, mediaType, format, byteSize, compressFormat, packageFormat, issued, modified, license, rights, accessRights, conformsTo, languages, checksumAlgorithm, checksumValue, accessServices, otherProperties);
    }
}
