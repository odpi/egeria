/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;


/**
 * NoteBuilder is able to build the properties for a Note entity.
 */
public class NoteBuilder extends ReferenceableBuilder
{
    private final String  title;
    private final String  text;

    /**
     * Constructor.
     *
     * @param qualifiedName unique name (qualifiedName) for the note
     * @param title   title of the note.
     * @param text   String - the text of the note.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public NoteBuilder(String               qualifiedName,
                       String               title,
                       String               text,
                       OMRSRepositoryHelper repositoryHelper,
                       String               serviceName,
                       String               serverName)
    {
        super(qualifiedName,
              OpenMetadataAPIMapper.NOTE_TYPE_GUID,
              OpenMetadataAPIMapper.NOTE_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.text = text;
        this.title = title;
    }


    /**
     * Return the entity properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.TITLE_PROPERTY_NAME,
                                                                  title,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.TEXT_PROPERTY_NAME,
                                                                  text,
                                                                  methodName);
        return properties;
    }
}
