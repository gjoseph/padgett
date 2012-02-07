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
    UserDetails self;

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

    boolean isMe(String nick) {
        return self.nickname == nick
    }

    boolean isMe(UserDetails user) {
        return self.equals(user)
    }

    /**
     * Called when connecting
     */
    private void initSelf() {
        self = [
                nickname: getNick(),
                // we prepend the ~ because that's how the login name is returned by the server, thus making further comparisons (isMe()) simpler.
                login: '~' + getLogin(),
                hostname: getInetAddress().getHostName()
        ] as UserDetails
    }

    // ------- Plugins handling
    void restartAllPlugins() {

        // TODO --- really need to RELOAD if we want to reinit variables that are init'd outside callback closures...

        plugins.reverseEach { f, p ->
            delegateTo(p, 'stop')
        }
        plugins.each { f, p ->
            delegateTo(p, 'start')
        }
    }

    // ------- Plugins delegation
    void toPlugins(String command, final Object... args) {
        plugins.each { f, plugin ->
            def closure = cmd(plugin, command)
            if (closure) {
                // works :
                closure.metaClass.invokeMethod(closure, "doCall", args)
                // does not work : (while it works for single argument)
                // closure.call(args)
            }
        }
    }

    void toPlugins(Closure c) {
        plugins.each { f, p -> c.call(p) }
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
        toPlugins('onMessage', msg)
    }

    @Override
    void onPrivateMessage(String sender, String login, String hostname, String message) {
        def msg = [
                text: message,
                user: [nickname: sender, login: login, hostname: hostname]
        ] as MessageDetails
        toPlugins('onPrivateMessage', msg)
    }

    @Override
    void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        def fromUser = [nickname: sourceNick, login: sourceLogin, hostname: sourceHostname]
        def toUser = [nickname: recipient]
        if (isMe(recipient)) {
            toPlugins('onSelfOp', channel, fromUser)
        } else {
            toPlugins('onOp', channel, toUser, fromUser)
        }
    }

    @Override
    void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        def fromUser = [nickname: sourceNick, login: sourceLogin, hostname: sourceHostname]
        def toUser = [nickname: recipient]
        if (isMe(recipient)) {
            toPlugins('onSelfDeOp', channel, fromUser)
        } else {
            toPlugins('onDeOp', channel, toUser, fromUser)
        }
    }

    @Override
    void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        def fromUser = [nickname: sourceNick, login: sourceLogin, hostname: sourceHostname]
        def toUser = [nickname: recipient]
        if (isMe(recipient)) {
            toPlugins('onSelfVoice', channel, fromUser)
        } else {
            toPlugins('onVoice', channel, toUser, fromUser)
        }
    }

    @Override
    void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        def fromUser = [nickname: sourceNick, login: sourceLogin, hostname: sourceHostname]
        def toUser = [nickname: recipient]
        if (isMe(recipient)) {
            toPlugins('onSelfDeVoice', channel, fromUser)
        } else {
            toPlugins('onDeVoice', channel, toUser, fromUser)
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        def user = [nickname: sender, login: login, hostname: hostname] as UserDetails
        if (isMe(user)) {
            channels.add channel
            toPlugins('onSelfJoin', channel)
        } else {
            toPlugins('onJoin', channel, user)
        }
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        def user = [nickname: sender, login: login, hostname: hostname] as UserDetails
        if (isMe(user)) {
            channels.remove channel
            toPlugins('onSelfPart', channel)
        } else {
            toPlugins('onPart', channel, user)
        }
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        def kicker = [nickname: kickerNick, login: kickerLogin, hostname: kickerHostname] as UserDetails
        def kickee = [nickname: recipientNick] as UserDetails

        // TODO also call onPart ?
        toPlugins('onKick', channel, kicker, kickee, reason)
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        def user = [nickname: sourceNick, login: sourceLogin, hostname: sourceHostname] as UserDetails
        /// TODO also call onPart for each channel the user was on ?
        toPlugins('onQuit', user)
    }

    @Override
    protected void onConnect() {
        initSelf()
        toPlugins('onConnect')
    }

    @Override
    protected void onDisconnect() {
        toPlugins('onDisonnect')
    }

    // ------  Underlying methods
    @Override
    void log(String line) {
        super.log(line)
    }

}

