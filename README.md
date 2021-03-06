Padgett is an IRC robot written in Groovy, exposing a simple plugin system.
Every bit of functionality in Padgett is written as a plugin, and plugins can be deployed or reloaded without restarting the bot.

Padgett - the name
==================

> (Middle English) little page; (Middle English) server, helper
[ 2 syll. pad-get(t), pa-dge-tt ] Padgett is a form of the English Paget. Padgett is a form of the English Paige,
which is itself from the ordinary English word page ('servant, helper'). This word is in turn derived via Old French and Italian from the Greek 'paidion'.
In medieval times, pages were male servants to lords, but the modern use of the name is almost exclusively for girls, for reasons that are unclear.

Also, whatever. "Padgett" sounds like "Gadget", so why not.


Done, to document
=================
* plugins are loaded in subfolders,
* name pattern for plugins: *.botplugin - so (hopefully) classes and other utils can stay there - TODO: verify.
* different callbacks is action taken by self

TODO
====
* verify or enforce plugin order (alphabetical?)
* verify plugins can utilize other classes
  ** Can plugins use Grapes ?
* daemonize: see http://yajsw.sourceforge.net ?
* instructions, setup, install

* let plugins react on the bot's own messages (i.e sent by other plugins)... this might get verbose but... well...let's try ?
  * use a different callback so plugins can react differently->done, see isMe() and its usage
  * verify we can't send the bot in a crazy loop (see Echo plugin)

* configuration:
  Should there be one "config" object ?
  Should each plugin look at the same set of arbitrary properties ?
  Should each plugin simply have its own configuration ? (should it be embedded in the plugin code, make it non distributable?)
  Should each plugin register its config items in the shared config object ?

  Should plugins "talk" to each other ?
  (for example they could share one Jira::proxy instance)

* Observe configuration

* plugin name ?
* plugin dependencies ? (could be useful, or even needed, for proper reload/restart)

* save plugin states ?

* ability to schedule jobs (i.e plugins shouldn't block the plugin loop when looking up a url or jira issue for ex)

* add help/description in plugins ?
* add an help plugin, lists available plugin and their "help" string ?

* Check Jenkins' bots:
  https://wiki.jenkins-ci.org/display/JENKINS/IRC+Bot
  https://github.com/jenkinsci/ircbot-plugin
  https://github.com/jenkinsci/backend-ircbot

* Checkout Netty - http://netty.io/ - http://www.jboss.org/netty - is there an irc implementation ? other protocols ? 
  Might help with:
    * Abstracting IRC away if we wanted to support other protocols
    * Have a better or more efficient api than pircbot ?

Plugins to write
----------------

* log
* meetbot (with +voice)
* jira info
* "shut up" (bot will be silent until his name is said)
  -- is this still relevant ? every other plugin would need to "check" if they can talk ??
  -- perhaps make it a meta plugin that is able to enable/disable other plugins ?
* wait for user (bot will notice you when user join and/or send a message to the user)

# DSL to allow messages like
  @padgett create room with @greg @fred and @bob
  @padgett create private room for "foo bar" with ...
  @padgett create temp room with ...
  @padgett ...with log? or just auto-log all chans

* auto-invite - monitor for people login in/out and invite them to channels where they were previously invited

* log, auto-op etc.. includes/excludes ? By default include all, exclude configured chans ?

Build & Run
===========
Build with `gradle build`
Run with `java -jar build/libs/padgett-ircbot.jar`
See the wiki for help on installing clients, building and running from sources, etc.
