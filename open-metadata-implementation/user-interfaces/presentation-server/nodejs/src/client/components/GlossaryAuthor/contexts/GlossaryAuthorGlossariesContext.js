/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { createContext, useState } from "react";
import PropTypes from "prop-types";
export const GlossaryAuthorGlossariesContext = createContext();
export const GlossaryAuthorGlossariesContextConsumer =
  GlossaryAuthorGlossariesContext.Consumer;
const GlossaryAuthorGlossariesContextProvider = (props) => {
  const [baseGlossariesURL, setBaseGlossariesURL] = useState();
  console.log(
    "GlossaryAuthorGlossariesContext GlossaryAuthorGlossariesContextProvider",
    GlossaryAuthorGlossariesContextProvider
  );

  const TaskNames = {
    ADD_GLOSSARY: Symbol("add-glossary"),
    EDIT_GLOSSARY: Symbol("edit-glossary"),
    GLOSSARY_CHILDREN: Symbol("children"),
  };
  Object.freeze(TaskNames);

  const setGlossariesURL = (url) => {
    setBaseGlossariesURL(url);
  };
  const getGlossariesURL = () => {
    return baseGlossariesURL;
  };
  const getAddGlossariesURL = () => {
    return baseGlossariesURL + "/" + TaskNames.ADD_GLOSSARY.toString();
  };
  const getEditGlossariesURL = (name) => {
    return (
      baseGlossariesURL + "/" + name + "/" + TaskNames.EDIT_GLOSSARY.toString()
    );
  };
  const getGlossaryChildren = (name) => {
    return (
      baseGlossariesURL +
      "/" +
      name +
      "/" +
      TaskNames.GLOSSARY_CHILDREN.toString()
    );
  };

  return (
    <GlossaryAuthorGlossariesContext.Provider
      value={{
        setGlossariesURL,
        getGlossariesURL,
        getAddGlossariesURL,
        getEditGlossariesURL,
        getGlossaryChildren,
      }}
    >
      {props.children}
    </GlossaryAuthorGlossariesContext.Provider>
  );
};
export default GlossaryAuthorGlossariesContextProvider;
