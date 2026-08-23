# Mafia Freemium — ProGuard rules (release)

-keep class com.android.vending.billing.** { *; }
-keep class com.android.billingclient.** { *; }
-keepclassmembers class * extends androidx.datastore.** { *; }
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
