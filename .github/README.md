# Library

A Library/core mod built on the Minecraft Forge API [https://files.minecraftforge.net/](https://files.minecraftforge.net)




[![](http://cf.way2muchnoise.eu/0.svg)](https://www.curseforge.com/minecraft/mc-mods/flib) 
[![](http://cf.way2muchnoise.eu/versions/0.svg)](https://www.curseforge.com/minecraft/mc-mods/flib)

[![Build](https://github.com/Lothrazar/FLib/actions/workflows/build.yml/badge.svg)](https://github.com/Lothrazar/FLib/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Discord](https://img.shields.io/discord/749302798797242449.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/uWZ3jf56fV)
[![Support](https://img.shields.io/badge/Patreon-Support-orange.svg?logo=Patreon)](https://www.patreon.com/Lothrazar)
[![Twitter Badge](https://img.shields.io/badge/contact-twitter-blue.svg)](https://twitter.com/lothrazar)
[![links](https://img.shields.io/badge/more-links-ff69b4.svg)](https://allmylinks.com/lothrazar)

# Currently used by

- https://github.com/Lothrazar/ForgeTemplate
- https://github.com/Lothrazar/Scraps

## Adding as a dependency
Use the libs folder in the repositories section of your build scripts as follows
```
  flatDir {
      dir 'libs'
  }
```
And then use the blank keyword to add the version as a dependency
```
    implementation fg.deobf("blank:flib-${mc_version}:${flib_version}")
```
And then your gradle.properties file will get the version number set that you are using
```
flib_version=0.0.1
```
If you want to rely on a remote maven server instead of your local libs directory, then with the repository
```
  maven { url = 'https://www.cursemaven.com'  }
```
You just need to replace the latest file id (from the url of the curseforge file) in the right hand side in the dependencies section
```
    implementation fg.deobf("curse.maven:cyclic-239286:3249448")
```

For examples of these setups, see this setup https://github.com/Lothrazar/ForgeTemplate/blob/trunk/1.18/build.gradle#L113

