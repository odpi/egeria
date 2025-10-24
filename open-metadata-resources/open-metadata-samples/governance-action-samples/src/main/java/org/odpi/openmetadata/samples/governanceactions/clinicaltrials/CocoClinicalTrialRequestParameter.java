/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;


import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileRequestParameter;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestParameter provides some standard definitions for request parameters used to pass properties
 * to governance actions when they run.  Using standard names for request parameters wherever necessary
 * helps to simplify the integration of governance services.
 */
public enum CocoClinicalTrialRequestParameter
{
    CLINICAL_TRIAL_ID("clinicalTrialId",
                      "Identifier code for the clinical trial.",
                      DataType.STRING.getName(),
                      "PROJ-CT-TBDF"),

    CLINICAL_TRIAL_NAME("clinicalTrialName",
                        "Display name for the clinical trial.",
                        DataType.STRING.getName(),
                        "Teddy Bear Drop Foot Clinical Trial"),

    CLINICAL_TRIAL_DESCRIPTION("clinicalTrialDescription",
                               "Description of the clinical trial.",
                               DataType.STRING.getName(),
                               "Clinical trial related to the new treatment for Teddy Bear Drop Foot."),


    DATA_LAKE_SCHEMA_TEMPLATE("dataLakeSchemaTemplateGUID",
                              "Unique identifier of the template to use when cataloguing the unity catalog schema where the clinical trial results are to be stored.  Also add the placeholders used by this template as request parameters.",
                              DataType.STRING.getName(),
                              "5bf92b0f-3970-41ea-b0a3-aacfbf6fd92e"),

    DATA_LAKE_SCHEMA_NAME("dataLakeSchemaName",
                          "Name of the schema to use in the data lake for the clinical trial.",
                          DataType.STRING.getName(),
                          "teddy_bear_drop_foot"),

    DATA_LAKE_SCHEMA_DESCRIPTION("dataLakeSchemaDescription",
                                 "Description of the schema to use in the data lake for the clinical trial.",
                                 DataType.STRING.getName(),
                                 "Example clinical trial used for education and testing of governance procedures."),

    DATA_LAKE_VOLUME_TEMPLATE("dataLakeVolumeTemplateGUID",
                              "Unique identifier of the template to use when cataloguing the directory where the weekly measurements results are to be stored.  Also add the placeholders used by this template as request parameters.",
                              DataType.STRING.getName(),
                              "92d2d2dc-0798-41f0-9512-b10548d312b7"),

    DATA_LAKE_VOLUME_PATH_NAME("dataLakeVolumeDirectoryPathName",
                              "Path name to store the files for the volume.",
                               DataType.STRING.getName(),
                               "/deployments/data/coco-data-lake/research/clinical-trials/drop-foot/weekly-measurements"),

    DATA_LAKE_VOLUME_NAME("dataLakeVolumeName",
                          "Name of the volume in Unity Catalog (UC).",
                          DataType.STRING.getName(),
                          "weekly-measurements"),

    DATA_LAKE_VOLUME_DESCRIPTION("dataLakeVolumeDescription",
                                 "Short description of the volume in Unity Catalog (UC).",
                                 DataType.STRING.getName(),
                                 "Weekly measurements for Teddy Bear Drop Foot clinical trial"),

    VALIDATED_WEEKLY_MEASUREMENT_FILES_DATA_SET_NAME("validatedWeeklyFilesDataSetName",
                                                     "Name of the data set that will list the files that have been certified.",
                                                     DataType.STRING.getName(),
                                                     "Validated Incoming Weekly Measurements For Teddy Bear Drop Foot Clinical Trial"),

    LANDING_AREA_DIRECTORY_PATH_NAME("landingAreaDirectoryPathName",
                                     "Path name of the hospital's landing area directory.",
                                     DataType.STRING.getName(),
                                     "landing-area/hospitals/oak-dene/clinical-trials/drop-foot"),

    LANDING_AREA_DIRECTORY_TEMPLATE("landingAreaDirectoryTemplateGUID",
                                    "Unique identifier of the template to use when creating the FileFolder for hospital's landing area files.",
                                    DataType.STRING.getName(),
                                    "fbdd8efd-1b69-474c-bb6d-0a304b394146"),

    LANDING_AREA_FILE_TEMPLATE("landingAreaFileTemplateGUID",
                               "Unique identifier of the template to use in the landing area when onboarding a file from a hospital.  A new, partially filled out template will be created for the hospital.  This template is of type CSVFile.",
                               DataType.STRING.getName(),
                               "5e5ffc97-237d-46c6-95c3-49405035dedc"),

