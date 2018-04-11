package vkn

import glfw_.advance
import glfw_.appBuffer
import glfw_.appBuffer.ptr
import glm_.BYTES
import glm_.f
import glm_.vec2.Vec2i
import glm_.vec2.Vec2t
import org.lwjgl.PointerBuffer
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.Pointer
import org.lwjgl.vulkan.*
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer
import kotlin.reflect.KMutableProperty0

object vk {

    inline fun ApplicationInfo(block: VkApplicationInfo.() -> Unit): VkApplicationInfo = VkApplicationInfo.create(ptr.advance(VkApplicationInfo.SIZEOF)).also(block)

    inline fun InstanceCreateInfo(block: VkInstanceCreateInfo.() -> Unit): VkInstanceCreateInfo = VkInstanceCreateInfo.create(ptr.advance(VkInstanceCreateInfo.SIZEOF)).also(block)

    inline fun DebugReportCallbackCreateInfoEXT(block: VkDebugReportCallbackCreateInfoEXT.() -> Unit): VkDebugReportCallbackCreateInfoEXT = VkDebugReportCallbackCreateInfoEXT.create(ptr.advance(VkDebugReportCallbackCreateInfoEXT.SIZEOF)).also(block)

    inline fun DeviceQueueCreateInfo(block: VkDeviceQueueCreateInfo.() -> Unit): VkDeviceQueueCreateInfo = VkDeviceQueueCreateInfo.create(ptr.advance(VkDeviceQueueCreateInfo.SIZEOF)).also(block)
    inline fun DeviceQueueCreateInfo(capacity: Int): VkDeviceQueueCreateInfo.Buffer = VkDeviceQueueCreateInfo.create(ptr.advance(VkDeviceQueueCreateInfo.SIZEOF * capacity), capacity)

    inline fun SurfaceFormatKHR(capacity: Int): VkSurfaceFormatKHR.Buffer = VkSurfaceFormatKHR.create(ptr.advance(VkSurfaceFormatKHR.SIZEOF * capacity), capacity)

    inline fun DeviceCreateInfo(block: VkDeviceCreateInfo.() -> Unit): VkDeviceCreateInfo = VkDeviceCreateInfo.create(ptr.advance(VkDeviceCreateInfo.SIZEOF)).also(block)

    inline fun CommandPoolCreateInfo(block: VkCommandPoolCreateInfo.() -> Unit): VkCommandPoolCreateInfo = VkCommandPoolCreateInfo.create(ptr.advance(VkCommandPoolCreateInfo.SIZEOF)).also(block)

    inline fun FormatProperties(block: VkFormatProperties.() -> Unit): VkFormatProperties = VkFormatProperties.create(ptr.advance(VkFormatProperties.SIZEOF)).also(block)

    inline fun SemaphoreCreateInfo(block: VkSemaphoreCreateInfo.() -> Unit): VkSemaphoreCreateInfo = VkSemaphoreCreateInfo.create(ptr.advance(VkSemaphoreCreateInfo.SIZEOF)).also(block)

    inline fun SubmitInfo(block: VkSubmitInfo.() -> Unit): VkSubmitInfo = VkSubmitInfo.create(ptr.advance(VkSubmitInfo.SIZEOF)).also(block)

    inline fun SurfaceCapabilitiesKHR(block: VkSurfaceCapabilitiesKHR.() -> Unit): VkSurfaceCapabilitiesKHR = VkSurfaceCapabilitiesKHR.create(ptr.advance(VkSurfaceCapabilitiesKHR.SIZEOF)).also(block)

    inline fun Extent2D(block: VkExtent2D.() -> Unit): VkExtent2D = VkExtent2D.create(ptr.advance(VkExtent2D.SIZEOF)).also(block)


    inline fun ExtensionProperties(capacity: Int): VkExtensionProperties.Buffer = VkExtensionProperties.create(ptr.advance(VkExtensionProperties.SIZEOF * capacity), capacity)
    inline fun createCommandPool(device: VkDevice, createInfo: VkCommandPoolCreateInfo, commandPool: LongBuffer) = VkResult of VK10.nvkCreateCommandPool(device, createInfo.adr, NULL, memAddress(commandPool))


    inline fun createInstance(createInfo: VkInstanceCreateInfo, instance: KMutableProperty0<VkInstance>): VkResult {
        val pInstance = appBuffer.pointer
        val res = VK10.nvkCreateInstance(createInfo.adr, NULL, pInstance)
        instance.set(VkInstance(MemoryUtil.memGetLong(pInstance), createInfo))
        return VkResult of res
    }

    inline fun createDebugReportCallbackEXT(instance: VkInstance, createInfo: VkDebugReportCallbackCreateInfoEXT,
                                            callback: KMutableProperty0<Long>): VkResult {
        val long = appBuffer.long
        return VkResult of EXTDebugReport.nvkCreateDebugReportCallbackEXT(instance, createInfo.adr, NULL, long).also {
            callback.set(MemoryUtil.memGetLong(long))
        }
    }

    inline fun enumeratePhysicalDevices(instance: VkInstance): ArrayList<VkPhysicalDevice> {
        // Physical device
        val pCount = appBuffer.int
        // Get number of available physical devices
        VK_CHECK_RESULT(VK10.nvkEnumeratePhysicalDevices(instance, pCount, NULL))
        // Enumerate devices
        val count = memGetInt(pCount)
        val devices = appBuffer.pointerBuffer(count)
        VK_CHECK_RESULT(VK10.nvkEnumeratePhysicalDevices(instance, pCount, devices.adr))
        val res = arrayListOf<VkPhysicalDevice>()
        for (i in 0 until count)
            res += VkPhysicalDevice(devices[i], instance)
        return res
    }

    inline fun getPhysicalDeviceQueueFamilyProperties(physicalDevice: VkPhysicalDevice): ArrayList<VkQueueFamilyProperties> {
        val pCount = appBuffer.int
        VK10.nvkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, pCount, NULL)
        val count = memGetInt(pCount)
        val pQueueFamilyProperties = VkQueueFamilyProperties.calloc(count)
        VK10.nvkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, pCount, pQueueFamilyProperties.adr)
        return pQueueFamilyProperties.toCollection(arrayListOf())
    }

    inline fun enumerateDeviceExtensionProperties(physicalDevice: VkPhysicalDevice, layerName: String? = null): ArrayList<String> {
        val pCount = appBuffer.int
        val pLayerName = layerName?.utf8?.let(::memAddress) ?: NULL
        VK_CHECK_RESULT(VK10.nvkEnumerateDeviceExtensionProperties(physicalDevice, pLayerName, pCount, NULL))
        val count = memGetInt(pCount)
        val res = ArrayList<String>(count)
        if (count > 0) {
            val properties = ExtensionProperties(count)
            VK_CHECK_RESULT(VK10.nvkEnumerateDeviceExtensionProperties(physicalDevice, pLayerName, pCount, properties.adr))
            properties.map { it.extensionNameString() }.toCollection(res)
        }
        return res
    }

    inline fun createDevice(physicalDevice: VkPhysicalDevice, createInfo: VkDeviceCreateInfo, device: KMutableProperty0<VkDevice?>)
            : VkResult {
        val pDevice = appBuffer.pointer
        return VkResult of VK10.nvkCreateDevice(physicalDevice, createInfo.adr, NULL, pDevice).also {
            device.set(VkDevice(memGetLong(pDevice), physicalDevice, createInfo))
        }
    }

    inline fun getDeviceQueue(device: VkDevice, queueFamilyIndex: Int, queueIndex: Int, queue: KMutableProperty0<VkQueue>) {
        val pQueue = appBuffer.pointer
        VK10.nvkGetDeviceQueue(device, queueFamilyIndex, queueIndex, pQueue)
        queue.set(VkQueue(memGetLong(pQueue), device))
    }

    inline fun getPhysicalDeviceFormatProperties(physicalDevice: VkPhysicalDevice, format: VkFormat): VkFormatProperties =
            FormatProperties {
                VK10.nvkGetPhysicalDeviceFormatProperties(physicalDevice, format.i, adr)
            }

    inline fun createSemaphore(device: VkDevice, createInfo: VkSemaphoreCreateInfo, semaphore: KMutableProperty0<VkSemaphore>): VkResult {
        val pSemaphore = appBuffer.long
        return VkResult of VK10.nvkCreateSemaphore(device, createInfo.adr, NULL, pSemaphore).also {
            semaphore.set(pSemaphore)
        }
    }

    inline fun createSemaphore(device: VkDevice, createInfo: VkSemaphoreCreateInfo, semaphore: VkSemaphorePtr) =
            VkResult of VK10.nvkCreateSemaphore(device, createInfo.adr, NULL, memAddress(semaphore))

    inline fun getPhysicalDeviceSurfaceFormatsKHR(physicalDevice: VkPhysicalDevice, surface: VkSurfaceKHR): ArrayList<VkSurfaceFormatKHR> {
        val pCount = appBuffer.int
        VK_CHECK_RESULT(KHRSurface.nvkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice, surface, pCount, NULL))
        val count = memGetInt(pCount)
        assert(count > 0)
        val surfaceFormats = SurfaceFormatKHR(count)
        VK_CHECK_RESULT(KHRSurface.nvkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice, surface, pCount, surfaceFormats.adr))
        return surfaceFormats.toCollection(arrayListOf())
    }

    inline fun createCommandPool(device: VkDevice, createInfo: VkCommandPoolCreateInfo, commandPool: KMutableProperty0<VkCommandPool>): VkResult {
        val pCommandPool = appBuffer.long
        return VkResult of VK10.nvkCreateCommandPool(device, createInfo.adr, NULL, pCommandPool).also {
            commandPool.set(memGetLong(pCommandPool))
        }
    }

    inline fun getPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice: VkPhysicalDevice, surface: VkSurfaceKHR): VkSurfaceCapabilitiesKHR =
            SurfaceCapabilitiesKHR {
                VK_CHECK_RESULT(KHRSurface.nvkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice, surface, adr))
            }

    inline fun getPhysicalDeviceSurfacePresentModesKHR(physicalDevice: VkPhysicalDevice, surface: VkSurfaceKHR): ArrayList<VkPresentMode> {
        val pCount = appBuffer.int
        VK_CHECK_RESULT(KHRSurface.nvkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice, surface, pCount, NULL))
        val count = memGetInt(pCount)
        assert(count > 0)
        val presentModes = appBuffer.intArray(count)
        KHRSurface.nvkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice, surface, pCount, presentModes)
        val res = ArrayList<VkPresentMode>()
        for (i in 0 until count) res += VkPresentMode of memGetInt(presentModes + Int.BYTES * i)
        return res
    }
}

inline var VkApplicationInfo.type
    get() = VkStructureType of VkApplicationInfo.nsType(adr)
    set(value) = VkApplicationInfo.nsType(adr, value.i)
inline var VkApplicationInfo.next
    get() = VkApplicationInfo.npNext(adr)
    set(value) = VkApplicationInfo.npNext(adr, value)
inline var VkApplicationInfo.applicationName
    get() = VkApplicationInfo.npApplicationNameString(adr)
    set(value) = VkApplicationInfo.npApplicationName(adr, value?.utf8)
inline var VkApplicationInfo.applicationVersion
    get() = VkApplicationInfo.napplicationVersion(adr)
    set(value) = VkApplicationInfo.napiVersion(adr, value)
inline var VkApplicationInfo.engineName
    get() = VkApplicationInfo.npEngineNameString(adr)
    set(value) = VkApplicationInfo.npEngineName(adr, value?.utf8)
inline var VkApplicationInfo.apiVersion
    get() = VkApplicationInfo.napiVersion(adr)
    set(value) = VkApplicationInfo.napiVersion(adr, value)


inline var VkInstanceCreateInfo.type
    get() = VkStructureType of VkInstanceCreateInfo.nsType(adr)
    set(value) = VkInstanceCreateInfo.nsType(adr, value.i)
inline var VkInstanceCreateInfo.next
    get() = VkInstanceCreateInfo.npNext(adr)
    set(value) = VkInstanceCreateInfo.npNext(adr, value)
inline var VkInstanceCreateInfo.flags: VkInstanceCreateFlags
    get() = VkInstanceCreateInfo.nflags(adr)
    set(value) = VkInstanceCreateInfo.nflags(adr, value)
inline var VkInstanceCreateInfo.applicationInfo
    get() = VkInstanceCreateInfo.npApplicationInfo(adr)
    set(value) = VkInstanceCreateInfo.npApplicationInfo(adr, value)
inline var VkInstanceCreateInfo.enabledLayerNames
    get() = VkInstanceCreateInfo.nppEnabledLayerNames(adr).toArrayList()
    set(value) = VkInstanceCreateInfo.nppEnabledLayerNames(adr, value.toPointerBuffer())
inline var VkInstanceCreateInfo.enabledExtensionNames
    get() = VkInstanceCreateInfo.nppEnabledExtensionNames(adr).toArrayList()
    set(value) = VkInstanceCreateInfo.nppEnabledExtensionNames(adr, value.toPointerBuffer())

//typedef struct VkAllocationCallbacks {
//    void*                                   pUserData;
//    PFN_vkAllocationFunction                pfnAllocation;
//    PFN_vkReallocationFunction              pfnReallocation;
//    PFN_vkFreeFunction                      pfnFree;
//    PFN_vkInternalAllocationNotification    pfnInternalAllocation;
//    PFN_vkInternalFreeNotification          pfnInternalFree;
//} VkAllocationCallbacks;
//
//typedef struct VkPhysicalDeviceFeatures {
//    VkBool32    robustBufferAccess;
//    VkBool32    fullDrawIndexUint32;
//    VkBool32    imageCubeArray;
//    VkBool32    independentBlend;
//    VkBool32    geometryShader;
//    VkBool32    tessellationShader;
//    VkBool32    sampleRateShading;
//    VkBool32    dualSrcBlend;
//    VkBool32    logicOp;
//    VkBool32    multiDrawIndirect;
//    VkBool32    drawIndirectFirstInstance;
//    VkBool32    depthClamp;
//    VkBool32    depthBiasClamp;
//    VkBool32    fillModeNonSolid;
//    VkBool32    depthBounds;
//    VkBool32    wideLines;
//    VkBool32    largePoints;
//    VkBool32    alphaToOne;
//    VkBool32    multiViewport;
//    VkBool32    samplerAnisotropy;
//    VkBool32    textureCompressionETC2;
//    VkBool32    textureCompressionASTC_LDR;
//    VkBool32    textureCompressionBC;
//    VkBool32    occlusionQueryPrecise;
//    VkBool32    pipelineStatisticsQuery;
//    VkBool32    vertexPipelineStoresAndAtomics;
//    VkBool32    fragmentStoresAndAtomics;
//    VkBool32    shaderTessellationAndGeometryPointSize;
//    VkBool32    shaderImageGatherExtended;
//    VkBool32    shaderStorageImageExtendedFormats;
//    VkBool32    shaderStorageImageMultisample;
//    VkBool32    shaderStorageImageReadWithoutFormat;
//    VkBool32    shaderStorageImageWriteWithoutFormat;
//    VkBool32    shaderUniformBufferArrayDynamicIndexing;
//    VkBool32    shaderSampledImageArrayDynamicIndexing;
//    VkBool32    shaderStorageBufferArrayDynamicIndexing;
//    VkBool32    shaderStorageImageArrayDynamicIndexing;
//    VkBool32    shaderClipDistance;
//    VkBool32    shaderCullDistance;
//    VkBool32    shaderFloat64;
//    VkBool32    shaderInt64;
//    VkBool32    shaderInt16;
//    VkBool32    shaderResourceResidency;
//    VkBool32    shaderResourceMinLod;
//    VkBool32    sparseBinding;
//    VkBool32    sparseResidencyBuffer;
//    VkBool32    sparseResidencyImage2D;
//    VkBool32    sparseResidencyImage3D;
//    VkBool32    sparseResidency2Samples;
//    VkBool32    sparseResidency4Samples;
//    VkBool32    sparseResidency8Samples;
//    VkBool32    sparseResidency16Samples;
//    VkBool32    sparseResidencyAliased;
//    VkBool32    variableMultisampleRate;
//    VkBool32    inheritedQueries;
//} VkPhysicalDeviceFeatures;

inline val VkFormatProperties.linearTilingFeatures: VkFormatFeatureFlags get() = VkFormatProperties.nlinearTilingFeatures(adr)
inline val VkFormatProperties.optimalTilingFeatures: VkFormatFeatureFlags get() = VkFormatProperties.noptimalTilingFeatures(adr)
inline val VkFormatProperties.bufferFeatures: VkFormatFeatureFlags get() = VkFormatProperties.nbufferFeatures(adr)

//typedef struct VkExtent3D {
//    uint32_t    width;
//    uint32_t    height;
//    uint32_t    depth;
//} VkExtent3D;
//
//typedef struct VkImageFormatProperties {
//    VkExtent3D            maxExtent;
//    uint32_t              maxMipLevels;
//    uint32_t              maxArrayLayers;
//    VkSampleCountFlags    sampleCounts;
//    VkDeviceSize          maxResourceSize;
//} VkImageFormatProperties;
//
//typedef struct VkPhysicalDeviceLimits {
//    uint32_t              maxImageDimension1D;
//    uint32_t              maxImageDimension2D;
//    uint32_t              maxImageDimension3D;
//    uint32_t              maxImageDimensionCube;
//    uint32_t              maxImageArrayLayers;
//    uint32_t              maxTexelBufferElements;
//    uint32_t              maxUniformBufferRange;
//    uint32_t              maxStorageBufferRange;
//    uint32_t              maxPushConstantsSize;
//    uint32_t              maxMemoryAllocationCount;
//    uint32_t              maxSamplerAllocationCount;
//    VkDeviceSize          bufferImageGranularity;
//    VkDeviceSize          sparseAddressSpaceSize;
//    uint32_t              maxBoundDescriptorSets;
//    uint32_t              maxPerStageDescriptorSamplers;
//    uint32_t              maxPerStageDescriptorUniformBuffers;
//    uint32_t              maxPerStageDescriptorStorageBuffers;
//    uint32_t              maxPerStageDescriptorSampledImages;
//    uint32_t              maxPerStageDescriptorStorageImages;
//    uint32_t              maxPerStageDescriptorInputAttachments;
//    uint32_t              maxPerStageResources;
//    uint32_t              maxDescriptorSetSamplers;
//    uint32_t              maxDescriptorSetUniformBuffers;
//    uint32_t              maxDescriptorSetUniformBuffersDynamic;
//    uint32_t              maxDescriptorSetStorageBuffers;
//    uint32_t              maxDescriptorSetStorageBuffersDynamic;
//    uint32_t              maxDescriptorSetSampledImages;
//    uint32_t              maxDescriptorSetStorageImages;
//    uint32_t              maxDescriptorSetInputAttachments;
//    uint32_t              maxVertexInputAttributes;
//    uint32_t              maxVertexInputBindings;
//    uint32_t              maxVertexInputAttributeOffset;
//    uint32_t              maxVertexInputBindingStride;
//    uint32_t              maxVertexOutputComponents;
//    uint32_t              maxTessellationGenerationLevel;
//    uint32_t              maxTessellationPatchSize;
//    uint32_t              maxTessellationControlPerVertexInputComponents;
//    uint32_t              maxTessellationControlPerVertexOutputComponents;
//    uint32_t              maxTessellationControlPerPatchOutputComponents;
//    uint32_t              maxTessellationControlTotalOutputComponents;
//    uint32_t              maxTessellationEvaluationInputComponents;
//    uint32_t              maxTessellationEvaluationOutputComponents;
//    uint32_t              maxGeometryShaderInvocations;
//    uint32_t              maxGeometryInputComponents;
//    uint32_t              maxGeometryOutputComponents;
//    uint32_t              maxGeometryOutputVertices;
//    uint32_t              maxGeometryTotalOutputComponents;
//    uint32_t              maxFragmentInputComponents;
//    uint32_t              maxFragmentOutputAttachments;
//    uint32_t              maxFragmentDualSrcAttachments;
//    uint32_t              maxFragmentCombinedOutputResources;
//    uint32_t              maxComputeSharedMemorySize;
//    uint32_t              maxComputeWorkGroupCount[3];
//    uint32_t              maxComputeWorkGroupInvocations;
//    uint32_t              maxComputeWorkGroupSize[3];
//    uint32_t              subPixelPrecisionBits;
//    uint32_t              subTexelPrecisionBits;
//    uint32_t              mipmapPrecisionBits;
//    uint32_t              maxDrawIndexedIndexValue;
//    uint32_t              maxDrawIndirectCount;
//    float                 maxSamplerLodBias;
//    float                 maxSamplerAnisotropy;
//    uint32_t              maxViewports;
//    uint32_t              maxViewportDimensions[2];
//    float                 viewportBoundsRange[2];
//    uint32_t              viewportSubPixelBits;
//    size_t                minMemoryMapAlignment;
//    VkDeviceSize          minTexelBufferOffsetAlignment;
//    VkDeviceSize          minUniformBufferOffsetAlignment;
//    VkDeviceSize          minStorageBufferOffsetAlignment;
//    int32_t               minTexelOffset;
//    uint32_t              maxTexelOffset;
//    int32_t               minTexelGatherOffset;
//    uint32_t              maxTexelGatherOffset;
//    float                 minInterpolationOffset;
//    float                 maxInterpolationOffset;
//    uint32_t              subPixelInterpolationOffsetBits;
//    uint32_t              maxFramebufferWidth;
//    uint32_t              maxFramebufferHeight;
//    uint32_t              maxFramebufferLayers;
//    VkSampleCountFlags    framebufferColorSampleCounts;
//    VkSampleCountFlags    framebufferDepthSampleCounts;
//    VkSampleCountFlags    framebufferStencilSampleCounts;
//    VkSampleCountFlags    framebufferNoAttachmentsSampleCounts;
//    uint32_t              maxColorAttachments;
//    VkSampleCountFlags    sampledImageColorSampleCounts;
//    VkSampleCountFlags    sampledImageIntegerSampleCounts;
//    VkSampleCountFlags    sampledImageDepthSampleCounts;
//    VkSampleCountFlags    sampledImageStencilSampleCounts;
//    VkSampleCountFlags    storageImageSampleCounts;
//    uint32_t              maxSampleMaskWords;
//    VkBool32              timestampComputeAndGraphics;
//    float                 timestampPeriod;
//    uint32_t              maxClipDistances;
//    uint32_t              maxCullDistances;
//    uint32_t              maxCombinedClipAndCullDistances;
//    uint32_t              discreteQueuePriorities;
//    float                 pointSizeRange[2];
//    float                 lineWidthRange[2];
//    float                 pointSizeGranularity;
//    float                 lineWidthGranularity;
//    VkBool32              strictLines;
//    VkBool32              standardSampleLocations;
//    VkDeviceSize          optimalBufferCopyOffsetAlignment;
//    VkDeviceSize          optimalBufferCopyRowPitchAlignment;
//    VkDeviceSize          nonCoherentAtomSize;
//} VkPhysicalDeviceLimits;
//
//typedef struct VkPhysicalDeviceSparseProperties {
//    VkBool32    residencyStandard2DBlockShape;
//    VkBool32    residencyStandard2DMultisampleBlockShape;
//    VkBool32    residencyStandard3DBlockShape;
//    VkBool32    residencyAlignedMipSize;
//    VkBool32    residencyNonResidentStrict;
//} VkPhysicalDeviceSparseProperties;
//
//typedef struct VkPhysicalDeviceProperties {
//    uint32_t                            apiVersion;
//    uint32_t                            driverVersion;
//    uint32_t                            vendorID;
//    uint32_t                            deviceID;
//    VkPhysicalDeviceType                deviceType;
//    char                                deviceName[VK_MAX_PHYSICAL_DEVICE_NAME_SIZE];
//    uint8_t                             pipelineCacheUUID[VK_UUID_SIZE];
//    VkPhysicalDeviceLimits              limits;
//    VkPhysicalDeviceSparseProperties    sparseProperties;
//} VkPhysicalDeviceProperties;
//
//typedef struct VkQueueFamilyProperties {
//    VkQueueFlags    queueFlags;
//    uint32_t        queueCount;
//    uint32_t        timestampValidBits;
//    VkExtent3D      minImageTransferGranularity;
//} VkQueueFamilyProperties;
//
//typedef struct VkMemoryType {
//    VkMemoryPropertyFlags    propertyFlags;
//    uint32_t                 heapIndex;
//} VkMemoryType;
//
//typedef struct VkMemoryHeap {
//    VkDeviceSize         size;
//    VkMemoryHeapFlags    flags;
//} VkMemoryHeap;
//
//typedef struct VkPhysicalDeviceMemoryProperties {
//    uint32_t        memoryTypeCount;
//    VkMemoryType    memoryTypes[VK_MAX_MEMORY_TYPES];
//    uint32_t        memoryHeapCount;
//    VkMemoryHeap    memoryHeaps[VK_MAX_MEMORY_HEAPS];
//} VkPhysicalDeviceMemoryProperties;
//
//typedef void (VKAPI_PTR *PFN_vkVoidFunction)(void);

inline var VkDeviceQueueCreateInfo.type: VkStructureType
    get() = VkStructureType of VkDeviceQueueCreateInfo.nsType(adr)
    set(value) = VkDeviceQueueCreateInfo.nsType(adr, value.i)
inline var VkDeviceQueueCreateInfo.next
    get() = VkDeviceQueueCreateInfo.npNext(adr)
    set(value) = VkDeviceQueueCreateInfo.npNext(adr, value)
inline var VkDeviceQueueCreateInfo.flags: VkDeviceQueueCreateFlags
    get() = VkDeviceQueueCreateInfo.nflags(adr)
    set(value) = VkDeviceQueueCreateInfo.nflags(adr, value)
inline var VkDeviceQueueCreateInfo.queueFamilyIndex
    get() = VkDeviceQueueCreateInfo.nqueueFamilyIndex(adr)
    set(value) = VkDeviceQueueCreateInfo.nqueueFamilyIndex(adr, value)
//inline val VkDeviceQueueCreateInfo.queueCount get() = VkDeviceQueueCreateInfo.nqueueCount(address)
inline var VkDeviceQueueCreateInfo.queuePriorities: FloatBuffer
    get() = VkDeviceQueueCreateInfo.npQueuePriorities(adr)
    set(value) = VkDeviceQueueCreateInfo.npQueuePriorities(adr, value)

//typedef struct VkDeviceQueueCreateInfo {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkDeviceQueueCreateFlags    flags;
//    uint32_t                    queueFamilyIndex;
//    uint32_t                    queueCount;
//    const float*                pQueuePriorities;
//} VkDeviceQueueCreateInfo;

inline var VkDeviceCreateInfo.type: VkStructureType
    get() = VkStructureType of VkDeviceCreateInfo.nsType(adr)
    set(value) = VkDeviceCreateInfo.nsType(adr, value.i)
inline var VkDeviceCreateInfo.next
    get() = VkDeviceCreateInfo.npNext(adr)
    set(value) = VkDeviceCreateInfo.npNext(adr, value)
inline var VkDeviceCreateInfo.flags: VkDeviceQueueCreateFlags
    get() = VkDeviceCreateInfo.nflags(adr)
    set(value) = VkDeviceCreateInfo.nflags(adr, value)
//inline val VkDeviceCreateInfo.queueCreateInfoCount get() = queueCreateInfoCount()
inline var VkDeviceCreateInfo.queueCreateInfos: ArrayList<VkDeviceQueueCreateInfo>
    get() {
        val count = VkDeviceCreateInfo.nqueueCreateInfoCount(adr)
        val infos = vk.DeviceQueueCreateInfo(count)
        val res = ArrayList<VkDeviceQueueCreateInfo>()
        for (i in 0 until count) res += infos[i]
        return res
    }
    set(value) {
        val infos = vk.DeviceQueueCreateInfo(value.size)
        for (i in value.indices) infos.put(i, value[i])
        VkDeviceCreateInfo.npQueueCreateInfos(adr, infos)
    }
inline var VkDeviceCreateInfo.enabledLayerNames: ArrayList<String>
    get() = VkDeviceCreateInfo.nppEnabledLayerNames(adr).toArrayList()
    set(value) = VkDeviceCreateInfo.nppEnabledLayerNames(adr, value.toPointerBuffer())
inline var VkDeviceCreateInfo.enabledExtensionNames: ArrayList<String>
    get() = VkDeviceCreateInfo.nppEnabledExtensionNames(adr).toArrayList()
    set(value) = VkDeviceCreateInfo.nppEnabledExtensionNames(adr, value.toPointerBuffer())
inline var VkDeviceCreateInfo.enabledFeatures: VkPhysicalDeviceFeatures?
    get() = VkDeviceCreateInfo.npEnabledFeatures(adr)
    set(value) = VkDeviceCreateInfo.npEnabledFeatures(adr, value)

//
//typedef struct VkExtensionProperties {
//    char        extensionName[VK_MAX_EXTENSION_NAME_SIZE];
//    uint32_t    specVersion;
//} VkExtensionProperties;
//
//typedef struct VkLayerProperties {
//    char        layerName[VK_MAX_EXTENSION_NAME_SIZE];
//    uint32_t    specVersion;
//    uint32_t    implementationVersion;
//    char        description[VK_MAX_DESCRIPTION_SIZE];
//} VkLayerProperties;

inline var VkSubmitInfo.type: VkStructureType
    get() = VkStructureType of VkSubmitInfo.nsType(adr)
    set(value) = VkSubmitInfo.nsType(adr, value.i)
inline var VkSubmitInfo.next
    get() = VkSubmitInfo.npNext(adr)
    set(value) = VkSubmitInfo.npNext(adr, value)
inline var VkSubmitInfo.waitSemaphoreCount
    get() = VkSubmitInfo.nwaitSemaphoreCount(adr)
    set(value) = VkSubmitInfo.nwaitSemaphoreCount(adr, value)
inline var VkSubmitInfo.waitSemaphores
    get() = VkSubmitInfo.npWaitSemaphores(adr)
    set(value) = VkSubmitInfo.npWaitSemaphores(adr, value)
inline var VkSubmitInfo.waitDstStageMask: IntBuffer?
    get() = VkSubmitInfo.npWaitDstStageMask(adr)
    set(value) = VkSubmitInfo.npWaitDstStageMask(adr, value)
//inline val VkSubmitInfo.commandBufferCount get() = VkSubmitInfo.ncommandBufferCount(address)
inline var VkSubmitInfo.commandBuffers
    get() = pCommandBuffers()
    set(value) {
        pCommandBuffers(value)
    }
//inline val VkSubmitInfo.signalSemaphoreCount get() = VkSubmitInfo.nsignalSemaphoreCount(address)
inline var VkSubmitInfo.signalSemaphores
    get() = VkSubmitInfo.npSignalSemaphores(adr)
    set(value) = VkSubmitInfo.npSignalSemaphores(adr, value)

//typedef struct VkSubmitInfo {
//    VkStructureType                sType;
//    const void*                    pNext;
//    uint32_t                       waitSemaphoreCount;
//    const VkSemaphore*             pWaitSemaphores;
//    const VkPipelineStageFlags*    pWaitDstStageMask;
//    uint32_t                       commandBufferCount;
//    const VkCommandBuffer*         pCommandBuffers;
//    uint32_t                       signalSemaphoreCount;
//    const VkSemaphore*             pSignalSemaphores;
//} VkSubmitInfo;
//
//typedef struct VkMemoryAllocateInfo {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkDeviceSize       allocationSize;
//    uint32_t           memoryTypeIndex;
//} VkMemoryAllocateInfo;
//
//typedef struct VkMappedMemoryRange {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkDeviceMemory     memory;
//    VkDeviceSize       offset;
//    VkDeviceSize       size;
//} VkMappedMemoryRange;
//
//typedef struct VkMemoryRequirements {
//    VkDeviceSize    size;
//    VkDeviceSize    alignment;
//    uint32_t        memoryTypeBits;
//} VkMemoryRequirements;
//
//typedef struct VkSparseImageFormatProperties {
//    VkImageAspectFlags          aspectMask;
//    VkExtent3D                  imageGranularity;
//    VkSparseImageFormatFlags    flags;
//} VkSparseImageFormatProperties;
//
//typedef struct VkSparseImageMemoryRequirements {
//    VkSparseImageFormatProperties    formatProperties;
//    uint32_t                         imageMipTailFirstLod;
//    VkDeviceSize                     imageMipTailSize;
//    VkDeviceSize                     imageMipTailOffset;
//    VkDeviceSize                     imageMipTailStride;
//} VkSparseImageMemoryRequirements;
//
//typedef struct VkSparseMemoryBind {
//    VkDeviceSize               resourceOffset;
//    VkDeviceSize               size;
//    VkDeviceMemory             memory;
//    VkDeviceSize               memoryOffset;
//    VkSparseMemoryBindFlags    flags;
//} VkSparseMemoryBind;
//
//typedef struct VkSparseBufferMemoryBindInfo {
//    VkBuffer                     buffer;
//    uint32_t                     bindCount;
//    const VkSparseMemoryBind*    pBinds;
//} VkSparseBufferMemoryBindInfo;
//
//typedef struct VkSparseImageOpaqueMemoryBindInfo {
//    VkImage                      image;
//    uint32_t                     bindCount;
//    const VkSparseMemoryBind*    pBinds;
//} VkSparseImageOpaqueMemoryBindInfo;
//
//typedef struct VkImageSubresource {
//    VkImageAspectFlags    aspectMask;
//    uint32_t              mipLevel;
//    uint32_t              arrayLayer;
//} VkImageSubresource;
//
//typedef struct VkOffset3D {
//    int32_t    x;
//    int32_t    y;
//    int32_t    z;
//} VkOffset3D;
//
//typedef struct VkSparseImageMemoryBind {
//    VkImageSubresource         subresource;
//    VkOffset3D                 offset;
//    VkExtent3D                 extent;
//    VkDeviceMemory             memory;
//    VkDeviceSize               memoryOffset;
//    VkSparseMemoryBindFlags    flags;
//} VkSparseImageMemoryBind;
//
//typedef struct VkSparseImageMemoryBindInfo {
//    VkImage                           image;
//    uint32_t                          bindCount;
//    const VkSparseImageMemoryBind*    pBinds;
//} VkSparseImageMemoryBindInfo;
//
//typedef struct VkBindSparseInfo {
//    VkStructureType                             sType;
//    const void*                                 pNext;
//    uint32_t                                    waitSemaphoreCount;
//    const VkSemaphore*                          pWaitSemaphores;
//    uint32_t                                    bufferBindCount;
//    const VkSparseBufferMemoryBindInfo*         pBufferBinds;
//    uint32_t                                    imageOpaqueBindCount;
//    const VkSparseImageOpaqueMemoryBindInfo*    pImageOpaqueBinds;
//    uint32_t                                    imageBindCount;
//    const VkSparseImageMemoryBindInfo*          pImageBinds;
//    uint32_t                                    signalSemaphoreCount;
//    const VkSemaphore*                          pSignalSemaphores;
//} VkBindSparseInfo;

inline var VkFenceCreateInfo.type: VkStructureType
    get() = VkStructureType of VkFenceCreateInfo.nsType(adr)
    set(value) = VkFenceCreateInfo.nsType(adr, value.i)
inline var VkFenceCreateInfo.next
    get() = VkFenceCreateInfo.npNext(adr)
    set(value) = VkFenceCreateInfo.npNext(adr, value)
inline var VkFenceCreateInfo.flags: VkSemaphoreCreateFlags
    get() = VkFenceCreateInfo.nflags(adr)
    set(value) = VkFenceCreateInfo.nflags(adr, value)


inline var VkSemaphoreCreateInfo.type: VkStructureType
    get() = VkStructureType of VkSemaphoreCreateInfo.nsType(adr)
    set(value) = VkSemaphoreCreateInfo.nsType(adr, value.i)
inline var VkSemaphoreCreateInfo.next
    get() = VkSemaphoreCreateInfo.npNext(adr)
    set(value) = VkSemaphoreCreateInfo.npNext(adr, value)
inline var VkSemaphoreCreateInfo.flags: VkSemaphoreCreateFlags
    get() = VkSemaphoreCreateInfo.nflags(adr)
    set(value) = VkSemaphoreCreateInfo.nflags(adr, value)


//typedef struct VkSemaphoreCreateInfo {
//    VkStructureType           sType;
//    const void*               pNext;
//    VkSemaphoreCreateFlags    flags;
//} VkSemaphoreCreateInfo;
//
//typedef struct VkEventCreateInfo {
//    VkStructureType       sType;
//    const void*           pNext;
//    VkEventCreateFlags    flags;
//} VkEventCreateInfo;
//
//typedef struct VkQueryPoolCreateInfo {
//    VkStructureType                  sType;
//    const void*                      pNext;
//    VkQueryPoolCreateFlags           flags;
//    VkQueryType                      queryType;
//    uint32_t                         queryCount;
//    VkQueryPipelineStatisticFlags    pipelineStatistics;
//} VkQueryPoolCreateInfo;

inline var VkBufferCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkBufferCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkBufferCreateInfo.flags: VkBufferCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkBufferCreateInfo.size: VkDeviceSize
    get() = size()
    set(value) {
        size(value)
    }
inline var VkBufferCreateInfo.usage: VkBufferUsageFlags
    get() = usage()
    set(value) {
        usage(value)
    }
inline var VkBufferCreateInfo.sharingMode: VkSharingMode
    get() = sharingMode()
    set(value) {
        sharingMode(value)
    }
inline val VkBufferCreateInfo.queueFamilyIndexCount get() = queueFamilyIndexCount()
inline var VkBufferCreateInfo.queueFamilyIndices
    get() = pQueueFamilyIndices()
    set(value) {
        pQueueFamilyIndices(value)
    }


