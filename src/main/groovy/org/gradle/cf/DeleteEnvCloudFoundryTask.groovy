/*
 * Copyright 2012 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.gradle.cf

import org.cloudfoundry.client.lib.CloudApplication
import org.gradle.api.tasks.TaskAction

/**
 * A basic task which can be used to delete an environment variable to an application on CloudFoundry platform.
 */
class DeleteEnvCloudFoundryTask extends AbstractEnvCloudFoundryTask {

    Map<String, String> envers = [:]

    DeleteEnvCloudFoundryTask() {
        super()
        description = 'Basic task which can be used to delete an environment variable to an application on CloudFoundry platform'
    }

    @TaskAction
    void deleteEnvironmentVariables() {
        connectToCloudFoundry()
        CloudApplication app = client?.getApplication(getApplication())

        def envAsMap = app.getEnvAsMap()
        log('Deleting environment variables for ' + getApplication() + ': ' + getEnvers())
        envAsMap -= getEnvers()
        app.setEnv(envAsMap)
        client.updateApplicationEnv(getApplication(), envAsMap)

        listEnvironmentVariables(app)
    }
}
