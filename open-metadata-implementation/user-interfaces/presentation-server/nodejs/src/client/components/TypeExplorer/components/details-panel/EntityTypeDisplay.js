/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }           from "react";

import PropTypes                       from "prop-types";

import { TypesContext }                from "../../contexts/TypesContext";

import TypePropertiesDisplay           from "./TypePropertiesDisplay";

import EntityRelationshipsDisplay      from "./EntityRelationshipsDisplay";

import EntityClassificationsDisplay   from "./EntityClassificationsDisplay";

import "./details-panel.scss";



export default function EntityTypeDisplay(props) {

    const typesContext = useContext(TypesContext);

    const typeName    = props.typeName;

    const entityExpl  = typesContext.getEntityType(typeName);


    return (
      <div className="instance-details-container">
        <div className="instance-details-item">Entity Type : {typeName}</div>
        <div className="instance-details-item">{entityExpl.entityDef.description}</div>        
        <div className="instance-details-item">Attributes : { !entityExpl.inheritedAttributes && !entityExpl.entityDef.propertiesDefinition ? "empty" :
          <TypePropertiesDisplay expl={entityExpl} />}</div>     
        <div className="instance-details-item">Relationships : { !entityExpl.inheritedRelationshipNames && !entityExpl.relationshipNames ? "empty" :
          <EntityRelationshipsDisplay expl={entityExpl} />}</div>     
        <div className="instance-details-item">Classifications : { !entityExpl.inheritedClassificationNames && !entityExpl.classificationNames ? "empty" :
          <EntityClassificationsDisplay expl={entityExpl} />}</div>   
      
      
       
      </div>
    );
  }

  EntityTypeDisplay.propTypes = {
    typeName: PropTypes.string
  };
  
/*

 <div className="instance-details-item">Classifications : { !entity.classifications ? "empty" :
              <InstanceClassificationsDisplay classifications={entity.classifications} />}</div>  
        <div className="instance-details-item">GUID : {entity.guid}</div>
        <div className="instance-details-item">Home Repository : 
          <ul className="details-sublist">
            <li className="details-sublist-item">metadataCollectionName : {entity.metadataCollectionName}</li>
            <li className="details-sublist-item">metadataCollectionId : {entity.metadataCollectionId}</li>
          </ul>
        </div>
        <div className="instance-details-item">OMRS Control Properties :
          <ul className="details-sublist">
            <li className="details-sublist-item">createdBy : {entity.createdBy}</li>
            <li className="details-sublist-item">createTime : {entity.createTime}</li>
            <li className="details-sublist-item">updatedBy : {entity.updatedBy ? entity.updatedBy : "empty"}</li>
            <li className="details-sublist-item">updateTime : {entity.updateTime ? entity.updateTime : "empty"}</li>
            <li className="details-sublist-item">maintainedBy : { !entity.maintainedBy ? "empty" :
              <ul className="details-sublist">
                {entity.maintainedBy.sort().map( (mtr) => <li className="details-sublist-item" key={mtr}> {mtr}</li> )}      
              </ul>
              }              
            </li>
            <li className="details-sublist-item">instanceLicense : {entity.instanceLicense ? entity.instanceLicense : "empty"}</li>
            <li className="details-sublist-item">instanceProvenanceType : {entity.instanceProvenanceType ? entity.instanceProvenanceType : "empty"}</li> 
            <li className="details-sublist-item">replicatedBy : {entity.replicatedBy ? entity.replicatedBy : "empty"}</li>
          </ul>

        </div>




*/