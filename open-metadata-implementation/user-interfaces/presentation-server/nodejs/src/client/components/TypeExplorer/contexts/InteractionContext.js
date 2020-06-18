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

 
  return (
    <InteractionContext.Provider
      value={{
        showPortal,
        hidePortal,
        getPortalAnchor,
        portalCancel,
        portalSubmit
      }}
    >      

    <div id="tex-portal" ref={portalAnchor}></div>
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

