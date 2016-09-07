package com.github.jeysal.gradle.plugin.graphviz

/**
 * @author Tim Seckinger
 * @since 9/2/16
 */
class GraphvizExtension {
    public static final String NAME = 'graphviz'

    /**
     * Whether to download and use viz.js as Graphviz executable.
     */
    boolean useVizJs = true
    /**
     * Path of the Graphviz executable (or folder that contains it) to use.<br/>
     * If not set, PATH environment variable is used.
     */
    String executablePath = ''
    /**
     * Graphviz layout engine to use. Defaults to 'dot'.
     */
    String layout = 'dot'
}
