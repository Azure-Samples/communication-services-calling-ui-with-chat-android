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

-dontwarn aQute.bnd.annotation.spi.ServiceProvider
-dontwarn com.microsoft.device.display.DisplayMask
-dontwarn io.micrometer.context.ContextAccessor
-dontwarn kotlinx.android.parcel.Parcelize
-dontwarn kotlinx.parcelize.Parcelize
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn reactor.blockhound.integration.BlockHoundIntegration
-dontwarn com.google.auto.value.AutoValue
-dontwarn com.microsoft.fluentui.icons.ListItemIcons
-dontwarn com.microsoft.fluentui.icons.listitemicons.ChevronKt
-dontwarn com.sun.net.httpserver.Headers
-dontwarn com.sun.net.httpserver.HttpContext
-dontwarn com.sun.net.httpserver.HttpExchange
-dontwarn com.sun.net.httpserver.HttpHandler
-dontwarn com.sun.net.httpserver.HttpServer
-dontwarn java.awt.Desktop$Action
-dontwarn java.awt.Desktop
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn lombok.NonNull
-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient

-keep class com.azure.android.** { *; }

-keep class com.fasterxml.** { *; }
-keep class com.google.android.material.snackbar.** { *; }
-keep class com.microsoft.** { *; }
-keep class com.skype.** {*;}
-keep class javax.xml.stream.** { *; }
