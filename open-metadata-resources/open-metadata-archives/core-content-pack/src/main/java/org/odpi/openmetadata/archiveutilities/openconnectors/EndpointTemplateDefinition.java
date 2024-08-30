/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;


import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.List;

/**
 * A description of the default endpoint templates.
 */
public enum EndpointTemplateDefinition implements TemplateDefinition
{
    JDBC_ENDPOINT_TEMPLATE("3d79ce50-1887-4627-ad71-ba4649aba2bc",
                           DeployedImplementationType.JDBC_ENDPOINT,
                           "REST",
                             "jdbc:postgresql://" + PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" + PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/postgres",
                             PostgresPlaceholderProperty.getJDBCEndpointPlaceholderPropertyTypes()),

    REST_ENDPOINT_TEMPLATE("9ea4bff4-d193-492f-bcad-6e68c07c6f9e",
                          DeployedImplementationType.REST_API_ENDPOINT,
                           "JDBC",
                          PlaceholderProperty.HOST_URL.getPlaceholder() + ":" + PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                          PlaceholderProperty.getHTTPEndpointPlaceholderPropertyTypes()),


    ;


    private final String                     guid;
    private final DeployedImplementationType deployedImplementationType;
    private final String                     protocol;
    private final String                        networkAddress;
    private final List<PlaceholderPropertyType> placeholderPropertyTypes;


    /**
     * Construct the description of the template.
     *
     * @param guid                             fixed unique identifier
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param protocol                         type of the protocol
     * @param networkAddress                   network address for the endpoint
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     */
    EndpointTemplateDefinition(String                        guid,
                               DeployedImplementationType    deployedImplementationType,
                               String                        protocol,
                               String                        networkAddress,
                               List<PlaceholderPropertyType> placeholderPropertyTypes)
    {
        this.guid                       = guid;
        this.deployedImplementationType = deployedImplementationType;
        this.protocol                   = protocol;
        this.networkAddress             = networkAddress;
        this.placeholderPropertyTypes   = placeholderPropertyTypes;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return guid;
    }


    /**
     * Return the name to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateName()
    {
        return deployedImplementationType.getDeployedImplementationType() + " template";
    }


    /**
     * Return the description to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateDescription()
    {
        return "Create a " + deployedImplementationType.getDeployedImplementationType()+ ".";
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "V1.0";
    }

    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    @Override
    public DeployedImplementationType getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the name of the server.
     *
     * @return string
     */
    public String getServerName()
    {
        return PlaceholderProperty.SERVER_NAME.getPlaceholder();
    }


    /**
     * Return the server description.
     *
     * @return string
     */
    public String getEndpointDescription()
    {
        return PlaceholderProperty.DESCRIPTION.getPlaceholder();
    }

    /**
     * Return the endpoint address.
     *
     * @return string
     */
    public String getNetworkAddress()
    {
        return networkAddress;
    }


    /**
     * Return the endpoint protocol.
     *
     * @return string
     */
    public String getProtocol()
    {
        return protocol;
    }


    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    @Override
    public List<PlaceholderPropertyType> getPlaceholders()
    {
        return placeholderPropertyTypes;
    }

    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    @Override
    public List<ReplacementAttributeType> getReplacementAttributes()
    {
        return null;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateDefinition{templateName='" + getTemplateName() + "'}";
    }
}
