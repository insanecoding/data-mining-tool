let gulp = require('gulp');
let change = require('gulp-change');
let gulpif = require('gulp-if');
let addsrc = require('gulp-add-src');
let del = require('del');

gulp.task('clean', function () {
    return del(['../resources/static/**/*.*']);
});

function performChange(content) {
    // /static/
    content = content.replace(/\/static\//g, '');
    // static\\
    content = content.replace(/static\\/g, '');
    // static/
    content = content.replace(/static\//g, '');
    return content;
}

gulp.task('change', function() {
    return gulp.src('build/static/**/*.*', {base: 'build/static'})
        .pipe(gulpif("!*.js", change(performChange)))
        .pipe(gulp.dest('../resources/static/'))
        .pipe(addsrc('build/*.*'))
        .pipe(gulpif("!*.js", change(performChange)))
        .pipe(gulp.dest('../resources/static/'));
});

gulp.task('move', function () {
    return gulp.src('build/static/**/*.*', {base: 'build'})
        .pipe(gulp.dest('../resources/'))
        .pipe(addsrc('build/*.*'))
        .pipe(gulp.dest('../resources/'));
});


gulp.task('default', [], function() {
    console.log("Default");
});