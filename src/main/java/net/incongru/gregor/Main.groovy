package net.incongru.gregor

import static net.incongru.gregor.pluginloader.FileChange.*
import net.incongru.gregor.pluginloader.DirectoryWatcher
import net.incongru.gregor.pluginloader.FileChange

public class Main {
    final GregorTheBot gregor

    public static void main(String[] args) {
        new Main().start()
    }

    def Main() {
        gregor = new GregorTheBot()
    }

    void start() {
        def watcher = new DirectoryWatcher('plugins', this) {
            @Override
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

        Timer timer = new Timer()
        timer.schedule(watcher, 5000, 10000)

        // this implicitly keeps the app running - should manage threading ourselves ?
        gregor.name = "groovy_bot"
        gregor.setVerbose true // if true, verbose messages output to console.
        gregor.connect "irc.freenode.net"
        gregor.joinChannel "#greg-test"
    }

    void onPluginChange(File file, FileChange change) {
        switch (change) {
            case removed:
                def plugin = gregor.plugins.remove(file)
                plugin?.stop()
                break
            case added:
                def plugin = loadPlugin(file)
                plugin?.start()
                gregor.plugins.put(file, plugin)
                break
            case modified:
                def oldPlugin = gregor.plugins.remove(file)
                oldPlugin?.stop()
                def plugin = loadPlugin(file)
                plugin?.start()
                gregor.plugins.put(file, plugin)
                break
        }
    }

    Plugin loadPlugin(File file) {
        try {
            ClassLoader parent = getClass().getClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);
            Class groovyClass = loader.parseClass(file);
            //def groovyObj = groovyClass.newInstance(bot:gregor) as GroovyObject
            if (Script.class.isAssignableFrom(groovyClass)) {
                def script = groovyClass.newInstance() as Script
                def binding = [bot: gregor] as Binding
                script.setBinding(binding)
                script.run()
                return binding.getVariable('plugin') as Plugin
            } else {
                return groovyClass.newInstance(bot:gregor) as Plugin
            }
            // return (Plugin) groovyClass.newInstance()
        } catch (Exception e) {
            gregor.broadcastNotice("Could not load plugin from ${file}: ${e.message}")
            return null
        }
    }
}
