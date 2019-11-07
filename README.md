## 特点

-   支持标签
-   无需注册注销
-   支持线程切换
-   支持Lifecycle生命周期跟随
-   支持Kotlin
-   代码量最少



不支持粘性事件, 建议自己序列化到本地来控制, 市面上的粘性事件基本上都是属于全局变量, 在意外销毁的时候会导致数据丢失引发空指针异常.

不支持注解, 因为我认为函数比注解更加方便而且代码量更少也不存在任何性能影响, 以及便于数据共享. 

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
implementation 'com.github.liangjingkanji:RxBus:1.1'
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



如果你不在Activity或者Frament中观察, 可以使用`receiveTag|receiveEvent`这两个函数, 他们返回`Disposable`用于手动注销观察者.

使用方法一致



