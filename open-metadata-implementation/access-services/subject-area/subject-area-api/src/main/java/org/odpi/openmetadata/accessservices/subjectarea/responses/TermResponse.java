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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Term object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermResponse extends SubjectAreaOMASAPIResponse
{
    private Term term = null;

    /**
     * Default constructor
     */
    public TermResponse()
    {
        this.setResponseCategory(ResponseCategory.Term);
    }
    public TermResponse(Term term)
    {
        this.term=term;
        this.setResponseCategory(ResponseCategory.Term);
    }


    /**
     * Return the Term object.
     *
     * @return term
     */
    public Term getTerm()
    {
        return term;
    }

    /**
     * Set up the Term object.
     *
     * @param term - term object
     */
    public void setTerm(Term term)
    {
        this.term = term;
    }


    @Override
    public String toString()
    {
        return "TermResponse{" +
                "term=" + term +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
