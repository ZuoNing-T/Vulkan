package vkn

import vulkan.base.tools


typealias VkFlags = Int

typealias VkPipelineCacheHeaderversion = Int

val VkPipelineCacheHeaderVersion_ONE: VkPipelineCacheHeaderversion = 1

val VkPipelineCacheHeaderVersion_BEGIN_RANGE = VkPipelineCacheHeaderVersion_ONE
val VkPipelineCacheHeaderVersion_END_RANGE = VkPipelineCacheHeaderVersion_ONE
val VkPipelineCacheHeaderVersion_RANGE_SIZE = VkPipelineCacheHeaderVersion_ONE - VkPipelineCacheHeaderVersion_ONE + 1

typealias VkResult = Int

typealias VkBool32 = Int

val Vk_SUCCESS: VkResult = 0
val Vk_NOT_READY: VkResult = 1
val Vk_TIMEOUT: VkResult = 2
val Vk_EVENT_SET: VkResult = 3
val Vk_EVENT_RESET: VkResult = 4
val Vk_INCOMPLETE: VkResult = 5
val Vk_ERROR_OUT_OF_HOST_MEMORY: VkResult = -1
val Vk_ERROR_OUT_OF_DEVICE_MEMORY: VkResult = -2
val Vk_ERROR_INITIALIZATION_FAILED: VkResult = -3
val Vk_ERROR_DEVICE_LOST: VkResult = -4
val Vk_ERROR_MEMORY_MAP_FAILED: VkResult = -5
val Vk_ERROR_LAYER_NOT_PRESENT: VkResult = -6
val Vk_ERROR_EXTENSION_NOT_PRESENT: VkResult = -7
val Vk_ERROR_FEATURE_NOT_PRESENT: VkResult = -8
val Vk_ERROR_INCOMPATIBLE_DRIVER: VkResult = -9
val Vk_ERROR_TOO_MANY_OBJECTS: VkResult = -10
val Vk_ERROR_FORMAT_NOT_SUPPORTED: VkResult = -11
val Vk_ERROR_FRAGMENTED_POOL: VkResult = -12
val Vk_ERROR_SURFACE_LOST_KHR: VkResult = -1000000000
val Vk_ERROR_NATIVE_WINDOW_IN_USE_KHR: VkResult = -1000000001
val Vk_SUBOPTIMAL_KHR: VkResult = 1000001003
val Vk_ERROR_OUT_OF_DATE_KHR: VkResult = -1000001004
val Vk_ERROR_INCOMPATIBLE_DISPLAY_KHR: VkResult = -1000003001
val Vk_ERROR_VALIDATION_FAILED_EXT: VkResult = -1000011001
val Vk_ERROR_INVALID_SHADER_NV: VkResult = -1000012000
val Vk_ERROR_OUT_OF_POOL_MEMORY_KHR: VkResult = -1000069000
val Vk_ERROR_INVALID_EXTERNAL_HANDLE_KHR: VkResult = -1000072003
val Vk_ERROR_NOT_PERMITTED_EXT: VkResult = -1000174001

val Vk_RESULT_BEGIN_RANGE = Vk_ERROR_FRAGMENTED_POOL
val Vk_RESULT_END_RANGE = Vk_INCOMPLETE
val Vk_RESULT_RANGE_SIZE = Vk_INCOMPLETE - Vk_ERROR_FRAGMENTED_POOL + 1

val VkResult.string get() = tools.errorString(this)
operator fun VkResult.invoke() = this != Vk_SUCCESS


fun VkResult.check() {
    if (this != Vk_SUCCESS)
        throw Error("Fatal : VkResult is $this")
}

typealias VkStructureType = Int

