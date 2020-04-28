/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;

/**
 * This is a factory that is passed in the classname of a Line and the Line itself. It returns the appropriate
 * SubjectAreaOMASAPIResponse for use as a rest call response. The Line is embedded in these responses.
 */
public class ResponseFactory
{
    /**
     * Get a response from the factory.
     * @param bundleName classname
     * @param line line object
     * @return SubjectAreaOMASAPIResponse rest response
     */
   public SubjectAreaOMASAPIResponse getInstance(String bundleName, Line line) {
       SubjectAreaOMASAPIResponse response = null;
       if (bundleName.equals(Synonym.class.getName())){
          response = new SynonymRelationshipResponse((Synonym)line);

       } else if (bundleName.equals(Antonym.class.getName())){
           response = new AntonymRelationshipResponse((Antonym) line);

       } else  if (bundleName.equals(Categorization.class.getName())){
           response = new TermCategorizationRelationshipResponse((Categorization) line);
       } else if (bundleName.equals(Isa.class.getName())){
           response = new TermISARelationshipResponse((Isa) line);

       } else if (bundleName.equals(PreferredTerm.class.getName())){
           response = new PreferredTermRelationshipResponse((PreferredTerm) line);

       } else if (bundleName.equals(RelatedTerm.class.getName())){
           response = new RelatedTermResponse((RelatedTerm) line);

       } else if (bundleName.equals(ReplacementTerm.class.getName())){
           response = new ReplacementRelationshipResponse((ReplacementTerm) line);

       } else if (bundleName.equals(TypedBy.class.getName())){
           response = new TermTYPEDBYRelationshipResponse((TypedBy) line);

       } else if (bundleName.equals(SemanticAssignment.class.getName())){
           response = new SemanticAssignementRelationshipResponse((SemanticAssignment) line);

       } else if (bundleName.equals(Categorization.class.getName())){
           response = new TermCategorizationRelationshipResponse((Categorization) line);

       } else if (bundleName.equals(Hasa.class.getName())){
           response = new TermHASARelationshipResponse((Hasa) line);

       } else if (bundleName.equals(Isa.class.getName())){
           response = new TermISARelationshipResponse((Isa) line);

       } else if (bundleName.equals(IsaTypeOf.class.getName())){
           response = new TermISATYPEOFRelationshipResponse((IsaTypeOf) line);

       }    else if (bundleName.equals(Translation.class.getName()))
       {
           response = new TranslationRelationshipResponse((Translation) line);

       } else if (bundleName.equals(UsedInContext.class.getName()))
       {
           response = new UsedInContextRelationshipResponse((UsedInContext) line);

       } else if (bundleName.equals(ValidValue.class.getName()))
       {
           response = new ValidValueRelationshipResponse((ValidValue) line);

       } else if (bundleName.equals(TermAnchor.class.getName()))
       {
        response = new TermAnchorRelationshipResponse((TermAnchor) line);

       } else if (bundleName.equals(CategoryAnchor.class.getName()))
       {
           response = new CategoryAnchorRelationshipResponse((CategoryAnchor) line);

       } else if (bundleName.equals(ProjectScope.class.getName()))
       {
           response = new ProjectScopeRelationshipResponse((ProjectScope) line);
       }
       return response;
   }
}
