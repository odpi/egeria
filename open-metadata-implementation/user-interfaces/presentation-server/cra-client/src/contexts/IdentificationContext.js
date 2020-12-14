/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { createContext, useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";

export const IdentificationContext = createContext();
export const IdentificationContextConsumer = IdentificationContext.Consumer;

const IdentificationContextProvider = props => {
  const [userId, setUserId] = useState("");
  const [user, setUser] = useState({});
  // TODO this will change if the user changes the url - are there any implications to that?
  const [serverName] = useState(window.location.pathname.split("/")[1]);
  const [authenticated, setAuthenticated] = useState(false); //TODO do we need this?

  /**
   * Get the home url for the browser
   */
  const getBrowserURL = pageName => {
    const url = "/" + serverName + "/" + pageName;
    console.log("getBrowserURL" + url);
    return url;
  };
  /**
 * Get the url to use for rest calls 
 * @param {*} serviceName this is the viewservice name. 
 */
const getRestURL = (serviceName) => {
  const url =
    window.location.protocol +
    "//" +
    window.location.hostname +
    ":" +
    window.location.port +
    "/servers/" +
    serverName +
    "/" +
    serviceName +
    "/users/" +
    userId;
  return url;
};

const getUser = async () => {
  try {
    console.log("called getUser");
    const response = await axios.get('/user');
    console.log({response})
    if (response.status === 200) {
      setUserId(response.data.user.username);
      setUser(response.data.user);
      return response.data.user;
    } else {
      console.error("Error fetching user");
      console.dir(response);
      setUserId("");
      return "";
    }
  } catch (err) {
    console.error(err);
    setUserId("");
    return "";
  }
}

  return (
    <IdentificationContext.Provider
      value={{
        userId,
        setUserId,
        user,
        setUser,
        authenticated,
        setAuthenticated,
        serverName,
        getBrowserURL,
        getRestURL,
        getUser,
      }}
    >
      {props.children}
    </IdentificationContext.Provider>
  );
};
IdentificationContextProvider.propTypes = {
  children: PropTypes.node
};
export default IdentificationContextProvider;
// export default IdentificationContext;