val VkStructureType_APPLICATION_INFO: VkStructureType = 0
val VkStructureType_INSTANCE_CREATE_INFO: VkStructureType = 1
val VkStructureType_DEVICE_QUEUE_CREATE_INFO: VkStructureType = 2
val VkStructureType_DEVICE_CREATE_INFO: VkStructureType = 3
val VkStructureType_SUBMIT_INFO: VkStructureType = 4
val VkStructureType_MEMORY_ALLOCATE_INFO: VkStructureType = 5
val VkStructureType_MAPPED_MEMORY_RANGE: VkStructureType = 6
val VkStructureType_BIND_SPARSE_INFO: VkStructureType = 7
val VkStructureType_FENCE_CREATE_INFO: VkStructureType = 8
val VkStructureType_SEMAPHORE_CREATE_INFO: VkStructureType = 9
val VkStructureType_EVENT_CREATE_INFO: VkStructureType = 10
val VkStructureType_QUERY_POOL_CREATE_INFO: VkStructureType = 11
val VkStructureType_BUFFER_CREATE_INFO: VkStructureType = 12
val VkStructureType_BUFFER_VIEW_CREATE_INFO: VkStructureType = 13
val VkStructureType_IMAGE_CREATE_INFO: VkStructureType = 14
val VkStructureType_IMAGE_VIEW_CREATE_INFO: VkStructureType = 15
val VkStructureType_SHADER_MODULE_CREATE_INFO: VkStructureType = 16
val VkStructureType_PIPELINE_CACHE_CREATE_INFO: VkStructureType = 17
val VkStructureType_PIPELINE_SHADER_STAGE_CREATE_INFO: VkStructureType = 18
val VkStructureType_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO: VkStructureType = 19
val VkStructureType_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO: VkStructureType = 20
val VkStructureType_PIPELINE_TESSELLATION_STATE_CREATE_INFO: VkStructureType = 21
val VkStructureType_PIPELINE_VIEWPORT_STATE_CREATE_INFO: VkStructureType = 22
val VkStructureType_PIPELINE_RASTERIZATION_STATE_CREATE_INFO: VkStructureType = 23
val VkStructureType_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO: VkStructureType = 24
val VkStructureType_PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO: VkStructureType = 25
val VkStructureType_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO: VkStructureType = 26
val VkStructureType_PIPELINE_DYNAMIC_STATE_CREATE_INFO: VkStructureType = 27
val VkStructureType_GRAPHICS_PIPELINE_CREATE_INFO: VkStructureType = 28
val VkStructureType_COMPUTE_PIPELINE_CREATE_INFO: VkStructureType = 29
val VkStructureType_PIPELINE_LAYOUT_CREATE_INFO: VkStructureType = 30
val VkStructureType_SAMPLER_CREATE_INFO: VkStructureType = 31
val VkStructureType_DESCRIPTOR_SET_LAYOUT_CREATE_INFO: VkStructureType = 32
val VkStructureType_DESCRIPTOR_POOL_CREATE_INFO: VkStructureType = 33
val VkStructureType_DESCRIPTOR_SET_ALLOCATE_INFO: VkStructureType = 34
val VkStructureType_WRITE_DESCRIPTOR_SET: VkStructureType = 35
val VkStructureType_COPY_DESCRIPTOR_SET: VkStructureType = 36
val VkStructureType_FRAMEBUFFER_CREATE_INFO: VkStructureType = 37
val VkStructureType_RENDER_PASS_CREATE_INFO: VkStructureType = 38
val VkStructureType_COMMAND_POOL_CREATE_INFO: VkStructureType = 39
val VkStructureType_COMMAND_BUFFER_ALLOCATE_INFO: VkStructureType = 40
val VkStructureType_COMMAND_BUFFER_INHERITANCE_INFO: VkStructureType = 41
val VkStructureType_COMMAND_BUFFER_BEGIN_INFO: VkStructureType = 42
val VkStructureType_RENDER_PASS_BEGIN_INFO: VkStructureType = 43
val VkStructureType_BUFFER_MEMORY_BARRIER: VkStructureType = 44
val VkStructureType_IMAGE_MEMORY_BARRIER: VkStructureType = 45
val VkStructureType_MEMORY_BARRIER: VkStructureType = 46
val VkStructureType_LOADER_INSTANCE_CREATE_INFO: VkStructureType = 47
val VkStructureType_LOADER_DEVICE_CREATE_INFO: VkStructureType = 48
val VkStructureType_SWAPCHAIN_CREATE_INFO_KHR: VkStructureType = 1000001000
val VkStructureType_PRESENT_INFO_KHR: VkStructureType = 1000001001
val VkStructureType_DISPLAY_MODE_CREATE_INFO_KHR: VkStructureType = 1000002000
val VkStructureType_DISPLAY_SURFACE_CREATE_INFO_KHR: VkStructureType = 1000002001
val VkStructureType_DISPLAY_PRESENT_INFO_KHR: VkStructureType = 1000003000
val VkStructureType_XLIB_SURFACE_CREATE_INFO_KHR: VkStructureType = 1000004000
val VkStructureType_XCB_SURFACE_CREATE_INFO_KHR: VkStructureType = 1000005000
val VkStructureType_WAYLAND_SURFACE_CREATE_INFO_KHR: VkStructureType = 1000006000
val VkStructureType_MIR_SURFACE_CREATE_INFO_KHR: VkStructureType = 1000007000
val VkStructureType_ANDROID_SURFACE_CREATE_INFO_KHR: VkStructureType = 1000008000
val VkStructureType_WIN32_SURFACE_CREATE_INFO_KHR: VkStructureType = 1000009000
val VkStructureType_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT: VkStructureType = 1000011000
val VkStructureType_PIPELINE_RASTERIZATION_STATE_RASTERIZATION_ORDER_AMD: VkStructureType = 1000018000
val VkStructureType_DEBUG_MARKER_OBJECT_NAME_INFO_EXT: VkStructureType = 1000022000
val VkStructureType_DEBUG_MARKER_OBJECT_TAG_INFO_EXT: VkStructureType = 1000022001
val VkStructureType_DEBUG_MARKER_MARKER_INFO_EXT: VkStructureType = 1000022002
val VkStructureType_DEDICATED_ALLOCATION_IMAGE_CREATE_INFO_NV: VkStructureType = 1000026000
val VkStructureType_DEDICATED_ALLOCATION_BUFFER_CREATE_INFO_NV: VkStructureType = 1000026001
val VkStructureType_DEDICATED_ALLOCATION_MEMORY_ALLOCATE_INFO_NV: VkStructureType = 1000026002
val VkStructureType_TEXTURE_LOD_GATHER_FORMAT_PROPERTIES_AMD: VkStructureType = 1000041000
val VkStructureType_RENDER_PASS_MULTIVIEW_CREATE_INFO_KHX: VkStructureType = 1000053000
val VkStructureType_PHYSICAL_DEVICE_MULTIVIEW_FEATURES_KHX: VkStructureType = 1000053001
val VkStructureType_PHYSICAL_DEVICE_MULTIVIEW_PROPERTIES_KHX: VkStructureType = 1000053002
val VkStructureType_EXTERNAL_MEMORY_IMAGE_CREATE_INFO_NV: VkStructureType = 1000056000
val VkStructureType_EXPORT_MEMORY_ALLOCATE_INFO_NV: VkStructureType = 1000056001
val VkStructureType_IMPORT_MEMORY_WIN32_HANDLE_INFO_NV: VkStructureType = 1000057000
val VkStructureType_EXPORT_MEMORY_WIN32_HANDLE_INFO_NV: VkStructureType = 1000057001
val VkStructureType_WIN32_KEYED_MUTEX_ACQUIRE_RELEASE_INFO_NV: VkStructureType = 1000058000
val VkStructureType_PHYSICAL_DEVICE_FEATURES_2_KHR: VkStructureType = 1000059000
val VkStructureType_PHYSICAL_DEVICE_PROPERTIES_2_KHR: VkStructureType = 1000059001
val VkStructureType_FORMAT_PROPERTIES_2_KHR: VkStructureType = 1000059002
val VkStructureType_IMAGE_FORMAT_PROPERTIES_2_KHR: VkStructureType = 1000059003
val VkStructureType_PHYSICAL_DEVICE_IMAGE_FORMAT_INFO_2_KHR: VkStructureType = 1000059004
val VkStructureType_QUEUE_FAMILY_PROPERTIES_2_KHR: VkStructureType = 1000059005
val VkStructureType_PHYSICAL_DEVICE_MEMORY_PROPERTIES_2_KHR: VkStructureType = 1000059006
val VkStructureType_SPARSE_IMAGE_FORMAT_PROPERTIES_2_KHR: VkStructureType = 1000059007
val VkStructureType_PHYSICAL_DEVICE_SPARSE_IMAGE_FORMAT_INFO_2_KHR: VkStructureType = 1000059008
val VkStructureType_MEMORY_ALLOCATE_FLAGS_INFO_KHX: VkStructureType = 1000060000
val VkStructureType_DEVICE_GROUP_RENDER_PASS_BEGIN_INFO_KHX: VkStructureType = 1000060003
val VkStructureType_DEVICE_GROUP_COMMAND_BUFFER_BEGIN_INFO_KHX: VkStructureType = 1000060004
val VkStructureType_DEVICE_GROUP_SUBMIT_INFO_KHX: VkStructureType = 1000060005
val VkStructureType_DEVICE_GROUP_BIND_SPARSE_INFO_KHX: VkStructureType = 1000060006
val VkStructureType_ACQUIRE_NEXT_IMAGE_INFO_KHX: VkStructureType = 1000060010
val VkStructureType_BIND_BUFFER_MEMORY_DEVICE_GROUP_INFO_KHX: VkStructureType = 1000060013
val VkStructureType_BIND_IMAGE_MEMORY_DEVICE_GROUP_INFO_KHX: VkStructureType = 1000060014
val VkStructureType_DEVICE_GROUP_PRESENT_CAPABILITIES_KHX: VkStructureType = 1000060007
val VkStructureType_IMAGE_SWAPCHAIN_CREATE_INFO_KHX: VkStructureType = 1000060008
val VkStructureType_BIND_IMAGE_MEMORY_SWAPCHAIN_INFO_KHX: VkStructureType = 1000060009
val VkStructureType_DEVICE_GROUP_PRESENT_INFO_KHX: VkStructureType = 1000060011
val VkStructureType_DEVICE_GROUP_SWAPCHAIN_CREATE_INFO_KHX: VkStructureType = 1000060012
val VkStructureType_VALIDATION_FLAGS_EXT: VkStructureType = 1000061000
val VkStructureType_VI_SURFACE_CREATE_INFO_NN: VkStructureType = 1000062000
val VkStructureType_PHYSICAL_DEVICE_GROUP_PROPERTIES_KHX: VkStructureType = 1000070000
val VkStructureType_DEVICE_GROUP_DEVICE_CREATE_INFO_KHX: VkStructureType = 1000070001
val VkStructureType_PHYSICAL_DEVICE_EXTERNAL_IMAGE_FORMAT_INFO_KHR: VkStructureType = 1000071000
val VkStructureType_EXTERNAL_IMAGE_FORMAT_PROPERTIES_KHR: VkStructureType = 1000071001
val VkStructureType_PHYSICAL_DEVICE_EXTERNAL_BUFFER_INFO_KHR: VkStructureType = 1000071002
val VkStructureType_EXTERNAL_BUFFER_PROPERTIES_KHR: VkStructureType = 1000071003
val VkStructureType_PHYSICAL_DEVICE_ID_PROPERTIES_KHR: VkStructureType = 1000071004
val VkStructureType_EXTERNAL_MEMORY_BUFFER_CREATE_INFO_KHR: VkStructureType = 1000072000
val VkStructureType_EXTERNAL_MEMORY_IMAGE_CREATE_INFO_KHR: VkStructureType = 1000072001
val VkStructureType_EXPORT_MEMORY_ALLOCATE_INFO_KHR: VkStructureType = 1000072002
val VkStructureType_IMPORT_MEMORY_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000073000
val VkStructureType_EXPORT_MEMORY_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000073001
val VkStructureType_MEMORY_WIN32_HANDLE_PROPERTIES_KHR: VkStructureType = 1000073002
val VkStructureType_MEMORY_GET_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000073003
val VkStructureType_IMPORT_MEMORY_FD_INFO_KHR: VkStructureType = 1000074000
val VkStructureType_MEMORY_FD_PROPERTIES_KHR: VkStructureType = 1000074001
val VkStructureType_MEMORY_GET_FD_INFO_KHR: VkStructureType = 1000074002
val VkStructureType_WIN32_KEYED_MUTEX_ACQUIRE_RELEASE_INFO_KHR: VkStructureType = 1000075000
val VkStructureType_PHYSICAL_DEVICE_EXTERNAL_SEMAPHORE_INFO_KHR: VkStructureType = 1000076000
val VkStructureType_EXTERNAL_SEMAPHORE_PROPERTIES_KHR: VkStructureType = 1000076001
val VkStructureType_EXPORT_SEMAPHORE_CREATE_INFO_KHR: VkStructureType = 1000077000
val VkStructureType_IMPORT_SEMAPHORE_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000078000
val VkStructureType_EXPORT_SEMAPHORE_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000078001
val VkStructureType_D3D12_FENCE_SUBMIT_INFO_KHR: VkStructureType = 1000078002
val VkStructureType_SEMAPHORE_GET_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000078003
val VkStructureType_IMPORT_SEMAPHORE_FD_INFO_KHR: VkStructureType = 1000079000
val VkStructureType_SEMAPHORE_GET_FD_INFO_KHR: VkStructureType = 1000079001
val VkStructureType_PHYSICAL_DEVICE_PUSH_DESCRIPTOR_PROPERTIES_KHR: VkStructureType = 1000080000
val VkStructureType_PHYSICAL_DEVICE_16BIT_STORAGE_FEATURES_KHR: VkStructureType = 1000083000
val VkStructureType_PRESENT_REGIONS_KHR: VkStructureType = 1000084000
val VkStructureType_DESCRIPTOR_UPDATE_TEMPLATE_CREATE_INFO_KHR: VkStructureType = 1000085000
val VkStructureType_OBJECT_TABLE_CREATE_INFO_NVX: VkStructureType = 1000086000
val VkStructureType_INDIRECT_COMMANDS_LAYOUT_CREATE_INFO_NVX: VkStructureType = 1000086001
val VkStructureType_CMD_PROCESS_COMMANDS_INFO_NVX: VkStructureType = 1000086002
val VkStructureType_CMD_RESERVE_SPACE_FOR_COMMANDS_INFO_NVX: VkStructureType = 1000086003
val VkStructureType_DEVICE_GENERATED_COMMANDS_LIMITS_NVX: VkStructureType = 1000086004
val VkStructureType_DEVICE_GENERATED_COMMANDS_FEATURES_NVX: VkStructureType = 1000086005
val VkStructureType_PIPELINE_VIEWPORT_W_SCALING_STATE_CREATE_INFO_NV: VkStructureType = 1000087000
val VkStructureType_SURFACE_CAPABILITIES_2_EXT: VkStructureType = 1000090000
val VkStructureType_DISPLAY_POWER_INFO_EXT: VkStructureType = 1000091000
val VkStructureType_DEVICE_EVENT_INFO_EXT: VkStructureType = 1000091001
val VkStructureType_DISPLAY_EVENT_INFO_EXT: VkStructureType = 1000091002
val VkStructureType_SWAPCHAIN_COUNTER_CREATE_INFO_EXT: VkStructureType = 1000091003
val VkStructureType_PRESENT_TIMES_INFO_GOOGLE: VkStructureType = 1000092000
val VkStructureType_PHYSICAL_DEVICE_MULTIVIEW_PER_VIEW_ATTRIBUTES_PROPERTIES_NVX: VkStructureType = 1000097000
val VkStructureType_PIPELINE_VIEWPORT_SWIZZLE_STATE_CREATE_INFO_NV: VkStructureType = 1000098000
val VkStructureType_PHYSICAL_DEVICE_DISCARD_RECTANGLE_PROPERTIES_EXT: VkStructureType = 1000099000
val VkStructureType_PIPELINE_DISCARD_RECTANGLE_STATE_CREATE_INFO_EXT: VkStructureType = 1000099001
val VkStructureType_PHYSICAL_DEVICE_CONSERVATIVE_RASTERIZATION_PROPERTIES_EXT: VkStructureType = 1000101000
val VkStructureType_PIPELINE_RASTERIZATION_CONSERVATIVE_STATE_CREATE_INFO_EXT: VkStructureType = 1000101001
val VkStructureType_HDR_METADATA_EXT: VkStructureType = 1000105000
val VkStructureType_SHARED_PRESENT_SURFACE_CAPABILITIES_KHR: VkStructureType = 1000111000
val VkStructureType_PHYSICAL_DEVICE_EXTERNAL_FENCE_INFO_KHR: VkStructureType = 1000112000
val VkStructureType_EXTERNAL_FENCE_PROPERTIES_KHR: VkStructureType = 1000112001
val VkStructureType_EXPORT_FENCE_CREATE_INFO_KHR: VkStructureType = 1000113000
val VkStructureType_IMPORT_FENCE_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000114000
val VkStructureType_EXPORT_FENCE_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000114001
val VkStructureType_FENCE_GET_WIN32_HANDLE_INFO_KHR: VkStructureType = 1000114002
val VkStructureType_IMPORT_FENCE_FD_INFO_KHR: VkStructureType = 1000115000
val VkStructureType_FENCE_GET_FD_INFO_KHR: VkStructureType = 1000115001
val VkStructureType_PHYSICAL_DEVICE_POINT_CLIPPING_PROPERTIES_KHR: VkStructureType = 1000117000
val VkStructureType_RENDER_PASS_INPUT_ATTACHMENT_ASPECT_CREATE_INFO_KHR: VkStructureType = 1000117001
val VkStructureType_IMAGE_VIEW_USAGE_CREATE_INFO_KHR: VkStructureType = 1000117002
val VkStructureType_PIPELINE_TESSELLATION_DOMAIN_ORIGIN_STATE_CREATE_INFO_KHR: VkStructureType = 1000117003
val VkStructureType_PHYSICAL_DEVICE_SURFACE_INFO_2_KHR: VkStructureType = 1000119000
val VkStructureType_SURFACE_CAPABILITIES_2_KHR: VkStructureType = 1000119001
val VkStructureType_SURFACE_FORMAT_2_KHR: VkStructureType = 1000119002
val VkStructureType_PHYSICAL_DEVICE_VARIABLE_POINTER_FEATURES_KHR: VkStructureType = 1000120000
val VkStructureType_IOS_SURFACE_CREATE_INFO_MVK: VkStructureType = 1000122000
val VkStructureType_MACOS_SURFACE_CREATE_INFO_MVK: VkStructureType = 1000123000
val VkStructureType_MEMORY_DEDICATED_REQUIREMENTS_KHR: VkStructureType = 1000127000
val VkStructureType_MEMORY_DEDICATED_ALLOCATE_INFO_KHR: VkStructureType = 1000127001
val VkStructureType_PHYSICAL_DEVICE_SAMPLER_FILTER_MINMAX_PROPERTIES_EXT: VkStructureType = 1000130000
val VkStructureType_SAMPLER_REDUCTION_MODE_CREATE_INFO_EXT: VkStructureType = 1000130001
val VkStructureType_SAMPLE_LOCATIONS_INFO_EXT: VkStructureType = 1000143000
val VkStructureType_RENDER_PASS_SAMPLE_LOCATIONS_BEGIN_INFO_EXT: VkStructureType = 1000143001
val VkStructureType_PIPELINE_SAMPLE_LOCATIONS_STATE_CREATE_INFO_EXT: VkStructureType = 1000143002
val VkStructureType_PHYSICAL_DEVICE_SAMPLE_LOCATIONS_PROPERTIES_EXT: VkStructureType = 1000143003
val VkStructureType_MULTISAMPLE_PROPERTIES_EXT: VkStructureType = 1000143004
val VkStructureType_BUFFER_MEMORY_REQUIREMENTS_INFO_2_KHR: VkStructureType = 1000146000
val VkStructureType_IMAGE_MEMORY_REQUIREMENTS_INFO_2_KHR: VkStructureType = 1000146001
val VkStructureType_IMAGE_SPARSE_MEMORY_REQUIREMENTS_INFO_2_KHR: VkStructureType = 1000146002
val VkStructureType_MEMORY_REQUIREMENTS_2_KHR: VkStructureType = 1000146003
val VkStructureType_SPARSE_IMAGE_MEMORY_REQUIREMENTS_2_KHR: VkStructureType = 1000146004
val VkStructureType_IMAGE_FORMAT_LIST_CREATE_INFO_KHR: VkStructureType = 1000147000
val VkStructureType_PHYSICAL_DEVICE_BLEND_OPERATION_ADVANCED_FEATURES_EXT: VkStructureType = 1000148000
val VkStructureType_PHYSICAL_DEVICE_BLEND_OPERATION_ADVANCED_PROPERTIES_EXT: VkStructureType = 1000148001
val VkStructureType_PIPELINE_COLOR_BLEND_ADVANCED_STATE_CREATE_INFO_EXT: VkStructureType = 1000148002
val VkStructureType_PIPELINE_COVERAGE_TO_COLOR_STATE_CREATE_INFO_NV: VkStructureType = 1000149000
val VkStructureType_PIPELINE_COVERAGE_MODULATION_STATE_CREATE_INFO_NV: VkStructureType = 1000152000
val VkStructureType_SAMPLER_YCBCR_CONVERSION_CREATE_INFO_KHR: VkStructureType = 1000156000
val VkStructureType_SAMPLER_YCBCR_CONVERSION_INFO_KHR: VkStructureType = 1000156001
val VkStructureType_BIND_IMAGE_PLANE_MEMORY_INFO_KHR: VkStructureType = 1000156002
val VkStructureType_IMAGE_PLANE_MEMORY_REQUIREMENTS_INFO_KHR: VkStructureType = 1000156003
val VkStructureType_PHYSICAL_DEVICE_SAMPLER_YCBCR_CONVERSION_FEATURES_KHR: VkStructureType = 1000156004
val VkStructureType_SAMPLER_YCBCR_CONVERSION_IMAGE_FORMAT_PROPERTIES_KHR: VkStructureType = 1000156005
val VkStructureType_BIND_BUFFER_MEMORY_INFO_KHR: VkStructureType = 1000157000
val VkStructureType_BIND_IMAGE_MEMORY_INFO_KHR: VkStructureType = 1000157001
val VkStructureType_VALIDATION_CACHE_CREATE_INFO_EXT: VkStructureType = 1000160000
val VkStructureType_SHADER_MODULE_VALIDATION_CACHE_CREATE_INFO_EXT: VkStructureType = 1000160001
val VkStructureType_DEVICE_QUEUE_GLOBAL_PRIORITY_CREATE_INFO_EXT: VkStructureType = 1000174000
val VkStructureType_IMPORT_MEMORY_HOST_POINTER_INFO_EXT: VkStructureType = 1000178000
val VkStructureType_MEMORY_HOST_POINTER_PROPERTIES_EXT: VkStructureType = 1000178001
val VkStructureType_PHYSICAL_DEVICE_EXTERNAL_MEMORY_HOST_PROPERTIES_EXT: VkStructureType = 1000178002

