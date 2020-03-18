/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexPreTraversal {


    // The RexPreTraversal class enables the packaging and interrogation of a predictive stats
    // for a potential traversal. It does NOT contain the sub-graph - that is in a RexTraversal
    // object.
    // The PreTraversal includes the entityGUID of the entity on which it is centered and the depth.
    // The PreTraversal is formed from the result of a traversal with no filtering - it includes
    // 3 maps of type information - one per category - keyed by typeName, where each entry (value)
    // contains the typeGUID and number of instances of that type in the traversal, i.e:
    //   {  typeName --> { typeGUID : <string> , count : <int> } }
    // The typeName and typeGUID are both included in order to minimise conversion in the browser for
    // display purposes. All 3 maps are keyed by typeName as names are available for classifications.
    // In a RexTypeStats for a classification the typeGUID is ignored (set to null).
    // A PreTraversal does not need a gen.


    private String                    entityGUID;                    // must be non-null
    private Map<String,RexTypeStats>  entityInstanceCounts;          // a map from typename to stats
    private Map<String,RexTypeStats>  relationshipInstanceCounts;    // a map from typename to stats
    private Map<String,RexTypeStats>  classificationInstanceCounts;  // a map from typename to stats
    private Integer                   depth;                         // the depth of traversal


    public RexPreTraversal() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getEntityGUID() { return entityGUID; }

    public Map<String,RexTypeStats> getEntityInstanceCounts() { return entityInstanceCounts; }

    public Map<String,RexTypeStats> getRelationshipInstanceCounts() {
        return relationshipInstanceCounts;
    }

    public Map<String,RexTypeStats> getClassificationInstanceCounts() {
        return classificationInstanceCounts;
    }

    public Integer getDepth() { return depth; }

    public void setEntityGUID(String entityGUID) { this.entityGUID = entityGUID; }

    public void setEntityInstanceCounts(Map<String,RexTypeStats> entityInstanceCounts) { this.entityInstanceCounts = entityInstanceCounts; }

    public void setRelationshipInstanceCounts(Map<String,RexTypeStats> relationshipInstanceCounts) { this.relationshipInstanceCounts = relationshipInstanceCounts; }

    public void setClassificationInstanceCounts(Map<String,RexTypeStats> classificationInstanceCounts) { this.classificationInstanceCounts = classificationInstanceCounts; }

    public void setDepth(Integer depth) { this.depth = depth; }



    @Override
    public String toString()
    {
        return "RexPreTraversal{" +
                ", entityGUID=" + entityGUID +
                ", depth=" + depth +
                ", entityInstanceCounts=" + entityInstanceCounts +
                ", relationshipInstanceCounts=" + relationshipInstanceCounts +
                ", classificationInstanceCounts=" + classificationInstanceCounts +
                '}';
    }



}
