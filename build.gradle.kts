// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
val storePassword by extra("08092004_GameDiary")
val keyPassword by extra("08092004_GameDiary")
val keyFilePath by extra("C:\\Users\\youch\\Desktop\\GameDiaryKey.jks")
