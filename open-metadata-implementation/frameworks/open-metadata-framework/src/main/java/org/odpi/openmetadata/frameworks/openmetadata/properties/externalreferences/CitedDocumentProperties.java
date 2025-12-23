/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceProperties stores information about an link to an external resource that is relevant to
 * open metadata.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CitedDocumentProperties extends ExternalReferenceProperties
{
    private int          numberOfPages           = 0;
    private String       pageRange               = null;
    private String       publicationSeries       = null;
    private String       publicationSeriesVolume = null;
    private String       publisher               = null;
    private String       edition                 = null;
    private Date         firstPublicationDate    = null;
    private Date         publicationDate         = null;
    private String       publicationCity         = null;
    private String       publicationYear         = null;
    private List<String> publicationNumbers      = null;


    /**
     * Default constructor
     */
    public CitedDocumentProperties()
    {
        super();
        super.typeName = OpenMetadataType.CITED_DOCUMENT.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public CitedDocumentProperties(CitedDocumentProperties template)
    {
        super(template);

        if (template != null)
        {
            numberOfPages = template.getNumberOfPages();
            pageRange         = template.getPageRange();
            publicationSeries       = template.getPublicationSeries();
            publicationSeriesVolume = template.getPublicationSeriesVolume();
            publisher               = template.getPublisher();
            edition         = template.getEdition();
            firstPublicationDate = template.getFirstPublicationDate();
            publicationDate = template.getPublicationDate();
            publicationCity = template.getPublicationCity();
            publicationYear = template.getPublicationYear();
            publicationNumbers = template.getPublicationNumbers();
        }
    }


    /**
     * Return the page range of the reference.
     *
     * @return string
     */
    public String getPageRange()
    {
        return pageRange;
    }


    /**
     * Set up the page range of the reference.
     *
     * @param pageRange string
     */
    public void setPageRange(String pageRange)
    {
        this.pageRange = pageRange;
    }


    /**
     * Return the publication series that this reference comes from.
     *
     * @return string
     */
    public String getPublicationSeries() { return publicationSeries; }


    /**
     * Set up the publication series that this reference comes from.
     *
     * @param publicationSeries string
     */
    public void setPublicationSeries(String publicationSeries)
    {
        this.publicationSeries = publicationSeries;
    }


    /**
     * Return the volume.
     *
     * @return String description
     */
    public String getPublicationSeriesVolume() { return publicationSeriesVolume; }


    /**
     * Set up the volume.
     *
     * @param publicationSeriesVolume String description
     */
    public void setPublicationSeriesVolume(String publicationSeriesVolume)
    {
        this.publicationSeriesVolume = publicationSeriesVolume;
    }


    /**
     * Return the name of the publisher.
     *
     * @return string
     */
    public String getPublisher() { return publisher; }


    /**
     * Set up the name of the publisher.
     *
     * @param publisher string
     */
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }



    /**
     * Return the edition of the resource that this external reference represents.
     *
     * @return string identifier
     */
    public String getEdition() { return edition; }


    /**
     * Set up the edition of the resource that this external reference represents.
     *
     * @param edition string identifier
     */
    public void setEdition(String edition)
    {
        this.edition = edition;
    }


    /**
     * Return the name of the city where the document was published from.
     *
     * @return string
     */
    public String getPublicationCity() { return publicationCity; }


    /**
     * Set up the name of the city where the document was published from.
     *
     * @param publicationCity string
     */
    public void setPublicationCity(String publicationCity)
    {
        this.publicationCity = publicationCity;
    }

    /**
     * Return the number of pages in the reference.
     *
     * @return int
     */
    public int getNumberOfPages()
    {
        return numberOfPages;
    }


    /**
     * Set up the number of pages in the reference.
     *
     * @param numberOfPages int
     */
    public void setNumberOfPages(int numberOfPages)
    {
        this.numberOfPages = numberOfPages;
    }


    /**
     * Return the date when this was first published.
     *
     * @return date
     */
    public Date getFirstPublicationDate()
    {
        return firstPublicationDate;
    }


    /**
     * Set up the date when this was first published.
     *
     * @param firstPublicationDate date
     */
    public void setFirstPublicationDate(Date firstPublicationDate)
    {
        this.firstPublicationDate = firstPublicationDate;
    }


    /**
     * Return the publication date of this version.
     *
     * @return date
     */
    public Date getPublicationDate()
    {
        return publicationDate;
    }


    /**
     * Set up the publication date of this version.
     *
     * @param publicationDate date
     */
    public void setPublicationDate(Date publicationDate)
    {
        this.publicationDate = publicationDate;
    }

    /**
     * Return the year of publication of this version.
     *
     * @return string
     */
    public String getPublicationYear()
    {
        return publicationYear;
    }


    /**
     * Set up the year of publication of this version.
     *
     * @param publicationYear string
     */
    public void setPublicationYear(String publicationYear)
    {
        this.publicationYear = publicationYear;
    }


    /**
     * Set up the identifiers used to uniquely identify this document (eg ISBN).
     *
     * @return string
     */
    public List<String> getPublicationNumbers()
    {
        return publicationNumbers;
    }


    /**
     * Set up the identifiers used to uniquely identify this document (eg ISBN).
     *
     * @param publicationNumbers string
     */
    public void setPublicationNumbers(List<String> publicationNumbers)
    {
        this.publicationNumbers = publicationNumbers;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CitedDocumentProperties{" +
                "numberOfPages=" + numberOfPages +
                ", pageRange='" + pageRange + '\'' +
                ", publicationSeries='" + publicationSeries + '\'' +
                ", publicationSeriesVolume='" + publicationSeriesVolume + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition='" + edition + '\'' +
                ", firstPublicationDate=" + firstPublicationDate +
                ", publicationDate=" + publicationDate +
                ", publicationCity='" + publicationCity + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", publicationNumbers=" + publicationNumbers +
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
        CitedDocumentProperties that = (CitedDocumentProperties) objectToCompare;
        return numberOfPages == that.numberOfPages &&
                Objects.equals(pageRange, that.pageRange) &&
                Objects.equals(publicationSeries, that.publicationSeries) &&
                Objects.equals(publicationSeriesVolume, that.publicationSeriesVolume) &&
                Objects.equals(publisher, that.publisher) && Objects.equals(edition, that.edition) &&
                Objects.equals(firstPublicationDate, that.firstPublicationDate) &&
                Objects.equals(publicationDate, that.publicationDate) &&
                Objects.equals(publicationCity, that.publicationCity) &&
                Objects.equals(publicationYear, that.publicationYear) &&
                Objects.equals(publicationNumbers, that.publicationNumbers);
    }


    /**
     * Uses the guid to create a hashcode.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), numberOfPages, pageRange, publicationSeries, publicationSeriesVolume, publisher, edition, firstPublicationDate, publicationDate, publicationCity, publicationYear, publicationNumbers);
    }
}