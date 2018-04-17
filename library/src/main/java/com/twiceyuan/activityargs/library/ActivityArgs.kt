package com.twiceyuan.activityargs.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType

/**
 * Created by twiceYuan on 2018/3/26.
 *
 * Pass the args from intent
 */

/**
 * 定义传递参数数据 Model 所要继承的类。例如：
 * ```
 * data class Starter(val name: String) : ActivityArgs {
 *     override fun targetClass() = SomeActivity::class.java
 * }
 * ```
 */
abstract class ActivityArgs(@Transient private val targetClass: Class<out Activity>) {
    fun launch(context: Context) {
        context.startActivityWithArgs(targetClass, this)
    }
}

/**
 * 判断当前类型是否为 T 的子类型
 */
inline fun <reified T> Class<*>.isSubType() = T::class.java.isAssignableFrom(this)

/**
 * 利用 ActivityArgs 启动一个 Activity
 */
private fun Context.startActivityWithArgs(targetClass: Class<out Activity>, args: ActivityArgs) {
    val intent = Intent(this, targetClass)

    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    // 临时读取一个 field
    @Suppress("UNCHECKED_CAST")
    fun <T> Field.read(obj: Any): T? {
        return if (isAccessible) {
            get(obj) as T?
        } else {
            isAccessible = true
            val temp = get(obj)
            isAccessible = false
            temp as T?
        }
    }

    args::class.java.declaredFields.forEach { field ->
        /**
         * 根据 [args] 中定义的成员类型和名称存储到 intent 中
         */
        when {
            field.type.isSubType<String>() -> {
                intent.putExtra(field.name, field.read<String>(args))
                return@forEach
            }

            field.type.isSubType<CharSequence>() -> {
                intent.putExtra(field.name, field.read<CharSequence>(args))
                return@forEach
            }

            field.type.isSubType<Parcelable>() -> {
                intent.putExtra(field.name, field.read<Parcelable>(args))
                return@forEach
            }

        // Array 类
            field.type.isSubType<Array<Boolean>>() -> {
                intent.putExtra(field.name, field.read<Array<Boolean>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Byte>>() -> {
                intent.putExtra(field.name, field.read<Array<Byte>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Char>>() -> {
                intent.putExtra(field.name, field.read<Array<Char>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Double>>() -> {
                intent.putExtra(field.name, field.read<Array<Double>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Float>>() -> {
                intent.putExtra(field.name, field.read<Array<Float>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Int>>() -> {
                intent.putExtra(field.name, field.read<Array<Int>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Long>>() -> {
                intent.putExtra(field.name, field.read<Array<Long>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Short>>() -> {
                intent.putExtra(field.name, field.read<Array<Short>>(args))
                return@forEach
            }
            field.type.isSubType<Array<CharSequence>>() -> {
                intent.putExtra(field.name, field.read<Array<CharSequence>>(args))
                return@forEach
            }
            field.type.isSubType<Array<String>>() -> {
                intent.putExtra(field.name, field.read<Array<String>>(args))
                return@forEach
            }
            field.type.isSubType<Array<Parcelable>>() -> {
                intent.putExtra(field.name, field.read<Array<Parcelable>>(args))
                return@forEach
            }

        // ArrayList 类
            field.type.isSubType<ArrayList<Int>>() -> {
                intent.putExtra(field.name, field.read<ArrayList<Int>>(args))
                return@forEach
            }
            field.type.isSubType<ArrayList<Parcelable>>() -> {
                intent.putExtra(field.name, field.read<ArrayList<Parcelable>>(args))
                return@forEach
            }
            field.type.isSubType<ArrayList<String>>() -> {
                intent.putExtra(field.name, field.read<ArrayList<String>>(args))
                return@forEach
            }
            field.type.isSubType<ArrayList<CharSequence>>() -> {
                intent.putExtra(field.name, field.read<ArrayList<CharSequence>>(args))
                return@forEach
            }

        // 所有基本类型都以 Serializable 存储，来解决 bundle 中没有分辨 null 值使用默认值的问题
            arrayOf(
                    field.type.isSubType<Int>(),
                    field.type.isSubType<Boolean>(),
                    field.type.isSubType<Double>(),
                    field.type.isSubType<Long>(),
                    field.type.isSubType<Short>(),
                    field.type.isSubType<Float>(),
                    field.type.isSubType<Byte>(),
                    field.type.isSubType<Serializable>()
            ).any() -> {
                intent.putExtra(field.name, field.read<Serializable>(args))
                return@forEach
            }

            else -> {
                throw DataBeanNotLegalException(
                        "${args.javaClass.canonicalName} 中定义了 Bundle 不支持存储的类型: " +
                                "成员 ${field.type.canonicalName}[${field.name}]"
                )
            }
        }
    }
    startActivity(intent)
}

class DataBeanNotLegalException(msg: String) : RuntimeException(msg)

inline fun <reified Data : ActivityArgs> Activity.parseActivityArgs(): Data {
    return parseActivityArgs(this, Data::class.java)
}

fun <Data : ActivityArgs> parseActivityArgs(activity: Activity, dataClass: Class<Data>): Data = with(activity) {

    val constructorMap = hashMapOf<KParameter, Any?>()

    val primaryConstructor = dataClass.kotlin.constructors.firstOrNull() ?: throw DataBeanNotLegalException(
            "数据类应该有一个主构造器"
    )

    if (primaryConstructor.parameters.isEmpty()) throw DataBeanNotLegalException(
            "数据类应该至少有一个构造器参数"
    )

    primaryConstructor.parameters.forEach {

        val key = it.name
        constructorMap[it] = when (it.type.javaType) {
        // 原始类型
            is Class<*> -> {
                val typeClass = it.type.javaType as Class<*>
                when {
                    typeClass.isSubType<String>() -> intent.getStringExtra(key)
                    typeClass.isSubType<Parcelable>() -> intent.getParcelableExtra<Parcelable>(key)

                    arrayOf(
                            typeClass.isSubType<Int>(),
                            typeClass.isSubType<Boolean>(),
                            typeClass.isSubType<Double>(),
                            typeClass.isSubType<Long>(),
                            typeClass.isSubType<Short>(),
                            typeClass.isSubType<Float>(),
                            typeClass.isSubType<Byte>(),
                            typeClass.isSubType<Serializable>()
                    ).any() -> intent.getSerializableExtra(key)

                    else -> null
                }
            }
        // 带泛型参数的（ArrayList）
            is ParameterizedType -> {
                val rawType = (it.type.javaType as ParameterizedType).rawType as Class<*>
                when {
                    rawType.isSubType<Serializable>() -> intent.getSerializableExtra(key)
                    rawType.isSubType<Parcelable>() -> intent.getParcelableExtra<Parcelable>(key)

                // Array 类
                    rawType.isSubType<Array<Boolean>>() -> intent.getBooleanArrayExtra(key)
                    rawType.isSubType<Array<Byte>>() -> intent.getByteArrayExtra(key)
                    rawType.isSubType<Array<Char>>() -> intent.getCharArrayExtra(key)
                    rawType.isSubType<Array<Double>>() -> intent.getDoubleArrayExtra(key)
                    rawType.isSubType<Array<Float>>() -> intent.getFloatArrayExtra(key)
                    rawType.isSubType<Array<Int>>() -> intent.getIntArrayExtra(key)
                    rawType.isSubType<Array<Long>>() -> intent.getLongArrayExtra(key)
                    rawType.isSubType<Array<Short>>() -> intent.getShortArrayExtra(key)
                    rawType.isSubType<Array<String>>() -> intent.getStringArrayExtra(key)
                    rawType.isSubType<Array<CharSequence>>() -> intent.getCharSequenceArrayExtra(key)
                    rawType.isSubType<Array<Parcelable>>() -> intent.getParcelableArrayExtra(key)

                // ArrayList 类
                    rawType.isSubType<ArrayList<Int>>() -> intent.getIntegerArrayListExtra(key)
                    rawType.isSubType<ArrayList<Parcelable>>() -> intent.getParcelableArrayListExtra<Parcelable>(key)
                    rawType.isSubType<ArrayList<String>>() -> intent.getStringArrayListExtra(key)
                    rawType.isSubType<ArrayList<CharSequence>>() -> intent.getCharSequenceArrayListExtra(key)
                    else -> null
                }
            }
            else -> null
        }
    }

    return primaryConstructor.callBy(constructorMap)
}