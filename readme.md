# Data Mining app

### Information:

The repository contains software infrastructure for data mining experiments with website classification
at COMSEC laboratory (SPIIRAS Institute).

This fullstack application helps to: 
* import categorized websites (URLBlacklist and Shalla Security Blacklist are now used),
* download their HTML contents, 
* extract features (all text from page, text from specific tags, ngrams and HTML tags' statistics),
* create and prepare experiments for RapidMiner software (all the parameters are defined via web interface)
* create schemes of RapidMiner workflow,
* execute the schemes,
* generate .xlsx report with classification results 

### Tech Stack:
* react + redux + react-router-redux at the frontend
* create-react-app was used as boilerplate (zero configuration, webpack and babel for free, ES6+)
* material ui for styling
* react-grid-system for adaptive grid (looks well on mobile devices)
* spring boot + hibernate + lombok at the backend
* apache-poi to generate excel reports
* postgresql 9.4 to store data
* connection via websocket (sockjs is used)

# How to run: 

gradle is the primary task runner, it builds the backend and runs npm for frontend tasks

### First run:

`gradlew runMe` - first launch. The app will fetch all the dependencies: frontend and backend (may take a while)
If you develop with Intellij Idea, install Lombok plugin for your convenience

### Develop:

`gradlew bootRun` - the application starts at `localhost:8080` (hot reloading also enabled). 
Tip: if you're using Intellij Idea, disable safe write in order to see changes immediately  

`gradlew updateFrontend` - delete previous frontend assets, build current frontend production and copy into server folder 
 
`cd src/main/frontend npm start` - starts frontend development server at `localhost:3000` 
with proxying to `localhost:8080`. This way we don't need to enable CORS. But you should have local NodeJS installed 

We've got two profiles: `dev` and `prod`

* `dev` works with h2 database (visit: `http://localhost:8080/h2-console/` to see database console). Note that 
`jdbc:h2:mem:testdb` is the url and `sa` is the username)
* `prod` uses PostgreSQL 9.4 database

`gradlew clean updateFrontend build` to make project (tests will be run as well)
