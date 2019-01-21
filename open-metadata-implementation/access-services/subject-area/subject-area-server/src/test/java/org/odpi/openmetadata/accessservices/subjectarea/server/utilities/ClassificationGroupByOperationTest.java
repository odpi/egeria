/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.utilities;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidentialityLevel;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaTermRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.ClassificationGroupByOperation;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassificationGroupByOperationTest {
    @Test
    void testnulls(){
        ClassificationGroupByOperation classificationGroupByOperation = new ClassificationGroupByOperation(SubjectAreaTermRESTServices.SUBJECT_AREA_TERM_CLASSIFICATIONS,new HashSet<Classification>(),new HashSet<Classification>());
        assert(classificationGroupByOperation.getAddClassifications().isEmpty() == true);
        assert(classificationGroupByOperation.getUpdateClassifications().isEmpty() == true);
        assert(classificationGroupByOperation.getRemoveClassifications().isEmpty() == true);
    }
    @Test
    void testAdd(){
        Set<Classification> existing= new HashSet<>();
        Set<Classification> requesting=new HashSet<>(Arrays.asList(new SpineObject(), new SpineAttribute(),new ObjectIdentifier(),new Confidentiality(),new Confidence(),
                new Criticality(),new Retention(),new AbstractConcept(),new  ActivityDescription(), new DataValue(),new ContextDefinition()));
        ClassificationGroupByOperation classificationGroupByOperation = new ClassificationGroupByOperation(SubjectAreaTermRESTServices.SUBJECT_AREA_TERM_CLASSIFICATIONS,existing,requesting);
        assert(classificationGroupByOperation.getAddClassifications().size() == 11);
        assert(classificationGroupByOperation.getUpdateClassifications().isEmpty() ==true);
        assert(classificationGroupByOperation.getRemoveClassifications().isEmpty() == true);
    }
    @Test
    void testUpdate(){
        Set<Classification> existing=new HashSet<>(Arrays.asList(new SpineObject(), new SpineAttribute(),new ObjectIdentifier(),new Confidentiality(),new Confidence(),
                new Criticality(),new Retention(),new AbstractConcept(),new DataValue(),new  ActivityDescription(),new ContextDefinition()));
        Set<Classification> requesting=new HashSet<>(Arrays.asList(new SpineObject(), new SpineAttribute(),new ObjectIdentifier(),new Confidentiality(),new Confidence(),
                new Criticality(),new Retention(),new AbstractConcept(),new DataValue(),new  ActivityDescription(),new ContextDefinition()));
        ClassificationGroupByOperation classificationGroupByOperation = new ClassificationGroupByOperation(SubjectAreaTermRESTServices.SUBJECT_AREA_TERM_CLASSIFICATIONS,existing,requesting);
        assert(classificationGroupByOperation.getAddClassifications().isEmpty() == true);
        assert(classificationGroupByOperation.getUpdateClassifications().size() == 11);
        assert(classificationGroupByOperation.getRemoveClassifications().isEmpty() == true);
    }

    @Test
    void testUpdateValue(){
        Confidentiality existingConfidentiality = new Confidentiality();
        existingConfidentiality.setLevel(ConfidentialityLevel.Confidential);
        Confidentiality requestedConfidentiality = new Confidentiality();
        requestedConfidentiality.setLevel(ConfidentialityLevel.Restricted);
        Set<Classification> existing=new HashSet<>(Arrays.asList(existingConfidentiality));
        Set<Classification> requesting=new HashSet<>(Arrays.asList(requestedConfidentiality));

        ClassificationGroupByOperation classificationGroupByOperation = new ClassificationGroupByOperation(SubjectAreaTermRESTServices.SUBJECT_AREA_TERM_CLASSIFICATIONS,existing,requesting);
        assert(classificationGroupByOperation.getAddClassifications().size() == 0);
        assert(classificationGroupByOperation.getUpdateClassifications().size() == 1);
        Classification updatedClassification = (classificationGroupByOperation.getUpdateClassifications().get(0));
        Confidentiality updatedConfidentiality = (Confidentiality)updatedClassification;
        assert(updatedConfidentiality.getLevel().equals(ConfidentialityLevel.Restricted));
     }
        @Test
    void testRemove(){
        // taxonomy should not be removed
        Set<Classification> existing=new HashSet<>(Arrays.asList(new SpineObject(), new SpineAttribute(),new ObjectIdentifier(),new Confidentiality(),new Confidence(),
                new Criticality(),new Retention(),new AbstractConcept(),new DataValue(),new ContextDefinition(),new Taxonomy()));
        Set<Classification> requesting=new HashSet<>();;
        ClassificationGroupByOperation classificationGroupByOperation = new ClassificationGroupByOperation(SubjectAreaTermRESTServices.SUBJECT_AREA_TERM_CLASSIFICATIONS,existing,requesting);
        assert(classificationGroupByOperation.getAddClassifications().isEmpty() == true);
        assert(classificationGroupByOperation.getUpdateClassifications().isEmpty() == true);
        assert(classificationGroupByOperation.getRemoveClassifications().size() == 10);
    }
    @Test
    void testvaried(){
        Set<Classification> existing= new HashSet<>(Arrays.asList(new SpineObject(), new SpineAttribute(),new ObjectIdentifier(),new Confidentiality(),new Confidence(),
                new DataValue(),new ContextDefinition(),   //expect these 2 to be removed
                new Taxonomy()                             // expect to be ignored
        ));
        Set<Classification> requesting=new HashSet<>(Arrays.asList(new SpineObject(), new SpineAttribute(),new ObjectIdentifier(),new Confidentiality(),new Confidence(),
                new Criticality(),new Retention(),new AbstractConcept()      // expect these 3 to be added
        ));
        ClassificationGroupByOperation classificationGroupByOperation = new ClassificationGroupByOperation(SubjectAreaTermRESTServices.SUBJECT_AREA_TERM_CLASSIFICATIONS,existing,requesting);
        assert(classificationGroupByOperation.getAddClassifications().size() == 3);
        assert(classificationGroupByOperation.getUpdateClassifications().size() == 5);
        assert(classificationGroupByOperation.getRemoveClassifications().size() == 2);
    }


}
