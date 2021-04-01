/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;


import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
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
    public static void validateRelationship(Relationship relationship) throws SubjectAreaFVTCheckedException {
        if (relationship==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected relationship to exist,  ");
        }
        if (relationship.getName()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected relationship to have a name,  ");
        }
        if (relationship.getSystemAttributes()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + "'s system attributes to exist,  ");
        }
        if (relationship.getSystemAttributes().getGUID()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + "'s userId  to exist,  ");
        }
        if (relationship.isReadOnly()) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + " not to be readonly");
        }
        if (relationship.getEnd1() ==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + " end1 to have a value");
        }
        if (relationship.getEnd1().getNodeQualifiedName() ==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + " end1 qualified name to have a value");
        }
        if (relationship.getEnd1().getNodeGuid() ==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + " end1 guid to have a value");
        }
        if (relationship.getEnd2() ==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + " end2 to have a value");
        }
        if (relationship.getEnd2().getNodeQualifiedName() ==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + " end2 qualified name to have a value");
        }
        if (relationship.getEnd2().getNodeGuid() ==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + relationship.getName() + " end2 guid to have a value");
        }
    }
    public static void checkEnds(Relationship relationship1, Relationship relationship2, String relationshipName, String operation) throws SubjectAreaFVTCheckedException {
        if (!relationship1.getEnd1().getNodeGuid().equals(relationship2.getEnd1().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: update end 1 not as expected for relationship " + relationshipName + " operation " + operation);
        }
        if (!relationship1.getEnd2().getNodeGuid().equals(relationship2.getEnd2().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: update end 2 not as expected for relationship " + relationshipName + " operation " + operation);
        }
    }
}