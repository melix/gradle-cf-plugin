GRADLE PLUGIN FOR CLOUD FOUNDRY
===============================

This plugin allows you to deploy, update, remove applications thanks to Gradle tasks. It is intended to
replace the use of vmc to deploy such applications.

The plugin adds the following tasks:

* cf-info: Connects to a CloudFoundry platform and returns info about the platform
* cf-login: Logs in, then logs out from the platform. Used to check credentials.
* cf-apps: Lists applications on the cloud platform
* cf-push: Deploys an application
* cf-update: Updates an application
* cf-start: Starts an application
* cf-stop: Stops an application
* cf-restart: Restarts an application
* cf-status: Displays information about an application
* cf-delete-app: Removes an application from the cloud
* cf-delete-service: Removes a service
* cf-add-service: Add a service
* cf-bind: Binds a service to an application
* cf-unbind: Unbinds a service from an application
* cf-add-user: Registers a user to the cloud
* cf-delete-user: Unregisters the user from the cloud

Configuring
-----------

Configuration is either project based or task based. It is simpler to use project configuration. Here is a sample
Gradle project.

```buildscript {
       repositories {
   	     mavenCentral()
       }
       dependencies {
           classpath group: 'org.gradle.api.plugins', name: 'gradle-cf-plugin', version: '0.1.0-SNAPSHOT'
       }
   }

   apply plugin: 'cloudfoundry'

   cloudfoundry {
      username = 'login@domain.foo'
      password = 's3cr3t'
      application = 'appName'
      framework = 'grails'
      warFile = new File('/path/to/app.war')
      uris = ['http://appName.cloudfoundry.com']
   }```

Then usage is simple:

This will deploy the application:
```gradle cf-push```

The configuration options are:
* target: the URL of the target API (http://api.cloudfoundry.com by default)
* username: your username
* password: your password
* application: the name of your application (defaults to the Gradle project name)
* framework: the identifer of the framework your application is using
* startApp: should the application be started right after upload? (defaults to true)
* memory: amount of memory for an application (defaults to 512)
* instances: if >0, number of instances
* URIs: list of URIs where to deploy
* services: list of services the application uses
* warFile (type: File): path to the WAR file to be deployed

Adding a service
----------------

Adding a service makes use of another configuration section:
```
cloudfoundryService {
  	serviceName = 'service1'
   	vendor = 'mongodb'
   	version = '1.8'
   }
```

The ```cloudfoundryService``` section accepts several parameters:
* serviceName: the name of the service to be created. If no service name is specified, a service name is generated.
* vendor: the name of the service vendor (mongodb, ...), see cf-info for a list of available services
* version (optional): the version of the service
* tier (optional): the tier option of the service (by default, 'free')
* type (optional): the type of the service
* bind (optional): false by default. If true, the service is bound to an application. In that case, the application name must be specified.

Removing a service
------------------

Removing a service makes use of the same ```cloudfoundryService``` section, and uses the ```serviceName``` option. If
the value of the service name is ```*```, then *all services* are removed.

Overriding properties from command line
---------------------------------------

In addition to the build.gradle based configuration, it is also possible to use command line options to set properties.
For example, to create a new service, you can use the following command line (assuming you set the cloudfoundry section
in the build.gradle file):

```gradle cf-add-service -PcloudfoundryService.serviceName='mongodb-1' -PcloudfoundryService.vendor='mongodb'```

Future work
-----------

Future work includes ability to add users and support other features of Cloud Foundry. Any help
is appreciated.