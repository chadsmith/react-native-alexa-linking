# react-native-alexa-linking
React Native module for [Alexa App-to-App Linking](https://developer.amazon.com/en-US/docs/alexa/account-linking/app-to-app-account-linking-starting-from-your-app.html)

## Install
```
$ npm i git://github.com/chadsmith/react-native-alexa-linking.git
$ react-native link react-native-alexa-linking
```

## Usage
```javascript
import AlexaLinking from 'react-native-alexa-linking';

// Get Alexa app URL and LWA fallback URL from your service
const links = await MyService.getAlexaURLs();

// Method 1: Open appURL if Alexa app is installed, otherwise open fallbackURL
AlexaLinking.openURL(links.appURL, links.fallbackURL);

// Method 2: Open Alexa app if installed, otherwise handle fallback on your own
AlexaLinking.openURL(links.appURL).then(appOpened => {
  // load fallbackURL in webview if !appOpened
});
```
