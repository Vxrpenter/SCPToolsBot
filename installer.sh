#!/bin/bash

#
# Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
#
# Licenced under the MIT License, any non-license compliant usage of this file(s) content
# is prohibited. If you did not receive a copy of the license with this file, you
# may obtain the license at
#
#  https://mit-license.org/
#
# This software may be used commercially if the usage is license compliant. The software
# is provided without any sort of WARRANTY, and the authors cannot be held liable for
# any form of claim, damages or other liabilities.
#
# Note: This is no legal advice, please read the license conditions
#

#
# Everything with comment has to be worked on
#

# Installation Values
export filename="NULL"
export installType=""
export repo="https://github.com/Vxrpenter/SCPToolsBot"
export version="1.1.1"
export configPath=""

# Jar Installation
export versionLink="${repo}/releases/download/v${version}/SCP_Tools-${version}.jar"
export filename="SCP_Tools-${version}.jar"

# Base Configuration Values
export botToken=""
export botSecret=""
export botGuildId=""
export botLanguage="en_US"

# Webserver Configuration Values
export webServerActive=false
export webServerPort=80
export webServerRedirectUri="/auth/discord/redirect"
export webServerUri=""

# Cedmod Configuration Values
export cedmodActive=false
export cedmodInstanceUrl=""
export cedmodApiKey=""

echo "
  ______     ______     ______      ______   ______     ______     __         ______
 /\  ___\   /\  ___\   /\  == \    /\__  _\ /\  __ \   /\  __ \   /\ \       /\  ___\\
 \ \___  \  \ \ \____  \ \  _-/    \/_/\ \/ \ \ \/\ \  \ \ \/\ \  \ \ \____  \ \___  \\
  \/\_____\  \ \_____\  \ \_\         \ \_\  \ \_____\  \ \_____\  \ \_____\  \/\_____\\
   \/_____/   \/_____/   \/_/          \/_/   \/_____/   \/_____/   \/_____/   \/_____/

Copyright (c) 2024 Vxrpenter
"


# Basic Functions
function confirm() {
  read -rp ":: Do you wish to proceed? [Y/n]: " response
  case $response in
    [Nn]* )
        echo "Stopping installation, exiting..."
        exit
  esac
}

# Root Check
if [ "$UID" -ne "0" ]; then
    echo "This script needs to be run as root"
    exit 9
fi

# Java Checker
#
# This part of the script is based of on this post on stackoverflow https://stackoverflow.com/questions/7334754/correct-way-to-check-java-version-from-bash-script/7335524#7335524
if type -p java > /dev/null 2>&1; then
    echo "Found java in executable PATH"
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo "Found java executable in JAVA_HOME"
    _java="$JAVA_HOME/bin/java"
else
    echo "No java installation found, please install java 22 or higher using your distributions package manager"
fi

if [[ "$_java" ]]; then
    jarVersion=$("$_java" -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
    if [[ "$jarVersion" -lt 22 ]]; then
        echo "Java version lower than 22, please upgrade your java installation"
        exit
    fi
fi
echo "Correct java version for bot is installed, proceeding with installation..."
echo ""

function jarInstall() {
  curl -LO $versionLink

  echo ""
  echo "Executing jar-file to generate configuration files..."

  sudo java -jar $filename > /dev/null 2>&1
  configPath=$(pwd)
}

function dockerInstall() {
  echo ""
  read -rp ":: Please enter the config bind path? Default [/var/lib/ScpTools/]: " response
  case $response in
    "" )
      echo "Using default config bind path: /var/lib/ScpTools/"
      configPath="/var/lib/ScpTools/"
    ;;
    * )
      echo "Using config bind path: $response"
      configPath=$response
  esac

  echo ""
  echo "Cloning repository branch $version"
  git clone --depth 1 --branch $version $repo > /dev/null 2>&1
  cd SCPToolsBot/ || exit > /dev/null 2>&1

  echo ""
  echo "Setting config bind path in .env file"
  env="CONFIG_PATH = ${configPath}"
  echo "$env" > ".env"

  systemctl start docker > /dev/null 2>&1

  sudo docker compose up > /dev/null 2>&1 & pid=$!

  # This spinner code has been mostly taken from stackoverflow - https://stackoverflow.com/a/12498305
  spin[0]="⠁"
  spin[1]="⠂"
  spin[2]="⠄"
  spin[3]="⡀"
  spin[4]="⢀"
  spin[5]="⠠"
  spin[6]="⠐"
  spin[7]="⠈"

  echo ""
  echo -en "Running docker compose up, this can take a few minutes, don't stop your machine: ${spin[0]}"

  tput civis
  while kill -0 $pid 2> /dev/null; do
      for i in "${spin[@]}"; do
          echo -ne "\b$i"


          sleep 0.1
      done
  done
  tput cnorm

  echo "Concluded compose, shutting down..."
  sudo docker compose down > /dev/null 2>&1
}

