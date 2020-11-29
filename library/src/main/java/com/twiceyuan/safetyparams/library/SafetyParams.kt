package com.twiceyuan.safetyparams.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType

/**
 * Created by twiceYuan on 2018/3/26.
 *
 * 在 Activity、Fragment 之间类型安全地传递参数
 */

sealed class SafetyParams

private fun <TargetClass : Class<*>> SafetyParams.genericTypeParam(): TargetClass {
    @Suppress("UNCHECKED_CAST")
    return (this::class.java.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0]
            as TargetClass
}

/**
 * 定义传递参数数据 Model 所要继承的类。例如：
 * ```
 * data class Starter(val name: String) : SafetyParams(SomeActivity::class.java)
 * ```
 */
abstract class ActivityParams<ActivityType : Activity> : SafetyParams() {

    private val targetClass: Class<ActivityType> by lazy { genericTypeParam() }

    fun launch(context: Context) {
        context.startActivityWithArgs(targetClass, this)
    }

    @Suppress("unused")
    fun intent(context: Context) = Intent(context, targetClass).apply {
        putExtras(this@ActivityParams.toBundle())
    }
}

abstract class FragmentParams<FragmentType : Fragment> : SafetyParams() {

    private val targetClass: Class<FragmentType> by lazy { genericTypeParam() }

    @Suppress("UNCHECKED_CAST")
    fun newInstance(): FragmentType {
        return targetClass.newInstance().apply {
            arguments = this@FragmentParams.toBundle()
        }
    }
}

/**
 * 判断当前类型是否为 T 的子类型
 */
inline fun <reified T> Class<*>.isSubType() = T::class.java.isAssignableFrom(this)

/**
 * 利用 SafetyParams 启动一个 Activity
 */
private fun Context.startActivityWithArgs(targetClass: Class<out Activity>, args: SafetyParams) {
    val intent = Intent(this, targetClass)
    intent.putExtras(args.toBundle())
    startActivity(intent)
}

/**
 * SafetyParams 对象转换为一个 Bundle
 */
