package com.example.demo.filters;

public interface RouteFilter {
    /**
     * The id of this filter. It must be unique to this app
     * @return a string representation of the id
     */
    String getId();

    /**
     * The host name of the source.
     * Not required if a source path is provided
     * @return
     */
    String getSourceHost();

    /**
     * The source path to match against.
     * This is what will be evaluated to determine if the filter applies
     * @return
     */
    String getSourceFilterPath();

    /**
     * The new host to direct the call to.
     * @return
     */
    String getTargetUri();
}
