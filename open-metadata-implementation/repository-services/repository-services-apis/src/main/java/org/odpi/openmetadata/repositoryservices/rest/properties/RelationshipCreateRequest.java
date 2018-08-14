/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipCreateRequest extends OMRSAPIRequest {

    private String relationshipTypeGUID = null;
    private InstanceProperties initialProperties = null;
    private String entityOneGUID = null;
    private String entityTwoGUID = null;
    private InstanceStatus initialStatus = null;

    /**
     * Default constructor
     */
    public RelationshipCreateRequest() {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelationshipCreateRequest(OMRSAPIRequest template) {
        super(template);
    }

    public String getRelationshipTypeGUID() {
        return relationshipTypeGUID;
    }

    public void setRelationshipTypeGUID(String relationshipTypeGUID) {
        this.relationshipTypeGUID = relationshipTypeGUID;
    }

    public InstanceProperties getInitialProperties() {
        return initialProperties;
    }

    public void setInitialProperties(InstanceProperties initialProperties) {
        this.initialProperties = initialProperties;
    }

    public String getEntityOneGUID() {
        return entityOneGUID;
    }

    public void setEntityOneGUID(String entityOneGUID) {
        this.entityOneGUID = entityOneGUID;
    }

    public String getEntityTwoGUID() {
        return entityTwoGUID;
    }

    public void setEntityTwoGUID(String entityTwoGUID) {
        this.entityTwoGUID = entityTwoGUID;
    }

    public InstanceStatus getInitialStatus() {
        return initialStatus;
    }

    public void setInitialStatus(InstanceStatus initialStatus) {
        this.initialStatus = initialStatus;
    }


    @Override
    public String toString() {
        return "RelationshipCreateRequest{" +
                "relationshipTypeGUID='" + relationshipTypeGUID + '\'' +
                ", initialProperties=" + initialProperties +
                ", entityOneGuid=" + entityOneGUID +
                ", entityTwoGuid=" + entityTwoGUID +
                ", initialStatus=" + initialStatus +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare) {
        if (this == objectToCompare) {
            return true;
        }
        if (!(objectToCompare instanceof RelationshipCreateRequest)) {
            return false;
        }
        RelationshipCreateRequest that = (RelationshipCreateRequest) objectToCompare;
        return Objects.equals(getRelationshipTypeGUID(), that.getRelationshipTypeGUID()) &&
                Objects.equals(getInitialProperties(), that.getInitialProperties()) &&
                Objects.equals(getEntityOneGUID(), that.getEntityOneGUID()) &&
                Objects.equals(getEntityTwoGUID(), that.getEntityTwoGUID()) &&
                getInitialStatus() == that.getInitialStatus();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode() {

        return Objects.hash(getRelationshipTypeGUID(),
                getInitialProperties(),
                getEntityOneGUID(),
                getEntityTwoGUID(),
                getInitialStatus());
    }

}
