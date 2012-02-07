package net.incongru.padgett

import org.codehaus.groovy.runtime.InvokerHelper

class ConfigWatcher extends TimerTask {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ConfigWatcher.class);

    final groovy.util.Proxy configProxy = new groovy.util.Proxy() {
        @Override
        Object getProperty(String property) {
            try {
                return super.getProperty(property)
            } catch (MissingFieldException e) {
                return InvokerHelper.getProperty(adaptee, property);
            }
        }
    }

    final PadgettTheBotess bot
    final File file
    long ts = -1

    protected ConfigWatcher(PadgettTheBotess bot, File configFile) {
        super()
        this.bot = bot
        this.file = configFile
        loadConfiguration()
    }

    @Override
    void run() {
        def currentTS = file.lastModified()
        if (ts>0 && currentTS > ts) {
            log.info "Reloading configuration from ${file}"
            loadConfiguration()
            bot.restartAllPlugins()
        }
    }

    void loadConfiguration() {
        try {
            ts=file.lastModified()
            Class cfgClass = GroovyUtil.loadClass(file)
            def script = cfgClass.newInstance() as Script
            script.run()
            log.info "Loaded configuration from ${file}"
            configProxy.setAdaptee(script.binding)
        } catch (Exception e) {
            throw new IllegalStateException("Could not load configuration from ${file.absolutePath}: ${e.message}", e)
        }
    }

}
