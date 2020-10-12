/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * If not logged in redirect to the login screen otehreise continue to process the request by calling the next in the middleware chain.
 * @param {*} req request
 * @param {*} res response
 * @param {*} next function of the next in the middleware chain 
 */
const loggedIn = (req, res, next) => {

  if (req.user) {
    next();
  } else {
    res.redirect("/" + req.query.serverName + "/login");
  }

}

module.exports = loggedIn;