val VkStructureType_BEGIN_RANGE = VkStructureType_APPLICATION_INFO
val VkStructureType_END_RANGE = VkStructureType_LOADER_DEVICE_CREATE_INFO
val VkStructureType_RANGE_SIZE = VkStructureType_LOADER_DEVICE_CREATE_INFO - VkStructureType_APPLICATION_INFO + 1


//typedef enum VkSystemAllocationScope {
//    VK_SYSTEM_ALLOCATION_SCOPE_COMMAND = 0,
//    VK_SYSTEM_ALLOCATION_SCOPE_OBJECT = 1,
//    VK_SYSTEM_ALLOCATION_SCOPE_CACHE = 2,
//    VK_SYSTEM_ALLOCATION_SCOPE_DEVICE = 3,
//    VK_SYSTEM_ALLOCATION_SCOPE_INSTANCE = 4,
//    VK_SYSTEM_ALLOCATION_SCOPE_BEGIN_RANGE = VK_SYSTEM_ALLOCATION_SCOPE_COMMAND,
//    VK_SYSTEM_ALLOCATION_SCOPE_END_RANGE = VK_SYSTEM_ALLOCATION_SCOPE_INSTANCE,
//    VK_SYSTEM_ALLOCATION_SCOPE_RANGE_SIZE = (VK_SYSTEM_ALLOCATION_SCOPE_INSTANCE - VK_SYSTEM_ALLOCATION_SCOPE_COMMAND + 1),
//    VK_SYSTEM_ALLOCATION_SCOPE_MAX_ENUM = 0x7FFFFFFF
//} VkSystemAllocationScope;
//
//typedef enum VkInternalAllocationType {
//    VK_INTERNAL_ALLOCATION_TYPE_EXECUTABLE = 0,
//    VK_INTERNAL_ALLOCATION_TYPE_BEGIN_RANGE = VK_INTERNAL_ALLOCATION_TYPE_EXECUTABLE,
//    VK_INTERNAL_ALLOCATION_TYPE_END_RANGE = VK_INTERNAL_ALLOCATION_TYPE_EXECUTABLE,
//    VK_INTERNAL_ALLOCATION_TYPE_RANGE_SIZE = (VK_INTERNAL_ALLOCATION_TYPE_EXECUTABLE - VK_INTERNAL_ALLOCATION_TYPE_EXECUTABLE + 1),
//    VK_INTERNAL_ALLOCATION_TYPE_MAX_ENUM = 0x7FFFFFFF
//} VkInternalAllocationType;
//
typealias VkFormat = Int

val VkFormat_UNDEFINED: VkFormat = 0
val VkFormat_R4G4_UNORM_PACK8: VkFormat = 1
val VkFormat_R4G4B4A4_UNORM_PACK16: VkFormat = 2
val VkFormat_B4G4R4A4_UNORM_PACK16: VkFormat = 3
val VkFormat_R5G6B5_UNORM_PACK16: VkFormat = 4
val VkFormat_B5G6R5_UNORM_PACK16: VkFormat = 5
val VkFormat_R5G5B5A1_UNORM_PACK16: VkFormat = 6
val VkFormat_B5G5R5A1_UNORM_PACK16: VkFormat = 7
val VkFormat_A1R5G5B5_UNORM_PACK16: VkFormat = 8
val VkFormat_R8_UNORM: VkFormat = 9
val VkFormat_R8_SNORM: VkFormat = 10
val VkFormat_R8_USCALED: VkFormat = 11
val VkFormat_R8_SSCALED: VkFormat = 12
val VkFormat_R8_UINT: VkFormat = 13
val VkFormat_R8_SINT: VkFormat = 14
val VkFormat_R8_SRGB: VkFormat = 15
val VkFormat_R8G8_UNORM: VkFormat = 16
val VkFormat_R8G8_SNORM: VkFormat = 17
val VkFormat_R8G8_USCALED: VkFormat = 18
val VkFormat_R8G8_SSCALED: VkFormat = 19
val VkFormat_R8G8_UINT: VkFormat = 20
val VkFormat_R8G8_SINT: VkFormat = 21
val VkFormat_R8G8_SRGB: VkFormat = 22
val VkFormat_R8G8B8_UNORM: VkFormat = 23
val VkFormat_R8G8B8_SNORM: VkFormat = 24
val VkFormat_R8G8B8_USCALED: VkFormat = 25
val VkFormat_R8G8B8_SSCALED: VkFormat = 26
val VkFormat_R8G8B8_UINT: VkFormat = 27
val VkFormat_R8G8B8_SINT: VkFormat = 28
val VkFormat_R8G8B8_SRGB: VkFormat = 29
val VkFormat_B8G8R8_UNORM: VkFormat = 30
val VkFormat_B8G8R8_SNORM: VkFormat = 31
val VkFormat_B8G8R8_USCALED: VkFormat = 32
val VkFormat_B8G8R8_SSCALED: VkFormat = 33
val VkFormat_B8G8R8_UINT: VkFormat = 34
val VkFormat_B8G8R8_SINT: VkFormat = 35
val VkFormat_B8G8R8_SRGB: VkFormat = 36
val VkFormat_R8G8B8A8_UNORM: VkFormat = 37
val VkFormat_R8G8B8A8_SNORM: VkFormat = 38
val VkFormat_R8G8B8A8_USCALED: VkFormat = 39
val VkFormat_R8G8B8A8_SSCALED: VkFormat = 40
val VkFormat_R8G8B8A8_UINT: VkFormat = 41
val VkFormat_R8G8B8A8_SINT: VkFormat = 42
val VkFormat_R8G8B8A8_SRGB: VkFormat = 43
val VkFormat_B8G8R8A8_UNORM: VkFormat = 44
val VkFormat_B8G8R8A8_SNORM: VkFormat = 45
val VkFormat_B8G8R8A8_USCALED: VkFormat = 46
val VkFormat_B8G8R8A8_SSCALED: VkFormat = 47
val VkFormat_B8G8R8A8_UINT: VkFormat = 48
val VkFormat_B8G8R8A8_SINT: VkFormat = 49
val VkFormat_B8G8R8A8_SRGB: VkFormat = 50
val VkFormat_A8B8G8R8_UNORM_PACK32: VkFormat = 51
val VkFormat_A8B8G8R8_SNORM_PACK32: VkFormat = 52
val VkFormat_A8B8G8R8_USCALED_PACK32: VkFormat = 53
val VkFormat_A8B8G8R8_SSCALED_PACK32: VkFormat = 54
val VkFormat_A8B8G8R8_UINT_PACK32: VkFormat = 55
val VkFormat_A8B8G8R8_SINT_PACK32: VkFormat = 56
val VkFormat_A8B8G8R8_SRGB_PACK32: VkFormat = 57
val VkFormat_A2R10G10B10_UNORM_PACK32: VkFormat = 58
val VkFormat_A2R10G10B10_SNORM_PACK32: VkFormat = 59
val VkFormat_A2R10G10B10_USCALED_PACK32: VkFormat = 60
val VkFormat_A2R10G10B10_SSCALED_PACK32: VkFormat = 61
val VkFormat_A2R10G10B10_UINT_PACK32: VkFormat = 62
val VkFormat_A2R10G10B10_SINT_PACK32: VkFormat = 63
val VkFormat_A2B10G10R10_UNORM_PACK32: VkFormat = 64
val VkFormat_A2B10G10R10_SNORM_PACK32: VkFormat = 65
val VkFormat_A2B10G10R10_USCALED_PACK32: VkFormat = 66
val VkFormat_A2B10G10R10_SSCALED_PACK32: VkFormat = 67
val VkFormat_A2B10G10R10_UINT_PACK32: VkFormat = 68
val VkFormat_A2B10G10R10_SINT_PACK32: VkFormat = 69
val VkFormat_R16_UNORM: VkFormat = 70
val VkFormat_R16_SNORM: VkFormat = 71
val VkFormat_R16_USCALED: VkFormat = 72
val VkFormat_R16_SSCALED: VkFormat = 73
val VkFormat_R16_UINT: VkFormat = 74
val VkFormat_R16_SINT: VkFormat = 75
val VkFormat_R16_SFLOAT: VkFormat = 76
val VkFormat_R16G16_UNORM: VkFormat = 77
val VkFormat_R16G16_SNORM: VkFormat = 78
val VkFormat_R16G16_USCALED: VkFormat = 79
val VkFormat_R16G16_SSCALED: VkFormat = 80
val VkFormat_R16G16_UINT: VkFormat = 81
val VkFormat_R16G16_SINT: VkFormat = 82
val VkFormat_R16G16_SFLOAT: VkFormat = 83
val VkFormat_R16G16B16_UNORM: VkFormat = 84
val VkFormat_R16G16B16_SNORM: VkFormat = 85
val VkFormat_R16G16B16_USCALED: VkFormat = 86
val VkFormat_R16G16B16_SSCALED: VkFormat = 87
val VkFormat_R16G16B16_UINT: VkFormat = 88
val VkFormat_R16G16B16_SINT: VkFormat = 89
val VkFormat_R16G16B16_SFLOAT: VkFormat = 90
val VkFormat_R16G16B16A16_UNORM: VkFormat = 91
val VkFormat_R16G16B16A16_SNORM: VkFormat = 92
val VkFormat_R16G16B16A16_USCALED: VkFormat = 93
val VkFormat_R16G16B16A16_SSCALED: VkFormat = 94
val VkFormat_R16G16B16A16_UINT: VkFormat = 95
val VkFormat_R16G16B16A16_SINT: VkFormat = 96
val VkFormat_R16G16B16A16_SFLOAT: VkFormat = 97
val VkFormat_R32_UINT: VkFormat = 98
val VkFormat_R32_SINT: VkFormat = 99
val VkFormat_R32_SFLOAT: VkFormat = 100
val VkFormat_R32G32_UINT: VkFormat = 101
val VkFormat_R32G32_SINT: VkFormat = 102
val VkFormat_R32G32_SFLOAT: VkFormat = 103
val VkFormat_R32G32B32_UINT: VkFormat = 104
val VkFormat_R32G32B32_SINT: VkFormat = 105
val VkFormat_R32G32B32_SFLOAT: VkFormat = 106
val VkFormat_R32G32B32A32_UINT: VkFormat = 107
val VkFormat_R32G32B32A32_SINT: VkFormat = 108
val VkFormat_R32G32B32A32_SFLOAT: VkFormat = 109
val VkFormat_R64_UINT: VkFormat = 110
val VkFormat_R64_SINT: VkFormat = 111
val VkFormat_R64_SFLOAT: VkFormat = 112
val VkFormat_R64G64_UINT: VkFormat = 113
val VkFormat_R64G64_SINT: VkFormat = 114
val VkFormat_R64G64_SFLOAT: VkFormat = 115
val VkFormat_R64G64B64_UINT: VkFormat = 116
val VkFormat_R64G64B64_SINT: VkFormat = 117
val VkFormat_R64G64B64_SFLOAT: VkFormat = 118
val VkFormat_R64G64B64A64_UINT: VkFormat = 119
val VkFormat_R64G64B64A64_SINT: VkFormat = 120
val VkFormat_R64G64B64A64_SFLOAT: VkFormat = 121
val VkFormat_B10G11R11_UFLOAT_PACK32: VkFormat = 122
val VkFormat_E5B9G9R9_UFLOAT_PACK32: VkFormat = 123
val VkFormat_D16_UNORM: VkFormat = 124
val VkFormat_X8_D24_UNORM_PACK32: VkFormat = 125
val VkFormat_D32_SFLOAT: VkFormat = 126
val VkFormat_S8_UINT: VkFormat = 127
val VkFormat_D16_UNORM_S8_UINT: VkFormat = 128
val VkFormat_D24_UNORM_S8_UINT: VkFormat = 129
val VkFormat_D32_SFLOAT_S8_UINT: VkFormat = 130
val VkFormat_BC1_RGB_UNORM_BLOCK: VkFormat = 131
val VkFormat_BC1_RGB_SRGB_BLOCK: VkFormat = 132
val VkFormat_BC1_RGBA_UNORM_BLOCK: VkFormat = 133
val VkFormat_BC1_RGBA_SRGB_BLOCK: VkFormat = 134
val VkFormat_BC2_UNORM_BLOCK: VkFormat = 135
val VkFormat_BC2_SRGB_BLOCK: VkFormat = 136
val VkFormat_BC3_UNORM_BLOCK: VkFormat = 137
val VkFormat_BC3_SRGB_BLOCK: VkFormat = 138
val VkFormat_BC4_UNORM_BLOCK: VkFormat = 139
val VkFormat_BC4_SNORM_BLOCK: VkFormat = 140
val VkFormat_BC5_UNORM_BLOCK: VkFormat = 141
val VkFormat_BC5_SNORM_BLOCK: VkFormat = 142
val VkFormat_BC6H_UFLOAT_BLOCK: VkFormat = 143
val VkFormat_BC6H_SFLOAT_BLOCK: VkFormat = 144
val VkFormat_BC7_UNORM_BLOCK: VkFormat = 145
val VkFormat_BC7_SRGB_BLOCK: VkFormat = 146
val VkFormat_ETC2_R8G8B8_UNORM_BLOCK: VkFormat = 147
val VkFormat_ETC2_R8G8B8_SRGB_BLOCK: VkFormat = 148
val VkFormat_ETC2_R8G8B8A1_UNORM_BLOCK: VkFormat = 149
val VkFormat_ETC2_R8G8B8A1_SRGB_BLOCK: VkFormat = 150
val VkFormat_ETC2_R8G8B8A8_UNORM_BLOCK: VkFormat = 151
val VkFormat_ETC2_R8G8B8A8_SRGB_BLOCK: VkFormat = 152
val VkFormat_EAC_R11_UNORM_BLOCK: VkFormat = 153
val VkFormat_EAC_R11_SNORM_BLOCK: VkFormat = 154
val VkFormat_EAC_R11G11_UNORM_BLOCK: VkFormat = 155
val VkFormat_EAC_R11G11_SNORM_BLOCK: VkFormat = 156
val VkFormat_ASTC_4x4_UNORM_BLOCK: VkFormat = 157
val VkFormat_ASTC_4x4_SRGB_BLOCK: VkFormat = 158
val VkFormat_ASTC_5x4_UNORM_BLOCK: VkFormat = 159
val VkFormat_ASTC_5x4_SRGB_BLOCK: VkFormat = 160
val VkFormat_ASTC_5x5_UNORM_BLOCK: VkFormat = 161
val VkFormat_ASTC_5x5_SRGB_BLOCK: VkFormat = 162
val VkFormat_ASTC_6x5_UNORM_BLOCK: VkFormat = 163
val VkFormat_ASTC_6x5_SRGB_BLOCK: VkFormat = 164
val VkFormat_ASTC_6x6_UNORM_BLOCK: VkFormat = 165
val VkFormat_ASTC_6x6_SRGB_BLOCK: VkFormat = 166
val VkFormat_ASTC_8x5_UNORM_BLOCK: VkFormat = 167
val VkFormat_ASTC_8x5_SRGB_BLOCK: VkFormat = 168
val VkFormat_ASTC_8x6_UNORM_BLOCK: VkFormat = 169
val VkFormat_ASTC_8x6_SRGB_BLOCK: VkFormat = 170
val VkFormat_ASTC_8x8_UNORM_BLOCK: VkFormat = 171
val VkFormat_ASTC_8x8_SRGB_BLOCK: VkFormat = 172
val VkFormat_ASTC_10x5_UNORM_BLOCK: VkFormat = 173
val VkFormat_ASTC_10x5_SRGB_BLOCK: VkFormat = 174
val VkFormat_ASTC_10x6_UNORM_BLOCK: VkFormat = 175
val VkFormat_ASTC_10x6_SRGB_BLOCK: VkFormat = 176
val VkFormat_ASTC_10x8_UNORM_BLOCK: VkFormat = 177
val VkFormat_ASTC_10x8_SRGB_BLOCK: VkFormat = 178
val VkFormat_ASTC_10x10_UNORM_BLOCK: VkFormat = 179
val VkFormat_ASTC_10x10_SRGB_BLOCK: VkFormat = 180
val VkFormat_ASTC_12x10_UNORM_BLOCK: VkFormat = 181
val VkFormat_ASTC_12x10_SRGB_BLOCK: VkFormat = 182
val VkFormat_ASTC_12x12_UNORM_BLOCK: VkFormat = 183
val VkFormat_ASTC_12x12_SRGB_BLOCK: VkFormat = 184
val VkFormat_PVRTC1_2BPP_UNORM_BLOCK_IMG: VkFormat = 1000054000
val VkFormat_PVRTC1_4BPP_UNORM_BLOCK_IMG: VkFormat = 1000054001
val VkFormat_PVRTC2_2BPP_UNORM_BLOCK_IMG: VkFormat = 1000054002
val VkFormat_PVRTC2_4BPP_UNORM_BLOCK_IMG: VkFormat = 1000054003
val VkFormat_PVRTC1_2BPP_SRGB_BLOCK_IMG: VkFormat = 1000054004
val VkFormat_PVRTC1_4BPP_SRGB_BLOCK_IMG: VkFormat = 1000054005
val VkFormat_PVRTC2_2BPP_SRGB_BLOCK_IMG: VkFormat = 1000054006
val VkFormat_PVRTC2_4BPP_SRGB_BLOCK_IMG: VkFormat = 1000054007
val VkFormat_G8B8G8R8_422_UNORM_KHR: VkFormat = 1000156000
val VkFormat_B8G8R8G8_422_UNORM_KHR: VkFormat = 1000156001
val VkFormat_G8_B8_R8_3PLANE_420_UNORM_KHR: VkFormat = 1000156002
val VkFormat_G8_B8R8_2PLANE_420_UNORM_KHR: VkFormat = 1000156003
val VkFormat_G8_B8_R8_3PLANE_422_UNORM_KHR: VkFormat = 1000156004
val VkFormat_G8_B8R8_2PLANE_422_UNORM_KHR: VkFormat = 1000156005
val VkFormat_G8_B8_R8_3PLANE_444_UNORM_KHR: VkFormat = 1000156006
val VkFormat_R10X6_UNORM_PACK16_KHR: VkFormat = 1000156007
val VkFormat_R10X6G10X6_UNORM_2PACK16_KHR: VkFormat = 1000156008
val VkFormat_R10X6G10X6B10X6A10X6_UNORM_4PACK16_KHR: VkFormat = 1000156009
val VkFormat_G10X6B10X6G10X6R10X6_422_UNORM_4PACK16_KHR: VkFormat = 1000156010
val VkFormat_B10X6G10X6R10X6G10X6_422_UNORM_4PACK16_KHR: VkFormat = 1000156011
val VkFormat_G10X6_B10X6_R10X6_3PLANE_420_UNORM_3PACK16_KHR: VkFormat = 1000156012
val VkFormat_G10X6_B10X6R10X6_2PLANE_420_UNORM_3PACK16_KHR: VkFormat = 1000156013
val VkFormat_G10X6_B10X6_R10X6_3PLANE_422_UNORM_3PACK16_KHR: VkFormat = 1000156014
val VkFormat_G10X6_B10X6R10X6_2PLANE_422_UNORM_3PACK16_KHR: VkFormat = 1000156015
val VkFormat_G10X6_B10X6_R10X6_3PLANE_444_UNORM_3PACK16_KHR: VkFormat = 1000156016
val VkFormat_R12X4_UNORM_PACK16_KHR: VkFormat = 1000156017
val VkFormat_R12X4G12X4_UNORM_2PACK16_KHR: VkFormat = 1000156018
val VkFormat_R12X4G12X4B12X4A12X4_UNORM_4PACK16_KHR: VkFormat = 1000156019
val VkFormat_G12X4B12X4G12X4R12X4_422_UNORM_4PACK16_KHR: VkFormat = 1000156020
val VkFormat_B12X4G12X4R12X4G12X4_422_UNORM_4PACK16_KHR: VkFormat = 1000156021
val VkFormat_G12X4_B12X4_R12X4_3PLANE_420_UNORM_3PACK16_KHR: VkFormat = 1000156022
val VkFormat_G12X4_B12X4R12X4_2PLANE_420_UNORM_3PACK16_KHR: VkFormat = 1000156023
val VkFormat_G12X4_B12X4_R12X4_3PLANE_422_UNORM_3PACK16_KHR: VkFormat = 1000156024
val VkFormat_G12X4_B12X4R12X4_2PLANE_422_UNORM_3PACK16_KHR: VkFormat = 1000156025
val VkFormat_G12X4_B12X4_R12X4_3PLANE_444_UNORM_3PACK16_KHR: VkFormat = 1000156026
val VkFormat_G16B16G16R16_422_UNORM_KHR: VkFormat = 1000156027
val VkFormat_B16G16R16G16_422_UNORM_KHR: VkFormat = 1000156028
val VkFormat_G16_B16_R16_3PLANE_420_UNORM_KHR: VkFormat = 1000156029
val VkFormat_G16_B16R16_2PLANE_420_UNORM_KHR: VkFormat = 1000156030
val VkFormat_G16_B16_R16_3PLANE_422_UNORM_KHR: VkFormat = 1000156031
val VkFormat_G16_B16R16_2PLANE_422_UNORM_KHR: VkFormat = 1000156032
val VkFormat_G16_B16_R16_3PLANE_444_UNORM_KHR: VkFormat = 1000156033

