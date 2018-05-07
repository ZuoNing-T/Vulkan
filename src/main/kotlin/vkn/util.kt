package vkn

import glfw_.advance
import glfw_.appBuffer
import glfw_.appBuffer.ptr
import gli_.extension
import glm_.BYTES
import glm_.i
import glm_.mat3x3.Mat3
import glm_.mat4x4.Mat4
import glm_.set
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import graphics.scenery.spirvcrossj.*
import org.lwjgl.PointerBuffer
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.Pointer
import org.lwjgl.system.Struct
import org.lwjgl.system.StructBuffer
import org.lwjgl.vulkan.VK10
import org.lwjgl.vulkan.VkDevice
import org.lwjgl.vulkan.VkPhysicalDevice
import uno.buffer.bufferBig
import uno.kotlin.buffers.indices
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.findAnnotation


//fun pointerBufferOf(vararg strings: String): PointerBuffer {
//    val buf = pointerBufferBig(strings.size)
//    for (i in strings.indices)
//        buf[i] = strings[i]
//    return buf
//}
//
//operator fun PointerBuffer.set(index: Int, string: String) {
//    put(index, string.memUTF16)
//}
inline operator fun PointerBuffer.set(index: Int, long: Long) {
    put(index, long)
}

inline operator fun PointerBuffer.set(index: Int, pointer: Pointer) {
    put(index, pointer)
}

//operator fun PointerBuffer.plusAssign(string: String) {
//    put(string.stackUTF16)
//}

//operator fun <T> PointerBuffer.plusAssign(elements: Iterable<T>) {
//    for (item in elements)
//        if (item is String)
//            put(item.memUTF16)
//        else
//            throw Error()
//}
//
//fun PointerBuffer.isNotEmpty() = position() > 0

typealias VkBuffer = Long
typealias VkBufferView = Long
typealias VkCommandPool = Long
typealias VkDebugReportCallback = Long
typealias VkDescriptorPool = Long
typealias VkDescriptorSet = Long
typealias VkDescriptorSetLayout = Long
typealias VkDeviceMemory = Long
typealias VkDeviceSize = Long
typealias VkEvent = Long
typealias VkFence = Long
typealias VkFramebuffer = Long
typealias VkImage = Long
typealias VkImageView = Long
typealias VkPipeline = Long
typealias VkPipelineCache = Long
typealias VkPipelineLayout = Long
typealias VkQueryPool = Long
typealias VkRenderPass = Long
typealias VkSampler = Long
typealias VkSemaphore = Long
typealias VkShaderModule = Long
typealias VkSurfaceKHR = Long
typealias VkSwapchainKHR = Long

typealias VkBufferBuffer = LongBuffer
typealias VkDescriptorSetBuffer = LongBuffer
typealias VkDeviceMemoryBuffer = LongBuffer
typealias VkDeviceSizeBuffer = LongBuffer
typealias VkSemaphoreBuffer = LongBuffer
typealias VkSwapchainKhrBuffer = LongBuffer
typealias VkResultBuffer = IntBuffer
typealias VkSamplerBuffer = LongBuffer
typealias VkImageViewBuffer = LongBuffer

typealias VkImageArray = LongArray
typealias VkImageViewArray = LongArray


object LongArrayList {
    operator fun ArrayList<Long>.set(index: Int, long: LongBuffer) {
        set(index, long[0])
    }

    infix fun ArrayList<Long>.resize(newSize: Int) {
        if (size < newSize)
            for (i in size until newSize)
                add(NULL)
        else if (size > newSize)
            for (i in size downTo newSize + 1)
                removeAt(lastIndex)
    }
}

object VkPhysicalDeviceArrayList {
//    operator fun ArrayList<VkPhysicalDevice>.set(index: Int, long: LongBuffer) {
//        set(index, long[0])
//    }

    infix fun ArrayList<VkPhysicalDevice>.resize(newSize: Int) {
        if (size < newSize) TODO()
//            for (i in size until newSize)
//                add(VkPhysicalDevice())
        else if (size > newSize)
            for (i in size downTo newSize + 1)
                removeAt(lastIndex)
    }
}


inline fun vkDestroySemaphores(device: VkDevice, semaphores: VkSemaphoreBuffer) {
    for (i in 0 until semaphores.remaining())
        VK10.nvkDestroySemaphore(device, semaphores[i], NULL)
}


inline fun vkDestroyBuffer(device: VkDevice, buffer: VkBuffer) = VK10.nvkDestroyBuffer(device, buffer, NULL)