    DATA_LAKE_FILE_TEMPLATE("dataLakeFileTemplateGUID",
                            "Unique identifier of the template to use in the data lake when onboarding a file from a hospital.  A new, partially filled out template will be created for the hospital.  This template is of type CSVFile.",
                            DataType.STRING.getName(),
                            "b2ec7c9d-3462-488a-897d-8e873658dded"),

    VALIDATED_DATA_FILES_COLLECTION_TEMPLATE("validatedWeeklyFilesTemplateGUID",
                                             "Unique identifier of the DataFileCollection template used to create the asset that holds the list of validated weekly measurement files.",
                                             DataType.STRING.getName(),
                                             "409f1f89-00c6-4297-8436-6a25f2b2e2f2"),

    AIRFLOW_DAG_NAME("airflowDAGName",
                     "Name of the Apache Airflow DAG that copies the data from validated data files into the treatment validation sandbox.",
                     DataType.STRING.getName(),
                     "populateSandbox"),

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
    public static List<RequestParameterType> getClinicalTrialSetUpRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        requestParameterTypes.add(DATA_LAKE_VOLUME_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_SCHEMA_TEMPLATE.getRequestParameterType());

        return requestParameterTypes;
    }


    /**
     * Retrieve all the defined request parameters
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getNominateHospitalRequestParameterTypes()
    {
        return null;
    }


    /**
     * Retrieve all the defined request parameters
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getCertifyHospitalRequestParameterTypes()
    {
        return null;
    }


    /**
     * Retrieve all the defined request parameters
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getSetUpDataLakeRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        /*
         * Supplied by Set Up Clinical Trial
         */
        requestParameterTypes.add(DATA_LAKE_SCHEMA_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_VOLUME_TEMPLATE.getRequestParameterType());

        /*
         * Mandatory supplied by caller
         */
        requestParameterTypes.add(DATA_LAKE_SCHEMA_NAME.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_VOLUME_PATH_NAME.getRequestParameterType());
        requestParameterTypes.add(LANDING_AREA_DIRECTORY_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(LANDING_AREA_FILE_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_FILE_TEMPLATE.getRequestParameterType()); // generic template

        /*
         * Option supplied by caller.
         */
        requestParameterTypes.add(DATA_LAKE_SCHEMA_DESCRIPTION.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_VOLUME_NAME.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_VOLUME_DESCRIPTION.getRequestParameterType());
        requestParameterTypes.add(AIRFLOW_DAG_NAME.getRequestParameterType());
        requestParameterTypes.add(VALIDATED_WEEKLY_MEASUREMENT_FILES_DATA_SET_NAME.getRequestParameterType());
        requestParameterTypes.add(VALIDATED_DATA_FILES_COLLECTION_TEMPLATE.getRequestParameterType());

        for (PlaceholderPropertyType postgresPlaceholderProperty : PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes())
        {
            RequestParameterType requestParameterType = new RequestParameterType();

            requestParameterType.setName(postgresPlaceholderProperty.getName());
            requestParameterType.setDescription(postgresPlaceholderProperty.getDescription());
            requestParameterType.setDataType(postgresPlaceholderProperty.getDataType());
            requestParameterType.setExample(postgresPlaceholderProperty.getExample());

            requestParameterTypes.add(requestParameterType);
        }
        return requestParameterTypes;
    }


    /**
     * Retrieve all the defined request parameters
     *
     * @return list of request parameter types
     */
    public static List<RequestParameterType> getHospitalOnboardingRequestParameterTypes()
    {
        List<RequestParameterType> requestParameterTypes = new ArrayList<>();

        /*
         * Supplied by Set Up Clinical Trial
         */
        requestParameterTypes.add(LANDING_AREA_DIRECTORY_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(LANDING_AREA_FILE_TEMPLATE.getRequestParameterType());
        requestParameterTypes.add(DATA_LAKE_FILE_TEMPLATE.getRequestParameterType()); // generic template

        /*
         * Supplied by
         */
        requestParameterTypes.add(MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getRequestParameterType());

        /*
         * Supplied by caller
         */
        requestParameterTypes.add(LANDING_AREA_DIRECTORY_PATH_NAME.getRequestParameterType());

        /*
         * Creates internally
         */
        // requestParameterTypes.add(MoveCopyFileRequestParameter.DESTINATION_TEMPLATE_NAME.getRequestParameterType());

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
