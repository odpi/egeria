/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This property header implements any common mechanisms that all property objects need.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Annotation.class, name = "Annotation"),
                @JsonSubTypes.Type(value = DataField.class, name = "DataField"),
                @JsonSubTypes.Type(value = DataFieldLink.class, name = "DataFieldLink"),
                @JsonSubTypes.Type(value = DiscoveryAnalysisReport.class, name = "DiscoveryAnalysisReport"),
                @JsonSubTypes.Type(value = DiscoveryEngineProperties.class, name = "DiscoveryEngineProperties"),
                @JsonSubTypes.Type(value = DiscoveryServiceProperties.class, name = "DiscoveryServiceProperties")
        })
public abstract class PropertyBase implements Serializable
{
    public static final long CURRENT_AUDIT_HEADER_VERSION = 1;

    private static final long serialVersionUID = 1L;

    /*
     * The Element header is only set when items are retrieved from the metadata repositories.
     */
    private ElementHeader elementHeader = null;


    /*
     * The typeName and extendedProperties are used to identify subtypes and any properties their type definition supports.
     */
    private String              typeName                     = null;
    private Map<String, Object> extendedProperties           = null;


    /*
     * Version number for this header.  This is used to ensure that all the critical header information
     * in read in a back-level version of the ODF.  The default is 0 to indicate that the instance came from
     * a version of the OCF that does not have a version number encoded.
     */
    private long headerVersion = 0;

    /**
     * Typical Constructor
     */
    public PropertyBase()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public PropertyBase(PropertyBase template)
    {
        if (template != null)
        {
            elementHeader        = template.getElementHeader();
            typeName             = template.getTypeName();
            extendedProperties   = template.getExtendedProperties();
            headerVersion        = template.getHeaderVersion();
        }
    }


    /**
     * Return the version of this header.  This is used by the OMAS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all the header properties.
     *
     * @return long version number - the value is incremented each time a new non-informational field is added
     * to the audit header.
     */
    public long getHeaderVersion()
    {
        return headerVersion;
    }


    /**
     * Return the version of this header.  This is used by the OMAS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all the header properties.
     *
     * @param headerVersion long version number - the value is incremented each time a new non-informational field is added
     * to the audit header.
     */
    public void setHeaderVersion(long headerVersion)
    {
        this.headerVersion = headerVersion;
    }


    /**
     * The element header is setup when an element is retrieved from the repository.  It contains the control information and
     * classifications for the object.  It does not need to be filled out for create and update requests.
     *
     * @return element header
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * The element header is setup when an element is retrieved from the repository.  It contains the control information and
     * classifications for the object.  It does not need to be filled out for create and update requests.
     *
     * @param elementHeader control header
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the name of the type of annotation - default is "Annotation".
     *
     * @return unique open metadata type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name of the type of annotation - default is "Annotation".
     *
     * @param typeName unique open metadata type name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }
}