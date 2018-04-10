package glfw_

import glm_.vec2.Vec2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.Platform
import org.lwjgl.vulkan.VkAllocationCallbacks
import org.lwjgl.vulkan.VkInstance
import vkn.VkSurfaceKHR
import vkn.address
import vkn.check
import vkn.getLong

/**
 * Created by elect on 22/04/17.
 */

object glfw {

    fun init() {

        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        /* This window hint is required to use OpenGL 3.1+ on macOS */
        if (Platform.get() == Platform.MACOSX)
            windowHint.forwardComp = true
    }

    val vulkanSupported get() = GLFWVulkan.glfwVulkanSupported()

    fun <R> windowHint(block: windowHint.() -> R) = windowHint.block()

    val primaryMonitor get() = glfwGetPrimaryMonitor()

    val videoMode get() = glfwGetVideoMode(primaryMonitor)!!

    var start = System.nanoTime()
    val time get() = (System.nanoTime() - start) / 1e9f

    fun videoMode(monitor: Long) = glfwGetVideoMode(monitor)

    val resolution
        get() = Vec2i(videoMode.width(), videoMode.height())

    var swapInterval = 0
        set(value) = glfwSwapInterval(value)

    fun terminate() {
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    fun pollEvents() = glfwPollEvents()

    val requiredInstanceExtensions: ArrayList<String>
        get() {
            val pCount = appBuffer.intBuffer
            val ppNames = GLFWVulkan.nglfwGetRequiredInstanceExtensions(pCount.address)
            val a = GLFWVulkan.glfwGetRequiredInstanceExtensions()
            val count = pCount[0]
            val pNames = MemoryUtil.memPointerBufferSafe(ppNames, count) ?: return arrayListOf()
            val res = ArrayList<String>(count)
            for (i in 0 until count)
                res += MemoryUtil.memASCII(pNames[i])
            return res
        }

    fun createWindowSurface(window: GlfwWindow, instance: VkInstance, allocator: VkAllocationCallbacks? = null): VkSurfaceKHR =
            getLong { GLFWVulkan.glfwCreateWindowSurface(instance, window.handle, allocator, it).check() }
}

