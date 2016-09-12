var gulp = require('gulp');
var sass = require('gulp-sass');
var browserSync = require('browser-sync');
var argv = require('yargs').argv;

var paths = {
    dist: './dist',
    html: ['src/**/*.html'],
    sass: ['src/**/*.scss']
};

gulp.task('default', () => {
    if (argv.s) {
        gulp.start('server');
    } 

    if (argv.w) {
        gulp.start('watch');
    } else {
        gulp.start('release');
    }
});
    
gulp.task('server', () => {
    argv.p = argv.p || 8888;
    
    browserSync.init({
        server: {
            baseDir: paths.dist
        },
        ui: {
            port: argv.p + 1,
            weinre: {
                port: argv.p + 2
            }
        },
        port: argv.p,
        stratPath: '/'
    });
});

gulp.task('build:html', () => {
    gulp.src(paths.html)
        .pipe(gulp.dest(paths.dist))
        .pipe(browserSync.reload({stream: true}));
});

gulp.task('build:sass', () => {
    gulp.src(paths.sass)
        .pipe(sass())
        .pipe(gulp.dest(paths.dist))
        .pipe(browserSync.reload({stream: true}));
});

gulp.task('release', ['build:html', 'build:sass']);

gulp.task('watch', ['release'], () => {
    gulp.watch(paths.sass, ['build:sass']);
    gulp.watch(paths.html, ['build:html']);
});