/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.LibraryCategoryReference;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * LibraryCategoryReferenceResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * LibraryCategoryReference object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LibraryCategoryReferenceResponse extends SubjectAreaOMASAPIResponse
{
    private LibraryCategoryReference libraryCategoryReference = null;

    /**
     * Default constructor
     */
    public LibraryCategoryReferenceResponse()
    {
        this.setResponseCategory(ResponseCategory.LibraryCategoryReferenceRelationshipRelationship);
    }
    public LibraryCategoryReferenceResponse(LibraryCategoryReference libraryCategoryReference)
    {
        this.libraryCategoryReference =libraryCategoryReference;
        this.setResponseCategory(ResponseCategory.LibraryCategoryReferenceRelationshipRelationship);
    }

    /**
     * Return the LibraryCategoryReference object.
     *
     * @return LibraryCategoryReferenceRelationshipResponse
     */
    public LibraryCategoryReference getLibraryCategoryReference()
    {
        return libraryCategoryReference;
    }

    public void setLibraryCategoryReference(LibraryCategoryReference libraryCategoryReference)
    {
        this.libraryCategoryReference = libraryCategoryReference;
    }


    @Override
    public String toString()
    {
        return "LibraryCategoryReferenceResponse{" +
                "libraryCategoryReference=" + libraryCategoryReference +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
