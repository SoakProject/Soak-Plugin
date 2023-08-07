# Soak-Plugin

Run Bukkit/Spigot/Paper on a Sponge server without hitting compatibility issues with Forge or Sponge.

## Compatibility list

Find the plugins compatiblely level on our issues board above.

### Estimated compatibility

currently, Soak is still in the early days and as a result, do not expect many plugins to be supported

## Downloads

Soak will originally release with the ability to only run a single plugin to battle test it for those plugins, but when its ready it will be downloadable from Sponge's Ore  

### I want Soak for all plugins

You can gain a fully copy of Soak by either

- building Soak yourself
- Press the actions tab above and download the latest build (github account required)

## How this works

Soak maps Paper methods into Sponge API methods

## So Pore?

in short, yes but newer

Pore was a Sponge plugin that allowed Bukkit plugins to run on older Sponge versions.
Soak is a Sponge plugin that allows Bukkit plugins to run on Sponge API 8+

Pore ended for a (few reasons)[https://caseif.blog/content/post.php?id=41], here are some

### Sponge has become increasing different to Sponge

this was true around the 1.12.2 time, however Minecraft 1.13 came out and rewrote Sponge a lot and Bukkit a fair bit too. As a result, both share similarities, allowing for easy mapping.

### Many plugins wont work

The Spigot team are well aware of how many plugins are bypassing the Bukkit api. While the original attempts to move developers to use just Bukkit API was to mention to use it in the small print of a announcement. The Spigot team have started to add common methods that developers needed such as the ability to get the players ping though the Bukkit api. This has lead to more and more plugins using pure Bukkit API code, meaning that Soak will be able to simulate it

### Many plugins that do work, wont work with Forge mods

This is still true due to Bukkit's insistence on fixed lists. However Bukkit now supports Datapacks which can add items to these fixed lists which breaks the rules of Java, but plugins expect these lists to expand

### Sponge is about new beginnings

Soak will only simulate newer plugins in attempt for Sponge being more attractive option for Admins. I must rather see developers use native Sponge code, but developers tend to target where the users are, and Admins tend to use platforms where the developers are. By making Sponge more attractive to Admins, it will hopefully help break the cycle that Bukkit made.


