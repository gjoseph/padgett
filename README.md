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

TODO
====
* verify or enforce plugin order (alphabetical?)
* verify plugins can utilize other classes
  ** Can plugins use Grapes ?
* daemonize
* instructions, setup, install

* let plugins react on the bot's own messages (i.e sent by other plugins)... this might get verbose but... well...let's try ?
  * use a different callback so plugins can react differently

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

* Check Jenkins' bots:
  https://wiki.jenkins-ci.org/display/JENKINS/IRC+Bot
  https://github.com/jenkinsci/ircbot-plugin
  https://github.com/jenkinsci/backend-ircbot

Plugins to write
----------------

* log
* meetbot (with +voice)
* jira info
* "shut up" (bot will be silent until his name is said)
  -- is this still relevant ? every other plugin would need to "check" if they can talk ??
  -- perhaps make it a meta plugin that is able to enable/disable other plugins ?
* wait for user (bot will notice you when user join and/or send a message to the user)
