#import <React/RCTUtils.h>
#import "RNAlexaLinking.h"

@implementation RNAlexaLinking

RCT_EXPORT_MODULE();

+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

RCT_EXPORT_METHOD(openURL:(NSURL *)appURL
                  fallback:(NSURL * __nullable)fallbackURL
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    [RCTSharedApplication() openURL:appURL options:@{UIApplicationOpenURLOptionUniversalLinksOnly: @YES} completionHandler:^(BOOL appOpened) {
        if (appOpened) {
            resolve(@YES);
        }
        else if(fallbackURL) {
            [RCTSharedApplication() openURL:fallbackURL options:@{} completionHandler:^(BOOL fallbackOpened) {
                if (fallbackOpened) {
                    resolve(@YES);
                }
                else {
                    resolve(@NO);
                }
            }];
        }
        else {
            resolve(@NO);
        }
    }];
}

@end
