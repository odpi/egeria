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
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedGUIDException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UserNotAuthorizedExceptionResponse is the response that wraps a UserNotAuthorizedException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UnrecognizedGUIDExceptionResponse extends SubjectAreaOMASAPIExceptionResponse
{
    public String getGuid() {
        return guid;
    }

    private final String guid;
    // TODO add some useful fields here - like the field name that was incorrect and its value.
    /**
     * Default constructor
     */
    public UnrecognizedGUIDExceptionResponse(SubjectAreaCheckedExceptionBase e)
    {
        super(e);
        this.setResponseCategory(ResponseCategory.UnrecognizedGUIDException);
        this.guid=((UnrecognizedGUIDException)e).getGuid();

    }

    @Override
    public String toString()
    {
        return "UserNotAuthorizedExceptionResponse{" +
                super.toString() +

                "category=" + this.responseCategory +
                '}';
    }
}
