/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.util.List;

/**
 * DataEngineClient provides the client-side interface for a data engine tool to create processes with ports,
 * schemas and relationships.
 */
public interface DataEngineClient {

    /**
     * Creates the process, with all the ports, schema types and corresponding relationships
     *
     * @param userId              the name of the calling user
     * @param qualifiedName       the qualifiedName name of the process
     * @param processName         the name of the process
     * @param description         the description of the process
     * @param latestChange        the description for the latest change done for the process
     * @param zoneMembership      the list of zones of the process
     * @param displayName         the display name of the process
     * @param formula             the formula for the process
     * @param owner               the name of the owner for this process
     * @param ownerType           the type of the owner for this process
     * @param portImplementations the list of port implementations
     * @param portAliases         the list of port aliases
     * @param lineageMappings     the list of lineage mappings
     *
     * @return unique identifier of the process in the repository
     *
     * @throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException the bean properties are
     * invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String createProcess(String userId, String qualifiedName, String processName, String description,
                         String latestChange, List<String> zoneMembership, String displayName, String formula,
                         String owner, OwnerType ownerType, List<PortImplementation> portImplementations,
                         List<PortAlias> portAliases, List<LineageMapping> lineageMappings)
            throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException;

    /**
     * Create the software server capability entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the server
     * @param name          the name of the server
     * @param description   the description of the server
     * @param type          the type of the server
     * @param version       the version of the server
     * @param patchLevel    the patch level of the server
     * @param source        the source of the server
     *
     * @return unique identifier of the server in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String createSoftwareServerCapability(String userId, String qualifiedName, String name, String description,
                                          String type, String version, String patchLevel, String source)
            throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException;

    /**
     * Create the schema type entity, with the corresponding schema attributes and relationships
     *
     * @param userId           the name of the calling user
     * @param qualifiedName    the qualifiedName name of the schema type
     * @param displayName      the display name of the schema type
     * @param author           the author of the schema type
     * @param encodingStandard the encoding for the schema type
     * @param usage            the usage for the schema type
     * @param versionNumber    the version number for the schema type
     * @param attributeList    the list of attributes for the schema type.
     *
     * @return unique identifier of the schema type in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String createSchemaType(String userId, String qualifiedName, String displayName, String author,
                            String encodingStandard, String usage, String versionNumber, List<Attribute> attributeList)
            throws InvalidParameterException,
                   PropertyServerException,
                   UserNotAuthorizedException;


    /**
     * Create the port implementation entity, with the corresponding schema type and port schema relationship
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     * @param schemaType    the schema type attached to the port implementation
     *
     * @return unique identifier of the port implementation in the repository
     *
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException the bean properties are
     * invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String createPortImplementation(String userId, String qualifiedName, String displayName, PortType portType,
                                    SchemaType schemaType)
            throws
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException;

    /**
     * Create the Port Alias entity with a PortDelegation relationship
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     * @param delegatesTo   the qualified name of the delegated port
     *
     * @return unique identifier of the port alias in the repository
     *
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException the bean properties are
     * invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String createPortAlias(String userId, String qualifiedName, String displayName, PortType portType,
                           String delegatesTo)
            throws
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException;


    /**
     * Add lineage mapping relationships between schema types
     *
     * @param userId          the name of the calling user
     * @param lineageMappings list of lineage mappings
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    void addLineageMappings(String userId, List<LineageMapping> lineageMappings) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;

    /**
     * Add ports and process ports relationship to an existing port
     * @param userId          the name of the calling user
     * @param portGUIDs the list of port GUIDs
     * @param processGUID the process GUYID
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    void addPortsToProcess(String userId, List<String> portGUIDs, String processGUID)
            throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException;
}
