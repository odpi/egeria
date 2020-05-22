import * as React from 'react';
import Cookies from './Cookies';
var CookiesContext = React.createContext(new Cookies());
export var Provider = CookiesContext.Provider, Consumer = CookiesContext.Consumer;
export default CookiesContext;
