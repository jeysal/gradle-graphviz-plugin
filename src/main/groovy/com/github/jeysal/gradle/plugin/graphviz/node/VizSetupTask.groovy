package com.github.jeysal.gradle.plugin.graphviz.node

import com.moowork.gradle.node.npm.NpmSetupTask
import com.moowork.gradle.node.npm.NpmTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem

/**
 * @author Tim Seckinger
 * @since 9/2/16
 */
class VizSetupTask extends NpmTask {
    public static final String NAME = 'vizSetup'

    public static final String VIZ_MODULE_NAME = 'viz.js-cli-wrapper'
    public static final String VIZ_MODULE_VERSION = '^1.3.0'

    @Input
    private boolean windows

    @Input
    File node
    @Input
    File dot
    @OutputFile
    File wrapper

    VizSetupTask() {
        group = 'Node'
        description = 'Installs the viz binaries via npm'

        final npmSilenceParams = (logging.level ?: project.logging.level ?: project.gradle.startParameter.logLevel) ==
                LogLevel.DEBUG ? [] : ['--silent']

        setNpmCommand('install',
                *npmSilenceParams,
                "$VIZ_MODULE_NAME@$VIZ_MODULE_VERSION")
        dependsOn NpmSetupTask.NAME

        windows = OperatingSystem.current().windows
        wrapper = new File(project.gradle.startParameter.projectCacheDir ?: new File(project.projectDir, '.gradle'),
                'graphvizdot' + (windows ? '.cmd' : ''))

        // nodeModulesDir might still change
        project.afterEvaluate {
            final File nodeModules = new File(project.extensions.node.nodeModulesDir as File, 'node_modules')
            outputs.files(new File(nodeModules, VIZ_MODULE_NAME),
                    new File(nodeModules, '.bin'))

            node = new File(project.extensions.node.variant.nodeBinDir as File,
                    'node' + (windows ? '.exe' : ''))
            dot = new File(project.extensions.node.nodeModulesDir as File,
                    "node_modules/$VIZ_MODULE_NAME/src/main/js/cli/dot.js")
        }
    }

    boolean isWindows() {
        return windows
    }

    /**
     * Creates a wrapper script that calls node on the viz.js-cli-wrapper dot binary.
     * The regular .bin wrappers created by npm are not sufficient since we cannot assume that node is on path.
     */
    @TaskAction
    void createWrapper() {
        wrapper.text = windows ? /@"$node.path" "$dot.path" %*/ : $/#!/usr/bin/env sh
"$node.path" "$dot.path" "$@"/$
        wrapper.setExecutable(true, false)
    }
}
