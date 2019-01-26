/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.LibraryTermReference;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * LibraryTermReferenceResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * LibraryTermReference object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LibraryTermReferenceResponse extends SubjectAreaOMASAPIResponse
{
    private LibraryTermReference libraryTermReference = null;

    /**
     * Default constructor
     */
    public LibraryTermReferenceResponse()
    {
        this.setResponseCategory(ResponseCategory.LibraryTermReferenceRelationshipRelationship);
    }
    public LibraryTermReferenceResponse(LibraryTermReference libraryTermReference)
    {
        this();
        this.libraryTermReference =libraryTermReference;
    }

    /**
     * Return the LibraryTermReference object.
     *
     * @return LibraryTermReferenceRelationshipResponse
     */
    public LibraryTermReference getLibraryTermReference()
    {
        return libraryTermReference;
    }

    public void setLibraryTermReference(LibraryTermReference libraryTermReference)
    {
        this.libraryTermReference = libraryTermReference;
    }


    @Override
    public String toString()
    {
        return "LibraryTermReferenceResponse{" +
                "libraryTermReference=" + libraryTermReference +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
