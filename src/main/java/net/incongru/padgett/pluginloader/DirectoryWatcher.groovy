package net.incongru.padgett.pluginloader

import groovy.io.FileType

/**
 * Largely inspired by http://cadrlife.blogspot.com/2010/07/watch-directory-for-changes-in-groovy.html
 */
class DirectoryWatcher extends TimerTask {
    final static def sortByTypeThenName = { a, b ->
        a.isDirectory() != b.isDirectory() ? a.isDirectory() <=> b.isDirectory() : String.CASE_INSENSITIVE_ORDER.compare(a.name, b.name)
    }

    final File rootDir
    final def callback
    final def dir = [:]
    // Exclude temp files created by vim, emacs, etc...

    public DirectoryWatcher(String path, callback) {
        this.rootDir = new File(path);
        this.callback = callback;
        run()
    }

    public final void run() {
        def checkedFiles = new HashSet();
        rootDir.traverse(
                type: FileType.FILES,
                sort: sortByTypeThenName,
                nameFilter: ~/.*\.botplugin$/,
                { file ->
                    // println "* Visiting ${file}"
                    def currentTS = dir.get(file)
                    checkedFiles.add(file)
                    if (currentTS == null) {
                        dir.put(file, file.lastModified())
                        onChange(file, FileChange.added)
                    }
                    else if (currentTS != file.lastModified()) {
                        dir.put(file, file.lastModified())
                        onChange(file, FileChange.modified)
                    }

                }
        )
        // println "Visited all files."

        // now check for deleted files
        def deletedFiles = dir.clone().keySet() - checkedFiles
        deletedFiles.each {
            dir.remove(it)
            onChange(it, FileChange.removed)
        }
    }

    protected void onChange(File file, FileChange change) {
        println "${file.absolutePath} was ${change}"
        try {
            callback.onPluginChange(file, change)
        } catch (Throwable t) {
            t.printStackTrace()
            println "Oh lord, we were not able to act on a change for ${file}: ${t.message}"
        }
    }
}

