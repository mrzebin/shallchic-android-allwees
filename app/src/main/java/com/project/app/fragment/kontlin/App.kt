package com.project.app.fragment.kontlin

class Greeter(val name:String){
    fun greet(){
        println("Hello $name")
    }
}

fun sum(a:Int,b:Int):Unit{
    println("a-" + a+ "-b:" +b)
}

/**
 * public 修饰的方法必须明确返回数据类型
 */
fun sumTemplate(a:Int,b:Int):Int{
    return a+b
}

/**
 * 变成参数用vararg
 */
fun vars(vararg v:Int){
    for(vI in v){
        println(vI)
    }
}

/**
 * 匿名函数
 * lambda
 */
var sumlambda:(Int,Int) ->Int={x,y->x+y}

/**
 * kotlin的空参数的处理有俩种方式:1.再后面加!!类似抛出空异常 2.再后面加?表示变量可以为null
 */

/**
 *null 的判断
 *  var age:String?="23"
    var ages=age!!.toInt()
    var age1 = age?.toInt()
    var age2 = age?.toInt()?:-1
 */

/**
 * 区间 ..
 * in/!in
 * step 步长
 * downTo 最少到多少
 * until 排除
 */

/**
 * kotlin 基本数据类型
 * byte Short int long float Double
 * 不同java String 不属于数据类型,是一个独立的数据类型
 * 长整型以L结尾
 * 0x表示16进制
 * 0b表示8进制
 *
 */


/**
 * 位操作符
 * 对于Int和Long类型，还有一系列的位操作符可以使用，分别是：shl(bits) – 左移位 (Java’s <<) shr(bits) – 右移位 (Java’s >>) ushr(bits) – 无符号右移位 (Java’s >>>)
 * and(bits) – 与
 * or(bits) – 或
 * xor(bits) – 异或
 * inv() – 反向
 */


/**
 * 数组
 * 俩种方式:
 * arrayOf()
 * 工厂函数
 *
 * var a = arrayOf(1,2,3)
 * var b = Array(3,{i->(i*2)})
 */

/**
 * 字符串
 * String 可以通过trimMargin()方法来删除空格
 */

fun parseInt(str:String): Int {
    return str.toInt()
}

fun getStringLength(obj:Any):Int?{
    //这里可以使用!is
    if(obj is String){
        return obj.length
    }
    return null
}

open class Base{
    open fun f(){

    }
}

abstract class Derived :Base(){
}

interface TestInterFace{
    fun test()
}

class Test {
    var v = "成员属性"
    fun setInterFace(test: TestInterFace) {
        test.test()
    }
}

fun MutableList<Int>.snap(index1:Int,index2:Int){
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

class C{
    fun foo(){
        println("成员函数")
    }
}

fun foo() {
    println("扩展函数")
}

fun Any?.toString():String{
    if(this == null){
        return "null"
    }
    return toString()
}

class MyClass{
    companion object
}

fun MyClass.Companion.foo(){
    println("伴生对象扩展函数foo")
}

val MyClass.Companion.no:Int
    get() = 10

class F{
    fun bar(){
        println("F class bar")
    }
}

class D{
    fun baz(){
        println("D class baz")
    }

    fun F.foo(){
        bar()
        baz()
    }

    fun caller(f:F){
        f.foo()
    }
}

class Box<T>(t:T){
    var value = t
}

fun main(){
    var box:Box<Int> = Box(1)

}