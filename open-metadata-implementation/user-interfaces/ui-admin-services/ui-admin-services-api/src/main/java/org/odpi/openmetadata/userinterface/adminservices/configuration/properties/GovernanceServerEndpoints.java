/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;

import java.util.Objects;
/**
 * org.odpi.openmetadata.userinterface.adminservices.configuration.properties.GovernanceServerEndpoints an endpoint for each governance server.
 * If specified then the endpoint overrides the OMAG endpoint
 */
public class GovernanceServerEndpoints {
    private ServerEndpointConfig date_engine;
    private ServerEndpointConfig data_platform;
    private ServerEndpointConfig discovery_engine;
    private ServerEndpointConfig open_lineage;
    private ServerEndpointConfig security_officer;
    private ServerEndpointConfig security_sync;
    private ServerEndpointConfig stewardship;
    private ServerEndpointConfig virtualisation;
    /**
     * Get the date engine endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getDateEngine() {
        return date_engine;
    }
    /**
     * Set the date engine endpoint 
     * @param date_engine the endpoint for this governance server
     */
    public void setDateEngine(ServerEndpointConfig date_engine) {
        this.date_engine = date_engine;
    }
    /**
     * Get the data platform endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getDataPlatform() {
        return data_platform;
    }
    /**
     * Set the date platform endpoint 
     * @param data_platform the endpoint for this governance server
     */
    public void setDataPlatform(ServerEndpointConfig data_platform) {
        this.data_platform = data_platform;
    }
    /**
     * Get the discovery engine endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getDiscoveryEngine() {
        return discovery_engine;
    }
    /**
     * Set the discovery engine endpoint 
     * @param discovery_engine the endpoint for this governance server
     */
    public void setDiscoveryEngine(ServerEndpointConfig discovery_engine) {
        this.discovery_engine = discovery_engine;
    }
    /**
     * Get the open lineage endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getOpenLineage() {
        return open_lineage;
    }
    /**
     * Set the open lineage endpoint 
     * @param open_lineage the endpoint for this governance server
     */
    public void setOpenLineage(ServerEndpointConfig open_lineage) {
        this.open_lineage = open_lineage;
    }
    /**
     * Get the security officer endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getSecurityOfficer() {
        return security_officer;
    }
    /**
     * Set the security officer endpoint 
     * @param security_officer the endpoint for this governance server
     */
    public void setSecurityOfficer(ServerEndpointConfig security_officer) {
        this.security_officer = security_officer;
    }
    /**
     * Get the security sync endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getSecuritySync() {
        return security_sync;
    }
    /**
     * Set the security sync endpoint 
     * @param security_sync the endpoint for this governance server
     */
    public void setSecuritySync(ServerEndpointConfig security_sync) {
        this.security_sync = security_sync;
    }
    /**
     * Get the stewardship endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getStewardship() {
        return stewardship;
    }
    /**
     * Set the stewardship endpoint 
     * @param stewardship the endpoint for this governance server
     */
    public void setStewardship(ServerEndpointConfig stewardship) {
        this.stewardship = stewardship;
    }
    /**
     * Get the virtualisation endpoint 
     * @return endpoint for this governance server
     */
    public ServerEndpointConfig getVirtualisation() {
        return virtualisation;
    }
    /**
     * Set the virtualisation endpoint 
     * @param virtualisation the endpoint for this governance server
     */
    public void setVirtualisation(ServerEndpointConfig virtualisation) {
        this.virtualisation = virtualisation;
    }

    @Override
    public String toString() {
        return "GovernanceServerEndpoints{" +
                "date_engine=" + date_engine +
                ", data_platform=" + data_platform +
                ", discovery_engine=" + discovery_engine +
                ", open_lineage=" + open_lineage +
                ", security_officer=" + security_officer +
                ", security_sync=" + security_sync +
                ", stewardship=" + stewardship +
                ", virtualisation=" + virtualisation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GovernanceServerEndpoints)) return false;
        GovernanceServerEndpoints that = (GovernanceServerEndpoints) o;
        return Objects.equals(getDateEngine(), that.getDateEngine()) &&
                Objects.equals(getDataPlatform(), that.getDataPlatform()) &&
                Objects.equals(getDiscoveryEngine(), that.getDiscoveryEngine()) &&
                Objects.equals(getOpenLineage(), that.getOpenLineage()) &&
                Objects.equals(getSecurityOfficer(), that.getSecurityOfficer()) &&
                Objects.equals(getSecuritySync(), that.getSecuritySync()) &&
                Objects.equals(getStewardship(), that.getStewardship()) &&
                Objects.equals(getVirtualisation(), that.getVirtualisation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateEngine(), getDataPlatform(), getDiscoveryEngine(), getOpenLineage(), getSecurityOfficer(), getSecuritySync(), getStewardship(), getVirtualisation());
    }
}
