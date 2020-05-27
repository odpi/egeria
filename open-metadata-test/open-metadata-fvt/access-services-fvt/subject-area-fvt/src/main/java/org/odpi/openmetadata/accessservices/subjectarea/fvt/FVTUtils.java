/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;


import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;

/**
 * FVT utilities
 */
public class FVTUtils {
    public static void validateNode(Node node) throws SubjectAreaFVTCheckedException {
        if (node==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected node to exist,  ");
        }
        if (node.getName()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected node to have a name,  ");
        }
        if (node.getSystemAttributes()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + node.getName() + "'s system attributes to exist,  ");
        }
        if (node.getSystemAttributes().getGUID()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + node.getName() + "'s userId  to exist,  ");
        }
    }
    public static void validateLine(Line line) throws SubjectAreaFVTCheckedException {
        if (line==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected line to exist,  ");
        }
        if (line.getName()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected line to have a name,  ");
        }
        if (line.getSystemAttributes()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + line.getName() + "'s system attributes to exist,  ");
        }
        if (line.getSystemAttributes().getGUID()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + line.getName() + "'s userId  to exist,  ");
        }
    }

    /**
     * Check that the guids for a relationship end 1 match
     * @param lineType string Type of Line
     * @param entityGuid entityGuid to compare
     * @param relationshipEnd1Guid relationshipEnd1Guid to compare
     * @throws SubjectAreaFVTCheckedException
     */
    static void checkGuidEnd1s(String lineType,String  entityGuid, String relationshipEnd1Guid) throws SubjectAreaFVTCheckedException {
        if (!entityGuid.equals(relationshipEnd1Guid)) {
            throw new SubjectAreaFVTCheckedException("ERROR: "+ lineType + " Relationship end 1 userId not as expected entityGuid=" + entityGuid+",relationshipEnd1Guid="+relationshipEnd1Guid);
        }
    }
    /**
     * Check that the guids for a relationship end 2 match
     * @param lineType string Type of Line
     * @param entityGuid entityGuid to compare
     * @param relationshipEnd2Guid relationshipEnd2Guid to compare
     * @throws SubjectAreaFVTCheckedException
     */
    static void checkGuidEnd2s(String lineType,String  entityGuid, String relationshipEnd2Guid) throws SubjectAreaFVTCheckedException {
        if (!entityGuid.equals(relationshipEnd2Guid)) {
            throw new SubjectAreaFVTCheckedException("ERROR: "+ lineType + " Relationship end 2 userId not as expected entityGuid=" + entityGuid+",relationshipEnd2Guid="+relationshipEnd2Guid);
        }
    }
}