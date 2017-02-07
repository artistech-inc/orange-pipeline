# Git Repo for orange Pipeline Web-App

This is a web-app which will utilize various 3rd party apps/scripts for extracting entity information from documents.

## Version 1.3

- Initial version of JIE Multimedia

## Downloading

```sh
git clone https://github.com/artistech-inc/pipeline-base.git
cd pipeline-base
mvn clean install
cd ..
git clone https://github.com/artistech-inc/orange-pipeline.git
cd orange-pipeline-web
mvn clean package
```

## Configuration

Update the [pipeline.yml]((https://github.com/artistech-inc/orange-pipeline-web/blob/master/src/main/resources/pipeline.yml) file.  Each component must have the proper path value set.  This is the location where the external process will execute from.

- JIE-Multimedia
- Visualization Generation

The `data-path` value must be somewhere that Tomcat can write to.

## Compilation

This project should be able opened using Netbeans as a Maven Web-App. It will automatically detect the type of project.  
The project can also be compiled on the command line directly using maven.  
Depends on pipeline-base project.  See above.

## Deployment

The output from compilation is in the `target/` directory as `orange-pipeline-web-1.3.war`. This war can be deployed to Tomcat's `webapps` directory. Once deployed, it can be accessed via `http://<ip_address:port>/orange-pipeline-web-1.3/`.

## Bugs

- Issues and bugs can be e-mailed or registered.

## TODO

- Output console
- Delete data with session timeout

## Future (possibly not in scope)

- Database
- Users
