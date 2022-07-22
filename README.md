# Direct to Chat
Direct to Chat is a minimal Android application that helps you send WhatsApp messages to unsaved contacts without needing to save their number to the phone book first. The app leverages official WhatsApp API to open the chat window of a particular number directly.

Note that I was unable to find a way to include direct calling or video calling support using any official means. Any help in that regard would be much appreciated.

## Download from Play Store
If you want the official experience and do not want to use .apk files, you can download this app from the Google Play Store. [^1]
[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.hamohdy.whatsappdirect&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1)


## How it works
Using the ``android.intent.action.VIEW`` action and WhatsApp\'s official [api.whatsapp.com](https://api.whatsapp.com) website, it is quite easy to open the chat window to a particular phone number, regardless of whether it is in the contact book or not. To ensure that the browser does not catch this intent, the ``setPackage(String packageName)`` method is used to explicitly specify the target package.

The data provided to the intent is of the form ```https://api.whatsapp.com/send?phone=CCxxxxxxxxxx&text=yyyyyy``` where CC is the country code, xxxxxxxxx is the WhatsApp number, and yyyyy is an optional parameter to pre-fill the text box with some text after the chat window is opened.

### Note
The country code is necessary while providing the phone number. To help in this regard, the app contains a list of 255 countries with their ISD and ISO codes, along with the icons in png format for almost all of the countries.

## Importing into Gradle
This step is quite simple. Clone this repository and open the build.gradle file directly in Android Studio or the IDE of your choice.
Once you build the project, you can install it onto any Android device running Android 9.0 Pie or above.

## Screenshot
[![Screenshot1](https://github.com/hafizmdyasir/WhatsAppDirect/blob/master/screenshots/Screenshot_20220622-125521_WhatsApp%20Direct.jpg)

## Attributions
This application makes uses of the following sources:
- Flags of all countries obtained via [Flagpedia.net](https://flagpedia.net)
- [Fluent UI System Icons](https://github.com/microsoft/fluentui-system-icons) by Microsoft
- [Fast Scroller](https://github.com/L4Digital/FastScroll) by L4Digital

[^1]: Google Play and the Google Play logo are trademarks of Google LLC.
