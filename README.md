如果你的项目存在Kotlin推荐使用[Channel](https://github.com/liangjingkanji/Channel)

## 特点

-   支持标签
-   无需注册注销
-   支持线程切换
-   支持Lifecycle生命周期跟随
-   支持Kotlin
-   代码量最少



不支持粘性事件, 建议自己序列化到本地来控制, 市面上的粘性事件基本上都是属于全局变量, 在界面意外销毁的时候会导致数据丢失引发空指针异常. 

不支持注解, 因为我认为函数比注解更加方便而且代码量更少也不存在任何性能影响, 以及便于数据共享. 

## 安装

project 的 build.gradle

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```



module 的 build.gradle

```groovy
implementation 'com.github.liangjingkanji:RxBus:1.0.2'
```





## 发送事件

```kotlin
sendEvent(event)

sendEvent(event, "refresh_event")
```



## 观察事件

```kotlin
observeEvent<Model> {
	// 事件回调
}

observeEvent<Model>("标签") {
	// 事件回调
}
```



建议标签采取一定规范命名, 以便全局搜索标签事件. 例如: `refresh_event` 以`_event`为后缀

一旦给某个事件观察者添加Tag标签. 该事件发送就必须包含标签, 否则无法接受到. (空字符串标签无效)


函数

```kotlin
inline fun <reified T> LifecycleOwner.observeEvent(
    vararg tags: String,
    scheduler: Scheduler = Schedulers.trampoline(),
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    noinline block: T.() -> Unit
)
```



## 标签事件

标签事件就是比一般事件多了一个字符串作为标记, 或者单纯一个标记没有事件对象.

应用场景: 

1.  某些事件你只需要通知某个界面自己刷新, 这个时候去创建一个对象作为事件来通知显得很多余. 
2.  有时候你只需要显示一个很简短的信息, 例如给上个界面发送一个Int类型的ID



示例

```kotlin
// 发送标签
sendTag("refresh_event")

sendEvent(123123, "refresh_event")


// 接受标签事件
observeTag("refresh_event", "finish_event"){
	when(this){
        "refresh_event" -> {}
        else -> {}
    }
}
```



函数

```kotlin
fun sendTag(tag: String)

fun LifecycleOwner.observeTag(
    vararg tags: String,
    scheduler: Scheduler = Schedulers.trampoline(),
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    block: String.() -> Unit
)
```



如果你不在Activity或者Frament中观察, 可以使用`receiveTag|receiveEvent`这两个函数, 他们返回`Disposable`用于手动注销观察者.使用方法一致



