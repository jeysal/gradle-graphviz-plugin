package com.github.jeysal.gradle.plugin.graphviz

import groovy.transform.CompileStatic

/**
 * Provides configuration for integration with other plugins / build steps.
 * @author Tim Seckinger
 * @since 9/25/16
 */
@CompileStatic
class GraphvizHooksExtension {
    public static final String NAME = 'graphvizHooks'

    /**
     * If true, asciidoctor will automatically be told where to find the Graphviz dot executable
     * for use in asciidoctor-diagram. Defaults to true.
     */
    def asciidoctor = true
}
