import React from 'react';
import {render} from 'react-dom';
import {Router, Route, hashHistory} from 'react-router';

import App from './modules/App.jsx';
import About from './modules/About.jsx';
import Repos from './modules/Repos.jsx';

render(
    <Router history={hashHistory}>
        <Route path="/" component={App} />
        <Route path="/repos" component={Repos} />
        <Route path="/about" component={About} />
    </Router>,
    document.getElementById('app')
);