module.exports = {
    entry: './index.jsx',
    
    output: {
        filename: 'bundle.js',
        publicPath: ''
    },
    
    module: {
        loaders: [
            {test: /\.js[x]?$/, exclude: /node_modules/, loader: 'babel-loader?presets[]=es2015&presets[]=react'},
            {test: /\.css$/, loader: 'style-loader!css-loader?modules'}
        ]
    }
};