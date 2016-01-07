fis.match('*.html', {
	preprocessor: require('./fis-preprocessor-layout/index')
});

fis.match('layout/*.html', {
    release: false
});
