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

# Fragment 的内部类不被 consumer-rules 里的规则影响，仍然会被混淆，找到好的解决方法之前先 keep fragment 的子类
-keep class * extends androidx.fragment.app.Fragment { *; }

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable