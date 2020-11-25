/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useState, useRef } from "react";

import PropTypes                                  from "prop-types";

import Portal                                     from "../components/portal/Portal";


/*
 * The InteractionContext provides the Portal for all user interactions.
 * 
 
 */
export const InteractionContext         = createContext();

export const InteractionContextConsumer = InteractionContext.Consumer;

const InteractionContextProvider = (props) => {

  /*
   * To manage user input and confirmations (e.g. search inputs, search results, pre-traversal results, etc.)
   * the RepositoryExplorer component provides one portal, which can be summoned by any child component with
   * the content, parameters and callbacks the component needs.
   */


  const [portalVisible,  setPortalVisible]  = useState(false);
  const [submitCallback, setSubmitCallback] = useState(null);
  const [cancelCallback, setCancelCallback] = useState(null);
  const [portalContent,  setPortalContent]  = useState(null);

  const portalAnchor = useRef(null);

  const getPortalAnchor = () => {
    return portalAnchor;
  };

  const portalCancel = () => {
    setPortalVisible(false);
    cancelCallback();
  };

  const portalSubmit = (evt) => {
    setPortalVisible(false);
    submitCallback(evt);
  };

  const showPortal = (content, submitCB, cancelCB) => {
    setPortalContent(content);
    setSubmitCallback(submitCB);
    setCancelCallback(cancelCB);
    setPortalVisible(true);
  };

  const hidePortal = () => {
    setPortalVisible(false);
    setSubmitCallback(null);
    setCancelCallback(null);
    setPortalContent(null);
  };

  /*
   * General failure reporting utility
   *
   *
   * This function provides a common utility for reporting errors to the user.
   * For now these are posted as alerts. That could be modified.
   * The 'operation' parameter should be a phrase that describes the operation in a non-technical
   * manner that matches the context and concepts the user will be familiar with. For example,
   * use a phrase like "get types for server" rather than "loadTypes".
   * The second parameter is a json response object that has the fields from the associated
   * RexViewServiceException. The main fields to note are (with example values):
   *  relatedHTTPCode                 :  400,
   *  exceptionClassName              : 'org.odpi.openmetadata.viewservices.rex.api.ffdc.RexViewServiceException',
   *  actionDescription               : 'getTypeExplorer',
   *  exceptionErrorMessage           : 'The repository explorer view service operation getTypeExplorer found that platform for server Metadata_Server2 is not available',
   *  exceptionErrorMessageId         : 'OMVS-REPOSITORY-EXPLORER-400-006',
   *  exceptionErrorMessageParameters : [ 'getTypeExplorer', 'Metadata_Server2' ],
   *  exceptionSystemAction           : 'The system reported that the platform is not reachable using the provided URL.',
   *  exceptionUserAction             : 'Check the platform is running and check the repository explorer resource endpoint configuration for the server and its platform.'
   * The exceptionErrorMessageParameters will already have been substituted into the exceptionErrorMessage,
   * so there should be no need to perform any formatting; that is all done by the view service.
   *
   */
  const reportFailedOperation = (operation, json) => {
    if (json !== null) {
      const relatedHTTPCode = json.relatedHTTPCode;
      const exceptionMessage = json.exceptionErrorMessage;

      let message = "Could not "+operation+" (status : "+relatedHTTPCode+"). "+exceptionMessage;
      message = message + "\n\nSystem detail: " + json.exceptionSystemAction;
      message = message + "\n\nRecommended user action: " + json.exceptionUserAction;
      alert(message);

    }
    else {
      alert("Attempt to "+operation+" did not get a JSON response from the view server");
    }
  }




 
  return (
    <InteractionContext.Provider
      value={{
        showPortal,
        hidePortal,
        getPortalAnchor,
        portalCancel,
        portalSubmit,
        reportFailedOperation
      }}
    >      

    <div id="rex-portal" ref={portalAnchor}></div>
      <Portal show={portalVisible} anchorCB={getPortalAnchor} cancelCallback={portalCancel} submitCallback={portalSubmit}>
          {portalContent}
      </Portal>
     {props.children}
    </InteractionContext.Provider>
  );
};

InteractionContextProvider.propTypes = {
  children: PropTypes.node  
};

export default InteractionContextProvider;

