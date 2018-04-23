package vkn

import glfw_.appBuffer.ptr
import glfw_.advance
import org.lwjgl.vulkan.*


//fun VmDescriptorBufferInfo(): VkDescriptorBufferInfo = VkDescriptorBufferInfo.malloc()
//fun VmDescriptorBufferInfo(capacity: Int): VkDescriptorBufferInfo.Buffer = VkDescriptorBufferInfo.malloc(capacity)
fun cVkDescriptorBufferInfo(): VkDescriptorBufferInfo = VkDescriptorBufferInfo.calloc()
fun cVkDescriptorBufferInfo(capacity: Int): VkDescriptorBufferInfo.Buffer = VkDescriptorBufferInfo.calloc(capacity)


//fun VbApplicationInfo(block: VkApplicationInfo.() -> Unit): VkApplicationInfo = VkApplicationInfo.create(ptr.advance(VkApplicationInfo.SIZEOF)).also(block)


inline fun cVkDescriptorSetLayoutCreateInfo(block: VkDescriptorSetLayoutCreateInfo.() -> Unit): VkDescriptorSetLayoutCreateInfo {
    val res = VkDescriptorSetLayoutCreateInfo.calloc()
    res.type = VkStructureType.DESCRIPTOR_SET_LAYOUT_CREATE_INFO
    res.block()
    return res
}
inline fun cVkPipelineColorBlendAttachmentState(capacity: Int): VkPipelineColorBlendAttachmentState.Buffer {
    return VkPipelineColorBlendAttachmentState.calloc(capacity)
}
inline fun cVkPipelineColorBlendAttachmentState(capacity: Int, block: VkPipelineColorBlendAttachmentState.() -> Unit): VkPipelineColorBlendAttachmentState.Buffer {
    val res = cVkPipelineColorBlendAttachmentState(capacity)
    res[0].block()
    return res
}
inline fun cVkPipelineColorBlendStateCreateInfo(block: VkPipelineColorBlendStateCreateInfo.() -> Unit): VkPipelineColorBlendStateCreateInfo {
    val res = VkPipelineColorBlendStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_COLOR_BLEND_STATE_CREATE_INFO
    res.block()
    return res
}
inline fun cVkPipelineDepthStencilStateCreateInfo(block: VkPipelineDepthStencilStateCreateInfo.() -> Unit): VkPipelineDepthStencilStateCreateInfo {
    val res = VkPipelineDepthStencilStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO
    res.block()
    return res
}
inline fun cVkPipelineDynamicStateCreateInfo(block: VkPipelineDynamicStateCreateInfo.() -> Unit): VkPipelineDynamicStateCreateInfo {
    val res = VkPipelineDynamicStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_DYNAMIC_STATE_CREATE_INFO
    res.block()
    return res
}
inline fun cVkPipelineInputAssemblyStateCreateInfo(block: VkPipelineInputAssemblyStateCreateInfo.() -> Unit): VkPipelineInputAssemblyStateCreateInfo {
    val res = VkPipelineInputAssemblyStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO
    res.block()
    return res
}

inline fun cVkPipelineMultisampleStateCreateInfo(block: VkPipelineMultisampleStateCreateInfo.() -> Unit): VkPipelineMultisampleStateCreateInfo {
    val res = VkPipelineMultisampleStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_MULTISAMPLE_STATE_CREATE_INFO
    res.block()
    return res
}

inline fun cVkPipelineRasterizationStateCreateInfo(block: VkPipelineRasterizationStateCreateInfo.() -> Unit): VkPipelineRasterizationStateCreateInfo {
    val res = VkPipelineRasterizationStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_RASTERIZATION_STATE_CREATE_INFO
    res.block()
    return res
}
inline fun cVkPipelineShaderStageCreateInfo(block: VkPipelineShaderStageCreateInfo.() -> Unit): VkPipelineShaderStageCreateInfo {
    val res = VkPipelineShaderStageCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_SHADER_STAGE_CREATE_INFO
    res.block()
    return res
}

inline fun cVkPipelineVertexInputStateCreateInfo(block: VkPipelineVertexInputStateCreateInfo.() -> Unit): VkPipelineVertexInputStateCreateInfo {
    val res = VkPipelineVertexInputStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO
    res.block()
    return res
}
inline fun cVkPipelineViewportStateCreateInfo(block: VkPipelineViewportStateCreateInfo.() -> Unit): VkPipelineViewportStateCreateInfo {
    val res = VkPipelineViewportStateCreateInfo.calloc()
    res.type = VkStructureType.PIPELINE_VIEWPORT_STATE_CREATE_INFO
    res.block()
    return res
}

inline fun cVkSemaphoreCreateInfo(block: VkSemaphoreCreateInfo.() -> Unit): VkSemaphoreCreateInfo {
    val res = VkSemaphoreCreateInfo.calloc()
    res.type = VkStructureType.SEMAPHORE_CREATE_INFO
    res.block()
    return res
}
inline fun cVkSubmitInfo(block: VkSubmitInfo.() -> Unit): VkSubmitInfo {
    val res = VkSubmitInfo.calloc()
    res.type = VkStructureType.SUBMIT_INFO
    res.block()
    return res
}

fun cVkVertexInputBindingDescription(capacity: Int, block:VkVertexInputBindingDescription.() -> Unit): VkVertexInputBindingDescription.Buffer {
    val res = VkVertexInputBindingDescription.calloc(capacity)
    res[0].block()
    return res
}