val VkFormat_BEGIN_RANGE = VkFormat_UNDEFINED
val VkFormat_END_RANGE = VkFormat_ASTC_12x12_SRGB_BLOCK
val VkFormat_RANGE_SIZE = VkFormat_ASTC_12x12_SRGB_BLOCK - VkFormat_UNDEFINED + 1


typealias VkImageType = Int

val VkImageType_1D: VkImageType = 0
val VkImageType_2D: VkImageType = 1
val VkImageType_3D: VkImageType = 2

val VkImageType_BEGIN_RANGE: Int = VkImageType_1D
val VkImageType_END_RANGE: Int = VkImageType_3D
val VkImageType_RANGE_SIZE: Int = VkImageType_3D - VkImageType_1D + 1

typealias VkImageTiling = Int

val VkImageTiling_OPTIMAL: VkImageTiling = 0
val VkImageTiling_LINEAR: VkImageTiling = 1

val VkImageTiling_BEGIN_RANGE: Int = VkImageTiling_OPTIMAL
val VkImageTiling_END_RANGE: Int = VkImageTiling_LINEAR
val VkImageTiling_RANGE_SIZE: Int = VkImageTiling_LINEAR - VkImageTiling_OPTIMAL + 1

typealias VkPhysicalDeviceType = Int

val VkPhysicalDeviceType_OTHER: VkPhysicalDeviceType = 0
val VkPhysicalDeviceType_INTEGRATED_GPU: VkPhysicalDeviceType = 1
val VkPhysicalDeviceType_DISCRETE_GPU: VkPhysicalDeviceType = 2
val VkPhysicalDeviceType_VIRTUAL_GPU: VkPhysicalDeviceType = 3
val VkPhysicalDeviceType_CPU: VkPhysicalDeviceType = 4

val VkPhysicalDeviceType_BEGIN_RANGE: Int = VkPhysicalDeviceType_OTHER
val VkPhysicalDeviceType_END_RANGE: Int = VkPhysicalDeviceType_CPU
val VkPhysicalDeviceType_RANGE_SIZE = VkPhysicalDeviceType_CPU - VkPhysicalDeviceType_OTHER + 1

//typedef enum VkQueryType {
//    VK_QUERY_TYPE_OCCLUSION = 0,
//    VK_QUERY_TYPE_PIPELINE_STATISTICS = 1,
//    VK_QUERY_TYPE_TIMESTAMP = 2,
//    VK_QUERY_TYPE_BEGIN_RANGE = VK_QUERY_TYPE_OCCLUSION,
//    VK_QUERY_TYPE_END_RANGE = VK_QUERY_TYPE_TIMESTAMP,
//    VK_QUERY_TYPE_RANGE_SIZE = (VK_QUERY_TYPE_TIMESTAMP - VK_QUERY_TYPE_OCCLUSION + 1),
//    VK_QUERY_TYPE_MAX_ENUM = 0x7FFFFFFF
//} VkQueryType;
//
typealias VkSharingMode = Int

val VkSharingMode_EXCLUSIVE: VkSharingMode = 0
val VkSharingMode_CONCURRENT: VkSharingMode = 1

val VkSharingMode_BEGIN_RANGE = VkSharingMode_EXCLUSIVE
val VkSharingMode_END_RANGE = VkSharingMode_CONCURRENT
val VkSharingMode_RANGE_SIZE = VkSharingMode_CONCURRENT - VkSharingMode_EXCLUSIVE + 1

typealias VkImageLayout = Int

val VkImageLayout_UNDEFINED: VkImageLayout = 0
val VkImageLayout_GENERAL: VkImageLayout = 1
val VkImageLayout_COLOR_ATTACHMENT_OPTIMAL: VkImageLayout = 2
val VkImageLayout_DEPTH_STENCIL_ATTACHMENT_OPTIMAL: VkImageLayout = 3
val VkImageLayout_DEPTH_STENCIL_READ_ONLY_OPTIMAL: VkImageLayout = 4
val VkImageLayout_SHADER_READ_ONLY_OPTIMAL: VkImageLayout = 5
val VkImageLayout_TRANSFER_SRC_OPTIMAL: VkImageLayout = 6
val VkImageLayout_TRANSFER_DST_OPTIMAL: VkImageLayout = 7
val VkImageLayout_PREINITIALIZED: VkImageLayout = 8
val VkImageLayout_PRESENT_SRC_KHR: VkImageLayout = 1000001002
val VkImageLayout_SHARED_PRESENT_KHR: VkImageLayout = 1000111000
val VkImageLayout_DEPTH_READ_ONLY_STENCIL_ATTACHMENT_OPTIMAL_KHR: VkImageLayout = 1000117000
val VkImageLayout_DEPTH_ATTACHMENT_STENCIL_READ_ONLY_OPTIMAL_KHR: VkImageLayout = 1000117001

val VkImageLayout_BEGIN_RANGE: Int = VkImageLayout_UNDEFINED
val VkImageLayout_END_RANGE: Int = VkImageLayout_PREINITIALIZED
val VkImageLayout_RANGE_SIZE = VkImageLayout_PREINITIALIZED - VkImageLayout_UNDEFINED + 1



typealias VkImageViewType = Int

val VkImageViewType_1D: VkImageViewType = 0
val VkImageViewType_2D: VkImageViewType = 1
val VkImageViewType_3D: VkImageViewType = 2
val VkImageViewType_CUBE: VkImageViewType = 3
val VkImageViewType_1D_ARRAY: VkImageViewType = 4
val VkImageViewType_2D_ARRAY: VkImageViewType = 5
val VkImageViewType_CUBE_ARRAY: VkImageViewType = 6

