/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossaryauthor.properties;

import org.junit.Test;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;


public class BreadCrumbTrailTest {
    @Test
    public void testBreadCrumbTrail() {


        BreadCrumb breadCrumb1 = new BreadCrumb("guid1", "displayName1", NodeType.Term);
        BreadCrumb breadCrumb2 = new BreadCrumb("guid2", "displayName2", NodeType.Category);
        BreadCrumb breadCrumb3 = new BreadCrumb("guid3", "displayName3", NodeType.Category);
        BreadCrumb breadCrumb4 = new BreadCrumb("guid4", "displayName4", NodeType.Category);
        BreadCrumb breadCrumb5 = new BreadCrumb("guid5", "displayName5", NodeType.Glossary);

        // test1 term cat glossary
        List<BreadCrumb> inputBreadCrumbs = new ArrayList<>();
        inputBreadCrumbs.add(breadCrumb1);
        inputBreadCrumbs.add(breadCrumb2);
        inputBreadCrumbs.add(breadCrumb5);

        BreadCrumbTrail breadCrumbTrail = new BreadCrumbTrail(inputBreadCrumbs);
        List<BreadCrumb> outputBreadCrumbs = breadCrumbTrail.getBreadCrumbs();

        assertEquals(outputBreadCrumbs.get(0).getPath(), "/nodeType/Glossary/guid/guid5/displayName/displayName5");

        // test1 term cat cat cat glossary
        inputBreadCrumbs = new ArrayList<>();
        inputBreadCrumbs.add(breadCrumb1);
        inputBreadCrumbs.add(breadCrumb2);
        inputBreadCrumbs.add(breadCrumb3);
        inputBreadCrumbs.add(breadCrumb4);
        inputBreadCrumbs.add(breadCrumb5);

        breadCrumbTrail = new BreadCrumbTrail(inputBreadCrumbs);
        outputBreadCrumbs = breadCrumbTrail.getBreadCrumbs();

        assertEquals(outputBreadCrumbs.get(4).getPath(), "/nodeType/Glossary/guid/guid5/displayName/displayName5" +
                "/nodeType/Category/guid/guid4/displayName/displayName4" +
                "/nodeType/Category/guid/guid3/displayName/displayName3" +
                "/nodeType/Category/guid/guid2/displayName/displayName2" +
                "/nodeType/Term/guid/guid1/displayName/displayName1");

        // test1 term cat cat cat glossary
        inputBreadCrumbs = new ArrayList<>();
        inputBreadCrumbs.add(breadCrumb1);
        inputBreadCrumbs.add(breadCrumb5);

        breadCrumbTrail = new BreadCrumbTrail(inputBreadCrumbs);
        outputBreadCrumbs = breadCrumbTrail.getBreadCrumbs();

        assertEquals(outputBreadCrumbs.get(1).getPath(), "/nodeType/Glossary/guid/guid5/displayName/displayName5" +
                "/nodeType/Term/guid/guid1/displayName/displayName1");


    }


}
