/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;




import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;

import java.util.Arrays;
import java.util.List;

public class DinoCohortListResponse extends DinoViewOMVSAPIResponse {


    // A DinoCohortListResponse contains a list of CohortDescription objects.

    private List<CohortDescription> cohortList = null;

    /**
     * Default constructor
     */
    public DinoCohortListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoCohortListResponse(DinoCohortListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.cohortList = template.getCohortList();
        }
    }


    /**
     * Return the serverList.
     *
     * @return bean
     */
    public List<CohortDescription> getCohortList()
    {
        return cohortList;
    }


    /**
     * Set the serverList.
     *
     * @param cohortList - bean
     */
    public void setCohortList(List<CohortDescription> cohortList)
    {
        this.cohortList = cohortList;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoCohortListResponse{" +
                "cohortList=" + cohortList +
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
