package net.incongru.gregor.pluginloader

/**
 * Largely inspired by http://cadrlife.blogspot.com/2010/07/watch-directory-for-changes-in-groovy.html
 */
abstract class DirectoryWatcher extends TimerTask {
    final static FileFilter fileFilter = {file -> !(file.name =~ /\.swp$|\~$|^\./)} as FileFilter

    final String path
    final def callback
    final def dir = [:]
    // Exclude temp files created by vim, emacs, etc...

    public DirectoryWatcher(String path, callback) {
        this.path = path;
        this.callback = callback;
        run()
    }

    public final void run() {
        def checkedFiles = new HashSet();
        def files = new File(path).listFiles(fileFilter);

        // scan the files and check for modification/addition
        for (File file: files) {
            def currentTS = dir.get(file)
            checkedFiles.add(file)
            if (currentTS == null) {
                // new file
                dir.put(file, file.lastModified())
                onChange(file, FileChange.added)
            }
            else if (currentTS != file.lastModified()) {
                // modified file
                dir.put(file, file.lastModified())
                onChange(file, FileChange.modified)
            }
        }

        // now check for deleted files
        def deletedFiles = dir.clone().keySet() - checkedFiles
        deletedFiles.each {
            dir.remove(it)
            onChange(it, FileChange.removed)
        }
    }

    protected abstract void onChange(File file, FileChange change)
}

