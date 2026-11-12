/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DcatConcept describes a controlled value used in a DCAT description, such as a theme (skos:Concept), a licence
 * (dct:LicenseDocument) or a media type.  It is identified by its IRI and may also have a human-readable label.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatConcept
{
    private String iri = null;
    private String label = null;


    /**
     * Default constructor
     */
    public DcatConcept()
    {
    }


    /**
     * Return the IRI that identifies the concept.
     *
     * @return string IRI
     */
    public String getIri()
    {
        return iri;
    }


    /**
     * Set up the IRI that identifies the concept.
     *
     * @param iri string IRI
     */
    public void setIri(String iri)
    {
        this.iri = iri;
    }


    /**
     * Return the human-readable label of the concept (skos:prefLabel, dct:title or rdfs:label).
     *
     * @return string label
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the human-readable label of the concept (skos:prefLabel, dct:title or rdfs:label).
     *
     * @param label string label
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatConcept{" +
                       "iri='" + iri + '\'' +
                       ", label=" + label +
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
        DcatConcept that = (DcatConcept) objectToCompare;
        return Objects.equals(iri, that.iri) &&
                       Objects.equals(label, that.label);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(iri, label);
    }
}