function chooseInstall() {
  echo ""
  echo "What install do you want to pursue?"
  PS3=":: Choose install method: "
  select installOption in Jar Docker
  do
    installType=$installOption
    break
  done

  case $installType in
    Jar )
      jarInstall
    ;;
    Docker )
      dockerInstall
    ;;
    * )
      echo "This is no install method, please repeat..."
      chooseInstall
  esac
}

echo "Do you want to proceed with the installation?"
confirm

chooseInstall

echo ""
echo "Bot process has concluded, do you want to do a complete installation with configuration or end it here?"
confirm

# Token Configuration
echo ""
echo "The bot token can be obtained from the discord developer portal!"
function token() {
  read -rp ":: What is your bot token?: " tokenResponse

  read -rp ":: Please repeat token for confirmation: " response
  if [ "$tokenResponse" == "$response" ]; then
    botToken=$tokenResponse
  else
    echo "Tokens don't match, please try entering it again"
    token
  fi
}
token

# Secret Configuration
echo ""
echo "The bot secret can be obtained from the discord developer portal under the OAuth section!"
function secret() {
  read -rp ":: What is your bot secret?: " secretResponse

  read -rp ":: Please repeat secret for confirmation: " response
  if [ "$secretResponse" == "$response" ]; then
    botSecret=$secretResponse
  else
    echo "Secrets don't match, please try entering it again"
    token
  fi
}
secret

# Guild Configuration
echo ""
echo "The guild id can be obtained by activating developer mode and right clicking the server!"
function guild() {
  read -rp ":: What is your bot guildId?: " guildResponse

  read -rp ":: Please repeat id for confirmation: " response
  if [ "$guildResponse" == "$response" ]; then
    botGuildId=$guildResponse
  else
    echo "Id's don't match, please try entering it again"
    token
  fi
}
guild

# Language Configuration
echo ""
echo "What language preset do you want to use?"
PS3=":: Enter number of the language option: "
select option in English German
do
  language=$option
  break
done

case $language in
    English )
        botLanguage="en_US"
    ;;
    German )
        botLanguage="de_DE"
    ;;
    * )
        echo "This is no language, exiting..."
        exit
esac

# Webserver Configuration
function webserver() {
  # Port Configuration
  echo ""
  echo "What port do you want the webserver to run under?"
  read -rp ":: Enter port here:  " port

  webServerPort=$port

  echo ""
  echo "What firewall do you want to add the port to?"
  PS3=":: Add to firewall with: "
  select portOption in UFW SKIP
  do
    firewallType=$portOption
    break
  done

  case $firewallType in
    UFW )
      ufw allow "$webServerPort"
    ;;
    SKIP )
      echo "Skipping firewall setup, make sure to setup yourself to make webserver work"
    ;;
    * )
      echo "This is no firewall, exiting..."
      exit
  esac

  # Redirect Uri Configuration
  echo ""
  echo "Enter in a URI that you want the OAuth redirect to be at: "
  read -rp ":: Enter redirect here: [leave empty to go with default]: " redirect
  case $redirect in
    "" )
      echo "Using default $webServerRedirectUri"
    ;;
    * )
      webServerRedirectUri=$redirect
  esac

  # Uri Configuration
  echo ""
  echo "Enter Url of your server, ip or domain with 'http://' or 'https://'!"
  read -rp ":: Enter url: " uri

  webServerUri=$uri

  echo ""
  echo "Finished up webserver setup, proceeding with further configuration"
}

