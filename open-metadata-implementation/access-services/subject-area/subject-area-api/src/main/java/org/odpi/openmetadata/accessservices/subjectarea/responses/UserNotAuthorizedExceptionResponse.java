/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UserNotAuthorizedExceptionResponse is the response that wraps a UserNotAuthorizedException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserNotAuthorizedExceptionResponse extends SubjectAreaOMASAPIResponse
{

    protected String userId = null;
    public UserNotAuthorizedExceptionResponse(SubjectAreaCheckedException e)
    {
        super(e);
        this.setResponseCategory(ResponseCategory.UserNotAuthorizedException);
        userId = ((UserNotAuthorizedException)e).getUserId();
    }

    @Override
    public String toString()
    {
        return "UserNotAuthorizedExceptionResponse{" +
                super.toString() + ", userId = " + userId + '}';
    }
    /**
     * Return the userId
     *
     * @return String message id
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Set up the userId
     *
     * @param userId String userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