val VkImageViewType_BEGIN_RANGE: Int = VkImageViewType_1D
val VkImageViewType_END_RANGE: Int = VkImageViewType_CUBE_ARRAY
val VkImageViewType_RANGE_SIZE = VkImageViewType_CUBE_ARRAY - VkImageViewType_1D + 1

typealias VkComponentSwizzle = Int

val VkComponentSwizzle_IDENTITY: VkComponentSwizzle = 0
val VkComponentSwizzle_ZERO: VkComponentSwizzle = 1
val VkComponentSwizzle_ONE: VkComponentSwizzle = 2
val VkComponentSwizzle_R: VkComponentSwizzle = 3
val VkComponentSwizzle_G: VkComponentSwizzle = 4
val VkComponentSwizzle_B: VkComponentSwizzle = 5
val VkComponentSwizzle_A: VkComponentSwizzle = 6

val VkComponentSwizzle_BEGIN_RANGE: Int = VkComponentSwizzle_IDENTITY
val VkComponentSwizzle_END_RANGE: Int = VkComponentSwizzle_A
val VkComponentSwizzle_RANGE_SIZE = VkComponentSwizzle_A - VkComponentSwizzle_IDENTITY + 1


typealias VkVertexInputRate = Int

val VkVertexInputRate_VERTEX: VkVertexInputRate = 0
val VkVertexInputRate_INSTANCE: VkVertexInputRate = 1

val VkVertexInputRate_BEGIN_RANGE: Int = VkVertexInputRate_VERTEX
val VkVertexInputRate_END_RANGE: Int = VkVertexInputRate_INSTANCE
val VkVertexInputRate_RANGE_SIZE = VkVertexInputRate_INSTANCE - VkVertexInputRate_VERTEX + 1

typealias VkPrimitiveTopology = Int

val VkPrimitiveTopology_POINT_LIST: VkPrimitiveTopology = 0
val VkPrimitiveTopology_LINE_LIST: VkPrimitiveTopology = 1
val VkPrimitiveTopology_LINE_STRIP: VkPrimitiveTopology = 2
val VkPrimitiveTopology_TRIANGLE_LIST: VkPrimitiveTopology = 3
val VkPrimitiveTopology_TRIANGLE_STRIP: VkPrimitiveTopology = 4
val VkPrimitiveTopology_TRIANGLE_FAN: VkPrimitiveTopology = 5
val VkPrimitiveTopology_LINE_LIST_WITH_ADJACENCY: VkPrimitiveTopology = 6
val VkPrimitiveTopology_LINE_STRIP_WITH_ADJACENCY: VkPrimitiveTopology = 7
val VkPrimitiveTopology_TRIANGLE_LIST_WITH_ADJACENCY: VkPrimitiveTopology = 8
val VkPrimitiveTopology_TRIANGLE_STRIP_WITH_ADJACENCY: VkPrimitiveTopology = 9
val VkPrimitiveTopology_PATCH_LIST: VkPrimitiveTopology = 10

val VkPrimitiveTopology_BEGIN_RANGE: Int = VkPrimitiveTopology_POINT_LIST
val VkPrimitiveTopology_END_RANGE: Int = VkPrimitiveTopology_PATCH_LIST
val VkPrimitiveTopology_RANGE_SIZE = VkPrimitiveTopology_PATCH_LIST - VkPrimitiveTopology_POINT_LIST + 1


typealias VkPolygonMode = Int

val VkPoligonMode_FILL: VkPolygonMode = 0
val VkPoligonMode_LINE: VkPolygonMode = 1
val VkPoligonMode_POINT: VkPolygonMode = 2
val VkPoligonMode_FILL_RECTANGLE_NV: VkPolygonMode = 1000153000

val VkPoligonMode_BEGIN_RANGE: Int = VkPoligonMode_FILL
val VkPoligonMode_END_RANGE: Int = VkPoligonMode_POINT
val VkPoligonMode_RANGE_SIZE = VkPoligonMode_POINT - VkPoligonMode_FILL + 1

typealias VkFrontFace = Int

val VkFrontFace_COUNTER_CLOCKWISE: VkFrontFace = 0
val VkFrontFace_CLOCKWISE: VkFrontFace = 1

val VkFrontFace_BEGIN_RANGE: Int = VkFrontFace_COUNTER_CLOCKWISE
val VkFrontFace_END_RANGE: Int = VkFrontFace_CLOCKWISE
val VkFrontFace_RANGE_SIZE = VkFrontFace_CLOCKWISE - VkFrontFace_COUNTER_CLOCKWISE + 1


typealias VkCompareOp = Int

val VkCompareOp_NEVER: VkCompareOp = 0
val VkCompareOp_LESS: VkCompareOp = 1
val VkCompareOp_EQUAL: VkCompareOp = 2
val VkCompareOp_LESS_OR_EQUAL: VkCompareOp = 3
val VkCompareOp_GREATER: VkCompareOp = 4
val VkCompareOp_NOT_EQUAL: VkCompareOp = 5
val VkCompareOp_GREATER_OR_EQUAL: VkCompareOp = 6
val VkCompareOp_ALWAYS: VkCompareOp = 7

val VkCompareOp_BEGIN_RANGE: Int = VkCompareOp_NEVER
val VkCompareOp_END_RANGE: Int = VkCompareOp_ALWAYS
val VkCompareOp_RANGE_SIZE = VkCompareOp_ALWAYS - VkCompareOp_NEVER + 1

typealias VkStencilOp = Int

val VkStencilOp_KEEP: VkStencilOp = 0
val VkStencilOp_ZERO: VkStencilOp = 1
val VkStencilOp_REPLACE: VkStencilOp = 2
val VkStencilOp_INCREMENT_AND_CLAMP: VkStencilOp = 3
val VkStencilOp_DECREMENT_AND_CLAMP: VkStencilOp = 4
val VkStencilOp_INVERT: VkStencilOp = 5
val VkStencilOp_INCREMENT_AND_WRAP: VkStencilOp = 6
val VkStencilOp_DECREMENT_AND_WRAP: VkStencilOp = 7

val VkStencilOp_BEGIN_RANGE: Int = VkStencilOp_KEEP
val VkStencilOp_END_RANGE: Int = VkStencilOp_DECREMENT_AND_WRAP
val VkStencilOp_RANGE_SIZE = VkStencilOp_DECREMENT_AND_WRAP - VkStencilOp_KEEP + 1


typealias VkLogicOp = Int

val VkLogicOp_CLEAR: VkLogicOp = 0
val VkLogicOp_AND: VkLogicOp = 1
val VkLogicOp_AND_REVERSE: VkLogicOp = 2
val VkLogicOp_COPY: VkLogicOp = 3
val VkLogicOp_AND_INVERTED: VkLogicOp = 4
val VkLogicOp_NO_OP: VkLogicOp = 5
val VkLogicOp_XOR: VkLogicOp = 6
val VkLogicOp_OR: VkLogicOp = 7
val VkLogicOp_NOR: VkLogicOp = 8
val VkLogicOp_EQUIVALENT: VkLogicOp = 9
val VkLogicOp_INVERT: VkLogicOp = 10
val VkLogicOp_OR_REVERSE: VkLogicOp = 11
val VkLogicOp_COPY_INVERTED: VkLogicOp = 12
val VkLogicOp_OR_INVERTED: VkLogicOp = 13
val VkLogicOp_NAND: VkLogicOp = 14
val VkLogicOp_SET: VkLogicOp = 15

val VkLogicOp_BEGIN_RANGE: Int = VkLogicOp_CLEAR
val VkLogicOp_END_RANGE: Int = VkLogicOp_SET
val VkLogicOp_RANGE_SIZE = VkLogicOp_SET - VkLogicOp_CLEAR + 1

typealias VkBlendFactor = Int

val VkBlendFactor_ZERO: VkBlendFactor = 0
val VkBlendFactor_ONE: VkBlendFactor = 1
val VkBlendFactor_SRC_COLOR: VkBlendFactor = 2
val VkBlendFactor_ONE_MINUS_SRC_COLOR: VkBlendFactor = 3
val VkBlendFactor_DST_COLOR: VkBlendFactor = 4
val VkBlendFactor_ONE_MINUS_DST_COLOR: VkBlendFactor = 5
val VkBlendFactor_SRC_ALPHA: VkBlendFactor = 6
val VkBlendFactor_ONE_MINUS_SRC_ALPHA: VkBlendFactor = 7
val VkBlendFactor_DST_ALPHA: VkBlendFactor = 8
val VkBlendFactor_ONE_MINUS_DST_ALPHA: VkBlendFactor = 9
val VkBlendFactor_CONSTANT_COLOR: VkBlendFactor = 10
val VkBlendFactor_ONE_MINUS_CONSTANT_COLOR: VkBlendFactor = 11
val VkBlendFactor_CONSTANT_ALPHA: VkBlendFactor = 12
val VkBlendFactor_ONE_MINUS_CONSTANT_ALPHA: VkBlendFactor = 13
val VkBlendFactor_SRC_ALPHA_SATURATE: VkBlendFactor = 14
val VkBlendFactor_SRC1_COLOR: VkBlendFactor = 15
val VkBlendFactor_ONE_MINUS_SRC1_COLOR: VkBlendFactor = 16
val VkBlendFactor_SRC1_ALPHA: VkBlendFactor = 17
val VkBlendFactor_ONE_MINUS_SRC1_ALPHA: VkBlendFactor = 18

val VkBlendFactor_BEGIN_RANGE: Int = VkBlendFactor_ZERO
val VkBlendFactor_END_RANGE: Int = VkBlendFactor_ONE_MINUS_SRC1_ALPHA
val VkBlendFactor_RANGE_SIZE = VkBlendFactor_ONE_MINUS_SRC1_ALPHA - VkBlendFactor_ZERO + 1


typealias VkBlendOp = Int

val VkBlendOp_ADD: VkBlendOp = 0
val VkBlendOp_SUBTRACT: VkBlendOp = 1
val VkBlendOp_REVERSE_SUBTRACT: VkBlendOp = 2
val VkBlendOp_MIN: VkBlendOp = 3
val VkBlendOp_MAX: VkBlendOp = 4
val VkBlendOp_ZERO_EXT: VkBlendOp = 1000148000
val VkBlendOp_SRC_EXT: VkBlendOp = 1000148001
val VkBlendOp_DST_EXT: VkBlendOp = 1000148002
val VkBlendOp_SRC_OVER_EXT: VkBlendOp = 1000148003
val VkBlendOp_DST_OVER_EXT: VkBlendOp = 1000148004
val VkBlendOp_SRC_IN_EXT: VkBlendOp = 1000148005
val VkBlendOp_DST_IN_EXT: VkBlendOp = 1000148006
val VkBlendOp_SRC_OUT_EXT: VkBlendOp = 1000148007
val VkBlendOp_DST_OUT_EXT: VkBlendOp = 1000148008
val VkBlendOp_SRC_ATOP_EXT: VkBlendOp = 1000148009
val VkBlendOp_DST_ATOP_EXT: VkBlendOp = 1000148010
val VkBlendOp_XOR_EXT: VkBlendOp = 1000148011
val VkBlendOp_MULTIPLY_EXT: VkBlendOp = 1000148012
val VkBlendOp_SCREEN_EXT: VkBlendOp = 1000148013
val VkBlendOp_OVERLAY_EXT: VkBlendOp = 1000148014
val VkBlendOp_DARKEN_EXT: VkBlendOp = 1000148015
val VkBlendOp_LIGHTEN_EXT: VkBlendOp = 1000148016
val VkBlendOp_COLORDODGE_EXT: VkBlendOp = 1000148017
val VkBlendOp_COLORBURN_EXT: VkBlendOp = 1000148018
val VkBlendOp_HARDLIGHT_EXT: VkBlendOp = 1000148019
val VkBlendOp_SOFTLIGHT_EXT: VkBlendOp = 1000148020
val VkBlendOp_DIFFERENCE_EXT: VkBlendOp = 1000148021
val VkBlendOp_EXCLUSION_EXT: VkBlendOp = 1000148022
val VkBlendOp_INVERT_EXT: VkBlendOp = 1000148023
val VkBlendOp_INVERT_RGB_EXT: VkBlendOp = 1000148024
val VkBlendOp_LINEARDODGE_EXT: VkBlendOp = 1000148025
val VkBlendOp_LINEARBURN_EXT: VkBlendOp = 1000148026
val VkBlendOp_VIVIDLIGHT_EXT: VkBlendOp = 1000148027
val VkBlendOp_LINEARLIGHT_EXT: VkBlendOp = 1000148028
val VkBlendOp_PINLIGHT_EXT: VkBlendOp = 1000148029
val VkBlendOp_HARDMIX_EXT: VkBlendOp = 1000148030
val VkBlendOp_HSL_HUE_EXT: VkBlendOp = 1000148031
val VkBlendOp_HSL_SATURATION_EXT: VkBlendOp = 1000148032
val VkBlendOp_HSL_COLOR_EXT: VkBlendOp = 1000148033
val VkBlendOp_HSL_LUMINOSITY_EXT: VkBlendOp = 1000148034
val VkBlendOp_PLUS_EXT: VkBlendOp = 1000148035
val VkBlendOp_PLUS_CLAMPED_EXT: VkBlendOp = 1000148036
val VkBlendOp_PLUS_CLAMPED_ALPHA_EXT: VkBlendOp = 1000148037
val VkBlendOp_PLUS_DARKER_EXT: VkBlendOp = 1000148038
val VkBlendOp_MINUS_EXT: VkBlendOp = 1000148039
val VkBlendOp_MINUS_CLAMPED_EXT: VkBlendOp = 1000148040
val VkBlendOp_CONTRAST_EXT: VkBlendOp = 1000148041
val VkBlendOp_INVERT_OVG_EXT: VkBlendOp = 1000148042
val VkBlendOp_RED_EXT: VkBlendOp = 1000148043
val VkBlendOp_GREEN_EXT: VkBlendOp = 1000148044
val VkBlendOp_BLUE_EXT: VkBlendOp = 1000148045

