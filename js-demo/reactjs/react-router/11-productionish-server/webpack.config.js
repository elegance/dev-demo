var webpack = require('webpack');

module.exports = {
    entry: './index.jsx',
    
    output: {
        path: 'public',
        filename: 'bundle.js',
        publicPath: '/'
    },
    
    module: {
        loaders: [
            {test: /\.js[x]?$/, exclude: /node_modules/, loader: 'babel-loader?presets[]=es2015&presets[]=react'},
            {test: /\.css$/, loader: 'style-loader!css-loader?modules'}
        ]
    },
    
    plugins: process.env.NODE_ENV === 'production' ? [
        new webapck.optimize.DedupePlugin(),
        new webpack.optimize.OccurrenceOrderPlugin(),
        new webpack.optimize.UglifyJsPlugin()
    ] : []
};