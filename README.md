# ActivityArgs

在 Kotlin 下通过更简单和类型安全的方式进行 Activity 传值。

## 用法 

接收方定义数据类型：

```kotlin
class ReceiverActivity : AppCompatActivity() {

    // 定义数据类型，成员可定义成任何 intent 可存储的数据类型（int 等原始类型会被存储为 Serializable 以分辨 null 与 非 null）
    data class Starter(
            val name: String,
            val phone: String,
            val emails: ArrayList<String>?,
            val parcelableBean: ParcelableBean,
            val nestedBean: Father,
            val age: Int
    ) : ActivityArgs() {
        // 定义要启动的目标 Activity
        override fun targetClass() = ReceiverActivity::class.java
    }

    // 解析传进来的值
    private val args by lazy { parseActivityArgs<Starter>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        tv_content.text = args.toJsonTree()
    }

    private fun Any.toJsonTree() = JSONObject(Gson().toJson(this)).toString(2) ?: "{}"
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

