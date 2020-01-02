/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EnterpriseAccessConfig describes the properties that control the enterprise access services that the
 * OMRS provides to the Open Metadata Access Services (OMASs).
 * <ul>
 *     <li>
 *         enterpriseMetadataCollectionName - name of the combined metadata collection covered by the connected open
 *                                        metadata repositories.  Used for messages.
 *     </li>
 *     <li>
 *         enterpriseMetadataCollectionId - unique identifier for the combined metadata collection covered by the
 *                                      connected open metadata repositories.
 *     </li>
 *     <li>
 *         enterpriseOMRSTopicConnection - connection for the enterprise OMRS Topic connector.
 *     </li>
 *     <li>
 *         enterpriseOMRSTopicProtocolVersion - the protocol version for the events passed on the
 *                                            enterprise OMRS topic.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EnterpriseAccessConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private String                           enterpriseMetadataCollectionName   = null;
    private String                           enterpriseMetadataCollectionId     = null;
    private Connection                       enterpriseOMRSTopicConnection      = null;
    private OpenMetadataEventProtocolVersion enterpriseOMRSTopicProtocolVersion = null;


    /**
     * Default Constructor does nothing.
     */
    public EnterpriseAccessConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EnterpriseAccessConfig(EnterpriseAccessConfig  template)
    {
        super(template);
    }


    /**
     * Constructor to set up all configuration values.
     *
     * @param enterpriseMetadataCollectionName name of the combined metadata collection covered by the connected open
     *                                        metadata repositories.  Used for messages.
     * @param enterpriseMetadataCollectionId unique identifier for the combined metadata collection covered by the
     *                                      connected open metadata repositories.
     * @param enterpriseOMRSTopicConnection connection for the OMRS Topic connector.
     * @param enterpriseOMRSTopicProtocolVersion protocol version enum
     */
    public EnterpriseAccessConfig(String                           enterpriseMetadataCollectionName,
                                  String                           enterpriseMetadataCollectionId,
                                  Connection                       enterpriseOMRSTopicConnection,
                                  OpenMetadataEventProtocolVersion enterpriseOMRSTopicProtocolVersion)
    {
        this.enterpriseMetadataCollectionName = enterpriseMetadataCollectionName;
        this.enterpriseMetadataCollectionId = enterpriseMetadataCollectionId;
        this.enterpriseOMRSTopicConnection = enterpriseOMRSTopicConnection;
        this.enterpriseOMRSTopicProtocolVersion = enterpriseOMRSTopicProtocolVersion;
    }


    /**
     * Return the name of the combined metadata collection covered by the connected open
     * metadata repositories.  Used for messages.
     *
     * @return String name
     */
    public String getEnterpriseMetadataCollectionName()
    {
        return enterpriseMetadataCollectionName;
    }


    /**
     * Set up the name of the combined metadata collection covered by the connected open
     * metadata repositories.  Used for messages.
     *
     * @param enterpriseMetadataCollectionName String name
     */
    public void setEnterpriseMetadataCollectionName(String enterpriseMetadataCollectionName)
    {
        this.enterpriseMetadataCollectionName = enterpriseMetadataCollectionName;
    }


    /**
     * Return the unique identifier for the combined metadata collection covered by the
     * connected open metadata repositories.
     *
     * @return Unique identifier (guid)
     */
    public String getEnterpriseMetadataCollectionId()
    {
        return enterpriseMetadataCollectionId;
    }


    /**
     * Set up the unique identifier for the combined metadata collection covered by the
     * connected open metadata repositories.
     *
     * @param enterpriseMetadataCollectionId Unique identifier (guid)
     */
    public void setEnterpriseMetadataCollectionId(String enterpriseMetadataCollectionId)
    {
        this.enterpriseMetadataCollectionId = enterpriseMetadataCollectionId;
    }


    /**
     * Return the connection for the Enterprise OMRS Topic connector.
     *
     * @return Connection object
     */
    public Connection getEnterpriseOMRSTopicConnection()
    {
        return enterpriseOMRSTopicConnection;
    }


    /**
     * Set up the connection for the Enterprise OMRS Topic connector.
     *
     * @param enterpriseOMRSTopicConnection Connection object
     */
    public void setEnterpriseOMRSTopicConnection(Connection enterpriseOMRSTopicConnection)
    {
        this.enterpriseOMRSTopicConnection = enterpriseOMRSTopicConnection;
    }


    /**
     * Return the protocol version to use on the EnterpriseOMRSTopicConnector.
     *
     * @return protocol version enum
     */
    public OpenMetadataEventProtocolVersion getEnterpriseOMRSTopicProtocolVersion()
    {
        return enterpriseOMRSTopicProtocolVersion;
    }


    /**
     * Set up the protocol version to use on the EnterpriseOMRSTopicConnector.
     *
     * @param enterpriseOMRSTopicProtocolVersion protocol version enum
     */
    public void setEnterpriseOMRSTopicProtocolVersion(OpenMetadataEventProtocolVersion enterpriseOMRSTopicProtocolVersion)
    {
        this.enterpriseOMRSTopicProtocolVersion = enterpriseOMRSTopicProtocolVersion;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */



    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */




    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
}
