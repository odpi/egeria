/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToLicenseType;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.entities.LicenseType.LicenseType;
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
 * This Reference is called LicensesReference. It is used in type Referenceable to represent a
 * reference to an OMAS entity of type licenseType. The reference also contains information
 * about the relationships; the relationships guid, name, relationships attributes and a list of unique attributes.
 * Relationship attributes are attributes of the relationships.
 *
 * It is possible to work with the relationships itself using the OMAS API using the relationships guid
 * contained in this reference.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LicensesReference extends Reference implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(LicensesReference.class);
    private static final String className = LicensesReference.class.getName();

    final static protected String relationship_Type = "License";
    protected LicenseType licenseType =null;



    public LicenseType getLicenseType() {
        return  licenseType;
    }
    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }
    private String licenseGUID;
    /**
     * Unique identifier of the actual license.
     * @return LicenseGUID
     */
    public String getLicenseGUID() {
        return this.licenseGUID;
    }

    public void setLicenseGUID(String licenseGUID) {
        this.licenseGUID = licenseGUID;
    }
    private String start;
    /**
     * Start date for the license.
     * @return Start
     */
    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }
    private String end;
    /**
     * End date for the license.
     * @return End
     */
    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    private String conditions;
    /**
     * Any special conditions or endorsements over the basic license type.
     * @return Conditions
     */
    public String getConditions() {
        return this.conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    private String licensedBy;
    /**
     * Person or organization that owns the intellectual property.
     * @return LicensedBy
     */
    public String getLicensedBy() {
        return this.licensedBy;
    }

    public void setLicensedBy(String licensedBy) {
        this.licensedBy = licensedBy;
    }
    private String custodian;
    /**
     * The person, engine or organization tht will ensure the license is honored.
     * @return Custodian
     */
    public String getCustodian() {
        return this.custodian;
    }

    public void setCustodian(String custodian) {
        this.custodian = custodian;
    }
    private String licensee;
    /**
     * The person or organization that holds the license.
     * @return Licensee
     */
    public String getLicensee() {
        return this.licensee;
    }

    public void setLicensee(String licensee) {
        this.licensee = licensee;
    }
    private String notes;
    /**
     * Additional notes about the license.
     * @return Notes
     */
    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public LicensesReference() {
        this(null, null, (Map<String, Object>) null);
    }

    public LicensesReference(String guid) {
        this(guid, null, (Map<String, Object>) null);
    }

    public LicensesReference(String guid, String relatedEndType) {
        this(guid, relatedEndType, (Map<String, Object>) null);
    }

    public LicensesReference(String relatedEndType, Map<String, Object> uniqueAttributes) {
        this(null, relatedEndType, uniqueAttributes);
    }

    public LicensesReference(String guid, String relatedEndType, Map<String, Object> uniqueAttributes) {
        setRelationshipGuid(guid);
        setRelatedEndType(relatedEndType);
        setUniqueAttributes(uniqueAttributes);
    }

    public LicensesReference(Reference other) {
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
    public LicensesReference(String entityGuid, OMRSLine line) {
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

    public LicensesReference(Map objIdMap) {
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
	sb.append("this.licenseGUID ");
	sb.append("this.start ");
	sb.append("this.end ");
	sb.append("this.conditions ");
	sb.append("this.licensedBy ");
	sb.append("this.custodian ");
	sb.append("this.licensee ");
	sb.append("this.notes ");
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
        LicensesReference typedThat =(LicensesReference)that;
        if (this.licenseGUID != null && !Objects.equals(this.licenseGUID,typedThat.getLicenseGUID())) {
            return false;
        }
        if (this.start != null && !Objects.equals(this.start,typedThat.getStart())) {
            return false;
        }
        if (this.end != null && !Objects.equals(this.end,typedThat.getEnd())) {
            return false;
        }
        if (this.conditions != null && !Objects.equals(this.conditions,typedThat.getConditions())) {
            return false;
        }
        if (this.licensedBy != null && !Objects.equals(this.licensedBy,typedThat.getLicensedBy())) {
            return false;
        }
        if (this.custodian != null && !Objects.equals(this.custodian,typedThat.getCustodian())) {
            return false;
        }
        if (this.licensee != null && !Objects.equals(this.licensee,typedThat.getLicensee())) {
            return false;
        }
        if (this.notes != null && !Objects.equals(this.notes,typedThat.getNotes())) {
            return false;
        }

        return Objects.equals(relatedEndType, that.getRelatedEndType()) &&
                Objects.equals(uniqueAttributes, that.getUniqueAttributes());
    }


    @Override
    public int hashCode() {
        return relatedEndGuid != null ? Objects.hash(relatedEndGuid) : Objects.hash(relatedEndType, uniqueAttributes
, this.licenseGUID
, this.start
, this.end
, this.conditions
, this.licensedBy
, this.custodian
, this.licensee
, this.notes
);
    }
}
