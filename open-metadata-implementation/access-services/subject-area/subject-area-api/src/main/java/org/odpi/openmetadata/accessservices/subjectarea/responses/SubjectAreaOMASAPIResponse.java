/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedNameException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubjectAreaOMASAPIResponse provides a common header for Asset Consumer OMAS managed responses to its REST API.
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
                @JsonSubTypes.Type(value = GlossaryResponse.class, name = "GlossaryResponse"),
                @JsonSubTypes.Type(value = GlossaryResponse.class, name = "TermResponse"),
                @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse"),
                @JsonSubTypes.Type(value = ProjectResponse.class, name = "ProjectResponse"),
                /*
                 Exception responses - note that each excpetion has the same 4 Exception orientated fields.
                 Ideally these should be in a superclass. Due to restrictions in the @JsonSubTypes processing it  is only possible to have
                 one level of inheritance at this time.
                 */
                @JsonSubTypes.Type(value = ClassificationExceptionResponse.class, name = "ClassificationExceptionResponse"),
                @JsonSubTypes.Type(value = EntityNotDeletedExceptionResponse.class, name = "EntityNotDeletedExceptionResponse") ,
                @JsonSubTypes.Type(value = FunctionNotSupportedExceptionResponse.class, name = "FunctionNotSupportedExceptionResponse") ,
                @JsonSubTypes.Type(value = GUIDNotPurgedExceptionResponse.class, name = "GUIDNotPurgedExceptionResponse") ,
                @JsonSubTypes.Type(value = InvalidParameterExceptionResponse.class, name = "InvalidParameterExceptionResponse") ,
                @JsonSubTypes.Type(value = MetadataServerUncontactableExceptionResponse.class, name = "MetadataServerUncontactableExceptionResponse") ,
                @JsonSubTypes.Type(value = PossibleClassificationsResponse.class, name = "PossibleClassificationsResponse") ,
                @JsonSubTypes.Type(value = PossibleRelationshipsResponse.class, name = "PossibleRelationshipsResponse") ,
                @JsonSubTypes.Type(value = RelationshipNotDeletedExceptionResponse.class, name = "RelationshipNotDeletedExceptionResponse") ,
                @JsonSubTypes.Type(value = StatusNotsupportedExceptionResponse.class, name = "StatusNotsupportedExceptionResponse") ,
                @JsonSubTypes.Type(value = UnrecognizedGUIDExceptionResponse.class, name = "UnrecognizedGUIDExceptionResponse") ,
                @JsonSubTypes.Type(value = UnrecognizedNameExceptionResponse.class, name = "UnrecognizedNameExceptionResponse") ,
                @JsonSubTypes.Type(value = UserNotAuthorizedExceptionResponse.class, name = "UserNotAuthorizedExceptionResponse")
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
