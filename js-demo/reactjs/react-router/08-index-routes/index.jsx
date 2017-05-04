import React from 'react';
import {render} from 'react-dom';
import {Router, Route, IndexRoute, hashHistory} from 'react-router';

import App from './modules/App.jsx';
import About from './modules/About.jsx';
import Repos from './modules/Repos.jsx';
import Repo from './modules/Repo.jsx';
import Home from './modules/Home.jsx';

render(
    <Router history={hashHistory}>
        <Router path="/" component={App}>
        
            <IndexRoute component={Home} />
        
            <Router path="/repos" component={Repos}></Router>
            
            <Router path="/repos/:username/:repoName" component={Repo}></Router>
            
            <Router path="/about" component={About}></Router>
        </Router>
    </Router>,
    document.getElementById('app')
);