# SafetyParams

[![](https://jitpack.io/v/twiceyuan/SafetyParams.svg)](https://jitpack.io/#twiceyuan/SafetyParams)

在 Kotlin 下通过更简单和类型安全的方式进行 Activity/Fragment 传值。

## 用法 

定义数据类型，成员可定义成任何 intent 可存储的数据类型（int 等原始类型会被存储为 Serializable 以分辨 null 与 非 null）：

```kotlin
class ReceiverActivity : AppCompatActivity() {

    // 定义一个 data bean
    data class Params(
            val name: String,
            val phone: String,
            val emails: ArrayList<String>?,
            val parcelableBean: ParcelableBean,
            val nestedBean: Father,
            val age: Int
    ) : ActivityParams<ReceiverActivity>() // 通过泛型和 host 类绑定

    // 解析接收到的类型
    private val args by parseParams<Params>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvContent.text = args.toJsonTree() // 直接取值进行使用
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

## Java 兼容性

Java 下反射会丢失构造器参数的名称，暂无兼容 Java 计划。
