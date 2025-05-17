<div align="center">
  <img src="https://github.com/user-attachments/assets/686226b2-a520-47cc-876e-73fd35f50a06" height="240" width="200" alt="ScpToolsBot" title="ScpToolsBot"/>

  <a href="https://github.com/Vxrpenter/SCPToolsBot/releases"><img src="https://img.shields.io/github/v/release/Vxrpenter/SCPToolsBot?include_prereleases&logo=github&logoSize=amg&logoColor=%2300DC82&labelColor=333834&sort=date&display_name=tag&style=for-the-badge&label=LATEST%20RELEASE&color=white"/></a>&nbsp;
  <img src="https://img.shields.io/github/downloads/Vxrpenter/SCPToolsBot/total?style=for-the-badge&logo=gitbook&logoSize=amg&label=Downloads&labelColor=333834&logoColor=%2300DC82&color=white"/>&nbsp;
  
  <a href="https://github.com/Vxrpenter/SCPToolsBot/blob/master/LICENSE"><img src="https://img.shields.io/github/license/Vxrpenter/SCPToolsBot?style=for-the-badge&logo=amazoniam&logoSize=amg&logoColor=%2300DC82&label=Licenced%20Under&labelColor=333834&color=white"/></a>&nbsp;

  | Languages                                                                                                                                                                                                                                                                                                                                                                                                                                        | Main Dependencies                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | Compatibility                                                                                                                                                                                                                                                                                                                                                |
  |--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
  | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/kotlin/kotlin-original.svg" title="Kotlin" alt="Kotlin" width="40" height="40"/><img src="https://github.com/devicons/devicon/blob/master/icons/java/java-original.svg" title="Java" alt="Java" width="40" height="40"/>&nbsp;<img src="https://github.com/devicons/devicon/blob/master/icons/yaml/yaml-original.svg" title="yaml" alt="yaml" width="40" height="40"/>&nbsp; | <img src="https://raw.githubusercontent.com/discord-jda/JDA/refs/heads/assets/assets/readme/logo.png" title="jda" alt="jda" width="40" height="40"/>&nbsp;<img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jetbrains/jetbrains-original.svg" alt="Jetbrains" title="Jetbrains" width="40" height="40"/><img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/sqlite/sqlite-original.svg" title="sqlite" alt="sqlite" width="50" height="50"/>&nbsp;                                                                                                                       | <a href="https://cedmod.nl/"><img src="https://avatars.githubusercontent.com/u/64701232?s=48&v=4" title="cedmod" alt="cedmod" width="50" height="50"/></a>&nbsp;<a href="https://github.com/RowpannSCP/XP"><img src="https://github.com/user-attachments/assets/3dad76d5-2344-4b1f-90dd-3f83a9c74b30" title="xp" alt="xp" width="40" height="40"/></a>&nbsp; |
  | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/json/json-plain.svg" title="Json" alt="Json" width="40" height="40"/>                                                                                                                                                                                                                                                                                                        | <img src="https://raw.githubusercontent.com/JetBrains/Exposed/refs/heads/main/docs/logo-dark.png" title="Exposed" alt="Exposed" width="100" height="40"/>&nbsp;<a href="https://github.com/Vxrpenter/SecretLab-Api-Tools"><img src="https://camo.githubusercontent.com/f4568c03aa9a1c0574a0e3fb8c71eed4d804ad4fecdb39f313995eaa2cdba9d5/68747470733a2f2f7777772e6769616e74626f6d622e636f6d2f612f75706c6f6164732f7363616c655f6d656469756d2f33352f3335393536302f323936303933392d6c6f676f5f7374796c697a65642e706e67" title="SecretLab-Api-Tools" alt="SecretLab-Api-Tools" width="40" height="40"/></a>&nbsp; |                                                                                                                                                                                                                                                                                                                                                              |
</div>

---

## What is ScpToolsBot
ScpTools Bot is an application
to enhance your Scp Secret Laboratory server
by providing quality-of-life features in combination with moderation and team management tools.
It also integrates with plugins like cedmod
to build on already existing infrastructure and to use features they offer.

