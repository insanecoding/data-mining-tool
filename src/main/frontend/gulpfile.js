let gulp = require('gulp');
let addsrc = require('gulp-add-src');

gulp.task('copy-resources', function () {
    return gulp.src('build/static/**/*.*', {base: 'build'})
        .pipe(gulp.dest('../resources/'))
        .pipe(addsrc('build/*.*'))
        .pipe(gulp.dest('../resources/'));
});

gulp.task('default', [], function() {
    console.log("Default");
});