/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Like properties object records a single user's "like" of an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Like extends ElementHeader
{
    /*
     * Attributes of a Like
     */
    private String user = null;


    /**
     * Default Constructor
     */
    public Like()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateLike - element to copy
     */
    public Like(Like templateLike)
    {
        super(templateLike);

        if (templateLike != null)
        {
            /*
             * Copy the user name from the supplied like.
             */
            user = templateLike.getUser();
        }
    }


    /**
     * Return the user id of the person who created the like.  Null means the user id is not known.
     *
     * @return String - liking user
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the like. Null means the user id is not known.
     *
     * @param user - String - liking user
     */
    public void setUser(String user) {
        this.user = user;
    }
}