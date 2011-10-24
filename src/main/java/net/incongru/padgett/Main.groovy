package net.incongru.padgett

import static net.incongru.padgett.pluginloader.FileChange.*
import net.incongru.padgett.pluginloader.DirectoryWatcher
import net.incongru.padgett.pluginloader.FileChange

public class Main {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

    final PadgettTheBotess ircbot

    public static void main(String[] args) {
        new Main().start()
    }

    def Main() {
        ircbot = new PadgettTheBotess()
    }

    void start() {
        def watcher = new DirectoryWatcher('plugins', this)

        Timer timer = new Timer()
        timer.schedule(watcher, 5000, 10000)

        // this implicitly keeps the app running - should we manage threading ourselves ?
//        ircbot.name = "padgett_bot"
        ircbot.setVerbose true // if true, verbose messages output to console.
        //ircbot.connect "localhost", 6667
        // ircbot.joinChannel "#greg-test"
    }

    void onPluginChange(File file, FileChange change) {
        switch (change) {
            case removed:
                def plugin = ircbot.plugins.remove(file)
                ircbot.delegateTo(plugin, 'stop')
                break
            case added:
                def plugin = loadPlugin(file)
                if (plugin) {
                    ircbot.delegateTo(plugin, 'start')
                    ircbot.plugins.put(file, plugin)
                }
                break
            case modified:
                def oldPlugin = ircbot.plugins.remove(file)
                ircbot.delegateTo(oldPlugin, 'stop')
                def plugin = loadPlugin(file)
                if (plugin) {
                    ircbot.delegateTo(plugin, 'start')
                    ircbot.plugins.put(file, plugin)
                }
                break
        }
    }

    def loadPlugin(File file) {
        try {
            ClassLoader parent = getClass().getClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);
            Class groovyClass = loader.parseClass(file);
            //def groovyObj = groovyClass.newInstance(bot:ircbot) as GroovyObject
            if (Script.class.isAssignableFrom(groovyClass)) {
                println "> Loading ${file.name} as a Script"
                def script = groovyClass.newInstance() as Script
                def binding = [bot: ircbot] as Binding
                script.setBinding(binding)
                script.run()
                return binding.getVariable('plugin')// as Plugin
                // if we use this form, we have a Map of closures; this can't seem to be "cast" to the Plugin interface
                // since we want plugins to be able to only listen to events they care about, that interface is probably unnecessary anyway

            } else {
                println "> Loading ${file.name} as a Plugin"
                return groovyClass.newInstance(bot:ircbot) as Plugin
            }
            // return (Plugin) groovyClass.newInstance()
        } catch (Exception e) {
            // ircbot.broadcastNotice
            log.error("Could not load plugin from ${file}: ${e.message}")
            return null
        }
    }
}
