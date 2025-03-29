/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

/**
 * A description of templates for simple data sets.  These templates include an asset that may or may not have a connection.  The
 * connection is omitted if there is no connectorTypeGUID.
 */
public enum DataSetTemplateDefinition
{
    DATA_FILE_COLLECTION("26d6bcdc-ce05-4e0b-8685-cd40777dc5f9",
                         DeployedImplementationType.DATA_FILE_COLLECTION,
                         DeployedImplementationType.DATA_FILE_COLLECTION.getDeployedImplementationType() + ":" + PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                         new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                         ContentPackDefinition.CORE_CONTENT_PACK),


    ;


    private final String                               guid;
    private final DeployedImplementationTypeDefinition deployedImplementationType;
    private final String                               qualifiedName;
    private final String                               connectorTypeGUID;
    private final ContentPackDefinition                contentPackDefinition;


    /**
     * Construct the description of the template.
     *
     * @param guid fixed unique identifier
     * @param deployedImplementationType deployed implementation type for the technology
     * @param qualifiedName optional server name
     * @param connectorTypeGUID connector type to link to the connection
     * @param contentPackDefinition            which content pack does this server belong?
     */
    DataSetTemplateDefinition(String                               guid,
                              DeployedImplementationTypeDefinition deployedImplementationType,
                              String                               qualifiedName,
                              String                               connectorTypeGUID,
                              ContentPackDefinition                contentPackDefinition)
    {
        this.guid                          = guid;
        this.deployedImplementationType    = deployedImplementationType;
        this.qualifiedName                 = qualifiedName;
        this.connectorTypeGUID             = connectorTypeGUID;
        this.contentPackDefinition         = contentPackDefinition;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    public String getTemplateGUID()
    {
        return guid;
    }


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    public DeployedImplementationTypeDefinition getDeployedImplementationType()
    {
        return deployedImplementationType;
    }



    /**
     * Return the name of the server where this asset resides.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the connector type GUID for the connection.
     *
     * @return string
     */
    public String getConnectorTypeGUID()
    {
        return connectorTypeGUID;
    }


    /**
     * Get identifier of content pack where this template should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateDefinition{templateName='" + getDeployedImplementationType() + "'}";
    }
}
