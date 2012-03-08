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

import org.gradle.api.GradleException

/**
 * Base class for tasks related to the createApplication API.
 *
 * @author Cedric Champeau
 */
abstract class AbstractCreateApplicationCloudFoundryTask extends AbstractCloudFoundryTask {
    String application
    String framework
    int memory
    List<String> uris
    List<String> services
    File warFile

    int instances = -1
    boolean startApp = true

    protected void ensureWarFile() {
        if (!getWarFile() || !getWarFile().isFile()) {
            throw new GradleException("You must specify a valid WAR file ('${getWarFile()}' is not valid)")
        }
    }
}
