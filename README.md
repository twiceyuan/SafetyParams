# SafetyParams

[![](https://jitpack.io/v/twiceyuan/SafetyParams.svg)](https://jitpack.io/#twiceyuan/SafetyParams)

在 Kotlin 下通过更简单和类型安全的方式进行 Activity/Fragment 传值。

## 用法 

定义数据类型，成员可定义成任何 intent 可存储的数据类型（int 等原始类型会被存储为 Serializable 以分辨 null 与 非 null）：

```kotlin
class ReceiverActivity : AppCompatActivity() {
    // 定义参数类型
    data class Params(
            val name: String,
            val phone: String,
            val emails: ArrayList<String>?,
            val parcelableBean: ParcelableBean,
            val nestedBean: Father,
            val age: Int
    ) : SafetyActivityParams(ReceiverActivity::class.java)

    // 解析
    private val args by lazy { parseParams<Params>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        // 直接取值
        tv_content.text = args.toJsonTree()
    }
}
```


发送方：

```kotlin
ReceiverActivity.Params(
    age = 25,
    name = "Tony",
    phone = "123456789",
    parcelableBean = ParcelableBean("Tony", 100),
    emails = arrayListOf("somemail1@gmail.com, somemail2@gmail.com"),
    nestedBean = Father("Father", 25, Child("child", 1))
).launch(this)
```

## 引用依赖

因 JitPack 没有生成 Kotlin 的 source 包，暂时使用私有仓库进行引用：

```groovy
repositories {
    // 添加仓库地址
    maven { url "https://jitpack.io" }
}

dependencies {
    // 添加引用
    implementation "com.github.twiceyuan:SafetyParams:$latest_version"
}
```

### __ext.latest_version=__  [![](https://jitpack.io/v/twiceyuan/SafetyParams.svg)](https://jitpack.io/#twiceyuan/SafetyParams)


## ProGuard

```
-keep class * extends com.twiceyuan.safetyparams.library.SafetyParams {
    <init>(...);
    *;
}

-keep class * implements android.os.Parcelable { *; }
-keep class * implements java.io.Serializable { *; }

-dontwarn org.jetbrains.annotations.**
-dontwarn kotlin.reflect.jvm.internal.**

-keep class kotlin.reflect.jvm.internal.** { *; }
-keep class kotlin.Metadata { *; }
-keep public class kotlin.reflect.jvm.internal.impl.builtins.* { public *; }
```

## TODO

Java 下反射会丢失构造器参数的名称，暂时没有找到好的方案来在 Java 下使用这个工具。
