## 特点

-   支持粘性事件
-   支持标签
-   无需注册注销
-   支持线程切换
-   支持Lifecycle生命周期跟随
-   支持Kotlin
-   代码量最少



## 安装

project of build.gradle

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```



module of build.gradle

```groovy
implementation 'com.github.liangjingkanji:RxBus:1.0'
```


## 发送事件

```kotlin
fun sendEvent(event: Any)
```



## 观察事件

```kotlin
observerEvent<Model> {
	// 事件回调
}
```



函数

```kotlin
inline fun <reified T> LifecycleOwner.observerEvent(
    scheduler: Scheduler = Schedulers.trampoline(),
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    noinline block: T.() -> Unit
)
```



## 标签事件

示例

```kotlin
// 发送标签
sendTag("refresh_event")


// 接受标签事件
observerTag("refresh_event", "finish_event"){
	when(this){
        "refresh_event" -> {}
        else -> {}
    }
}
```



函数

```kotlin
fun sendTag(tag: String)

fun LifecycleOwner.observerTag(
    vararg tags: String,
    scheduler: Scheduler = Schedulers.trampoline(),
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    block: String.() -> Unit
)
```



## 粘性事件

示例

```kotlin
sendEvent(Model(1))

getEvent<Model>()?.let {
	// get data 
}
```



函数

```kotlin
fun setEvent(event: Any?)

inline fun <reified T> getEvent(): T?
```

