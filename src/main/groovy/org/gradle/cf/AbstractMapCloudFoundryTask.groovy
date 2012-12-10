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

abstract class AbstractMapCloudFoundryTask extends AbstractCloudFoundryTask {

    String application
    List<String> uris

    AbstractMapCloudFoundryTask() {
        super()
    }

    protected void listUriMappings(CloudApplication application) {
        def uris = application.uris
        log("Current URI mappings for ${getApplication()}: ")
        if (uris.isEmpty()) {
            log('\tNone found')
        }
        for (uri in uris) {
            log('\t' + uri)
        }
    }
}
