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

已经包含在 consumer-rules 文件中，不必单独手动添加。文件内容为：
```
-keep class * extends com.twiceyuan.safetyparams.library.SafetyParams { *; }
-keep class * extends android.os.Parcelable { *; }
-keep class * extends java.io.Serializable { *; }
```

需要注意的是，如果 SafetyParams 的子类定义为 Fragment 的内部类，并且没有定义 keep Fragment 成员配置的话，该子类仍然会被混淆，原因暂不清楚（尝试把该内部类定义在其他 Class 下均未发现该现象）。

如果需要混淆避免该情况，有两种建议的做法：
1. 把 SafetyParams 定义在外部，例如和 Fragment 平级的 class
2. 添加 keep Fragment 的配置：`-keep class * extends androidx.fragment.app.Fragment { *; }`

## Java 兼容性

Java 下反射会丢失构造器参数的名称，暂无兼容 Java 计划。
