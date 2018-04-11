package com.twiceyuan.activityargs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

/**
 * Created by twiceYuan on 2018/3/26.
 *
 * Pass the args from intent
 */
abstract class ActivityArgs {
    fun launch(context: Context) {
        context.startActivityWithArgs(targetClass(), this)
    }

    abstract fun targetClass(): Class<out Activity>
}

inline fun <reified T> Class<*>.isSubType() = T::class.java.isAssignableFrom(this)

fun Context.startActivityWithArgs(targetClass: Class<out Activity>, args: Any) {
    val intent = Intent(this, targetClass)

    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    fun Field.read(obj: Any): Any? {
        return if (isAccessible) {
            get(obj)
        } else {
            isAccessible = true
            val temp = get(obj)
            isAccessible = false
            temp
        }
    }

    args::class.java.declaredFields.forEach { field ->
        @Suppress("UNCHECKED_CAST")
        when {
        // String 类型单独存储，因为可以区分 null
            field.type.isSubType<String>() -> {
                intent.putExtra(field.name, field.read(args) as String)
            }


        // Array 类
            field.type.isSubType<Array<Boolean>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Boolean>)
                return@forEach
            }
            field.type.isSubType<Array<Byte>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Byte>)
                return@forEach
            }
            field.type.isSubType<Array<Char>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Char>)
                return@forEach
            }
            field.type.isSubType<Array<Double>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Double>)
                return@forEach
            }
            field.type.isSubType<Array<Float>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Float>)
                return@forEach
            }
            field.type.isSubType<Array<Int>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Int>)
                return@forEach
            }
            field.type.isSubType<Array<Long>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Long>)
                return@forEach
            }
            field.type.isSubType<Array<Short>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Short>)
                return@forEach
            }
            field.type.isSubType<Array<CharSequence>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<CharSequence>)
                return@forEach
            }
            field.type.isSubType<Array<String>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<String>)
                return@forEach
            }
            field.type.isSubType<Array<Parcelable>>() -> {
                intent.putExtra(field.name, field.read(args) as Array<Parcelable>)
                return@forEach
            }

        // ArrayList 类
            field.type.isSubType<ArrayList<Int>>() -> {
                intent.putExtra(field.name, field.read(args) as ArrayList<Int>)
                return@forEach
            }
            field.type.isSubType<ArrayList<Parcelable>>() -> {
                intent.putExtra(field.name, field.read(args) as ArrayList<Parcelable>)
                return@forEach
            }
            field.type.isSubType<ArrayList<String>>() -> {
                intent.putExtra(field.name, field.read(args) as ArrayList<String>)
                return@forEach
            }
            field.type.isSubType<ArrayList<CharSequence>>() -> {
                intent.putExtra(field.name, field.read(args) as ArrayList<CharSequence>)
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
                intent.putExtra(field.name, field.read(args) as Serializable)
            }

        // Parcelable 以及嵌套 Parcelable
            field.type.isSubType<Parcelable>() -> {
                intent.putExtra(field.name, field.read(args) as Parcelable)
                return@forEach
            }
        }
    }
    startActivity(intent)
}

class DataBeanNotLegalException(msg: String) : RuntimeException(msg)

inline fun <reified Data : ActivityArgs> Activity.parseActivityArgs(): Data {

    val constructorMap = hashMapOf<KParameter, Any?>()

    val primaryConstructor = Data::class.primaryConstructor ?: throw DataBeanNotLegalException(
            "数据类应该有一个主构造器"
    )

    if (primaryConstructor.parameters.isEmpty()) throw DataBeanNotLegalException(
            "数据类应该至少有一个构造器参数"
    )

    primaryConstructor.parameters.forEach {

        constructorMap[it] = when (it.type.javaType) {
        // 原始类型
            is Class<*> -> when (it.type.classifier) {
                String::class -> {
                    intent.getStringExtra(it.name)
                }
                Int::class,
                Boolean::class,
                Double::class,
                Long::class,
                Short::class,
                Float::class,
                Byte::class,
                Serializable::class -> {
                    intent.getSerializableExtra(it.name)
                }
                else -> null
            }
        // 带泛型参数的（ArrayList）
            is ParameterizedType -> {
                val rawType = (it.type.javaType as ParameterizedType).rawType as Class<*>
                when {
                    rawType.isSubType<Serializable>() -> intent.getSerializableExtra(it.name)
                    rawType.isSubType<Parcelable>() -> intent.getParcelableExtra<Parcelable>(it.name)

                // Array 类
                    rawType.isSubType<Array<Boolean>>() -> intent.getBooleanArrayExtra(it.name)
                    rawType.isSubType<Array<Byte>>() -> intent.getByteArrayExtra(it.name)
                    rawType.isSubType<Array<Char>>() -> intent.getCharArrayExtra(it.name)
                    rawType.isSubType<Array<Double>>() -> intent.getDoubleArrayExtra(it.name)
                    rawType.isSubType<Array<Float>>() -> intent.getFloatArrayExtra(it.name)
                    rawType.isSubType<Array<Int>>() -> intent.getIntArrayExtra(it.name)
                    rawType.isSubType<Array<Long>>() -> intent.getLongArrayExtra(it.name)
                    rawType.isSubType<Array<Short>>() -> intent.getShortArrayExtra(it.name)
                    rawType.isSubType<Array<CharSequence>>() -> intent.getCharSequenceArrayExtra(it.name)
                    rawType.isSubType<Array<String>>() -> intent.getStringArrayExtra(it.name)
                    rawType.isSubType<Array<Parcelable>>() -> intent.getParcelableArrayExtra(it.name)

                // ArrayList 类
                    rawType.isSubType<ArrayList<Int>>() -> intent.getIntegerArrayListExtra(it.name)
                    rawType.isSubType<ArrayList<Parcelable>>() -> intent.getParcelableArrayListExtra<Parcelable>(it.name)
                    rawType.isSubType<ArrayList<String>>() -> intent.getStringArrayListExtra(it.name)
                    rawType.isSubType<ArrayList<CharSequence>>() -> intent.getCharSequenceArrayListExtra(it.name)
                    else -> null
                }
            }
            else -> null
        }
    }

    return primaryConstructor.callBy(constructorMap)
}