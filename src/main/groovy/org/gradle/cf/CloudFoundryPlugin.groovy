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

import org.gradle.api.Plugin
import org.gradle.api.Project

class CloudFoundryPlugin implements Plugin<Project> {
    void apply(Project project) {
        // register extension
        def config = new CloudFoundryPluginExtension()
        project.extensions.cloudfoundry = config
        def serviceConfig = new CloudFoundryServiceExtension()
        project.extensions.cloudfoundryService = serviceConfig
        config.application = project.name

        // register tasks
        project.task('cf-login', type: LoginCloudFoundryTask)
        project.task('cf-start', type: StartApplicationCloudFoundryTask)
        project.task('cf-restart', type: StartApplicationCloudFoundryTask)
        project.task('cf-stop', type: StopApplicationCloudFoundryTask)
        project.task('cf-info', type: InfoCloudFoundryTask)
        project.task('cf-status', type: StatusCloudFoundryTask)
        project.task('cf-delete-app', type: DeleteApplicationCloudFoundryTask)
        project.task('cf-add-service', type: AddServiceCloudFoundryTask)
        project.task('cf-delete-service', type: DeleteServiceCloudFoundryTask)
        project.task('cf-push', type: PushApplicationCloudFoundryTask)
        project.task('cf-update', type: UpdateApplicationCloudFoundryTask)

        // initiate properties
        project.tasks.withType(AbstractCloudFoundryTask).each { task ->
            config.properties.each { p, v ->
                if (p!='class' && p!='metaClass') {
                    if (task.hasProperty(p)) {
                        task.conventionMapping[p] = { config.getProperty(p) }
                    }
                }
            }
        }
        project.tasks.withType(AddServiceCloudFoundryTask).each { task ->
            ['serviceName','vendor','version','type','tier','bind'].each { p ->
                task.conventionMapping[p] = { serviceConfig.getProperty(p) }
            }
        }
        project.tasks.withType(DeleteServiceCloudFoundryTask).each { task ->
            task.conventionMapping.serviceName = { serviceConfig.serviceName }
        }
    }
}
