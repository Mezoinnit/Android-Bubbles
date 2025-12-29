# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/mezo/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# Keep the BubbleActivity and NotificationHelper to prevent aggressive shrinking from removing them
-keep class com.mezo.bubblefeature.BubbleActivity { *; }
-keep class com.mezo.bubblefeature.data.datasource.NotificationDataSource { *; }
