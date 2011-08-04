package net.incongru.gregor

import org.jibble.pircbot.PircBot
import org.codehaus.groovy.control.messages.Message
import net.incongru.gregor.Plugin
import net.incongru.gregor.MessageDetails

/**
 * http://groovyconsole.appspot.com/script/46002
 * ref. http://www.jibble.org/pircbot.php
 */
//@Grab("pircbot:pircbot:1.5.0")
public class GregorTheBot extends PircBot {
    final plugins = [:] as Map<File, Plugin>
    final channels = [] as List<String>

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

    @Override
    void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message =~ name) {
            sendNotice channel, "You said: $message"
            sendMessage channel, "May I help you?"
        }
        def msg = [
                channel:channel,
                text:message,
                nickname:sender,
                login:login,
                hostname:hostname
        ] as MessageDetails
        plugins.values().each { plugin ->
            if (plugin) {
                if (plugin.metaClass.respondsTo(plugin, 'onMessage')) {
                    plugin?.onMessage(msg)
                }
            }
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        def msg = [
                text:message,
                nickname:sender,
                login:login,
                hostname:hostname
        ] as MessageDetails
        plugins.values().each { plugin ->
            plugin?.onPrivateMessage(msg)
        }
    }

    @Override
    void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        sendMessage channel, "Thank you so much!"
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        channels.add channel
        sendMessage(channel, "Hello, world.")
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        sendMessage(channel, "Goodbye !")
        channels.remove channel
    }



}

