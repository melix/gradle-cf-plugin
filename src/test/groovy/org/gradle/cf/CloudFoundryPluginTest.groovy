package org.gradle.cf

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class CloudFoundryPluginTest {
    @Test
    public void pluginIsLoaded() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'cloudfoundry'
    }
}
