/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;

/**
 *
 */
public class ResponseFactory
{
   public SubjectAreaOMASAPIResponse getInstance(String bundleName, Line line) {
       SubjectAreaOMASAPIResponse response = null;
       if (bundleName.equals(Synonym.class.getName())){
          response = new SynonymRelationshipResponse((Synonym)line);

       } else if (bundleName.equals(Antonym.class.getName())){
           response = new AntonymRelationshipResponse((Antonym) line);

       } else  if (bundleName.equals(TermCategorizationRelationship.class.getName())){
           response = new TermCategorizationRelationshipResponse((TermCategorizationRelationship) line);
       } else if (bundleName.equals(ISARelationship.class.getName())){
           response = new TermISARelationshipResponse((ISARelationship) line);

       } else if (bundleName.equals(PreferredTerm.class.getName())){
           response = new PreferredTermRelationshipResponse((PreferredTerm) line);

       } else if (bundleName.equals(RelatedTerm.class.getName())){
           response = new RelatedTermResponse((RelatedTerm) line);

       } else if (bundleName.equals(ReplacementTerm.class.getName())){
           response = new ReplacementRelationshipResponse((ReplacementTerm) line);

       } else if (bundleName.equals(TermTYPEDBYRelationship.class.getName())){
           response = new TermTYPEDBYRelationshipResponse((TermTYPEDBYRelationship) line);

       } else if (bundleName.equals(SemanticAssignment.class.getName())){
           response = new SemanticAssignementRelationshipResponse((SemanticAssignment) line);

       } else if (bundleName.equals(TermCategorizationRelationship.class.getName())){
           response = new TermCategorizationRelationshipResponse((TermCategorizationRelationship) line);

       } else if (bundleName.equals(TermHASARelationship.class.getName())){
           response = new TermHASARelationshipResponse((TermHASARelationship) line);

       } else if (bundleName.equals(ISARelationship.class.getName())){
           response = new TermISARelationshipResponse((ISARelationship) line);

       } else if (bundleName.equals(TermISATypeOFRelationship.class.getName())){
           response = new TermISATYPEOFRelationshipResponse((TermISATypeOFRelationship) line);

       }    else if (bundleName.equals(Translation.class.getName()))
       {
           response = new TranslationRelationshipResponse((Translation) line);

       } else if (bundleName.equals(UsedInContext.class.getName()))
       {
           response = new UsedInContextRelationshipResponse((UsedInContext) line);

       } else if (bundleName.equals(ValidValue.class.getName()))
       {
           response = new ValidValueRelationshipResponse((ValidValue) line);

       }
       return response;
   }
}
