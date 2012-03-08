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

import org.gradle.api.tasks.TaskAction
import org.cloudfoundry.client.lib.CloudApplication

/**
 * A basic task which can be used to check the status of an application.
 */
class StatusCloudFoundryTask extends AbstractCloudFoundryTask {
    String application

    StatusCloudFoundryTask() {
        super()
        description = 'Returns information about an application deployed on the cloud'
    }

    @TaskAction
    void login() {
        connectToCloudFoundry()
        if (client) {
            CloudApplication app = client.getApplication(getApplication())
            log """Application '${app.name} is ${app.state}'
URIs: ${app.uris.join(" ")}
Memory: ${app.memory}MB
Services: ${app.services.join(" ")}
Instances: ${app.runningInstances}/${app.instances}
"""
        }
    }
}
