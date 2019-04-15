/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.references.OpenDiscoveryEngineToOpenDiscoveryService;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.entities.OpenDiscoveryService.OpenDiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This is a reference, which is a view of a relationships from the perspective of one of the ends. A relationships is
 * the link between 2 OMAS entities.
 *
 * A reference is used when working with OMAS entity APIs. An OMAS entity has attributes, classifications
 * and references.
 *
 * This Reference is called SupportedDiscoveryServicesReference. It is used in type OpenDiscoveryEngine to represent a
 * reference to an OMAS entity of type openDiscoveryService. The reference also contains information
 * about the relationships; the relationships guid, name, relationships attributes and a list of unique attributes.
 * Relationship attributes are attributes of the relationships.
 *
 * It is possible to work with the relationships itself using the OMAS API using the relationships guid
 * contained in this reference.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedDiscoveryServicesReference extends Reference implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(SupportedDiscoveryServicesReference.class);
    private static final String className = SupportedDiscoveryServicesReference.class.getName();

    final static protected String relationship_Type = "SupportedDiscoveryService";
    protected OpenDiscoveryService openDiscoveryService =null;



    public OpenDiscoveryService getOpenDiscoveryService() {
        return  openDiscoveryService;
    }
    public void setOpenDiscoveryService(OpenDiscoveryService openDiscoveryService) {
        this.openDiscoveryService = openDiscoveryService;
    }


    public SupportedDiscoveryServicesReference() {
        this(null, null, (Map<String, Object>) null);
    }

    public SupportedDiscoveryServicesReference(String guid) {
        this(guid, null, (Map<String, Object>) null);
    }

    public SupportedDiscoveryServicesReference(String guid, String relatedEndType) {
        this(guid, relatedEndType, (Map<String, Object>) null);
    }

    public SupportedDiscoveryServicesReference(String relatedEndType, Map<String, Object> uniqueAttributes) {
        this(null, relatedEndType, uniqueAttributes);
    }

    public SupportedDiscoveryServicesReference(String guid, String relatedEndType, Map<String, Object> uniqueAttributes) {
        setRelationshipGuid(guid);
        setRelatedEndType(relatedEndType);
        setUniqueAttributes(uniqueAttributes);
    }

    public SupportedDiscoveryServicesReference(Reference other) {
        if (other != null) {
            setRelationshipGuid(other.getRelationshipGuid());
            setRelatedEndGuid(other.getRelatedEndGuid());
            setRelatedEndType(other.getRelatedEndType());
            setUniqueAttributes(other.getUniqueAttributes());
        }
    }
    
      /**
       * Populate the reference from a relationships
       * @param entityGuid String entity Guid
       * @param line OMRSLine
       */
    public SupportedDiscoveryServicesReference(String entityGuid, OMRSLine line) {
        setRelationshipGuid(line.getGuid());
        if (entityGuid.equals(line.getEntity1Guid())) {
            setRelatedEndGuid(line.getEntity2Guid());
            setRelatedEndType(line.getEntity2Type());
            // TODO UniqueAttributes
            //setUniqueAttributes(line.get
        } else {
            setRelatedEndGuid(line.getEntity1Guid());
            setRelatedEndType(line.getEntity1Type());
            // TODO UniqueAttributes
            //setUniqueAttributes(line.get
        }
    }    

    public SupportedDiscoveryServicesReference(Map objIdMap) {
        if (objIdMap != null) {
            Object reg = objIdMap.get(KEY_RELATED_END_GUID);
            Object rg = objIdMap.get(KEY_RELATIONSHIP_GUID);
            Object t = objIdMap.get(KEY_TYPENAME);
            Object u = objIdMap.get(KEY_UNIQUE_ATTRIBUTES);

            if (reg != null) {
                setRelatedEndGuid(reg.toString());
            }
            if (rg != null) {
                setRelationshipGuid(rg.toString());
            }


            if (t != null) {
                setRelatedEndType(t.toString());
            }

            if (u != null && u instanceof Map) {
                setUniqueAttributes((Map) u);
            }
        }
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Reference{");
        sb.append("relatedEndGuid='").append(getRelatedEndGuid()).append('\'');
        sb.append("relationshipGuid='").append(getRelationshipGuid()).append('\'');
        sb.append("relatedEndType='").append(getRelatedEndType()).append('\'');
        sb.append(", uniqueAttributes={");
//  AtlasBaseTypeDef.dumpObjects(uniqueAttributes, sb);
        sb.append("}");
 	sb.append("{");
 	sb.append('}');


        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reference that = (Reference) o;

        if (relatedEndGuid != null && !Objects.equals(relatedEndGuid, that.getRelatedEndGuid())) {
            return false;
        }
        if (relationshipGuid != null && !Objects.equals(relationshipGuid, that.getRelationshipGuid())) {
            return false;
        }
        SupportedDiscoveryServicesReference typedThat =(SupportedDiscoveryServicesReference)that;

        return Objects.equals(relatedEndType, that.getRelatedEndType()) &&
                Objects.equals(uniqueAttributes, that.getUniqueAttributes());
    }


    @Override
    public int hashCode() {
        return relatedEndGuid != null ? Objects.hash(relatedEndGuid) : Objects.hash(relatedEndType, uniqueAttributes
);
    }
}
