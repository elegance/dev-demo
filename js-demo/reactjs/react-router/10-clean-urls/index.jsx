import React from 'react';
import {render} from 'react-dom';
import {Router, Route, browserHistory} from 'react-router';

import App from './modules/App.jsx';
import About from './modules/About.jsx';
import Repos from './modules/Repos.jsx';
import Repo from './modules/Repo.jsx'

render(
    <Router history={browserHistory}>
        <Router path="/" component={App}>
            <Router path="/repos" component={Repos}></Router>
            
            <Router path="/repos/:username/:repoName" component={Repo}></Router>
            
            <Router path="/about" component={About}></Router>
        </Router>
    </Router>,
    document.getElementById('app')
);