echo ""
echo "Do you want to set up webserver? This is used for OAuth requests and needed for some features."
function confirmWebserver() {
  read -rp ":: Do you wish to proceed? [y/N]: " response
  case $response in
    [Yy]* )
      webServerActive=true
      webserver
    ;;
    * )
      echo "Skipping webserver setup"
  esac
}
confirmWebserver

# CedMod Configuration
function cedmod() {
  # Instance Url Configuration
  echo ""
  echo "Enter in the url of your CedMod instance, including 'https'!"
  read -rp ":: Enter Url: " url
  cedmodInstanceÚrl=$url

  # Api Key Configuration
  echo ""
  echo "Enter in the api key of your CedMod instance"
  read -rp ":: Enter api key: " api
  cedmodApiKey=$api

  echo ""
  echo "Finished up CedMod setup, proceeding with further configuration"
}

echo ""
echo "Do you want to set up cedmod api integration? This is used for some features to request data from the server."
function confirmCedMod() {
  read -rp ":: Do you wish to proceed [y/N]: " response
  case $response in
    [Yy]* )
      cedmodActive=true
      cedmod
    ;;
    * )
      echo "Skipping CedMod setup"
  esac
}
confirmCedMod

# End of configuration States
echo ""
echo "Do you want to write the configured values to the config?"
confirm

#
# Here comes the YAML Parser and the configuration writer stuff
#
config="#                                                           ______     ______     ______      ______   ______     ______     __         ______
#                                                          /\  ___\   /\  ___\   /\  == \    /\__  _\ /\  __ \   /\  __ \   /\ \       /\  ___\\
#                                                          \ \___  \  \ \ \____  \ \  _-/    \/_/\ \/ \ \ \/\ \  \ \ \/\ \  \ \ \____  \ \___  \\
#                                                           \/\_____\  \ \_____\  \ \_\         \ \_\  \ \_____\  \ \_____\  \ \_____\  \/\_____\\
#                                                            \/_____/   \/_____/   \/_/          \/_/   \/_____/   \/_____/   \/_____/   \/_____/
#Copyright (c) 2024 Vxrpenter

# The token of your bot application, create one here https://discord.com/developers/
token: \"$botToken\"
# The client secret of your bot, get it from here https://discord.com/developers/ under OAuth section
client_secret: \"$botSecret\"
# Your server ID, you can get it by activating discord developer mode and right-clicking your server
guild_id: \"$botGuildId\"

# Which language should the bot use?
#Choose from these supported languages or duplicate one of the translation files and change it yourself
# available translations: [\"en_US\", \"de_DE\"]
load_translation: \"$botLanguage\"

# Activates debug logging, which gives you much more information on what the bot is doing.
debug: false
# ** Warning ** This feature is not recommended
#Displays all available information
advanced_debug: false

# Settings for the update checker
updates:
  # Activate to ignore all versions that contain the beta tag
  ignore_beta: false
  # Activate to ignore all versions that contain the alpha tag
  ignore_alpha: true

# The activity type of the bot, choose from the available list: [COMPETING, CUSTOM_STATUS, LISTENING, PLAYING, WATCHING]
activity_type: \"PLAYING\"
# The text that is being displayed in the activity
activity_content: \"/help\"

database:
  # Defines which datatype should be used, choose between [NONE, SQLITE]
  # ** WARNING ** if you set this to none, and no other database option is given, the process will crash on startup
  use_predefined_database_sets: \"SQLITE\"
  # ** WARNING ** When using custom, you may experience issues. This is a work in progress feature, so it is unstable, report problems on the GitHub page
  # Which type of custom db should be used: [SQLITE, MYSQL, POSTGRESQL, MARIADB]
  custom_type: \"\"
  # Here you can input a custom database url to connect to. Make sure to not include https:// or http://, just use the plain url for
  # database connection, e.g., < localhost:3306/test >
  custom_url: \"\"
  # Here you've input the custom db password
  custom_username: \"\"
  # Here you have to enter the password of your db session
  custom_password: \"\"

