# Privacy Policy for WhatsApp Direct
> As a valued user, your privacy is important.

## General information
I don't believe in long and lengthy policies that the user cannot understand. Instead, here is what you must understand about using WhatsApp Direct, which I call "the app" in this document:
1. Neither I nor the app collects any data whatsoever from your device.
2. I have made efforts, to the best of my abilities, to keep user privacy at the core of my philosophy.
3. This app in itself does not contain data, information, or content not suitable for children. However, it is used to open WhatsApp chats and the chat windows opened are at the sole discretion of the user. Whatever number you enter, the app will try to open the corresponding chat window. 

## Legal protection
If the user decides to use this app for any sort of unethical, immoral, or illegal means, they will be solely responsible for it. 

## Personal data collection
As previously mentioned, this app does not collect or store any personal data. 

### Clipboard data (not collected)
Each time you come back to the main screen of the app, or in programmer's terms, the MainActivity's `onResume()` function is called, the app checks the clipboard for any copied content. If this content is found to be a phone number, the app provides the option to paste it.
This options does not store the clipboard data in local storage. Instead, the copied content is copied into a variable, which is removed from memory once the app shuts down.

### Data entered into the app (not collected)
The country you choose, the number you enter, and the message you enter are temporarily stored in the `savedInstanceState` of the MainActivity. Once again, this data is cleared up as soon as the app is closed.

## Links to websites
There are a few necessary links inside the app for attribution purposes, plus a link to my profile page. Clicking on them will lead you to the website as required. Once there, you will be governed by the policies of the website you are visiting. 

## Contact
If you have questions, feel free to reach out to me via the following website:
[Mohammad Yasir](https://hafizmdyasir.github.io)