val VkBlendOp_BEGIN_RANGE: Int = VkBlendOp_ADD
val VkBlendOp_END_RANGE: Int = VkBlendOp_MAX
val VkBlendOp_RANGE_SIZE = VkBlendOp_MAX - VkBlendOp_ADD + 1


typealias VkDynamicState = Int

val VkDynamicState_VIEWPORT: VkDynamicState = 0
val VkDynamicState_SCISSOR: VkDynamicState = 1
val VkDynamicState_LINE_WIDTH: VkDynamicState = 2
val VkDynamicState_DEPTH_BIAS: VkDynamicState = 3
val VkDynamicState_BLEND_CONSTANTS: VkDynamicState = 4
val VkDynamicState_DEPTH_BOUNDS: VkDynamicState = 5
val VkDynamicState_STENCIL_COMPARE_MASK: VkDynamicState = 6
val VkDynamicState_STENCIL_WRITE_MASK: VkDynamicState = 7
val VkDynamicState_STENCIL_REFERENCE: VkDynamicState = 8
val VkDynamicState_VIEWPORT_W_SCALING_NV: VkDynamicState = 1000087000
val VkDynamicState_DISCARD_RECTANGLE_EXT: VkDynamicState = 1000099000
val VkDynamicState_SAMPLE_LOCATIONS_EXT: VkDynamicState = 1000143000

val VkDynamicState_BEGIN_RANGE: Int = VkDynamicState_VIEWPORT
val VkDynamicState_END_RANGE: Int = VkDynamicState_STENCIL_REFERENCE
val VkDynamicState_RANGE_SIZE = VkDynamicState_STENCIL_REFERENCE - VkDynamicState_VIEWPORT + 1
//
//typedef enum VkFilter {
//    VK_FILTER_NEAREST = 0,
//    VK_FILTER_LINEAR = 1,
//    VK_FILTER_CUBIC_IMG = 1000015000,
//    VK_FILTER_BEGIN_RANGE = VK_FILTER_NEAREST,
//    VK_FILTER_END_RANGE = VK_FILTER_LINEAR,
//    VK_FILTER_RANGE_SIZE = (VK_FILTER_LINEAR - VK_FILTER_NEAREST + 1),
//    VK_FILTER_MAX_ENUM = 0x7FFFFFFF
//} VkFilter;
//
//typedef enum VkSamplerMipmapMode {
//    VK_SAMPLER_MIPMAP_MODE_NEAREST = 0,
//    VK_SAMPLER_MIPMAP_MODE_LINEAR = 1,
//    VK_SAMPLER_MIPMAP_MODE_BEGIN_RANGE = VK_SAMPLER_MIPMAP_MODE_NEAREST,
//    VK_SAMPLER_MIPMAP_MODE_END_RANGE = VK_SAMPLER_MIPMAP_MODE_LINEAR,
//    VK_SAMPLER_MIPMAP_MODE_RANGE_SIZE = (VK_SAMPLER_MIPMAP_MODE_LINEAR - VK_SAMPLER_MIPMAP_MODE_NEAREST + 1),
//    VK_SAMPLER_MIPMAP_MODE_MAX_ENUM = 0x7FFFFFFF
//} VkSamplerMipmapMode;
//
//typedef enum VkSamplerAddressMode {
//    VK_SAMPLER_ADDRESS_MODE_REPEAT = 0,
//    VK_SAMPLER_ADDRESS_MODE_MIRRORED_REPEAT = 1,
//    VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE = 2,
//    VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER = 3,
//    VK_SAMPLER_ADDRESS_MODE_MIRROR_CLAMP_TO_EDGE = 4,
//    VK_SAMPLER_ADDRESS_MODE_BEGIN_RANGE = VK_SAMPLER_ADDRESS_MODE_REPEAT,
//    VK_SAMPLER_ADDRESS_MODE_END_RANGE = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER,
//    VK_SAMPLER_ADDRESS_MODE_RANGE_SIZE = (VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER - VK_SAMPLER_ADDRESS_MODE_REPEAT + 1),
//    VK_SAMPLER_ADDRESS_MODE_MAX_ENUM = 0x7FFFFFFF
//} VkSamplerAddressMode;
//
//typedef enum VkBorderColor {
//    VK_BORDER_COLOR_FLOAT_TRANSPARENT_BLACK = 0,
//    VK_BORDER_COLOR_INT_TRANSPARENT_BLACK = 1,
//    VK_BORDER_COLOR_FLOAT_OPAQUE_BLACK = 2,
//    VK_BORDER_COLOR_INT_OPAQUE_BLACK = 3,
//    VK_BORDER_COLOR_FLOAT_OPAQUE_WHITE = 4,
//    VK_BORDER_COLOR_INT_OPAQUE_WHITE = 5,
//    VK_BORDER_COLOR_BEGIN_RANGE = VK_BORDER_COLOR_FLOAT_TRANSPARENT_BLACK,
//    VK_BORDER_COLOR_END_RANGE = VK_BORDER_COLOR_INT_OPAQUE_WHITE,
//    VK_BORDER_COLOR_RANGE_SIZE = (VK_BORDER_COLOR_INT_OPAQUE_WHITE - VK_BORDER_COLOR_FLOAT_TRANSPARENT_BLACK + 1),
//    VK_BORDER_COLOR_MAX_ENUM = 0x7FFFFFFF
//} VkBorderColor;
//
typealias VkDescriptorType = Int

val VkDescriptorType_SAMPLER: VkDescriptorType = 0
val VkDescriptorType_COMBINED_IMAGE_SAMPLER: VkDescriptorType = 1
val VkDescriptorType_SAMPLED_IMAGE: VkDescriptorType = 2
val VkDescriptorType_STORAGE_IMAGE: VkDescriptorType = 3
val VkDescriptorType_UNIFORM_TEXEL_BUFFER: VkDescriptorType = 4
val VkDescriptorType_STORAGE_TEXEL_BUFFER: VkDescriptorType = 5
val VkDescriptorType_UNIFORM_BUFFER: VkDescriptorType = 6
val VkDescriptorType_STORAGE_BUFFER: VkDescriptorType = 7
val VkDescriptorType_UNIFORM_BUFFER_DYNAMIC: VkDescriptorType = 8
val VkDescriptorType_STORAGE_BUFFER_DYNAMIC: VkDescriptorType = 9
val VkDescriptorType_INPUT_ATTACHMENT: VkDescriptorType = 10

val VkDescriptorType_BEGIN_RANGE: Int = VkDescriptorType_SAMPLER
val VkDescriptorType_END_RANGE: Int = VkDescriptorType_INPUT_ATTACHMENT
val VkDescriptorType_RANGE_SIZE = VkDescriptorType_INPUT_ATTACHMENT - VkDescriptorType_SAMPLER + 1

typealias VkAttachmentLoadOp = Int

val VkAttachmentLoadOp_LOAD: VkAttachmentLoadOp = 0
val VkAttachmentLoadOp_CLEAR: VkAttachmentLoadOp = 1
val VkAttachmentLoadOp_DONT_CARE: VkAttachmentLoadOp = 2

val VkAttachmentLoadOp_BEGIN_RANGE: Int = VkAttachmentLoadOp_LOAD
val VkAttachmentLoadOp_END_RANGE: Int = VkAttachmentLoadOp_DONT_CARE
val VkAttachmentLoadOp_RANGE_SIZE = VkAttachmentLoadOp_DONT_CARE - VkAttachmentLoadOp_LOAD + 1


typealias VkAttachmentStoreOp = Int

val VkAttachmentStoreOp_STORE: VkAttachmentStoreOp = 0
val VkAttachmentStoreOp_DONT_CARE: VkAttachmentStoreOp = 1

val VkAttachmentStoreOp_BEGIN_RANGE: Int = VkAttachmentStoreOp_STORE
val VkAttachmentStoreOp_END_RANGE: Int = VkAttachmentStoreOp_DONT_CARE
val VkAttachmentStoreOp_RANGE_SIZE = VkAttachmentStoreOp_DONT_CARE - VkAttachmentStoreOp_STORE + 1

typealias VkPipelineBindPoint = Int

val VkPipelineBindPoint_GRAPHICS: VkPipelineBindPoint = 0
val VkPipelineBindPoint_COMPUTE: VkPipelineBindPoint = 1

val VkPipelineBindPoint_BEGIN_RANGE: Int = VkPipelineBindPoint_GRAPHICS
val VkPipelineBindPoint_END_RANGE: Int = VkPipelineBindPoint_COMPUTE
val VkPipelineBindPoint_RANGE_SIZE = VkPipelineBindPoint_COMPUTE - VkPipelineBindPoint_GRAPHICS + 1


typealias VkCommandBufferLevel = Int

val VkCommandBufferLevel_PRIMARY: VkCommandBufferLevel = 0
val VkCommandBufferLevel_SECONDARY: VkCommandBufferLevel = 1

val VkCommandBufferLevel_BEGIN_RANGE: Int = VkCommandBufferLevel_PRIMARY
val VkCommandBufferLevel_END_RANGE: Int = VkCommandBufferLevel_SECONDARY
val VkCommandBufferLevel_RANGE_SIZE: Int = VkCommandBufferLevel_SECONDARY - VkCommandBufferLevel_PRIMARY + 1

typealias VkIndexType = Int

val VkIndexType_UINT16: VkIndexType = 0
val VkIndexType_UINT32: VkIndexType = 1

val VkIndexType_BEGIN_RANGE: Int = VkIndexType_UINT16
val VkIndexType_END_RANGE: Int = VkIndexType_UINT32
val VkIndexType_RANGE_SIZE = VkIndexType_UINT32 - VkIndexType_UINT16 + 1

typealias VkSubpassContents = Int

val VkSubpassContents_INLINE: VkSubpassContents = 0
val VkSubpassContents_SECONDARY_COMMAND_BUFFERS: VkSubpassContents = 1

val VkSubpassContents_BEGIN_RANGE = VkSubpassContents_INLINE
val VkSubpassContents_END_RANGE = VkSubpassContents_SECONDARY_COMMAND_BUFFERS
val VkSubpassContents_RANGE_SIZE = VkSubpassContents_SECONDARY_COMMAND_BUFFERS - VkSubpassContents_INLINE + 1


//
//typedef enum VkObjectType {
//    VK_OBJECT_TYPE_UNKNOWN = 0,
//    VK_OBJECT_TYPE_INSTANCE = 1,
//    VK_OBJECT_TYPE_PHYSICAL_DEVICE = 2,
//    VK_OBJECT_TYPE_DEVICE = 3,
//    VK_OBJECT_TYPE_QUEUE = 4,
//    VK_OBJECT_TYPE_SEMAPHORE = 5,
//    VK_OBJECT_TYPE_COMMAND_BUFFER = 6,
//    VK_OBJECT_TYPE_FENCE = 7,
//    VK_OBJECT_TYPE_DEVICE_MEMORY = 8,
//    VK_OBJECT_TYPE_BUFFER = 9,
//    VK_OBJECT_TYPE_IMAGE = 10,
//    VK_OBJECT_TYPE_EVENT = 11,
//    VK_OBJECT_TYPE_QUERY_POOL = 12,
//    VK_OBJECT_TYPE_BUFFER_VIEW = 13,
//    VK_OBJECT_TYPE_IMAGE_VIEW = 14,
//    VK_OBJECT_TYPE_SHADER_MODULE = 15,
//    VK_OBJECT_TYPE_PIPELINE_CACHE = 16,
//    VK_OBJECT_TYPE_PIPELINE_LAYOUT = 17,
//    VK_OBJECT_TYPE_RENDER_PASS = 18,
//    VK_OBJECT_TYPE_PIPELINE = 19,
//    VK_OBJECT_TYPE_DESCRIPTOR_SET_LAYOUT = 20,
//    VK_OBJECT_TYPE_SAMPLER = 21,
//    VK_OBJECT_TYPE_DESCRIPTOR_POOL = 22,
//    VK_OBJECT_TYPE_DESCRIPTOR_SET = 23,
//    VK_OBJECT_TYPE_FRAMEBUFFER = 24,
//    VK_OBJECT_TYPE_COMMAND_POOL = 25,
//    VK_OBJECT_TYPE_SURFACE_KHR = 1000000000,
//    VK_OBJECT_TYPE_SWAPCHAIN_KHR = 1000001000,
//    VK_OBJECT_TYPE_DISPLAY_KHR = 1000002000,
//    VK_OBJECT_TYPE_DISPLAY_MODE_KHR = 1000002001,
//    VK_OBJECT_TYPE_DEBUG_REPORT_CALLBACK_EXT = 1000011000,
//    VK_OBJECT_TYPE_DESCRIPTOR_UPDATE_TEMPLATE_KHR = 1000085000,
//    VK_OBJECT_TYPE_OBJECT_TABLE_NVX = 1000086000,
//    VK_OBJECT_TYPE_INDIRECT_COMMANDS_LAYOUT_NVX = 1000086001,
//    VK_OBJECT_TYPE_SAMPLER_YCBCR_CONVERSION_KHR = 1000156000,
//    VK_OBJECT_TYPE_VALIDATION_CACHE_EXT = 1000160000,
//    VK_OBJECT_TYPE_BEGIN_RANGE = VK_OBJECT_TYPE_UNKNOWN,
//    VK_OBJECT_TYPE_END_RANGE = VK_OBJECT_TYPE_COMMAND_POOL,
//    VK_OBJECT_TYPE_RANGE_SIZE = (VK_OBJECT_TYPE_COMMAND_POOL - VK_OBJECT_TYPE_UNKNOWN + 1),
//    VK_OBJECT_TYPE_MAX_ENUM = 0x7FFFFFFF
//} VkObjectType;
//
//typedef VkFlags VkInstanceCreateFlags;
//
typealias VkFormatFeatureFlagBits = Int

