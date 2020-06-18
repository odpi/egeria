/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }           from "react";

import PropTypes                       from "prop-types";

import { TypesContext }                from "../../contexts/TypesContext";

import ClassificationPropertiesDisplay   from "./ClassificationPropertiesDisplay";

import ClassificationEntitiesDisplay     from "./ClassificationEntitiesDisplay";


import "./details-panel.scss";



export default function ClassificationTypeDisplay(props) {

  const typesContext = useContext(TypesContext);

  const typeName    = props.typeName;

  const classificationExpl  = typesContext.getClassificationType(typeName);


  return (
    <div className="instance-details-container">
      <div className="instance-details-item-bold">Classification Type : {typeName}</div>
      <div className="instance-details-item">{classificationExpl.classificationDef.description}</div>
      <div className="instance-details-item">Attributes : { !classificationExpl.classificationDef.propertiesDefinition ? "none" :
        <ClassificationPropertiesDisplay expl={classificationExpl} />}</div>
      <div className="instance-details-item">Valid entity types : { !classificationExpl.classificationDef.validEntityDefs && !classificationExpl.classificationDef.validEntityDefs.length > 0 ? "none" :
        <ClassificationEntitiesDisplay expl={classificationExpl} />}</div>
    </div>
  );
}

ClassificationTypeDisplay.propTypes = {
  typeName: PropTypes.string
};
  