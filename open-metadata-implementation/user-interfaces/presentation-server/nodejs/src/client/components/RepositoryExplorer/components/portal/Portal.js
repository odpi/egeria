/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useEffect, useState }     from "react";

import ReactDOM                           from "react-dom";

import PropTypes                          from "prop-types";


import "./portal.scss";



export default function Portal(props) {


  const [newElement, setNewElement] = useState(null);
  

  /*
   * Emulate componentDidMount - to append the wrapper
   */
  const componentDidMount = () => {
    const portalRoot = props.anchorCB().current;
    let locElement = document.createElement("div");
    portalRoot.appendChild(locElement);
    setNewElement(locElement);

    return () => {
      // componentWillUnmount... to remove the wrapper element
      portalRoot.removeChild(locElement);
      setNewElement(null);
    }
  };
  useEffect (componentDidMount ,[]);

  let content = (
    <div className="portal-backdrop">
      <div className="portal-div">
        {props.children}
      </div>
    </div>
  );

  // Render the Portal's children in the wrapper element...  
  if (props.show) {
    if (newElement) {
      return (
        ReactDOM.createPortal(content, newElement)
      );
    }
  }
  return null;
}

Portal.propTypes = {
  children: PropTypes.node,
  show : PropTypes.bool,
  anchorCB: PropTypes.func.isRequired
};
