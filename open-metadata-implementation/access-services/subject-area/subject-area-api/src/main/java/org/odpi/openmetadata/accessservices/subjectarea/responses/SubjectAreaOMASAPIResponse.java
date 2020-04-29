/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.LibraryTermReference;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubjectAreaOMASAPIResponse provides a common header for Subject Area OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CategoryResponse.class, name = "CategoryResponse"),
                @JsonSubTypes.Type(value = CategoriesResponse.class, name = "CategoriesResponse"),
                @JsonSubTypes.Type(value = GlossaryResponse.class, name = "GlossaryResponse"),
                @JsonSubTypes.Type(value = GlossariesResponse.class, name = "GlossariesResponse"),
                @JsonSubTypes.Type(value = SubjectAreaDefinitionResponse.class, name = "SubjectAreaDefinitionResponse"),
                @JsonSubTypes.Type(value = SubjectAreaDefinitionsResponse.class, name = "SubjectAreaDefinitionsResponse"),
                @JsonSubTypes.Type(value = TermResponse.class, name = "TermResponse"),
                @JsonSubTypes.Type(value = TermsResponse.class, name = "TermsResponse"),
                @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse"),
                @JsonSubTypes.Type(value = ProjectResponse.class, name = "ProjectResponse"),
                @JsonSubTypes.Type(value = ProjectsResponse.class, name = "ProjectsResponse"),
                @JsonSubTypes.Type(value = LinesResponse.class, name = "LinesResponse"),

                // Lines

                // term to term relationship responses
                @JsonSubTypes.Type(value = TermHASARelationshipResponse.class, name = "TermHASARelationshipResponse"),
                @JsonSubTypes.Type(value = RelatedTermResponse.class, name = "RelatedTermResponse"),
                @JsonSubTypes.Type(value = SynonymRelationshipResponse.class, name = "SynonymRelationshipResponse"),
                @JsonSubTypes.Type(value = AntonymRelationshipResponse.class, name = "AntonymRelationshipResponse"),
                @JsonSubTypes.Type(value = PreferredTermRelationshipResponse.class, name = "PreferredTermRelationshipResponse"),
                @JsonSubTypes.Type(value = ReplacementRelationshipResponse.class, name = "ReplacementRelationshipResponse"),
                @JsonSubTypes.Type(value = TranslationRelationshipResponse.class, name = "TranslationRelationshipResponse"),
                @JsonSubTypes.Type(value = ValidValueRelationshipResponse.class, name = "ValidValueRelationshipResponse"),
                @JsonSubTypes.Type(value = UsedInContextRelationshipResponse.class, name = "UsedInContextRelationshipResponse"),
                @JsonSubTypes.Type(value = TermISATYPEOFRelationshipResponse.class, name = "TermISATYPEOFRelationshipResponse"),
                @JsonSubTypes.Type(value = TermTYPEDBYRelationshipResponse.class, name = "TermTYPEDBYRelationshipResponse"),
                @JsonSubTypes.Type(value = TermISARelationshipResponse.class,name = "TermISARelationshipResponse"),
                // term to glossary response
                @JsonSubTypes.Type(value = TermAnchorRelationshipResponse.class,name = "TermAnchorRelationshipResponse"),
                // category to glossary response
                @JsonSubTypes.Type(value =CategoryAnchorRelationshipResponse.class,name = "CategoryAnchorRelationshipResponse"),
                // category to term response
                @JsonSubTypes.Type(value = TermCategorizationRelationshipResponse.class, name = "TermCategorizationRelationshipResponse"),
                // Term to asset response
                @JsonSubTypes.Type(value = SemanticAssignementRelationshipResponse.class, name = "SemanticAssignementRelationshipResponse"),
                // external glossary responses
                @JsonSubTypes.Type(value = LibraryTermReference.class, name = "LibraryTermReference"),
                @JsonSubTypes.Type(value = LibraryCategoryReferenceResponse.class, name = "LibraryCategoryReferenceResponse"),

                @JsonSubTypes.Type(value = ProjectScopeRelationshipResponse.class, name = "ProjectScopeRelationshipResponse"),
                /*
                 Exception responses - note that each exception has the same 4 Exception orientated fields.
                 Ideally these should be in a superclass. Due to restrictions in the @JsonSubTypes processing it is only possible to have
                 one level of inheritance at this time.
                 */
                @JsonSubTypes.Type(value = ClassificationExceptionResponse.class, name = "ClassificationExceptionResponse"),
                @JsonSubTypes.Type(value = EntityNotDeletedExceptionResponse.class, name = "EntityNotDeletedExceptionResponse") ,
                @JsonSubTypes.Type(value = FunctionNotSupportedExceptionResponse.class, name = "FunctionNotSupportedExceptionResponse") ,
                @JsonSubTypes.Type(value = GUIDNotPurgedExceptionResponse.class, name = "GUIDNotPurgedExceptionResponse") ,
                @JsonSubTypes.Type(value = InvalidParameterExceptionResponse.class, name = "InvalidParameterExceptionResponse") ,
                @JsonSubTypes.Type(value = MetadataServerUncontactableExceptionResponse.class, name = "MetadataServerUncontactableExceptionResponse") ,
                @JsonSubTypes.Type(value = RelationshipNotDeletedExceptionResponse.class, name = "RelationshipNotDeletedExceptionResponse") ,
                @JsonSubTypes.Type(value = StatusNotsupportedExceptionResponse.class, name = "StatusNotsupportedExceptionResponse") ,
                @JsonSubTypes.Type(value = UnrecognizedGUIDExceptionResponse.class, name = "UnrecognizedGUIDExceptionResponse") ,
                @JsonSubTypes.Type(value = UnrecognizedNameExceptionResponse.class, name = "UnrecognizedNameExceptionResponse") ,
                @JsonSubTypes.Type(value = UserNotAuthorizedExceptionResponse.class, name = "UserNotAuthorizedExceptionResponse"),
                @JsonSubTypes.Type(value = GraphResponse.class, name = "GraphResponse")

        })
public abstract class SubjectAreaOMASAPIResponse
{
    protected int       relatedHTTPCode = 200;
    protected ResponseCategory responseCategory;

    /**
     * Default constructor
     */
    public SubjectAreaOMASAPIResponse()
    {
    }


    /**
     * Return the HTTP Code to use if forwarding response to HTTP client.
     *
     * @return integer HTTP status code
     */
    public int getRelatedHTTPCode()
    {
        return relatedHTTPCode;
    }


    /**
     * Set up the HTTP Code to use if forwarding response to HTTP client.
     *
     * @param relatedHTTPCode - integer HTTP status code
     */
    public void setRelatedHTTPCode(int relatedHTTPCode)
    {
        this.relatedHTTPCode = relatedHTTPCode;
    }

    public ResponseCategory getResponseCategory() {
        return responseCategory;
    }

    public void setResponseCategory(ResponseCategory responseCategory) {
        this.responseCategory = responseCategory;
    }

    @Override
    public String toString()
    {
        return "relatedHTTPCode=" + relatedHTTPCode +
                ", ResponseCategory='" + responseCategory + '\'' ;

    }
}