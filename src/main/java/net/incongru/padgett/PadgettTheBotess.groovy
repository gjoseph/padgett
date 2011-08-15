package net.incongru.padgett

import org.jibble.pircbot.PircBot

/**
 * http://groovyconsole.appspot.com/script/46002
 * ref. http://www.jibble.org/pircbot.php
 */
//@Grab("pircbot:pircbot:1.5.0")
public class PadgettTheBotess extends PircBot {
    final plugins = [:] as Map<File, Plugin>
    final channels = [] as List<String>

    // ----- Additional methods
    void broadcastMessage(String message) {
        channels.each { channel ->
            sendMessage channel, message
        }
    }

    void broadcastNotice(String notice) {
        channels.each { channel ->
            sendNotice channel, notice
        }
    }
/** pbly not needed
    void toPlugins(String command) {
//        toPlugins { cmd(it,command).call() }
        plugins.values().each { plugin ->
            cmd(plugin, command)?.call()
        }
    }
 */

    void toPlugins(String command, final Object... args) {
//        toPlugins { cmd(it,command).call(args) }
        plugins.values().each { plugin ->
            def closure = cmd(plugin, command)
            if (closure) {
                // works :
                closure.metaClass.invokeMethod(closure,"doCall",args)
                // does not work : (while it works for single argument)
                // closure.call(args)
            }
        }
    }

//    void toPlugins(String command, final Object arg) {
//        toPlugins { cmd(it,command).call(arg) }
//        plugins.values().each { plugin ->
//            cmd(plugin, command)?.call(arg)
//        }
//    }

    void toPlugins(Closure c) {
        plugins.values().each { c.call(it) }
    }

    void delegateTo(plugin, String command) {
        cmd(plugin, command)?.call()
    }

    void delegateTo(plugin, String command, final Object arg) {
        cmd(plugin, command)?.call(arg)
    }

    void delegateTo(plugin, String command, final Object... args) {
        cmd(plugin, command)?.call(args)
    }

    Closure cmd(plugin, String command) {
        return plugin?.get(command) ?: null
    }

    // ------ Event methods
    @Override
    void onMessage(String channel, String sender, String login, String hostname, String message) {
        def msg = [
                channel: channel,
                text: message,
                user: [nickname: sender, login: login, hostname: hostname]
        ] as MessageDetails
//        plugins.values().each { plugin ->
        //            delegateTo(plugin, 'onMessage', msg)
        //        }
        toPlugins('onMessage', msg)
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        def msg = [
                text: message,
                user: [nickname: sender, login: login, hostname: hostname]
        ] as MessageDetails
        toPlugins('onPrivateMessage', msg)
    }

    @Override
    void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        sendMessage channel, "Thank you so much!"
        def fromUser = [nickname: sourceNick, login: sourceLogin, hostname: sourceHostname]
        toPlugins('onOp', recipient, fromUser)
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        def user = [nickname: sender, login: login, hostname: hostname] as UserDetails
        toPlugins('onJoin', channel, user)

        /// TODO only keep track of the channels if user=self or do this in the joinChannel method!
        channels.add channel
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        def user = [nickname: sender, login: login, hostname: hostname] as UserDetails
        toPlugins('onPart', channel, user)
        /// TODO only keep track of the channels if user=self or do this in the joinChannel method!
        channels.remove channel
    }

    @Override
    protected void onConnect() {

    }

    @Override
    protected void onDisconnect() {
    }

    // ------  Underlying methods
    @Override
    void log(String line) {
        super.log(line)
    }


}

