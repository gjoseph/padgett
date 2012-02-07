package net.incongru.padgett

import net.incongru.padgett.pluginloader.DirectoryWatcher
import net.incongru.padgett.pluginloader.FileChange
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import static net.incongru.padgett.pluginloader.FileChange.*

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class)

    final PadgettTheBotess bot
    final def cfg
    final def timer

    public static void main(String[] args) {
        new Main('padgett.cfg' as File).start()
    }

    def Main(File configFile) {
        bot = new PadgettTheBotess()
        // TODO - watch configuration just as well - reload all plugins when changed
        // TODO - allow passing a different cfg file
        // TODO - wrap config in an object that validates configuration and logs hints ('plugin X needs Y to be configured')
        timer = new Timer()

        def configWatcher = new ConfigWatcher(bot, configFile)
        cfg = configWatcher.getConfigProxy()
        timer.schedule(configWatcher, 5000, 10000)
    }

    void start() {
        def pluginsWatcher = new DirectoryWatcher(cfg.pluginsDirectory, this)
        timer.schedule(pluginsWatcher, 5000, 10000)

        // this implicitly keeps the app running - should we manage threading ourselves ?
        // anything else is done by plugins
    }

    void onPluginChange(File file, FileChange change) {
        switch (change) {
            case removed:
                def plugin = bot.plugins.remove(file)
                bot.delegateTo(plugin, 'stop')
                break
            case added:
                def plugin = loadPlugin(file)
                if (plugin) {
                    bot.delegateTo(plugin, 'start')
                    bot.plugins.put(file, plugin)
                }
                break
            case modified:
                def oldPlugin = bot.plugins.remove(file)
                bot.delegateTo(oldPlugin, 'stop')
                def plugin = loadPlugin(file)
                if (plugin) {
                    bot.delegateTo(plugin, 'start')
                    bot.plugins.put(file, plugin)
                }
                break
        }
    }

    def loadPlugin(File file) {
        try {
            final Logger pluginLog = LoggerFactory.getLogger((file.path =~ "${cfg.pluginsDirectory}/(.*)\\.botplugin")[0][1])
            final Class groovyClass = GroovyUtil.loadClass(file)
            //def groovyObj = groovyClass.newInstance(bot:bot) as GroovyObject
            if (Script.class.isAssignableFrom(groovyClass)) {
                log.info "> Loading ${file.name} as a Script"
                def script = groovyClass.newInstance() as Script
                def binding = [bot: bot, cfg: cfg, log: pluginLog] as Binding
                script.setBinding(binding)
                script.run()
                return binding.getVariable('plugin')// as Plugin
                // if we use this form, we have a Map of closures; this can't seem to be "cast" to the Plugin interface
                // since we want plugins to be able to only listen to events they care about, that interface is probably unnecessary anyway
            } else {
                log.info "> Loading ${file.name} as a Plugin"
                return groovyClass.newInstance(bot: bot, cfg: cfg, log: pluginLog) as Plugin
            }
            // return (Plugin) groovyClass.newInstance()
        } catch (Exception e) {
            // bot.broadcastNotice
            log.error("Could not load plugin from ${file}: ${e.message}")
            return null
        }
    }

}
