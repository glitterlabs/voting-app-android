# Appolos - Android #

# Introduction #

Millions of people travel the world each year. They discover new lands, new cultures and news things made locally. Those travelers usually come back home with souvenirs for themselves and their loved ones. On the other hand, there are dozens of millions of people who don't travel for some reason: job, sickness, lack of money etc... Those who don't travel would certainly love to have a souvenir from a distant land but can't get their hands on because of some obstacle. Appolos Inc. wants to help people who don't travel get a souvenir from the land of their dreams. This will be possible through the App Appolos. People who travel can register on this App and people who don't travel can ask them to bring them a souvenir from abroad. Appolos also allows Immigrants to send packages back home through regular travelers. To sum up, Appolos accomplishes two functions in one: 1- Allowing non-travelers to get particular items or souvenirs from anywhere in the world through regular travelers; 2- Allowing immigrants anywhere in the world to send packages to their home country through regular travelers.


### Features ###

* User are able to do registration in very simple way that have simple UI , after registration app navigates to fill profile screen where user can add minimum information.

* If user is already registered then user can login direct and maintain session.
* User can add gift or trip along picture easily.
* App detects the current location and add gift according to current location
* App can search nearest trips within 50 miles and lists them.
* Ability to send request and reject trip gift requests 
* If requested accepted then both users can do chatting
* Traveler can send approval pics to gift user .
* Add gift owner side user can see current location of trip user
* App has payment gateway integration of STRIPE CONNECT api.
* App has History module that can maintain all successful transactions.
* App made in pure Material Design under proper guidelines




API Used : 

* *User Service* - For save profile data


* *Avatar Service* - for save profile image of users
- avatarService.createAvatar(avatarName,userName,filePath, description,new App42CallBack());

* *Storage Service* - for save gift and trip data

* *Push notification Service* - for send realtime push notifcations on devices

* *Buddy Service* - for send and accept request from gift user to trip user

* *Upload Service* - for upload gift approval pictures

### Version Code###

* 33 - specifies that this is latest apk than older, it must be higher than older apk version code.

### Version Name###

* 1.20 - specifies the version name of android app, it will display on playstore



### API Credientials ###

 APP42_API_KEY = "6edda500a16d4c2eec0bac73ec548e78d9734418edc10b60639fbccb72221a4d";
 
 APP42_SECRET_KEY = "89e65ebac5f0ed41295a6ea60ffd7bb2683758c543f1e4aadc3fd966108b7b8c";
 
 GOOGLE_PROJECT_NO="125533368013";
 
 GCM_SERVER_KEY="AIzaSyA5G0_KEMRDu3-_oqqlkV7A-wPODa6Lwds";
 
 STRIPE PUBLISHABLE_KEY = "pk_live_0Z7jzzDQKgxO8hzNnhHcQLJb";

 STRIPE SECRET_KEY = "sk_live_tRIuQgXbuxk0p85KpSdwatPu";

 STRIPE CLIENT_ID = "ca_8ovW2dyNYfVMiDZEaYSmpQqm7oUXtctL";

 STRIPE CALLBACK_URL = "https://appolos.co";

 STRIPE ACCOUNT_NAME = "ClientStripeAccount";


### System Requirement ###


* Microsoft® Windows® 7/8/10 (or) Mac® OS X® 10.8.5 or higher
* 2 GB RAM minimum, 8 GB RAM recommended
* 2 GB of available disk space minimum,
* 4 GB Recommended (500 MB for IDE + 1.5 GB for Android SDK and emulator system image)
* 1280 x 800 minimum screen resolution
* Java Development Kit (JDK) 8
* Android Studio 2.0 stable version 
  - Download link : [Android studio with SDK](http://developer.android.com/intl/ja/sdk/index.html)
* Android SDK Compiled version - 23
* Minimum SDK version - 15
* Gradle version - 2.0
* SourceTree - [LINK](https://www.sourcetreeapp.com/download)
* If you are using the SSH protocol, ensure sure your public key is in Bitbucket and loaded on the local system to which you are cloning.
* Git

### Detailed Steps to setup ANDROID STUDIO ###

### Setup and Sourcetree ###

Cloning a Git repository

You can use Sourcetree, Git from the terminal, or any client you like to clone your Git repository. These instructions show you how to clone your repository using Git from the terminal.

* Change to the local directory where you want to clone your repository.
* git clone https://johnnegus@bitbucket.org/johnnegus/appolos-android.git

### Run the project in Android Studio ###

* Import the project in Android Studio.
* Make sure internet is available, Android build will download artifacts from internet
* Sync the project.
* Enable the Developer option in mobile and attach a mobile ,make sure you have to install USB drivers on your system.
* RUN the project
* if Mobile is not available then create a new Android virtual device from AVD manager and start the Virtual device


## Project GIF demo ##