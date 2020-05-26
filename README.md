# Twitter-Bot-Sisko
A simple Twitter bot that calculates how many days are left until a specific date. The bot runs on a public Heroku server and goes through a scheduled task once every day in order to tweet how many days are left.


## Usage
In the Bot.java class there's a method called configureTwitter(). In here, there are 4 lines of code which have comments at the end of them. You need to change these lines of code so that you can use the bot on your own account. You'll need a Twitter Developer app for this in order to get your API keys.
For example, this is the first line you need to edit:
```
.setOAuthConsumerKey(System.getenv("h_consumerKey")) //Twitter API key
```
Replace "System.getenv("h_consumerKey") with your personal Consumer API key. 
Once replaced, it will look something like this:
```
.setOAuthConsumerKey("jg40lgmApfipz028AMhCgla0sZ62C") //Twitter API key
```
Do the same thing for the other 3 lines of code. The names of the API key codes match the pieces of code after

... System.getenv("h_API_KEY_NAME_HERE")
