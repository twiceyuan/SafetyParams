# ActivityArgs

在 Kotlin 下通过更简单和类型安全的方式进行 Activity 传值。

## 用法 

定义数据类型，成员可定义成任何 intent 可存储的数据类型（int 等原始类型会被存储为 Serializable 以分辨 null 与 非 null）：

```kotlin
data class Starter(
        val name: String,
        val phone: String,
        val emails: ArrayList<String>?,
        val parcelableBean: ParcelableBean, // 一个 Parcelable 类
        val nestedBean: Father, // 一个 Serializable 嵌套 Serializable 类
        val age: Int
) : ActivityArgs() {
    // 定义要启动的目标 Activity
    override fun targetClass() = ReceiverActivity::class.java
}
```


发送方：

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // ......
    ReceiverActivity.Starter(
            name = "Tony",
            phone = "123456789",
            emails = arrayListOf("somemail1@gmail.com, somemail2@gmail.com"),
            age = 25,
            parcelableBean = ParcelableBean("Tony", 100),
            nestedBean = Father("Father", 25, Child("child", 1))
    ).launch(this)
}
```


接收方：

```kotlin
class ReceiverActivity : AppCompatActivity() {

    // 解析传进来的值
    private val args by lazy { parseActivityArgs<Starter>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        // 直接进行取值
        xxx = args.name // "Tony"
        xxx = args.nestedBean.child.name // "child"
        // ...
    }
}
```

