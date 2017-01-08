#Data Mining app


#How to run: 
###First run:

`gradlew runMe` - first launch. The app will fetch all the dependencies: frontend and backend (may take a while)

###Develop:

`gradlew bootRun` - the application starts at `localhost:8080` (hot reloading also enabled). 
Tip: if you're using Intellij Idea, disable safe write in order to see changes immediately  

`gradlew updateFrontend` - delete previous frontend assets, build current frontend production and copy into server folder 
 
`cd src/main/frontend npm start` - starts frontend development server at `localhost:3000` 
with proxying to `localhost:8080`. This way we don't need to enable CORS

We've got two profiles: `dev` and `prod`

* `dev` works with h2 database (visit: `http://localhost:8080/h2-console/` to see database console). Note that 
`jdbc:h2:mem:testdb` is the url and `sa` is the username)
* `prod` uses PostgreSQL 9.4 database

`gradlew clean updateFrontend build` to make project (tests will be run as well)