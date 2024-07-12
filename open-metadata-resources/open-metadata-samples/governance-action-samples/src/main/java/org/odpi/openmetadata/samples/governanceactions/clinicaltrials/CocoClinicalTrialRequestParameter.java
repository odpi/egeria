/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;


import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderProperty;
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

    DATA_LAKE_FOLDER_TEMPLATE("dataLakeFolderTemplate",
                              "Qualified name of the template to use when cataloguing the directory where the weekly measurements results are to be stored.  Also add the placeholders used by this template as request parameters.",
                              "string",
                              "OSS Unity Catalog (UC) Volume:{{serverNetworkAddress}}:{{ucCatalogName}}.{{ucSchemaName}}.{{ucVolumeName}}"),
    DATA_LAKE_FOLDER_PARENT("dataLakeFolderParent",
                            "Optional qualified name of the parent data set that the folder supplies data to.  If supplied, the data lake folder is linked to this data set using the DataContentForDataSet relationship.",
                            "string",
                            "OSS Unity Catalog (UC) Schema:http://localhost:8080:unity.default"),

    CLINICAL_TRIAL_ID(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName(),
                      CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getDescription(),
                      CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getDataType(),
                      CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getExample()),
    CLINICAL_TRIAL_NAME(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName(),
                        CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getDescription(),
                        CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getDataType(),
                        CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getExample()),
    HOSPITAL_NAME(CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getName(),
                  CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getDescription(),
                  CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getDataType(),
                  CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getExample()),
    CONTACT_NAME(CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getName(),
                 CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getDescription(),
                 CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getDataType(),
                 CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getExample()),
    CONTACT_DEPT(CocoClinicalTrialPlaceholderProperty.CONTACT_DEPT.getName(),
                 CocoClinicalTrialPlaceholderProperty.CONTACT_DEPT.getDescription(),
                 CocoClinicalTrialPlaceholderProperty.CONTACT_DEPT.getDataType(),
                 CocoClinicalTrialPlaceholderProperty.CONTACT_DEPT.getExample()),

    DIRECTORY_NAME(PlaceholderProperty.DIRECTORY_NAME.getName(),
                   PlaceholderProperty.DIRECTORY_NAME.getDescription(),
                   PlaceholderProperty.DIRECTORY_NAME.getDataType(),
                   PlaceholderProperty.DIRECTORY_NAME.getExample()),
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
        return null;
    }


    public static List<RequestParameterType> getOnboardHospitalRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(CLINICAL_TRIAL_ID.getRequestParameterType());
        requestParameterTypes.add(CLINICAL_TRIAL_NAME.getRequestParameterType());
        requestParameterTypes.add(HOSPITAL_NAME.getRequestParameterType());
        requestParameterTypes.add(CONTACT_DEPT.getRequestParameterType());
        requestParameterTypes.add(CONTACT_NAME.getRequestParameterType());
        requestParameterTypes.add(DIRECTORY_NAME.getRequestParameterType());

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
