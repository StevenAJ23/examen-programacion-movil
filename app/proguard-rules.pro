# Retrofit + Gson: mantener anotaciones y clases de modelos
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.google.gson.** { *; }

# Room: mantener entidades y DAOs
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *

# Kotlin Serialization
-keepattributes RuntimeVisibleAnnotations
-keep class kotlinx.serialization.** { *; }
