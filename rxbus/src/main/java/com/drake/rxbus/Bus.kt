package com.drake.rxbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.ConcurrentHashMap

val bus = PublishSubject.create<Any>().toSerialized()
val stickyEvents = ConcurrentHashMap<Class<*>, Any>()

// <editor-fold desc="Send Event">

fun sendEvent(event: Any) {
    bus.onNext(event)
}

fun sendTag(tag: String) {
    sendEvent(BusEvent(tag))
}


// </editor-fold>


// <editor-fold desc="Observer Event">


inline fun <reified T> LifecycleOwner.observerEvent(
    scheduler: Scheduler = Schedulers.trampoline(),
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    noinline block: T.() -> Unit
) {

    val disposable = bus.ofType(T::class.java).observeOn(scheduler).subscribe(block)

    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (lifecycleEvent == event) {
                disposable.dispose()
            }
        }
    })
}


fun LifecycleOwner.observerTag(
    vararg tags: String,
    scheduler: Scheduler = Schedulers.trampoline(),
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    block: String.() -> Unit
) {

    val disposable = bus.ofType(BusEvent::class.java)
        .filter { tags.contains(it.tag) }
        .map { it.tag }
        .observeOn(scheduler)
        .subscribe(block)

    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (lifecycleEvent == event) {
                disposable.dispose()
            }
        }
    })
}

// </editor-fold>


// <editor-fold desc="Sticky Event">

fun setEvent(event: Any?) {
    event?.let {
        stickyEvents[event.javaClass] = event
    }
}


inline fun <reified T> getEvent(): T? {
    return try {
        stickyEvents.remove(T::class.java) as T
    } catch (e: Exception) {
        null
    }
}

// </editor-fold>


