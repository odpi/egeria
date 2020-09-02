/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                          from "react";

import PropTypes                      from "prop-types";

import InstanceStatusDisplay          from "./InstanceStatusDisplay";

import InstancePropertiesDisplay      from "./InstancePropertiesDisplay";

import InstanceClassificationsDisplay from "./InstanceClassificationsDisplay";

import "./details-panel.scss";


export default function EntityDisplay(props) {

    const expEntity    = props.expEntity;

    const entity       = expEntity.entityDetail;
    const entityDigest = expEntity.entityDigest;

    const label        = entityDigest.label;
    const gen          = entityDigest.gen;

    return (
      <div className="instance-details-container">
        <div className="instance-details-item">Entity : {label}</div>
        <div className="instance-details-item">Added in gen : {gen}</div>        
        <div className="instance-details-item">Type : {entity.type.typeDefName}</div>       
        <div className="instance-details-item">Version : {entity.version}</div>
        <div className="instance-details-item">Status : <InstanceStatusDisplay inst={entity} /></div>
        <div className="instance-details-item">Properties : { !entity.properties ? "empty" :
              <InstancePropertiesDisplay properties={entity.properties} />}</div>     
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

      </div>
    );
  }

  EntityDisplay.propTypes = {
    expEntity: PropTypes.object
  };
  