//typedef struct VkBufferViewCreateInfo {
//    VkStructureType            sType;
//    const void*                pNext;
//    VkBufferViewCreateFlags    flags;
//    VkBuffer                   buffer;
//    VkFormat                   format;
//    VkDeviceSize               offset;
//    VkDeviceSize               range;
//} VkBufferViewCreateInfo;
//
//typedef struct VkImageCreateInfo {
//    VkStructureType          sType;
//    const void*              pNext;
//    VkImageCreateFlags       flags;
//    VkImageType              imageType;
//    VkFormat                 format;
//    VkExtent3D               extent;
//    uint32_t                 mipLevels;
//    uint32_t                 arrayLayers;
//    VkSampleCountFlagBits    samples;
//    VkImageTiling            tiling;
//    VkImageUsageFlags        usage;
//    VkSharingMode            sharingMode;
//    uint32_t                 queueFamilyIndexCount;
//    const uint32_t*          pQueueFamilyIndices;
//    VkImageLayout            initialLayout;
//} VkImageCreateInfo;
//
//typedef struct VkSubresourceLayout {
//    VkDeviceSize    offset;
//    VkDeviceSize    size;
//    VkDeviceSize    rowPitch;
//    VkDeviceSize    arrayPitch;
//    VkDeviceSize    depthPitch;
//} VkSubresourceLayout;
//
//typedef struct VkComponentMapping {
//    VkComponentSwizzle    r;
//    VkComponentSwizzle    g;
//    VkComponentSwizzle    b;
//    VkComponentSwizzle    a;
//} VkComponentMapping;
//
//typedef struct VkImageSubresourceRange {
//    VkImageAspectFlags    aspectMask;
//    uint32_t              baseMipLevel;
//    uint32_t              levelCount;
//    uint32_t              baseArrayLayer;
//    uint32_t              layerCount;
//} VkImageSubresourceRange;
//
//typedef struct VkImageViewCreateInfo {
//    VkStructureType            sType;
//    const void*                pNext;
//    VkImageViewCreateFlags     flags;
//    VkImage                    image;
//    VkImageViewType            viewType;
//    VkFormat                   format;
//    VkComponentMapping         components;
//    VkImageSubresourceRange    subresourceRange;
//} VkImageViewCreateInfo;

inline var VkShaderModuleCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkShaderModuleCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkShaderModuleCreateInfo.flags: VkShaderModuleCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline val VkShaderModuleCreateInfo.codeSize get() = codeSize()
inline var VkShaderModuleCreateInfo.code
    get() = pCode()
    set(value) {
        pCode(value)
    }


//typedef struct VkPipelineCacheCreateInfo {
//    VkStructureType               sType;
//    const void*                   pNext;
//    VkPipelineCacheCreateFlags    flags;
//    size_t                        initialDataSize;
//    const void*                   pInitialData;
//} VkPipelineCacheCreateInfo;
//
//typedef struct VkSpecializationMapEntry {
//    uint32_t    constantID;
//    uint32_t    offset;
//    size_t      size;
//} VkSpecializationMapEntry;
//
//typedef struct VkSpecializationInfo {
//    uint32_t                           mapEntryCount;
//    const VkSpecializationMapEntry*    pMapEntries;
//    size_t                             dataSize;
//    const void*                        pData;
//} VkSpecializationInfo;


inline var VkPipelineShaderStageCreateInfo.type
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineShaderStageCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineShaderStageCreateInfo.flags: VkPipelineShaderStageCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkPipelineShaderStageCreateInfo.stage: VkShaderStageFlagBits
    get() = stage()
    set(value) {
        stage(value)
    }
inline var VkPipelineShaderStageCreateInfo.module: VkShaderModule
    get() = module()
    set(value) {
        module(value)
    }
//inline var VkPipelineShaderStageCreateInfo.name// TODO
//    get() = pName()
//    set(value) {
//        pName(value)
//    }
inline var VkPipelineShaderStageCreateInfo.specializationInfo
    get() = pSpecializationInfo()
    set(value) {
        pSpecializationInfo(value)
    }


inline var VkVertexInputBindingDescription.binding
    get() = binding()
    set(value) {
        binding(value)
    }
inline var VkVertexInputBindingDescription.stride
    get() = stride()
    set(value) {
        stride(value)
    }
inline var VkVertexInputBindingDescription.inputRate: VkVertexInputRate
    get() = inputRate()
    set(value) {
        inputRate(value)
    }


inline var VkVertexInputAttributeDescription.location
    get() = location()
    set(value) {
        location(value)
    }
inline var VkVertexInputAttributeDescription.binding
    get() = binding()
    set(value) {
        binding(value)
    }
inline var VkVertexInputAttributeDescription.format: VkFormat
    get() = VkFormat of format()
    set(value) {
        format(value.i)
    }
inline var VkVertexInputAttributeDescription.offset
    get() = offset()
    set(value) {
        offset(value)
    }


inline var VkPipelineVertexInputStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineVertexInputStateCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineVertexInputStateCreateInfo.flags: VkPipelineVertexInputStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline val VkPipelineVertexInputStateCreateInfo.vertexBindingDescriptionCount get() = vertexBindingDescriptionCount()
inline var VkPipelineVertexInputStateCreateInfo.vertexBindingDescriptions
    get() = pVertexBindingDescriptions()
    set(value) {
        pVertexBindingDescriptions(value)
    }
inline val VkPipelineVertexInputStateCreateInfo.vertexAttributeDescriptionCount get() = vertexAttributeDescriptionCount()
inline var VkPipelineVertexInputStateCreateInfo.vertexAttributeDescriptions
    get() = pVertexAttributeDescriptions()
    set(value) {
        pVertexAttributeDescriptions(value)
    }


//typedef struct VkPipelineVertexInputStateCreateInfo {
//    VkStructureType                             sType;
//    const void*                                 pNext;
//    VkPipelineVertexInputStateCreateFlags       flags;
//    uint32_t                                    vertexBindingDescriptionCount;
//    const VkVertexInputBindingDescription*      pVertexBindingDescriptions;
//    uint32_t                                    vertexAttributeDescriptionCount;
//    const VkVertexInputAttributeDescription*    pVertexAttributeDescriptions;
//} VkPipelineVertexInputStateCreateInfo;

inline var VkPipelineInputAssemblyStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineInputAssemblyStateCreateInfo.next: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineInputAssemblyStateCreateInfo.flags: VkPipelineInputAssemblyStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkPipelineInputAssemblyStateCreateInfo.topology: VkPipelineInputAssemblyStateCreateFlags
    get() = topology()
    set(value) {
        topology(value)
    }

//typedef struct VkPipelineInputAssemblyStateCreateInfo {
//    VkStructureType                            sType;
//    const void*                                pNext;
//    VkPipelineInputAssemblyStateCreateFlags    flags;
//    VkPrimitiveTopology                        topology;
//    VkBool32                                   primitiveRestartEnable;
//} VkPipelineInputAssemblyStateCreateInfo;
//
//typedef struct VkPipelineTessellationStateCreateInfo {
//    VkStructureType                           sType;
//    const void*                               pNext;
//    VkPipelineTessellationStateCreateFlags    flags;
//    uint32_t                                  patchControlPoints;
//} VkPipelineTessellationStateCreateInfo;


inline var VkViewport.x
    get() = x()
    set(value) {
        x(value)
    }
inline var VkViewport.y
    get() = y()
    set(value) {
        y(value)
    }
inline var VkViewport.width
    get() = width()
    set(value) {
        width(value)
    }
inline var VkViewport.height
    get() = height()
    set(value) {
        height(value)
    }
inline var VkViewport.minDepth
    get() = minDepth()
    set(value) {
        minDepth(value)
    }
inline var VkViewport.maxDepth
    get() = maxDepth()
    set(value) {
        maxDepth(value)
    }

//inline var VkViewport.pos
//    get() = Vec2(x, y)
//    set(value) {
//        x = value.x
//        y = value.y
//    }
inline fun VkViewport.size(size: Vec2t<out Number>) {
    width = size.x.f
    height = size.y.f
}

//inline var VkViewport.size
//    get() = Vec2(width, height)
//    set(value) {
//        width = value.x
//        height = value.y
//    }
inline fun VkViewport.depth(min: Float, max: Float) {
    minDepth = min
    maxDepth = max
}
//inline var VkViewport.depth
//    get() = Vec2(minDepth, maxDepth)
//    set(value) {
//        minDepth = value.x
//        maxDepth = value.y
//    }


inline var VkOffset2D.x
    get() = x()
    set(value) {
        x(value)
    }
inline var VkOffset2D.y
    get() = y()
    set(value) {
        y(value)
    }
inline var VkExtent2D.width
    get() = VkExtent2D.nwidth(adr)
    set(value) = VkExtent2D.nwidth(adr, value)
inline var VkExtent2D.height
    get() = VkExtent2D.nheight(adr)
    set(value) = VkExtent2D.nheight(adr, value)

inline fun VkExtent2D.size(v: Vec2i) = size(v.x, v.y)
inline fun VkExtent2D.size(x: Int, y: Int) {
    width = x
    height = y
}

//var VkExtent2D.size BUG
//    get() = Vec2i(width, height)
//    set(value) {
//        width = value.x
//        height = value.y
//    }
inline fun VkOffset2D.pos(x: Int, y: Int) {
    this.x = x
    this.y = y
}

//inline var VkOffset2D.pos BUG
//    get() = Vec2i(x, y)
//    set(value) {
//        x = value.x
//        y = value.y
//    }
inline var VkExtent3D.width
    get() = width()
    set(value) {
        width(value)
    }
inline var VkExtent3D.height
    get() = height()
    set(value) {
        height(value)
    }
inline var VkExtent3D.depth
    get() = depth()
    set(value) {
        depth(value)
    }

inline fun VkExtent3D.size(x: Int, y: Int, z: Int) {
    width = x
    height = y
    depth = z
}
//inline var VkExtent3D.size
//    get() = Vec3i(width, height, depth)
//    set(value) {
//        width = value.x
//        height = value.y
//        depth = value.z
//    } TODO BUG


inline var VkRect2D.offset: VkOffset2D
    get() = offset()
    set(value) {
        offset(value)
    }
inline var VkRect2D.extent: VkExtent2D
    get() = extent()
    set(value) {
        extent(value)
    }

inline var VkPipelineViewportStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineViewportStateCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineViewportStateCreateInfo.flags: VkPipelineViewportStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkPipelineViewportStateCreateInfo.viewportCount
    get() = viewportCount()
    set(value) {
        viewportCount(value)
    }
inline var VkPipelineViewportStateCreateInfo.viewports
    get() = pViewports()
    set(value) {
        pViewports(value)
    }
inline var VkPipelineViewportStateCreateInfo.scissorCount
    get() = scissorCount()
    set(value) {
        scissorCount(value)
    }
inline var VkPipelineViewportStateCreateInfo.scissors
    get() = pScissors()
    set(value) {
        pScissors(value)
    }

