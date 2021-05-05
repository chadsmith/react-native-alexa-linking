import { NativeModules } from 'react-native';

const { RNAlexaLinking } = NativeModules;

export default {
  openURL: (appURL, fallbackURL = null) =>
    RNAlexaLinking.openURL(appURL, fallbackURL),
};
