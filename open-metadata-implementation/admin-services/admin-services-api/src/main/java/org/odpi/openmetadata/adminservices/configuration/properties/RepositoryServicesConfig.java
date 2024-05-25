/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * RepositoryServicesConfig provides the configuration properties that are needed by the OMRS components
 * to manage access to the metadata repositories that are members of the open metadata repository cohorts that
 * this server connects to.
 * <ul>
 *     <li>
 *         auditLogConnection is a connection describing the connector to the AuditLog that the OMRS
 *         component should use.
 *     </li>
 *     <li>
 *         openMetadataArchiveConnections is a list of Open Metadata Archive Connections.
 *         An open metadata archive connection provides properties needed to create a connector to manage
 *         an open metadata archive.  This contains pre-built TypeDefs and metadata instance.
 *         The archives are managed by the OMRSArchiveManager.
 *     </li>
 *     <li>
 *         localRepositoryConfig describes the properties used to manage the local metadata repository for this server.
 *     </li>
 *     <li>
 *         enterpriseAccessConfig describes the properties that control the cohort federation services that the
 *         OMRS provides to the Open Metadata AccessServices (OMASs).
 *     </li>
 *     <li>
 *         cohortConfigList provides details of each open metadata repository cohort that the local server is
 *         connected to.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RepositoryServicesConfig extends AdminServicesConfigHeader
{
    private List<Connection>       auditLogConnections            = new ArrayList<>();
    private List<Connection>       openMetadataArchiveConnections = new ArrayList<>();
    private LocalRepositoryConfig  localRepositoryConfig          = null;
    private EnterpriseAccessConfig enterpriseAccessConfig         = null;
    private List<CohortConfig>     cohortConfigList               = new ArrayList<>();


    /**
     * Default constructor does nothing
     */
    public RepositoryServicesConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RepositoryServicesConfig(RepositoryServicesConfig  template)
    {
        super();

        if (template != null)
        {
            this.auditLogConnections = template.getAuditLogConnections();
            this.openMetadataArchiveConnections = template.getOpenMetadataArchiveConnections();
            this.localRepositoryConfig = template.getLocalRepositoryConfig();
            this.enterpriseAccessConfig = template.getEnterpriseAccessConfig();
            this.cohortConfigList = template.getCohortConfigList();
        }
    }


    /**
     * Return the Connection properties used to create an OCF Connector to the AuditLog.
     *
     * @return Connection object
     */
    public List<Connection> getAuditLogConnections()
    {
        if (auditLogConnections == null)
        {
            return null;
        }
        else if (auditLogConnections.isEmpty())
        {
            return null;
        }
        else
        {
            return auditLogConnections;
        }
    }


    /**
     * Set up the Connection properties used to create an OCF Connector to the AuditLog.
     *
     * @param auditLogConnections list of Connection objects
     */
    public void setAuditLogConnections(List<Connection> auditLogConnections)
    {
        this.auditLogConnections = auditLogConnections;
    }


    /**
     * Return the list of Connection object, each of which is used to create the Connector to an Open Metadata
     * Archive.  Open Metadata Archive contains pre-built metadata types and instances.
     *
     * @return list of Connection objects
     */
    public List<Connection> getOpenMetadataArchiveConnections()
    {
        if (openMetadataArchiveConnections == null)
        {
            return null;
        }
        else if (openMetadataArchiveConnections.isEmpty())
        {
            return null;
        }
        else
        {
            return openMetadataArchiveConnections;
        }
    }


    /**
     * Set up the list of Connection object, each of which is used to create the Connector to an Open Metadata
     * Archive.  Open Metadata Archive contains pre-built metadata types and instances.
     *
     * @param openMetadataArchiveConnections list of Connection objects
     */
    public void setOpenMetadataArchiveConnections(List<Connection> openMetadataArchiveConnections)
    {
        this.openMetadataArchiveConnections = openMetadataArchiveConnections;
    }


    /**
     * Return the configuration properties for the local repository.
     *
     * @return configuration properties
     */
    public LocalRepositoryConfig getLocalRepositoryConfig()
    {
        return localRepositoryConfig;
    }


    /**
     * Set up the configuration properties for the local repository.
     *
     * @param localRepositoryConfig configuration properties
     */
    public void setLocalRepositoryConfig(LocalRepositoryConfig localRepositoryConfig)
    {
        this.localRepositoryConfig = localRepositoryConfig;
    }


    /**
     * Return the configuration for the federation services provided by OMRS to the Open Metadata Access
     * Services (OMASs).
     *
     * @return configuration properties
     */
    public EnterpriseAccessConfig getEnterpriseAccessConfig()
    {
        return enterpriseAccessConfig;
    }


    /**
     * Set up the configuration for the federation services provided by OMRS to the Open Metadata Access
     * Services (OMASs).
     *
     * @param enterpriseAccessConfig configuration properties
     */
    public void setEnterpriseAccessConfig(EnterpriseAccessConfig enterpriseAccessConfig)
    {
        this.enterpriseAccessConfig = enterpriseAccessConfig;
    }


    /**
     * Return the configuration properties for each open metadata repository cohort that this local server
     * connects to.
     *
     * @return list of cohort configuration properties
     */
    public List<CohortConfig> getCohortConfigList()
    {
        if (cohortConfigList == null)
        {
            return null;
        }
        else if (cohortConfigList.isEmpty())
        {
            return null;
        }
        else
        {
            return cohortConfigList;
        }
    }


    /**
     * Set up the configuration properties for each open metadata repository cohort that this local server
     * connects to.
     *
     * @param cohortConfigList list of cohort configuration properties
     */
    public void setCohortConfigList(List<CohortConfig> cohortConfigList)
    {
        this.cohortConfigList = cohortConfigList;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "RepositoryServicesConfig{" +
                "auditLogConnections=" + auditLogConnections +
                ", openMetadataArchiveConnections=" + openMetadataArchiveConnections +
                ", localRepositoryConfig=" + localRepositoryConfig +
                ", enterpriseAccessConfig=" + enterpriseAccessConfig +
                ", cohortConfigList=" + cohortConfigList +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        RepositoryServicesConfig that = (RepositoryServicesConfig) objectToCompare;
        return Objects.equals(getAuditLogConnections(), that.getAuditLogConnections()) &&
                Objects.equals(getOpenMetadataArchiveConnections(), that.getOpenMetadataArchiveConnections()) &&
                Objects.equals(getLocalRepositoryConfig(), that.getLocalRepositoryConfig()) &&
                Objects.equals(getEnterpriseAccessConfig(), that.getEnterpriseAccessConfig()) &&
                Objects.equals(getCohortConfigList(), that.getCohortConfigList());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAuditLogConnections(), getOpenMetadataArchiveConnections(), getLocalRepositoryConfig(),
                            getEnterpriseAccessConfig(), getCohortConfigList());
    }
}
