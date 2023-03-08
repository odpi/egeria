/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineDetails {

    private static final long    serialVersionUID = 1L;

    private String                                    engineGUID;
    private String                                    engineDisplayName;
    private String                                    engineQualifiedName;
    private String                                    engineDescription;
    private String                                    engineTypeDescription;
    private String                                    engineVersion;
    private Map<String, ServicePropertiesAndRequests> serviceMap;


    /**
     * Default Constructor sets the properties to nulls
     */
    public EngineDetails()
    {
        /*
         * Nothing to do.
         */
    }

    public EngineDetails(String                                    engineGUID,
                         String                                    engineDisplayName,
                         String                                    engineQualifiedName,
                         String                                    engineDescription,
                         String                                    engineTypeDescription,
                         String                                    engineVersion,
                         Map<String, ServicePropertiesAndRequests> serviceMap )
    {

        this.engineGUID                  = engineGUID;
        this.engineDisplayName           = engineDisplayName;
        this.engineQualifiedName         = engineQualifiedName;
        this.engineDescription           = engineDescription;
        this.engineTypeDescription       = engineTypeDescription;
        this.engineVersion               = engineVersion;
        this.serviceMap                  = serviceMap;

    }


    public String getEngineGUID() {
        return engineGUID;
    }

    public void setEngineGUID(String engineGUID) {
        this.engineGUID = engineGUID;
    }

    public String getEngineDisplayName() { return engineDisplayName;  }

    public void setEngineDisplayName(String engineDisplayName) {
        this.engineDisplayName = engineDisplayName;
    }

    public String getEngineQualifiedName() {
        return engineQualifiedName;
    }

    public void setEngineQualifiedName(String engineQualifiedName) {
        this.engineQualifiedName = engineQualifiedName;
    }

    public String getEngineDescription() {
        return engineDescription;
    }

    public void setEngineDescription(String engineDescription) {
        this.engineDescription = engineDescription;
    }

    public String getEngineTypeDescription() {
        return engineTypeDescription;
    }

    public void setEngineTypeDescription(String engineTypeDescription) { this.engineTypeDescription = engineTypeDescription;  }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public Map<String, ServicePropertiesAndRequests> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, ServicePropertiesAndRequests> serviceMap) {
        this.serviceMap = serviceMap;
    }



    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EngineDetails{" +
                ", engineDisplayName='" + engineDisplayName + '\'' +
                ", engineQualifiedName='" + engineQualifiedName + '\'' +
                ", engineDescription='" + engineDescription + '\'' +
                ", engineTypeDescription='" + engineTypeDescription + '\'' +
                ", engineVersion='" + engineVersion + '\'' +
                ", engineGUID=" + engineGUID +'\'' +
                ", serviceMap=" + serviceMap +'\'' +
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
        EngineDetails that = (EngineDetails) objectToCompare;
        return Objects.equals(getEngineQualifiedName(), that.getEngineQualifiedName()) &&
                Objects.equals(getEngineGUID(), that.getEngineGUID()) &&
                Objects.equals(getEngineDisplayName(), that.getEngineDisplayName()) &&
                Objects.equals(getEngineVersion(), that.getEngineVersion()) &&
                Objects.equals(getEngineDescription(), that.getEngineDescription()) &&
                Objects.equals(getEngineTypeDescription(), that.getEngineTypeDescription()) &&
                Objects.equals(getServiceMap(), that.getServiceMap()) ;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getEngineQualifiedName(), getEngineGUID(), getEngineDisplayName(),
                            getEngineDescription(), getEngineVersion(), getEngineTypeDescription(),
                            getServiceMap() );
    }

}
