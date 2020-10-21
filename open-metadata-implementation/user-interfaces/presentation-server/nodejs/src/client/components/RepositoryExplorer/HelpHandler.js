/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "./contexts/InteractionContext";

import ReactMarkdown                          from 'react-markdown';

import RexInterfaceImage                     from './RexInterface.png';

import SearchResultsImage                   from './SearchResults.png';

import "./rex.scss";

const imageRefs = {
    image1 : RexInterfaceImage,
    image2 : SearchResultsImage
};


export default function HelpHandler(props) {

  const interactionContext    = useContext(InteractionContext);

 
  const help              = props.help;

  const cancelCallback = () => {
    interactionContext.hidePortal();
    props.onCancel();
  }

  const submitCallback = () => {
    interactionContext.hidePortal();
    props.onSubmit();
  }

  const transformImageURI = (uri) => {
    return imageRefs[uri];
  }

  const triggerPortal = () => {

    let dialogDisplay;

    if (!help.markdown) {

      /* 
       * There is nothing to display... 
       */
     
      dialogDisplay = (
      
      <div className="dialog-text">
            
        <p>
        There is no help information to display.
        </p>  
  
        <div className="dismiss-button-container">                     
          <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
          <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
        </div>
              
      </div>
        
      );
    }
    
    else {

      /* 
       * There is readme information to display... 
       */

      let resultsDisplay = (         

        <div>
          <ReactMarkdown source={help.markdown} transformImageUri={transformImageURI}/>
        </div>
      );


      dialogDisplay = (
      
        <div className="dialog-text">
          
          <p  className="dialog-text">
          Help text for Egeria Repository Explorer (Rex) user interface:
          </p>    
   
          <hr></hr>
               
          <div className="history-results-area">                        
            {resultsDisplay}
          </div>

          <div className="dismiss-button-container">                     
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
            <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
          </div>
            
        </div>
      
      );
    }

    interactionContext.showPortal(dialogDisplay);
  };


  /*
   * Emulate componentDidMount - to append the wrapper element
   */
  const componentDidMount = () => {    
    if (props.status === "complete") {      
      triggerPortal();
    }   
  };
  useEffect (componentDidMount ,[ props.status, props.history ]);
  
  /*
   * Render nothing - this component is invisible but controls what is displayed by the portal
   */
  return null;

}

HelpHandler.propTypes = {  
  status               : PropTypes.string,
  onCancel             : PropTypes.func.isRequired, 
  onSubmit             : PropTypes.func.isRequired, 
  help                 : PropTypes.object
   
};
