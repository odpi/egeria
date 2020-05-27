/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                     from "react";

import PropTypes                 from "prop-types";

import InstanceStatusDisplay     from "./InstanceStatusDisplay";

import InstancePropertiesDisplay from "./InstancePropertiesDisplay";

import "./details-panel.scss";



export default function RelationshipDisplay(props) {

    const expRelationship    = props.expRelationship;
    //const serverName         = expRelationship.serverName;
    const relationship       = expRelationship.relationship;
    const relationshipDigest = expRelationship.relationshipDigest;
    const label              = relationshipDigest.label;
    const gen                = relationshipDigest.gen;
    
    return (
      <div className="instance-details-container">
        <div className="instance-details-item">Relationship : {label}</div>
        <div className="instance-details-item">Added in gen : {gen}</div>        
        <div className="instance-details-item">Type : {relationship.type.typeDefName}</div>       
        <div className="instance-details-item">Version : {relationship.version}</div>
        <div className="instance-details-item">Status : <InstanceStatusDisplay inst={relationship} /></div>
        <div className="instance-details-item">Properties : { !relationship.properties ? "empty" :
              <InstancePropertiesDisplay properties={relationship.properties} />}</div>         
        <div className="instance-details-item">GUID : {relationship.guid}</div>
        <div className="instance-details-item">Home Repository : 
          <ul className="details-sublist">
            <li className="details-sublist-item">metadataCollectionName : {relationship.metadataCollectionName}</li>
            <li className="details-sublist-item">metadataCollectionId : {relationship.metadataCollectionId}</li>
          </ul>
        </div>
        <div className="instance-details-item">OMRS Control Properties :
          <ul className="details-sublist">
            <li className="details-sublist-item">createdBy : {relationship.createdBy}</li>
            <li className="details-sublist-item">createTime : {relationship.createTime}</li>
            <li className="details-sublist-item">updatedBy : {relationship.updatedBy ? relationship.updatedBy : "empty"}</li>
            <li className="details-sublist-item">updateTime : {relationship.updateTime ? relationship.updateTime : "empty"}</li>
            <li className="details-sublist-item">maintainedBy  { !relationship.maintainedBy ? "empty" :
              <ul className="details-sublist">
                {relationship.maintainedBy.sort().map( (mtr) => <li className="details-sublist-item" key={mtr}> {mtr}</li> )}      
              </ul>
              }              
            </li>         
            <li className="details-sublist-item">instanceLicense : {relationship.instanceLicense ? relationship.instanceLicense : "empty"}</li>
            <li className="details-sublist-item">instanceProvenanceType : {relationship.instanceProvenanceType ? relationship.instanceProvenanceType : "empty"}</li> 
            <li className="details-sublist-item">replicatedBy : {relationship.replicatedBy ? relationship.replicatedBy : "empty"}</li>
          </ul>
        </div>
      </div>
    );
  }

  // TODO - add display of ends

  RelationshipDisplay.propTypes = {
    expRelationship: PropTypes.object
  };
  


  /* TODO - remove when happy - duplicate for entity display
      <ul className="details-sublist">
                {relationship.maintainedBy ? relationship.maintainedBy.sort().map( (mtr) => <li key={mtr}> {mtr}</li> ) : <li>list is empty</li>}       
              </ul>
            </li>
            */