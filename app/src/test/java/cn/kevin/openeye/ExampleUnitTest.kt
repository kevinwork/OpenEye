package cn.kevin.openeye

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)


        runBlocking {  }
    }


    fun testDefault() {
        //协程构建器 默认工作者线程
        GlobalScope.launch {

            println("launch: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
            delay(1000)
            println("launch2: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        }
        println("main: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        Thread.sleep(2000)
        println("testDefault end")
    }

    /**
     * 切换至指定线程
     */
    fun testSwitchThread() {
        GlobalScope.launch {
            println("launch: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
            withContext(Dispatchers.IO) {
                println("launch IO: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
                delay(1000)
                println("launch2 IO: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
            }
            println("launch2: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        }
        println("main: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        Thread.sleep(2000)
        println("testSwitchThread end")
    }

    /**
     * 切换至指定线程
     */
    @Test
    fun testSwitchThread2() {
        GlobalScope.launch(Dispatchers.IO) {
            println("launch: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
            withContext(Dispatchers.Main) {
                println("launch IO: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
                delay(1000)
                println("launch2 IO: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
            }
            println("launch2: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        }
        println("main: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        Thread.sleep(2000)
        println("testSwitchThread end")
    }

}