# mcpingtest

A Minecraft plugin allowing you to set up a server for testing in-game latency.  
Note: there's also a standalone server that does the same thing built on top of Minestom, currently supports 1.19.3.

## Why?

The reason for both versions to exist is simple: pinging the server host or sending a HTTP request to it isn't as accurate as testing latency on top of protocol that will be used. 

## How? 

This version utilizes the [Player#getPing](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Player.html#getPing()) method in the Spigot API. It gives an estimate of player's ping. This is why ping can be 0 right after joining the server. 

## Usage

Currently, the plugin requires version 1.20 ~~or above~~. I might change that requirement at some point.
You can start using the plugin on your server pretty quickly:
1. Download the latest build from [releases](https://github.com/bartosz11/mcpingtest/releases/latest).
2. Put the JAR file to the plugins directory.
3. Restart the server.
4. (optional) change the configuration in mcpingtest/config.yml.
5. (optional) restart the server after changing the config.  

To start a ping test, use the command ``/pingtest start``. To stop it, use command ``/pingtest stop``.
