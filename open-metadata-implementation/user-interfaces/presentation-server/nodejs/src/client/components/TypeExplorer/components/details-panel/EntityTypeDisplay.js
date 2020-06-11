/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }           from "react";

import PropTypes                       from "prop-types";

import { TypesContext }                from "../../contexts/TypesContext";

import EntityPropertiesDisplay           from "./EntityPropertiesDisplay";

import EntityRelationshipsDisplay      from "./EntityRelationshipsDisplay";

import EntityClassificationsDisplay   from "./EntityClassificationsDisplay";

import "./details-panel.scss";



export default function EntityTypeDisplay(props) {

  const typesContext = useContext(TypesContext);

  const typeName    = props.typeName;

  const entityExpl  = typesContext.getEntityType(typeName);


  return (
    <div className="instance-details-container">
      <div className="instance-details-item-bold">Entity Type : {typeName}</div>
      <div className="instance-details-item">{entityExpl.entityDef.description}</div>        
      <div className="instance-details-item">Attributes : { !entityExpl.inheritedAttributes && !entityExpl.entityDef.propertiesDefinition ? "none" :
        <EntityPropertiesDisplay expl={entityExpl} />}</div>     
      <div className="instance-details-item">Relationships : { !entityExpl.inheritedRelationshipNames && !entityExpl.relationshipNames ? "none" :
        <EntityRelationshipsDisplay expl={entityExpl} />}</div>     
      <div className="instance-details-item">Classifications : { !entityExpl.inheritedClassificationNames && !entityExpl.classificationNames ? "none" :
        <EntityClassificationsDisplay expl={entityExpl} />}</div>
    </div>
  );
}

EntityTypeDisplay.propTypes = {
  typeName: PropTypes.string
};
