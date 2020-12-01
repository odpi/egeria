/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.rest;

import org.odpi.openmetadata.viewservices.serverauthor.api.properties.Platform;

import java.util.Arrays;
import java.util.Set;

/**
 * A Server Author response containing the known platforms
 */
public class ServerAuthorPlatformsResponse extends ServerAuthorViewOMVSAPIResponse {
    /**
     * Associated platform
     */
    Set<Platform> platforms = null;

    /**
     * Default constructor
     */
    public ServerAuthorPlatformsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerAuthorPlatformsResponse(ServerAuthorPlatformsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.platforms = template.getPlatforms();
        }
    }

    /**
     * Get the platforms
     * @return the platforms
     */
    public Set<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * set the platforms
     * @param platforms the platforms
     */
    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerAuthorResponse{" +
                "platform=" + platforms +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }

}
