package net.incongru.padgett

/**
 *
 */
public interface Plugin {
    String name
    
    void start(/*GregorTheBot bot*/)

    void stop(/*GregorTheBot bot*/)

    void onMessage(/*GregorTheBot bot,*/ MessageDetails message)

    void onPrivateMessage(/*GregorTheBot bot,*/ MessageDetails message)

    // all "event" methods of org.jibble.pircbot.PircBot: do we want to use this ?
    /*
    protected void onConnect() {}
    protected void onDisconnect() {}
    protected void onServerResponse(int code, String response) {}
    protected void onUserList(String channel, User[] users) {}
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {}
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {}
    protected void onAction(String sender, String login, String hostname, String target, String action) {}
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {}
    protected void onJoin(String channel, String sender, String login, String hostname) {}
    protected void onPart(String channel, String sender, String login, String hostname) {}
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {}
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {}
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {}
    protected void onTopic(String channel, String topic) {}
    protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {}
    protected void onChannelInfo(String channel, int userCount, String topic) {}
    protected void onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {}
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {}
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
    protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
    protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
    protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {}
    protected void onSetChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {}
    protected void onRemoveChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {}
    protected void onSetChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname, int limit) {}
    protected void onRemoveChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onSetChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {}
    protected void onRemoveChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {}
    protected void onSetTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onRemoveTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onSetNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onRemoveNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onSetInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onRemoveInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onSetModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onRemoveModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onSetPrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onRemovePrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onSetSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onRemoveSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {}
    protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel)  {}
    protected void onDccSendRequest(String sourceNick, String sourceLogin, String sourceHostname, String filename, long address, int port, int size) {}
    protected void onDccChatRequest(String sourceNick, String sourceLogin, String sourceHostname, long address, int port) {}
    protected void onIncomingFileTransfer(DccFileTransfer transfer) {}
    protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {}
    protected void onIncomingChatRequest(DccChat chat) {}
    protected void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {}
    protected void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue) {}
    protected void onServerPing(String response) {}
    protected void onTime(String sourceNick, String sourceLogin, String sourceHostname, String target) {}
    protected void onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target) {}
    protected void onUnknown(String line) {}
    */
}