val VkFormatFeature_SAMPLED_IMAGE_BIT: VkFormatFeatureFlagBits = 0x00000001
val VkFormatFeature_STORAGE_IMAGE_BIT: VkFormatFeatureFlagBits = 0x00000002
val VkFormatFeature_STORAGE_IMAGE_ATOMIC_BIT: VkFormatFeatureFlagBits = 0x00000004
val VkFormatFeature_UNIFORM_TEXEL_BUFFER_BIT: VkFormatFeatureFlagBits = 0x00000008
val VkFormatFeature_STORAGE_TEXEL_BUFFER_BIT: VkFormatFeatureFlagBits = 0x00000010
val VkFormatFeature_STORAGE_TEXEL_BUFFER_ATOMIC_BIT: VkFormatFeatureFlagBits = 0x00000020
val VkFormatFeature_VERTEX_BUFFER_BIT: VkFormatFeatureFlagBits = 0x00000040
val VkFormatFeature_COLOR_ATTACHMENT_BIT: VkFormatFeatureFlagBits = 0x00000080
val VkFormatFeature_COLOR_ATTACHMENT_BLEND_BIT: VkFormatFeatureFlagBits = 0x00000100
val VkFormatFeature_DEPTH_STENCIL_ATTACHMENT_BIT: VkFormatFeatureFlagBits = 0x00000200
val VkFormatFeature_BLIT_SRC_BIT: VkFormatFeatureFlagBits = 0x00000400
val VkFormatFeature_BLIT_DST_BIT: VkFormatFeatureFlagBits = 0x00000800
val VkFormatFeature_SAMPLED_IMAGE_FILTER_LINEAR_BIT: VkFormatFeatureFlagBits = 0x00001000
val VkFormatFeature_SAMPLED_IMAGE_FILTER_CUBIC_BIT_IMG: VkFormatFeatureFlagBits = 0x00002000
val VkFormatFeature_TRANSFER_SRC_BIT_KHR: VkFormatFeatureFlagBits = 0x00004000
val VkFormatFeature_TRANSFER_DST_BIT_KHR: VkFormatFeatureFlagBits = 0x00008000
val VkFormatFeature_SAMPLED_IMAGE_FILTER_MINMAX_BIT_EXT: VkFormatFeatureFlagBits = 0x00010000
val VkFormatFeature_MIDPOINT_CHROMA_SAMPLES_BIT_KHR: VkFormatFeatureFlagBits = 0x00020000
val VkFormatFeature_SAMPLED_IMAGE_YCBCR_CONVERSION_LINEAR_FILTER_BIT_KHR: VkFormatFeatureFlagBits = 0x00040000
val VkFormatFeature_SAMPLED_IMAGE_YCBCR_CONVERSION_SEPARATE_RECONSTRUCTION_FILTER_BIT_KHR: VkFormatFeatureFlagBits = 0x00080000
val VkFormatFeature_SAMPLED_IMAGE_YCBCR_CONVERSION_CHROMA_RECONSTRUCTION_EXPLICIT_BIT_KHR: VkFormatFeatureFlagBits = 0x00100000
val VkFormatFeature_SAMPLED_IMAGE_YCBCR_CONVERSION_CHROMA_RECONSTRUCTION_EXPLICIT_FORCEABLE_BIT_KHR: VkFormatFeatureFlagBits = 0x00200000
val VkFormatFeature_DISJOINT_BIT_KHR: VkFormatFeatureFlagBits = 0x00400000
val VkFormatFeature_COSITED_CHROMA_SAMPLES_BIT_KHR: VkFormatFeatureFlagBits = 0x00800000

typealias VkFormatFeatureFlags = VkFlags

typealias VkImageUsageFlagBits = Int

val VkImageUsage_TRANSFER_SRC_BIT: VkImageUsageFlagBits = 0x00000001
val VkImageUsage_TRANSFER_DST_BIT: VkImageUsageFlagBits = 0x00000002
val VkImageUsage_SAMPLED_BIT: VkImageUsageFlagBits = 0x00000004
val VkImageUsage_STORAGE_BIT: VkImageUsageFlagBits = 0x00000008
val VkImageUsage_COLOR_ATTACHMENT_BIT: VkImageUsageFlagBits = 0x00000010
val VkImageUsage_DEPTH_STENCIL_ATTACHMENT_BIT: VkImageUsageFlagBits = 0x00000020
val VkImageUsage_TRANSIENT_ATTACHMENT_BIT: VkImageUsageFlagBits = 0x00000040
val VkImageUsage_INPUT_ATTACHMENT_BIT: VkImageUsageFlagBits = 0x00000080

typealias VkImageUsageFlags = VkFlags

typealias VkImageCreateFlagBits = Int

val VkImageCreate_SPARSE_BINDING_BIT: VkImageCreateFlagBits = 0x00000001
val VkImageCreate_SPARSE_RESIDENCY_BIT: VkImageCreateFlagBits = 0x00000002
val VkImageCreate_SPARSE_ALIASED_BIT: VkImageCreateFlagBits = 0x00000004
val VkImageCreate_MUTABLE_FORMAT_BIT: VkImageCreateFlagBits = 0x00000008
val VkImageCreate_CUBE_COMPATIBLE_BIT: VkImageCreateFlagBits = 0x00000010
val VkImageCreate_BIND_SFR_BIT_KHX: VkImageCreateFlagBits = 0x00000040
val VkImageCreate_2D_ARRAY_COMPATIBLE_BIT_KHR: VkImageCreateFlagBits = 0x00000020
val VkImageCreate_BLOCK_TEXEL_VIEW_COMPATIBLE_BIT_KHR: VkImageCreateFlagBits = 0x00000080
val VkImageCreate_EXTENDED_USAGE_BIT_KHR: VkImageCreateFlagBits = 0x00000100
val VkImageCreate_SAMPLE_LOCATIONS_COMPATIBLE_DEPTH_BIT_EXT: VkImageCreateFlagBits = 0x00001000
val VkImageCreate_DISJOINT_BIT_KHR: VkImageCreateFlagBits = 0x00000200
val VkImageCreate_ALIAS_BIT_KHR: VkImageCreateFlagBits = 0x00000400

typealias VkImageCreateFlags = VkFlags

typealias VkSampleCountFlagBits = Int

val VkSampleCount_1_BIT: VkSampleCountFlagBits = 0x00000001
val VkSampleCount_2_BIT: VkSampleCountFlagBits = 0x00000002
val VkSampleCount_4_BIT: VkSampleCountFlagBits = 0x00000004
val VkSampleCount_8_BIT: VkSampleCountFlagBits = 0x00000008
val VkSampleCount_16_BIT: VkSampleCountFlagBits = 0x00000010
val VkSampleCount_32_BIT: VkSampleCountFlagBits = 0x00000020
val VkSampleCount_64_BIT: VkSampleCountFlagBits = 0x00000040

typealias VkSampleCountFlags = VkFlags


typealias VkQueueFlagBits = Int

val VkQueue_GRAPHICS_BIT: VkQueueFlagBits = 0x00000001
val VkQueue_COMPUTE_BIT: VkQueueFlagBits = 0x00000002
val VkQueue_TRANSFER_BIT: VkQueueFlagBits = 0x00000004
val VkQueue_SPARSE_BINDING_BIT: VkQueueFlagBits = 0x00000008

typealias VkQueueFlags = VkFlags

enum class VkMemoryProperty(val i: Int) {
    DEVICE_LOCAL_BIT(0x00000001),
    HOST_VISIBLE_BIT(0x00000002),
    HOST_COHERENT_BIT(0x00000004),
    HOST_CACHED_BIT(0x00000008),
    LAZILY_ALLOCATED_BIT(0x00000010);

    infix fun or(b: VkMemoryProperty): VkMemoryPropertyFlags = i or b.i
}

typealias VkMemoryPropertyFlags = VkFlags

//typedef enum VkMemoryHeapFlagBits {
//    VK_MEMORY_HEAP_DEVICE_LOCAL_BIT = 0x00000001,
//    VK_MEMORY_HEAP_MULTI_INSTANCE_BIT_KHX = 0x00000002,
//    VK_MEMORY_HEAP_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkMemoryHeapFlagBits;
//typedef VkFlags VkMemoryHeapFlags;
//typedef VkFlags VkDeviceCreateFlags;
//typedef VkFlags VkDeviceQueueCreateFlags;
//
typealias VkPipelineStageFlagBits = Int

val VkPipelineStage_TOP_OF_PIPE_BIT: VkPipelineStageFlagBits = 0x00000001
val VkPipelineStage_DRAW_INDIRECT_BIT: VkPipelineStageFlagBits = 0x00000002
val VkPipelineStage_VERTEX_INPUT_BIT: VkPipelineStageFlagBits = 0x00000004
val VkPipelineStage_VERTEX_SHADER_BIT: VkPipelineStageFlagBits = 0x00000008
val VkPipelineStage_TESSELLATION_CONTROL_SHADER_BIT: VkPipelineStageFlagBits = 0x00000010
val VkPipelineStage_TESSELLATION_EVALUATION_SHADER_BIT: VkPipelineStageFlagBits = 0x00000020
val VkPipelineStage_GEOMETRY_SHADER_BIT: VkPipelineStageFlagBits = 0x00000040
val VkPipelineStage_FRAGMENT_SHADER_BIT: VkPipelineStageFlagBits = 0x00000080
val VkPipelineStage_EARLY_FRAGMENT_TESTS_BIT: VkPipelineStageFlagBits = 0x00000100
val VkPipelineStage_LATE_FRAGMENT_TESTS_BIT: VkPipelineStageFlagBits = 0x00000200
val VkPipelineStage_COLOR_ATTACHMENT_OUTPUT_BIT: VkPipelineStageFlagBits = 0x00000400
val VkPipelineStage_COMPUTE_SHADER_BIT: VkPipelineStageFlagBits = 0x00000800
val VkPipelineStage_TRANSFER_BIT: VkPipelineStageFlagBits = 0x00001000
val VkPipelineStage_BOTTOM_OF_PIPE_BIT: VkPipelineStageFlagBits = 0x00002000
val VkPipelineStage_HOST_BIT: VkPipelineStageFlagBits = 0x00004000
val VkPipelineStage_ALL_GRAPHICS_BIT: VkPipelineStageFlagBits = 0x00008000
val VkPipelineStage_ALL_COMMANDS_BIT: VkPipelineStageFlagBits = 0x00010000
val VkPipelineStage_COMMAND_PROCESS_BIT_NVX: VkPipelineStageFlagBits = 0x00020000


typealias VkPipelineStageFlags = VkFlags

typealias VkMemoryMapFlags = VkFlags

typealias VkImageAspectFlagBits = Int

val VkImageAspect_COLOR_BIT: VkImageAspectFlagBits = 0x00000001
val VkImageAspect_DEPTH_BIT: VkImageAspectFlagBits = 0x00000002
val VkImageAspect_STENCIL_BIT: VkImageAspectFlagBits = 0x00000004
val VkImageAspect_METADATA_BIT: VkImageAspectFlagBits = 0x00000008
val VkImageAspect_PLANE_0_BIT_KHR: VkImageAspectFlagBits = 0x00000010
val VkImageAspect_PLANE_1_BIT_KHR: VkImageAspectFlagBits = 0x00000020
val VkImageAspect_PLANE_2_BIT_KHR: VkImageAspectFlagBits = 0x00000040

typealias VkImageAspectFlags = VkFlags

//typedef enum VkSparseImageFormatFlagBits {
//    VK_SPARSE_IMAGE_FORMAT_SINGLE_MIPTAIL_BIT = 0x00000001,
//    VK_SPARSE_IMAGE_FORMAT_ALIGNED_MIP_SIZE_BIT = 0x00000002,
//    VK_SPARSE_IMAGE_FORMAT_NONSTANDARD_BLOCK_SIZE_BIT = 0x00000004,
//    VK_SPARSE_IMAGE_FORMAT_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkSparseImageFormatFlagBits;
//typedef VkFlags VkSparseImageFormatFlags;
//
//typedef enum VkSparseMemoryBindFlagBits {
//    VK_SPARSE_MEMORY_BIND_METADATA_BIT = 0x00000001,
//    VK_SPARSE_MEMORY_BIND_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkSparseMemoryBindFlagBits;
//typedef VkFlags VkSparseMemoryBindFlags;
//
typealias VkFenceCreateFlagBits = Int

val VkFenceCreateFlag_SIGNALED_BIT: VkFenceCreateFlagBits = 0x00000001