private fun SafetyParams.toBundle(): Bundle {

    val bundle = Bundle()

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

    this::class.java.declaredFields.forEach { field ->
        /**
         * 根据 args 中定义的成员类型和名称存储到 intent 中
         */
        when {
            field.type.isSubType<String>() -> {
                bundle.putString(field.name, field.read<String>(this))
                return@forEach
            }

            field.type.isSubType<CharSequence>() -> {
                bundle.putCharSequence(field.name, field.read<CharSequence>(this))
                return@forEach
            }

            field.type.isSubType<Parcelable>() -> {
                bundle.putParcelable(field.name, field.read<Parcelable>(this))
                return@forEach
            }

            // Array 类
            field.type.isSubType<Array<Boolean>>() -> {
                bundle.putBooleanArray(field.name, field.read<BooleanArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<Byte>>() -> {
                bundle.putByteArray(field.name, field.read<ByteArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<Char>>() -> {
                bundle.putCharArray(field.name, field.read<CharArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<Double>>() -> {
                bundle.putDoubleArray(field.name, field.read<DoubleArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<Float>>() -> {
                bundle.putFloatArray(field.name, field.read<FloatArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<Int>>() -> {
                bundle.putIntArray(field.name, field.read<IntArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<Long>>() -> {
                bundle.putLongArray(field.name, field.read<LongArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<Short>>() -> {
                bundle.putShortArray(field.name, field.read<ShortArray>(this))
                return@forEach
            }
            field.type.isSubType<Array<CharSequence>>() -> {
                bundle.putCharSequenceArray(field.name, field.read<Array<CharSequence>>(this))
                return@forEach
            }
            field.type.isSubType<Array<String>>() -> {
                bundle.putStringArray(field.name, field.read<Array<String>>(this))
                return@forEach
            }
            field.type.isSubType<Array<Parcelable>>() -> {
                bundle.putParcelableArray(field.name, field.read<Array<Parcelable>>(this))
                return@forEach
            }

            // ArrayList 类
            field.type.isSubType<ArrayList<Int>>() -> {
                bundle.putIntegerArrayList(field.name, field.read<ArrayList<Int>>(this))
                return@forEach
            }
            field.type.isSubType<ArrayList<Parcelable>>() -> {
                bundle.putParcelableArrayList(field.name, field.read<ArrayList<Parcelable>>(this))
                return@forEach
            }
            field.type.isSubType<ArrayList<String>>() -> {
                bundle.putStringArrayList(field.name, field.read<ArrayList<String>>(this))
                return@forEach
            }
            field.type.isSubType<ArrayList<CharSequence>>() -> {
                bundle.putCharSequenceArrayList(field.name, field.read<ArrayList<CharSequence>>(this))
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
                bundle.putSerializable(field.name, field.read<Serializable>(this))
                return@forEach
            }

            else -> {
                throw DataBeanNotLegalException(
                        "${this.javaClass.canonicalName} 中定义了 Bundle 不支持存储的类型: " +
                                "成员 ${field.type.canonicalName}[${field.name}]"
                )
            }
        }
    }
    return bundle
}

class DataBeanNotLegalException(msg: String) : RuntimeException(msg)

inline fun <reified Data : SafetyParams> Activity.parseParams(): Lazy<Data> {
    return lazy {
        intent?.extras?.toArgs() ?: throw java.lang.RuntimeException("No arguments passed.")
    }
}

inline fun <reified Data : SafetyParams> Fragment.parseParams(): Lazy<Data> {
    return lazy {
        arguments?.toArgs() ?: throw java.lang.RuntimeException("No arguments passed.")
    }
}

inline fun <reified Data : SafetyParams> Bundle.toArgs(): Data {

    val dataClass = Data::class.java

    val constructorMap = hashMapOf<KParameter, Any?>()

    val primaryConstructor = dataClass.kotlin.constructors.firstOrNull()
            ?: throw DataBeanNotLegalException("数据类应该有一个主构造器")

    if (primaryConstructor.parameters.isEmpty())
        throw DataBeanNotLegalException("数据类应该至少有一个构造器参数")

    primaryConstructor.parameters.forEach {

        val key = it.name
        constructorMap[it] = when (it.type.javaType) {
            // 原始类型
            is Class<*> -> {
                val typeClass = it.type.javaType as Class<*>
                when {
                    typeClass.isSubType<String>() -> getString(key)
                    typeClass.isSubType<Parcelable>() -> getParcelable<Parcelable>(key)

                    arrayOf(
                            typeClass.isSubType<Int>(),
                            typeClass.isSubType<Boolean>(),
                            typeClass.isSubType<Double>(),
                            typeClass.isSubType<Long>(),
                            typeClass.isSubType<Short>(),
                            typeClass.isSubType<Float>(),
                            typeClass.isSubType<Byte>(),
                            typeClass.isSubType<Serializable>()
                    ).any() -> getSerializable(key)

                    else -> null
                }
            }
            // 带泛型参数的（ArrayList）
            is ParameterizedType -> {
                val rawType = (it.type.javaType as ParameterizedType).rawType as Class<*>
                when {
                    rawType.isSubType<Serializable>() -> getSerializable(key)
                    rawType.isSubType<Parcelable>() -> getParcelable<Parcelable>(key)

                    // Array 类
                    rawType.isSubType<Array<Boolean>>() -> getBooleanArray(key)
                    rawType.isSubType<Array<Byte>>() -> getByteArray(key)
                    rawType.isSubType<Array<Char>>() -> getCharArray(key)
                    rawType.isSubType<Array<Double>>() -> getDoubleArray(key)
                    rawType.isSubType<Array<Float>>() -> getFloatArray(key)
                    rawType.isSubType<Array<Int>>() -> getIntArray(key)
                    rawType.isSubType<Array<Long>>() -> getLongArray(key)
                    rawType.isSubType<Array<Short>>() -> getShortArray(key)
                    rawType.isSubType<Array<String>>() -> getStringArray(key)
                    rawType.isSubType<Array<CharSequence>>() -> getCharSequenceArray(key)
                    rawType.isSubType<Array<Parcelable>>() -> getParcelableArray(key)

                    // ArrayList 类
                    rawType.isSubType<ArrayList<Int>>() -> getIntegerArrayList(key)
                    rawType.isSubType<ArrayList<Parcelable>>() -> getParcelableArrayList<Parcelable>(key)
                    rawType.isSubType<ArrayList<String>>() -> getStringArrayList(key)
                    rawType.isSubType<ArrayList<CharSequence>>() -> getCharSequenceArrayList(key)
                    else -> null
                }
            }
            else -> null
        }
    }

    return primaryConstructor.callBy(constructorMap)
}