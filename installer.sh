#!/bin/bash

#
# Everything with comment has to be worked on
#

# Installation Values
export filename="NULL"
export versionLink="https://github.com/Vxrpenter/SCPToolsBot/releases/download/v.1.0.1/SCP_Tools-1.0.1.jar"

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
  read -rp ":: Do you wish to proceed? [Y/N]: " response
  case $response in
    [Yy]* )

    ;;
    [Nn]* )
        echo "Stopping installation, exiting..."
        exit
    ;;
    * )
        echo "No clear input given, try again"
        confirm
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
if type -p java; then
    echo "Found java in executable PATH"
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo "Found java executable in JAVA_HOME"
    _java="$JAVA_HOME/bin/java"
else
    echo "No java installation found, please install java 22 or higher using your distributions package manager"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
    if [[ "$version" -lt 22 ]]; then
        echo "Java version lower than 22, please upgrade your java installation"
        exit
    fi
fi
echo "Correct java version for bot is installed, proceeding with installation..."
echo ""

echo "Do you want to proceed with the installation?"
confirm

wget $versionLink
filename="SCP_Tools-1.0.0.jar"

# First Bot Startup
echo ""
echo "Executing jar-file to generate configuration files..."

sudo java -jar $filename

echo ""
echo "Bot process has been ended, do you want to do a complete installation with configuration or end it here?"
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
    echo "Tokens don't match, please try entering it again"
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
    echo "Tokens don't match, please try entering it again"
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

# Database Configuration
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
      # Put UFW command here
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
  read -rp ":: Do you wish to proceed? [Y/N]: " response
  case $response in
    [Yy]* )
      webServerActive=true
      webserver
    ;;
    [Nn]* )
        echo "Skipping webserver setup"
    ;;
    * )
        echo "No clear input given, try again"
        confirmWebserver
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
  read -rp ":: Do you wish to proceed [Y/N]: " response
  case $response in
    [Yy]* )
      cedmodActive=true
      cedmod
    ;;
    [Nn]* )
      echo "Skipping CedMod setup"
    ;;
    * )
      echo "No clear input given, try again"
      confirmCedMod
  esac
}
confirmCedMod

# End of configuration States
echo ""
echo "The configuration is done, displaying all configured values"

echo ""
echo " - Bot Configurations - "
echo "Token: $botToken"
echo "Secret: $botSecret"
echo "GuildId: $botGuildId"
echo "Language: $botLanguage"

echo ""
echo " - Webserver Configurations - "
if [ $webServerActive == true ]; then
  echo "Webserver Active: $webServerActive"
  echo "Webserver Port: $webServerPort"
  echo "Webserver Redirect Uri: $webServerRedirectUri"
  echo "Webserver Uri: $webServerUri"
fi

echo ""
echo " - CedMod Configurations - "
if [ $cedmodActive == true ]; then
  echo "CedMod Active: $cedmodActive"
  echo "CedMod Instance Url: $cedmodInstanceUrl"
  echo "CedMod Api Key: $cedmodApiKey"
fi

echo ""
echo "Do you want to write them to the configuration?"
confirm

#
# Here comes the YAML Parser and the configuration writer stuff
#
workingDir=$(pwd)
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
  # Here you can input a custom database url to connect to. This will only work when the setting is predefined to NONE
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
  # This activates the CedMod Api integration. This feature is only used for the following functions, only activate if you have these features in use: Regulars
  # CedMod Api is only available to users who request access from the CedMod team, ask on their discord for more information - https://discord.gg/p69SGfwxxm
  active: $cedmodActive
  # Include https://
  instance_url: \"$cedmodInstanceUrl\"
  # Put the plain API key here
  api_key: \"$cedmodApiKey\"

# ** WARNING ** For this setting to work, you need to activate the webserver.
#The webserver is generally just a redirect to receive data from the discord OAuth Api, so it
# should not interrupt any other web stuff running on your server.
verify:
  # This link can be obtained from the oauth section on the discord developer portal. First, enter the redirect you entered in the webserver section exactly
  # as described here: <uri>:<port><redirect_uri>, e.g. http://localhost:80/auth/discord/redirect. After entering the redirect, click on save to proceed with the
  # setup. You now have to scroll down to the 'OAuth2 URL Generator' where you need to click on 2 options, 'identify' and 'connections', after that scroll down
  # and select your redirect for the redirect url. Now you can copy the generated url into your clipboard. Paste it in here.
  oauth_link: \"\"
  # Which channel should verify logs be sent to?
  verify_log_channel: \"\"

notice_of_departure:
  # Which channel should the form be sent to be accepted by moderators?
  decision_channel_id: \"\"
  # Which channel should the departure timekeeping messages be sent to?
  notice_channel_id: \"\"
  # List of role's that are able to accept/dismiss/revoke notices (INPUT ID'S ONLY)
  roles_access_notices: []
  # Put the following in the type: [NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]
  check_type: \"HOURS\"
  # The rate at that the notices are checked.
  check_rate: 1

regulars:
  # Should example configuration be created (deactivate if you want to delete the example config)
  create_example_configuration: true
  # Should the bot only load from specified folder within the /regulars/ folder?
  only_load_certain_folder: false
  # Specify which folders should be specified if the option above is active
  only_load_folders: []
"

echo "$config" > "$workingDir/SCPToolsBot/configs/config.yml"

echo ""
echo "Installation wrapped up, existing..."
exit