typealias VkFenceCreateFlags = VkFlags
typealias VkSemaphoreCreateFlags = VkFlags
//typedef VkFlags VkEventCreateFlags;
//typedef VkFlags VkQueryPoolCreateFlags;
//
//typedef enum VkQueryPipelineStatisticFlagBits {
//    VK_QUERY_PIPELINE_STATISTIC_INPUT_ASSEMBLY_VERTICES_BIT = 0x00000001,
//    VK_QUERY_PIPELINE_STATISTIC_INPUT_ASSEMBLY_PRIMITIVES_BIT = 0x00000002,
//    VK_QUERY_PIPELINE_STATISTIC_VERTEX_SHADER_INVOCATIONS_BIT = 0x00000004,
//    VK_QUERY_PIPELINE_STATISTIC_GEOMETRY_SHADER_INVOCATIONS_BIT = 0x00000008,
//    VK_QUERY_PIPELINE_STATISTIC_GEOMETRY_SHADER_PRIMITIVES_BIT = 0x00000010,
//    VK_QUERY_PIPELINE_STATISTIC_CLIPPING_INVOCATIONS_BIT = 0x00000020,
//    VK_QUERY_PIPELINE_STATISTIC_CLIPPING_PRIMITIVES_BIT = 0x00000040,
//    VK_QUERY_PIPELINE_STATISTIC_FRAGMENT_SHADER_INVOCATIONS_BIT = 0x00000080,
//    VK_QUERY_PIPELINE_STATISTIC_TESSELLATION_CONTROL_SHADER_PATCHES_BIT = 0x00000100,
//    VK_QUERY_PIPELINE_STATISTIC_TESSELLATION_EVALUATION_SHADER_INVOCATIONS_BIT = 0x00000200,
//    VK_QUERY_PIPELINE_STATISTIC_COMPUTE_SHADER_INVOCATIONS_BIT = 0x00000400,
//    VK_QUERY_PIPELINE_STATISTIC_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkQueryPipelineStatisticFlagBits;
//typedef VkFlags VkQueryPipelineStatisticFlags;
//
//typedef enum VkQueryResultFlagBits {
//    VK_QUERY_RESULT_64_BIT = 0x00000001,
//    VK_QUERY_RESULT_WAIT_BIT = 0x00000002,
//    VK_QUERY_RESULT_WITH_AVAILABILITY_BIT = 0x00000004,
//    VK_QUERY_RESULT_PARTIAL_BIT = 0x00000008,
//    VK_QUERY_RESULT_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkQueryResultFlagBits;
//typedef VkFlags VkQueryResultFlags;

typealias VkBufferCreateFlagBits = Int

val VkBufferCreate_SPARSE_BINDING_BIT: VkBufferCreateFlagBits = 0x00000001
val VkBufferCreate_SPARSE_RESIDENCY_BIT: VkBufferCreateFlagBits = 0x00000002
val VkBufferCreate_SPARSE_ALIASED_BIT: VkBufferCreateFlagBits = 0x00000004

typealias VkBufferCreateFlags = VkFlags

typealias VkBufferUsageFlagBits = Int

val VkBufferUsage_TRANSFER_SRC_BIT: VkBufferUsageFlagBits = 0x00000001
val VkBufferUsage_TRANSFER_DST_BIT: VkBufferUsageFlagBits = 0x00000002
val VkBufferUsage_UNIFORM_TEXEL_BUFFER_BIT: VkBufferUsageFlagBits = 0x00000004
val VkBufferUsage_STORAGE_TEXEL_BUFFER_BIT: VkBufferUsageFlagBits = 0x00000008
val VkBufferUsage_UNIFORM_BUFFER_BIT: VkBufferUsageFlagBits = 0x00000010
val VkBufferUsage_STORAGE_BUFFER_BIT: VkBufferUsageFlagBits = 0x00000020
val VkBufferUsage_INDEX_BUFFER_BIT: VkBufferUsageFlagBits = 0x00000040
val VkBufferUsage_VERTEX_BUFFER_BIT: VkBufferUsageFlagBits = 0x00000080
val VkBufferUsage_INDIRECT_BUFFER_BIT: VkBufferUsageFlagBits = 0x00000100

typealias VkBufferUsageFlags = VkFlags
typealias VkBufferViewCreateFlags = VkFlags
typealias VkImageViewCreateFlags = VkFlags

typealias VkShaderModuleCreateFlags = VkFlags
typealias VkPipelineCacheCreateFlags = VkFlags

//
//typedef enum VkPipelineCreateFlagBits {
//    VK_PIPELINE_CREATE_DISABLE_OPTIMIZATION_BIT = 0x00000001,
//    VK_PIPELINE_CREATE_ALLOW_DERIVATIVES_BIT = 0x00000002,
//    VK_PIPELINE_CREATE_DERIVATIVE_BIT = 0x00000004,
//    VK_PIPELINE_CREATE_VIEW_INDEX_FROM_DEVICE_INDEX_BIT_KHX = 0x00000008,
//    VK_PIPELINE_CREATE_DISPATCH_BASE_KHX = 0x00000010,
//    VK_PIPELINE_CREATE_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkPipelineCreateFlagBits;
typealias VkPipelineCreateFlags = VkFlags

typealias VkPipelineShaderStageCreateFlags = VkFlags

typealias VkShaderStageFlagBits = Int

val VkShaderStage_VERTEX_BIT: VkShaderStageFlagBits = 0x00000001
val VkShaderStage_TESSELLATION_CONTROL_BIT: VkShaderStageFlagBits = 0x00000002
val VkShaderStage_TESSELLATION_EVALUATION_BIT: VkShaderStageFlagBits = 0x00000004
val VkShaderStage_GEOMETRY_BIT: VkShaderStageFlagBits = 0x00000008
val VkShaderStage_FRAGMENT_BIT: VkShaderStageFlagBits = 0x00000010
val VkShaderStage_COMPUTE_BIT: VkShaderStageFlagBits = 0x00000020
val VkShaderStage_ALL_GRAPHICS: VkShaderStageFlagBits = 0x0000001F
val VkShaderStage_ALL: VkShaderStageFlagBits = 0x7FFFFFFF


typealias VkPipelineVertexInputStateCreateFlags = VkFlags
typealias VkPipelineInputAssemblyStateCreateFlags = VkFlags

//typedef VkFlags VkPipelineTessellationStateCreateFlags;
typealias VkPipelineViewportStateCreateFlags = VkFlags

typealias VkPipelineRasterizationStateCreateFlags = VkFlags


typealias VkCullModeFlagBits = Int

val VkCullMode_NONE: VkCullModeFlagBits = 0
val VkCullMode_FRONT_BIT: VkCullModeFlagBits = 0x00000001
val VkCullMode_BACK_BIT: VkCullModeFlagBits = 0x00000002
val VkCullMode_FRONT_AND_BACK: VkCullModeFlagBits = 0x00000003

typealias VkCullModeFlags = VkFlags
typealias VkPipelineMultisampleStateCreateFlags = VkFlags
typealias VkPipelineDepthStencilStateCreateFlags = VkFlags

typealias VkPipelineColorBlendStateCreateFlags = VkFlags

typealias VkColorComponentFlagBits = Int

val VkColorComponent_R_BIT: VkColorComponentFlagBits = 0x00000001
val VkColorComponent_G_BIT: VkColorComponentFlagBits = 0x00000002
val VkColorComponent_B_BIT: VkColorComponentFlagBits = 0x00000004
val VkColorComponent_A_BIT: VkColorComponentFlagBits = 0x00000008


typealias VkColorComponentFlags = VkFlags
typealias VkPipelineDynamicStateCreateFlags = VkFlags
typealias VkPipelineLayoutCreateFlags = VkFlags

typealias VkShaderStageFlags = VkFlags

typealias VkSamplerCreateFlags = VkFlags

typealias VkDescriptorSetLayoutCreateFlagBits = Int

val VkDescriptorSetLayoutCreate_PUSH_DESCRIPTOR_BIT_KHR: VkDescriptorSetLayoutCreateFlagBits = 0x00000001

typealias VkDescriptorSetLayoutCreateFlags = VkFlags

typealias VkDescriptorPoolCreateFlagBits = Int

val VkDescriptiorPoolCreate_FREE_DESCRIPTOR_SET_BIT: VkDescriptorPoolCreateFlagBits = 0x00000001

typealias VkDescriptorPoolCreateFlags = VkFlags
//typedef VkFlags VkDescriptorPoolResetFlags;
typealias VkFramebufferCreateFlags = VkFlags

typealias VkRenderPassCreateFlags = VkFlags

typealias VkAttachmentDescriptionFlagBits = Int

val VkAttachmentDescription_MAY_ALIAS_BIT: VkAttachmentDescriptionFlagBits = 0x00000001

typealias VkAttachmentDescriptionFlags = VkFlags


typealias VkSubpassDescriptionFlagBits = Int

val VkSubpassDescription_PER_VIEW_ATTRIBUTES_BIT_NVX: VkSubpassDescriptionFlagBits = 0x00000001
val VkSubpassDescription_PER_VIEW_POSITION_X_ONLY_BIT_NVX: VkSubpassDescriptionFlagBits = 0x00000002

typealias VkSubpassDescriptionFlags = VkFlags

typealias VkAccessFlagBits = Int

val VkAccess_INDIRECT_COMMAND_READ_BIT: VkAccessFlagBits = 0x00000001
val VkAccess_INDEX_READ_BIT: VkAccessFlagBits = 0x00000002
val VkAccess_VERTEX_ATTRIBUTE_READ_BIT: VkAccessFlagBits = 0x00000004
val VkAccess_UNIFORM_READ_BIT: VkAccessFlagBits = 0x00000008
val VkAccess_INPUT_ATTACHMENT_READ_BIT: VkAccessFlagBits = 0x00000010
val VkAccess_SHADER_READ_BIT: VkAccessFlagBits = 0x00000020
val VkAccess_SHADER_WRITE_BIT: VkAccessFlagBits = 0x00000040
val VkAccess_COLOR_ATTACHMENT_READ_BIT: VkAccessFlagBits = 0x00000080
val VkAccess_COLOR_ATTACHMENT_WRITE_BIT: VkAccessFlagBits = 0x00000100
val VkAccess_DEPTH_STENCIL_ATTACHMENT_READ_BIT: VkAccessFlagBits = 0x00000200
val VkAccess_DEPTH_STENCIL_ATTACHMENT_WRITE_BIT: VkAccessFlagBits = 0x00000400
val VkAccess_TRANSFER_READ_BIT: VkAccessFlagBits = 0x00000800
val VkAccess_TRANSFER_WRITE_BIT: VkAccessFlagBits = 0x00001000
val VkAccess_HOST_READ_BIT: VkAccessFlagBits = 0x00002000
val VkAccess_HOST_WRITE_BIT: VkAccessFlagBits = 0x00004000
val VkAccess_MEMORY_READ_BIT: VkAccessFlagBits = 0x00008000
val VkAccess_MEMORY_WRITE_BIT: VkAccessFlagBits = 0x00010000
val VkAccess_COMMAND_PROCESS_READ_BIT_NVX: VkAccessFlagBits = 0x00020000
val VkAccess_COMMAND_PROCESS_WRITE_BIT_NVX: VkAccessFlagBits = 0x00040000
val VkAccess_COLOR_ATTACHMENT_READ_NONCOHERENT_BIT_EXT: VkAccessFlagBits = 0x00080000

typealias VkAccessFlags = VkFlags

typealias VkDependencyFlagBits = Int

val VkDependency_BY_REGION_BIT: VkDependencyFlagBits = 0x00000001
val VkDependency_VIEW_LOCAL_BIT_KHX: VkDependencyFlagBits = 0x00000002
val VkDependency_DEVICE_GROUP_BIT_KHX: VkDependencyFlagBits = 0x00000004

typealias VkDependencyFlags = VkFlags

typealias VkCommandPoolCreateFlag = Int

val VkCommandPoolCreate_TRANSIENT_BIT: VkCommandPoolCreateFlag = 0x00000001
val VkCommandPoolCreate_RESET_COMMAND_BUFFER_BIT: VkCommandPoolCreateFlag = 0x00000002

typealias VkCommandPoolCreateFlags = VkFlags

//typedef enum VkCommandPoolResetFlagBits {
//    VK_COMMAND_POOL_RESET_RELEASE_RESOURCES_BIT = 0x00000001,
//    VK_COMMAND_POOL_RESET_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkCommandPoolResetFlagBits;
//typedef VkFlags VkCommandPoolResetFlags;
//
//typedef enum VkCommandBufferUsageFlagBits {
//    VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT = 0x00000001,
//    VK_COMMAND_BUFFER_USAGE_RENDER_PASS_CONTINUE_BIT = 0x00000002,
//    VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT = 0x00000004,
//    VK_COMMAND_BUFFER_USAGE_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkCommandBufferUsageFlagBits;
typealias VkCommandBufferUsageFlags = VkFlags
//
//typedef enum VkQueryControlFlagBits {
//    VK_QUERY_CONTROL_PRECISE_BIT = 0x00000001,
//    VK_QUERY_CONTROL_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkQueryControlFlagBits;
//typedef VkFlags VkQueryControlFlags;
//
//typedef enum VkCommandBufferResetFlagBits {
//    VK_COMMAND_BUFFER_RESET_RELEASE_RESOURCES_BIT = 0x00000001,
//    VK_COMMAND_BUFFER_RESET_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkCommandBufferResetFlagBits;
//typedef VkFlags VkCommandBufferResetFlags;
//
//typedef enum VkStencilFaceFlagBits {
//    VK_STENCIL_FACE_FRONT_BIT = 0x00000001,
//    VK_STENCIL_FACE_BACK_BIT = 0x00000002,
//    VK_STENCIL_FRONT_AND_BACK = 0x00000003,
//    VK_STENCIL_FACE_FLAG_BITS_MAX_ENUM = 0x7FFFFFFF
//} VkStencilFaceFlagBits;
//typedef VkFlags VkStencilFaceFlags;
//