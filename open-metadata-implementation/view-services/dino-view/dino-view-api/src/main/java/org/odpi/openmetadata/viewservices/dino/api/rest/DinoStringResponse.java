/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;


import java.util.Arrays;
import java.util.List;

public class DinoStringResponse extends DinoViewOMVSAPIResponse {


    private String string = null;

    /**
     * Default constructor
     */
    public DinoStringResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoStringResponse(DinoStringResponse template)
    {
        super(template);

        if (template != null)
        {
            this.string = template.getString();
        }
    }


    /**
     * Return the string.
     *
     * @return bean
     */
    public String getString()
    {
        return string;
    }


    /**
     * Set the string.
     *
     * @param string - bean
     */
    public void setString(String string)
    {
        this.string = string;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoStringResponse{" +
                "string=" + string +
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