For more information check out the [wiki](https://github.com/Vxrpenter/SCPToolsBot/wiki)

## What Features are included

| Feature                        | What is this                                                                                                                                                                                           |
|--------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Fully customizable translation | All text can be changed to anything you can imagine, there are even color codes and other utility functions for even more customizability                                                              |
| Configuration options          | Many features can be changed to personal liking, e.g. disabling a certain part of it or enabling something else that is not active by default                                                          |
| Status bot cluster             | A cluster of bots that show the player count of your server. They also send connection messages to a channel that was configured by the sysadmin, that show if the server can currently not be reached |
| Custom Support System          | Support system that is fitted in with scp secret laboratory, add new pre configured category or let the user decide. Sync actions in tickets with actions on the server to make operations more smooth |
| Notice of Departures           | Staff can file a notice of departure that marks a timeframe they will not be available. These are also checked periodically to ensure that everyone gets notified if the notice runs out               |
| Fully configurable commands    | You can change command names, permissions, options, choices and subcommands without messing with the functionality.                                                                                    |
| Playtime role syncing          | Give your players a reward by allowing them to earn roles for certain playtime counts. They automatically get synced with their ingame time and updated when reaching a new milestone                  |
| Cedmod synchronization         | You can link your cedmod instance by utilizing CedMod's api user feature. The bot will be able to interact with the panel and use it's features according to your desires                              |

For more indepth explanation see the [wiki](https://github.com/Vxrpenter/SCPToolsBot/wiki/Feature-Overview)

## Installation
> [!NOTE]
> Bots have to run all the time to be effective, so make sure to run it on a machine that is online 24/7

You can easily install the bot by downloading and installing the `installer.sh` from the latest release. For more installation info you can look into the [installation guide](https://github.com/Vxrpenter/SCPToolsBot/wiki/Installation) which covers docker installation etc.

```sh
chmod +x installer.sh

sudo ./installer.sh
```

## Build from Source
```sh
git clone https://github.com/Vxrpenter/SCPToolsBot

cd SCPToolsBot

chmod +x gradlew
./gradlew shadowjar
```

## Handling Issues
If you're running into any issues,
please first check the [wiki](https://github.com/Vxrpenter/SCPToolsBot/wiki/Issue-Handling) for help.
If you can't fix the issue, create an issue here so we're able to review it and find possible bugs. If you have any questions about the working or plans of development contact `@vxrpenter` on discord

---

## Build information
Full releases are marked as such in the release.
If a release contains the `alpha` tag, it is experimental and features and more have not been tested.

Releases containing the `beta` tag are mostly tested but could still be considered unstable.

Releases will be marked as stable if they're thought to run well after testing.
If a release is marked as unstable, it might not run well or produce bugs, for example, memory leaks

When a new build is released, it might take some time until the wiki is updated

Changelogs only contain the most important information,
and small one-line changes will not be included if they don't have a major effect on stability and/or performance

## Licencing
This project, as stated above, is licenced under the MIT license.
It can be modified and used commercially.
You can distribute it as you like.
I'm not liable for anything done with it, and no warranty is given

> [!IMPORTANT]
> When using code from this repo, you have to add a License and copyright notice.

This license is not in place to annoy anyone but to protect the work, passion and time that has gone into this project
> This is no legal advice

## Special Thanks
- Special thanks goes to [ced777ric](https://github.com/ced777ric) who helped me a lot with the cedmod api integration, especially when trying to find the specific endpoints use case or usability
- Also to [SeekEDstroy](https://github.com/SeekEDstroy) for help with user id linking and for pointing out some bugs
---

## Star History
Thank you a lot for starring this repo. It is really helpful and makes me happy to work on this project. 

If you like this project, you may consider sharing it with others Either way, thank you really much
[![Stargazers over time](https://starchart.cc/Vxrpenter/SCPToolsBot.svg?variant=adaptive)](https://starchart.cc/Vxrpenter/SCPToolsBot)

---

<div align="center">
  First release <a href="https://github.com/Vxrpenter/SCPToolsBot/releases/tag/v.0.1.0">0.1.0</a> released on 15. Oct. 2024

  Repo created in 2024 ‎ ‎ ‎ ‎ This is the kotlin refactored version

  Licenced under MIT since made public
</div>
