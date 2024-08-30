/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.controls;


import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum PlaceholderProperty
{
    /**
     * The host IP address or domain name.
     */
    HOST_IDENTIFIER ("hostIdentifier", "The host IP address or domain name.", "string", "coconet.com"),

    /**
     * The host IP address or domain name.
     */
    HOST_URL ("hostURL", "The host IP address or domain name of the server with the HTTP protocol prefix.", "string", "https://coconet.com"),

    /**
     * The number of the port to use to connect to a service.
     */
    PORT_NUMBER ("portNumber", "The number of the port to use to connect to a service.", "string", "1234"),

    /**
     * The network address of the server
     */
    SERVER_NETWORK_ADDRESS("serverNetworkAddress", "The network address of the server.", "string", "http://localhost:8080"),

    /**
     * The userId to store in the userId attribute of the connection.
     */
    CONNECTION_USER_ID ("connectionUserId",
                        "The userId to store in the userId attribute of the connection.",
                        "string",
                        "user1"),

    /**
     * The password to store in the clearPassword attribute of the connection.
     */
    CONNECTION_PASSWORD ("connectionPassword",
                         "The password to store in the clearPassword attribute of the connection.",
                         "string",
                         "secret"),

    /**
     * The name of the server being catalogued.
     */
    SERVER_NAME ("serverName", "The name of the server being catalogued.", "string", "myServer"),

    /**
     * The name of the schema being catalogued.
     */
    SCHEMA_NAME ("schemaName", "The name of the schema being catalogued.", "string", "MyServer.schema"),

    /**
     * The display name is used to identify the element.
     */
    DISPLAY_NAME("displayName",
                 "The display name is used to identify the element.  It does not need to be unique, but it should help someone know what the element is about.",
                 "string",
                 "myDataSet"),

    /**
     * The description of the element to help a consumer understand its content and purpose.
     */
    DESCRIPTION ("description",
                 "The description of the element to help a consumer understand its content and purpose.",
                 "string",
                 "This file contains a week's worth of patient data for the Teddy Bear Drop Foot clinical trial."),

    /**
     * The description of the element to help a consumer understand its content and purpose.
     */
    VERSION_IDENTIFIER ("versionIdentifier",
                 "The string identifier for the element.  This is typically of the form Vx.y.z where x is the major version number, y is the minor version number, and z is an option patch identifier.",
                 "string",
                 "V1.0"),

    /**
     * The formula used to populate the data set.
     */
    FORMULA("formula", "The formula used to populate the data set.", "string", null),

    /**
     * The language/format used in the data set's formula.
     */
    FORMULA_TYPE("formulaType", "The language/format used in the data set's formula.", "string", null),


    /**
     * The full pathname of the file including the directory names, file name and file extension.
     */
    FILE_SYSTEM_NAME ("fileSystemName", "The unique name for the file system that this file/directory belongs.  It may be a machine name or a URL to a remote file store.", "string", "/a/b/c/myFile.txt"),


    /**
     * The format standard used in the file system.
     */
    FORMAT ("format", "The format standard used in the file system.", "string", "APFS"),


    /**
     * Is encryption enabled on this file system? If known, what type of encryption?
     */
    ENCRYPTION ("encryption", "Is encryption enabled on this file system? If known, what type of encryption?", "string", "Not Enabled"),

    /**
     * The full pathname of the file including the directory names, file name and file extension.
     */
    DIRECTORY_PATH_NAME ("directoryPathName", "The full path name of the directory including the parent directory names and optional file system name, if applicable", "string", "/a/b/myFiles"),

    /**
     * The name of the leaf directory, without its enclosing directories.
     */
    DIRECTORY_NAME("directoryName", "The name of the leaf directory, without its enclosing directories.", "string", "myFiles"),

    /**
     * The full pathname of the file including the directory names, file name and file extension.
     */
    FILE_PATH_NAME ("filePathName", "The full path name of the file including the directory names, file name and optional file extension, if applicable.", "string", "/a/b/myFiles/myFile.txt"),

    /**
     * The short name of the file with its extension but without the directory names.
     */
    FILE_NAME("fileName", "The short name of the file with its extension but without the directory names.", "string", "myFile.txt"),

    /**
     * The logical file type of the file.
     */
    FILE_TYPE("fileType",
              "The logical file type of the file.",
              "string",
              "Text File"),

    /**
     * The postfix identifier in the file name that indicates the format of the file.
     */
    FILE_EXTENSION ("fileExtension",
                    "The postfix identifier in the file name that indicates the format of the file.",
                    "string",
                    "txt"),

    /**
     * The encoding scheme used on the file.
     */
    FILE_ENCODING ("fileEncoding",
                   "The encoding scheme used on the file.",
                   "string",
                   "JSON"),

    /**
     * The programming language used to encode the file.
     */
    PROGRAMMING_LANGUAGE ("programmingLanguage",
                          "The programming language used to encode the file.",
                          "string",
                          "Java"),

    /**
     * Descriptive metadata values embedded within the file.
     */
    EMBEDDED_METADATA ("embeddedMetadata",
                       "Descriptive metadata values embedded within the file.",
                       "map<string, string>",
                       null),

    /**
     * The date that the file was created.
     */
    CREATION_DATE ("dateCreated",
                  "The date that the file was created.",
                  "string",
                  null),


    /**
     * The date that the file was created.
     */
    LAST_UPDATE_DATE ("dateLastModified",
                   "The date that the file was last changed.",
                   "string",
                   null),

    LAST_ACCESSED_DATE ("dateLastAccessed",
                        "The date that the file was last read.",
                        "string",
                        null),

    /**
     * The date/time that the data/resource was received.
     */
    RECEIVED_DATE("dateReceived", "The date/time that the data/resource was received.", "string", "2024-07-10T16:11:09"),


    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    PlaceholderProperty(String name,
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
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Return the placeholder to use when building templates.
     *
     * @return placeholder property
     */
    public String getPlaceholder()
    {
        return "{{" + name + "}}";
    }

    /**
     * Return the description of the placeholder property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the placeholder property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the placeholder property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getHostPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DISPLAY_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }




    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getHTTPEndpointPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getServerWithUserIdAndPasswordPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_PASSWORD.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getServerWithUserIdOnlyPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }




    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getUnsecuredServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }






    /**
     * Retrieve the Kafka Topic defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getFileSystemPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(HOST_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_SYSTEM_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(FORMAT.getPlaceholderType());
        placeholderPropertyTypes.add(ENCRYPTION.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getDataStorePlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_PASSWORD.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DISPLAY_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for directories (file folder)
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getDataSetPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(DISPLAY_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(FORMULA.getPlaceholderType());
        placeholderPropertyTypes.add(FORMULA_TYPE.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Retrieve all the defined placeholder properties for data files
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getDataFilesPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(FILE_SYSTEM_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_EXTENSION.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_ENCODING.getPlaceholderType());
        placeholderPropertyTypes.add(VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(DESCRIPTION.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for data files
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getMediaFilesPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(FILE_SYSTEM_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_EXTENSION.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_ENCODING.getPlaceholderType());
        placeholderPropertyTypes.add(EMBEDDED_METADATA.getPlaceholderType());
        placeholderPropertyTypes.add(VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(DESCRIPTION.getPlaceholderType());
        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for directories (file folder)
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getFolderPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(FILE_SYSTEM_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(DIRECTORY_PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(DIRECTORY_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(DESCRIPTION.getPlaceholderType());
        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for files associated with software
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getSoftwareFilesPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(FILE_SYSTEM_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_PATH_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_EXTENSION.getPlaceholderType());
        placeholderPropertyTypes.add(FILE_ENCODING.getPlaceholderType());
        placeholderPropertyTypes.add(PROGRAMMING_LANGUAGE.getPlaceholderType());
        placeholderPropertyTypes.add(VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(DESCRIPTION.getPlaceholderType());

        return placeholderPropertyTypes;
    }

    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return placeholder property type
     */
    public PlaceholderPropertyType getPlaceholderType()
    {
        PlaceholderPropertyType placeholderPropertyType = new PlaceholderPropertyType();

        placeholderPropertyType.setName(name);
        placeholderPropertyType.setDescription(description);
        placeholderPropertyType.setDataType(dataType);
        placeholderPropertyType.setExample(example);
        placeholderPropertyType.setRequired(true);

        return placeholderPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PlaceholderProperty{ name=" + name + "}";
    }
}
