/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }        from "react";

import PropTypes                    from "prop-types";

import { FocusContext }             from "../../contexts/FocusContext";

import { TypesContext }             from "../../contexts/TypesContext";

import { RequestContext }           from "../../contexts/RequestContext";

import "./details-panel.scss";




export default function ClassificationEntitiesDisplay(props) {

  const explorer           = props.expl;

  const focusContext       = useContext(FocusContext);

  const typesContext       = useContext(TypesContext);

  const requestContext       = useContext(RequestContext);



  const entityLinkHandler = (evt) => {
    const typeName = evt.target.id;
    focusContext.typeSelected("Entity",typeName);
  };



  /*
   * Accept a TypeDefLink and do the deprecation checks and format the display of the
   * entity type name.
   */
  const formatEntity = (entityTypeDefLink) => {

    /*
     * If the classification has an entity type in which the status is
     * deprecated, then do not use typesContext.isTypeDeprecated to
     * look in tex.entities to see if the entity type has been
     * deprecated - if it has it will not be present. This display
     * will be based on the status of the classification's validEntityType
     * entry for the entity type.
     * Therefore the following is gleaned from the validEntityType's TypeDefLink
     * (not from the types context)
     */
    let entityTypeName = entityTypeDefLink.name;
    let entityTypeStatus = entityTypeDefLink.status;
    let entityTypeDeprecated = entityTypeStatus === "DEPRECATED_TYPEDEF";

    if (requestContext.deprecatedTypeOption || !entityTypeDeprecated) {

      /*
       * The entity is supposedly current and should be present in the Type Explorer.
       * This function could just assume it is present, but it is better to be sure
       * to avoid embedding a link to a type that is not in the explorer. If it is
       * missing the TypesContext will not know about it. If it is found, then it is
       * OK to check whether the TypesContext thinks it is deprecated and include a
       * link to the entity type. If deprecated types are allowed and the entity type
       * is deprecated it will be styled as such - i.e. [<name>]. If it is not found
       * (i.e. has been deprecated and the user has elected not to include deprecated
       * types) then include a text substitute for the entity type.
       */

      /*
       * Check the types context knows the entity type
       */
      let entityType = typesContext.getEntityType(entityTypeName);
      if (!entityType) {
        /*
         * Trouble. The types context does not have the type. No need to panic, just
         * provide the user with a clear visual indication that the server's type system
         * is not self consistent.
         */
        const entityEntry = (
          <div> Entity Type : NOT FOUND!! </div>
        );
        return entityEntry;
      }
      else {

        /*
         * The entity type is known to the types context
         * Display it including a visual indication of whether the TypesContext
         * has it marked as deprecated (i.e. from the type's own TypeDefStatus)
         */
        let entityTypeDisplayName;
        let deprecated = typesContext.isTypeDeprecated("Entity", entityTypeName);
        if (!deprecated) {
          entityTypeDisplayName = entityTypeName;
        }
        else {
          entityTypeDisplayName = "["+ entityTypeName +"]";
        }

        const entityEntry = (
          <div>
             <button className="linkable" id={entityTypeName} onClick={entityLinkHandler}> {entityTypeDisplayName} </button>
          </div>
        );
        return entityEntry;
      }
    }
    else {

      /*
       * The entity type status indicates the entity is not current and we are not showing deprecated types
       * Offer a text substitute for the entity
       */
      let entityTypeDisplayName = "["+ entityTypeName +"]";
      const entityEntry = (
        <div>
           Entity Type : {entityTypeDisplayName}
        </div>
      );
      return entityEntry;
    }
  }



  const expandEntities = (clsDef) => {

    /*
     * Parse the list of validEntityDefs and sort it by name
     */
    let validEntityNames = [];
    let validEntityDefsMap = {};

    const validEntityDefs = clsDef.validEntityDefs;
    if (validEntityDefs !== undefined) {
      validEntityDefs.forEach(validEntityDef => {
        validEntityDefsMap[validEntityDef.name] = validEntityDef;
        validEntityNames.push(validEntityDef.name);
      });
    }

    const validEntityNamesSorted = validEntityNames.sort();

    let entityList =  validEntityNamesSorted.map( (vename) => 
      <li className="details-sublist-item" key={vename}>  {formatEntity(validEntityDefsMap[vename])}  </li>
    );

    return entityList;
  };

  let ends;

    ends = (              
      <ul className="details-sublist">       
       {expandEntities(explorer.classificationDef)}          
      </ul>
      
    );
  

  return ends;
}

ClassificationEntitiesDisplay.propTypes = {
  expl: PropTypes.object 
};
