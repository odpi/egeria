/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdentifierProperties describes the properties of the identifier for a metadata element in
 * an external asset manager.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ExternalIdentifierProperties extends MetadataCorrelationProperties
{
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public ExternalIdentifierProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template object to copy.
     */
    public ExternalIdentifierProperties(ExternalIdentifierProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdentifierProperties{" +
                       "assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
                       ", assetManagerDescription='" + getSynchronizationDescription() + '\'' +
                       ", externalIdentifier='" + getExternalIdentifier() + '\'' +
                       ", externalIdentifierName='" + getExternalIdentifierName() + '\'' +
                       ", externalIdentifierUsage='" + getExternalIdentifierUsage() + '\'' +
                       ", keyPattern=" + getKeyPattern() +
                       ", mappingProperties=" + getMappingProperties() +
                       '}';
    }
}
