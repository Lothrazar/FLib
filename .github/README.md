# Future Library (FLIB)

A Library/core mod built on Minecraft Java.
Current versions are built on NeoForge.

[![CurseForge](https://img.shields.io/badge/CurseForge-F16436?style=flat-square&logo=curseforge&logoColor=white)](https://www.curseforge.com/minecraft/mc-mods/flib)
[![Modrinth](https://img.shields.io/badge/Modrinth-1bd96a?style=flat-square&logo=modrinth&logoColor=white)](https://modrinth.com/mod/flib)



[![Curseforge](http://cf.way2muchnoise.eu/661261.svg)](https://www.curseforge.com/minecraft/mc-mods/flib) 
[![](http://cf.way2muchnoise.eu/versions/661261.svg)](https://www.curseforge.com/minecraft/mc-mods/flib)

[![Build](https://github.com/Lothrazar/FLib/actions/workflows/build.yml/badge.svg)](https://github.com/Lothrazar/FLib/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![socials](https://img.shields.io/badge/more-links-ff69b4.svg)](https://allmylinks.com/lothrazar)
[![Support](https://img.shields.io/badge/Patreon-Support-orange.svg?logo=Patreon)](https://www.patreon.com/Lothrazar)


# Currently used by

- https://github.com/Lothrazar/ForgeTemplate
- https://github.com/Lothrazar/Scraps
- https://github.com/Lothrazar/TinyLightBulbs
- https://github.com/Lothrazar/ElementaryOres
- https://github.com/Lothrazar/SaplingGrowthControl
- https://github.com/Lothrazar/CustomGameRules
- https://github.com/Lothrazar/BlockyDoors

And more!

## Adding as a dependency
Use the libs folder in the repositories section of your build scripts as follows
```
  flatDir {
      dir 'libs'
  }
```
And then use the blank keyword to add the version inside your dependencies block
```
    implementation "local.mods:flib:${minecraft_version}-${flib_version}"
```
And then your gradle.properties file will get the version number set that you are using
```
flib_version=0.0.1
```
Inside your minecraft - runs - client block in build.gradle, you will need this to avoid mixin errors
```
 property 'mixin.env.disableRefMap', 'true'
```

If you want to rely on a remote maven server instead of your local libs directory, then with the repository
```
  maven { url = 'https://www.cursemaven.com'  }
```
You need to replace the latest file id (from the url of the curseforge file) in the right hand side in the dependencies section
```
    implementation fg.deobf("curse.maven:flib-661261:0000000")
```
And then add a dependency at the bottom of your META-INF/mods.toml page
```
[[dependencies.examplemod]]
    modId="flib"
    type="required" # was mandatory=true
    versionRange="[1.0.0,)"
    ordering="NONE"
    side="BOTH"
```
