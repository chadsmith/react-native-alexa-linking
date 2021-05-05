package com.github.chadsmith.RNAlexaLinking;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

class RNAlexaLinkingModule extends ReactContextBaseJavaModule {

    RNAlexaLinkingModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @Override
    public String getName() {
        return "RNAlexaLinking";
    }

    @ReactMethod
    public void openURL(@Nullable String appURL, @Nullable String fallbackURL, final Promise promise) {
        if (isInstalled()) {
            openURL(appURL, promise);
        }
        else {
            openURL(fallbackURL, promise);
        }
    }

    private void openURL(@Nullable String url, final Promise promise) {
        if (url == null || url.isEmpty()) {
            promise.resolve(false);
            return;
        }
        try {
            Activity currentActivity = getCurrentActivity();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url).normalizeScheme());

            String selfPackageName = getReactApplicationContext().getPackageName();
            ComponentName componentName =
                    intent.resolveActivity(getReactApplicationContext().getPackageManager());
            String otherPackageName = (componentName != null ? componentName.getPackageName() : "");

            // If there is no currentActivity or we are launching to a different package we need to set
            // the FLAG_ACTIVITY_NEW_TASK flag
            if (currentActivity == null || !selfPackageName.equals(otherPackageName)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            if (currentActivity != null) {
                currentActivity.startActivity(intent);
            } else {
                getReactApplicationContext().startActivity(intent);
            }

            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(
                    new JSApplicationIllegalArgumentException(
                            "Could not check if URL '" + url + "' can be opened: " + e.getMessage()));
        }
    }

    private boolean isInstalled() {
        try {
            String ALEXA_PACKAGE_NAME = "com.amazon.dee.app";
            PackageInfo packageInfo = getReactApplicationContext().getPackageManager().getPackageInfo(ALEXA_PACKAGE_NAME, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                int REQUIRED_MINIMUM_VERSION_CODE = 866607211;
                return packageInfo.getLongVersionCode() > REQUIRED_MINIMUM_VERSION_CODE;
            } else {
                return packageInfo != null;
            }
        }
        catch (Exception ignored) {
            // The Alexa App is not installed
            return false;
        }
    }
}
