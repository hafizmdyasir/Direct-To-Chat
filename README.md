# WhatsAppDirect
WhatsApp Direct is a minimal Android application that helps you send WhatsApp messages to unsaved contacts without needing to save their number to the phone book first. The app leverages official WhatsApp API to open the chat window of a particular number directly.

Note that I was unable to find a way to include direct calling or video calling support using any official means. Any help in that regard would be much appreciated.

## How it works
Using the ``android.intent.action.VIEW`` action and WhatsApp\'s official [api.whatsapp.com](https://api.whatsapp.com) website, it is quite easy to open the chat window to a particular phone number, regardless of whether it is in the contact book or not. To ensure that the browser does not catch this intent, the ``setPackage(String packageName)`` method is used to explicitly specify the target package.

The data provided to the intent is of the form ```https://api.whatsapp.com/send?phone=CCxxxxxxxxxx&text=yyyyyy``` where CC is the country code, xxxxxxxxx is the WhatsApp number, and yyyyy is an optional parameter to pre-fill the text box with some text after the chat window is opened.

### Note
The country code is necessary while providing the phone number. To help in this regard, the app contains a list of 255 countries with their ISD and ISO codes, along with the icons in png format for almost all of the countries.

## Importing into Gradle
This step is quite simple. Clone this repository and open the build.gradle file directly in Android Studio or the IDE of your choice.
Once you build the project, you can install it onto any Android device running Android 9.0 Pie or above.
Due to lack of time and resources, I am unable to create a release APK and upload it to Google Play Store. Once again, any help in this regard would be much appreciated.

## Screenshots
[![Screenshot1](https://github.com/HafizYasir/WhatsAppDirect/blob/master/Screenshot_20210813-191918_WhatsApp%20Direct.jpg "Screenshot1")](https://github.com/HafizYasir/WhatsAppDirect/blob/master/Screenshot_20210813-191918_WhatsApp%20Direct.jpg "Screenshot1")

[![Screenshot 2 - Choosing a country](https://github.com/HafizYasir/WhatsAppDirect/blob/master/Screenshot_20210813-191930_WhatsApp%20Direct.jpg "Screenshot 2 - Choosing a country")](https://github.com/HafizYasir/WhatsAppDirect/blob/master/Screenshot_20210813-191930_WhatsApp%20Direct.jpg "Screenshot 2 - Choosing a country")

## Attributions
This application makes uses of the following sources:
- Flags of all countries obtained via [Flagpedia.net](https://flagpedia.net)
- [Fluent UI System Icons](https://github.com/microsoft/fluentui-system-icons) by Microsoft
- [Fast Scroller](https://github.com/L4Digital/FastScroll) by L4Digital
- [Sent](https://icons8.com/icon/ZznWGhUzgWtS/sent) icon by [Icons8](https://icons8.com)
