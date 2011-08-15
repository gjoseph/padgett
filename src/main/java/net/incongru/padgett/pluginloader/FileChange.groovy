package net.incongru.padgett.pluginloader

/**
 * So it looks like using an "inner" enum in Groovy is still a pain in the anus with 1.8.1...
 */
public enum FileChange {
    added, modified, removed
}