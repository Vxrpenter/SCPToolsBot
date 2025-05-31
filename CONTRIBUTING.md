# Introduction
Before proposing any changes, think about the changes importance and check if there is not already a pull-request or issue that discusses it.

Also note that this project has a code of conduct, so make sure to follow it.

# Ground Rules
There are some rules ground rules you'll need to follow before making/proposing any changes.

1. Ensure compatibility with all installation methods [e.g. Docker]
2. Make sure your changes can be compiled without any errors/warnings
3. Only change code that handles internal functions [e.g. configs, translations, updaing etc.] when you absolutely need to
4. Check if your changes have already been proposed in a similar form through a pull request or issue
5. Don't import random dependencies without making sure they are from trusted sources and don't contain any critical vulnerabilities
6. Do not use AI to write any code, comments, pull requests, issues etc.

# Getting Started
If you want to implement a feature / fix a bug or propose any other big changes, that cannot be changed with a few lines, follow this:

1. Fork the repository [here](https://github.com/Vxrpenter/SCPToolsBot/fork)
2. Push your changes to the fork
3. Create a pull request and propose your changes (make sure to follow the template)

# Review Process
When you propose changes, they will be reviewed and you will probably be asked to change some of the implemented code. Examples of these changes could be:
* Remove unnecessary code/functions etc.
* Remove dependencies that you implemented
* [...]

You will also be asked why you want these changes to be implemented and what exact benefit they provide
* What is the purpose of this feature?
* Couldn't you use [...] instead of this?
* What is the purpose of function [...]?
* [...]

NOTE: If your changes do not pass the automatic changes, your pull request will not be reviewed until you have fixed these compilation errors.

# Naming conventions
Please follow these naming conventions for commits, pull requests, issues, variables etc.

**Commits**
* Be precise and describe the changes in a short sentence
* Do not add too many extra comments to a commit
* Do not use slurs etc. in your commit messages
* Do not name commits using a single word [e.g. fix, idk, fixie, change, repair etc.]

<br/>

**Pull Request**
* Describe the changes with a good title
* Follow the request template
* Do not use slurs etc. in your message
* Do not name pull requests using a single word [e.g. fix, idk, fixie, change, repair etc.]

<br/>

**Issues**
* Describe your issue with all context available
* Give information about your enviorment [e.g. os, hardware, bot version, java version etc.]
* Follow the template
* Do not use slurs etc. in your message
* Do not name issues using a single word [e.g. fix, idk, fixie, change, repair etc.]

<br/>

**Variables**
* Use camel case for all variables that are not in config files
* Do not use kebab case

<br/>

**Classes and Packages**
* Packages are named lowercase only
* Classes are named using pascal case
