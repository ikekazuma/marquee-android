package dev.ikekazuma.marquee.core.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val type: DispatcherType)

enum class DispatcherType { IO, DEFAULT }
