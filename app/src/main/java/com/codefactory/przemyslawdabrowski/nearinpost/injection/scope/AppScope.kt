package com.codefactory.przemyslawdabrowski.nearinpost.injection.scope

import javax.inject.Scope

/**
 * Scope that last application lifecycle.
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppScope