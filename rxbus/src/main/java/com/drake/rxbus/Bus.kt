package com.drake.rxbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


val _bus = PublishSubject.create<Any>().toSerialized()

// <editor-fold desc="Send Event">

fun sendEvent(event: Any) {
    _bus.onNext(event)
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

    val disposable =
        _bus.ofType(T::class.java).observeOn(scheduler).onTerminateDetach().subscribe(block)

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

    val disposable = _bus.ofType(BusEvent::class.java)
        .filter { tags.contains(it.tag) }
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
    scheduler: Scheduler = Schedulers.trampoline(),
    noinline block: T.() -> Unit
): Disposable {

    return _bus.ofType(T::class.java).observeOn(scheduler).onTerminateDetach().subscribe(block)
}

fun receiveTag(
    vararg tags: String,
    scheduler: Scheduler = Schedulers.trampoline(),
    block: String.() -> Unit
): Disposable {

    return _bus.ofType(BusEvent::class.java)
        .filter { tags.contains(it.tag) }
        .map { it.tag }
        .observeOn(scheduler)
        .onTerminateDetach()
        .subscribe(block)
}

// </editor-fold>


