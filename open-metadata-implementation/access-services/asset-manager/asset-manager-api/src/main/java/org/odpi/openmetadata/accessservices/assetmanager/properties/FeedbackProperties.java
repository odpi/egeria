/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FeedbackProperties provides a base class for passing feedback objects as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FeedbackProperties extends RelationshipProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean    isPublic  = false;


    /**
     * Default constructor
     */
    public FeedbackProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FeedbackProperties(FeedbackProperties template)
    {
        super(template);

        if (template != null)
        {
            this.isPublic = template.getIsPublic();
        }
    }




    /**
     * Return whether the feedback is private or not
     *
     * @return boolean
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up the privacy flag.
     *
     * @param aPrivate boolean
     */
    public void setIsPublic(boolean aPrivate)
    {
        isPublic = aPrivate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "FeedbackProperties{" + '}';
    }
}
