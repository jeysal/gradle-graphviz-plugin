package com.github.jeysal.gradle.plugin.graphviz.asciidoctor

import com.github.jeysal.gradle.plugin.graphviz.GraphvizTask
import com.github.jeysal.gradle.plugin.graphviz.node.VizSetupTask
import org.asciidoctor.gradle.AsciidoctorTask
import org.gradle.api.Project

/**
 * @author Tim Seckinger
 * @since 9/24/16
 */
class AsciidoctorManager {
    private final Project project

    AsciidoctorManager(final Project project) {
        this.project = project
    }

    /**
     * Tell all asciidoctor tasks where to find graphviz (unless disabled)
     */
    void registerGraphviz() {
        if (project.extensions.graphvizHooks.asciidoctor) {
            final asciidoctorTasks = project.tasks.withType AsciidoctorTask

            asciidoctorTasks*.dependsOn VizSetupTask.NAME
            asciidoctorTasks*.attributes graphvizdot: project.tasks.findByName(GraphvizTask.NAME).executablePath
        }
    }
}
