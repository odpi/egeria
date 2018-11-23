/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Translation;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TranslationResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Translation object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermTranslationRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private Translation translation= null;

    /**
     * Default constructor
     */
    public TermTranslationRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TranslationRelationship);
    }
    public TermTranslationRelationshipResponse(Translation translation)
    {
        this.translation=translation;
        this.setResponseCategory(ResponseCategory.TranslationRelationship);
    }


    /**
     * Return the Translation object.
     *
     * @return translationResponse
     */
    public Translation getTranslation()
    {
        return translation;
    }

    public void setTranslation(Translation translation)
    {
        this.translation = translation;
    }


    @Override
    public String toString()
    {
        return "TranslationResponse{" +
                "translation=" + translation +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
