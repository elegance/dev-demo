import React from 'react';
import {render} from 'react-dom';
import {Router, Route, hashHistory} from 'react-router';

import App from './modules/App.jsx';
import About from './modules/About.jsx';
import Repos from './modules/Repos.jsx';

render(
    <Router history={hashHistory}>
        <Router path="/" component={App}>
            <Router path="/repos" component={Repos}></Router>
            <Router path="/about" component={About}></Router>
        </Router>
    </Router>,
    document.getElementById('app')
);