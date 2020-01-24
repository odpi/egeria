/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SuspectDuplicateAnnotation is the annotation used to record details of an asset that seems to be a duplicate of the asset being
 * analysed by a discovery service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SuspectDuplicateAnnotation extends Annotation
{
    private static final long serialVersionUID = 1L;

    List<String>  duplicateAnchorGUIDs = null;
    List<String>  matchingPropertyNames = null;
    List<String>  matchingClassificationNames = null;
    List<String>  matchingAttachmentGUIDs = null;
    List<String>  matchingRelationshipGUIDs = null;

    public SuspectDuplicateAnnotation()
    {
    }

    public SuspectDuplicateAnnotation(SuspectDuplicateAnnotation template)
    {
        super(template);

        if (template != null)
        {
            duplicateAnchorGUIDs = template.getDuplicateAnchorGUIDs();
            matchingPropertyNames = template.getMatchingPropertyNames();
            matchingClassificationNames = template.getMatchingClassificationNames();
            matchingAttachmentGUIDs = template.getMatchingAttachmentGUIDs();
            matchingRelationshipGUIDs = template.getMatchingRelationshipGUIDs();
        }
    }

    public List<String> getDuplicateAnchorGUIDs()
    {
        return duplicateAnchorGUIDs;
    }

    public void setDuplicateAnchorGUIDs(List<String> duplicateAnchorGUIDs)
    {
        this.duplicateAnchorGUIDs = duplicateAnchorGUIDs;
    }

    public List<String> getMatchingPropertyNames()
    {
        return matchingPropertyNames;
    }

    public void setMatchingPropertyNames(List<String> matchingPropertyNames)
    {
        this.matchingPropertyNames = matchingPropertyNames;
    }

    public List<String> getMatchingClassificationNames()
    {
        return matchingClassificationNames;
    }

    public void setMatchingClassificationNames(List<String> matchingClassificationNames)
    {
        this.matchingClassificationNames = matchingClassificationNames;
    }

    public List<String> getMatchingAttachmentGUIDs()
    {
        return matchingAttachmentGUIDs;
    }

    public void setMatchingAttachmentGUIDs(List<String> matchingAttachmentGUIDs)
    {
        this.matchingAttachmentGUIDs = matchingAttachmentGUIDs;
    }

    public List<String> getMatchingRelationshipGUIDs()
    {
        return matchingRelationshipGUIDs;
    }

    public void setMatchingRelationshipGUIDs(List<String> matchingRelationshipGUIDs)
    {
        this.matchingRelationshipGUIDs = matchingRelationshipGUIDs;
    }

    @Override
    public String toString()
    {
        return "SuspectDuplicateAnnotation{" +
                "matchingClassificationNames=" + matchingClassificationNames +
                ", matchingAttachmentGUIDs=" + matchingAttachmentGUIDs +
                ", matchingRelationshipGUIDs=" + matchingRelationshipGUIDs +
                ", annotationType='" + annotationType + '\'' +
                ", summary='" + summary + '\'' +
                ", confidenceLevel=" + confidenceLevel +
                ", expression='" + expression + '\'' +
                ", explanation='" + explanation + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", jsonProperties='" + jsonProperties + '\'' +
                ", numAttachedAnnotations=" + numAttachedAnnotations +
                ", annotationStatus=" + annotationStatus +
                ", reviewDate=" + reviewDate +
                ", steward='" + steward + '\'' +
                ", reviewComment='" + reviewComment + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                ", extendedProperties=" + extendedProperties +
                '}';
    }


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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SuspectDuplicateAnnotation that = (SuspectDuplicateAnnotation) objectToCompare;
        return Objects.equals(duplicateAnchorGUIDs, that.duplicateAnchorGUIDs) &&
                Objects.equals(matchingPropertyNames, that.matchingPropertyNames) &&
                Objects.equals(matchingClassificationNames, that.matchingClassificationNames) &&
                Objects.equals(matchingAttachmentGUIDs, that.matchingAttachmentGUIDs) &&
                Objects.equals(matchingRelationshipGUIDs, that.matchingRelationshipGUIDs);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), duplicateAnchorGUIDs, matchingPropertyNames, matchingClassificationNames, matchingAttachmentGUIDs, matchingRelationshipGUIDs);
    }
}
