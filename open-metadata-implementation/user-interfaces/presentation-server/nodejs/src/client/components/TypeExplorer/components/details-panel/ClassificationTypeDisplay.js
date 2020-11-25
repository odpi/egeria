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

  if (classificationExpl) {

    return (
      <div className="instance-details-container">
        <div className="instance-details-item-bold">Classification Type : {typeName}</div>
        <div className="instance-details-item">{classificationExpl.classificationDef.description}</div>
        <div className="type-details-item">Type Status : {classificationExpl.classificationDef.status}</div>
        <div className="instance-details-item">Attributes : { !classificationExpl.classificationDef.propertiesDefinition ? "none" :
          <ClassificationPropertiesDisplay expl={classificationExpl} />}</div>
        <div className="instance-details-item">Valid entity types : { !classificationExpl.classificationDef.validEntityDefs && !classificationExpl.classificationDef.validEntityDefs.length > 0 ? "none" :
          <ClassificationEntitiesDisplay expl={classificationExpl} />}</div>
      </div>
    );
  }
  else {
    return (
      <div className="type-details-container">
        <div className="type-details-item-bold">Classification type {typeName} not in server's types</div>
      </div>
    );
  }
}

ClassificationTypeDisplay.propTypes = {
  typeName: PropTypes.string
};
  