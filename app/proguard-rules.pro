# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keepattributes Exceptions
-keepclasseswithmembernames enum * { *; }
-keep class org.chennaimetrorail.appv1.modal.** { *; }
-keep class org.chennaimetrorail.appv1.modal.*

-keep class org.chennaimetrorail.appv1.travelcard.** { *; }
-keep class org.chennaimetrorail.appv1.travelcard.*
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**

# -keep class com.billdesk.sdk.** { *; }
-dontwarn com.billdesk.sdk.**
-keep class com.billdesk.sdk.** {*;}
-keep class com.billdesk.sdk.*

-keep public class org.json.*{ *; }
-keep class org.json.*{ *; }
-keepclassmembers,allowobfuscation class * {
    @org.json.* <fields>;
    @org.json.* <init>(...);
}
