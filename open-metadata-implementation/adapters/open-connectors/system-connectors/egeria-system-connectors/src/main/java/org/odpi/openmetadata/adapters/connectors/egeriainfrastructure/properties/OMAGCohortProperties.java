/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.properties.CohortConnectionStatus;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGCohortProperties captures details about a specific cohort from a particular server's point of view.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGCohortProperties
{
    private String                        cohortName          = null;
    private CohortConnectionStatus        connectionStatus    = null;
    private List<OMAGConnectorProperties> connectors          = null;
    private MemberRegistration            localRegistration   = null;
    private List<MemberRegistration>      remoteRegistrations = null;


    public OMAGCohortProperties()
    {
    }


    public String getCohortName()
    {
        return cohortName;
    }

    public void setCohortName(String cohortName)
    {
        this.cohortName = cohortName;
    }

    public CohortConnectionStatus getConnectionStatus()
    {
        return connectionStatus;
    }

    public void setConnectionStatus(CohortConnectionStatus connectionStatus)
    {
        this.connectionStatus = connectionStatus;
    }

    public List<OMAGConnectorProperties> getConnectors()
    {
        return connectors;
    }

    public void setConnectors(List<OMAGConnectorProperties> connectors)
    {
        this.connectors = connectors;
    }

    public MemberRegistration getLocalRegistration()
    {
        return localRegistration;
    }

    public void setLocalRegistration(MemberRegistration localRegistration)
    {
        this.localRegistration = localRegistration;
    }

    public List<MemberRegistration> getRemoteRegistrations()
    {
        return remoteRegistrations;
    }

    public void setRemoteRegistrations(List<MemberRegistration> remoteRegistrations)
    {
        this.remoteRegistrations = remoteRegistrations;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGCohortProperties{" +
                "cohortName='" + cohortName + '\'' +
                ", connectionStatus=" + connectionStatus +
                ", connectors=" + connectors +
                ", localRegistration=" + localRegistration +
                ", remoteRegistrations=" + remoteRegistrations +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        OMAGCohortProperties that = (OMAGCohortProperties) objectToCompare;
        return Objects.equals(cohortName, that.cohortName) && connectionStatus == that.connectionStatus && Objects.equals(connectors, that.connectors) && Objects.equals(localRegistration, that.localRegistration) && Objects.equals(remoteRegistrations, that.remoteRegistrations);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(cohortName, connectionStatus, connectors, localRegistration, remoteRegistrations);
    }
}
