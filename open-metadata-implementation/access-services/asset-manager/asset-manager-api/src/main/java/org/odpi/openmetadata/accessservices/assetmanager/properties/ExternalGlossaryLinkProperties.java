/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalGlossaryLinkProperties describes the properties of URL link to a remote glossary.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalGlossaryLinkProperties extends ExternalReferenceProperties
{
    private static final long     serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public ExternalGlossaryLinkProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template object to copy.
     */
    public ExternalGlossaryLinkProperties(ExternalGlossaryLinkProperties template)
    {
        super (template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalGlossaryLinkProperties{" +
                       "displayName='" + getDisplayName() + '\'' +
                       ", url='" + getUrl() + '\'' +
                       ", version='" + getVersion() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", organization='" + getOrganization() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}
