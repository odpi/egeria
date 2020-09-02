/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                          from "react";

import PropTypes                      from "prop-types";

import InstanceStatusDisplay          from "./InstanceStatusDisplay";

import InstancePropertiesDisplay      from "./InstancePropertiesDisplay";

import "./details-panel.scss";


export default function EntityProxyDisplay(props) {

  const entityProxy  = props.entityProxy;

  return (
    <div className="details-sub-group">
      <div className="details-sub-item">GUID : {entityProxy.guid}</div>
      <div className="details-sub-item">Type : {entityProxy.type.typeDefName}</div>       
      <div className="details-sub-item">Version : {entityProxy.version}</div>
      <div className="details-sub-item">Status : <InstanceStatusDisplay inst={entityProxy} /></div>
      <div className="details-sub-item">Properties : { !entityProxy.uniqueProperties ? "empty" :
        <InstancePropertiesDisplay properties={entityProxy.uniqueProperties} />}</div>            
      <div className="details-sub-item">Home Repository : 
        <ul className="details-sublist">
          <li className="details-sublist-item">metadataCollectionName : {entityProxy.metadataCollectionName}</li>
          <li className="details-sublist-item">metadataCollectionId : {entityProxy.metadataCollectionId}</li>
        </ul>
      </div>
    </div>
  );
}

 
EntityProxyDisplay.propTypes = {
  entityProxy: PropTypes.object
};
  
