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
 * Task which lists applications deployed on CloudFoundry.
 */
class AppsCloudFoundryTask extends AbstractCloudFoundryTask {

    AppsCloudFoundryTask() {
        super()
        description = 'Lists applications on the cloud'
    }

    @TaskAction
    void list() {
        connectToCloudFoundry()
        if (client) {
            List<CloudApplication> apps = client.applications
            StringBuilder sb = new StringBuilder('\nApplications\n------------\n')
            apps.eachWithIndex { CloudApplication app, int i ->
                sb.append("""$i- ${app.name} is ${app.state}
   Instances: ${app.runningInstances}/${app.instances} Memory: ${app.memory}MB Debug: ${app.debug?'on':'off'}
   Services: ${app.services.join(',')}
   URIs: ${app.uris.join(' ')}
   Environment variables: ${app.envAsMap.collect {k,v -> "$k:$v"}.join(' ')}
   Resources: ${app.resources.collect {k,v -> "$k:$v"}.join(' ')}
   Staging: ${app.staging.collect {k,v -> "$k:$v"}.join(' ')}
   Metadata: ${app.meta.collect {k,v -> "$k:$v"}.join(' ')}
   """)
            }
            log sb.toString()
            client.logout()
        }
    }
}
