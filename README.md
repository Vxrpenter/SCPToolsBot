<div align="center">
  <img src="https://github.com/user-attachments/assets/6529281b-d5ef-46ea-b0f2-7c63e1cdcdd5" height="240" width="200" alt="ScpToolsBot" title="ScpToolsBot"/>

  <a href="https://github.com/Vxrpenter/SCPToolsBot/releases"><img src="https://img.shields.io/github/v/release/Vxrpenter/SCPToolsBot?include_prereleases&sort=date&display_name=tag&style=for-the-badge&label=LATEST%20RELEASE&color=%23c9631f"/></a>&nbsp;
  ![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/vxrpenter/ScpToolsBot/total?style=for-the-badge&color=%23c9631f)&nbsp;

  | Languages                                                                                                                                                                                                                                                                                    | Dependencies                                                                                                                                                                                                                                                                                                                                                                                                                                                              | Compatibility                                                                                                                   |
  |----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
  | <img src="https://github.com/devicons/devicon/blob/master/icons/java/java-original.svg" title="Java" alt="Java" width="40" height="40"/>&nbsp;<img src="https://github.com/devicons/devicon/blob/master/icons/yaml/yaml-original.svg" title="yaml" alt="yaml" width="40" height="40"/>&nbsp; | <img src="https://raw.githubusercontent.com/discord-jda/JDA/refs/heads/assets/assets/readme/logo.png" title="jda" alt="jda" width="40" height="40"/>&nbsp;<img src="https://www.apache.org/foundation/press/kit/feather.png" title="apachecommons" alt="apachecommons" width="40" height="40"/>&nbsp;<img src="https://creazilla-store.fra1.digitaloceanspaces.com/icons/3257055/file-type-sqlite-icon-md.png" title="sqlite" alt="sqlite" width="50" height="50"/>&nbsp; | <a href="https://cedmod.nl/"><img src="https://avatars.githubusercontent.com/u/64701232?s=48&v=4" title="cedmod" alt="cedmod" width="50" height="50"/></a>&nbsp; |
  
</div>

<div align="center">
  <h1>Refactor Information</h1>

  
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg" width="100" height="100"/><img src="https://img.icons8.com/?size=100&id=11759&format=png&color=FFFFFF" width="100" height="100"/><img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/kotlin/kotlin-original.svg" width="100" height="100"/>

  ## Progress - `5%`

  ![GitHub commit activity (branch)](https://img.shields.io/github/commit-activity/w/Vxrpenter/SCPToolsBot/kotlin_refactor?style=for-the-badge&color=%239141ac&link=https%3A%2F%2Fgithub.com%2FVxrpenter%2FSCPToolsBot%2Ftree%2Fkotlin_refactor&logo=kotlin)
  ![GitHub branch status](https://img.shields.io/github/checks-status/Vxrpenter/SCPToolsBot/kotlin_refactor?style=for-the-badge&logo=kotlin&color=%239141ac)


  <h4>SCPToolsBot is getting a full kotlin refactor. This means that there will be no more content updates until the refactor is done. All issues that are created will be fixed normally but there will be no new features.</h4>
</div>

> [!TIP]
  > If you want to keep up with updates then you can look at [refactor branch](https://github.com/Vxrpenter/SCPToolsBot/tree/kotlin_refactor) where new commits and changes are pushed to.
  >
  > The old java version will be maintained on the [java branch](https://github.com/Vxrpenter/SCPToolsBot/tree/java-old) until the refactor is complete

---

## What is ScpToolsBot
ScpToolsBot is a discord bot application written in Java for Scp Secret Laboratory discord servers. It includes support functions, cedmod api syncing including community and team management features

If you need any more information check out the [wiki](https://github.com/Vxrpenter/SCPToolsBot/wiki)

## Feature Overview
Currently the bot comes with:
- Support system with unban file
- SL ruleparsing
- Text translation for features
- Configurable commands
- Notice of Departure for staff
- Automatic notice of departure runout detection
- automatic applying of playtime roles with the regular feature

More coming soon, look [here](https://github.com/Vxrpenter/SCPToolsBot/wiki/Feature-Overview) for more complete overview

## Installation
This bot application should be installed on a alltime running server with good internet connection to handle requests fast and be online all the time

1. Download the `.jar` file from the latest **STABLE** release
2. Install `JDK-23` or higher on your machine
3. Run the jar using `java -jar <filename>`
4. The process will automatically crash and create two new folders called `configs` and `translations`. Go into the `configs` folder and edit the `config.yml` file
5. After putting in required info start the bot again and it should be running

Get a full guide [here](https://github.com/Vxrpenter/SCPToolsBot/wiki/Installation)
## Handling Issues
**Bot not starting, why?**
- Outdated JDK, install higher version
- Missing token or guild ID in config

---

**Got error: _"Couldn't commence HTTP request to get banID, is the user banned?"_**

This means that no banId could be found meaning that the user isn't banned

---

**Got error: _"Unbanning of <ID> ID failed, does it exist?"_**

This means that the unban process failed because the user is not banned/the ban id is invalid and does not exist

Get a full guide [here](https://github.com/Vxrpenter/SCPToolsBot/wiki/Issue-Handling)

---

> [!NOTE]
> If you are getting an error, not stated in this documentation and are not able to fix it, please create an issue so the issue can be resolved

## Star Chart
[![Stargazers over time](https://starchart.cc/Vxrpenter/SCPToolsBot.svg?variant=dark)](https://starchart.cc/Vxrpenter/SCPToolsBot)

## Thank you's
- Thank you to [ced777ric](https://github.com/ced777ric) for helping me with the cedmod api
