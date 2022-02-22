package cn.kevin.openeye

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.time.LocalDateTime
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("cn.kevin.openeye", appContext.packageName)
    }

    @Test
    fun testSwitchThread2() {
        GlobalScope.launch(Dispatchers.Main) {
            for (index in 1..4) {
                delay(500)
                println("G1-MAIN: $index ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
            }
            withContext(Dispatchers.IO) {
                for (index in 1..4) {
                    delay(500)
                    println("G1-IO: $index ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
                }
            }
            println("G1-MAIN-2: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        }
        GlobalScope.launch(Dispatchers.Main) {
            for (index in 1..4) {
                delay(500)
                println("G2-MAIN: $index ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
            }
            withContext(Dispatchers.IO) {
                for (index in 1..4) {
                    delay(500)
                    println("G2-IO: $index ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
                }
            }
            println("G2-MAIN-2: ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        }
        for (index in 1..7) {
            Thread.sleep(1000)
            println("OUT: $index ${LocalDateTime.now()} ${Thread.currentThread().name}-${Thread.currentThread().id}")
        }
        println("testSwitchThread end")
    }

    @Test
    fun testRunBlocking() = runBlocking<Unit> {
        GlobalScope.launch {
            delay(1000)
            println("world")
        }
        println("hello ")
        delay(2000)
    }

    @Test
    fun testJob() {
        runBlocking {
            val job: Job = launch {
                delay(1000)
                println("test")
            }
            println("job")
            job.join()
        }
    }

    @Test
    fun coroutineScope() = runBlocking {
        launch {
            delay(200)
            println("launch")
        }
        coroutineScope {
            launch {
                delay(500)
                println("coroutineScope 2")
            }
            delay(100)
            println("coroutineScope")
        }
        println("runBlocking end")
    }

    @Test
    fun testExtension() = runBlocking <Unit>{
        delay(500)
        println("runBlocking")
        launch {
            testfun1()
        }
    }

    suspend fun testfun1() {
        println("start")
        coroutineScope {
            launch {
                delay(1000)
                println("launch->coroutineScope")
            }
        }
        println("end")
    }

    /**
     * cancel
     */
    @Test
    fun testCoroutinesCancel() = runBlocking<Unit> {
        //不属于runBlocking时间管理范围
        val job1 = GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO) {
                var i : Int = 0
                while (i < 5) {
                    println("coroutines running ${i++}...")
                    Thread.sleep(500)
                }
            }
            delay(1500)
            println("cancel, i am waiting...")
            job.cancelAndJoin()
            println("end...")
        }
        //阻塞当前线程直到下面代码被执行完
        println("runBlocking")
        delay(3000)
        println("end")
    }

    /**
     * cancel
     * isActive
     */
    @Test
    fun testCoroutinesCancel2() = runBlocking<Unit> {
        //不属于runBlocking时间管理范围
        val job1 = GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO) {
                var i : Int = 0
                while (isActive) {
                    println("coroutines running ${i++}...")
                    Thread.sleep(500)
                }
            }
            delay(1000)
            println("cancel, i am waiting...")
            job.cancelAndJoin()
            println("end...")
        }
        //阻塞当前线程直到下面代码被执行完
        println("runBlocking")
        delay(3000)
        println("end")
    }

    /**
     * cancel
     * exception
     */
    @Test
    fun testCoroutinesCancel3() = runBlocking<Unit> {
        //不属于runBlocking时间管理范围
        val job1 = GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO) {
                try {
                    repeat(6) { i->
                        //费挂起函数不能被取消
                        //Thread.sleep(500)
                        delay(500)
                        println("repeat:$i")
                    }
                } finally {
                    println("finally...")
                    delay(1000)

                }
            }
            delay(1000)
            println("cancel, i am waiting...")
            job.cancelAndJoin()
            println("end...")
        }
        //阻塞当前线程直到下面代码被执行完
       job1.join()
    }

    /**
     * can not cancel
     *
     */
    @Test
    fun testCoroutinesCancel4() = runBlocking<Unit> {
        //不属于runBlocking时间管理范围
        val job1 = GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO) {
                try {
                    repeat(6) { i->
                        //费挂起函数不能被取消
                        //Thread.sleep(500)
                        delay(500)
                        println("repeat:$i")
                    }
                } finally {
                    withContext(NonCancellable) {
                        println("finally...")
                        delay(1000L)
                        println("finally 1 sec...")
                    }

                }
            }
            delay(1000)
            println("cancel, i am waiting...")
            job.cancelAndJoin()
            println("end...")
        }
        //阻塞当前线程直到下面代码被执行完
        job1.join()
        println("end 2...")
    }

    /**
     *  overtime cancel itself
     *
     */
    @Test
    fun testCoroutinesCancelOvertime() = runBlocking<Unit> {
        //不属于runBlocking时间管理范围
        val job1 = GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO) {
                try {
                    withTimeout(2000) {
                        repeat(6) { i->
                            //费挂起函数不能被取消
                            //Thread.sleep(500)
                            delay(500)
                            println("repeat:$i")
                        }
                    }

                } catch (e: TimeoutCancellationException) {
                    //withTimeout取消协程时会出现这个异常，而不是由外部cancel引起
                    println("TimeoutCancellationException...")
                } finally {
                    println("finally...")
                }
            }
            delay(5000)
            println("cancel, i am waiting...")
            job.cancelAndJoin()
            println("end...")
        }
        //阻塞当前线程直到下面代码被执行完
        job1.join()
        println("end 2...")
    }

    /**
     * 输出总是0
     */
    class AsyncResource {

        companion object {
            var acquired = 0
        }


        class Resource {
            init { acquired++ } // Acquire the resource
            fun close() { acquired-- } // Release the resource
        }

        @Test
        fun testAsyncResource() {
            runBlocking {
                repeat(100_000) { // Launch 100K coroutines
                    launch {
                        var resource: Resource? = null
                        try {
                            withTimeout(60) { // Timeout of 60 ms
                                delay(50) // Delay for 50 ms
                                resource = Resource() // Acquire a resource and return it from withTimeout block
                            }
                        } finally {
                            resource?.close() // Release the resource
                        }
                    }
                }
            }
            // Outside of runBlocking all coroutines have completed
            println("acquired:$acquired") // Print the number of resources still acquired
        }
    }

    /**
     * not always 0
     *
     */
    class AsyncResourceNotZero {

        companion object {
            var acquired = 0
        }


        class Resource {
            init { acquired++ } // Acquire the resource
            fun close() { acquired-- } // Release the resource
        }

        @Test
        fun testAsyncResource() {
            runBlocking {
                repeat(100) { // Launch 100K coroutines
                    launch {
                        var resource: Resource? = null
                        withTimeout(60) { // Timeout of 60 ms
                            delay(50) // Delay for 50 ms
                            resource = Resource() // Acquire a resource and return it from withTimeout block
                        }
                        resource?.close() // Release the resource

                    }
                }
            }
            // Outside of runBlocking all coroutines have completed
            println("acquired:$acquired") // Print the number of resources still acquired
        }
    }

    /***
     * 组合挂起函数
     */
    private suspend fun doTask1(): Int {
        delay(1000)
        return 20
    }
    private suspend fun doTask2(): Int {
        delay(1500)
        return 35
    }

    private suspend fun calculateSum(): Int = coroutineScope {
        val one = async { doTask1() }
        val two = async { doTask2() }

        one.await()+two.await()
    }

    // somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulOneAsync() = GlobalScope.async {
        doTask1()
    }

    // somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulTwoAsync() = GlobalScope.async {
        doTask2()
    }

    @Test
    fun testCombineSuspendFunction() = runBlocking {
        //同步任务
        val time = measureTimeMillis {
            //串行执行
            val one = doTask1()
            val two = doTask2()
            println("answer is : ${one+two}")
        }
        println("complete : $time ms")
        //异步任务
        val time2 = measureTimeMillis {
            //并行执行
            val one = async { doTask1() }
            val two = async { doTask2() }
            one.start()
            two.start()
            println("answer2 is : ${one.await()+two.await()}")
        }
        println("complete2 : $time2 ms")
        //惰性启动异步任务 可以通过await启动，但是会串行执行
        //通过start启动会并行执行
        val time3 = measureTimeMillis {
            //并行执行
            val one = async(start = CoroutineStart.LAZY) { doTask1() }
            val two = async(start = CoroutineStart.LAZY) { doTask2() }
            //one.start()
            //two.start()
            println("answer3 is : ${one.await()+two.await()}")
        }
        println("complete3 : $time3 ms")
        //async风格 不建议 无法正确捕获异常（不是suspend）并终止协程
        val time4 = measureTimeMillis {
            // 我们可以在协程外面启动异步执行
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()
            // 但是等待结果必须调用其它的挂起或者阻塞
            // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程

            runBlocking {
                println("The answer4 is ${one.await() + two.await()}")
            }
        }
        println("Completed4 in $time4 ms")

        //结构化并发
        val time5 = measureTimeMillis {
            //并行执行
            println("answer5 is : ${calculateSum()}")
        }
        println("complete5 : $time5 ms")


    }

    /**
     * 协程上下文与调度器
     */

    //父协程的职责
    // 启动一个协程来处理某种传入请求（request）
    @Test
    fun testFatherCoroutines() = runBlocking<Unit> {
        val request = launch {
            repeat(3) { i -> // 启动少量的子作业
                launch  {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                    println("Coroutine $i is done")
                }
            }
            println("request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join() // 等待请求的完成，包括其所有子协程
        println("Now processing of the request is complete")
    }

    // 协程作用域
    class Activity {
        private val mainScope = MainScope()

        fun destroy() {
            mainScope.cancel()
        }
        // 继续运行……
        // 在 Activity 类中

        fun doSomething() {
            // 在示例中启动了 10 个协程，且每个都工作了不同的时长
            repeat(10) { i ->
                mainScope.launch {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                    println("Coroutine $i is done")
                }
            }
        }
    } // Activity 类结束

    @Test
    fun testCoroutinesScopeOperate() = runBlocking<Unit> {
        val activity = Activity()
        activity.doSomething() // 运行测试函数
        println("Launched coroutines")
        delay(600L) // 延迟半秒钟
        println("Destroying activity!")
        activity.destroy() // 取消所有的协程
        delay(1000) // 为了在视觉上确认它们没有工作
    }

    /**
     * 通道 channel
     * 流中传输值的一种方法
     * 如果channel中还有数据没有被读取，那么协程不会退出，会一直运行，需要调用cancel取消协程，才能使协程退出
     * 如果channel中数据没有被读取，不能再次send，需要取出后才能继续send，由此可见channel大小为1
     * 正确使用方式是发送一个接收一个。或者接收方阻塞等待接收，如果发送还在继续，接收却停止了，那么发送协程会阻塞在send函数
     */
    @Test
     fun testChannelDemo() = runBlocking<Unit>(Dispatchers.Main) {
        val channel = Channel<Int>()
        launch(Dispatchers.Main) {
            repeat(10) {
                x ->
                println("send ${x*x}")
                channel.send(x*x)
            }
        }
        repeat(5) {
            println("receive:${channel.receive()}")
        }
        println("end")
    }

    /**
     * 关闭通道
     * */
    @Test
    fun testCloseChannel() = runBlocking<Unit> {
        val channel = Channel<Int>()
        launch(Dispatchers.Main) {
            repeat(5) {
                    x ->
                println("send ${x*x}")
                channel.send(x*x)
            }
            channel.close()
        }
        //遍历通道
        for (ret in channel) {
            println("receive:${ret }")
        }
        println("end")
    }

    /**
     * 通道正常使用方式
     * */
    @Test
    fun testChannelNormalUsage() = runBlocking {
        val numbers = produceNumbers() // produces integers from 1 and on
        val squares = square(numbers) // squares integers
        repeat(5) {
            println(squares.receive()) // print first five
        }
        println("Done!") // we are done
        coroutineContext.cancelChildren() // cancel children coroutines
    }

    fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while (true) send(x++) // infinite stream of integers starting from 1
    }

    fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
        for (x in numbers) send(x * x)
    }




}