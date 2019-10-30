/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetlineage.model.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
public class RelationshipEvent extends AssetLineageEntityEvent {

    private GlossaryTerm glossaryTerm = null;
    private String relationshipGuid = null;
    private String typeDefName = null;
    private Map<String,AssetLineageEntityEvent> proxies = null;

    public GlossaryTerm getGlossaryTerm() {
        return glossaryTerm;
    }

    public void setGlossaryTerm(GlossaryTerm glossaryTerm) {
        this.glossaryTerm = glossaryTerm;
    }

    public String getTypeDefName() {
        return typeDefName;
    }

    public void setTypeDefName(String typeDefName) {
        this.typeDefName = typeDefName;
    }

    public String getRelationshipGuid() {
        return relationshipGuid;
    }

    public void setRelationshipGuid(String typeDefGUID) {
        this.relationshipGuid = typeDefGUID;
    }

    public Map<String,AssetLineageEntityEvent> getProxies() {
        return proxies;
    }

    public void setProxies(Map<String,AssetLineageEntityEvent> proxies) {
        this.proxies = proxies;
    }
}
