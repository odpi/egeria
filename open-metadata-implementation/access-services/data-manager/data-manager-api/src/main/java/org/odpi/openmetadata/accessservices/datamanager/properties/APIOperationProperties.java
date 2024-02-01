/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIOperationProperties is a class for an operation within an API specification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIOperationProperties extends ComplexSchemaTypeProperties
{
    private String path     = null;
    private String command     = null;


    /**
     * Default constructor
     */
    public APIOperationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public APIOperationProperties(APIOperationProperties template)
    {
        super(template);

        if (template != null)
        {
            path = template.getPath();
            command = template.getCommand();
        }
    }


    /**
     * Return the path name to add to the network address in the API's endpoint.
     *
     * @return string path name
     */
    public String getPath()
    {
        return path;
    }


    /**
     * Set up the path name to add to the network address in the API's endpoint.
     *
     * @param path string path name
     */
    public void setPath(String path)
    {
        this.path = path;
    }


    /**
     * Return the command (eg GET, POST or method name) that is called.
     *
     * @return string command name
     */
    public String getCommand()
    {
        return command;
    }


    /**
     * Set up the command (eg GET, POST or method name) that is called.
     *
     * @param command command string
     */
    public void setCommand(String command)
    {
        this.command = command;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "APIOperationProperties{" +
                       "path='" + path + '\'' +
                       ", command='" + command + '\'' +
                       ", versionNumber='" + getVersionNumber() + '\'' +
                       ", author='" + getAuthor() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", encodingStandard='" + getEncodingStandard() + '\'' +
                       ", namespace='" + getNamespace() + '\'' +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        APIOperationProperties that = (APIOperationProperties) objectToCompare;
        return Objects.equals(path, that.path) &&
                       Objects.equals(command, that.command);
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), path, command);
    }
}
