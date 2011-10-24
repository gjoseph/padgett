Padgett is an IRC robot written in Groovy, exposing a simple plugin system.
Every bit of functionality in Padgett is written as a plugin, and plugins can be deployed or reloaded without restarting the bot.

Padgett - the name
==================

(Middle English) little page; (Middle English) server, helper
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
* build, single jar
* instructions, setup, install
* don't callback when bot.name=user.nickname?

* configuration:
  Should there be one "config" object ?
  Should each plugin look at the same set of arbitrary properties ?
  Should each plugin simply have its own configuration ? (should it be embedded in the plugin code, make it non distributable?)
  Should each plugin register its config items in the shared config object ?

  Should plugins "talk" to each other ?
  (for example they could share one Jira::proxy instance)

* plugin name ?
* plugin dependencies ? (could be useful, or even needed, for proper reload/restart)

* save plugin states ?

Plugins to write
----------------
* config, connect, join channels
* log
* meetbot (with +voice)
* jira info
* "shut up" (bot will be silent until his name is said)
* wait for user (bot will notice you when user join and/or send a message to the user)

Development
===========
Use ngircd as server:
 * sudo port install ngircd
 * sudo cp /opt/local/etc/ngircd.conf.sample /opt/local/etc/ngircd.conf
 * ngircd -n

Use weechat as client - easy enough to start multiple instances
 * sudo port install weechat
 * vi ~/.weechat/
 * weechat-curse
 * cp -R ~/.weechat/ ~/.weechat2/
 * weechat-curses -d ~/.weechat2/
 * # rince and repeat for more clients/configs

Run from source
---------------
mvn clean compile && mvn dependency:build-classpath -Dmdep.outputFile=mvncp.out && java -cp `cat mvncp.out`:target/classes/ net.incongru.padgett.Main

