/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;




import java.util.Arrays;

public class DinoResponse extends DinoViewOMVSAPIResponse {


    //private TypeExplorer typeExplorer = null;

    /**
     * Default constructor
     */
    public DinoResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoResponse(DinoResponse template)
    {
        super(template);

        if (template != null)
        {
            //this.typeExplorer = template.getTypeExplorer();
        }
    }


    /**
     * Return the typeExplorer result.
     *
     * @return bean
     */
    //public TypeExplorer getTypeExplorer()
    //{
    //    return typeExplorer;
    //}


    /**
     * Set the typeExplorer result.
     *
     * @param typeExplorer - bean
     */
    //public void setTypeExplorer(TypeExplorer typeExplorer)
    //{
    //    this.typeExplorer = typeExplorer;
    //}



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoResponse{" +
                //"typeExplorer=" + typeExplorer +
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