inline var VkPipelineRasterizationStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineRasterizationStateCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.flags: VkPipelineRasterizationStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.depthClampEnable
    get() = depthClampEnable()
    set(value) {
        depthClampEnable(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.rasterizerDiscardEnable
    get() = rasterizerDiscardEnable()
    set(value) {
        rasterizerDiscardEnable(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.polygonMode: VkPolygonMode
    get() = polygonMode()
    set(value) {
        polygonMode(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.cullMode: VkCullModeFlags
    get() = cullMode()
    set(value) {
        cullMode(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.frontFace: VkFrontFace
    get() = frontFace()
    set(value) {
        frontFace(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.depthBiasEnable
    get() = depthBiasEnable()
    set(value) {
        depthBiasEnable(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.depthBiasConstantFactor
    get() = depthBiasConstantFactor()
    set(value) {
        depthBiasConstantFactor(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.depthBiasClamp
    get() = depthBiasClamp()
    set(value) {
        depthBiasClamp(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.depthBiasSlopeFactor
    get() = depthBiasSlopeFactor()
    set(value) {
        depthBiasSlopeFactor(value)
    }
inline var VkPipelineRasterizationStateCreateInfo.lineWidth
    get() = lineWidth()
    set(value) {
        lineWidth(value)
    }


inline var VkPipelineMultisampleStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineMultisampleStateCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineMultisampleStateCreateInfo.flags: VkPipelineMultisampleStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkPipelineMultisampleStateCreateInfo.rasterizationSamples: VkSampleCountFlagBits
    get() = rasterizationSamples()
    set(value) {
        rasterizationSamples(value)
    }
inline var VkPipelineMultisampleStateCreateInfo.sampleShadingEnable
    get() = sampleShadingEnable()
    set(value) {
        sampleShadingEnable(value)
    }
inline var VkPipelineMultisampleStateCreateInfo.minSampleShading
    get() = minSampleShading()
    set(value) {
        minSampleShading(value)
    }
inline var VkPipelineMultisampleStateCreateInfo.sampleMask: IntBuffer?
    get() = null
    set(value) {
        pSampleMask(value)
    }
inline var VkPipelineMultisampleStateCreateInfo.alphaToCoverageEnable
    get() = alphaToCoverageEnable()
    set(value) {
        alphaToCoverageEnable(value)
    }
inline var VkPipelineMultisampleStateCreateInfo.alphaToOneEnable
    get() = alphaToOneEnable()
    set(value) {
        alphaToOneEnable(value)
    }


inline var VkStencilOpState.failOp: VkStencilOp
    get() = failOp()
    set(value) {
        failOp(value)
    }
inline var VkStencilOpState.passOp: VkStencilOp
    get() = passOp()
    set(value) {
        passOp(value)
    }
inline var VkStencilOpState.depthFailOp: VkStencilOp
    get() = depthFailOp()
    set(value) {
        depthFailOp(value)
    }
inline var VkStencilOpState.compareOp: VkCompareOp
    get() = compareOp()
    set(value) {
        compareOp(value)
    }
inline var VkStencilOpState.compareMask
    get() = compareMask()
    set(value) {
        compareMask(value)
    }
inline var VkStencilOpState.writeMask
    get() = writeMask()
    set(value) {
        writeMask(value)
    }
inline var VkStencilOpState.reference
    get() = reference()
    set(value) {
        reference(value)
    }


inline var VkPipelineDepthStencilStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineDepthStencilStateCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.flags: VkPipelineDepthStencilStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.depthTestEnable
    get() = depthTestEnable()
    set(value) {
        depthTestEnable(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.depthWriteEnable
    get() = depthWriteEnable()
    set(value) {
        depthWriteEnable(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.depthCompareOp: VkCompareOp
    get() = depthCompareOp()
    set(value) {
        depthCompareOp(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.depthBoundsTestEnable
    get() = depthBoundsTestEnable()
    set(value) {
        depthBoundsTestEnable(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.stencilTestEnable
    get() = stencilTestEnable()
    set(value) {
        stencilTestEnable(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.front: VkStencilOpState
    get() = front()
    set(value) {
        front(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.back: VkStencilOpState
    get() = back()
    set(value) {
        back(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.minDepthBounds
    get() = minDepthBounds()
    set(value) {
        minDepthBounds(value)
    }
inline var VkPipelineDepthStencilStateCreateInfo.maxDepthBounds
    get() = maxDepthBounds()
    set(value) {
        maxDepthBounds(value)
    }


inline var VkPipelineColorBlendAttachmentState.blendEnable
    get() = blendEnable()
    set(value) {
        blendEnable(value)
    }
inline var VkPipelineColorBlendAttachmentState.srcColorBlendFactor: VkBlendFactor
    get() = srcColorBlendFactor()
    set(value) {
        srcColorBlendFactor(value)
    }
inline var VkPipelineColorBlendAttachmentState.dstColorBlendFactor: VkBlendFactor
    get() = dstColorBlendFactor()
    set(value) {
        dstColorBlendFactor(value)
    }
inline var VkPipelineColorBlendAttachmentState.colorBlendOp: VkBlendOp
    get() = colorBlendOp()
    set(value) {
        colorBlendOp(value)
    }
inline var VkPipelineColorBlendAttachmentState.srcAlphaBlendFactor: VkBlendFactor
    get() = srcAlphaBlendFactor()
    set(value) {
        srcAlphaBlendFactor(value)
    }
inline var VkPipelineColorBlendAttachmentState.dstAlphaBlendFactor: VkBlendFactor
    get() = dstAlphaBlendFactor()
    set(value) {
        dstAlphaBlendFactor(value)
    }
inline var VkPipelineColorBlendAttachmentState.alphaBlendOp: VkBlendOp
    get() = alphaBlendOp()
    set(value) {
        alphaBlendOp(value)
    }
inline var VkPipelineColorBlendAttachmentState.colorWriteMask: VkColorComponentFlags
    get() = colorWriteMask()
    set(value) {
        colorWriteMask(value)
    }


inline var VkPipelineColorBlendStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineColorBlendStateCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineColorBlendStateCreateInfo.flags: VkPipelineColorBlendStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkPipelineColorBlendStateCreateInfo.logicOpEnable
    get() = logicOpEnable()
    set(value) {
        logicOpEnable(value)
    }
inline var VkPipelineColorBlendStateCreateInfo.logicOp: VkLogicOp
    get() = logicOp()
    set(value) {
        logicOp(value)
    }
inline val VkPipelineColorBlendStateCreateInfo.attachmentCount get() = attachmentCount()
inline var VkPipelineColorBlendStateCreateInfo.attachments
    get() = pAttachments()
    set(value) {
        pAttachments(value)
    }
inline var VkPipelineColorBlendStateCreateInfo.blendConstants
    get() = blendConstants()
    set(value) {
        blendConstants(value)
    }


inline var VkPipelineDynamicStateCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineDynamicStateCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineDynamicStateCreateInfo.flags: VkPipelineDynamicStateCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline val VkPipelineDynamicStateCreateInfo.dynamicStateCount get() = dynamicStateCount()
inline var VkPipelineDynamicStateCreateInfo.dynamicStates
    get() = pDynamicStates()
    set(value) {
        pDynamicStates(value)
    }

//typedef struct VkPipelineDynamicStateCreateInfo {
//    VkStructureType                      sType;
//    const void*                          pNext;
//    VkPipelineDynamicStateCreateFlags    flags;
//    uint32_t                             dynamicStateCount;
//    const VkDynamicState*                pDynamicStates;
//} VkPipelineDynamicStateCreateInfo;
//

inline var VkGraphicsPipelineCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkGraphicsPipelineCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkGraphicsPipelineCreateInfo.flags: VkPipelineCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline val VkGraphicsPipelineCreateInfo.stageCount get() = stageCount()
inline var VkGraphicsPipelineCreateInfo.stages
    get() = pStages()
    set(value) {
        pStages(value)
    }
inline var VkGraphicsPipelineCreateInfo.vertexInputState
    get() = pVertexInputState()
    set(value) {
        pVertexInputState(value)
    }
inline var VkGraphicsPipelineCreateInfo.inputAssemblyState
    get() = pInputAssemblyState()
    set(value) {
        pInputAssemblyState(value)
    }
inline var VkGraphicsPipelineCreateInfo.tessellationState
    get() = pTessellationState()
    set(value) {
        pTessellationState(value)
    }
inline var VkGraphicsPipelineCreateInfo.viewportState
    get() = pViewportState()
    set(value) {
        pViewportState(value)
    }
inline var VkGraphicsPipelineCreateInfo.rasterizationState
    get() = pRasterizationState()
    set(value) {
        pRasterizationState(value)
    }
inline var VkGraphicsPipelineCreateInfo.multisampleState
    get() = pMultisampleState()
    set(value) {
        pMultisampleState(value)
    }
inline var VkGraphicsPipelineCreateInfo.depthStencilState
    get() = pDepthStencilState()
    set(value) {
        pDepthStencilState(value)
    }
inline var VkGraphicsPipelineCreateInfo.colorBlendState
    get() = pColorBlendState()
    set(value) {
        pColorBlendState(value)
    }
inline var VkGraphicsPipelineCreateInfo.dynamicState
    get() = pDynamicState()
    set(value) {
        pDynamicState(value)
    }
inline var VkGraphicsPipelineCreateInfo.layout: VkPipelineLayout
    get() = layout()
    set(value) {
        layout(value)
    }
inline var VkGraphicsPipelineCreateInfo.renderPass: VkRenderPass
    get() = renderPass()
    set(value) {
        renderPass(value)
    }
inline var VkGraphicsPipelineCreateInfo.subpass
    get() = subpass()
    set(value) {
        subpass(value)
    }
inline var VkGraphicsPipelineCreateInfo.basePipelineHandle: VkPipeline
    get() = basePipelineHandle()
    set(value) {
        basePipelineHandle(value)
    }
inline var VkGraphicsPipelineCreateInfo.basePipelineIndex
    get() = basePipelineIndex()
    set(value) {
        basePipelineIndex(value)
    }

//
//typedef struct VkComputePipelineCreateInfo {
//    VkStructureType                    sType;
//    const void*                        pNext;
//    VkPipelineCreateFlags              flags;
//    VkPipelineShaderStageCreateInfo    stage;
//    VkPipelineLayout                   layout;
//    VkPipeline                         basePipelineHandle;
//    int32_t                            basePipelineIndex;
//} VkComputePipelineCreateInfo;
//
//typedef struct VkPushConstantRange {
//    VkShaderStageFlags    stageFlags;
//    uint32_t              offset;
//    uint32_t              size;
//} VkPushConstantRange;
//

inline var VkPipelineLayoutCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkPipelineLayoutCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkPipelineLayoutCreateInfo.flags: VkPipelineLayoutCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline val VkPipelineLayoutCreateInfo.setLayoutCount get() = setLayoutCount()
inline var VkPipelineLayoutCreateInfo.setLayouts
    get() = pSetLayouts()
    set(value) {
        pSetLayouts(value)
    }
inline val VkPipelineLayoutCreateInfo.pushConstantRangeCount get() = pushConstantRangeCount()
inline var VkPipelineLayoutCreateInfo.pushConstantRanges
    get() = pPushConstantRanges()
    set(value) {
        pPushConstantRanges(value)
    }

//typedef struct VkPipelineLayoutCreateInfo {
//    VkStructureType                 sType;
//    const void*                     pNext;
//    VkPipelineLayoutCreateFlags     flags;
//    uint32_t                        setLayoutCount;
//    const VkDescriptorSetLayout*    pSetLayouts;
//    uint32_t                        pushConstantRangeCount;
//    const VkPushConstantRange*      pPushConstantRanges;
//} VkPipelineLayoutCreateInfo;
//
//typedef struct VkSamplerCreateInfo {
//    VkStructureType         sType;
//    const void*             pNext;
//    VkSamplerCreateFlags    flags;
//    VkFilter                magFilter;
//    VkFilter                minFilter;
//    VkSamplerMipmapMode     mipmapMode;
//    VkSamplerAddressMode    addressModeU;
//    VkSamplerAddressMode    addressModeV;
//    VkSamplerAddressMode    addressModeW;
//    float                   mipLodBias;
//    VkBool32                anisotropyEnable;
//    float                   maxAnisotropy;
//    VkBool32                compareEnable;
//    VkCompareOp             compareOp;
//    float                   minLod;
//    float                   maxLod;
//    VkBorderColor           borderColor;
//    VkBool32                unnormalizedCoordinates;
//} VkSamplerCreateInfo;
//

inline var VkDescriptorSetLayoutBinding.binding
    get() = binding()
    set(value) {
        binding(value)
    }
inline var VkDescriptorSetLayoutBinding.descriptorType: VkDescriptorType
    get() = descriptorType()
    set(value) {
        descriptorType(value)
    }
inline var VkDescriptorSetLayoutBinding.descriptorCount
    get() = descriptorCount()
    set(value) {
        descriptorCount(value)
    }
inline var VkDescriptorSetLayoutBinding.stageFlags: VkShaderStageFlags
    get() = stageFlags()
    set(value) {
        stageFlags(value)
    }
inline var VkDescriptorSetLayoutBinding.immutableSamplers: VkSamplerPtr?
    get() = pImmutableSamplers()
    set(value) {
        pImmutableSamplers(value)
    }


inline var VkDescriptorSetLayoutCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkDescriptorSetLayoutCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkDescriptorSetLayoutCreateInfo.flags: VkDescriptorSetLayoutCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline val VkDescriptorSetLayoutCreateInfo.bindingCount get() = bindingCount()
inline var VkDescriptorSetLayoutCreateInfo.bindings
    get() = pBindings()
    set(value) {
        pBindings(value)
    }

//typedef struct VkDescriptorSetLayoutCreateInfo {
//    VkStructureType                        sType;
//    const void*                            pNext;
//    VkDescriptorSetLayoutCreateFlags       flags;
//    uint32_t                               bindingCount;
//    const VkDescriptorSetLayoutBinding*    pBindings;
//} VkDescriptorSetLayoutCreateInfo;

inline var VkDescriptorPoolSize.type: VkDescriptorType
    get() = type()
    set(value) {
        type(value)
    }
inline var VkDescriptorPoolSize.descriptorCount
    get() = descriptorCount()
    set(value) {
        descriptorCount(value)
    }

//typedef struct VkDescriptorPoolSize {
//    VkDescriptorType    type;
//    uint32_t            descriptorCount;
//} VkDescriptorPoolSize;

inline var VkDescriptorPoolCreateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkDescriptorPoolCreateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkDescriptorPoolCreateInfo.flags: VkDescriptorPoolCreateFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkDescriptorPoolCreateInfo.maxSets
    get() = maxSets()
    set(value) {
        maxSets(value)
    }
inline val VkDescriptorPoolCreateInfo.poolSizeCount get() = poolSizeCount()
inline var VkDescriptorPoolCreateInfo.poolSizes
    get() = pPoolSizes()
    set(value) {
        pPoolSizes(value)
    }

//typedef struct VkDescriptorPoolCreateInfo {
//    VkStructureType                sType;
//    const void*                    pNext;
//    VkDescriptorPoolCreateFlags    flags;
//    uint32_t                       maxSets;
//    uint32_t                       poolSizeCount;
//    const VkDescriptorPoolSize*    pPoolSizes;
//} VkDescriptorPoolCreateInfo;

inline var VkDescriptorSetAllocateInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkDescriptorSetAllocateInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkDescriptorSetAllocateInfo.descriptorPool: VkDescriptorPool
    get() = descriptorPool()
    set(value) {
        descriptorPool(value)
    }
inline val VkDescriptorSetAllocateInfo.descriptorSetCount get() = descriptorSetCount()
inline var VkDescriptorSetAllocateInfo.setLayouts
    get() = pSetLayouts()
    set(value) {
        pSetLayouts(value)
    }

//typedef struct VkDescriptorSetAllocateInfo {
//    VkStructureType                 sType;
//    const void*                     pNext;
//    VkDescriptorPool                descriptorPool;
//    uint32_t                        descriptorSetCount;
//    const VkDescriptorSetLayout*    pSetLayouts;
//} VkDescriptorSetAllocateInfo;
//
//typedef struct VkDescriptorImageInfo {
//    VkSampler        sampler;
//    VkImageView      imageView;
//    VkImageLayout    imageLayout;
//} VkDescriptorImageInfo;
//

inline var VkDescriptorBufferInfo.buffer: VkBuffer
    get() = buffer()
    set(value) {
        buffer(value)
    }
inline var VkDescriptorBufferInfo.offset: VkDeviceSize
    get() = offset()
    set(value) {
        offset(value)
    }
inline var VkDescriptorBufferInfo.range: VkDeviceSize
    get() = range()
    set(value) {
        range(value)
    }
inline var VkDescriptorBufferInfo.Buffer.buffer: VkBuffer
    get() = buffer()
    set(value) {
        buffer(value)
    }
inline var VkDescriptorBufferInfo.Buffer.offset: VkDeviceSize
    get() = offset()
    set(value) {
        offset(value)
    }
inline var VkDescriptorBufferInfo.Buffer.range: VkDeviceSize
    get() = range()
    set(value) {
        range(value)
    }


inline var VkWriteDescriptorSet.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkWriteDescriptorSet.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkWriteDescriptorSet.dstSet: VkDescriptorSet
    get() = dstSet()
    set(value) {
        dstSet(value)
    }
inline var VkWriteDescriptorSet.dstBinding
    get() = dstBinding()
    set(value) {
        dstBinding(value)
    }
inline var VkWriteDescriptorSet.dstArrayElement
    get() = dstArrayElement()
    set(value) {
        dstArrayElement(value)
    }
inline val VkWriteDescriptorSet.descriptorCount get() = descriptorCount()
inline var VkWriteDescriptorSet.descriptorType: VkDescriptorType
    get() = descriptorType()
    set(value) {
        descriptorType(value)
    }
inline var VkWriteDescriptorSet.imageInfo
    get() = pImageInfo()
    set(value) {
        pImageInfo(value)
    }
inline var VkWriteDescriptorSet.bufferInfo
    get() = pBufferInfo()
    set(value) {
        pBufferInfo(value)
    }
inline var VkWriteDescriptorSet.texelBufferView
    get() = pTexelBufferView()
    set(value) {
        pTexelBufferView(value)
    }
//typedef struct VkWriteDescriptorSet {
//    VkStructureType                  sType;
//    const void*                      pNext;
//    VkDescriptorSet                  dstSet;
//    uint32_t                         dstBinding;
//    uint32_t                         dstArrayElement;
//    uint32_t                         descriptorCount;
//    VkDescriptorType                 descriptorType;
//    const VkDescriptorImageInfo*     pImageInfo;
//    const VkDescriptorBufferInfo*    pBufferInfo;
//    const VkBufferView*              pTexelBufferView;
//} VkWriteDescriptorSet;
//
//typedef struct VkCopyDescriptorSet {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkDescriptorSet    srcSet;
//    uint32_t           srcBinding;
//    uint32_t           srcArrayElement;
//    VkDescriptorSet    dstSet;
//    uint32_t           dstBinding;
//    uint32_t           dstArrayElement;
//    uint32_t           descriptorCount;
//} VkCopyDescriptorSet;
//
//typedef struct VkFramebufferCreateInfo {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkFramebufferCreateFlags    flags;
//    VkRenderPass                renderPass;
//    uint32_t                    attachmentCount;
//    const VkImageView*          pAttachments;
//    uint32_t                    width;
//    uint32_t                    height;
//    uint32_t                    layers;
//} VkFramebufferCreateInfo;
//
//typedef struct VkAttachmentDescription {
//    VkAttachmentDescriptionFlags    flags;
//    VkFormat                        format;
//    VkSampleCountFlagBits           samples;
//    VkAttachmentLoadOp              loadOp;
//    VkAttachmentStoreOp             storeOp;
//    VkAttachmentLoadOp              stencilLoadOp;
//    VkAttachmentStoreOp             stencilStoreOp;
//    VkImageLayout                   initialLayout;
//    VkImageLayout                   finalLayout;
//} VkAttachmentDescription;
//
//typedef struct VkAttachmentReference {
//    uint32_t         attachment;
//    VkImageLayout    layout;
//} VkAttachmentReference;
//
//typedef struct VkSubpassDescription {
//    VkSubpassDescriptionFlags       flags;
//    VkPipelineBindPoint             pipelineBindPoint;
//    uint32_t                        inputAttachmentCount;
//    const VkAttachmentReference*    pInputAttachments;
//    uint32_t                        colorAttachmentCount;
//    const VkAttachmentReference*    pColorAttachments;
//    const VkAttachmentReference*    pResolveAttachments;
//    const VkAttachmentReference*    pDepthStencilAttachment;
//    uint32_t                        preserveAttachmentCount;
//    const uint32_t*                 pPreserveAttachments;
//} VkSubpassDescription;
//
//typedef struct VkSubpassDependency {
//    uint32_t                srcSubpass;
//    uint32_t                dstSubpass;
//    VkPipelineStageFlags    srcStageMask;
//    VkPipelineStageFlags    dstStageMask;
//    VkAccessFlags           srcAccessMask;
//    VkAccessFlags           dstAccessMask;
//    VkDependencyFlags       dependencyFlags;
//} VkSubpassDependency;
//
//typedef struct VkRenderPassCreateInfo {
//    VkStructureType                   sType;
//    const void*                       pNext;
//    VkRenderPassCreateFlags           flags;
//    uint32_t                          attachmentCount;
//    const VkAttachmentDescription*    pAttachments;
//    uint32_t                          subpassCount;
//    const VkSubpassDescription*       pSubpasses;
//    uint32_t                          dependencyCount;
//    const VkSubpassDependency*        pDependencies;
//} VkRenderPassCreateInfo;

inline var VkCommandPoolCreateInfo.type: VkStructureType
    get() = VkStructureType of VkCommandPoolCreateInfo.nsType(adr)
    set(value) = VkCommandPoolCreateInfo.nsType(adr, value.i)
inline var VkCommandPoolCreateInfo.next
    get() = VkCommandPoolCreateInfo.npNext(adr)
    set(value) = VkCommandPoolCreateInfo.npNext(adr, value)
inline var VkCommandPoolCreateInfo.flags: VkCommandPoolCreateFlags
    get() = VkCommandPoolCreateInfo.nflags(adr)
    set(value) = VkCommandPoolCreateInfo.nflags(adr, value)
inline var VkCommandPoolCreateInfo.queueFamilyIndex
    get() = VkCommandPoolCreateInfo.nqueueFamilyIndex(adr)
    set(value) = VkCommandPoolCreateInfo.nqueueFamilyIndex(adr, value)

//typedef struct VkCommandPoolCreateInfo {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkCommandPoolCreateFlags    flags;
//    uint32_t                    queueFamilyIndex;
//} VkCommandPoolCreateInfo;
//
//typedef struct VkCommandBufferAllocateInfo {
//    VkStructureType         sType;
//    const void*             pNext;
//    VkCommandPool           commandPool;
//    VkCommandBufferLevel    level;
//    uint32_t                commandBufferCount;
//} VkCommandBufferAllocateInfo;
//
//typedef struct VkCommandBufferInheritanceInfo {
//    VkStructureType                  sType;
//    const void*                      pNext;
//    VkRenderPass                     renderPass;
//    uint32_t                         subpass;
//    VkFramebuffer                    framebuffer;
//    VkBool32                         occlusionQueryEnable;
//    VkQueryControlFlags              queryFlags;
//    VkQueryPipelineStatisticFlags    pipelineStatistics;
//} VkCommandBufferInheritanceInfo;
//

inline var VkCommandBufferBeginInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkCommandBufferBeginInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkCommandBufferBeginInfo.flags: VkCommandBufferUsageFlags
    get() = flags()
    set(value) {
        flags(value)
    }
inline var VkCommandBufferBeginInfo.pInheritanceInfo
    get() = pInheritanceInfo()
    set(value) {
        pInheritanceInfo(value)
    }

inline var VkBufferCopy.srcOffset: VkDeviceSize
    get() = srcOffset()
    set(value) {
        srcOffset(value)
    }
inline var VkBufferCopy.dstOffset: VkDeviceSize
    get() = dstOffset()
    set(value) {
        dstOffset(value)
    }
inline var VkBufferCopy.size: VkDeviceSize
    get() = size()
    set(value) {
        size(value)
    }
inline var VkBufferCopy.Buffer.size: VkDeviceSize
    get() = size()
    set(value) {
        size(value)
    }

//typedef struct VkImageSubresourceLayers {
//    VkImageAspectFlags    aspectMask;
//    uint32_t              mipLevel;
//    uint32_t              baseArrayLayer;
//    uint32_t              layerCount;
//} VkImageSubresourceLayers;
//
//typedef struct VkImageCopy {
//    VkImageSubresourceLayers    srcSubresource;
//    VkOffset3D                  srcOffset;
//    VkImageSubresourceLayers    dstSubresource;
//    VkOffset3D                  dstOffset;
//    VkExtent3D                  extent;
//} VkImageCopy;
//
//typedef struct VkImageBlit {
//    VkImageSubresourceLayers    srcSubresource;
//    VkOffset3D                  srcOffsets[2];
//    VkImageSubresourceLayers    dstSubresource;
//    VkOffset3D                  dstOffsets[2];
//} VkImageBlit;
//
//typedef struct VkBufferImageCopy {
//    VkDeviceSize                bufferOffset;
//    uint32_t                    bufferRowLength;
//    uint32_t                    bufferImageHeight;
//    VkImageSubresourceLayers    imageSubresource;
//    VkOffset3D                  imageOffset;
//    VkExtent3D                  imageExtent;
//} VkBufferImageCopy;
//

//typedef struct VkClearAttachment {
//    VkImageAspectFlags    aspectMask;
//    uint32_t              colorAttachment;
//    VkClearValue          clearValue;
//} VkClearAttachment;
//
//typedef struct VkClearRect {
//    VkRect2D    rect;
//    uint32_t    baseArrayLayer;
//    uint32_t    layerCount;
//} VkClearRect;
//
//typedef struct VkImageResolve {
//    VkImageSubresourceLayers    srcSubresource;
//    VkOffset3D                  srcOffset;
//    VkImageSubresourceLayers    dstSubresource;
//    VkOffset3D                  dstOffset;
//    VkExtent3D                  extent;
//} VkImageResolve;
//
//typedef struct VkMemoryBarrier {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkAccessFlags      srcAccessMask;
//    VkAccessFlags      dstAccessMask;
//} VkMemoryBarrier;
//
//typedef struct VkBufferMemoryBarrier {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkAccessFlags      srcAccessMask;
//    VkAccessFlags      dstAccessMask;
//    uint32_t           srcQueueFamilyIndex;
//    uint32_t           dstQueueFamilyIndex;
//    VkBuffer           buffer;
//    VkDeviceSize       offset;
//    VkDeviceSize       size;
//} VkBufferMemoryBarrier;
//
//typedef struct VkImageMemoryBarrier {
//    VkStructureType            sType;
//    const void*                pNext;
//    VkAccessFlags              srcAccessMask;
//    VkAccessFlags              dstAccessMask;
//    VkImageLayout              oldLayout;
//    VkImageLayout              newLayout;
//    uint32_t                   srcQueueFamilyIndex;
//    uint32_t                   dstQueueFamilyIndex;
//    VkImage                    image;
//    VkImageSubresourceRange    subresourceRange;
//} VkImageMemoryBarrier;

inline var VkRenderPassBeginInfo.type: VkStructureType
    get() = VkStructureType of sType()
    set(value) {
        sType(value.i)
    }
inline var VkRenderPassBeginInfo.next
    get() = pNext()
    set(value) {
        pNext(value)
    }
inline var VkRenderPassBeginInfo.renderPass: VkRenderPass
    get() = renderPass()
    set(value) {
        renderPass(value)
    }
inline var VkRenderPassBeginInfo.framebuffer: VkFramebuffer
    get() = framebuffer()
    set(value) {
        framebuffer(value)
    }
inline var VkRenderPassBeginInfo.renderArea: VkRect2D
    get() = renderArea()
    set(value) {
        renderArea(value)
    }
inline val VkRenderPassBeginInfo.clearValueCount get() = clearValueCount()
inline var VkRenderPassBeginInfo.clearValues
    get() = pClearValues()
    set(value) {
        pClearValues(value)
    }

//typedef struct VkRenderPassBeginInfo {
//    VkStructureType        sType;
//    const void*            pNext;
//    VkRenderPass           renderPass;
//    VkFramebuffer          framebuffer;
//    VkRect2D               renderArea;
//    uint32_t               clearValueCount;
//    const VkClearValue*    pClearValues;
//} VkRenderPassBeginInfo;
//
//typedef struct VkDispatchIndirectCommand {
//    uint32_t    x;
//    uint32_t    y;
//    uint32_t    z;
//} VkDispatchIndirectCommand;
//
//typedef struct VkDrawIndexedIndirectCommand {
//    uint32_t    indexCount;
//    uint32_t    instanceCount;
//    uint32_t    firstIndex;
//    int32_t     vertexOffset;
//    uint32_t    firstInstance;
//} VkDrawIndexedIndirectCommand;
//
//typedef struct VkDrawIndirectCommand {
//    uint32_t    vertexCount;
//    uint32_t    instanceCount;
//    uint32_t    firstVertex;
//    uint32_t    firstInstance;
//} VkDrawIndirectCommand;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateInstance)(const VkInstanceCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkInstance* pInstance);
//typedef void (VKAPI_PTR *PFN_vkDestroyInstance)(VkInstance instance, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkEnumeratePhysicalDevices)(VkInstance instance, uint32_t* pPhysicalDeviceCount, VkPhysicalDevice* pPhysicalDevices);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceFeatures)(VkPhysicalDevice physicalDevice, VkPhysicalDeviceFeatures* pFeatures);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceFormatProperties)(VkPhysicalDevice physicalDevice, VkFormat format, VkFormatProperties* pFormatProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceImageFormatProperties)(VkPhysicalDevice physicalDevice, VkFormat format, VkImageType type, VkImageTiling tiling, VkImageUsageFlags usage, VkImageCreateFlags flags, VkImageFormatProperties* pImageFormatProperties);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceProperties)(VkPhysicalDevice physicalDevice, VkPhysicalDeviceProperties* pProperties);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceQueueFamilyProperties)(VkPhysicalDevice physicalDevice, uint32_t* pQueueFamilyPropertyCount, VkQueueFamilyProperties* pQueueFamilyProperties);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceMemoryProperties)(VkPhysicalDevice physicalDevice, VkPhysicalDeviceMemoryProperties* pMemoryProperties);
//typedef PFN_vkVoidFunction (VKAPI_PTR *PFN_vkGetInstanceProcAddr)(VkInstance instance, const char* pName);
//typedef PFN_vkVoidFunction (VKAPI_PTR *PFN_vkGetDeviceProcAddr)(VkDevice device, const char* pName);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateDevice)(VkPhysicalDevice physicalDevice, const VkDeviceCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkDevice* pDevice);
//typedef void (VKAPI_PTR *PFN_vkDestroyDevice)(VkDevice device, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkEnumerateInstanceExtensionProperties)(const char* pLayerName, uint32_t* pPropertyCount, VkExtensionProperties* pProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkEnumerateDeviceExtensionProperties)(VkPhysicalDevice physicalDevice, const char* pLayerName, uint32_t* pPropertyCount, VkExtensionProperties* pProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkEnumerateInstanceLayerProperties)(uint32_t* pPropertyCount, VkLayerProperties* pProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkEnumerateDeviceLayerProperties)(VkPhysicalDevice physicalDevice, uint32_t* pPropertyCount, VkLayerProperties* pProperties);
//typedef void (VKAPI_PTR *PFN_vkGetDeviceQueue)(VkDevice device, uint32_t queueFamilyIndex, uint32_t queueIndex, VkQueue* pQueue);
//typedef VkResult (VKAPI_PTR *PFN_vkQueueSubmit)(VkQueue queue, uint32_t submitCount, const VkSubmitInfo* pSubmits, VkFence fence);
//typedef VkResult (VKAPI_PTR *PFN_vkQueueWaitIdle)(VkQueue queue);
//typedef VkResult (VKAPI_PTR *PFN_vkDeviceWaitIdle)(VkDevice device);
//typedef VkResult (VKAPI_PTR *PFN_vkAllocateMemory)(VkDevice device, const VkMemoryAllocateInfo* pAllocateInfo, const VkAllocationCallbacks* pAllocator, VkDeviceMemory* pMemory);
//typedef void (VKAPI_PTR *PFN_vkFreeMemory)(VkDevice device, VkDeviceMemory memory, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkMapMemory)(VkDevice device, VkDeviceMemory memory, VkDeviceSize offset, VkDeviceSize size, VkMemoryMapFlags flags, void** ppData);
//typedef void (VKAPI_PTR *PFN_vkUnmapMemory)(VkDevice device, VkDeviceMemory memory);
//typedef VkResult (VKAPI_PTR *PFN_vkFlushMappedMemoryRanges)(VkDevice device, uint32_t memoryRangeCount, const VkMappedMemoryRange* pMemoryRanges);
//typedef VkResult (VKAPI_PTR *PFN_vkInvalidateMappedMemoryRanges)(VkDevice device, uint32_t memoryRangeCount, const VkMappedMemoryRange* pMemoryRanges);
//typedef void (VKAPI_PTR *PFN_vkGetDeviceMemoryCommitment)(VkDevice device, VkDeviceMemory memory, VkDeviceSize* pCommittedMemoryInBytes);
//typedef VkResult (VKAPI_PTR *PFN_vkBindBufferMemory)(VkDevice device, VkBuffer buffer, VkDeviceMemory memory, VkDeviceSize memoryOffset);
//typedef VkResult (VKAPI_PTR *PFN_vkBindImageMemory)(VkDevice device, VkImage image, VkDeviceMemory memory, VkDeviceSize memoryOffset);
//typedef void (VKAPI_PTR *PFN_vkGetBufferMemoryRequirements)(VkDevice device, VkBuffer buffer, VkMemoryRequirements* pMemoryRequirements);
//typedef void (VKAPI_PTR *PFN_vkGetImageMemoryRequirements)(VkDevice device, VkImage image, VkMemoryRequirements* pMemoryRequirements);
//typedef void (VKAPI_PTR *PFN_vkGetImageSparseMemoryRequirements)(VkDevice device, VkImage image, uint32_t* pSparseMemoryRequirementCount, VkSparseImageMemoryRequirements* pSparseMemoryRequirements);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceSparseImageFormatProperties)(VkPhysicalDevice physicalDevice, VkFormat format, VkImageType type, VkSampleCountFlagBits samples, VkImageUsageFlags usage, VkImageTiling tiling, uint32_t* pPropertyCount, VkSparseImageFormatProperties* pProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkQueueBindSparse)(VkQueue queue, uint32_t bindInfoCount, const VkBindSparseInfo* pBindInfo, VkFence fence);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateFence)(VkDevice device, const VkFenceCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkFence* pFence);
//typedef void (VKAPI_PTR *PFN_vkDestroyFence)(VkDevice device, VkFence fence, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkResetFences)(VkDevice device, uint32_t fenceCount, const VkFence* pFences);
//typedef VkResult (VKAPI_PTR *PFN_vkGetFenceStatus)(VkDevice device, VkFence fence);
//typedef VkResult (VKAPI_PTR *PFN_vkWaitForFences)(VkDevice device, uint32_t fenceCount, const VkFence* pFences, VkBool32 waitAll, uint64_t timeout);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateSemaphore)(VkDevice device, const VkSemaphoreCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSemaphore* pSemaphore);
//typedef void (VKAPI_PTR *PFN_vkDestroySemaphore)(VkDevice device, VkSemaphore semaphore, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateEvent)(VkDevice device, const VkEventCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkEvent* pEvent);
//typedef void (VKAPI_PTR *PFN_vkDestroyEvent)(VkDevice device, VkEvent event, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkGetEventStatus)(VkDevice device, VkEvent event);
//typedef VkResult (VKAPI_PTR *PFN_vkSetEvent)(VkDevice device, VkEvent event);
//typedef VkResult (VKAPI_PTR *PFN_vkResetEvent)(VkDevice device, VkEvent event);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateQueryPool)(VkDevice device, const VkQueryPoolCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkQueryPool* pQueryPool);
//typedef void (VKAPI_PTR *PFN_vkDestroyQueryPool)(VkDevice device, VkQueryPool queryPool, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkGetQueryPoolResults)(VkDevice device, VkQueryPool queryPool, uint32_t firstQuery, uint32_t queryCount, size_t dataSize, void* pData, VkDeviceSize stride, VkQueryResultFlags flags);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateBuffer)(VkDevice device, const VkBufferCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkBuffer* pBuffer);
//typedef void (VKAPI_PTR *PFN_vkDestroyBuffer)(VkDevice device, VkBuffer buffer, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateBufferView)(VkDevice device, const VkBufferViewCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkBufferView* pView);
//typedef void (VKAPI_PTR *PFN_vkDestroyBufferView)(VkDevice device, VkBufferView bufferView, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateImage)(VkDevice device, const VkImageCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkImage* pImage);
//typedef void (VKAPI_PTR *PFN_vkDestroyImage)(VkDevice device, VkImage image, const VkAllocationCallbacks* pAllocator);
//typedef void (VKAPI_PTR *PFN_vkGetImageSubresourceLayout)(VkDevice device, VkImage image, const VkImageSubresource* pSubresource, VkSubresourceLayout* pLayout);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateImageView)(VkDevice device, const VkImageViewCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkImageView* pView);
//typedef void (VKAPI_PTR *PFN_vkDestroyImageView)(VkDevice device, VkImageView imageView, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateShaderModule)(VkDevice device, const VkShaderModuleCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkShaderModule* pShaderModule);
//typedef void (VKAPI_PTR *PFN_vkDestroyShaderModule)(VkDevice device, VkShaderModule shaderModule, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreatePipelineCache)(VkDevice device, const VkPipelineCacheCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkPipelineCache* pPipelineCache);
//typedef void (VKAPI_PTR *PFN_vkDestroyPipelineCache)(VkDevice device, VkPipelineCache pipelineCache, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPipelineCacheData)(VkDevice device, VkPipelineCache pipelineCache, size_t* pDataSize, void* pData);
//typedef VkResult (VKAPI_PTR *PFN_vkMergePipelineCaches)(VkDevice device, VkPipelineCache dstCache, uint32_t srcCacheCount, const VkPipelineCache* pSrcCaches);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateGraphicsPipelines)(VkDevice device, VkPipelineCache pipelineCache, uint32_t createInfoCount, const VkGraphicsPipelineCreateInfo* pCreateInfos, const VkAllocationCallbacks* pAllocator, VkPipeline* pPipelines);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateComputePipelines)(VkDevice device, VkPipelineCache pipelineCache, uint32_t createInfoCount, const VkComputePipelineCreateInfo* pCreateInfos, const VkAllocationCallbacks* pAllocator, VkPipeline* pPipelines);
//typedef void (VKAPI_PTR *PFN_vkDestroyPipeline)(VkDevice device, VkPipeline pipeline, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreatePipelineLayout)(VkDevice device, const VkPipelineLayoutCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkPipelineLayout* pPipelineLayout);
//typedef void (VKAPI_PTR *PFN_vkDestroyPipelineLayout)(VkDevice device, VkPipelineLayout pipelineLayout, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateSampler)(VkDevice device, const VkSamplerCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSampler* pSampler);
//typedef void (VKAPI_PTR *PFN_vkDestroySampler)(VkDevice device, VkSampler sampler, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateDescriptorSetLayout)(VkDevice device, const VkDescriptorSetLayoutCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkDescriptorSetLayout* pSetLayout);
//typedef void (VKAPI_PTR *PFN_vkDestroyDescriptorSetLayout)(VkDevice device, VkDescriptorSetLayout descriptorSetLayout, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateDescriptorPool)(VkDevice device, const VkDescriptorPoolCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkDescriptorPool* pDescriptorPool);
//typedef void (VKAPI_PTR *PFN_vkDestroyDescriptorPool)(VkDevice device, VkDescriptorPool descriptorPool, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkResetDescriptorPool)(VkDevice device, VkDescriptorPool descriptorPool, VkDescriptorPoolResetFlags flags);
//typedef VkResult (VKAPI_PTR *PFN_vkAllocateDescriptorSets)(VkDevice device, const VkDescriptorSetAllocateInfo* pAllocateInfo, VkDescriptorSet* pDescriptorSets);
//typedef VkResult (VKAPI_PTR *PFN_vkFreeDescriptorSets)(VkDevice device, VkDescriptorPool descriptorPool, uint32_t descriptorSetCount, const VkDescriptorSet* pDescriptorSets);
//typedef void (VKAPI_PTR *PFN_vkUpdateDescriptorSets)(VkDevice device, uint32_t descriptorWriteCount, const VkWriteDescriptorSet* pDescriptorWrites, uint32_t descriptorCopyCount, const VkCopyDescriptorSet* pDescriptorCopies);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateFramebuffer)(VkDevice device, const VkFramebufferCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkFramebuffer* pFramebuffer);
//typedef void (VKAPI_PTR *PFN_vkDestroyFramebuffer)(VkDevice device, VkFramebuffer framebuffer, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateRenderPass)(VkDevice device, const VkRenderPassCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkRenderPass* pRenderPass);
//typedef void (VKAPI_PTR *PFN_vkDestroyRenderPass)(VkDevice device, VkRenderPass renderPass, const VkAllocationCallbacks* pAllocator);
//typedef void (VKAPI_PTR *PFN_vkGetRenderAreaGranularity)(VkDevice device, VkRenderPass renderPass, VkExtent2D* pGranularity);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateCommandPool)(VkDevice device, const VkCommandPoolCreateInfo* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkCommandPool* pCommandPool);
//typedef void (VKAPI_PTR *PFN_vkDestroyCommandPool)(VkDevice device, VkCommandPool commandPool, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkResetCommandPool)(VkDevice device, VkCommandPool commandPool, VkCommandPoolResetFlags flags);
//typedef VkResult (VKAPI_PTR *PFN_vkAllocateCommandBuffers)(VkDevice device, const VkCommandBufferAllocateInfo* pAllocateInfo, VkCommandBuffer* pCommandBuffers);
//typedef void (VKAPI_PTR *PFN_vkFreeCommandBuffers)(VkDevice device, VkCommandPool commandPool, uint32_t commandBufferCount, const VkCommandBuffer* pCommandBuffers);
//typedef VkResult (VKAPI_PTR *PFN_vkBeginCommandBuffer)(VkCommandBuffer commandBuffer, const VkCommandBufferBeginInfo* pBeginInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkEndCommandBuffer)(VkCommandBuffer commandBuffer);
//typedef VkResult (VKAPI_PTR *PFN_vkResetCommandBuffer)(VkCommandBuffer commandBuffer, VkCommandBufferResetFlags flags);
//typedef void (VKAPI_PTR *PFN_vkCmdBindPipeline)(VkCommandBuffer commandBuffer, VkPipelineBindPoint pipelineBindPoint, VkPipeline pipeline);
//typedef void (VKAPI_PTR *PFN_vkCmdSetViewport)(VkCommandBuffer commandBuffer, uint32_t firstViewport, uint32_t viewportCount, const VkViewport* pViewports);
//typedef void (VKAPI_PTR *PFN_vkCmdSetScissor)(VkCommandBuffer commandBuffer, uint32_t firstScissor, uint32_t scissorCount, const VkRect2D* pScissors);
//typedef void (VKAPI_PTR *PFN_vkCmdSetLineWidth)(VkCommandBuffer commandBuffer, float lineWidth);
//typedef void (VKAPI_PTR *PFN_vkCmdSetDepthBias)(VkCommandBuffer commandBuffer, float depthBiasConstantFactor, float depthBiasClamp, float depthBiasSlopeFactor);
//typedef void (VKAPI_PTR *PFN_vkCmdSetBlendConstants)(VkCommandBuffer commandBuffer, const float blendConstants[4]);
//typedef void (VKAPI_PTR *PFN_vkCmdSetDepthBounds)(VkCommandBuffer commandBuffer, float minDepthBounds, float maxDepthBounds);
//typedef void (VKAPI_PTR *PFN_vkCmdSetStencilCompareMask)(VkCommandBuffer commandBuffer, VkStencilFaceFlags faceMask, uint32_t compareMask);
//typedef void (VKAPI_PTR *PFN_vkCmdSetStencilWriteMask)(VkCommandBuffer commandBuffer, VkStencilFaceFlags faceMask, uint32_t writeMask);
//typedef void (VKAPI_PTR *PFN_vkCmdSetStencilReference)(VkCommandBuffer commandBuffer, VkStencilFaceFlags faceMask, uint32_t reference);
//typedef void (VKAPI_PTR *PFN_vkCmdBindDescriptorSets)(VkCommandBuffer commandBuffer, VkPipelineBindPoint pipelineBindPoint, VkPipelineLayout layout, uint32_t firstSet, uint32_t descriptorSetCount, const VkDescriptorSet* pDescriptorSets, uint32_t dynamicOffsetCount, const uint32_t* pDynamicOffsets);
//typedef void (VKAPI_PTR *PFN_vkCmdBindIndexBuffer)(VkCommandBuffer commandBuffer, VkBuffer buffer, VkDeviceSize offset, VkIndexType indexType);
//typedef void (VKAPI_PTR *PFN_vkCmdBindVertexBuffers)(VkCommandBuffer commandBuffer, uint32_t firstBinding, uint32_t bindingCount, const VkBuffer* pBuffers, const VkDeviceSize* pOffsets);
//typedef void (VKAPI_PTR *PFN_vkCmdDraw)(VkCommandBuffer commandBuffer, uint32_t vertexCount, uint32_t instanceCount, uint32_t firstVertex, uint32_t firstInstance);
//typedef void (VKAPI_PTR *PFN_vkCmdDrawIndexed)(VkCommandBuffer commandBuffer, uint32_t indexCount, uint32_t instanceCount, uint32_t firstIndex, int32_t vertexOffset, uint32_t firstInstance);
//typedef void (VKAPI_PTR *PFN_vkCmdDrawIndirect)(VkCommandBuffer commandBuffer, VkBuffer buffer, VkDeviceSize offset, uint32_t drawCount, uint32_t stride);
//typedef void (VKAPI_PTR *PFN_vkCmdDrawIndexedIndirect)(VkCommandBuffer commandBuffer, VkBuffer buffer, VkDeviceSize offset, uint32_t drawCount, uint32_t stride);
//typedef void (VKAPI_PTR *PFN_vkCmdDispatch)(VkCommandBuffer commandBuffer, uint32_t groupCountX, uint32_t groupCountY, uint32_t groupCountZ);
//typedef void (VKAPI_PTR *PFN_vkCmdDispatchIndirect)(VkCommandBuffer commandBuffer, VkBuffer buffer, VkDeviceSize offset);
//typedef void (VKAPI_PTR *PFN_vkCmdCopyBuffer)(VkCommandBuffer commandBuffer, VkBuffer srcBuffer, VkBuffer dstBuffer, uint32_t regionCount, const VkBufferCopy* pRegions);
//typedef void (VKAPI_PTR *PFN_vkCmdCopyImage)(VkCommandBuffer commandBuffer, VkImage srcImage, VkImageLayout srcImageLayout, VkImage dstImage, VkImageLayout dstImageLayout, uint32_t regionCount, const VkImageCopy* pRegions);
//typedef void (VKAPI_PTR *PFN_vkCmdBlitImage)(VkCommandBuffer commandBuffer, VkImage srcImage, VkImageLayout srcImageLayout, VkImage dstImage, VkImageLayout dstImageLayout, uint32_t regionCount, const VkImageBlit* pRegions, VkFilter filter);
//typedef void (VKAPI_PTR *PFN_vkCmdCopyBufferToImage)(VkCommandBuffer commandBuffer, VkBuffer srcBuffer, VkImage dstImage, VkImageLayout dstImageLayout, uint32_t regionCount, const VkBufferImageCopy* pRegions);
//typedef void (VKAPI_PTR *PFN_vkCmdCopyImageToBuffer)(VkCommandBuffer commandBuffer, VkImage srcImage, VkImageLayout srcImageLayout, VkBuffer dstBuffer, uint32_t regionCount, const VkBufferImageCopy* pRegions);
//typedef void (VKAPI_PTR *PFN_vkCmdUpdateBuffer)(VkCommandBuffer commandBuffer, VkBuffer dstBuffer, VkDeviceSize dstOffset, VkDeviceSize dataSize, const void* pData);
//typedef void (VKAPI_PTR *PFN_vkCmdFillBuffer)(VkCommandBuffer commandBuffer, VkBuffer dstBuffer, VkDeviceSize dstOffset, VkDeviceSize size, uint32_t data);
//typedef void (VKAPI_PTR *PFN_vkCmdClearColorImage)(VkCommandBuffer commandBuffer, VkImage image, VkImageLayout imageLayout, const VkClearColorValue* pColor, uint32_t rangeCount, const VkImageSubresourceRange* pRanges);
//typedef void (VKAPI_PTR *PFN_vkCmdClearDepthStencilImage)(VkCommandBuffer commandBuffer, VkImage image, VkImageLayout imageLayout, const VkClearDepthStencilValue* pDepthStencil, uint32_t rangeCount, const VkImageSubresourceRange* pRanges);
//typedef void (VKAPI_PTR *PFN_vkCmdClearAttachments)(VkCommandBuffer commandBuffer, uint32_t attachmentCount, const VkClearAttachment* pAttachments, uint32_t rectCount, const VkClearRect* pRects);
//typedef void (VKAPI_PTR *PFN_vkCmdResolveImage)(VkCommandBuffer commandBuffer, VkImage srcImage, VkImageLayout srcImageLayout, VkImage dstImage, VkImageLayout dstImageLayout, uint32_t regionCount, const VkImageResolve* pRegions);
//typedef void (VKAPI_PTR *PFN_vkCmdSetEvent)(VkCommandBuffer commandBuffer, VkEvent event, VkPipelineStageFlags stageMask);
//typedef void (VKAPI_PTR *PFN_vkCmdResetEvent)(VkCommandBuffer commandBuffer, VkEvent event, VkPipelineStageFlags stageMask);
//typedef void (VKAPI_PTR *PFN_vkCmdWaitEvents)(VkCommandBuffer commandBuffer, uint32_t eventCount, const VkEvent* pEvents, VkPipelineStageFlags srcStageMask, VkPipelineStageFlags dstStageMask, uint32_t memoryBarrierCount, const VkMemoryBarrier* pMemoryBarriers, uint32_t bufferMemoryBarrierCount, const VkBufferMemoryBarrier* pBufferMemoryBarriers, uint32_t imageMemoryBarrierCount, const VkImageMemoryBarrier* pImageMemoryBarriers);
//typedef void (VKAPI_PTR *PFN_vkCmdPipelineBarrier)(VkCommandBuffer commandBuffer, VkPipelineStageFlags srcStageMask, VkPipelineStageFlags dstStageMask, VkDependencyFlags dependencyFlags, uint32_t memoryBarrierCount, const VkMemoryBarrier* pMemoryBarriers, uint32_t bufferMemoryBarrierCount, const VkBufferMemoryBarrier* pBufferMemoryBarriers, uint32_t imageMemoryBarrierCount, const VkImageMemoryBarrier* pImageMemoryBarriers);
//typedef void (VKAPI_PTR *PFN_vkCmdBeginQuery)(VkCommandBuffer commandBuffer, VkQueryPool queryPool, uint32_t query, VkQueryControlFlags flags);
//typedef void (VKAPI_PTR *PFN_vkCmdEndQuery)(VkCommandBuffer commandBuffer, VkQueryPool queryPool, uint32_t query);
//typedef void (VKAPI_PTR *PFN_vkCmdResetQueryPool)(VkCommandBuffer commandBuffer, VkQueryPool queryPool, uint32_t firstQuery, uint32_t queryCount);
//typedef void (VKAPI_PTR *PFN_vkCmdWriteTimestamp)(VkCommandBuffer commandBuffer, VkPipelineStageFlagBits pipelineStage, VkQueryPool queryPool, uint32_t query);
//typedef void (VKAPI_PTR *PFN_vkCmdCopyQueryPoolResults)(VkCommandBuffer commandBuffer, VkQueryPool queryPool, uint32_t firstQuery, uint32_t queryCount, VkBuffer dstBuffer, VkDeviceSize dstOffset, VkDeviceSize stride, VkQueryResultFlags flags);
//typedef void (VKAPI_PTR *PFN_vkCmdPushConstants)(VkCommandBuffer commandBuffer, VkPipelineLayout layout, VkShaderStageFlags stageFlags, uint32_t offset, uint32_t size, const void* pValues);
//typedef void (VKAPI_PTR *PFN_vkCmdBeginRenderPass)(VkCommandBuffer commandBuffer, const VkRenderPassBeginInfo* pRenderPassBegin, VkSubpassContents contents);
//typedef void (VKAPI_PTR *PFN_vkCmdNextSubpass)(VkCommandBuffer commandBuffer, VkSubpassContents contents);
//typedef void (VKAPI_PTR *PFN_vkCmdEndRenderPass)(VkCommandBuffer commandBuffer);
//typedef void (VKAPI_PTR *PFN_vkCmdExecuteCommands)(VkCommandBuffer commandBuffer, uint32_t commandBufferCount, const VkCommandBuffer* pCommandBuffers);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateInstance(
//const VkInstanceCreateInfo*                 pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkInstance*                                 pInstance);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyInstance(
//VkInstance                                  instance,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkEnumeratePhysicalDevices(
//VkInstance                                  instance,
//uint32_t*                                   pPhysicalDeviceCount,
//VkPhysicalDevice*                           pPhysicalDevices);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceFeatures(
//VkPhysicalDevice                            physicalDevice,
//VkPhysicalDeviceFeatures*                   pFeatures);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceFormatProperties(
//VkPhysicalDevice                            physicalDevice,
//VkFormat                                    format,
//VkFormatProperties*                         pFormatProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceImageFormatProperties(
//VkPhysicalDevice                            physicalDevice,
//VkFormat                                    format,
//VkImageType                                 type,
//VkImageTiling                               tiling,
//VkImageUsageFlags                           usage,
//VkImageCreateFlags                          flags,
//VkImageFormatProperties*                    pImageFormatProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceProperties(
//VkPhysicalDevice                            physicalDevice,
//VkPhysicalDeviceProperties*                 pProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceQueueFamilyProperties(
//VkPhysicalDevice                            physicalDevice,
//uint32_t*                                   pQueueFamilyPropertyCount,
//VkQueueFamilyProperties*                    pQueueFamilyProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceMemoryProperties(
//VkPhysicalDevice                            physicalDevice,
//VkPhysicalDeviceMemoryProperties*           pMemoryProperties);
//
//VKAPI_ATTR PFN_vkVoidFunction VKAPI_CALL vkGetInstanceProcAddr(
//VkInstance                                  instance,
//const char*                                 pName);
//
//VKAPI_ATTR PFN_vkVoidFunction VKAPI_CALL vkGetDeviceProcAddr(
//VkDevice                                    device,
//const char*                                 pName);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateDevice(
//VkPhysicalDevice                            physicalDevice,
//const VkDeviceCreateInfo*                   pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkDevice*                                   pDevice);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyDevice(
//VkDevice                                    device,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkEnumerateInstanceExtensionProperties(
//const char*                                 pLayerName,
//uint32_t*                                   pPropertyCount,
//VkExtensionProperties*                      pProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkEnumerateDeviceExtensionProperties(
//VkPhysicalDevice                            physicalDevice,
//const char*                                 pLayerName,
//uint32_t*                                   pPropertyCount,
//VkExtensionProperties*                      pProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkEnumerateInstanceLayerProperties(
//uint32_t*                                   pPropertyCount,
//VkLayerProperties*                          pProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkEnumerateDeviceLayerProperties(
//VkPhysicalDevice                            physicalDevice,
//uint32_t*                                   pPropertyCount,
//VkLayerProperties*                          pProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetDeviceQueue(
//VkDevice                                    device,
//uint32_t                                    queueFamilyIndex,
//uint32_t                                    queueIndex,
//VkQueue*                                    pQueue);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkQueueSubmit(
//VkQueue                                     queue,
//uint32_t                                    submitCount,
//const VkSubmitInfo*                         pSubmits,
//VkFence                                     fence);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkQueueWaitIdle(
//VkQueue                                     queue);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkDeviceWaitIdle(
//VkDevice                                    device);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkAllocateMemory(
//VkDevice                                    device,
//const VkMemoryAllocateInfo*                 pAllocateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkDeviceMemory*                             pMemory);
//
//VKAPI_ATTR void VKAPI_CALL vkFreeMemory(
//VkDevice                                    device,
//VkDeviceMemory                              memory,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkMapMemory(
//VkDevice                                    device,
//VkDeviceMemory                              memory,
//VkDeviceSize                                offset,
//VkDeviceSize                                size,
//VkMemoryMapFlags                            flags,
//void**                                      ppData);
//
//VKAPI_ATTR void VKAPI_CALL vkUnmapMemory(
//VkDevice                                    device,
//VkDeviceMemory                              memory);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkFlushMappedMemoryRanges(
//VkDevice                                    device,
//uint32_t                                    memoryRangeCount,
//const VkMappedMemoryRange*                  pMemoryRanges);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkInvalidateMappedMemoryRanges(
//VkDevice                                    device,
//uint32_t                                    memoryRangeCount,
//const VkMappedMemoryRange*                  pMemoryRanges);
//
//VKAPI_ATTR void VKAPI_CALL vkGetDeviceMemoryCommitment(
//VkDevice                                    device,
//VkDeviceMemory                              memory,
//VkDeviceSize*                               pCommittedMemoryInBytes);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkBindBufferMemory(
//VkDevice                                    device,
//VkBuffer                                    buffer,
//VkDeviceMemory                              memory,
//VkDeviceSize                                memoryOffset);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkBindImageMemory(
//VkDevice                                    device,
//VkImage                                     image,
//VkDeviceMemory                              memory,
//VkDeviceSize                                memoryOffset);
//
//VKAPI_ATTR void VKAPI_CALL vkGetBufferMemoryRequirements(
//VkDevice                                    device,
//VkBuffer                                    buffer,
//VkMemoryRequirements*                       pMemoryRequirements);
//
//VKAPI_ATTR void VKAPI_CALL vkGetImageMemoryRequirements(
//VkDevice                                    device,
//VkImage                                     image,
//VkMemoryRequirements*                       pMemoryRequirements);
//
//VKAPI_ATTR void VKAPI_CALL vkGetImageSparseMemoryRequirements(
//VkDevice                                    device,
//VkImage                                     image,
//uint32_t*                                   pSparseMemoryRequirementCount,
//VkSparseImageMemoryRequirements*            pSparseMemoryRequirements);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceSparseImageFormatProperties(
//VkPhysicalDevice                            physicalDevice,
//VkFormat                                    format,
//VkImageType                                 type,
//VkSampleCountFlagBits                       samples,
//VkImageUsageFlags                           usage,
//VkImageTiling                               tiling,
//uint32_t*                                   pPropertyCount,
//VkSparseImageFormatProperties*              pProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkQueueBindSparse(
//VkQueue                                     queue,
//uint32_t                                    bindInfoCount,
//const VkBindSparseInfo*                     pBindInfo,
//VkFence                                     fence);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateFence(
//VkDevice                                    device,
//const VkFenceCreateInfo*                    pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkFence*                                    pFence);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyFence(
//VkDevice                                    device,
//VkFence                                     fence,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkResetFences(
//VkDevice                                    device,
//uint32_t                                    fenceCount,
//const VkFence*                              pFences);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetFenceStatus(
//VkDevice                                    device,
//VkFence                                     fence);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkWaitForFences(
//VkDevice                                    device,
//uint32_t                                    fenceCount,
//const VkFence*                              pFences,
//VkBool32                                    waitAll,
//uint64_t                                    timeout);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateSemaphore(
//VkDevice                                    device,
//const VkSemaphoreCreateInfo*                pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSemaphore*                                pSemaphore);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroySemaphore(
//VkDevice                                    device,
//VkSemaphore                                 semaphore,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateEvent(
//VkDevice                                    device,
//const VkEventCreateInfo*                    pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkEvent*                                    pEvent);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyEvent(
//VkDevice                                    device,
//VkEvent                                     event,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetEventStatus(
//VkDevice                                    device,
//VkEvent                                     event);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkSetEvent(
//VkDevice                                    device,
//VkEvent                                     event);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkResetEvent(
//VkDevice                                    device,
//VkEvent                                     event);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateQueryPool(
//VkDevice                                    device,
//const VkQueryPoolCreateInfo*                pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkQueryPool*                                pQueryPool);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyQueryPool(
//VkDevice                                    device,
//VkQueryPool                                 queryPool,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetQueryPoolResults(
//VkDevice                                    device,
//VkQueryPool                                 queryPool,
//uint32_t                                    firstQuery,
//uint32_t                                    queryCount,
//size_t                                      dataSize,
//void*                                       pData,
//VkDeviceSize                                stride,
//VkQueryResultFlags                          flags);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateBuffer(
//VkDevice                                    device,
//const VkBufferCreateInfo*                   pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkBuffer*                                   pBuffer);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyBuffer(
//VkDevice                                    device,
//VkBuffer                                    buffer,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateBufferView(
//VkDevice                                    device,
//const VkBufferViewCreateInfo*               pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkBufferView*                               pView);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyBufferView(
//VkDevice                                    device,
//VkBufferView                                bufferView,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateImage(
//VkDevice                                    device,
//const VkImageCreateInfo*                    pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkImage*                                    pImage);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyImage(
//VkDevice                                    device,
//VkImage                                     image,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR void VKAPI_CALL vkGetImageSubresourceLayout(
//VkDevice                                    device,
//VkImage                                     image,
//const VkImageSubresource*                   pSubresource,
//VkSubresourceLayout*                        pLayout);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateImageView(
//VkDevice                                    device,
//const VkImageViewCreateInfo*                pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkImageView*                                pView);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyImageView(
//VkDevice                                    device,
//VkImageView                                 imageView,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateShaderModule(
//VkDevice                                    device,
//const VkShaderModuleCreateInfo*             pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkShaderModule*                             pShaderModule);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyShaderModule(
//VkDevice                                    device,
//VkShaderModule                              shaderModule,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreatePipelineCache(
//VkDevice                                    device,
//const VkPipelineCacheCreateInfo*            pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkPipelineCache*                            pPipelineCache);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyPipelineCache(
//VkDevice                                    device,
//VkPipelineCache                             pipelineCache,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPipelineCacheData(
//VkDevice                                    device,
//VkPipelineCache                             pipelineCache,
//size_t*                                     pDataSize,
//void*                                       pData);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkMergePipelineCaches(
//VkDevice                                    device,
//VkPipelineCache                             dstCache,
//uint32_t                                    srcCacheCount,
//const VkPipelineCache*                      pSrcCaches);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateGraphicsPipelines(
//VkDevice                                    device,
//VkPipelineCache                             pipelineCache,
//uint32_t                                    createInfoCount,
//const VkGraphicsPipelineCreateInfo*         pCreateInfos,
//const VkAllocationCallbacks*                pAllocator,
//VkPipeline*                                 pPipelines);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateComputePipelines(
//VkDevice                                    device,
//VkPipelineCache                             pipelineCache,
//uint32_t                                    createInfoCount,
//const VkComputePipelineCreateInfo*          pCreateInfos,
//const VkAllocationCallbacks*                pAllocator,
//VkPipeline*                                 pPipelines);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyPipeline(
//VkDevice                                    device,
//VkPipeline                                  pipeline,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreatePipelineLayout(
//VkDevice                                    device,
//const VkPipelineLayoutCreateInfo*           pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkPipelineLayout*                           pPipelineLayout);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyPipelineLayout(
//VkDevice                                    device,
//VkPipelineLayout                            pipelineLayout,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateSampler(
//VkDevice                                    device,
//const VkSamplerCreateInfo*                  pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSampler*                                  pSampler);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroySampler(
//VkDevice                                    device,
//VkSampler                                   sampler,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateDescriptorSetLayout(
//VkDevice                                    device,
//const VkDescriptorSetLayoutCreateInfo*      pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkDescriptorSetLayout*                      pSetLayout);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyDescriptorSetLayout(
//VkDevice                                    device,
//VkDescriptorSetLayout                       descriptorSetLayout,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateDescriptorPool(
//VkDevice                                    device,
//const VkDescriptorPoolCreateInfo*           pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkDescriptorPool*                           pDescriptorPool);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyDescriptorPool(
//VkDevice                                    device,
//VkDescriptorPool                            descriptorPool,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkResetDescriptorPool(
//VkDevice                                    device,
//VkDescriptorPool                            descriptorPool,
//VkDescriptorPoolResetFlags                  flags);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkAllocateDescriptorSets(
//VkDevice                                    device,
//const VkDescriptorSetAllocateInfo*          pAllocateInfo,
//VkDescriptorSet*                            pDescriptorSets);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkFreeDescriptorSets(
//VkDevice                                    device,
//VkDescriptorPool                            descriptorPool,
//uint32_t                                    descriptorSetCount,
//const VkDescriptorSet*                      pDescriptorSets);
//
//VKAPI_ATTR void VKAPI_CALL vkUpdateDescriptorSets(
//VkDevice                                    device,
//uint32_t                                    descriptorWriteCount,
//const VkWriteDescriptorSet*                 pDescriptorWrites,
//uint32_t                                    descriptorCopyCount,
//const VkCopyDescriptorSet*                  pDescriptorCopies);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateFramebuffer(
//VkDevice                                    device,
//const VkFramebufferCreateInfo*              pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkFramebuffer*                              pFramebuffer);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyFramebuffer(
//VkDevice                                    device,
//VkFramebuffer                               framebuffer,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateRenderPass(
//VkDevice                                    device,
//const VkRenderPassCreateInfo*               pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkRenderPass*                               pRenderPass);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyRenderPass(
//VkDevice                                    device,
//VkRenderPass                                renderPass,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR void VKAPI_CALL vkGetRenderAreaGranularity(
//VkDevice                                    device,
//VkRenderPass                                renderPass,
//VkExtent2D*                                 pGranularity);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateCommandPool(
//VkDevice                                    device,
//const VkCommandPoolCreateInfo*              pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkCommandPool*                              pCommandPool);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyCommandPool(
//VkDevice                                    device,
//VkCommandPool                               commandPool,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkResetCommandPool(
//VkDevice                                    device,
//VkCommandPool                               commandPool,
//VkCommandPoolResetFlags                     flags);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkAllocateCommandBuffers(
//VkDevice                                    device,
//const VkCommandBufferAllocateInfo*          pAllocateInfo,
//VkCommandBuffer*                            pCommandBuffers);
//
//VKAPI_ATTR void VKAPI_CALL vkFreeCommandBuffers(
//VkDevice                                    device,
//VkCommandPool                               commandPool,
//uint32_t                                    commandBufferCount,
//const VkCommandBuffer*                      pCommandBuffers);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkBeginCommandBuffer(
//VkCommandBuffer                             commandBuffer,
//const VkCommandBufferBeginInfo*             pBeginInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkEndCommandBuffer(
//VkCommandBuffer                             commandBuffer);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkResetCommandBuffer(
//VkCommandBuffer                             commandBuffer,
//VkCommandBufferResetFlags                   flags);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdBindPipeline(
//VkCommandBuffer                             commandBuffer,
//VkPipelineBindPoint                         pipelineBindPoint,
//VkPipeline                                  pipeline);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetViewport(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    firstViewport,
//uint32_t                                    viewportCount,
//const VkViewport*                           pViewports);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetScissor(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    firstScissor,
//uint32_t                                    scissorCount,
//const VkRect2D*                             pScissors);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetLineWidth(
//VkCommandBuffer                             commandBuffer,
//float                                       lineWidth);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetDepthBias(
//VkCommandBuffer                             commandBuffer,
//float                                       depthBiasConstantFactor,
//float                                       depthBiasClamp,
//float                                       depthBiasSlopeFactor);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetBlendConstants(
//VkCommandBuffer                             commandBuffer,
//const float                                 blendConstants[4]);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetDepthBounds(
//VkCommandBuffer                             commandBuffer,
//float                                       minDepthBounds,
//float                                       maxDepthBounds);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetStencilCompareMask(
//VkCommandBuffer                             commandBuffer,
//VkStencilFaceFlags                          faceMask,
//uint32_t                                    compareMask);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetStencilWriteMask(
//VkCommandBuffer                             commandBuffer,
//VkStencilFaceFlags                          faceMask,
//uint32_t                                    writeMask);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetStencilReference(
//VkCommandBuffer                             commandBuffer,
//VkStencilFaceFlags                          faceMask,
//uint32_t                                    reference);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdBindDescriptorSets(
//VkCommandBuffer                             commandBuffer,
//VkPipelineBindPoint                         pipelineBindPoint,
//VkPipelineLayout                            layout,
//uint32_t                                    firstSet,
//uint32_t                                    descriptorSetCount,
//const VkDescriptorSet*                      pDescriptorSets,
//uint32_t                                    dynamicOffsetCount,
//const uint32_t*                             pDynamicOffsets);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdBindIndexBuffer(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    buffer,
//VkDeviceSize                                offset,
//VkIndexType                                 indexType);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdBindVertexBuffers(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    firstBinding,
//uint32_t                                    bindingCount,
//const VkBuffer*                             pBuffers,
//const VkDeviceSize*                         pOffsets);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDraw(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    vertexCount,
//uint32_t                                    instanceCount,
//uint32_t                                    firstVertex,
//uint32_t                                    firstInstance);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDrawIndexed(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    indexCount,
//uint32_t                                    instanceCount,
//uint32_t                                    firstIndex,
//int32_t                                     vertexOffset,
//uint32_t                                    firstInstance);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDrawIndirect(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    buffer,
//VkDeviceSize                                offset,
//uint32_t                                    drawCount,
//uint32_t                                    stride);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDrawIndexedIndirect(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    buffer,
//VkDeviceSize                                offset,
//uint32_t                                    drawCount,
//uint32_t                                    stride);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDispatch(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    groupCountX,
//uint32_t                                    groupCountY,
//uint32_t                                    groupCountZ);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDispatchIndirect(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    buffer,
//VkDeviceSize                                offset);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdCopyBuffer(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    srcBuffer,
//VkBuffer                                    dstBuffer,
//uint32_t                                    regionCount,
//const VkBufferCopy*                         pRegions);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdCopyImage(
//VkCommandBuffer                             commandBuffer,
//VkImage                                     srcImage,
//VkImageLayout                               srcImageLayout,
//VkImage                                     dstImage,
//VkImageLayout                               dstImageLayout,
//uint32_t                                    regionCount,
//const VkImageCopy*                          pRegions);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdBlitImage(
//VkCommandBuffer                             commandBuffer,
//VkImage                                     srcImage,
//VkImageLayout                               srcImageLayout,
//VkImage                                     dstImage,
//VkImageLayout                               dstImageLayout,
//uint32_t                                    regionCount,
//const VkImageBlit*                          pRegions,
//VkFilter                                    filter);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdCopyBufferToImage(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    srcBuffer,
//VkImage                                     dstImage,
//VkImageLayout                               dstImageLayout,
//uint32_t                                    regionCount,
//const VkBufferImageCopy*                    pRegions);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdCopyImageToBuffer(
//VkCommandBuffer                             commandBuffer,
//VkImage                                     srcImage,
//VkImageLayout                               srcImageLayout,
//VkBuffer                                    dstBuffer,
//uint32_t                                    regionCount,
//const VkBufferImageCopy*                    pRegions);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdUpdateBuffer(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    dstBuffer,
//VkDeviceSize                                dstOffset,
//VkDeviceSize                                dataSize,
//const void*                                 pData);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdFillBuffer(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    dstBuffer,
//VkDeviceSize                                dstOffset,
//VkDeviceSize                                size,
//uint32_t                                    data);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdClearColorImage(
//VkCommandBuffer                             commandBuffer,
//VkImage                                     image,
//VkImageLayout                               imageLayout,
//const VkClearColorValue*                    pColor,
//uint32_t                                    rangeCount,
//const VkImageSubresourceRange*              pRanges);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdClearDepthStencilImage(
//VkCommandBuffer                             commandBuffer,
//VkImage                                     image,
//VkImageLayout                               imageLayout,
//const VkClearDepthStencilValue*             pDepthStencil,
//uint32_t                                    rangeCount,
//const VkImageSubresourceRange*              pRanges);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdClearAttachments(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    attachmentCount,
//const VkClearAttachment*                    pAttachments,
//uint32_t                                    rectCount,
//const VkClearRect*                          pRects);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdResolveImage(
//VkCommandBuffer                             commandBuffer,
//VkImage                                     srcImage,
//VkImageLayout                               srcImageLayout,
//VkImage                                     dstImage,
//VkImageLayout                               dstImageLayout,
//uint32_t                                    regionCount,
//const VkImageResolve*                       pRegions);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetEvent(
//VkCommandBuffer                             commandBuffer,
//VkEvent                                     event,
//VkPipelineStageFlags                        stageMask);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdResetEvent(
//VkCommandBuffer                             commandBuffer,
//VkEvent                                     event,
//VkPipelineStageFlags                        stageMask);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdWaitEvents(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    eventCount,
//const VkEvent*                              pEvents,
//VkPipelineStageFlags                        srcStageMask,
//VkPipelineStageFlags                        dstStageMask,
//uint32_t                                    memoryBarrierCount,
//const VkMemoryBarrier*                      pMemoryBarriers,
//uint32_t                                    bufferMemoryBarrierCount,
//const VkBufferMemoryBarrier*                pBufferMemoryBarriers,
//uint32_t                                    imageMemoryBarrierCount,
//const VkImageMemoryBarrier*                 pImageMemoryBarriers);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdPipelineBarrier(
//VkCommandBuffer                             commandBuffer,
//VkPipelineStageFlags                        srcStageMask,
//VkPipelineStageFlags                        dstStageMask,
//VkDependencyFlags                           dependencyFlags,
//uint32_t                                    memoryBarrierCount,
//const VkMemoryBarrier*                      pMemoryBarriers,
//uint32_t                                    bufferMemoryBarrierCount,
//const VkBufferMemoryBarrier*                pBufferMemoryBarriers,
//uint32_t                                    imageMemoryBarrierCount,
//const VkImageMemoryBarrier*                 pImageMemoryBarriers);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdBeginQuery(
//VkCommandBuffer                             commandBuffer,
//VkQueryPool                                 queryPool,
//uint32_t                                    query,
//VkQueryControlFlags                         flags);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdEndQuery(
//VkCommandBuffer                             commandBuffer,
//VkQueryPool                                 queryPool,
//uint32_t                                    query);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdResetQueryPool(
//VkCommandBuffer                             commandBuffer,
//VkQueryPool                                 queryPool,
//uint32_t                                    firstQuery,
//uint32_t                                    queryCount);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdWriteTimestamp(
//VkCommandBuffer                             commandBuffer,
//VkPipelineStageFlagBits                     pipelineStage,
//VkQueryPool                                 queryPool,
//uint32_t                                    query);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdCopyQueryPoolResults(
//VkCommandBuffer                             commandBuffer,
//VkQueryPool                                 queryPool,
//uint32_t                                    firstQuery,
//uint32_t                                    queryCount,
//VkBuffer                                    dstBuffer,
//VkDeviceSize                                dstOffset,
//VkDeviceSize                                stride,
//VkQueryResultFlags                          flags);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdPushConstants(
//VkCommandBuffer                             commandBuffer,
//VkPipelineLayout                            layout,
//VkShaderStageFlags                          stageFlags,
//uint32_t                                    offset,
//uint32_t                                    size,
//const void*                                 pValues);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdBeginRenderPass(
//VkCommandBuffer                             commandBuffer,
//const VkRenderPassBeginInfo*                pRenderPassBegin,
//VkSubpassContents                           contents);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdNextSubpass(
//VkCommandBuffer                             commandBuffer,
//VkSubpassContents                           contents);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdEndRenderPass(
//VkCommandBuffer                             commandBuffer);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdExecuteCommands(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    commandBufferCount,
//const VkCommandBuffer*                      pCommandBuffers);
//#endif
//
//#define VK_KHR_surface 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkSurfaceKHR)
//
//#define VK_KHR_SURFACE_SPEC_VERSION       25
//#define VK_KHR_SURFACE_EXTENSION_NAME     "VK_KHR_surface"
//#define VK_COLORSPACE_SRGB_NONLINEAR_KHR  VK_COLOR_SPACE_SRGB_NONLINEAR_KHR
//
//
enum class VkColorSpace(val i: Int) {
    SRGB_NONLINEAR_KHR(0),
    DISPLAY_P3_NONLINEAR_EXT(1000104001),
    EXTENDED_SRGB_LINEAR_EXT(1000104002),
    DCI_P3_LINEAR_EXT(1000104003),
    DCI_P3_NONLINEAR_EXT(1000104004),
    BT709_LINEAR_EXT(1000104005),
    BT709_NONLINEAR_EXT(1000104006),
    BT2020_LINEAR_EXT(1000104007),
    HDR10_ST2084_EXT(1000104008),
    DOLBYVISION_EXT(1000104009),
    HDR10_HLG_EXT(1000104010),
    ADOBERGB_LINEAR_EXT(1000104011),
    ADOBERGB_NONLINEAR_EXT(1000104012),
    PASS_THROUGH_EXT(1000104013),
    EXTENDED_SRGB_NONLINEAR_EXT(1000104014);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}


enum class VkPresentMode(val i: Int) {
    IMMEDIATE_KHR(0),
    MAILBOX_KHR(1),
    FIFO_KHR(2),
    FIFO_RELAXED_KHR(3),
    SHARED_DEMAND_REFRESH_KHR(1000111000),
    SHARED_CONTINUOUS_REFRESH_KHR(1000111001);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}


enum class VkSurfaceTransform(val i: Int) {
    IDENTITY_BIT_KHR(0x00000001),
    ROTATE_90_BIT_KHR(0x00000002),
    ROTATE_180_BIT_KHR(0x00000004),
    ROTATE_270_BIT_KHR(0x00000008),
    HORIZONTAL_MIRROR_BIT_KHR(0x00000010),
    HORIZONTAL_MIRROR_ROTATE_90_BIT_KHR(0x00000020),
    HORIZONTAL_MIRROR_ROTATE_180_BIT_KHR(0x00000040),
    HORIZONTAL_MIRROR_ROTATE_270_BIT_KHR(0x00000080),
    INHERIT_BIT_KHR(0x00000100);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

typealias VkSurfaceTransformFlagsKHR = VkFlags

typealias VkCompositeAlphaFlagBitsKHR = Int

val VkCompositeAlpha_OPAQUE_BIT_KHR: VkCompositeAlphaFlagBitsKHR = 0x00000001
val VkCompositeAlpha_PRE_MULTIPLIED_BIT_KHR: VkCompositeAlphaFlagBitsKHR = 0x00000002
val VkCompositeAlpha_POST_MULTIPLIED_BIT_KHR: VkCompositeAlphaFlagBitsKHR = 0x00000004
val VkCompositeAlpha_INHERIT_BIT_KHR: VkCompositeAlphaFlagBitsKHR = 0x00000008

typealias VkCompositeAlphaFlagsKHR = VkFlags


inline val VkSurfaceCapabilitiesKHR.minImageCount get() = VkSurfaceCapabilitiesKHR.nminImageCount(adr)
inline val VkSurfaceCapabilitiesKHR.maxImageCount get() = VkSurfaceCapabilitiesKHR.nmaxImageCount(adr)
inline val VkSurfaceCapabilitiesKHR.currentExtent: VkExtent2D get() = VkSurfaceCapabilitiesKHR.ncurrentExtent(adr)
inline val VkSurfaceCapabilitiesKHR.minImageExtent: VkExtent2D get() = VkSurfaceCapabilitiesKHR.nminImageExtent(adr)
inline val VkSurfaceCapabilitiesKHR.maxImageExtent: VkExtent2D get() = VkSurfaceCapabilitiesKHR.nmaxImageExtent(adr)
inline val VkSurfaceCapabilitiesKHR.maxImageArrayLayers get() = VkSurfaceCapabilitiesKHR.nmaxImageArrayLayers(adr)
inline val VkSurfaceCapabilitiesKHR.supportedTransforms: VkSurfaceTransformFlagsKHR get() = VkSurfaceCapabilitiesKHR.nsupportedTransforms(adr)
inline val VkSurfaceCapabilitiesKHR.currentTransform: VkSurfaceTransform get() = VkSurfaceTransform of VkSurfaceCapabilitiesKHR.ncurrentTransform(adr)
inline val VkSurfaceCapabilitiesKHR.supportedCompositeAlpha: VkCompositeAlphaFlagsKHR get() = VkSurfaceCapabilitiesKHR.nsupportedCompositeAlpha(adr)
inline val VkSurfaceCapabilitiesKHR.supportedUsageFlags: VkImageUsageFlags get() = VkSurfaceCapabilitiesKHR.nsupportedUsageFlags(adr)

//typedef struct VkSurfaceCapabilitiesKHR {
//    uint32_t                         minImageCount;
//    uint32_t                         maxImageCount;
//    VkExtent2D                       currentExtent;
//    VkExtent2D                       minImageExtent;
//    VkExtent2D                       maxImageExtent;
//    uint32_t                         maxImageArrayLayers;
//    VkSurfaceTransformFlagsKHR       supportedTransforms;
//    VkSurfaceTransformFlagBitsKHR    currentTransform;
//    VkCompositeAlphaFlagsKHR         supportedCompositeAlpha;
//    VkImageUsageFlags                supportedUsageFlags;
//} VkSurfaceCapabilitiesKHR;

inline val VkSurfaceFormatKHR.format: VkFormat get() = VkFormat of VkSurfaceFormatKHR.nformat(adr)
inline val VkSurfaceFormatKHR.colorSpace: VkColorSpace get() = VkColorSpace of VkSurfaceFormatKHR.ncolorSpace(adr)
//
//typedef void (VKAPI_PTR *PFN_vkDestroySurfaceKHR)(VkInstance instance, VkSurfaceKHR surface, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceSurfaceSupportKHR)(VkPhysicalDevice physicalDevice, uint32_t queueFamilyIndex, VkSurfaceKHR surface, VkBool32* pSupported);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceSurfaceCapabilitiesKHR)(VkPhysicalDevice physicalDevice, VkSurfaceKHR surface, VkSurfaceCapabilitiesKHR* pSurfaceCapabilities);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceSurfaceFormatsKHR)(VkPhysicalDevice physicalDevice, VkSurfaceKHR surface, uint32_t* pSurfaceFormatCount, VkSurfaceFormatKHR* pSurfaceFormats);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceSurfacePresentModesKHR)(VkPhysicalDevice physicalDevice, VkSurfaceKHR surface, uint32_t* pPresentModeCount, VkPresentModeKHR* pPresentModes);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkDestroySurfaceKHR(
//VkInstance                                  instance,
//VkSurfaceKHR                                surface,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceSurfaceSupportKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t                                    queueFamilyIndex,
//VkSurfaceKHR                                surface,
//VkBool32*                                   pSupported);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceSurfaceCapabilitiesKHR(
//VkPhysicalDevice                            physicalDevice,
//VkSurfaceKHR                                surface,
//VkSurfaceCapabilitiesKHR*                   pSurfaceCapabilities);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceSurfaceFormatsKHR(
//VkPhysicalDevice                            physicalDevice,
//VkSurfaceKHR                                surface,
//uint32_t*                                   pSurfaceFormatCount,
//VkSurfaceFormatKHR*                         pSurfaceFormats);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceSurfacePresentModesKHR(
//VkPhysicalDevice                            physicalDevice,
//VkSurfaceKHR                                surface,
//uint32_t*                                   pPresentModeCount,
//VkPresentModeKHR*                           pPresentModes);
//#endif
//
//#define VK_KHR_swapchain 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkSwapchainKHR)
//
//#define VK_KHR_SWAPCHAIN_SPEC_VERSION     68
//#define VK_KHR_SWAPCHAIN_EXTENSION_NAME   "VK_KHR_swapchain"
//
//
typealias VkSwapchainCreateFlagBitsKHR = Int

val VkSwapchainCreate_BIND_SFR_BIT_KHX: VkSwapchainCreateFlagBitsKHR = 0x00000001

typealias VkSwapchainCreateFlagsKHR = VkFlags

//typedef struct VkSwapchainCreateInfoKHR {
//    VkStructureType                  sType;
//    const void*                      pNext;
//    VkSwapchainCreateFlagsKHR        flags;
//    VkSurfaceKHR                     surface;
//    uint32_t                         minImageCount;
//    VkFormat                         imageFormat;
//    VkColorSpaceKHR                  imageColorSpace;
//    VkExtent2D                       imageExtent;
//    uint32_t                         imageArrayLayers;
//    VkImageUsageFlags                imageUsage;
//    VkSharingMode                    imageSharingMode;
//    uint32_t                         queueFamilyIndexCount;
//    const uint32_t*                  pQueueFamilyIndices;
//    VkSurfaceTransformFlagBitsKHR    preTransform;
//    VkCompositeAlphaFlagBitsKHR      compositeAlpha;
//    VkPresentModeKHR                 presentMode;
//    VkBool32                         clipped;
//    VkSwapchainKHR                   oldSwapchain;
//} VkSwapchainCreateInfoKHR;
//
//typedef struct VkPresentInfoKHR {
//    VkStructureType          sType;
//    const void*              pNext;
//    uint32_t                 waitSemaphoreCount;
//    const VkSemaphore*       pWaitSemaphores;
//    uint32_t                 swapchainCount;
//    const VkSwapchainKHR*    pSwapchains;
//    const uint32_t*          pImageIndices;
//    VkResult*                pResults;
//} VkPresentInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateSwapchainKHR)(VkDevice device, const VkSwapchainCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSwapchainKHR* pSwapchain);
//typedef void (VKAPI_PTR *PFN_vkDestroySwapchainKHR)(VkDevice device, VkSwapchainKHR swapchain, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkGetSwapchainImagesKHR)(VkDevice device, VkSwapchainKHR swapchain, uint32_t* pSwapchainImageCount, VkImage* pSwapchainImages);
//typedef VkResult (VKAPI_PTR *PFN_vkAcquireNextImageKHR)(VkDevice device, VkSwapchainKHR swapchain, uint64_t timeout, VkSemaphore semaphore, VkFence fence, uint32_t* pImageIndex);
//typedef VkResult (VKAPI_PTR *PFN_vkQueuePresentKHR)(VkQueue queue, const VkPresentInfoKHR* pPresentInfo);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateSwapchainKHR(
//VkDevice                                    device,
//const VkSwapchainCreateInfoKHR*             pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSwapchainKHR*                             pSwapchain);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroySwapchainKHR(
//VkDevice                                    device,
//VkSwapchainKHR                              swapchain,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetSwapchainImagesKHR(
//VkDevice                                    device,
//VkSwapchainKHR                              swapchain,
//uint32_t*                                   pSwapchainImageCount,
//VkImage*                                    pSwapchainImages);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkAcquireNextImageKHR(
//VkDevice                                    device,
//VkSwapchainKHR                              swapchain,
//uint64_t                                    timeout,
//VkSemaphore                                 semaphore,
//VkFence                                     fence,
//uint32_t*                                   pImageIndex);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkQueuePresentKHR(
//VkQueue                                     queue,
//const VkPresentInfoKHR*                     pPresentInfo);
//#endif
//
//#define VK_KHR_display 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkDisplayKHR)
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkDisplayModeKHR)
//
//#define VK_KHR_DISPLAY_SPEC_VERSION       21
//#define VK_KHR_DISPLAY_EXTENSION_NAME     "VK_KHR_display"
//
//
//typedef enum VkDisplayPlaneAlphaFlagBitsKHR {
//    VK_DISPLAY_PLANE_ALPHA_OPAQUE_BIT_KHR = 0x00000001,
//    VK_DISPLAY_PLANE_ALPHA_GLOBAL_BIT_KHR = 0x00000002,
//    VK_DISPLAY_PLANE_ALPHA_PER_PIXEL_BIT_KHR = 0x00000004,
//    VK_DISPLAY_PLANE_ALPHA_PER_PIXEL_PREMULTIPLIED_BIT_KHR = 0x00000008,
//    VK_DISPLAY_PLANE_ALPHA_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkDisplayPlaneAlphaFlagBitsKHR;
//typedef VkFlags VkDisplayPlaneAlphaFlagsKHR;
//typedef VkFlags VkDisplayModeCreateFlagsKHR;
//typedef VkFlags VkDisplaySurfaceCreateFlagsKHR;
//
//typedef struct VkDisplayPropertiesKHR {
//    VkDisplayKHR                  display;
//    const char*                   displayName;
//    VkExtent2D                    physicalDimensions;
//    VkExtent2D                    physicalResolution;
//    VkSurfaceTransformFlagsKHR    supportedTransforms;
//    VkBool32                      planeReorderPossible;
//    VkBool32                      persistentContent;
//} VkDisplayPropertiesKHR;
//
//typedef struct VkDisplayModeParametersKHR {
//    VkExtent2D    visibleRegion;
//    uint32_t      refreshRate;
//} VkDisplayModeParametersKHR;
//
//typedef struct VkDisplayModePropertiesKHR {
//    VkDisplayModeKHR              displayMode;
//    VkDisplayModeParametersKHR    parameters;
//} VkDisplayModePropertiesKHR;
//
//typedef struct VkDisplayModeCreateInfoKHR {
//    VkStructureType                sType;
//    const void*                    pNext;
//    VkDisplayModeCreateFlagsKHR    flags;
//    VkDisplayModeParametersKHR     parameters;
//} VkDisplayModeCreateInfoKHR;
//
//typedef struct VkDisplayPlaneCapabilitiesKHR {
//    VkDisplayPlaneAlphaFlagsKHR    supportedAlpha;
//    VkOffset2D                     minSrcPosition;
//    VkOffset2D                     maxSrcPosition;
//    VkExtent2D                     minSrcExtent;
//    VkExtent2D                     maxSrcExtent;
//    VkOffset2D                     minDstPosition;
//    VkOffset2D                     maxDstPosition;
//    VkExtent2D                     minDstExtent;
//    VkExtent2D                     maxDstExtent;
//} VkDisplayPlaneCapabilitiesKHR;
//
//typedef struct VkDisplayPlanePropertiesKHR {
//    VkDisplayKHR    currentDisplay;
//    uint32_t        currentStackIndex;
//} VkDisplayPlanePropertiesKHR;
//
//typedef struct VkDisplaySurfaceCreateInfoKHR {
//    VkStructureType                   sType;
//    const void*                       pNext;
//    VkDisplaySurfaceCreateFlagsKHR    flags;
//    VkDisplayModeKHR                  displayMode;
//    uint32_t                          planeIndex;
//    uint32_t                          planeStackIndex;
//    VkSurfaceTransformFlagBitsKHR     transform;
//    float                             globalAlpha;
//    VkDisplayPlaneAlphaFlagBitsKHR    alphaMode;
//    VkExtent2D                        imageExtent;
//} VkDisplaySurfaceCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceDisplayPropertiesKHR)(VkPhysicalDevice physicalDevice, uint32_t* pPropertyCount, VkDisplayPropertiesKHR* pProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceDisplayPlanePropertiesKHR)(VkPhysicalDevice physicalDevice, uint32_t* pPropertyCount, VkDisplayPlanePropertiesKHR* pProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkGetDisplayPlaneSupportedDisplaysKHR)(VkPhysicalDevice physicalDevice, uint32_t planeIndex, uint32_t* pDisplayCount, VkDisplayKHR* pDisplays);
//typedef VkResult (VKAPI_PTR *PFN_vkGetDisplayModePropertiesKHR)(VkPhysicalDevice physicalDevice, VkDisplayKHR display, uint32_t* pPropertyCount, VkDisplayModePropertiesKHR* pProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateDisplayModeKHR)(VkPhysicalDevice physicalDevice, VkDisplayKHR display, const VkDisplayModeCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkDisplayModeKHR* pMode);
//typedef VkResult (VKAPI_PTR *PFN_vkGetDisplayPlaneCapabilitiesKHR)(VkPhysicalDevice physicalDevice, VkDisplayModeKHR mode, uint32_t planeIndex, VkDisplayPlaneCapabilitiesKHR* pCapabilities);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateDisplayPlaneSurfaceKHR)(VkInstance instance, const VkDisplaySurfaceCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceDisplayPropertiesKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t*                                   pPropertyCount,
//VkDisplayPropertiesKHR*                     pProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceDisplayPlanePropertiesKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t*                                   pPropertyCount,
//VkDisplayPlanePropertiesKHR*                pProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetDisplayPlaneSupportedDisplaysKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t                                    planeIndex,
//uint32_t*                                   pDisplayCount,
//VkDisplayKHR*                               pDisplays);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetDisplayModePropertiesKHR(
//VkPhysicalDevice                            physicalDevice,
//VkDisplayKHR                                display,
//uint32_t*                                   pPropertyCount,
//VkDisplayModePropertiesKHR*                 pProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateDisplayModeKHR(
//VkPhysicalDevice                            physicalDevice,
//VkDisplayKHR                                display,
//const VkDisplayModeCreateInfoKHR*           pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkDisplayModeKHR*                           pMode);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetDisplayPlaneCapabilitiesKHR(
//VkPhysicalDevice                            physicalDevice,
//VkDisplayModeKHR                            mode,
//uint32_t                                    planeIndex,
//VkDisplayPlaneCapabilitiesKHR*              pCapabilities);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateDisplayPlaneSurfaceKHR(
//VkInstance                                  instance,
//const VkDisplaySurfaceCreateInfoKHR*        pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//#endif
//
//#define VK_KHR_display_swapchain 1
//#define VK_KHR_DISPLAY_SWAPCHAIN_SPEC_VERSION 9
//#define VK_KHR_DISPLAY_SWAPCHAIN_EXTENSION_NAME "VK_KHR_display_swapchain"
//
//typedef struct VkDisplayPresentInfoKHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkRect2D           srcRect;
//    VkRect2D           dstRect;
//    VkBool32           persistent;
//} VkDisplayPresentInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateSharedSwapchainsKHR)(VkDevice device, uint32_t swapchainCount, const VkSwapchainCreateInfoKHR* pCreateInfos, const VkAllocationCallbacks* pAllocator, VkSwapchainKHR* pSwapchains);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateSharedSwapchainsKHR(
//VkDevice                                    device,
//uint32_t                                    swapchainCount,
//const VkSwapchainCreateInfoKHR*             pCreateInfos,
//const VkAllocationCallbacks*                pAllocator,
//VkSwapchainKHR*                             pSwapchains);
//#endif
//
//#ifdef VK_USE_PLATFORM_XLIB_KHR
//#define VK_KHR_xlib_surface 1
//#include <X11/Xlib.h>
//
//#define VK_KHR_XLIB_SURFACE_SPEC_VERSION  6
//#define VK_KHR_XLIB_SURFACE_EXTENSION_NAME "VK_KHR_xlib_surface"
//
//typedef VkFlags VkXlibSurfaceCreateFlagsKHR;
//
//typedef struct VkXlibSurfaceCreateInfoKHR {
//    VkStructureType                sType;
//    const void*                    pNext;
//    VkXlibSurfaceCreateFlagsKHR    flags;
//    Display*                       dpy;
//    Window                         window;
//} VkXlibSurfaceCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateXlibSurfaceKHR)(VkInstance instance, const VkXlibSurfaceCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//typedef VkBool32 (VKAPI_PTR *PFN_vkGetPhysicalDeviceXlibPresentationSupportKHR)(VkPhysicalDevice physicalDevice, uint32_t queueFamilyIndex, Display* dpy, VisualID visualID);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateXlibSurfaceKHR(
//VkInstance                                  instance,
//const VkXlibSurfaceCreateInfoKHR*           pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//
//VKAPI_ATTR VkBool32 VKAPI_CALL vkGetPhysicalDeviceXlibPresentationSupportKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t                                    queueFamilyIndex,
//Display*                                    dpy,
//VisualID                                    visualID);
//#endif
//#endif /* VK_USE_PLATFORM_XLIB_KHR */
//
//#ifdef VK_USE_PLATFORM_XCB_KHR
//#define VK_KHR_xcb_surface 1
//#include <xcb/xcb.h>
//
//#define VK_KHR_XCB_SURFACE_SPEC_VERSION   6
//#define VK_KHR_XCB_SURFACE_EXTENSION_NAME "VK_KHR_xcb_surface"
//
//typedef VkFlags VkXcbSurfaceCreateFlagsKHR;
//
//typedef struct VkXcbSurfaceCreateInfoKHR {
//    VkStructureType               sType;
//    const void*                   pNext;
//    VkXcbSurfaceCreateFlagsKHR    flags;
//    xcb_connection_t*             connection;
//    xcb_window_t                  window;
//} VkXcbSurfaceCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateXcbSurfaceKHR)(VkInstance instance, const VkXcbSurfaceCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//typedef VkBool32 (VKAPI_PTR *PFN_vkGetPhysicalDeviceXcbPresentationSupportKHR)(VkPhysicalDevice physicalDevice, uint32_t queueFamilyIndex, xcb_connection_t* connection, xcb_visualid_t visual_id);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateXcbSurfaceKHR(
//VkInstance                                  instance,
//const VkXcbSurfaceCreateInfoKHR*            pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//
//VKAPI_ATTR VkBool32 VKAPI_CALL vkGetPhysicalDeviceXcbPresentationSupportKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t                                    queueFamilyIndex,
//xcb_connection_t*                           connection,
//xcb_visualid_t                              visual_id);
//#endif
//#endif /* VK_USE_PLATFORM_XCB_KHR */
//
//#ifdef VK_USE_PLATFORM_WAYLAND_KHR
//#define VK_KHR_wayland_surface 1
//#include <wayland-client.h>
//
//#define VK_KHR_WAYLAND_SURFACE_SPEC_VERSION 6
//#define VK_KHR_WAYLAND_SURFACE_EXTENSION_NAME "VK_KHR_wayland_surface"
//
//typedef VkFlags VkWaylandSurfaceCreateFlagsKHR;
//
//typedef struct VkWaylandSurfaceCreateInfoKHR {
//    VkStructureType                   sType;
//    const void*                       pNext;
//    VkWaylandSurfaceCreateFlagsKHR    flags;
//    struct wl_display*                display;
//    struct wl_surface*                surface;
//} VkWaylandSurfaceCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateWaylandSurfaceKHR)(VkInstance instance, const VkWaylandSurfaceCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//typedef VkBool32 (VKAPI_PTR *PFN_vkGetPhysicalDeviceWaylandPresentationSupportKHR)(VkPhysicalDevice physicalDevice, uint32_t queueFamilyIndex, struct wl_display* display);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateWaylandSurfaceKHR(
//VkInstance                                  instance,
//const VkWaylandSurfaceCreateInfoKHR*        pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//
//VKAPI_ATTR VkBool32 VKAPI_CALL vkGetPhysicalDeviceWaylandPresentationSupportKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t                                    queueFamilyIndex,
//struct wl_display*                          display);
//#endif
//#endif /* VK_USE_PLATFORM_WAYLAND_KHR */
//
//#ifdef VK_USE_PLATFORM_MIR_KHR
//#define VK_KHR_mir_surface 1
//#include <mir_toolkit/client_types.h>
//
//#define VK_KHR_MIR_SURFACE_SPEC_VERSION   4
//#define VK_KHR_MIR_SURFACE_EXTENSION_NAME "VK_KHR_mir_surface"
//
//typedef VkFlags VkMirSurfaceCreateFlagsKHR;
//
//typedef struct VkMirSurfaceCreateInfoKHR {
//    VkStructureType               sType;
//    const void*                   pNext;
//    VkMirSurfaceCreateFlagsKHR    flags;
//    MirConnection*                connection;
//    MirSurface*                   mirSurface;
//} VkMirSurfaceCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateMirSurfaceKHR)(VkInstance instance, const VkMirSurfaceCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//typedef VkBool32 (VKAPI_PTR *PFN_vkGetPhysicalDeviceMirPresentationSupportKHR)(VkPhysicalDevice physicalDevice, uint32_t queueFamilyIndex, MirConnection* connection);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateMirSurfaceKHR(
//VkInstance                                  instance,
//const VkMirSurfaceCreateInfoKHR*            pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//
//VKAPI_ATTR VkBool32 VKAPI_CALL vkGetPhysicalDeviceMirPresentationSupportKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t                                    queueFamilyIndex,
//MirConnection*                              connection);
//#endif
//#endif /* VK_USE_PLATFORM_MIR_KHR */
//
//#ifdef VK_USE_PLATFORM_ANDROID_KHR
//#define VK_KHR_android_surface 1
//#include <android/native_window.h>
//
//#define VK_KHR_ANDROID_SURFACE_SPEC_VERSION 6
//#define VK_KHR_ANDROID_SURFACE_EXTENSION_NAME "VK_KHR_android_surface"
//
//typedef VkFlags VkAndroidSurfaceCreateFlagsKHR;
//
//typedef struct VkAndroidSurfaceCreateInfoKHR {
//    VkStructureType                   sType;
//    const void*                       pNext;
//    VkAndroidSurfaceCreateFlagsKHR    flags;
//    ANativeWindow*                    window;
//} VkAndroidSurfaceCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateAndroidSurfaceKHR)(VkInstance instance, const VkAndroidSurfaceCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateAndroidSurfaceKHR(
//VkInstance                                  instance,
//const VkAndroidSurfaceCreateInfoKHR*        pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//#endif
//#endif /* VK_USE_PLATFORM_ANDROID_KHR */
//
//#ifdef VK_USE_PLATFORM_WIN32_KHR
//#define VK_KHR_win32_surface 1
//#include <windows.h>
//
//#define VK_KHR_WIN32_SURFACE_SPEC_VERSION 6
//#define VK_KHR_WIN32_SURFACE_EXTENSION_NAME "VK_KHR_win32_surface"
//
//typedef VkFlags VkWin32SurfaceCreateFlagsKHR;
//
//typedef struct VkWin32SurfaceCreateInfoKHR {
//    VkStructureType                 sType;
//    const void*                     pNext;
//    VkWin32SurfaceCreateFlagsKHR    flags;
//    HINSTANCE                       hinstance;
//    HWND                            hwnd;
//} VkWin32SurfaceCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateWin32SurfaceKHR)(VkInstance instance, const VkWin32SurfaceCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//typedef VkBool32 (VKAPI_PTR *PFN_vkGetPhysicalDeviceWin32PresentationSupportKHR)(VkPhysicalDevice physicalDevice, uint32_t queueFamilyIndex);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateWin32SurfaceKHR(
//VkInstance                                  instance,
//const VkWin32SurfaceCreateInfoKHR*          pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//
//VKAPI_ATTR VkBool32 VKAPI_CALL vkGetPhysicalDeviceWin32PresentationSupportKHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t                                    queueFamilyIndex);
//#endif
//#endif /* VK_USE_PLATFORM_WIN32_KHR */
//
//#define VK_KHR_sampler_mirror_clamp_to_edge 1
//#define VK_KHR_SAMPLER_MIRROR_CLAMP_TO_EDGE_SPEC_VERSION 1
//#define VK_KHR_SAMPLER_MIRROR_CLAMP_TO_EDGE_EXTENSION_NAME "VK_KHR_sampler_mirror_clamp_to_edge"
//
//
//#define VK_KHR_get_physical_device_properties2 1
//#define VK_KHR_GET_PHYSICAL_DEVICE_PROPERTIES_2_SPEC_VERSION 1
//#define VK_KHR_GET_PHYSICAL_DEVICE_PROPERTIES_2_EXTENSION_NAME "VK_KHR_get_physical_device_properties2"
//
//typedef struct VkPhysicalDeviceFeatures2KHR {
//    VkStructureType             sType;
//    void*                       pNext;
//    VkPhysicalDeviceFeatures    features;
//} VkPhysicalDeviceFeatures2KHR;
//
//typedef struct VkPhysicalDeviceProperties2KHR {
//    VkStructureType               sType;
//    void*                         pNext;
//    VkPhysicalDeviceProperties    properties;
//} VkPhysicalDeviceProperties2KHR;
//
//typedef struct VkFormatProperties2KHR {
//    VkStructureType       sType;
//    void*                 pNext;
//    VkFormatProperties    formatProperties;
//} VkFormatProperties2KHR;
//
//typedef struct VkImageFormatProperties2KHR {
//    VkStructureType            sType;
//    void*                      pNext;
//    VkImageFormatProperties    imageFormatProperties;
//} VkImageFormatProperties2KHR;
//
//typedef struct VkPhysicalDeviceImageFormatInfo2KHR {
//    VkStructureType       sType;
//    const void*           pNext;
//    VkFormat              format;
//    VkImageType           type;
//    VkImageTiling         tiling;
//    VkImageUsageFlags     usage;
//    VkImageCreateFlags    flags;
//} VkPhysicalDeviceImageFormatInfo2KHR;
//
//typedef struct VkQueueFamilyProperties2KHR {
//    VkStructureType            sType;
//    void*                      pNext;
//    VkQueueFamilyProperties    queueFamilyProperties;
//} VkQueueFamilyProperties2KHR;
//
//typedef struct VkPhysicalDeviceMemoryProperties2KHR {
//    VkStructureType                     sType;
//    void*                               pNext;
//    VkPhysicalDeviceMemoryProperties    memoryProperties;
//} VkPhysicalDeviceMemoryProperties2KHR;
//
//typedef struct VkSparseImageFormatProperties2KHR {
//    VkStructureType                  sType;
//    void*                            pNext;
//    VkSparseImageFormatProperties    properties;
//} VkSparseImageFormatProperties2KHR;
//
//typedef struct VkPhysicalDeviceSparseImageFormatInfo2KHR {
//    VkStructureType          sType;
//    const void*              pNext;
//    VkFormat                 format;
//    VkImageType              type;
//    VkSampleCountFlagBits    samples;
//    VkImageUsageFlags        usage;
//    VkImageTiling            tiling;
//} VkPhysicalDeviceSparseImageFormatInfo2KHR;
//
//
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceFeatures2KHR)(VkPhysicalDevice physicalDevice, VkPhysicalDeviceFeatures2KHR* pFeatures);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceProperties2KHR)(VkPhysicalDevice physicalDevice, VkPhysicalDeviceProperties2KHR* pProperties);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceFormatProperties2KHR)(VkPhysicalDevice physicalDevice, VkFormat format, VkFormatProperties2KHR* pFormatProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceImageFormatProperties2KHR)(VkPhysicalDevice physicalDevice, const VkPhysicalDeviceImageFormatInfo2KHR* pImageFormatInfo, VkImageFormatProperties2KHR* pImageFormatProperties);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceQueueFamilyProperties2KHR)(VkPhysicalDevice physicalDevice, uint32_t* pQueueFamilyPropertyCount, VkQueueFamilyProperties2KHR* pQueueFamilyProperties);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceMemoryProperties2KHR)(VkPhysicalDevice physicalDevice, VkPhysicalDeviceMemoryProperties2KHR* pMemoryProperties);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceSparseImageFormatProperties2KHR)(VkPhysicalDevice physicalDevice, const VkPhysicalDeviceSparseImageFormatInfo2KHR* pFormatInfo, uint32_t* pPropertyCount, VkSparseImageFormatProperties2KHR* pProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceFeatures2KHR(
//VkPhysicalDevice                            physicalDevice,
//VkPhysicalDeviceFeatures2KHR*               pFeatures);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceProperties2KHR(
//VkPhysicalDevice                            physicalDevice,
//VkPhysicalDeviceProperties2KHR*             pProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceFormatProperties2KHR(
//VkPhysicalDevice                            physicalDevice,
//VkFormat                                    format,
//VkFormatProperties2KHR*                     pFormatProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceImageFormatProperties2KHR(
//VkPhysicalDevice                            physicalDevice,
//const VkPhysicalDeviceImageFormatInfo2KHR*  pImageFormatInfo,
//VkImageFormatProperties2KHR*                pImageFormatProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceQueueFamilyProperties2KHR(
//VkPhysicalDevice                            physicalDevice,
//uint32_t*                                   pQueueFamilyPropertyCount,
//VkQueueFamilyProperties2KHR*                pQueueFamilyProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceMemoryProperties2KHR(
//VkPhysicalDevice                            physicalDevice,
//VkPhysicalDeviceMemoryProperties2KHR*       pMemoryProperties);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceSparseImageFormatProperties2KHR(
//VkPhysicalDevice                            physicalDevice,
//const VkPhysicalDeviceSparseImageFormatInfo2KHR* pFormatInfo,
//uint32_t*                                   pPropertyCount,
//VkSparseImageFormatProperties2KHR*          pProperties);
//#endif
//
//#define VK_KHR_shader_draw_parameters 1
//#define VK_KHR_SHADER_DRAW_PARAMETERS_SPEC_VERSION 1
//#define VK_KHR_SHADER_DRAW_PARAMETERS_EXTENSION_NAME "VK_KHR_shader_draw_parameters"
//
//
//#define VK_KHR_maintenance1 1
//#define VK_KHR_MAINTENANCE1_SPEC_VERSION  1
//#define VK_KHR_MAINTENANCE1_EXTENSION_NAME "VK_KHR_maintenance1"
//
//typedef VkFlags VkCommandPoolTrimFlagsKHR;
//
//typedef void (VKAPI_PTR *PFN_vkTrimCommandPoolKHR)(VkDevice device, VkCommandPool commandPool, VkCommandPoolTrimFlagsKHR flags);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkTrimCommandPoolKHR(
//VkDevice                                    device,
//VkCommandPool                               commandPool,
//VkCommandPoolTrimFlagsKHR                   flags);
//#endif
//
//#define VK_KHR_external_memory_capabilities 1
//#define VK_LUID_SIZE_KHR                  8
//#define VK_KHR_EXTERNAL_MEMORY_CAPABILITIES_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_MEMORY_CAPABILITIES_EXTENSION_NAME "VK_KHR_external_memory_capabilities"
//
//
//typedef enum VkExternalMemoryHandleTypeFlagBitsKHR {
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_FD_BIT_KHR = 0x00000001,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_WIN32_BIT_KHR = 0x00000002,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_WIN32_KMT_BIT_KHR = 0x00000004,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D11_TEXTURE_BIT_KHR = 0x00000008,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D11_TEXTURE_KMT_BIT_KHR = 0x00000010,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D12_HEAP_BIT_KHR = 0x00000020,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D12_RESOURCE_BIT_KHR = 0x00000040,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_DMA_BUF_BIT_EXT = 0x00000200,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_HOST_ALLOCATION_BIT_EXT = 0x00000080,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_HOST_MAPPED_FOREIGN_MEMORY_BIT_EXT = 0x00000100,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkExternalMemoryHandleTypeFlagBitsKHR;
//typedef VkFlags VkExternalMemoryHandleTypeFlagsKHR;
//
//typedef enum VkExternalMemoryFeatureFlagBitsKHR {
//    VK_EXTERNAL_MEMORY_FEATURE_DEDICATED_ONLY_BIT_KHR = 0x00000001,
//    VK_EXTERNAL_MEMORY_FEATURE_EXPORTABLE_BIT_KHR = 0x00000002,
//    VK_EXTERNAL_MEMORY_FEATURE_IMPORTABLE_BIT_KHR = 0x00000004,
//    VK_EXTERNAL_MEMORY_FEATURE_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkExternalMemoryFeatureFlagBitsKHR;
//typedef VkFlags VkExternalMemoryFeatureFlagsKHR;
//
//typedef struct VkExternalMemoryPropertiesKHR {
//    VkExternalMemoryFeatureFlagsKHR       externalMemoryFeatures;
//    VkExternalMemoryHandleTypeFlagsKHR    exportFromImportedHandleTypes;
//    VkExternalMemoryHandleTypeFlagsKHR    compatibleHandleTypes;
//} VkExternalMemoryPropertiesKHR;
//
//typedef struct VkPhysicalDeviceExternalImageFormatInfoKHR {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkExternalMemoryHandleTypeFlagBitsKHR    handleType;
//} VkPhysicalDeviceExternalImageFormatInfoKHR;
//
//typedef struct VkExternalImageFormatPropertiesKHR {
//    VkStructureType                  sType;
//    void*                            pNext;
//    VkExternalMemoryPropertiesKHR    externalMemoryProperties;
//} VkExternalImageFormatPropertiesKHR;
//
//typedef struct VkPhysicalDeviceExternalBufferInfoKHR {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkBufferCreateFlags                      flags;
//    VkBufferUsageFlags                       usage;
//    VkExternalMemoryHandleTypeFlagBitsKHR    handleType;
//} VkPhysicalDeviceExternalBufferInfoKHR;
//
//typedef struct VkExternalBufferPropertiesKHR {
//    VkStructureType                  sType;
//    void*                            pNext;
//    VkExternalMemoryPropertiesKHR    externalMemoryProperties;
//} VkExternalBufferPropertiesKHR;
//
//typedef struct VkPhysicalDeviceIDPropertiesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    uint8_t            deviceUUID[VK_UUID_SIZE];
//    uint8_t            driverUUID[VK_UUID_SIZE];
//    uint8_t            deviceLUID[VK_LUID_SIZE_KHR];
//    uint32_t           deviceNodeMask;
//    VkBool32           deviceLUIDValid;
//} VkPhysicalDeviceIDPropertiesKHR;
//
//
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceExternalBufferPropertiesKHR)(VkPhysicalDevice physicalDevice, const VkPhysicalDeviceExternalBufferInfoKHR* pExternalBufferInfo, VkExternalBufferPropertiesKHR* pExternalBufferProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceExternalBufferPropertiesKHR(
//VkPhysicalDevice                            physicalDevice,
//const VkPhysicalDeviceExternalBufferInfoKHR* pExternalBufferInfo,
//VkExternalBufferPropertiesKHR*              pExternalBufferProperties);
//#endif
//
//#define VK_KHR_external_memory 1
//#define VK_KHR_EXTERNAL_MEMORY_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_MEMORY_EXTENSION_NAME "VK_KHR_external_memory"
//#define VK_QUEUE_FAMILY_EXTERNAL_KHR      (~0U-1)
//
//typedef struct VkExternalMemoryImageCreateInfoKHR {
//    VkStructureType                       sType;
//    const void*                           pNext;
//    VkExternalMemoryHandleTypeFlagsKHR    handleTypes;
//} VkExternalMemoryImageCreateInfoKHR;
//
//typedef struct VkExternalMemoryBufferCreateInfoKHR {
//    VkStructureType                       sType;
//    const void*                           pNext;
//    VkExternalMemoryHandleTypeFlagsKHR    handleTypes;
//} VkExternalMemoryBufferCreateInfoKHR;
//
//typedef struct VkExportMemoryAllocateInfoKHR {
//    VkStructureType                       sType;
//    const void*                           pNext;
//    VkExternalMemoryHandleTypeFlagsKHR    handleTypes;
//} VkExportMemoryAllocateInfoKHR;
//
//
//
//#ifdef VK_USE_PLATFORM_WIN32_KHR
//#define VK_KHR_external_memory_win32 1
//#define VK_KHR_EXTERNAL_MEMORY_WIN32_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_MEMORY_WIN32_EXTENSION_NAME "VK_KHR_external_memory_win32"
//
//typedef struct VkImportMemoryWin32HandleInfoKHR {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkExternalMemoryHandleTypeFlagBitsKHR    handleType;
//    HANDLE                                   handle;
//    LPCWSTR                                  name;
//} VkImportMemoryWin32HandleInfoKHR;
//
//typedef struct VkExportMemoryWin32HandleInfoKHR {
//    VkStructureType               sType;
//    const void*                   pNext;
//    const SECURITY_ATTRIBUTES*    pAttributes;
//    DWORD                         dwAccess;
//    LPCWSTR                       name;
//} VkExportMemoryWin32HandleInfoKHR;
//
//typedef struct VkMemoryWin32HandlePropertiesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           memoryTypeBits;
//} VkMemoryWin32HandlePropertiesKHR;
//
//typedef struct VkMemoryGetWin32HandleInfoKHR {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkDeviceMemory                           memory;
//    VkExternalMemoryHandleTypeFlagBitsKHR    handleType;
//} VkMemoryGetWin32HandleInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetMemoryWin32HandleKHR)(VkDevice device, const VkMemoryGetWin32HandleInfoKHR* pGetWin32HandleInfo, HANDLE* pHandle);
//typedef VkResult (VKAPI_PTR *PFN_vkGetMemoryWin32HandlePropertiesKHR)(VkDevice device, VkExternalMemoryHandleTypeFlagBitsKHR handleType, HANDLE handle, VkMemoryWin32HandlePropertiesKHR* pMemoryWin32HandleProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetMemoryWin32HandleKHR(
//VkDevice                                    device,
//const VkMemoryGetWin32HandleInfoKHR*        pGetWin32HandleInfo,
//HANDLE*                                     pHandle);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetMemoryWin32HandlePropertiesKHR(
//VkDevice                                    device,
//VkExternalMemoryHandleTypeFlagBitsKHR       handleType,
//HANDLE                                      handle,
//VkMemoryWin32HandlePropertiesKHR*           pMemoryWin32HandleProperties);
//#endif
//#endif /* VK_USE_PLATFORM_WIN32_KHR */
//
//#define VK_KHR_external_memory_fd 1
//#define VK_KHR_EXTERNAL_MEMORY_FD_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_MEMORY_FD_EXTENSION_NAME "VK_KHR_external_memory_fd"
//
//typedef struct VkImportMemoryFdInfoKHR {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkExternalMemoryHandleTypeFlagBitsKHR    handleType;
//    int                                      fd;
//} VkImportMemoryFdInfoKHR;
//
//typedef struct VkMemoryFdPropertiesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           memoryTypeBits;
//} VkMemoryFdPropertiesKHR;
//
//typedef struct VkMemoryGetFdInfoKHR {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkDeviceMemory                           memory;
//    VkExternalMemoryHandleTypeFlagBitsKHR    handleType;
//} VkMemoryGetFdInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetMemoryFdKHR)(VkDevice device, const VkMemoryGetFdInfoKHR* pGetFdInfo, int* pFd);
//typedef VkResult (VKAPI_PTR *PFN_vkGetMemoryFdPropertiesKHR)(VkDevice device, VkExternalMemoryHandleTypeFlagBitsKHR handleType, int fd, VkMemoryFdPropertiesKHR* pMemoryFdProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetMemoryFdKHR(
//VkDevice                                    device,
//const VkMemoryGetFdInfoKHR*                 pGetFdInfo,
//int*                                        pFd);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetMemoryFdPropertiesKHR(
//VkDevice                                    device,
//VkExternalMemoryHandleTypeFlagBitsKHR       handleType,
//int                                         fd,
//VkMemoryFdPropertiesKHR*                    pMemoryFdProperties);
//#endif
//
//#ifdef VK_USE_PLATFORM_WIN32_KHR
//#define VK_KHR_win32_keyed_mutex 1
//#define VK_KHR_WIN32_KEYED_MUTEX_SPEC_VERSION 1
//#define VK_KHR_WIN32_KEYED_MUTEX_EXTENSION_NAME "VK_KHR_win32_keyed_mutex"
//
//typedef struct VkWin32KeyedMutexAcquireReleaseInfoKHR {
//    VkStructureType          sType;
//    const void*              pNext;
//    uint32_t                 acquireCount;
//    const VkDeviceMemory*    pAcquireSyncs;
//    const uint64_t*          pAcquireKeys;
//    const uint32_t*          pAcquireTimeouts;
//    uint32_t                 releaseCount;
//    const VkDeviceMemory*    pReleaseSyncs;
//    const uint64_t*          pReleaseKeys;
//} VkWin32KeyedMutexAcquireReleaseInfoKHR;
//
//
//#endif /* VK_USE_PLATFORM_WIN32_KHR */
//
//#define VK_KHR_external_semaphore_capabilities 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_CAPABILITIES_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_CAPABILITIES_EXTENSION_NAME "VK_KHR_external_semaphore_capabilities"
//
//
//typedef enum VkExternalSemaphoreHandleTypeFlagBitsKHR {
//    VK_EXTERNAL_SEMAPHORE_HANDLE_TYPE_OPAQUE_FD_BIT_KHR = 0x00000001,
//    VK_EXTERNAL_SEMAPHORE_HANDLE_TYPE_OPAQUE_WIN32_BIT_KHR = 0x00000002,
//    VK_EXTERNAL_SEMAPHORE_HANDLE_TYPE_OPAQUE_WIN32_KMT_BIT_KHR = 0x00000004,
//    VK_EXTERNAL_SEMAPHORE_HANDLE_TYPE_D3D12_FENCE_BIT_KHR = 0x00000008,
//    VK_EXTERNAL_SEMAPHORE_HANDLE_TYPE_SYNC_FD_BIT_KHR = 0x00000010,
//    VK_EXTERNAL_SEMAPHORE_HANDLE_TYPE_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkExternalSemaphoreHandleTypeFlagBitsKHR;
//typedef VkFlags VkExternalSemaphoreHandleTypeFlagsKHR;
//
//typedef enum VkExternalSemaphoreFeatureFlagBitsKHR {
//    VK_EXTERNAL_SEMAPHORE_FEATURE_EXPORTABLE_BIT_KHR = 0x00000001,
//    VK_EXTERNAL_SEMAPHORE_FEATURE_IMPORTABLE_BIT_KHR = 0x00000002,
//    VK_EXTERNAL_SEMAPHORE_FEATURE_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkExternalSemaphoreFeatureFlagBitsKHR;
//typedef VkFlags VkExternalSemaphoreFeatureFlagsKHR;
//
//typedef struct VkPhysicalDeviceExternalSemaphoreInfoKHR {
//    VkStructureType                             sType;
//    const void*                                 pNext;
//    VkExternalSemaphoreHandleTypeFlagBitsKHR    handleType;
//} VkPhysicalDeviceExternalSemaphoreInfoKHR;
//
//typedef struct VkExternalSemaphorePropertiesKHR {
//    VkStructureType                          sType;
//    void*                                    pNext;
//    VkExternalSemaphoreHandleTypeFlagsKHR    exportFromImportedHandleTypes;
//    VkExternalSemaphoreHandleTypeFlagsKHR    compatibleHandleTypes;
//    VkExternalSemaphoreFeatureFlagsKHR       externalSemaphoreFeatures;
//} VkExternalSemaphorePropertiesKHR;
//
//
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceExternalSemaphorePropertiesKHR)(VkPhysicalDevice physicalDevice, const VkPhysicalDeviceExternalSemaphoreInfoKHR* pExternalSemaphoreInfo, VkExternalSemaphorePropertiesKHR* pExternalSemaphoreProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceExternalSemaphorePropertiesKHR(
//VkPhysicalDevice                            physicalDevice,
//const VkPhysicalDeviceExternalSemaphoreInfoKHR* pExternalSemaphoreInfo,
//VkExternalSemaphorePropertiesKHR*           pExternalSemaphoreProperties);
//#endif
//
//#define VK_KHR_external_semaphore 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_EXTENSION_NAME "VK_KHR_external_semaphore"
//
//
//typedef enum VkSemaphoreImportFlagBitsKHR {
//    VK_SEMAPHORE_IMPORT_TEMPORARY_BIT_KHR = 0x00000001,
//    VK_SEMAPHORE_IMPORT_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkSemaphoreImportFlagBitsKHR;
//typedef VkFlags VkSemaphoreImportFlagsKHR;
//
//typedef struct VkExportSemaphoreCreateInfoKHR {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkExternalSemaphoreHandleTypeFlagsKHR    handleTypes;
//} VkExportSemaphoreCreateInfoKHR;
//
//
//
//#ifdef VK_USE_PLATFORM_WIN32_KHR
//#define VK_KHR_external_semaphore_win32 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_WIN32_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_WIN32_EXTENSION_NAME "VK_KHR_external_semaphore_win32"
//
//typedef struct VkImportSemaphoreWin32HandleInfoKHR {
//    VkStructureType                             sType;
//    const void*                                 pNext;
//    VkSemaphore                                 semaphore;
//    VkSemaphoreImportFlagsKHR                   flags;
//    VkExternalSemaphoreHandleTypeFlagBitsKHR    handleType;
//    HANDLE                                      handle;
//    LPCWSTR                                     name;
//} VkImportSemaphoreWin32HandleInfoKHR;
//
//typedef struct VkExportSemaphoreWin32HandleInfoKHR {
//    VkStructureType               sType;
//    const void*                   pNext;
//    const SECURITY_ATTRIBUTES*    pAttributes;
//    DWORD                         dwAccess;
//    LPCWSTR                       name;
//} VkExportSemaphoreWin32HandleInfoKHR;
//
//typedef struct VkD3D12FenceSubmitInfoKHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           waitSemaphoreValuesCount;
//    const uint64_t*    pWaitSemaphoreValues;
//    uint32_t           signalSemaphoreValuesCount;
//    const uint64_t*    pSignalSemaphoreValues;
//} VkD3D12FenceSubmitInfoKHR;
//
//typedef struct VkSemaphoreGetWin32HandleInfoKHR {
//    VkStructureType                             sType;
//    const void*                                 pNext;
//    VkSemaphore                                 semaphore;
//    VkExternalSemaphoreHandleTypeFlagBitsKHR    handleType;
//} VkSemaphoreGetWin32HandleInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkImportSemaphoreWin32HandleKHR)(VkDevice device, const VkImportSemaphoreWin32HandleInfoKHR* pImportSemaphoreWin32HandleInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkGetSemaphoreWin32HandleKHR)(VkDevice device, const VkSemaphoreGetWin32HandleInfoKHR* pGetWin32HandleInfo, HANDLE* pHandle);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkImportSemaphoreWin32HandleKHR(
//VkDevice                                    device,
//const VkImportSemaphoreWin32HandleInfoKHR*  pImportSemaphoreWin32HandleInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetSemaphoreWin32HandleKHR(
//VkDevice                                    device,
//const VkSemaphoreGetWin32HandleInfoKHR*     pGetWin32HandleInfo,
//HANDLE*                                     pHandle);
//#endif
//#endif /* VK_USE_PLATFORM_WIN32_KHR */
//
//#define VK_KHR_external_semaphore_fd 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_FD_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_SEMAPHORE_FD_EXTENSION_NAME "VK_KHR_external_semaphore_fd"
//
//typedef struct VkImportSemaphoreFdInfoKHR {
//    VkStructureType                             sType;
//    const void*                                 pNext;
//    VkSemaphore                                 semaphore;
//    VkSemaphoreImportFlagsKHR                   flags;
//    VkExternalSemaphoreHandleTypeFlagBitsKHR    handleType;
//    int                                         fd;
//} VkImportSemaphoreFdInfoKHR;
//
//typedef struct VkSemaphoreGetFdInfoKHR {
//    VkStructureType                             sType;
//    const void*                                 pNext;
//    VkSemaphore                                 semaphore;
//    VkExternalSemaphoreHandleTypeFlagBitsKHR    handleType;
//} VkSemaphoreGetFdInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkImportSemaphoreFdKHR)(VkDevice device, const VkImportSemaphoreFdInfoKHR* pImportSemaphoreFdInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkGetSemaphoreFdKHR)(VkDevice device, const VkSemaphoreGetFdInfoKHR* pGetFdInfo, int* pFd);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkImportSemaphoreFdKHR(
//VkDevice                                    device,
//const VkImportSemaphoreFdInfoKHR*           pImportSemaphoreFdInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetSemaphoreFdKHR(
//VkDevice                                    device,
//const VkSemaphoreGetFdInfoKHR*              pGetFdInfo,
//int*                                        pFd);
//#endif
//
//#define VK_KHR_push_descriptor 1
//#define VK_KHR_PUSH_DESCRIPTOR_SPEC_VERSION 1
//#define VK_KHR_PUSH_DESCRIPTOR_EXTENSION_NAME "VK_KHR_push_descriptor"
//
//typedef struct VkPhysicalDevicePushDescriptorPropertiesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           maxPushDescriptors;
//} VkPhysicalDevicePushDescriptorPropertiesKHR;
//
//
//typedef void (VKAPI_PTR *PFN_vkCmdPushDescriptorSetKHR)(VkCommandBuffer commandBuffer, VkPipelineBindPoint pipelineBindPoint, VkPipelineLayout layout, uint32_t set, uint32_t descriptorWriteCount, const VkWriteDescriptorSet* pDescriptorWrites);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkCmdPushDescriptorSetKHR(
//VkCommandBuffer                             commandBuffer,
//VkPipelineBindPoint                         pipelineBindPoint,
//VkPipelineLayout                            layout,
//uint32_t                                    set,
//uint32_t                                    descriptorWriteCount,
//const VkWriteDescriptorSet*                 pDescriptorWrites);
//#endif
//
//#define VK_KHR_16bit_storage 1
//#define VK_KHR_16BIT_STORAGE_SPEC_VERSION 1
//#define VK_KHR_16BIT_STORAGE_EXTENSION_NAME "VK_KHR_16bit_storage"
//
//typedef struct VkPhysicalDevice16BitStorageFeaturesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           storageBuffer16BitAccess;
//    VkBool32           uniformAndStorageBuffer16BitAccess;
//    VkBool32           storagePushConstant16;
//    VkBool32           storageInputOutput16;
//} VkPhysicalDevice16BitStorageFeaturesKHR;
//
//
//
//#define VK_KHR_incremental_present 1
//#define VK_KHR_INCREMENTAL_PRESENT_SPEC_VERSION 1
//#define VK_KHR_INCREMENTAL_PRESENT_EXTENSION_NAME "VK_KHR_incremental_present"
//
//typedef struct VkRectLayerKHR {
//    VkOffset2D    offset;
//    VkExtent2D    extent;
//    uint32_t      layer;
//} VkRectLayerKHR;
//
//typedef struct VkPresentRegionKHR {
//    uint32_t                 rectangleCount;
//    const VkRectLayerKHR*    pRectangles;
//} VkPresentRegionKHR;
//
//typedef struct VkPresentRegionsKHR {
//    VkStructureType              sType;
//    const void*                  pNext;
//    uint32_t                     swapchainCount;
//    const VkPresentRegionKHR*    pRegions;
//} VkPresentRegionsKHR;
//
//
//
//#define VK_KHR_descriptor_update_template 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkDescriptorUpdateTemplateKHR)
//
//#define VK_KHR_DESCRIPTOR_UPDATE_TEMPLATE_SPEC_VERSION 1
//#define VK_KHR_DESCRIPTOR_UPDATE_TEMPLATE_EXTENSION_NAME "VK_KHR_descriptor_update_template"
//
//
//typedef enum VkDescriptorUpdateTemplateTypeKHR {
//    VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_DESCRIPTOR_SET_KHR = 0,
//    VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_PUSH_DESCRIPTORS_KHR = 1,
//    VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_BEGIN_RANGE_KHR = VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_DESCRIPTOR_SET_KHR,
//    VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_END_RANGE_KHR = VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_PUSH_DESCRIPTORS_KHR,
//    VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_RANGE_SIZE_KHR = (VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_PUSH_DESCRIPTORS_KHR - VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_DESCRIPTOR_SET_KHR + 1),
//    VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkDescriptorUpdateTemplateTypeKHR;
//
//typedef VkFlags VkDescriptorUpdateTemplateCreateFlagsKHR;
//
//typedef struct VkDescriptorUpdateTemplateEntryKHR {
//    uint32_t            dstBinding;
//    uint32_t            dstArrayElement;
//    uint32_t            descriptorCount;
//    VkDescriptorType    descriptorType;
//    size_t              offset;
//    size_t              stride;
//} VkDescriptorUpdateTemplateEntryKHR;
//
//typedef struct VkDescriptorUpdateTemplateCreateInfoKHR {
//    VkStructureType                              sType;
//    void*                                        pNext;
//    VkDescriptorUpdateTemplateCreateFlagsKHR     flags;
//    uint32_t                                     descriptorUpdateEntryCount;
//    const VkDescriptorUpdateTemplateEntryKHR*    pDescriptorUpdateEntries;
//    VkDescriptorUpdateTemplateTypeKHR            templateType;
//    VkDescriptorSetLayout                        descriptorSetLayout;
//    VkPipelineBindPoint                          pipelineBindPoint;
//    VkPipelineLayout                             pipelineLayout;
//    uint32_t                                     set;
//} VkDescriptorUpdateTemplateCreateInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateDescriptorUpdateTemplateKHR)(VkDevice device, const VkDescriptorUpdateTemplateCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkDescriptorUpdateTemplateKHR* pDescriptorUpdateTemplate);
//typedef void (VKAPI_PTR *PFN_vkDestroyDescriptorUpdateTemplateKHR)(VkDevice device, VkDescriptorUpdateTemplateKHR descriptorUpdateTemplate, const VkAllocationCallbacks* pAllocator);
//typedef void (VKAPI_PTR *PFN_vkUpdateDescriptorSetWithTemplateKHR)(VkDevice device, VkDescriptorSet descriptorSet, VkDescriptorUpdateTemplateKHR descriptorUpdateTemplate, const void* pData);
//typedef void (VKAPI_PTR *PFN_vkCmdPushDescriptorSetWithTemplateKHR)(VkCommandBuffer commandBuffer, VkDescriptorUpdateTemplateKHR descriptorUpdateTemplate, VkPipelineLayout layout, uint32_t set, const void* pData);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateDescriptorUpdateTemplateKHR(
//VkDevice                                    device,
//const VkDescriptorUpdateTemplateCreateInfoKHR* pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkDescriptorUpdateTemplateKHR*              pDescriptorUpdateTemplate);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyDescriptorUpdateTemplateKHR(
//VkDevice                                    device,
//VkDescriptorUpdateTemplateKHR               descriptorUpdateTemplate,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR void VKAPI_CALL vkUpdateDescriptorSetWithTemplateKHR(
//VkDevice                                    device,
//VkDescriptorSet                             descriptorSet,
//VkDescriptorUpdateTemplateKHR               descriptorUpdateTemplate,
//const void*                                 pData);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdPushDescriptorSetWithTemplateKHR(
//VkCommandBuffer                             commandBuffer,
//VkDescriptorUpdateTemplateKHR               descriptorUpdateTemplate,
//VkPipelineLayout                            layout,
//uint32_t                                    set,
//const void*                                 pData);
//#endif
//
//#define VK_KHR_shared_presentable_image 1
//#define VK_KHR_SHARED_PRESENTABLE_IMAGE_SPEC_VERSION 1
//#define VK_KHR_SHARED_PRESENTABLE_IMAGE_EXTENSION_NAME "VK_KHR_shared_presentable_image"
//
//typedef struct VkSharedPresentSurfaceCapabilitiesKHR {
//    VkStructureType      sType;
//    void*                pNext;
//    VkImageUsageFlags    sharedPresentSupportedUsageFlags;
//} VkSharedPresentSurfaceCapabilitiesKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetSwapchainStatusKHR)(VkDevice device, VkSwapchainKHR swapchain);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetSwapchainStatusKHR(
//VkDevice                                    device,
//VkSwapchainKHR                              swapchain);
//#endif
//
//#define VK_KHR_external_fence_capabilities 1
//#define VK_KHR_EXTERNAL_FENCE_CAPABILITIES_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_FENCE_CAPABILITIES_EXTENSION_NAME "VK_KHR_external_fence_capabilities"
//
//
//typedef enum VkExternalFenceHandleTypeFlagBitsKHR {
//    VK_EXTERNAL_FENCE_HANDLE_TYPE_OPAQUE_FD_BIT_KHR = 0x00000001,
//    VK_EXTERNAL_FENCE_HANDLE_TYPE_OPAQUE_WIN32_BIT_KHR = 0x00000002,
//    VK_EXTERNAL_FENCE_HANDLE_TYPE_OPAQUE_WIN32_KMT_BIT_KHR = 0x00000004,
//    VK_EXTERNAL_FENCE_HANDLE_TYPE_SYNC_FD_BIT_KHR = 0x00000008,
//    VK_EXTERNAL_FENCE_HANDLE_TYPE_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkExternalFenceHandleTypeFlagBitsKHR;
//typedef VkFlags VkExternalFenceHandleTypeFlagsKHR;
//
//typedef enum VkExternalFenceFeatureFlagBitsKHR {
//    VK_EXTERNAL_FENCE_FEATURE_EXPORTABLE_BIT_KHR = 0x00000001,
//    VK_EXTERNAL_FENCE_FEATURE_IMPORTABLE_BIT_KHR = 0x00000002,
//    VK_EXTERNAL_FENCE_FEATURE_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkExternalFenceFeatureFlagBitsKHR;
//typedef VkFlags VkExternalFenceFeatureFlagsKHR;
//
//typedef struct VkPhysicalDeviceExternalFenceInfoKHR {
//    VkStructureType                         sType;
//    const void*                             pNext;
//    VkExternalFenceHandleTypeFlagBitsKHR    handleType;
//} VkPhysicalDeviceExternalFenceInfoKHR;
//
//typedef struct VkExternalFencePropertiesKHR {
//    VkStructureType                      sType;
//    void*                                pNext;
//    VkExternalFenceHandleTypeFlagsKHR    exportFromImportedHandleTypes;
//    VkExternalFenceHandleTypeFlagsKHR    compatibleHandleTypes;
//    VkExternalFenceFeatureFlagsKHR       externalFenceFeatures;
//} VkExternalFencePropertiesKHR;
//
//
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceExternalFencePropertiesKHR)(VkPhysicalDevice physicalDevice, const VkPhysicalDeviceExternalFenceInfoKHR* pExternalFenceInfo, VkExternalFencePropertiesKHR* pExternalFenceProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceExternalFencePropertiesKHR(
//VkPhysicalDevice                            physicalDevice,
//const VkPhysicalDeviceExternalFenceInfoKHR* pExternalFenceInfo,
//VkExternalFencePropertiesKHR*               pExternalFenceProperties);
//#endif
//
//#define VK_KHR_external_fence 1
//#define VK_KHR_EXTERNAL_FENCE_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_FENCE_EXTENSION_NAME "VK_KHR_external_fence"
//
//
//typedef enum VkFenceImportFlagBitsKHR {
//    VK_FENCE_IMPORT_TEMPORARY_BIT_KHR = 0x00000001,
//    VK_FENCE_IMPORT_FLAG_BITS_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkFenceImportFlagBitsKHR;
//typedef VkFlags VkFenceImportFlagsKHR;
//
//typedef struct VkExportFenceCreateInfoKHR {
//    VkStructureType                      sType;
//    const void*                          pNext;
//    VkExternalFenceHandleTypeFlagsKHR    handleTypes;
//} VkExportFenceCreateInfoKHR;
//
//
//
//#ifdef VK_USE_PLATFORM_WIN32_KHR
//#define VK_KHR_external_fence_win32 1
//#define VK_KHR_EXTERNAL_FENCE_WIN32_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_FENCE_WIN32_EXTENSION_NAME "VK_KHR_external_fence_win32"
//
//typedef struct VkImportFenceWin32HandleInfoKHR {
//    VkStructureType                         sType;
//    const void*                             pNext;
//    VkFence                                 fence;
//    VkFenceImportFlagsKHR                   flags;
//    VkExternalFenceHandleTypeFlagBitsKHR    handleType;
//    HANDLE                                  handle;
//    LPCWSTR                                 name;
//} VkImportFenceWin32HandleInfoKHR;
//
//typedef struct VkExportFenceWin32HandleInfoKHR {
//    VkStructureType               sType;
//    const void*                   pNext;
//    const SECURITY_ATTRIBUTES*    pAttributes;
//    DWORD                         dwAccess;
//    LPCWSTR                       name;
//} VkExportFenceWin32HandleInfoKHR;
//
//typedef struct VkFenceGetWin32HandleInfoKHR {
//    VkStructureType                         sType;
//    const void*                             pNext;
//    VkFence                                 fence;
//    VkExternalFenceHandleTypeFlagBitsKHR    handleType;
//} VkFenceGetWin32HandleInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkImportFenceWin32HandleKHR)(VkDevice device, const VkImportFenceWin32HandleInfoKHR* pImportFenceWin32HandleInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkGetFenceWin32HandleKHR)(VkDevice device, const VkFenceGetWin32HandleInfoKHR* pGetWin32HandleInfo, HANDLE* pHandle);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkImportFenceWin32HandleKHR(
//VkDevice                                    device,
//const VkImportFenceWin32HandleInfoKHR*      pImportFenceWin32HandleInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetFenceWin32HandleKHR(
//VkDevice                                    device,
//const VkFenceGetWin32HandleInfoKHR*         pGetWin32HandleInfo,
//HANDLE*                                     pHandle);
//#endif
//#endif /* VK_USE_PLATFORM_WIN32_KHR */
//
//#define VK_KHR_external_fence_fd 1
//#define VK_KHR_EXTERNAL_FENCE_FD_SPEC_VERSION 1
//#define VK_KHR_EXTERNAL_FENCE_FD_EXTENSION_NAME "VK_KHR_external_fence_fd"
//
//typedef struct VkImportFenceFdInfoKHR {
//    VkStructureType                         sType;
//    const void*                             pNext;
//    VkFence                                 fence;
//    VkFenceImportFlagsKHR                   flags;
//    VkExternalFenceHandleTypeFlagBitsKHR    handleType;
//    int                                     fd;
//} VkImportFenceFdInfoKHR;
//
//typedef struct VkFenceGetFdInfoKHR {
//    VkStructureType                         sType;
//    const void*                             pNext;
//    VkFence                                 fence;
//    VkExternalFenceHandleTypeFlagBitsKHR    handleType;
//} VkFenceGetFdInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkImportFenceFdKHR)(VkDevice device, const VkImportFenceFdInfoKHR* pImportFenceFdInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkGetFenceFdKHR)(VkDevice device, const VkFenceGetFdInfoKHR* pGetFdInfo, int* pFd);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkImportFenceFdKHR(
//VkDevice                                    device,
//const VkImportFenceFdInfoKHR*               pImportFenceFdInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetFenceFdKHR(
//VkDevice                                    device,
//const VkFenceGetFdInfoKHR*                  pGetFdInfo,
//int*                                        pFd);
//#endif
//
//#define VK_KHR_maintenance2 1
//#define VK_KHR_MAINTENANCE2_SPEC_VERSION  1
//#define VK_KHR_MAINTENANCE2_EXTENSION_NAME "VK_KHR_maintenance2"
//
//
//typedef enum VkPointClippingBehaviorKHR {
//    VK_POINT_CLIPPING_BEHAVIOR_ALL_CLIP_PLANES_KHR = 0,
//    VK_POINT_CLIPPING_BEHAVIOR_USER_CLIP_PLANES_ONLY_KHR = 1,
//    VK_POINT_CLIPPING_BEHAVIOR_BEGIN_RANGE_KHR = VK_POINT_CLIPPING_BEHAVIOR_ALL_CLIP_PLANES_KHR,
//    VK_POINT_CLIPPING_BEHAVIOR_END_RANGE_KHR = VK_POINT_CLIPPING_BEHAVIOR_USER_CLIP_PLANES_ONLY_KHR,
//    VK_POINT_CLIPPING_BEHAVIOR_RANGE_SIZE_KHR = (VK_POINT_CLIPPING_BEHAVIOR_USER_CLIP_PLANES_ONLY_KHR - VK_POINT_CLIPPING_BEHAVIOR_ALL_CLIP_PLANES_KHR + 1),
//    VK_POINT_CLIPPING_BEHAVIOR_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkPointClippingBehaviorKHR;
//
//typedef enum VkTessellationDomainOriginKHR {
//    VK_TESSELLATION_DOMAIN_ORIGIN_UPPER_LEFT_KHR = 0,
//    VK_TESSELLATION_DOMAIN_ORIGIN_LOWER_LEFT_KHR = 1,
//    VK_TESSELLATION_DOMAIN_ORIGIN_BEGIN_RANGE_KHR = VK_TESSELLATION_DOMAIN_ORIGIN_UPPER_LEFT_KHR,
//    VK_TESSELLATION_DOMAIN_ORIGIN_END_RANGE_KHR = VK_TESSELLATION_DOMAIN_ORIGIN_LOWER_LEFT_KHR,
//    VK_TESSELLATION_DOMAIN_ORIGIN_RANGE_SIZE_KHR = (VK_TESSELLATION_DOMAIN_ORIGIN_LOWER_LEFT_KHR - VK_TESSELLATION_DOMAIN_ORIGIN_UPPER_LEFT_KHR + 1),
//    VK_TESSELLATION_DOMAIN_ORIGIN_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkTessellationDomainOriginKHR;
//
//typedef struct VkPhysicalDevicePointClippingPropertiesKHR {
//    VkStructureType               sType;
//    void*                         pNext;
//    VkPointClippingBehaviorKHR    pointClippingBehavior;
//} VkPhysicalDevicePointClippingPropertiesKHR;
//
//typedef struct VkInputAttachmentAspectReferenceKHR {
//    uint32_t              subpass;
//    uint32_t              inputAttachmentIndex;
//    VkImageAspectFlags    aspectMask;
//} VkInputAttachmentAspectReferenceKHR;
//
//typedef struct VkRenderPassInputAttachmentAspectCreateInfoKHR {
//    VkStructureType                               sType;
//    const void*                                   pNext;
//    uint32_t                                      aspectReferenceCount;
//    const VkInputAttachmentAspectReferenceKHR*    pAspectReferences;
//} VkRenderPassInputAttachmentAspectCreateInfoKHR;
//
//typedef struct VkImageViewUsageCreateInfoKHR {
//    VkStructureType      sType;
//    const void*          pNext;
//    VkImageUsageFlags    usage;
//} VkImageViewUsageCreateInfoKHR;
//
//typedef struct VkPipelineTessellationDomainOriginStateCreateInfoKHR {
//    VkStructureType                  sType;
//    const void*                      pNext;
//    VkTessellationDomainOriginKHR    domainOrigin;
//} VkPipelineTessellationDomainOriginStateCreateInfoKHR;
//
//
//
//#define VK_KHR_get_surface_capabilities2 1
//#define VK_KHR_GET_SURFACE_CAPABILITIES_2_SPEC_VERSION 1
//#define VK_KHR_GET_SURFACE_CAPABILITIES_2_EXTENSION_NAME "VK_KHR_get_surface_capabilities2"
//
//typedef struct VkPhysicalDeviceSurfaceInfo2KHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkSurfaceKHR       surface;
//} VkPhysicalDeviceSurfaceInfo2KHR;
//
//typedef struct VkSurfaceCapabilities2KHR {
//    VkStructureType             sType;
//    void*                       pNext;
//    VkSurfaceCapabilitiesKHR    surfaceCapabilities;
//} VkSurfaceCapabilities2KHR;
//
//typedef struct VkSurfaceFormat2KHR {
//    VkStructureType       sType;
//    void*                 pNext;
//    VkSurfaceFormatKHR    surfaceFormat;
//} VkSurfaceFormat2KHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceSurfaceCapabilities2KHR)(VkPhysicalDevice physicalDevice, const VkPhysicalDeviceSurfaceInfo2KHR* pSurfaceInfo, VkSurfaceCapabilities2KHR* pSurfaceCapabilities);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceSurfaceFormats2KHR)(VkPhysicalDevice physicalDevice, const VkPhysicalDeviceSurfaceInfo2KHR* pSurfaceInfo, uint32_t* pSurfaceFormatCount, VkSurfaceFormat2KHR* pSurfaceFormats);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceSurfaceCapabilities2KHR(
//VkPhysicalDevice                            physicalDevice,
//const VkPhysicalDeviceSurfaceInfo2KHR*      pSurfaceInfo,
//VkSurfaceCapabilities2KHR*                  pSurfaceCapabilities);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceSurfaceFormats2KHR(
//VkPhysicalDevice                            physicalDevice,
//const VkPhysicalDeviceSurfaceInfo2KHR*      pSurfaceInfo,
//uint32_t*                                   pSurfaceFormatCount,
//VkSurfaceFormat2KHR*                        pSurfaceFormats);
//#endif
//
//#define VK_KHR_variable_pointers 1
//#define VK_KHR_VARIABLE_POINTERS_SPEC_VERSION 1
//#define VK_KHR_VARIABLE_POINTERS_EXTENSION_NAME "VK_KHR_variable_pointers"
//
//typedef struct VkPhysicalDeviceVariablePointerFeaturesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           variablePointersStorageBuffer;
//    VkBool32           variablePointers;
//} VkPhysicalDeviceVariablePointerFeaturesKHR;
//
//
//
//#define VK_KHR_dedicated_allocation 1
//#define VK_KHR_DEDICATED_ALLOCATION_SPEC_VERSION 3
//#define VK_KHR_DEDICATED_ALLOCATION_EXTENSION_NAME "VK_KHR_dedicated_allocation"
//
//typedef struct VkMemoryDedicatedRequirementsKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           prefersDedicatedAllocation;
//    VkBool32           requiresDedicatedAllocation;
//} VkMemoryDedicatedRequirementsKHR;
//
//typedef struct VkMemoryDedicatedAllocateInfoKHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkImage            image;
//    VkBuffer           buffer;
//} VkMemoryDedicatedAllocateInfoKHR;
//
//
//
//#define VK_KHR_storage_buffer_storage_class 1
//#define VK_KHR_STORAGE_BUFFER_STORAGE_CLASS_SPEC_VERSION 1
//#define VK_KHR_STORAGE_BUFFER_STORAGE_CLASS_EXTENSION_NAME "VK_KHR_storage_buffer_storage_class"
//
//
//#define VK_KHR_relaxed_block_layout 1
//#define VK_KHR_RELAXED_BLOCK_LAYOUT_SPEC_VERSION 1
//#define VK_KHR_RELAXED_BLOCK_LAYOUT_EXTENSION_NAME "VK_KHR_relaxed_block_layout"
//
//
//#define VK_KHR_get_memory_requirements2 1
//#define VK_KHR_GET_MEMORY_REQUIREMENTS_2_SPEC_VERSION 1
//#define VK_KHR_GET_MEMORY_REQUIREMENTS_2_EXTENSION_NAME "VK_KHR_get_memory_requirements2"
//
//typedef struct VkBufferMemoryRequirementsInfo2KHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkBuffer           buffer;
//} VkBufferMemoryRequirementsInfo2KHR;
//
//typedef struct VkImageMemoryRequirementsInfo2KHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkImage            image;
//} VkImageMemoryRequirementsInfo2KHR;
//
//typedef struct VkImageSparseMemoryRequirementsInfo2KHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkImage            image;
//} VkImageSparseMemoryRequirementsInfo2KHR;
//
//typedef struct VkMemoryRequirements2KHR {
//    VkStructureType         sType;
//    void*                   pNext;
//    VkMemoryRequirements    memoryRequirements;
//} VkMemoryRequirements2KHR;
//
//typedef struct VkSparseImageMemoryRequirements2KHR {
//    VkStructureType                    sType;
//    void*                              pNext;
//    VkSparseImageMemoryRequirements    memoryRequirements;
//} VkSparseImageMemoryRequirements2KHR;
//
//
//typedef void (VKAPI_PTR *PFN_vkGetImageMemoryRequirements2KHR)(VkDevice device, const VkImageMemoryRequirementsInfo2KHR* pInfo, VkMemoryRequirements2KHR* pMemoryRequirements);
//typedef void (VKAPI_PTR *PFN_vkGetBufferMemoryRequirements2KHR)(VkDevice device, const VkBufferMemoryRequirementsInfo2KHR* pInfo, VkMemoryRequirements2KHR* pMemoryRequirements);
//typedef void (VKAPI_PTR *PFN_vkGetImageSparseMemoryRequirements2KHR)(VkDevice device, const VkImageSparseMemoryRequirementsInfo2KHR* pInfo, uint32_t* pSparseMemoryRequirementCount, VkSparseImageMemoryRequirements2KHR* pSparseMemoryRequirements);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkGetImageMemoryRequirements2KHR(
//VkDevice                                    device,
//const VkImageMemoryRequirementsInfo2KHR*    pInfo,
//VkMemoryRequirements2KHR*                   pMemoryRequirements);
//
//VKAPI_ATTR void VKAPI_CALL vkGetBufferMemoryRequirements2KHR(
//VkDevice                                    device,
//const VkBufferMemoryRequirementsInfo2KHR*   pInfo,
//VkMemoryRequirements2KHR*                   pMemoryRequirements);
//
//VKAPI_ATTR void VKAPI_CALL vkGetImageSparseMemoryRequirements2KHR(
//VkDevice                                    device,
//const VkImageSparseMemoryRequirementsInfo2KHR* pInfo,
//uint32_t*                                   pSparseMemoryRequirementCount,
//VkSparseImageMemoryRequirements2KHR*        pSparseMemoryRequirements);
//#endif
//
//#define VK_KHR_image_format_list 1
//#define VK_KHR_IMAGE_FORMAT_LIST_SPEC_VERSION 1
//#define VK_KHR_IMAGE_FORMAT_LIST_EXTENSION_NAME "VK_KHR_image_format_list"
//
//typedef struct VkImageFormatListCreateInfoKHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           viewFormatCount;
//    const VkFormat*    pViewFormats;
//} VkImageFormatListCreateInfoKHR;
//
//
//
//#define VK_KHR_sampler_ycbcr_conversion 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkSamplerYcbcrConversionKHR)
//
//#define VK_KHR_SAMPLER_YCBCR_CONVERSION_SPEC_VERSION 1
//#define VK_KHR_SAMPLER_YCBCR_CONVERSION_EXTENSION_NAME "VK_KHR_sampler_ycbcr_conversion"
//
//
//typedef enum VkSamplerYcbcrModelConversionKHR {
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_RGB_IDENTITY_KHR = 0,
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_IDENTITY_KHR = 1,
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_709_KHR = 2,
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_601_KHR = 3,
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_2020_KHR = 4,
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_BEGIN_RANGE_KHR = VK_SAMPLER_YCBCR_MODEL_CONVERSION_RGB_IDENTITY_KHR,
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_END_RANGE_KHR = VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_2020_KHR,
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_RANGE_SIZE_KHR = (VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_2020_KHR - VK_SAMPLER_YCBCR_MODEL_CONVERSION_RGB_IDENTITY_KHR + 1),
//    VK_SAMPLER_YCBCR_MODEL_CONVERSION_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkSamplerYcbcrModelConversionKHR;
//
//typedef enum VkSamplerYcbcrRangeKHR {
//    VK_SAMPLER_YCBCR_RANGE_ITU_FULL_KHR = 0,
//    VK_SAMPLER_YCBCR_RANGE_ITU_NARROW_KHR = 1,
//    VK_SAMPLER_YCBCR_RANGE_BEGIN_RANGE_KHR = VK_SAMPLER_YCBCR_RANGE_ITU_FULL_KHR,
//    VK_SAMPLER_YCBCR_RANGE_END_RANGE_KHR = VK_SAMPLER_YCBCR_RANGE_ITU_NARROW_KHR,
//    VK_SAMPLER_YCBCR_RANGE_RANGE_SIZE_KHR = (VK_SAMPLER_YCBCR_RANGE_ITU_NARROW_KHR - VK_SAMPLER_YCBCR_RANGE_ITU_FULL_KHR + 1),
//    VK_SAMPLER_YCBCR_RANGE_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkSamplerYcbcrRangeKHR;
//
//typedef enum VkChromaLocationKHR {
//    VK_CHROMA_LOCATION_COSITED_EVEN_KHR = 0,
//    VK_CHROMA_LOCATION_MIDPOINT_KHR = 1,
//    VK_CHROMA_LOCATION_BEGIN_RANGE_KHR = VK_CHROMA_LOCATION_COSITED_EVEN_KHR,
//    VK_CHROMA_LOCATION_END_RANGE_KHR = VK_CHROMA_LOCATION_MIDPOINT_KHR,
//    VK_CHROMA_LOCATION_RANGE_SIZE_KHR = (VK_CHROMA_LOCATION_MIDPOINT_KHR - VK_CHROMA_LOCATION_COSITED_EVEN_KHR + 1),
//    VK_CHROMA_LOCATION_MAX_ENUM_KHR = 0x7FFFFFFF
//} VkChromaLocationKHR;
//
//typedef struct VkSamplerYcbcrConversionCreateInfoKHR {
//    VkStructureType                     sType;
//    const void*                         pNext;
//    VkFormat                            format;
//    VkSamplerYcbcrModelConversionKHR    ycbcrModel;
//    VkSamplerYcbcrRangeKHR              ycbcrRange;
//    VkComponentMapping                  components;
//    VkChromaLocationKHR                 xChromaOffset;
//    VkChromaLocationKHR                 yChromaOffset;
//    VkFilter                            chromaFilter;
//    VkBool32                            forceExplicitReconstruction;
//} VkSamplerYcbcrConversionCreateInfoKHR;
//
//typedef struct VkSamplerYcbcrConversionInfoKHR {
//    VkStructureType                sType;
//    const void*                    pNext;
//    VkSamplerYcbcrConversionKHR    conversion;
//} VkSamplerYcbcrConversionInfoKHR;
//
//typedef struct VkBindImagePlaneMemoryInfoKHR {
//    VkStructureType          sType;
//    const void*              pNext;
//    VkImageAspectFlagBits    planeAspect;
//} VkBindImagePlaneMemoryInfoKHR;
//
//typedef struct VkImagePlaneMemoryRequirementsInfoKHR {
//    VkStructureType          sType;
//    const void*              pNext;
//    VkImageAspectFlagBits    planeAspect;
//} VkImagePlaneMemoryRequirementsInfoKHR;
//
//typedef struct VkPhysicalDeviceSamplerYcbcrConversionFeaturesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           samplerYcbcrConversion;
//} VkPhysicalDeviceSamplerYcbcrConversionFeaturesKHR;
//
//typedef struct VkSamplerYcbcrConversionImageFormatPropertiesKHR {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           combinedImageSamplerDescriptorCount;
//} VkSamplerYcbcrConversionImageFormatPropertiesKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateSamplerYcbcrConversionKHR)(VkDevice device, const VkSamplerYcbcrConversionCreateInfoKHR* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSamplerYcbcrConversionKHR* pYcbcrConversion);
//typedef void (VKAPI_PTR *PFN_vkDestroySamplerYcbcrConversionKHR)(VkDevice device, VkSamplerYcbcrConversionKHR ycbcrConversion, const VkAllocationCallbacks* pAllocator);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateSamplerYcbcrConversionKHR(
//VkDevice                                    device,
//const VkSamplerYcbcrConversionCreateInfoKHR* pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSamplerYcbcrConversionKHR*                pYcbcrConversion);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroySamplerYcbcrConversionKHR(
//VkDevice                                    device,
//VkSamplerYcbcrConversionKHR                 ycbcrConversion,
//const VkAllocationCallbacks*                pAllocator);
//#endif
//
//#define VK_KHR_bind_memory2 1
//#define VK_KHR_BIND_MEMORY_2_SPEC_VERSION 1
//#define VK_KHR_BIND_MEMORY_2_EXTENSION_NAME "VK_KHR_bind_memory2"
//
//typedef struct VkBindBufferMemoryInfoKHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkBuffer           buffer;
//    VkDeviceMemory     memory;
//    VkDeviceSize       memoryOffset;
//} VkBindBufferMemoryInfoKHR;
//
//typedef struct VkBindImageMemoryInfoKHR {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkImage            image;
//    VkDeviceMemory     memory;
//    VkDeviceSize       memoryOffset;
//} VkBindImageMemoryInfoKHR;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkBindBufferMemory2KHR)(VkDevice device, uint32_t bindInfoCount, const VkBindBufferMemoryInfoKHR* pBindInfos);
//typedef VkResult (VKAPI_PTR *PFN_vkBindImageMemory2KHR)(VkDevice device, uint32_t bindInfoCount, const VkBindImageMemoryInfoKHR* pBindInfos);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkBindBufferMemory2KHR(
//VkDevice                                    device,
//uint32_t                                    bindInfoCount,
//const VkBindBufferMemoryInfoKHR*            pBindInfos);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkBindImageMemory2KHR(
//VkDevice                                    device,
//uint32_t                                    bindInfoCount,
//const VkBindImageMemoryInfoKHR*             pBindInfos);
//#endif
//
//#define VK_EXT_debug_report 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkDebugReportCallbackEXT)
//
//#define VK_EXT_DEBUG_REPORT_SPEC_VERSION  9
//#define VK_EXT_DEBUG_REPORT_EXTENSION_NAME "VK_EXT_debug_report"
//#define DEBUG_REPORT_CREATE_INFO_EXT DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT
//#define VK_DEBUG_REPORT_OBJECT_TYPE_DEBUG_REPORT_EXT VK_DEBUG_REPORT_OBJECT_TYPE_DEBUG_REPORT_CALLBACK_EXT_EXT
//

//typedef VkBool32 (VKAPI_PTR *PFN_vkDebugReportCallbackEXT)(
//VkDebugReportFlagsEXT                       flags,
//VkDebugReportObjectTypeEXT                  objectType,
//uint64_t                                    object,
//size_t                                      location,
//int32_t                                     messageCode,
//const char*                                 pLayerPrefix,
//const char*                                 pMessage,
//void*                                       pUserData);

inline var VkDebugReportCallbackCreateInfoEXT.type
    get() = VkStructureType of VkDebugReportCallbackCreateInfoEXT.nsType(adr)
    set(value) = VkDebugReportCallbackCreateInfoEXT.nsType(adr, value.i)
inline var VkDebugReportCallbackCreateInfoEXT.next
    get() = VkDebugReportCallbackCreateInfoEXT.npNext(adr)
    set(value) = VkDebugReportCallbackCreateInfoEXT.npNext(adr, value)
inline var VkDebugReportCallbackCreateInfoEXT.flags: VkDebugReportFlagsEXT
    get() = VkDebugReportCallbackCreateInfoEXT.nflags(adr)
    set(value) = VkDebugReportCallbackCreateInfoEXT.nflags(adr, value)
inline var VkDebugReportCallbackCreateInfoEXT.callback: VkDebugReportCallbackEXTI
    get() = VkDebugReportCallbackCreateInfoEXT.npfnCallback(adr)
    set(value) = VkDebugReportCallbackCreateInfoEXT.npfnCallback(adr, value)
inline var VkDebugReportCallbackCreateInfoEXT.userData
    get() = VkDebugReportCallbackCreateInfoEXT.npUserData(adr)
    set(value) = VkDebugReportCallbackCreateInfoEXT.npUserData(adr, value)

//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateDebugReportCallbackEXT)(VkInstance instance, const VkDebugReportCallbackCreateInfoEXT* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkDebugReportCallbackEXT* pCallback);
//typedef void (VKAPI_PTR *PFN_vkDestroyDebugReportCallbackEXT)(VkInstance instance, VkDebugReportCallbackEXT callback, const VkAllocationCallbacks* pAllocator);
//typedef void (VKAPI_PTR *PFN_vkDebugReportMessageEXT)(VkInstance instance, VkDebugReportFlagsEXT flags, VkDebugReportObjectTypeEXT objectType, uint64_t object, size_t location, int32_t messageCode, const char* pLayerPrefix, const char* pMessage);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateDebugReportCallbackEXT(
//VkInstance                                  instance,
//const VkDebugReportCallbackCreateInfoEXT*   pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkDebugReportCallbackEXT*                   pCallback);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyDebugReportCallbackEXT(
//VkInstance                                  instance,
//VkDebugReportCallbackEXT                    callback,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR void VKAPI_CALL vkDebugReportMessageEXT(
//VkInstance                                  instance,
//VkDebugReportFlagsEXT                       flags,
//VkDebugReportObjectTypeEXT                  objectType,
//uint64_t                                    object,
//size_t                                      location,
//int32_t                                     messageCode,
//const char*                                 pLayerPrefix,
//const char*                                 pMessage);
//#endif
//
//#define VK_NV_glsl_shader 1
//#define VK_NV_GLSL_SHADER_SPEC_VERSION    1
//#define VK_NV_GLSL_SHADER_EXTENSION_NAME  "VK_NV_glsl_shader"
//
//
//#define VK_EXT_depth_range_unrestricted 1
//#define VK_EXT_DEPTH_RANGE_UNRESTRICTED_SPEC_VERSION 1
//#define VK_EXT_DEPTH_RANGE_UNRESTRICTED_EXTENSION_NAME "VK_EXT_depth_range_unrestricted"
//
//
//#define VK_IMG_filter_cubic 1
//#define VK_IMG_FILTER_CUBIC_SPEC_VERSION  1
//#define VK_IMG_FILTER_CUBIC_EXTENSION_NAME "VK_IMG_filter_cubic"
//
//
//#define VK_AMD_rasterization_order 1
//#define VK_AMD_RASTERIZATION_ORDER_SPEC_VERSION 1
//#define VK_AMD_RASTERIZATION_ORDER_EXTENSION_NAME "VK_AMD_rasterization_order"
//
//
//typedef enum VkRasterizationOrderAMD {
//    VK_RASTERIZATION_ORDER_STRICT_AMD = 0,
//    VK_RASTERIZATION_ORDER_RELAXED_AMD = 1,
//    VK_RASTERIZATION_ORDER_BEGIN_RANGE_AMD = VK_RASTERIZATION_ORDER_STRICT_AMD,
//    VK_RASTERIZATION_ORDER_END_RANGE_AMD = VK_RASTERIZATION_ORDER_RELAXED_AMD,
//    VK_RASTERIZATION_ORDER_RANGE_SIZE_AMD = (VK_RASTERIZATION_ORDER_RELAXED_AMD - VK_RASTERIZATION_ORDER_STRICT_AMD + 1),
//    VK_RASTERIZATION_ORDER_MAX_ENUM_AMD = 0x7FFFFFFF
//} VkRasterizationOrderAMD;
//
//typedef struct VkPipelineRasterizationStateRasterizationOrderAMD {
//    VkStructureType            sType;
//    const void*                pNext;
//    VkRasterizationOrderAMD    rasterizationOrder;
//} VkPipelineRasterizationStateRasterizationOrderAMD;
//
//
//
//#define VK_AMD_shader_trinary_minmax 1
//#define VK_AMD_SHADER_TRINARY_MINMAX_SPEC_VERSION 1
//#define VK_AMD_SHADER_TRINARY_MINMAX_EXTENSION_NAME "VK_AMD_shader_trinary_minmax"
//
//
//#define VK_AMD_shader_explicit_vertex_parameter 1
//#define VK_AMD_SHADER_EXPLICIT_VERTEX_PARAMETER_SPEC_VERSION 1
//#define VK_AMD_SHADER_EXPLICIT_VERTEX_PARAMETER_EXTENSION_NAME "VK_AMD_shader_explicit_vertex_parameter"
//
//
//#define VK_EXT_debug_marker 1
//#define VK_EXT_DEBUG_MARKER_SPEC_VERSION  4
//#define VK_EXT_DEBUG_MARKER_EXTENSION_NAME "VK_EXT_debug_marker"
//
//typedef struct VkDebugMarkerObjectNameInfoEXT {
//    VkStructureType               sType;
//    const void*                   pNext;
//    VkDebugReportObjectTypeEXT    objectType;
//    uint64_t                      object;
//    const char*                   pObjectName;
//} VkDebugMarkerObjectNameInfoEXT;
//
//typedef struct VkDebugMarkerObjectTagInfoEXT {
//    VkStructureType               sType;
//    const void*                   pNext;
//    VkDebugReportObjectTypeEXT    objectType;
//    uint64_t                      object;
//    uint64_t                      tagName;
//    size_t                        tagSize;
//    const void*                   pTag;
//} VkDebugMarkerObjectTagInfoEXT;
//
//typedef struct VkDebugMarkerMarkerInfoEXT {
//    VkStructureType    sType;
//    const void*        pNext;
//    const char*        pMarkerName;
//    float              color[4];
//} VkDebugMarkerMarkerInfoEXT;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkDebugMarkerSetObjectTagEXT)(VkDevice device, const VkDebugMarkerObjectTagInfoEXT* pTagInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkDebugMarkerSetObjectNameEXT)(VkDevice device, const VkDebugMarkerObjectNameInfoEXT* pNameInfo);
//typedef void (VKAPI_PTR *PFN_vkCmdDebugMarkerBeginEXT)(VkCommandBuffer commandBuffer, const VkDebugMarkerMarkerInfoEXT* pMarkerInfo);
//typedef void (VKAPI_PTR *PFN_vkCmdDebugMarkerEndEXT)(VkCommandBuffer commandBuffer);
//typedef void (VKAPI_PTR *PFN_vkCmdDebugMarkerInsertEXT)(VkCommandBuffer commandBuffer, const VkDebugMarkerMarkerInfoEXT* pMarkerInfo);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkDebugMarkerSetObjectTagEXT(
//VkDevice                                    device,
//const VkDebugMarkerObjectTagInfoEXT*        pTagInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkDebugMarkerSetObjectNameEXT(
//VkDevice                                    device,
//const VkDebugMarkerObjectNameInfoEXT*       pNameInfo);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDebugMarkerBeginEXT(
//VkCommandBuffer                             commandBuffer,
//const VkDebugMarkerMarkerInfoEXT*           pMarkerInfo);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDebugMarkerEndEXT(
//VkCommandBuffer                             commandBuffer);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDebugMarkerInsertEXT(
//VkCommandBuffer                             commandBuffer,
//const VkDebugMarkerMarkerInfoEXT*           pMarkerInfo);
//#endif
//
//#define VK_AMD_gcn_shader 1
//#define VK_AMD_GCN_SHADER_SPEC_VERSION    1
//#define VK_AMD_GCN_SHADER_EXTENSION_NAME  "VK_AMD_gcn_shader"
//
//
//#define VK_NV_dedicated_allocation 1
//#define VK_NV_DEDICATED_ALLOCATION_SPEC_VERSION 1
//#define VK_NV_DEDICATED_ALLOCATION_EXTENSION_NAME "VK_NV_dedicated_allocation"
//
//typedef struct VkDedicatedAllocationImageCreateInfoNV {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkBool32           dedicatedAllocation;
//} VkDedicatedAllocationImageCreateInfoNV;
//
//typedef struct VkDedicatedAllocationBufferCreateInfoNV {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkBool32           dedicatedAllocation;
//} VkDedicatedAllocationBufferCreateInfoNV;
//
//typedef struct VkDedicatedAllocationMemoryAllocateInfoNV {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkImage            image;
//    VkBuffer           buffer;
//} VkDedicatedAllocationMemoryAllocateInfoNV;
//
//
//
//#define VK_AMD_draw_indirect_count 1
//#define VK_AMD_DRAW_INDIRECT_COUNT_SPEC_VERSION 1
//#define VK_AMD_DRAW_INDIRECT_COUNT_EXTENSION_NAME "VK_AMD_draw_indirect_count"
//
//typedef void (VKAPI_PTR *PFN_vkCmdDrawIndirectCountAMD)(VkCommandBuffer commandBuffer, VkBuffer buffer, VkDeviceSize offset, VkBuffer countBuffer, VkDeviceSize countBufferOffset, uint32_t maxDrawCount, uint32_t stride);
//typedef void (VKAPI_PTR *PFN_vkCmdDrawIndexedIndirectCountAMD)(VkCommandBuffer commandBuffer, VkBuffer buffer, VkDeviceSize offset, VkBuffer countBuffer, VkDeviceSize countBufferOffset, uint32_t maxDrawCount, uint32_t stride);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkCmdDrawIndirectCountAMD(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    buffer,
//VkDeviceSize                                offset,
//VkBuffer                                    countBuffer,
//VkDeviceSize                                countBufferOffset,
//uint32_t                                    maxDrawCount,
//uint32_t                                    stride);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDrawIndexedIndirectCountAMD(
//VkCommandBuffer                             commandBuffer,
//VkBuffer                                    buffer,
//VkDeviceSize                                offset,
//VkBuffer                                    countBuffer,
//VkDeviceSize                                countBufferOffset,
//uint32_t                                    maxDrawCount,
//uint32_t                                    stride);
//#endif
//
//#define VK_AMD_negative_viewport_height 1
//#define VK_AMD_NEGATIVE_VIEWPORT_HEIGHT_SPEC_VERSION 1
//#define VK_AMD_NEGATIVE_VIEWPORT_HEIGHT_EXTENSION_NAME "VK_AMD_negative_viewport_height"
//
//
//#define VK_AMD_gpu_shader_half_float 1
//#define VK_AMD_GPU_SHADER_HALF_FLOAT_SPEC_VERSION 1
//#define VK_AMD_GPU_SHADER_HALF_FLOAT_EXTENSION_NAME "VK_AMD_gpu_shader_half_float"
//
//
//#define VK_AMD_shader_ballot 1
//#define VK_AMD_SHADER_BALLOT_SPEC_VERSION 1
//#define VK_AMD_SHADER_BALLOT_EXTENSION_NAME "VK_AMD_shader_ballot"
//
//
//#define VK_AMD_texture_gather_bias_lod 1
//#define VK_AMD_TEXTURE_GATHER_BIAS_LOD_SPEC_VERSION 1
//#define VK_AMD_TEXTURE_GATHER_BIAS_LOD_EXTENSION_NAME "VK_AMD_texture_gather_bias_lod"
//
//typedef struct VkTextureLODGatherFormatPropertiesAMD {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           supportsTextureGatherLODBiasAMD;
//} VkTextureLODGatherFormatPropertiesAMD;
//
//
//
//#define VK_AMD_shader_info 1
//#define VK_AMD_SHADER_INFO_SPEC_VERSION   1
//#define VK_AMD_SHADER_INFO_EXTENSION_NAME "VK_AMD_shader_info"
//
//
//typedef enum VkShaderInfoTypeAMD {
//    VK_SHADER_INFO_TYPE_STATISTICS_AMD = 0,
//    VK_SHADER_INFO_TYPE_BINARY_AMD = 1,
//    VK_SHADER_INFO_TYPE_DISASSEMBLY_AMD = 2,
//    VK_SHADER_INFO_TYPE_BEGIN_RANGE_AMD = VK_SHADER_INFO_TYPE_STATISTICS_AMD,
//    VK_SHADER_INFO_TYPE_END_RANGE_AMD = VK_SHADER_INFO_TYPE_DISASSEMBLY_AMD,
//    VK_SHADER_INFO_TYPE_RANGE_SIZE_AMD = (VK_SHADER_INFO_TYPE_DISASSEMBLY_AMD - VK_SHADER_INFO_TYPE_STATISTICS_AMD + 1),
//    VK_SHADER_INFO_TYPE_MAX_ENUM_AMD = 0x7FFFFFFF
//} VkShaderInfoTypeAMD;
//
//typedef struct VkShaderResourceUsageAMD {
//    uint32_t    numUsedVgprs;
//    uint32_t    numUsedSgprs;
//    uint32_t    ldsSizePerLocalWorkGroup;
//    size_t      ldsUsageSizeInBytes;
//    size_t      scratchMemUsageInBytes;
//} VkShaderResourceUsageAMD;
//
//typedef struct VkShaderStatisticsInfoAMD {
//    VkShaderStageFlags          shaderStageMask;
//    VkShaderResourceUsageAMD    resourceUsage;
//    uint32_t                    numPhysicalVgprs;
//    uint32_t                    numPhysicalSgprs;
//    uint32_t                    numAvailableVgprs;
//    uint32_t                    numAvailableSgprs;
//    uint32_t                    computeWorkGroupSize[3];
//} VkShaderStatisticsInfoAMD;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetShaderInfoAMD)(VkDevice device, VkPipeline pipeline, VkShaderStageFlagBits shaderStage, VkShaderInfoTypeAMD infoType, size_t* pInfoSize, void* pInfo);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetShaderInfoAMD(
//VkDevice                                    device,
//VkPipeline                                  pipeline,
//VkShaderStageFlagBits                       shaderStage,
//VkShaderInfoTypeAMD                         infoType,
//size_t*                                     pInfoSize,
//void*                                       pInfo);
//#endif
//
//#define VK_AMD_shader_image_load_store_lod 1
//#define VK_AMD_SHADER_IMAGE_LOAD_STORE_LOD_SPEC_VERSION 1
//#define VK_AMD_SHADER_IMAGE_LOAD_STORE_LOD_EXTENSION_NAME "VK_AMD_shader_image_load_store_lod"
//
//
//#define VK_KHX_multiview 1
//#define VK_KHX_MULTIVIEW_SPEC_VERSION     1
//#define VK_KHX_MULTIVIEW_EXTENSION_NAME   "VK_KHX_multiview"
//
//typedef struct VkRenderPassMultiviewCreateInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           subpassCount;
//    const uint32_t*    pViewMasks;
//    uint32_t           dependencyCount;
//    const int32_t*     pViewOffsets;
//    uint32_t           correlationMaskCount;
//    const uint32_t*    pCorrelationMasks;
//} VkRenderPassMultiviewCreateInfoKHX;
//
//typedef struct VkPhysicalDeviceMultiviewFeaturesKHX {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           multiview;
//    VkBool32           multiviewGeometryShader;
//    VkBool32           multiviewTessellationShader;
//} VkPhysicalDeviceMultiviewFeaturesKHX;
//
//typedef struct VkPhysicalDeviceMultiviewPropertiesKHX {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           maxMultiviewViewCount;
//    uint32_t           maxMultiviewInstanceIndex;
//} VkPhysicalDeviceMultiviewPropertiesKHX;
//
//
//
//#define VK_IMG_format_pvrtc 1
//#define VK_IMG_FORMAT_PVRTC_SPEC_VERSION  1
//#define VK_IMG_FORMAT_PVRTC_EXTENSION_NAME "VK_IMG_format_pvrtc"
//
//
//#define VK_NV_external_memory_capabilities 1
//#define VK_NV_EXTERNAL_MEMORY_CAPABILITIES_SPEC_VERSION 1
//#define VK_NV_EXTERNAL_MEMORY_CAPABILITIES_EXTENSION_NAME "VK_NV_external_memory_capabilities"
//
//
//typedef enum VkExternalMemoryHandleTypeFlagBitsNV {
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_WIN32_BIT_NV = 0x00000001,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_WIN32_KMT_BIT_NV = 0x00000002,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D11_IMAGE_BIT_NV = 0x00000004,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D11_IMAGE_KMT_BIT_NV = 0x00000008,
//    VK_EXTERNAL_MEMORY_HANDLE_TYPE_FLAG_BITS_MAX_ENUM_NV = 0x7FFFFFFF
//} VkExternalMemoryHandleTypeFlagBitsNV;
//typedef VkFlags VkExternalMemoryHandleTypeFlagsNV;
//
//typedef enum VkExternalMemoryFeatureFlagBitsNV {
//    VK_EXTERNAL_MEMORY_FEATURE_DEDICATED_ONLY_BIT_NV = 0x00000001,
//    VK_EXTERNAL_MEMORY_FEATURE_EXPORTABLE_BIT_NV = 0x00000002,
//    VK_EXTERNAL_MEMORY_FEATURE_IMPORTABLE_BIT_NV = 0x00000004,
//    VK_EXTERNAL_MEMORY_FEATURE_FLAG_BITS_MAX_ENUM_NV = 0x7FFFFFFF
//} VkExternalMemoryFeatureFlagBitsNV;
//typedef VkFlags VkExternalMemoryFeatureFlagsNV;
//
//typedef struct VkExternalImageFormatPropertiesNV {
//    VkImageFormatProperties              imageFormatProperties;
//    VkExternalMemoryFeatureFlagsNV       externalMemoryFeatures;
//    VkExternalMemoryHandleTypeFlagsNV    exportFromImportedHandleTypes;
//    VkExternalMemoryHandleTypeFlagsNV    compatibleHandleTypes;
//} VkExternalImageFormatPropertiesNV;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceExternalImageFormatPropertiesNV)(VkPhysicalDevice physicalDevice, VkFormat format, VkImageType type, VkImageTiling tiling, VkImageUsageFlags usage, VkImageCreateFlags flags, VkExternalMemoryHandleTypeFlagsNV externalHandleType, VkExternalImageFormatPropertiesNV* pExternalImageFormatProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceExternalImageFormatPropertiesNV(
//VkPhysicalDevice                            physicalDevice,
//VkFormat                                    format,
//VkImageType                                 type,
//VkImageTiling                               tiling,
//VkImageUsageFlags                           usage,
//VkImageCreateFlags                          flags,
//VkExternalMemoryHandleTypeFlagsNV           externalHandleType,
//VkExternalImageFormatPropertiesNV*          pExternalImageFormatProperties);
//#endif
//
//#define VK_NV_external_memory 1
//#define VK_NV_EXTERNAL_MEMORY_SPEC_VERSION 1
//#define VK_NV_EXTERNAL_MEMORY_EXTENSION_NAME "VK_NV_external_memory"
//
//typedef struct VkExternalMemoryImageCreateInfoNV {
//    VkStructureType                      sType;
//    const void*                          pNext;
//    VkExternalMemoryHandleTypeFlagsNV    handleTypes;
//} VkExternalMemoryImageCreateInfoNV;
//
//typedef struct VkExportMemoryAllocateInfoNV {
//    VkStructureType                      sType;
//    const void*                          pNext;
//    VkExternalMemoryHandleTypeFlagsNV    handleTypes;
//} VkExportMemoryAllocateInfoNV;
//
//
//
//#ifdef VK_USE_PLATFORM_WIN32_KHR
//#define VK_NV_external_memory_win32 1
//#define VK_NV_EXTERNAL_MEMORY_WIN32_SPEC_VERSION 1
//#define VK_NV_EXTERNAL_MEMORY_WIN32_EXTENSION_NAME "VK_NV_external_memory_win32"
//
//typedef struct VkImportMemoryWin32HandleInfoNV {
//    VkStructureType                      sType;
//    const void*                          pNext;
//    VkExternalMemoryHandleTypeFlagsNV    handleType;
//    HANDLE                               handle;
//} VkImportMemoryWin32HandleInfoNV;
//
//typedef struct VkExportMemoryWin32HandleInfoNV {
//    VkStructureType               sType;
//    const void*                   pNext;
//    const SECURITY_ATTRIBUTES*    pAttributes;
//    DWORD                         dwAccess;
//} VkExportMemoryWin32HandleInfoNV;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetMemoryWin32HandleNV)(VkDevice device, VkDeviceMemory memory, VkExternalMemoryHandleTypeFlagsNV handleType, HANDLE* pHandle);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetMemoryWin32HandleNV(
//VkDevice                                    device,
//VkDeviceMemory                              memory,
//VkExternalMemoryHandleTypeFlagsNV           handleType,
//HANDLE*                                     pHandle);
//#endif
//#endif /* VK_USE_PLATFORM_WIN32_KHR */
//
//#ifdef VK_USE_PLATFORM_WIN32_KHR
//#define VK_NV_win32_keyed_mutex 1
//#define VK_NV_WIN32_KEYED_MUTEX_SPEC_VERSION 1
//#define VK_NV_WIN32_KEYED_MUTEX_EXTENSION_NAME "VK_NV_win32_keyed_mutex"
//
//typedef struct VkWin32KeyedMutexAcquireReleaseInfoNV {
//    VkStructureType          sType;
//    const void*              pNext;
//    uint32_t                 acquireCount;
//    const VkDeviceMemory*    pAcquireSyncs;
//    const uint64_t*          pAcquireKeys;
//    const uint32_t*          pAcquireTimeoutMilliseconds;
//    uint32_t                 releaseCount;
//    const VkDeviceMemory*    pReleaseSyncs;
//    const uint64_t*          pReleaseKeys;
//} VkWin32KeyedMutexAcquireReleaseInfoNV;
//
//
//#endif /* VK_USE_PLATFORM_WIN32_KHR */
//
//#define VK_KHX_device_group 1
//#define VK_KHX_DEVICE_GROUP_SPEC_VERSION  2
//#define VK_KHX_DEVICE_GROUP_EXTENSION_NAME "VK_KHX_device_group"
//#define VK_MAX_DEVICE_GROUP_SIZE_KHX      32
//
//
//typedef enum VkPeerMemoryFeatureFlagBitsKHX {
//    VK_PEER_MEMORY_FEATURE_COPY_SRC_BIT_KHX = 0x00000001,
//    VK_PEER_MEMORY_FEATURE_COPY_DST_BIT_KHX = 0x00000002,
//    VK_PEER_MEMORY_FEATURE_GENERIC_SRC_BIT_KHX = 0x00000004,
//    VK_PEER_MEMORY_FEATURE_GENERIC_DST_BIT_KHX = 0x00000008,
//    VK_PEER_MEMORY_FEATURE_FLAG_BITS_MAX_ENUM_KHX = 0x7FFFFFFF
//} VkPeerMemoryFeatureFlagBitsKHX;
//typedef VkFlags VkPeerMemoryFeatureFlagsKHX;
//
//typedef enum VkMemoryAllocateFlagBitsKHX {
//    VK_MEMORY_ALLOCATE_DEVICE_MASK_BIT_KHX = 0x00000001,
//    VK_MEMORY_ALLOCATE_FLAG_BITS_MAX_ENUM_KHX = 0x7FFFFFFF
//} VkMemoryAllocateFlagBitsKHX;
//typedef VkFlags VkMemoryAllocateFlagsKHX;
//
//typedef enum VkDeviceGroupPresentModeFlagBitsKHX {
//    VK_DEVICE_GROUP_PRESENT_MODE_LOCAL_BIT_KHX = 0x00000001,
//    VK_DEVICE_GROUP_PRESENT_MODE_REMOTE_BIT_KHX = 0x00000002,
//    VK_DEVICE_GROUP_PRESENT_MODE_SUM_BIT_KHX = 0x00000004,
//    VK_DEVICE_GROUP_PRESENT_MODE_LOCAL_MULTI_DEVICE_BIT_KHX = 0x00000008,
//    VK_DEVICE_GROUP_PRESENT_MODE_FLAG_BITS_MAX_ENUM_KHX = 0x7FFFFFFF
//} VkDeviceGroupPresentModeFlagBitsKHX;
//typedef VkFlags VkDeviceGroupPresentModeFlagsKHX;
//
//typedef struct VkMemoryAllocateFlagsInfoKHX {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkMemoryAllocateFlagsKHX    flags;
//    uint32_t                    deviceMask;
//} VkMemoryAllocateFlagsInfoKHX;
//
//typedef struct VkDeviceGroupRenderPassBeginInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           deviceMask;
//    uint32_t           deviceRenderAreaCount;
//    const VkRect2D*    pDeviceRenderAreas;
//} VkDeviceGroupRenderPassBeginInfoKHX;
//
//typedef struct VkDeviceGroupCommandBufferBeginInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           deviceMask;
//} VkDeviceGroupCommandBufferBeginInfoKHX;
//
//typedef struct VkDeviceGroupSubmitInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           waitSemaphoreCount;
//    const uint32_t*    pWaitSemaphoreDeviceIndices;
//    uint32_t           commandBufferCount;
//    const uint32_t*    pCommandBufferDeviceMasks;
//    uint32_t           signalSemaphoreCount;
//    const uint32_t*    pSignalSemaphoreDeviceIndices;
//} VkDeviceGroupSubmitInfoKHX;
//
//typedef struct VkDeviceGroupBindSparseInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           resourceDeviceIndex;
//    uint32_t           memoryDeviceIndex;
//} VkDeviceGroupBindSparseInfoKHX;
//
//typedef struct VkBindBufferMemoryDeviceGroupInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           deviceIndexCount;
//    const uint32_t*    pDeviceIndices;
//} VkBindBufferMemoryDeviceGroupInfoKHX;
//
//typedef struct VkBindImageMemoryDeviceGroupInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           deviceIndexCount;
//    const uint32_t*    pDeviceIndices;
//    uint32_t           SFRRectCount;
//    const VkRect2D*    pSFRRects;
//} VkBindImageMemoryDeviceGroupInfoKHX;
//
//typedef struct VkDeviceGroupPresentCapabilitiesKHX {
//    VkStructureType                     sType;
//    const void*                         pNext;
//    uint32_t                            presentMask[VK_MAX_DEVICE_GROUP_SIZE_KHX];
//    VkDeviceGroupPresentModeFlagsKHX    modes;
//} VkDeviceGroupPresentCapabilitiesKHX;
//
//typedef struct VkImageSwapchainCreateInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkSwapchainKHR     swapchain;
//} VkImageSwapchainCreateInfoKHX;
//
//typedef struct VkBindImageMemorySwapchainInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkSwapchainKHR     swapchain;
//    uint32_t           imageIndex;
//} VkBindImageMemorySwapchainInfoKHX;
//
//typedef struct VkAcquireNextImageInfoKHX {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkSwapchainKHR     swapchain;
//    uint64_t           timeout;
//    VkSemaphore        semaphore;
//    VkFence            fence;
//    uint32_t           deviceMask;
//} VkAcquireNextImageInfoKHX;
//
//typedef struct VkDeviceGroupPresentInfoKHX {
//    VkStructureType                        sType;
//    const void*                            pNext;
//    uint32_t                               swapchainCount;
//    const uint32_t*                        pDeviceMasks;
//    VkDeviceGroupPresentModeFlagBitsKHX    mode;
//} VkDeviceGroupPresentInfoKHX;
//
//typedef struct VkDeviceGroupSwapchainCreateInfoKHX {
//    VkStructureType                     sType;
//    const void*                         pNext;
//    VkDeviceGroupPresentModeFlagsKHX    modes;
//} VkDeviceGroupSwapchainCreateInfoKHX;
//
//
//typedef void (VKAPI_PTR *PFN_vkGetDeviceGroupPeerMemoryFeaturesKHX)(VkDevice device, uint32_t heapIndex, uint32_t localDeviceIndex, uint32_t remoteDeviceIndex, VkPeerMemoryFeatureFlagsKHX* pPeerMemoryFeatures);
//typedef void (VKAPI_PTR *PFN_vkCmdSetDeviceMaskKHX)(VkCommandBuffer commandBuffer, uint32_t deviceMask);
//typedef void (VKAPI_PTR *PFN_vkCmdDispatchBaseKHX)(VkCommandBuffer commandBuffer, uint32_t baseGroupX, uint32_t baseGroupY, uint32_t baseGroupZ, uint32_t groupCountX, uint32_t groupCountY, uint32_t groupCountZ);
//typedef VkResult (VKAPI_PTR *PFN_vkGetDeviceGroupPresentCapabilitiesKHX)(VkDevice device, VkDeviceGroupPresentCapabilitiesKHX* pDeviceGroupPresentCapabilities);
//typedef VkResult (VKAPI_PTR *PFN_vkGetDeviceGroupSurfacePresentModesKHX)(VkDevice device, VkSurfaceKHR surface, VkDeviceGroupPresentModeFlagsKHX* pModes);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDevicePresentRectanglesKHX)(VkPhysicalDevice physicalDevice, VkSurfaceKHR surface, uint32_t* pRectCount, VkRect2D* pRects);
//typedef VkResult (VKAPI_PTR *PFN_vkAcquireNextImage2KHX)(VkDevice device, const VkAcquireNextImageInfoKHX* pAcquireInfo, uint32_t* pImageIndex);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkGetDeviceGroupPeerMemoryFeaturesKHX(
//VkDevice                                    device,
//uint32_t                                    heapIndex,
//uint32_t                                    localDeviceIndex,
//uint32_t                                    remoteDeviceIndex,
//VkPeerMemoryFeatureFlagsKHX*                pPeerMemoryFeatures);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdSetDeviceMaskKHX(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    deviceMask);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdDispatchBaseKHX(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    baseGroupX,
//uint32_t                                    baseGroupY,
//uint32_t                                    baseGroupZ,
//uint32_t                                    groupCountX,
//uint32_t                                    groupCountY,
//uint32_t                                    groupCountZ);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetDeviceGroupPresentCapabilitiesKHX(
//VkDevice                                    device,
//VkDeviceGroupPresentCapabilitiesKHX*        pDeviceGroupPresentCapabilities);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetDeviceGroupSurfacePresentModesKHX(
//VkDevice                                    device,
//VkSurfaceKHR                                surface,
//VkDeviceGroupPresentModeFlagsKHX*           pModes);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDevicePresentRectanglesKHX(
//VkPhysicalDevice                            physicalDevice,
//VkSurfaceKHR                                surface,
//uint32_t*                                   pRectCount,
//VkRect2D*                                   pRects);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkAcquireNextImage2KHX(
//VkDevice                                    device,
//const VkAcquireNextImageInfoKHX*            pAcquireInfo,
//uint32_t*                                   pImageIndex);
//#endif
//
//#define VK_EXT_validation_flags 1
//#define VK_EXT_VALIDATION_FLAGS_SPEC_VERSION 1
//#define VK_EXT_VALIDATION_FLAGS_EXTENSION_NAME "VK_EXT_validation_flags"
//
//
//typedef enum VkValidationCheckEXT {
//    VK_VALIDATION_CHECK_ALL_EXT = 0,
//    VK_VALIDATION_CHECK_SHADERS_EXT = 1,
//    VK_VALIDATION_CHECK_BEGIN_RANGE_EXT = VK_VALIDATION_CHECK_ALL_EXT,
//    VK_VALIDATION_CHECK_END_RANGE_EXT = VK_VALIDATION_CHECK_SHADERS_EXT,
//    VK_VALIDATION_CHECK_RANGE_SIZE_EXT = (VK_VALIDATION_CHECK_SHADERS_EXT - VK_VALIDATION_CHECK_ALL_EXT + 1),
//    VK_VALIDATION_CHECK_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkValidationCheckEXT;
//
//typedef struct VkValidationFlagsEXT {
//    VkStructureType          sType;
//    const void*              pNext;
//    uint32_t                 disabledValidationCheckCount;
//    VkValidationCheckEXT*    pDisabledValidationChecks;
//} VkValidationFlagsEXT;
//
//
//
//#ifdef VK_USE_PLATFORM_VI_NN
//#define VK_NN_vi_surface 1
//#define VK_NN_VI_SURFACE_SPEC_VERSION     1
//#define VK_NN_VI_SURFACE_EXTENSION_NAME   "VK_NN_vi_surface"
//
//typedef VkFlags VkViSurfaceCreateFlagsNN;
//
//typedef struct VkViSurfaceCreateInfoNN {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkViSurfaceCreateFlagsNN    flags;
//    void*                       window;
//} VkViSurfaceCreateInfoNN;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateViSurfaceNN)(VkInstance instance, const VkViSurfaceCreateInfoNN* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateViSurfaceNN(
//VkInstance                                  instance,
//const VkViSurfaceCreateInfoNN*              pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//#endif
//#endif /* VK_USE_PLATFORM_VI_NN */
//
//#define VK_EXT_shader_subgroup_ballot 1
//#define VK_EXT_SHADER_SUBGROUP_BALLOT_SPEC_VERSION 1
//#define VK_EXT_SHADER_SUBGROUP_BALLOT_EXTENSION_NAME "VK_EXT_shader_subgroup_ballot"
//
//
//#define VK_EXT_shader_subgroup_vote 1
//#define VK_EXT_SHADER_SUBGROUP_VOTE_SPEC_VERSION 1
//#define VK_EXT_SHADER_SUBGROUP_VOTE_EXTENSION_NAME "VK_EXT_shader_subgroup_vote"
//
//
//#define VK_KHX_device_group_creation 1
//#define VK_KHX_DEVICE_GROUP_CREATION_SPEC_VERSION 1
//#define VK_KHX_DEVICE_GROUP_CREATION_EXTENSION_NAME "VK_KHX_device_group_creation"
//
//typedef struct VkPhysicalDeviceGroupPropertiesKHX {
//    VkStructureType     sType;
//    void*               pNext;
//    uint32_t            physicalDeviceCount;
//    VkPhysicalDevice    physicalDevices[VK_MAX_DEVICE_GROUP_SIZE_KHX];
//    VkBool32            subsetAllocation;
//} VkPhysicalDeviceGroupPropertiesKHX;
//
//typedef struct VkDeviceGroupDeviceCreateInfoKHX {
//    VkStructureType            sType;
//    const void*                pNext;
//    uint32_t                   physicalDeviceCount;
//    const VkPhysicalDevice*    pPhysicalDevices;
//} VkDeviceGroupDeviceCreateInfoKHX;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkEnumeratePhysicalDeviceGroupsKHX)(VkInstance instance, uint32_t* pPhysicalDeviceGroupCount, VkPhysicalDeviceGroupPropertiesKHX* pPhysicalDeviceGroupProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkEnumeratePhysicalDeviceGroupsKHX(
//VkInstance                                  instance,
//uint32_t*                                   pPhysicalDeviceGroupCount,
//VkPhysicalDeviceGroupPropertiesKHX*         pPhysicalDeviceGroupProperties);
//#endif
//
//#define VK_NVX_device_generated_commands 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkObjectTableNVX)
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkIndirectCommandsLayoutNVX)
//
//#define VK_NVX_DEVICE_GENERATED_COMMANDS_SPEC_VERSION 3
//#define VK_NVX_DEVICE_GENERATED_COMMANDS_EXTENSION_NAME "VK_NVX_device_generated_commands"
//
//
//typedef enum VkIndirectCommandsTokenTypeNVX {
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_PIPELINE_NVX = 0,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_DESCRIPTOR_SET_NVX = 1,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_INDEX_BUFFER_NVX = 2,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_VERTEX_BUFFER_NVX = 3,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_PUSH_CONSTANT_NVX = 4,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_DRAW_INDEXED_NVX = 5,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_DRAW_NVX = 6,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_DISPATCH_NVX = 7,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_BEGIN_RANGE_NVX = VK_INDIRECT_COMMANDS_TOKEN_TYPE_PIPELINE_NVX,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_END_RANGE_NVX = VK_INDIRECT_COMMANDS_TOKEN_TYPE_DISPATCH_NVX,
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_RANGE_SIZE_NVX = (VK_INDIRECT_COMMANDS_TOKEN_TYPE_DISPATCH_NVX - VK_INDIRECT_COMMANDS_TOKEN_TYPE_PIPELINE_NVX + 1),
//    VK_INDIRECT_COMMANDS_TOKEN_TYPE_MAX_ENUM_NVX = 0x7FFFFFFF
//} VkIndirectCommandsTokenTypeNVX;
//
//typedef enum VkObjectEntryTypeNVX {
//    VK_OBJECT_ENTRY_TYPE_DESCRIPTOR_SET_NVX = 0,
//    VK_OBJECT_ENTRY_TYPE_PIPELINE_NVX = 1,
//    VK_OBJECT_ENTRY_TYPE_INDEX_BUFFER_NVX = 2,
//    VK_OBJECT_ENTRY_TYPE_VERTEX_BUFFER_NVX = 3,
//    VK_OBJECT_ENTRY_TYPE_PUSH_CONSTANT_NVX = 4,
//    VK_OBJECT_ENTRY_TYPE_BEGIN_RANGE_NVX = VK_OBJECT_ENTRY_TYPE_DESCRIPTOR_SET_NVX,
//    VK_OBJECT_ENTRY_TYPE_END_RANGE_NVX = VK_OBJECT_ENTRY_TYPE_PUSH_CONSTANT_NVX,
//    VK_OBJECT_ENTRY_TYPE_RANGE_SIZE_NVX = (VK_OBJECT_ENTRY_TYPE_PUSH_CONSTANT_NVX - VK_OBJECT_ENTRY_TYPE_DESCRIPTOR_SET_NVX + 1),
//    VK_OBJECT_ENTRY_TYPE_MAX_ENUM_NVX = 0x7FFFFFFF
//} VkObjectEntryTypeNVX;
//
//
//typedef enum VkIndirectCommandsLayoutUsageFlagBitsNVX {
//    VK_INDIRECT_COMMANDS_LAYOUT_USAGE_UNORDERED_SEQUENCES_BIT_NVX = 0x00000001,
//    VK_INDIRECT_COMMANDS_LAYOUT_USAGE_SPARSE_SEQUENCES_BIT_NVX = 0x00000002,
//    VK_INDIRECT_COMMANDS_LAYOUT_USAGE_EMPTY_EXECUTIONS_BIT_NVX = 0x00000004,
//    VK_INDIRECT_COMMANDS_LAYOUT_USAGE_INDEXED_SEQUENCES_BIT_NVX = 0x00000008,
//    VK_INDIRECT_COMMANDS_LAYOUT_USAGE_FLAG_BITS_MAX_ENUM_NVX = 0x7FFFFFFF
//} VkIndirectCommandsLayoutUsageFlagBitsNVX;
//typedef VkFlags VkIndirectCommandsLayoutUsageFlagsNVX;
//
//typedef enum VkObjectEntryUsageFlagBitsNVX {
//    VK_OBJECT_ENTRY_USAGE_GRAPHICS_BIT_NVX = 0x00000001,
//    VK_OBJECT_ENTRY_USAGE_COMPUTE_BIT_NVX = 0x00000002,
//    VK_OBJECT_ENTRY_USAGE_FLAG_BITS_MAX_ENUM_NVX = 0x7FFFFFFF
//} VkObjectEntryUsageFlagBitsNVX;
//typedef VkFlags VkObjectEntryUsageFlagsNVX;
//
//typedef struct VkDeviceGeneratedCommandsFeaturesNVX {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkBool32           computeBindingPointSupport;
//} VkDeviceGeneratedCommandsFeaturesNVX;
//
//typedef struct VkDeviceGeneratedCommandsLimitsNVX {
//    VkStructureType    sType;
//    const void*        pNext;
//    uint32_t           maxIndirectCommandsLayoutTokenCount;
//    uint32_t           maxObjectEntryCounts;
//    uint32_t           minSequenceCountBufferOffsetAlignment;
//    uint32_t           minSequenceIndexBufferOffsetAlignment;
//    uint32_t           minCommandsTokenBufferOffsetAlignment;
//} VkDeviceGeneratedCommandsLimitsNVX;
//
//typedef struct VkIndirectCommandsTokenNVX {
//    VkIndirectCommandsTokenTypeNVX    tokenType;
//    VkBuffer                          buffer;
//    VkDeviceSize                      offset;
//} VkIndirectCommandsTokenNVX;
//
//typedef struct VkIndirectCommandsLayoutTokenNVX {
//    VkIndirectCommandsTokenTypeNVX    tokenType;
//    uint32_t                          bindingUnit;
//    uint32_t                          dynamicCount;
//    uint32_t                          divisor;
//} VkIndirectCommandsLayoutTokenNVX;
//
//typedef struct VkIndirectCommandsLayoutCreateInfoNVX {
//    VkStructureType                            sType;
//    const void*                                pNext;
//    VkPipelineBindPoint                        pipelineBindPoint;
//    VkIndirectCommandsLayoutUsageFlagsNVX      flags;
//    uint32_t                                   tokenCount;
//    const VkIndirectCommandsLayoutTokenNVX*    pTokens;
//} VkIndirectCommandsLayoutCreateInfoNVX;
//
//typedef struct VkCmdProcessCommandsInfoNVX {
//    VkStructureType                      sType;
//    const void*                          pNext;
//    VkObjectTableNVX                     objectTable;
//    VkIndirectCommandsLayoutNVX          indirectCommandsLayout;
//    uint32_t                             indirectCommandsTokenCount;
//    const VkIndirectCommandsTokenNVX*    pIndirectCommandsTokens;
//    uint32_t                             maxSequencesCount;
//    VkCommandBuffer                      targetCommandBuffer;
//    VkBuffer                             sequencesCountBuffer;
//    VkDeviceSize                         sequencesCountOffset;
//    VkBuffer                             sequencesIndexBuffer;
//    VkDeviceSize                         sequencesIndexOffset;
//} VkCmdProcessCommandsInfoNVX;
//
//typedef struct VkCmdReserveSpaceForCommandsInfoNVX {
//    VkStructureType                sType;
//    const void*                    pNext;
//    VkObjectTableNVX               objectTable;
//    VkIndirectCommandsLayoutNVX    indirectCommandsLayout;
//    uint32_t                       maxSequencesCount;
//} VkCmdReserveSpaceForCommandsInfoNVX;
//
//typedef struct VkObjectTableCreateInfoNVX {
//    VkStructureType                      sType;
//    const void*                          pNext;
//    uint32_t                             objectCount;
//    const VkObjectEntryTypeNVX*          pObjectEntryTypes;
//    const uint32_t*                      pObjectEntryCounts;
//    const VkObjectEntryUsageFlagsNVX*    pObjectEntryUsageFlags;
//    uint32_t                             maxUniformBuffersPerDescriptor;
//    uint32_t                             maxStorageBuffersPerDescriptor;
//    uint32_t                             maxStorageImagesPerDescriptor;
//    uint32_t                             maxSampledImagesPerDescriptor;
//    uint32_t                             maxPipelineLayouts;
//} VkObjectTableCreateInfoNVX;
//
//typedef struct VkObjectTableEntryNVX {
//    VkObjectEntryTypeNVX          type;
//    VkObjectEntryUsageFlagsNVX    flags;
//} VkObjectTableEntryNVX;
//
//typedef struct VkObjectTablePipelineEntryNVX {
//    VkObjectEntryTypeNVX          type;
//    VkObjectEntryUsageFlagsNVX    flags;
//    VkPipeline                    pipeline;
//} VkObjectTablePipelineEntryNVX;
//
//typedef struct VkObjectTableDescriptorSetEntryNVX {
//    VkObjectEntryTypeNVX          type;
//    VkObjectEntryUsageFlagsNVX    flags;
//    VkPipelineLayout              pipelineLayout;
//    VkDescriptorSet               descriptorSet;
//} VkObjectTableDescriptorSetEntryNVX;
//
//typedef struct VkObjectTableVertexBufferEntryNVX {
//    VkObjectEntryTypeNVX          type;
//    VkObjectEntryUsageFlagsNVX    flags;
//    VkBuffer                      buffer;
//} VkObjectTableVertexBufferEntryNVX;
//
//typedef struct VkObjectTableIndexBufferEntryNVX {
//    VkObjectEntryTypeNVX          type;
//    VkObjectEntryUsageFlagsNVX    flags;
//    VkBuffer                      buffer;
//    VkIndexType                   indexType;
//} VkObjectTableIndexBufferEntryNVX;
//
//typedef struct VkObjectTablePushConstantEntryNVX {
//    VkObjectEntryTypeNVX          type;
//    VkObjectEntryUsageFlagsNVX    flags;
//    VkPipelineLayout              pipelineLayout;
//    VkShaderStageFlags            stageFlags;
//} VkObjectTablePushConstantEntryNVX;
//
//
//typedef void (VKAPI_PTR *PFN_vkCmdProcessCommandsNVX)(VkCommandBuffer commandBuffer, const VkCmdProcessCommandsInfoNVX* pProcessCommandsInfo);
//typedef void (VKAPI_PTR *PFN_vkCmdReserveSpaceForCommandsNVX)(VkCommandBuffer commandBuffer, const VkCmdReserveSpaceForCommandsInfoNVX* pReserveSpaceInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateIndirectCommandsLayoutNVX)(VkDevice device, const VkIndirectCommandsLayoutCreateInfoNVX* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkIndirectCommandsLayoutNVX* pIndirectCommandsLayout);
//typedef void (VKAPI_PTR *PFN_vkDestroyIndirectCommandsLayoutNVX)(VkDevice device, VkIndirectCommandsLayoutNVX indirectCommandsLayout, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkCreateObjectTableNVX)(VkDevice device, const VkObjectTableCreateInfoNVX* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkObjectTableNVX* pObjectTable);
//typedef void (VKAPI_PTR *PFN_vkDestroyObjectTableNVX)(VkDevice device, VkObjectTableNVX objectTable, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkRegisterObjectsNVX)(VkDevice device, VkObjectTableNVX objectTable, uint32_t objectCount, const VkObjectTableEntryNVX* const*    ppObjectTableEntries, const uint32_t* pObjectIndices);
//typedef VkResult (VKAPI_PTR *PFN_vkUnregisterObjectsNVX)(VkDevice device, VkObjectTableNVX objectTable, uint32_t objectCount, const VkObjectEntryTypeNVX* pObjectEntryTypes, const uint32_t* pObjectIndices);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceGeneratedCommandsPropertiesNVX)(VkPhysicalDevice physicalDevice, VkDeviceGeneratedCommandsFeaturesNVX* pFeatures, VkDeviceGeneratedCommandsLimitsNVX* pLimits);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkCmdProcessCommandsNVX(
//VkCommandBuffer                             commandBuffer,
//const VkCmdProcessCommandsInfoNVX*          pProcessCommandsInfo);
//
//VKAPI_ATTR void VKAPI_CALL vkCmdReserveSpaceForCommandsNVX(
//VkCommandBuffer                             commandBuffer,
//const VkCmdReserveSpaceForCommandsInfoNVX*  pReserveSpaceInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateIndirectCommandsLayoutNVX(
//VkDevice                                    device,
//const VkIndirectCommandsLayoutCreateInfoNVX* pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkIndirectCommandsLayoutNVX*                pIndirectCommandsLayout);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyIndirectCommandsLayoutNVX(
//VkDevice                                    device,
//VkIndirectCommandsLayoutNVX                 indirectCommandsLayout,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateObjectTableNVX(
//VkDevice                                    device,
//const VkObjectTableCreateInfoNVX*           pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkObjectTableNVX*                           pObjectTable);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyObjectTableNVX(
//VkDevice                                    device,
//VkObjectTableNVX                            objectTable,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkRegisterObjectsNVX(
//VkDevice                                    device,
//VkObjectTableNVX                            objectTable,
//uint32_t                                    objectCount,
//const VkObjectTableEntryNVX* const*         ppObjectTableEntries,
//const uint32_t*                             pObjectIndices);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkUnregisterObjectsNVX(
//VkDevice                                    device,
//VkObjectTableNVX                            objectTable,
//uint32_t                                    objectCount,
//const VkObjectEntryTypeNVX*                 pObjectEntryTypes,
//const uint32_t*                             pObjectIndices);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceGeneratedCommandsPropertiesNVX(
//VkPhysicalDevice                            physicalDevice,
//VkDeviceGeneratedCommandsFeaturesNVX*       pFeatures,
//VkDeviceGeneratedCommandsLimitsNVX*         pLimits);
//#endif
//
//#define VK_NV_clip_space_w_scaling 1
//#define VK_NV_CLIP_SPACE_W_SCALING_SPEC_VERSION 1
//#define VK_NV_CLIP_SPACE_W_SCALING_EXTENSION_NAME "VK_NV_clip_space_w_scaling"
//
//typedef struct VkViewportWScalingNV {
//    float    xcoeff;
//    float    ycoeff;
//} VkViewportWScalingNV;
//
//typedef struct VkPipelineViewportWScalingStateCreateInfoNV {
//    VkStructureType                sType;
//    const void*                    pNext;
//    VkBool32                       viewportWScalingEnable;
//    uint32_t                       viewportCount;
//    const VkViewportWScalingNV*    pViewportWScalings;
//} VkPipelineViewportWScalingStateCreateInfoNV;
//
//
//typedef void (VKAPI_PTR *PFN_vkCmdSetViewportWScalingNV)(VkCommandBuffer commandBuffer, uint32_t firstViewport, uint32_t viewportCount, const VkViewportWScalingNV* pViewportWScalings);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkCmdSetViewportWScalingNV(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    firstViewport,
//uint32_t                                    viewportCount,
//const VkViewportWScalingNV*                 pViewportWScalings);
//#endif
//
//#define VK_EXT_direct_mode_display 1
//#define VK_EXT_DIRECT_MODE_DISPLAY_SPEC_VERSION 1
//#define VK_EXT_DIRECT_MODE_DISPLAY_EXTENSION_NAME "VK_EXT_direct_mode_display"
//
//typedef VkResult (VKAPI_PTR *PFN_vkReleaseDisplayEXT)(VkPhysicalDevice physicalDevice, VkDisplayKHR display);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkReleaseDisplayEXT(
//VkPhysicalDevice                            physicalDevice,
//VkDisplayKHR                                display);
//#endif
//
//#ifdef VK_USE_PLATFORM_XLIB_XRANDR_EXT
//#define VK_EXT_acquire_xlib_display 1
//#include <X11/extensions/Xrandr.h>
//
//#define VK_EXT_ACQUIRE_XLIB_DISPLAY_SPEC_VERSION 1
//#define VK_EXT_ACQUIRE_XLIB_DISPLAY_EXTENSION_NAME "VK_EXT_acquire_xlib_display"
//
//typedef VkResult (VKAPI_PTR *PFN_vkAcquireXlibDisplayEXT)(VkPhysicalDevice physicalDevice, Display* dpy, VkDisplayKHR display);
//typedef VkResult (VKAPI_PTR *PFN_vkGetRandROutputDisplayEXT)(VkPhysicalDevice physicalDevice, Display* dpy, RROutput rrOutput, VkDisplayKHR* pDisplay);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkAcquireXlibDisplayEXT(
//VkPhysicalDevice                            physicalDevice,
//Display*                                    dpy,
//VkDisplayKHR                                display);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetRandROutputDisplayEXT(
//VkPhysicalDevice                            physicalDevice,
//Display*                                    dpy,
//RROutput                                    rrOutput,
//VkDisplayKHR*                               pDisplay);
//#endif
//#endif /* VK_USE_PLATFORM_XLIB_XRANDR_EXT */
//
//#define VK_EXT_display_surface_counter 1
//#define VK_EXT_DISPLAY_SURFACE_COUNTER_SPEC_VERSION 1
//#define VK_EXT_DISPLAY_SURFACE_COUNTER_EXTENSION_NAME "VK_EXT_display_surface_counter"
//#define SURFACE_CAPABILITIES2_EXT SURFACE_CAPABILITIES_2_EXT
//
//
//typedef enum VkSurfaceCounterFlagBitsEXT {
//    VK_SURFACE_COUNTER_VBLANK_EXT = 0x00000001,
//    VK_SURFACE_COUNTER_FLAG_BITS_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkSurfaceCounterFlagBitsEXT;
//typedef VkFlags VkSurfaceCounterFlagsEXT;
//
//typedef struct VkSurfaceCapabilities2EXT {
//    VkStructureType                  sType;
//    void*                            pNext;
//    uint32_t                         minImageCount;
//    uint32_t                         maxImageCount;
//    VkExtent2D                       currentExtent;
//    VkExtent2D                       minImageExtent;
//    VkExtent2D                       maxImageExtent;
//    uint32_t                         maxImageArrayLayers;
//    VkSurfaceTransformFlagsKHR       supportedTransforms;
//    VkSurfaceTransformFlagBitsKHR    currentTransform;
//    VkCompositeAlphaFlagsKHR         supportedCompositeAlpha;
//    VkImageUsageFlags                supportedUsageFlags;
//    VkSurfaceCounterFlagsEXT         supportedSurfaceCounters;
//} VkSurfaceCapabilities2EXT;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetPhysicalDeviceSurfaceCapabilities2EXT)(VkPhysicalDevice physicalDevice, VkSurfaceKHR surface, VkSurfaceCapabilities2EXT* pSurfaceCapabilities);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPhysicalDeviceSurfaceCapabilities2EXT(
//VkPhysicalDevice                            physicalDevice,
//VkSurfaceKHR                                surface,
//VkSurfaceCapabilities2EXT*                  pSurfaceCapabilities);
//#endif
//
//#define VK_EXT_display_control 1
//#define VK_EXT_DISPLAY_CONTROL_SPEC_VERSION 1
//#define VK_EXT_DISPLAY_CONTROL_EXTENSION_NAME "VK_EXT_display_control"
//
//
//typedef enum VkDisplayPowerStateEXT {
//    VK_DISPLAY_POWER_STATE_OFF_EXT = 0,
//    VK_DISPLAY_POWER_STATE_SUSPEND_EXT = 1,
//    VK_DISPLAY_POWER_STATE_ON_EXT = 2,
//    VK_DISPLAY_POWER_STATE_BEGIN_RANGE_EXT = VK_DISPLAY_POWER_STATE_OFF_EXT,
//    VK_DISPLAY_POWER_STATE_END_RANGE_EXT = VK_DISPLAY_POWER_STATE_ON_EXT,
//    VK_DISPLAY_POWER_STATE_RANGE_SIZE_EXT = (VK_DISPLAY_POWER_STATE_ON_EXT - VK_DISPLAY_POWER_STATE_OFF_EXT + 1),
//    VK_DISPLAY_POWER_STATE_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkDisplayPowerStateEXT;
//
//typedef enum VkDeviceEventTypeEXT {
//    VK_DEVICE_EVENT_TYPE_DISPLAY_HOTPLUG_EXT = 0,
//    VK_DEVICE_EVENT_TYPE_BEGIN_RANGE_EXT = VK_DEVICE_EVENT_TYPE_DISPLAY_HOTPLUG_EXT,
//    VK_DEVICE_EVENT_TYPE_END_RANGE_EXT = VK_DEVICE_EVENT_TYPE_DISPLAY_HOTPLUG_EXT,
//    VK_DEVICE_EVENT_TYPE_RANGE_SIZE_EXT = (VK_DEVICE_EVENT_TYPE_DISPLAY_HOTPLUG_EXT - VK_DEVICE_EVENT_TYPE_DISPLAY_HOTPLUG_EXT + 1),
//    VK_DEVICE_EVENT_TYPE_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkDeviceEventTypeEXT;
//
//typedef enum VkDisplayEventTypeEXT {
//    VK_DISPLAY_EVENT_TYPE_FIRST_PIXEL_OUT_EXT = 0,
//    VK_DISPLAY_EVENT_TYPE_BEGIN_RANGE_EXT = VK_DISPLAY_EVENT_TYPE_FIRST_PIXEL_OUT_EXT,
//    VK_DISPLAY_EVENT_TYPE_END_RANGE_EXT = VK_DISPLAY_EVENT_TYPE_FIRST_PIXEL_OUT_EXT,
//    VK_DISPLAY_EVENT_TYPE_RANGE_SIZE_EXT = (VK_DISPLAY_EVENT_TYPE_FIRST_PIXEL_OUT_EXT - VK_DISPLAY_EVENT_TYPE_FIRST_PIXEL_OUT_EXT + 1),
//    VK_DISPLAY_EVENT_TYPE_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkDisplayEventTypeEXT;
//
//typedef struct VkDisplayPowerInfoEXT {
//    VkStructureType           sType;
//    const void*               pNext;
//    VkDisplayPowerStateEXT    powerState;
//} VkDisplayPowerInfoEXT;
//
//typedef struct VkDeviceEventInfoEXT {
//    VkStructureType         sType;
//    const void*             pNext;
//    VkDeviceEventTypeEXT    deviceEvent;
//} VkDeviceEventInfoEXT;
//
//typedef struct VkDisplayEventInfoEXT {
//    VkStructureType          sType;
//    const void*              pNext;
//    VkDisplayEventTypeEXT    displayEvent;
//} VkDisplayEventInfoEXT;
//
//typedef struct VkSwapchainCounterCreateInfoEXT {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkSurfaceCounterFlagsEXT    surfaceCounters;
//} VkSwapchainCounterCreateInfoEXT;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkDisplayPowerControlEXT)(VkDevice device, VkDisplayKHR display, const VkDisplayPowerInfoEXT* pDisplayPowerInfo);
//typedef VkResult (VKAPI_PTR *PFN_vkRegisterDeviceEventEXT)(VkDevice device, const VkDeviceEventInfoEXT* pDeviceEventInfo, const VkAllocationCallbacks* pAllocator, VkFence* pFence);
//typedef VkResult (VKAPI_PTR *PFN_vkRegisterDisplayEventEXT)(VkDevice device, VkDisplayKHR display, const VkDisplayEventInfoEXT* pDisplayEventInfo, const VkAllocationCallbacks* pAllocator, VkFence* pFence);
//typedef VkResult (VKAPI_PTR *PFN_vkGetSwapchainCounterEXT)(VkDevice device, VkSwapchainKHR swapchain, VkSurfaceCounterFlagBitsEXT counter, uint64_t* pCounterValue);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkDisplayPowerControlEXT(
//VkDevice                                    device,
//VkDisplayKHR                                display,
//const VkDisplayPowerInfoEXT*                pDisplayPowerInfo);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkRegisterDeviceEventEXT(
//VkDevice                                    device,
//const VkDeviceEventInfoEXT*                 pDeviceEventInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkFence*                                    pFence);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkRegisterDisplayEventEXT(
//VkDevice                                    device,
//VkDisplayKHR                                display,
//const VkDisplayEventInfoEXT*                pDisplayEventInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkFence*                                    pFence);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetSwapchainCounterEXT(
//VkDevice                                    device,
//VkSwapchainKHR                              swapchain,
//VkSurfaceCounterFlagBitsEXT                 counter,
//uint64_t*                                   pCounterValue);
//#endif
//
//#define VK_GOOGLE_display_timing 1
//#define VK_GOOGLE_DISPLAY_TIMING_SPEC_VERSION 1
//#define VK_GOOGLE_DISPLAY_TIMING_EXTENSION_NAME "VK_GOOGLE_display_timing"
//
//typedef struct VkRefreshCycleDurationGOOGLE {
//    uint64_t    refreshDuration;
//} VkRefreshCycleDurationGOOGLE;
//
//typedef struct VkPastPresentationTimingGOOGLE {
//    uint32_t    presentID;
//    uint64_t    desiredPresentTime;
//    uint64_t    actualPresentTime;
//    uint64_t    earliestPresentTime;
//    uint64_t    presentMargin;
//} VkPastPresentationTimingGOOGLE;
//
//typedef struct VkPresentTimeGOOGLE {
//    uint32_t    presentID;
//    uint64_t    desiredPresentTime;
//} VkPresentTimeGOOGLE;
//
//typedef struct VkPresentTimesInfoGOOGLE {
//    VkStructureType               sType;
//    const void*                   pNext;
//    uint32_t                      swapchainCount;
//    const VkPresentTimeGOOGLE*    pTimes;
//} VkPresentTimesInfoGOOGLE;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetRefreshCycleDurationGOOGLE)(VkDevice device, VkSwapchainKHR swapchain, VkRefreshCycleDurationGOOGLE* pDisplayTimingProperties);
//typedef VkResult (VKAPI_PTR *PFN_vkGetPastPresentationTimingGOOGLE)(VkDevice device, VkSwapchainKHR swapchain, uint32_t* pPresentationTimingCount, VkPastPresentationTimingGOOGLE* pPresentationTimings);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetRefreshCycleDurationGOOGLE(
//VkDevice                                    device,
//VkSwapchainKHR                              swapchain,
//VkRefreshCycleDurationGOOGLE*               pDisplayTimingProperties);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetPastPresentationTimingGOOGLE(
//VkDevice                                    device,
//VkSwapchainKHR                              swapchain,
//uint32_t*                                   pPresentationTimingCount,
//VkPastPresentationTimingGOOGLE*             pPresentationTimings);
//#endif
//
//#define VK_NV_sample_mask_override_coverage 1
//#define VK_NV_SAMPLE_MASK_OVERRIDE_COVERAGE_SPEC_VERSION 1
//#define VK_NV_SAMPLE_MASK_OVERRIDE_COVERAGE_EXTENSION_NAME "VK_NV_sample_mask_override_coverage"
//
//
//#define VK_NV_geometry_shader_passthrough 1
//#define VK_NV_GEOMETRY_SHADER_PASSTHROUGH_SPEC_VERSION 1
//#define VK_NV_GEOMETRY_SHADER_PASSTHROUGH_EXTENSION_NAME "VK_NV_geometry_shader_passthrough"
//
//
//#define VK_NV_viewport_array2 1
//#define VK_NV_VIEWPORT_ARRAY2_SPEC_VERSION 1
//#define VK_NV_VIEWPORT_ARRAY2_EXTENSION_NAME "VK_NV_viewport_array2"
//
//
//#define VK_NVX_multiview_per_view_attributes 1
//#define VK_NVX_MULTIVIEW_PER_VIEW_ATTRIBUTES_SPEC_VERSION 1
//#define VK_NVX_MULTIVIEW_PER_VIEW_ATTRIBUTES_EXTENSION_NAME "VK_NVX_multiview_per_view_attributes"
//
//typedef struct VkPhysicalDeviceMultiviewPerViewAttributesPropertiesNVX {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           perViewPositionAllComponents;
//} VkPhysicalDeviceMultiviewPerViewAttributesPropertiesNVX;
//
//
//
//#define VK_NV_viewport_swizzle 1
//#define VK_NV_VIEWPORT_SWIZZLE_SPEC_VERSION 1
//#define VK_NV_VIEWPORT_SWIZZLE_EXTENSION_NAME "VK_NV_viewport_swizzle"
//
//
//typedef enum VkViewportCoordinateSwizzleNV {
//    VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_X_NV = 0,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_X_NV = 1,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_Y_NV = 2,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_Y_NV = 3,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_Z_NV = 4,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_Z_NV = 5,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_W_NV = 6,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_W_NV = 7,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_BEGIN_RANGE_NV = VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_X_NV,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_END_RANGE_NV = VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_W_NV,
//    VK_VIEWPORT_COORDINATE_SWIZZLE_RANGE_SIZE_NV = (VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_W_NV - VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_X_NV + 1),
//    VK_VIEWPORT_COORDINATE_SWIZZLE_MAX_ENUM_NV = 0x7FFFFFFF
//} VkViewportCoordinateSwizzleNV;
//
//typedef VkFlags VkPipelineViewportSwizzleStateCreateFlagsNV;
//
//typedef struct VkViewportSwizzleNV {
//    VkViewportCoordinateSwizzleNV    x;
//    VkViewportCoordinateSwizzleNV    y;
//    VkViewportCoordinateSwizzleNV    z;
//    VkViewportCoordinateSwizzleNV    w;
//} VkViewportSwizzleNV;
//
//typedef struct VkPipelineViewportSwizzleStateCreateInfoNV {
//    VkStructureType                                sType;
//    const void*                                    pNext;
//    VkPipelineViewportSwizzleStateCreateFlagsNV    flags;
//    uint32_t                                       viewportCount;
//    const VkViewportSwizzleNV*                     pViewportSwizzles;
//} VkPipelineViewportSwizzleStateCreateInfoNV;
//
//
//
//#define VK_EXT_discard_rectangles 1
//#define VK_EXT_DISCARD_RECTANGLES_SPEC_VERSION 1
//#define VK_EXT_DISCARD_RECTANGLES_EXTENSION_NAME "VK_EXT_discard_rectangles"
//
//
//typedef enum VkDiscardRectangleModeEXT {
//    VK_DISCARD_RECTANGLE_MODE_INCLUSIVE_EXT = 0,
//    VK_DISCARD_RECTANGLE_MODE_EXCLUSIVE_EXT = 1,
//    VK_DISCARD_RECTANGLE_MODE_BEGIN_RANGE_EXT = VK_DISCARD_RECTANGLE_MODE_INCLUSIVE_EXT,
//    VK_DISCARD_RECTANGLE_MODE_END_RANGE_EXT = VK_DISCARD_RECTANGLE_MODE_EXCLUSIVE_EXT,
//    VK_DISCARD_RECTANGLE_MODE_RANGE_SIZE_EXT = (VK_DISCARD_RECTANGLE_MODE_EXCLUSIVE_EXT - VK_DISCARD_RECTANGLE_MODE_INCLUSIVE_EXT + 1),
//    VK_DISCARD_RECTANGLE_MODE_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkDiscardRectangleModeEXT;
//
//typedef VkFlags VkPipelineDiscardRectangleStateCreateFlagsEXT;
//
//typedef struct VkPhysicalDeviceDiscardRectanglePropertiesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           maxDiscardRectangles;
//} VkPhysicalDeviceDiscardRectanglePropertiesEXT;
//
//typedef struct VkPipelineDiscardRectangleStateCreateInfoEXT {
//    VkStructureType                                  sType;
//    const void*                                      pNext;
//    VkPipelineDiscardRectangleStateCreateFlagsEXT    flags;
//    VkDiscardRectangleModeEXT                        discardRectangleMode;
//    uint32_t                                         discardRectangleCount;
//    const VkRect2D*                                  pDiscardRectangles;
//} VkPipelineDiscardRectangleStateCreateInfoEXT;
//
//
//typedef void (VKAPI_PTR *PFN_vkCmdSetDiscardRectangleEXT)(VkCommandBuffer commandBuffer, uint32_t firstDiscardRectangle, uint32_t discardRectangleCount, const VkRect2D* pDiscardRectangles);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkCmdSetDiscardRectangleEXT(
//VkCommandBuffer                             commandBuffer,
//uint32_t                                    firstDiscardRectangle,
//uint32_t                                    discardRectangleCount,
//const VkRect2D*                             pDiscardRectangles);
//#endif
//
//#define VK_EXT_conservative_rasterization 1
//#define VK_EXT_CONSERVATIVE_RASTERIZATION_SPEC_VERSION 1
//#define VK_EXT_CONSERVATIVE_RASTERIZATION_EXTENSION_NAME "VK_EXT_conservative_rasterization"
//
//
//typedef enum VkConservativeRasterizationModeEXT {
//    VK_CONSERVATIVE_RASTERIZATION_MODE_DISABLED_EXT = 0,
//    VK_CONSERVATIVE_RASTERIZATION_MODE_OVERESTIMATE_EXT = 1,
//    VK_CONSERVATIVE_RASTERIZATION_MODE_UNDERESTIMATE_EXT = 2,
//    VK_CONSERVATIVE_RASTERIZATION_MODE_BEGIN_RANGE_EXT = VK_CONSERVATIVE_RASTERIZATION_MODE_DISABLED_EXT,
//    VK_CONSERVATIVE_RASTERIZATION_MODE_END_RANGE_EXT = VK_CONSERVATIVE_RASTERIZATION_MODE_UNDERESTIMATE_EXT,
//    VK_CONSERVATIVE_RASTERIZATION_MODE_RANGE_SIZE_EXT = (VK_CONSERVATIVE_RASTERIZATION_MODE_UNDERESTIMATE_EXT - VK_CONSERVATIVE_RASTERIZATION_MODE_DISABLED_EXT + 1),
//    VK_CONSERVATIVE_RASTERIZATION_MODE_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkConservativeRasterizationModeEXT;
//
//typedef VkFlags VkPipelineRasterizationConservativeStateCreateFlagsEXT;
//
//typedef struct VkPhysicalDeviceConservativeRasterizationPropertiesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    float              primitiveOverestimationSize;
//    float              maxExtraPrimitiveOverestimationSize;
//    float              extraPrimitiveOverestimationSizeGranularity;
//    VkBool32           primitiveUnderestimation;
//    VkBool32           conservativePointAndLineRasterization;
//    VkBool32           degenerateTrianglesRasterized;
//    VkBool32           degenerateLinesRasterized;
//    VkBool32           fullyCoveredFragmentShaderInputVariable;
//    VkBool32           conservativeRasterizationPostDepthCoverage;
//} VkPhysicalDeviceConservativeRasterizationPropertiesEXT;
//
//typedef struct VkPipelineRasterizationConservativeStateCreateInfoEXT {
//    VkStructureType                                           sType;
//    const void*                                               pNext;
//    VkPipelineRasterizationConservativeStateCreateFlagsEXT    flags;
//    VkConservativeRasterizationModeEXT                        conservativeRasterizationMode;
//    float                                                     extraPrimitiveOverestimationSize;
//} VkPipelineRasterizationConservativeStateCreateInfoEXT;
//
//
//
//#define VK_EXT_swapchain_colorspace 1
//#define VK_EXT_SWAPCHAIN_COLOR_SPACE_SPEC_VERSION 3
//#define VK_EXT_SWAPCHAIN_COLOR_SPACE_EXTENSION_NAME "VK_EXT_swapchain_colorspace"
//
//
//#define VK_EXT_hdr_metadata 1
//#define VK_EXT_HDR_METADATA_SPEC_VERSION  1
//#define VK_EXT_HDR_METADATA_EXTENSION_NAME "VK_EXT_hdr_metadata"
//
//typedef struct VkXYColorEXT {
//    float    x;
//    float    y;
//} VkXYColorEXT;
//
//typedef struct VkHdrMetadataEXT {
//    VkStructureType    sType;
//    const void*        pNext;
//    VkXYColorEXT       displayPrimaryRed;
//    VkXYColorEXT       displayPrimaryGreen;
//    VkXYColorEXT       displayPrimaryBlue;
//    VkXYColorEXT       whitePoint;
//    float              maxLuminance;
//    float              minLuminance;
//    float              maxContentLightLevel;
//    float              maxFrameAverageLightLevel;
//} VkHdrMetadataEXT;
//
//
//typedef void (VKAPI_PTR *PFN_vkSetHdrMetadataEXT)(VkDevice device, uint32_t swapchainCount, const VkSwapchainKHR* pSwapchains, const VkHdrMetadataEXT* pMetadata);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkSetHdrMetadataEXT(
//VkDevice                                    device,
//uint32_t                                    swapchainCount,
//const VkSwapchainKHR*                       pSwapchains,
//const VkHdrMetadataEXT*                     pMetadata);
//#endif
//
//#ifdef VK_USE_PLATFORM_IOS_MVK
//#define VK_MVK_ios_surface 1
//#define VK_MVK_IOS_SURFACE_SPEC_VERSION   2
//#define VK_MVK_IOS_SURFACE_EXTENSION_NAME "VK_MVK_ios_surface"
//
//typedef VkFlags VkIOSSurfaceCreateFlagsMVK;
//
//typedef struct VkIOSSurfaceCreateInfoMVK {
//    VkStructureType               sType;
//    const void*                   pNext;
//    VkIOSSurfaceCreateFlagsMVK    flags;
//    const void*                   pView;
//} VkIOSSurfaceCreateInfoMVK;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateIOSSurfaceMVK)(VkInstance instance, const VkIOSSurfaceCreateInfoMVK* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateIOSSurfaceMVK(
//VkInstance                                  instance,
//const VkIOSSurfaceCreateInfoMVK*            pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//#endif
//#endif /* VK_USE_PLATFORM_IOS_MVK */
//
//#ifdef VK_USE_PLATFORM_MACOS_MVK
//#define VK_MVK_macos_surface 1
//#define VK_MVK_MACOS_SURFACE_SPEC_VERSION 2
//#define VK_MVK_MACOS_SURFACE_EXTENSION_NAME "VK_MVK_macos_surface"
//
//typedef VkFlags VkMacOSSurfaceCreateFlagsMVK;
//
//typedef struct VkMacOSSurfaceCreateInfoMVK {
//    VkStructureType                 sType;
//    const void*                     pNext;
//    VkMacOSSurfaceCreateFlagsMVK    flags;
//    const void*                     pView;
//} VkMacOSSurfaceCreateInfoMVK;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateMacOSSurfaceMVK)(VkInstance instance, const VkMacOSSurfaceCreateInfoMVK* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkSurfaceKHR* pSurface);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateMacOSSurfaceMVK(
//VkInstance                                  instance,
//const VkMacOSSurfaceCreateInfoMVK*          pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkSurfaceKHR*                               pSurface);
//#endif
//#endif /* VK_USE_PLATFORM_MACOS_MVK */
//
//#define VK_EXT_external_memory_dma_buf 1
//#define VK_EXT_EXTERNAL_MEMORY_DMA_BUF_SPEC_VERSION 1
//#define VK_EXT_EXTERNAL_MEMORY_DMA_BUF_EXTENSION_NAME "VK_EXT_external_memory_dma_buf"
//
//
//#define VK_EXT_queue_family_foreign 1
//#define VK_EXT_QUEUE_FAMILY_FOREIGN_SPEC_VERSION 1
//#define VK_EXT_QUEUE_FAMILY_FOREIGN_EXTENSION_NAME "VK_EXT_queue_family_foreign"
//#define VK_QUEUE_FAMILY_FOREIGN_EXT       (~0U-2)
//
//
//#define VK_EXT_sampler_filter_minmax 1
//#define VK_EXT_SAMPLER_FILTER_MINMAX_SPEC_VERSION 1
//#define VK_EXT_SAMPLER_FILTER_MINMAX_EXTENSION_NAME "VK_EXT_sampler_filter_minmax"
//
//
//typedef enum VkSamplerReductionModeEXT {
//    VK_SAMPLER_REDUCTION_MODE_WEIGHTED_AVERAGE_EXT = 0,
//    VK_SAMPLER_REDUCTION_MODE_MIN_EXT = 1,
//    VK_SAMPLER_REDUCTION_MODE_MAX_EXT = 2,
//    VK_SAMPLER_REDUCTION_MODE_BEGIN_RANGE_EXT = VK_SAMPLER_REDUCTION_MODE_WEIGHTED_AVERAGE_EXT,
//    VK_SAMPLER_REDUCTION_MODE_END_RANGE_EXT = VK_SAMPLER_REDUCTION_MODE_MAX_EXT,
//    VK_SAMPLER_REDUCTION_MODE_RANGE_SIZE_EXT = (VK_SAMPLER_REDUCTION_MODE_MAX_EXT - VK_SAMPLER_REDUCTION_MODE_WEIGHTED_AVERAGE_EXT + 1),
//    VK_SAMPLER_REDUCTION_MODE_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkSamplerReductionModeEXT;
//
//typedef struct VkSamplerReductionModeCreateInfoEXT {
//    VkStructureType              sType;
//    const void*                  pNext;
//    VkSamplerReductionModeEXT    reductionMode;
//} VkSamplerReductionModeCreateInfoEXT;
//
//typedef struct VkPhysicalDeviceSamplerFilterMinmaxPropertiesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           filterMinmaxSingleComponentFormats;
//    VkBool32           filterMinmaxImageComponentMapping;
//} VkPhysicalDeviceSamplerFilterMinmaxPropertiesEXT;
//
//
//
//#define VK_AMD_gpu_shader_int16 1
//#define VK_AMD_GPU_SHADER_INT16_SPEC_VERSION 1
//#define VK_AMD_GPU_SHADER_INT16_EXTENSION_NAME "VK_AMD_gpu_shader_int16"
//
//
//#define VK_AMD_mixed_attachment_samples 1
//#define VK_AMD_MIXED_ATTACHMENT_SAMPLES_SPEC_VERSION 1
//#define VK_AMD_MIXED_ATTACHMENT_SAMPLES_EXTENSION_NAME "VK_AMD_mixed_attachment_samples"
//
//
//#define VK_AMD_shader_fragment_mask 1
//#define VK_AMD_SHADER_FRAGMENT_MASK_SPEC_VERSION 1
//#define VK_AMD_SHADER_FRAGMENT_MASK_EXTENSION_NAME "VK_AMD_shader_fragment_mask"
//
//
//#define VK_EXT_shader_stencil_export 1
//#define VK_EXT_SHADER_STENCIL_EXPORT_SPEC_VERSION 1
//#define VK_EXT_SHADER_STENCIL_EXPORT_EXTENSION_NAME "VK_EXT_shader_stencil_export"
//
//
//#define VK_EXT_sample_locations 1
//#define VK_EXT_SAMPLE_LOCATIONS_SPEC_VERSION 1
//#define VK_EXT_SAMPLE_LOCATIONS_EXTENSION_NAME "VK_EXT_sample_locations"
//
//typedef struct VkSampleLocationEXT {
//    float    x;
//    float    y;
//} VkSampleLocationEXT;
//
//typedef struct VkSampleLocationsInfoEXT {
//    VkStructureType               sType;
//    const void*                   pNext;
//    VkSampleCountFlagBits         sampleLocationsPerPixel;
//    VkExtent2D                    sampleLocationGridSize;
//    uint32_t                      sampleLocationsCount;
//    const VkSampleLocationEXT*    pSampleLocations;
//} VkSampleLocationsInfoEXT;
//
//typedef struct VkAttachmentSampleLocationsEXT {
//    uint32_t                    attachmentIndex;
//    VkSampleLocationsInfoEXT    sampleLocationsInfo;
//} VkAttachmentSampleLocationsEXT;
//
//typedef struct VkSubpassSampleLocationsEXT {
//    uint32_t                    subpassIndex;
//    VkSampleLocationsInfoEXT    sampleLocationsInfo;
//} VkSubpassSampleLocationsEXT;
//
//typedef struct VkRenderPassSampleLocationsBeginInfoEXT {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    uint32_t                                 attachmentInitialSampleLocationsCount;
//    const VkAttachmentSampleLocationsEXT*    pAttachmentInitialSampleLocations;
//    uint32_t                                 postSubpassSampleLocationsCount;
//    const VkSubpassSampleLocationsEXT*       pPostSubpassSampleLocations;
//} VkRenderPassSampleLocationsBeginInfoEXT;
//
//typedef struct VkPipelineSampleLocationsStateCreateInfoEXT {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkBool32                    sampleLocationsEnable;
//    VkSampleLocationsInfoEXT    sampleLocationsInfo;
//} VkPipelineSampleLocationsStateCreateInfoEXT;
//
//typedef struct VkPhysicalDeviceSampleLocationsPropertiesEXT {
//    VkStructureType       sType;
//    void*                 pNext;
//    VkSampleCountFlags    sampleLocationSampleCounts;
//    VkExtent2D            maxSampleLocationGridSize;
//    float                 sampleLocationCoordinateRange[2];
//    uint32_t              sampleLocationSubPixelBits;
//    VkBool32              variableSampleLocations;
//} VkPhysicalDeviceSampleLocationsPropertiesEXT;
//
//typedef struct VkMultisamplePropertiesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    VkExtent2D         maxSampleLocationGridSize;
//} VkMultisamplePropertiesEXT;
//
//
//typedef void (VKAPI_PTR *PFN_vkCmdSetSampleLocationsEXT)(VkCommandBuffer commandBuffer, const VkSampleLocationsInfoEXT* pSampleLocationsInfo);
//typedef void (VKAPI_PTR *PFN_vkGetPhysicalDeviceMultisamplePropertiesEXT)(VkPhysicalDevice physicalDevice, VkSampleCountFlagBits samples, VkMultisamplePropertiesEXT* pMultisampleProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR void VKAPI_CALL vkCmdSetSampleLocationsEXT(
//VkCommandBuffer                             commandBuffer,
//const VkSampleLocationsInfoEXT*             pSampleLocationsInfo);
//
//VKAPI_ATTR void VKAPI_CALL vkGetPhysicalDeviceMultisamplePropertiesEXT(
//VkPhysicalDevice                            physicalDevice,
//VkSampleCountFlagBits                       samples,
//VkMultisamplePropertiesEXT*                 pMultisampleProperties);
//#endif
//
//#define VK_EXT_blend_operation_advanced 1
//#define VK_EXT_BLEND_OPERATION_ADVANCED_SPEC_VERSION 2
//#define VK_EXT_BLEND_OPERATION_ADVANCED_EXTENSION_NAME "VK_EXT_blend_operation_advanced"
//
//
//typedef enum VkBlendOverlapEXT {
//    VK_BLEND_OVERLAP_UNCORRELATED_EXT = 0,
//    VK_BLEND_OVERLAP_DISJOINT_EXT = 1,
//    VK_BLEND_OVERLAP_CONJOINT_EXT = 2,
//    VK_BLEND_OVERLAP_BEGIN_RANGE_EXT = VK_BLEND_OVERLAP_UNCORRELATED_EXT,
//    VK_BLEND_OVERLAP_END_RANGE_EXT = VK_BLEND_OVERLAP_CONJOINT_EXT,
//    VK_BLEND_OVERLAP_RANGE_SIZE_EXT = (VK_BLEND_OVERLAP_CONJOINT_EXT - VK_BLEND_OVERLAP_UNCORRELATED_EXT + 1),
//    VK_BLEND_OVERLAP_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkBlendOverlapEXT;
//
//typedef struct VkPhysicalDeviceBlendOperationAdvancedFeaturesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    VkBool32           advancedBlendCoherentOperations;
//} VkPhysicalDeviceBlendOperationAdvancedFeaturesEXT;
//
//typedef struct VkPhysicalDeviceBlendOperationAdvancedPropertiesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           advancedBlendMaxColorAttachments;
//    VkBool32           advancedBlendIndependentBlend;
//    VkBool32           advancedBlendNonPremultipliedSrcColor;
//    VkBool32           advancedBlendNonPremultipliedDstColor;
//    VkBool32           advancedBlendCorrelatedOverlap;
//    VkBool32           advancedBlendAllOperations;
//} VkPhysicalDeviceBlendOperationAdvancedPropertiesEXT;
//
//typedef struct VkPipelineColorBlendAdvancedStateCreateInfoEXT {
//    VkStructureType      sType;
//    const void*          pNext;
//    VkBool32             srcPremultiplied;
//    VkBool32             dstPremultiplied;
//    VkBlendOverlapEXT    blendOverlap;
//} VkPipelineColorBlendAdvancedStateCreateInfoEXT;
//
//
//
//#define VK_NV_fragment_coverage_to_color 1
//#define VK_NV_FRAGMENT_COVERAGE_TO_COLOR_SPEC_VERSION 1
//#define VK_NV_FRAGMENT_COVERAGE_TO_COLOR_EXTENSION_NAME "VK_NV_fragment_coverage_to_color"
//
//typedef VkFlags VkPipelineCoverageToColorStateCreateFlagsNV;
//
//typedef struct VkPipelineCoverageToColorStateCreateInfoNV {
//    VkStructureType                                sType;
//    const void*                                    pNext;
//    VkPipelineCoverageToColorStateCreateFlagsNV    flags;
//    VkBool32                                       coverageToColorEnable;
//    uint32_t                                       coverageToColorLocation;
//} VkPipelineCoverageToColorStateCreateInfoNV;
//
//
//
//#define VK_NV_framebuffer_mixed_samples 1
//#define VK_NV_FRAMEBUFFER_MIXED_SAMPLES_SPEC_VERSION 1
//#define VK_NV_FRAMEBUFFER_MIXED_SAMPLES_EXTENSION_NAME "VK_NV_framebuffer_mixed_samples"
//
//
//typedef enum VkCoverageModulationModeNV {
//    VK_COVERAGE_MODULATION_MODE_NONE_NV = 0,
//    VK_COVERAGE_MODULATION_MODE_RGB_NV = 1,
//    VK_COVERAGE_MODULATION_MODE_ALPHA_NV = 2,
//    VK_COVERAGE_MODULATION_MODE_RGBA_NV = 3,
//    VK_COVERAGE_MODULATION_MODE_BEGIN_RANGE_NV = VK_COVERAGE_MODULATION_MODE_NONE_NV,
//    VK_COVERAGE_MODULATION_MODE_END_RANGE_NV = VK_COVERAGE_MODULATION_MODE_RGBA_NV,
//    VK_COVERAGE_MODULATION_MODE_RANGE_SIZE_NV = (VK_COVERAGE_MODULATION_MODE_RGBA_NV - VK_COVERAGE_MODULATION_MODE_NONE_NV + 1),
//    VK_COVERAGE_MODULATION_MODE_MAX_ENUM_NV = 0x7FFFFFFF
//} VkCoverageModulationModeNV;
//
//typedef VkFlags VkPipelineCoverageModulationStateCreateFlagsNV;
//
//typedef struct VkPipelineCoverageModulationStateCreateInfoNV {
//    VkStructureType                                   sType;
//    const void*                                       pNext;
//    VkPipelineCoverageModulationStateCreateFlagsNV    flags;
//    VkCoverageModulationModeNV                        coverageModulationMode;
//    VkBool32                                          coverageModulationTableEnable;
//    uint32_t                                          coverageModulationTableCount;
//    const float*                                      pCoverageModulationTable;
//} VkPipelineCoverageModulationStateCreateInfoNV;
//
//
//
//#define VK_NV_fill_rectangle 1
//#define VK_NV_FILL_RECTANGLE_SPEC_VERSION 1
//#define VK_NV_FILL_RECTANGLE_EXTENSION_NAME "VK_NV_fill_rectangle"
//
//
//#define VK_EXT_post_depth_coverage 1
//#define VK_EXT_POST_DEPTH_COVERAGE_SPEC_VERSION 1
//#define VK_EXT_POST_DEPTH_COVERAGE_EXTENSION_NAME "VK_EXT_post_depth_coverage"
//
//
//#define VK_EXT_validation_cache 1
//VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkValidationCacheEXT)
//
//#define VK_EXT_VALIDATION_CACHE_SPEC_VERSION 1
//#define VK_EXT_VALIDATION_CACHE_EXTENSION_NAME "VK_EXT_validation_cache"
//
//
//typedef enum VkValidationCacheHeaderVersionEXT {
//    VK_VALIDATION_CACHE_HEADER_VERSION_ONE_EXT = 1,
//    VK_VALIDATION_CACHE_HEADER_VERSION_BEGIN_RANGE_EXT = VK_VALIDATION_CACHE_HEADER_VERSION_ONE_EXT,
//    VK_VALIDATION_CACHE_HEADER_VERSION_END_RANGE_EXT = VK_VALIDATION_CACHE_HEADER_VERSION_ONE_EXT,
//    VK_VALIDATION_CACHE_HEADER_VERSION_RANGE_SIZE_EXT = (VK_VALIDATION_CACHE_HEADER_VERSION_ONE_EXT - VK_VALIDATION_CACHE_HEADER_VERSION_ONE_EXT + 1),
//    VK_VALIDATION_CACHE_HEADER_VERSION_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkValidationCacheHeaderVersionEXT;
//
//typedef VkFlags VkValidationCacheCreateFlagsEXT;
//
//typedef struct VkValidationCacheCreateInfoEXT {
//    VkStructureType                    sType;
//    const void*                        pNext;
//    VkValidationCacheCreateFlagsEXT    flags;
//    size_t                             initialDataSize;
//    const void*                        pInitialData;
//} VkValidationCacheCreateInfoEXT;
//
//typedef struct VkShaderModuleValidationCacheCreateInfoEXT {
//    VkStructureType         sType;
//    const void*             pNext;
//    VkValidationCacheEXT    validationCache;
//} VkShaderModuleValidationCacheCreateInfoEXT;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkCreateValidationCacheEXT)(VkDevice device, const VkValidationCacheCreateInfoEXT* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkValidationCacheEXT* pValidationCache);
//typedef void (VKAPI_PTR *PFN_vkDestroyValidationCacheEXT)(VkDevice device, VkValidationCacheEXT validationCache, const VkAllocationCallbacks* pAllocator);
//typedef VkResult (VKAPI_PTR *PFN_vkMergeValidationCachesEXT)(VkDevice device, VkValidationCacheEXT dstCache, uint32_t srcCacheCount, const VkValidationCacheEXT* pSrcCaches);
//typedef VkResult (VKAPI_PTR *PFN_vkGetValidationCacheDataEXT)(VkDevice device, VkValidationCacheEXT validationCache, size_t* pDataSize, void* pData);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkCreateValidationCacheEXT(
//VkDevice                                    device,
//const VkValidationCacheCreateInfoEXT*       pCreateInfo,
//const VkAllocationCallbacks*                pAllocator,
//VkValidationCacheEXT*                       pValidationCache);
//
//VKAPI_ATTR void VKAPI_CALL vkDestroyValidationCacheEXT(
//VkDevice                                    device,
//VkValidationCacheEXT                        validationCache,
//const VkAllocationCallbacks*                pAllocator);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkMergeValidationCachesEXT(
//VkDevice                                    device,
//VkValidationCacheEXT                        dstCache,
//uint32_t                                    srcCacheCount,
//const VkValidationCacheEXT*                 pSrcCaches);
//
//VKAPI_ATTR VkResult VKAPI_CALL vkGetValidationCacheDataEXT(
//VkDevice                                    device,
//VkValidationCacheEXT                        validationCache,
//size_t*                                     pDataSize,
//void*                                       pData);
//#endif
//
//#define VK_EXT_shader_viewport_index_layer 1
//#define VK_EXT_SHADER_VIEWPORT_INDEX_LAYER_SPEC_VERSION 1
//#define VK_EXT_SHADER_VIEWPORT_INDEX_LAYER_EXTENSION_NAME "VK_EXT_shader_viewport_index_layer"
//
//
//#define VK_EXT_global_priority 1
//#define VK_EXT_GLOBAL_PRIORITY_SPEC_VERSION 2
//#define VK_EXT_GLOBAL_PRIORITY_EXTENSION_NAME "VK_EXT_global_priority"
//
//
//typedef enum VkQueueGlobalPriorityEXT {
//    VK_QUEUE_GLOBAL_PRIORITY_LOW_EXT = 128,
//    VK_QUEUE_GLOBAL_PRIORITY_MEDIUM_EXT = 256,
//    VK_QUEUE_GLOBAL_PRIORITY_HIGH_EXT = 512,
//    VK_QUEUE_GLOBAL_PRIORITY_REALTIME_EXT = 1024,
//    VK_QUEUE_GLOBAL_PRIORITY_BEGIN_RANGE_EXT = VK_QUEUE_GLOBAL_PRIORITY_LOW_EXT,
//    VK_QUEUE_GLOBAL_PRIORITY_END_RANGE_EXT = VK_QUEUE_GLOBAL_PRIORITY_REALTIME_EXT,
//    VK_QUEUE_GLOBAL_PRIORITY_RANGE_SIZE_EXT = (VK_QUEUE_GLOBAL_PRIORITY_REALTIME_EXT - VK_QUEUE_GLOBAL_PRIORITY_LOW_EXT + 1),
//    VK_QUEUE_GLOBAL_PRIORITY_MAX_ENUM_EXT = 0x7FFFFFFF
//} VkQueueGlobalPriorityEXT;
//
//typedef struct VkDeviceQueueGlobalPriorityCreateInfoEXT {
//    VkStructureType             sType;
//    const void*                 pNext;
//    VkQueueGlobalPriorityEXT    globalPriority;
//} VkDeviceQueueGlobalPriorityCreateInfoEXT;
//
//
//
//#define VK_EXT_external_memory_host 1
//#define VK_EXT_EXTERNAL_MEMORY_HOST_SPEC_VERSION 1
//#define VK_EXT_EXTERNAL_MEMORY_HOST_EXTENSION_NAME "VK_EXT_external_memory_host"
//
//typedef struct VkImportMemoryHostPointerInfoEXT {
//    VkStructureType                          sType;
//    const void*                              pNext;
//    VkExternalMemoryHandleTypeFlagBitsKHR    handleType;
//    void*                                    pHostPointer;
//} VkImportMemoryHostPointerInfoEXT;
//
//typedef struct VkMemoryHostPointerPropertiesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    uint32_t           memoryTypeBits;
//} VkMemoryHostPointerPropertiesEXT;
//
//typedef struct VkPhysicalDeviceExternalMemoryHostPropertiesEXT {
//    VkStructureType    sType;
//    void*              pNext;
//    VkDeviceSize       minImportedHostPointerAlignment;
//} VkPhysicalDeviceExternalMemoryHostPropertiesEXT;
//
//
//typedef VkResult (VKAPI_PTR *PFN_vkGetMemoryHostPointerPropertiesEXT)(VkDevice device, VkExternalMemoryHandleTypeFlagBitsKHR handleType, const void* pHostPointer, VkMemoryHostPointerPropertiesEXT* pMemoryHostPointerProperties);
//
//#ifndef VK_NO_PROTOTYPES
//VKAPI_ATTR VkResult VKAPI_CALL vkGetMemoryHostPointerPropertiesEXT(
//VkDevice                                    device,
//VkExternalMemoryHandleTypeFlagBitsKHR       handleType,
//const void*                                 pHostPointer,
//VkMemoryHostPointerPropertiesEXT*           pMemoryHostPointerProperties);
//#endif
//
//#ifdef __cplusplus
//}

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