val FloatBuffer.adr get() = MemoryUtil.memAddress(this)
val IntBuffer.adr get() = MemoryUtil.memAddress(this)

inline val Pointer.adr get() = address()


fun PointerBuffer?.toArrayList(): ArrayList<String> {
    val count = this?.remaining() ?: 0
    if (this == null || count == 0) return arrayListOf()
    val res = ArrayList<String>(count)
    for (i in 0 until count)
        res += get(i).utf8
    return res
}

fun Collection<String>.toPointerBuffer(): PointerBuffer {
    val pointers = PointerBuffer.create(ptr.advance(Pointer.POINTER_SIZE * size), size)
    for (i in indices)
        pointers.put(i, elementAt(i).utf8)
    return pointers
}


fun glslToSpirv(path: Path): ByteBuffer {

    var compileFail = false
    var linkFail = false
    val program = TProgram()

    val code = Files.readAllLines(path).joinToString("\n")

    val extension = path.extension
    val shaderType = when (extension) {
        "vert" -> EShLanguage.EShLangVertex
        "frag" -> EShLanguage.EShLangFragment
        "geom" -> EShLanguage.EShLangGeometry
        "tesc" -> EShLanguage.EShLangTessControl
        "tese" -> EShLanguage.EShLangTessEvaluation
        "comp" -> EShLanguage.EShLangCompute
        else -> throw RuntimeException("Unknown shader extension .$extension")
    }

    println("${path.fileName}: Compiling shader code  (${code.length} bytes)... ")

    val shader = TShader(shaderType).apply {
        setStrings(arrayOf(code), 1)
        setAutoMapBindings(true)
    }

    val messages = EShMessages.EShMsgDefault or EShMessages.EShMsgVulkanRules or EShMessages.EShMsgSpvRules

    val resources = libspirvcrossj.getDefaultTBuiltInResource()
    if (!shader.parse(resources, 450, false, messages))
        compileFail = true

    if (compileFail) {
        println("Info log: " + shader.infoLog)
        println("Debug log: " + shader.infoDebugLog)
        throw RuntimeException("Compilation of ${path.fileName} failed")
    }

    program.addShader(shader)

    if (!program.link(EShMessages.EShMsgDefault) || !program.mapIO())
        linkFail = true

    if (linkFail) {
        System.err.println(program.infoLog)
        System.err.println(program.infoDebugLog)

        throw RuntimeException("Linking of program ${path.fileName} failed!")
    }


    val spirv = IntVec()
    libspirvcrossj.glslangToSpv(program.getIntermediate(shaderType), spirv)

    println("Generated " + spirv.capacity() + " bytes of SPIRV bytecode.")

    //System.out.println(shader);
    //System.out.println(program);

    return spirv.toByteBuffer()
}

private fun IntVec.toByteBuffer(): ByteBuffer {
    val bytes = appBuffer.buffer(size().i * Int.BYTES)
    val ints = bytes.asIntBuffer()
    for (i in ints.indices)
        ints[i] = get(i).i
    withLong { }
    return bytes
}


operator fun <T : Struct, SELF : StructBuffer<T, SELF>> StructBuffer<T, SELF>.set(index: Int, value: T) {
    put(index, value)
}

inline fun <R> withAddress(address: Long, block: WithAddress.() -> R): R {
    WithAddress.address = address
    WithAddress.offset = 0
    return WithAddress.block()
}

object WithAddress {

    var address = NULL
    var offset = 0

    fun addMat4(mat4: Mat4) {
        for (i in 0..3)
            for (j in 0..3) {
                memPutFloat(address + offset, mat4[i, j])
                offset += Float.BYTES
            }
    }

    fun addMat3(mat3: Mat3) {
        for (i in 0..2)
            for (j in 0..2) {
                memPutFloat(address + offset, mat3[i, j])
                offset += Float.BYTES
            }
    }

    fun addVec4(vec4: Vec4) {
        for (i in 0..3) {
            memPutFloat(address + offset, vec4[i])
            offset += Float.BYTES
        }
    }

    fun addVec3(vec3: Vec3) {
        for (i in 0..2) {
            memPutFloat(address + offset, vec3[i])
            offset += Float.BYTES
        }
    }

    fun addFloat(float: Float) {
        memPutFloat(address + offset, float)
        offset += Float.BYTES
    }

    fun addInt(int: Int) {
        memPutInt(address + offset, int)
        offset += Int.BYTES
    }
}


