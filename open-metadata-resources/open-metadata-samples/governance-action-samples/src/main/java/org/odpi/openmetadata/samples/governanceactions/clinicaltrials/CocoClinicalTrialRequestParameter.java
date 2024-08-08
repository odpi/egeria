/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;


import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileRequestParameter;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum CocoClinicalTrialRequestParameter
{
    DATA_LAKE_CATALOG("dataLakeCatalog",
                      "Qualified name of the catalog to use for clinical trials.   This acts as the metadata collection name for the data lake resources used in the clinical trial.",
                      "string",
                      "OSS Unity Catalog (UC) Catalog:http://localhost:8080:unity"),

    DATA_LAKE_VOLUME_TEMPLATE("dataLakeVolumeTemplateGUID",
                              "Unique identifier of the template to use when cataloguing the directory where the weekly measurements results are to be stored.  Also add the placeholders used by this template as request parameters.",
                              "string",
                              null),

    DATA_LAKE_FOLDER_PARENT("dataLakeFolderParent",
                            "Optional qualified name of the parent data set that the folder supplies data to.  If supplied, the data lake folder is linked to this data set using the DataContentForDataSet relationship.",
                            "string",
                            "OSS Unity Catalog (UC) Schema:http://localhost:8080:unity.default"),

    LANDING_AREA_DIRECTORY_NAME("landingAreaDirectoryName",
                                "Name of the hospital's landing area directory.",
                                "string",
                                null),

    LANDING_AREA_DIRECTORY_TEMPLATE("landingAreaDirectoryTemplateGUID",
                               "Unique identifier of the template to use when creating the FileFolder for hospital's landing area files.",
                               "string",
                               null),
    LANDING_AREA_FILE_TEMPLATE("landingAreaFileTemplateGUID",
                               "Unique identifier of the template to use in the landing area when onboarding a file from a hospital.  A new, partially filled out template will be created for the hospital.  This template is of type CSVFile.",
                               "string",
                               null),

    DATA_LAKE_FILE_TEMPLATE("dataLakeFileTemplateGUID",
                               "Unique identifier of the template to use in the data lake when onboarding a file from a hospital.  A new, partially filled out template will be created for the hospital.  This template is of type CSVFile.",
                               "string",
                               null),
    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     */
    CocoClinicalTrialRequestParameter(String name,
                                      String description,
                                      String dataType,
                                      String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType    = dataType;
        this.example     = example;
    }


    /**
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the request parameter.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the request parameter.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the request parameter to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined request parameters
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getSetUpDataLakeRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(DATA_LAKE_VOLUME_TEMPLATE.getRequestParameterType());

        return requestParameterTypes;
    }


    public static List<RequestParameterType> getOnboardHospitalRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(LANDING_AREA_FILE_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_FILE_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(MoveCopyFileRequestParameter.DESTINATION_TEMPLATE_NAME.getRequestParameterType());
        requestParameterTypes.add(MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getRequestParameterType());


        return requestParameterTypes;
    }



    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return request parameter type
     */
    public RequestParameterType getRequestParameterType()
    {
        RequestParameterType requestParameterType = new RequestParameterType();

        requestParameterType.setName(name);
        requestParameterType.setDescription(description);
        requestParameterType.setDataType(dataType);
        requestParameterType.setExample(example);

        return requestParameterType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "RequestParameter{ name=" + name + "}";
    }
}
