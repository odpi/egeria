/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { createContext, useState } from "react";
export const GlossaryAuthorContext = createContext();
export const GlossaryAuthorContextConsumer = GlossaryAuthorContext.Consumer;
const GlossaryAuthorContextProvider = (props) => {
  const [url, setUrl] = useState();
  console.log(
    "GlossaryAuthorContext GlossaryAuthorContextProvider",
    GlossaryAuthorContextProvider
  );

  const setBaseURL = (url) => {
    setUrl(url);
  };
  const getGlossaryAuthorURL = () => {
    return url + "/glossary-author";
  };

  return (
    <GlossaryAuthorContext.Provider
      value={{
        setBaseURL,
        getGlossaryAuthorURL,
      }}
    >
      {props.children}
    </GlossaryAuthorContext.Provider>
  );
};
export default GlossaryAuthorContextProvider;