abstract class Bufferizable {

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Order(val value: Int)

    val fieldOrderDefault: Array<String> by lazy {
        val properties = this::class.declaredMemberProperties
        val parts = properties.partition { it.findAnnotation<Order>() == null }
        val plain = parts.first.sortedBy { it.name }
        val annotated = parts.second.associateBy { it.findAnnotation<Order>()!!.value }
        val list = ArrayList<KProperty1<*, *>>()
        var plainIdx = 0
        for (i in properties.indices)
            list += annotated[i] ?: plain[plainIdx++]
        list.map { it.name }.toTypedArray()
    }

    open var fieldOrder = emptyArray<String>()
        get() = if (field.isEmpty()) fieldOrderDefault else field

    open val size: Int by lazy {
        fieldOrder.sumBy { field ->
            val member = this::class.declaredMemberProperties.find { it.name == field }!!
            when (member.returnType) {
                Mat4::class.defaultType -> Mat4.size
                Mat3::class.defaultType -> Mat3.size
                Vec4::class.defaultType -> Vec4.size
                Vec3::class.defaultType -> Vec3.size
                Float::class.defaultType -> Float.BYTES
                Int::class.defaultType -> Int.BYTES
                else -> throw Error(member.returnType.toString())
            }
        }
    }

    open infix fun to(address: Long) {

        WithAddress.address = address
        WithAddress.offset = 0

        for (i in data.indices)
            data[i].first(data[i].second.getter.call(this)!!)
    }

    infix fun from(address: Long): Unit = TODO()

    val data: Array<BufferizableData> by lazy {
        Array(fieldOrder.size) {
            val field = fieldOrder[it]
            val member = this::class.declaredMemberProperties.find { it.name == field }!!
            val func = when (member.returnType) {
                Mat4::class.defaultType -> WithAddress::addMat4
                Mat3::class.defaultType -> WithAddress::addMat3
                Vec4::class.defaultType -> WithAddress::addVec4
                Vec3::class.defaultType -> WithAddress::addVec3
                Float::class.defaultType -> WithAddress::addFloat
                Int::class.defaultType -> WithAddress::addInt
                else -> throw Error(member.returnType.toString())
            } as BufferizableAddFunctionType
            func to member
        }
    }
}

typealias BufferizableAddFunctionType = (Any) -> Unit
typealias BufferizableData = Pair<BufferizableAddFunctionType, KProperty1<out Bufferizable, Any?>>

fun bufferOf(vararg data: Bufferizable): ByteBuffer {
    val size = data.sumBy { it.size }
    val res = bufferBig(size)
    val address = memAddress(res)
    var offset = 0
    for (i in data.indices) {
        data[i] to address + offset
        offset += data[i].size
    }
    return res
}

//object uboVS : Bufferizable() {
//
//    var projectionMatrix = Mat4()
//    var modelMatrix = Mat4()
//    var viewMatrix = Mat4()
//
//    override val fieldOrder = arrayOf("projectionMatrix", "modelMatrix", "viewMatrix")
//
//    override infix fun to(address: Long) {
//        withAddress(address) {
//            //            add(projectionMatrix); add(modelMatrix); add(viewMatrix)
//        }
//    }
//}
//
//fun main(args: Array<String>) {
//    println(uboVS::class.declaredMemberProperties)
//    val member = uboVS::class.declaredMemberProperties.find { it.name == "projectionMatrix" }!!
//    println(member.returnType)
//    println(member.get(uboVS) as Mat4)
//    println(member.returnType == Mat4::class.defaultType)
//}

class FiledOrder {
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Order(val value: Int)

    @Order(3)
    var field1: Int = 0
    @Order(1)
    var field2: Int = 0
    // no annotation
    var field4: Int = 0
    var field3: Int = 0

    @Order(1)
    fun start() {
    }

    @Order(2)
    fun end() {
    }
}

fun main(args: Array<String>) {
    val properties = FiledOrder::class.declaredMemberProperties
    val parts = properties.partition { it.findAnnotation<FiledOrder.Order>() == null }
    val plain = parts.first.sortedBy { it.name }
    val annotated = parts.second.associateBy { it.findAnnotation<FiledOrder.Order>()!!.value }
    val list = ArrayList<KProperty1<*, *>>()
    var plainIdx = 0
    for (i in properties.indices)
        list += annotated[i] ?: plain[plainIdx++]
    println(list)
}