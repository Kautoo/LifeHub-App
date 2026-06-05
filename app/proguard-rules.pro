# ProGuard rules for LifeHub

# Keep Room entities
-keep class com.fizi.lifehub.data.local.entity.** { *; }

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
