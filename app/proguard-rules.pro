# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\mtpv\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
-keepattributes *Annotation*

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-dontwarn org.apache.**
-dontwarn org.slf4j.**
-dontwarn org.json.*
-dontwarn org.mortbay.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn javax.xml.**
-dontwarn javax.management.**
-dontwarn java.lang.management.**
-dontwarn android.support.**
-dontwarn com.google.code.**
-dontwarn oauth.signpost.**
-dontwarn twitter4j.**

-dontwarn org.kobjects.**
-dontwarn org.ksoap2.**
-dontwarn org.kxml2.**
-dontwarn org.xmlpull.v1.**


-ignorewarnings
-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontoptimize
-dontpreverify



##---------------Begin: proguard configuration common for all Android apps ----------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
##---------------End: proguard configuration common for all Android apps ----------


-assumenosideeffects class android.util.Log {
    public static *** e(...);
    public static *** w(...);
    public static *** wtf(...);
    public static *** d(...);
    public static *** v(...);
}