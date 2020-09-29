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
    public static void checkEnds(Line line1, Line line2, String lineName, String operation) throws SubjectAreaFVTCheckedException {
        if (!line1.getEnd1().getNodeGuid().equals(line2.getEnd1().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: update end 1 not as expected for relationship " + lineName + " operation " + operation);
        }
        if (!line1.getEnd2().getNodeGuid().equals(line2.getEnd2().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: update end 2 not as expected for relationship " + lineName + " operation " + operation);
        }
    }
}