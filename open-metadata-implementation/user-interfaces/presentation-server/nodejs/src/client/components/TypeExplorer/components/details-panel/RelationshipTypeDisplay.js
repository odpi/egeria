/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }           from "react";

import PropTypes                       from "prop-types";

import { TypesContext }                from "../../contexts/TypesContext";

import RelationshipPropertiesDisplay   from "./RelationshipPropertiesDisplay";

import RelationshipEntitiesDisplay     from "./RelationshipEntitiesDisplay";


import "./details-panel.scss";



export default function RelationshipTypeDisplay(props) {

  const typesContext = useContext(TypesContext);

  const typeName    = props.typeName;

  const relationshipExpl  = typesContext.getRelationshipType(typeName);


  return (
    <div className="type-details-container">
      <div className="type-details-item-bold">Relationship Type : {typeName}</div>
      <div className="type-details-item">{relationshipExpl.relationshipDef.description}</div>
      <div className="type-details-item">Attributes : { !relationshipExpl.relationshipDef.propertiesDefinition ? "none" :
        <RelationshipPropertiesDisplay expl={relationshipExpl} />}</div>
      <div className="type-details-item">Entities : { !relationshipExpl.relationshipDef.endDef1 && !relationshipExpl.relationshipDef.endDef2 ? "none" :
        <RelationshipEntitiesDisplay expl={relationshipExpl} />}</div>
    </div>
  );
}

RelationshipTypeDisplay.propTypes = {
  typeName: PropTypes.string
};
  