package com.example.androidcourse.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultCoroutineDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoCoroutineDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AppCoroutineScope