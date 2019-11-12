package com.drake.rxbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


@Suppress("ObjectPropertyName")
val _bus = PublishSubject.create<Bus<Any>>().toSerialized()

// <editor-fold desc="Send Event">

fun sendEvent(event: Any, tag: String = "") {
    _bus.onNext(Bus(event, tag))
}

fun sendTag(tag: String) {
    _bus.onNext(Bus(TagEvent(), tag = tag))
}


// </editor-fold>


// <editor-fold desc="Observer Event">

inline fun <reified T> LifecycleOwner.observeEvent(
        vararg tags: String = arrayOf(),
        scheduler: Scheduler = Schedulers.trampoline(),
        lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
        noinline block: Bus<T>.() -> Unit
) {

    val disposable =
            _bus.filter { val tag = it.tag; it.event is T && (tags.isEmpty() && tag.isBlank() || tags.contains(tag)) }
                    .map {
                        @Suppress("UNCHECKED_CAST")
                        it as Bus<T>
                    }
                    .observeOn(scheduler).onTerminateDetach().subscribe(block)

    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (lifecycleEvent == event) {
                disposable.dispose()
            }
        }
    })
}

fun LifecycleOwner.observeTag(
        vararg tags: String,
        scheduler: Scheduler = Schedulers.trampoline(),
        lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
        block: String.() -> Unit
) {

    val disposable = _bus
            .filter { it.event.javaClass == TagEvent::class.java && tags.contains(it.tag) }
            .map { it.tag }
            .observeOn(scheduler)
            .onTerminateDetach()
            .subscribe(block)

    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (lifecycleEvent == event) {
                disposable.dispose()
            }
        }
    })
}


inline fun <reified T> receiveEvent(
        vararg tags: String = arrayOf(),
        scheduler: Scheduler = Schedulers.trampoline(),
        noinline block: Bus<T>.() -> Unit
): Disposable {
    return _bus.filter {
        val tag = it.tag
        it.event is T && (tags.isEmpty() && tag.isBlank() || tags.contains(tag))
    }.map {
        @Suppress("UNCHECKED_CAST")
        it as Bus<T>
    }.observeOn(scheduler).onTerminateDetach().subscribe(block)
}

fun receiveTag(
        vararg tags: String,
        scheduler: Scheduler = Schedulers.trampoline(),
        block: String.() -> Unit
): Disposable {

    return _bus.filter { it.event.javaClass == TagEvent::class.java && tags.contains(it.tag) }
            .map { it.tag }
            .observeOn(scheduler)
            .onTerminateDetach()
            .subscribe(block)
}

// </editor-fold>


