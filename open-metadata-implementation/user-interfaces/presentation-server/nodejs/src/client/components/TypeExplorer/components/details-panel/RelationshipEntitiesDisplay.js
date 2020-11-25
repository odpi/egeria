/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }                   from "react";

import PropTypes                               from "prop-types";

import { FocusContext }                        from "../../contexts/FocusContext";

import { TypesContext }                        from "../../contexts/TypesContext";

import { RequestContext }                    from "../../contexts/RequestContext";

import "./details-panel.scss";



export default function RelationshipEntitiesDisplay(props) {

  const focusContext    = useContext(FocusContext);

  const typesContext    = useContext(TypesContext);

  const requestContext    = useContext(RequestContext);

  const explorer        = props.expl;

  
  const entityLinkHandler = (evt) => {
    const typeName = evt.target.id;
    focusContext.typeSelected("Entity",typeName);
  };

  const formatEnd = (endDef) => {

    /*
     * If the relationship end has a type in which the status is
     * deprecated, then do not use typesContext.isTypeDeprecated to
     * look in tex.entities to see if the entity type has been
     * deprecated - if it has it will not be present. This display
     * will be based on the status of the relationship end.
     * All of the following are gleaned from the end (not the types context)
     */
    let endTypeName = endDef.entityType.name;
    let endypeStatus = endDef.entityType.status;
    let endTypeDeprecated = endypeStatus === "DEPRECATED_TYPEDEF";

    if (requestContext.deprecatedTypeOption || !endTypeDeprecated) {

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
      let entityType = typesContext.getEntityType(endTypeName);
      if (!entityType) {
        /*
         * Trouble. The types context does not have the type. No need to panic, just
         * provide the user with a clear visual indication that the server's type system
         * is not self consistent.
         */
        const endEntry = (
          <ul className="details-sublist">
            <li className="details-sublist-item"> Entity Type : NOT FOUND!! </li>
            <li className="details-sublist-item"> Cardinality : {endDef.attributeCardinality} </li>
            <li className="details-sublist-item"> Attribute Name : {endDef.attributeName} </li>
          </ul>
        );
        return endEntry;
      }
      else {

        /*
         * The entity type is known to the types context
         * Display it including a visual indication of whether the TypesContext
         * has it marked as deprecated (i.e. from the type's own TypeDefStatus)
         */
        let entityTypeDisplayName;
        let deprecated = typesContext.isTypeDeprecated("Entity", endTypeName);
        if (!deprecated) {
          entityTypeDisplayName = endTypeName;
        }
        else {
          entityTypeDisplayName = "["+ endTypeName +"]";
        }

        const endEntry = (
          <ul className="details-sublist">
            <li className="details-sublist-item"> Entity Type : <button className="linkable"
                id={endTypeName} onClick={entityLinkHandler}>
                {entityTypeDisplayName}
              </button>
            </li>
            <li className="details-sublist-item"> Cardinality : {endDef.attributeCardinality} </li>
            <li className="details-sublist-item"> Attribute Name : {endDef.attributeName} </li>
          </ul>
        );
        return endEntry;
      }
    }
    else {

      /*
       * The end type status indicates the entity is not current and we are not showing deprecated types
       * Offer a text substitute for the entity
       */
      let entityTypeDisplayName = "["+ endTypeName +"]";
      const endEntry = (
        <ul className="details-sublist">
          <li className="details-sublist-item"> Entity Type : {entityTypeDisplayName} </li>
          <li className="details-sublist-item"> Cardinality : {endDef.attributeCardinality} </li>
          <li className="details-sublist-item"> Attribute Name : {endDef.attributeName} </li>
        </ul>
      );
      return endEntry;
    }
  };

  

  let ends;

  const expandEnds = (relDef) => {
    let endsList = (
    <div className="details-sub-container">
      <li className="end-sublist" key="end1"> 
        Entity @ end1: {formatEnd(relDef.endDef1)}                  
      </li>
      <li className="end-sublist" key="end2"> 
        Entity @ end2: {formatEnd(relDef.endDef2)}                  
      </li>
    </div>
    );

    return endsList;
  };

 
    ends = (              
      <ul className="details-sublist">       
       {expandEnds(explorer.relationshipDef)}          
      </ul>
      
    );
  

  return ends;
}

RelationshipEntitiesDisplay.propTypes = {
  expl: PropTypes.object 
};