webserver:
  # Should the webserver be launched? This feature is only used for regulars
  active: $webServerActive
  # The port under which the webserver will be launched
  port: $webServerPort
  # What uri to start the webserver under
  redirect_uri: \"$webServerRedirectUri\"
  # Where should the redirect be?
  uri: \"$webServerUri\"

cedmod:
  # This activates the CedMod Api integration. This feature is only used for the following functions, only activated if you have these features in use: Regulars
  # CedMod Api is only available to users who request access from the CedMod team, ask on their discord for more information - https://discord.gg/p69SGfwxxm
  active: false
  # Include https://
  instance_url: \"\"
  # Put the plain API key here
  api_key: \"\"

# ** NOTICE ** Custom level functions are currently not supported because of technical complexity
# This is a work-in-progress compatibility implementation of the XP plugin by RowspannSCP https://github.com/RowpannSCP/XP. It is intended to be used with the
# regulars feature for providing additional goals for playtime roles. This feature is highly WIP and will probably take a bit longer to implement, as I have to
# inspect their code to find some necessary information. The current config options are subject to change.
XP:
  # This activates the XP integration. This feature is only used for the following functions, only activated if you have these features in use: Regulars
  active: false
  # ** WARNING ** LiteDB will not be supported in any coming version as it would be really challenging to implement, in the end, bringing no real advantage
  # The address of the database which the plugin uses. If the plugin and bot are running on the same DB, the bot will identify it and not connect to the database
  # a second time and just pull data from the existing connection. Make sure you give the bot's user enough rights to do so.
  # Example database address: < localhost:3306/test >
  database_address: \"\"
  # The username of the database user you want the bot to use
  database_user: \"\"
  # The password of the user you want the bot to use
  database_password: \"\"
  # ** WARNING ** To keep this feature as simple as possible, there will only be one auth type supported. Also, Northwood authtype is currently not supported
  # Which type of authorization table do you want to query from?
  # Available auth types are [STEAM, DISCORD]
  auth_type: \"\"
  # This affects the 'a' parameter in the standard level function. This is the standard function used by the XP plugin: -50 + sqrt((4 * xp / a) + 9800) / 2
  additional_parameter: 1

# ** WARNING ** For this setting to work, you need to activate the webserver.
# The webserver is generally just a redirect to receive data from the discord OAuth Api, so it
# should not interrupt any other web stuff running on your server.
verify:
  # Activates the verify feature, make sure you have configured webserver accordingly
  active: false
  # This link can be obtained from the oauth section on the discord developer portal. First, enter the redirect you entered in the webserver section exactly
  # as described here: <uri>:<port><redirect_uri>, e.g. http://localhost:80/auth/discord/redirect. After entering the redirect, click on save to proceed with the
  # setup. You now have to scroll down to the 'OAuth2 URL Generator' where you need to click on 2 options, 'identify' and 'connections', after that scroll down
  # and select your redirect for the redirect url. Now you can copy the generated url into your clipboard. Paste it in here.
  oauth_link: \"\"
  # Which channel should verify logs be sent to?
  verify_log_channel: \"\"

notice_of_departure:
  # Activates the notice of departure feature
  active: false
  # The formatting that is used to format the notice of departure dates
  date_formatting: \"dd.MM.yyyy\"
  # Which channel should the form be sent to, to be accepted by moderators?
  decision_channel_id: \"\"
  # Which channel should the notice messages be sent to?
  notice_channel_id: \"\"
  # List of role's that are able to accept/dismiss/revoke notices (INPUT ID'S ONLY)
  roles_access_notices: []
  # Put the following in the type: [NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]
  check_type: \"HOURS\"
  # The rate at that the notices are checked.
  check_rate: 1

regulars:
  # Activates regulars, make sure you have correctly configured the regular's configs, as well as activated the necessary compatibility settings
  active: false
  # Should example configuration be created (deactivate if you want to delete the example config)
  create_example_configuration: true
  # Should the bot only load from specified folder within the /regulars/ folder?
  only_load_certain_folder: false
  # Specify which folders should be specified if the option above is active
  only_load_folders: []
"

echo "$config" > "${configPath}/SCPToolsBot/configs/config.yml"

echo ""
echo "Installation wrapped up, existing..."
exit
