var gulp = require('gulp');
var browserSync = require('browser-sync').create();
var reload = browserSync.reload;
var sass = require('gulp-sass');

var paths = {
	sass: ['/style/*.scss'],
	dist: './dist/'
};

gulp.task('server', ['sass'], function() {
	browserSync.init({
		server: {
			baseDir: paths.dist
		},
		ui: {
			port: 3009
		},
		port: 3008
	});
	gulp.watch(paths.sass, ['sass']);
});

gulp.task('sass', function() {
	gulp.src(paths.sass)
		.pipe(sass())
		.pipe(gulp.dest('css'))
		.pipe(reload({stream: true}));
});
