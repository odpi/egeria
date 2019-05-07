package org.odpi.openmetadata.accessservices.assetlineage.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
public class RelationshipEvent extends AssetLineageEvent {

    private EntityProxy entityProxyOne;
    private EntityProxy entityProxyTwo;
    private String typeDefName;

    public EntityProxy getEntityProxyOne() {
        return entityProxyOne;
    }

    public void setEntityProxyOne(EntityProxy entityProxyOne) {
        this.entityProxyOne = entityProxyOne;
    }

    public EntityProxy getEntityProxyTwo() {
        return entityProxyTwo;
    }

    public void setEntityProxyTwo(EntityProxy entityProxyTwo) {
        this.entityProxyTwo = entityProxyTwo;
    }

    public String getTypeDefName() {
        return typeDefName;
    }

    public void setTypeDefName(String typeDefName) {
        this.typeDefName = typeDefName;
    }

}
