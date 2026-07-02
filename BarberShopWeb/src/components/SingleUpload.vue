<template>
  <el-upload
      ref="uploadRef"
      class="avatar-uploader"
      action="#"
      :limit="1"
      :disabled="Boolean(uploadImgUrl)"
      accept="image/*"
      :auto-upload="false"
      :show-file-list="false"
      :on-change="handleUploadFile"
  >
    <div class="avatar-img-box" :class="$props.size">
      <template v-if="uploadImgUrl">
        <el-image class="avatar-img" :src="uploadImgUrl"/>
        <div class="mask-box" :class="$props.size">
          <el-icon class="mask-icon" @click.stop="previewImage">
            <ZoomIn/>
          </el-icon>
          <el-icon class="mask-icon" @click.stop="removeImage">
            <Delete/>
          </el-icon>
        </div>
      </template>
      <template v-else>
        <el-icon class="avatar-uploader-icon">
          <Plus/>
        </el-icon>
      </template>
    </div>
  </el-upload>

  <!-- 图片预览弹窗 -->
  <el-image-viewer
      v-if="isPreviewAvatar"
      :url-list="previewUrlList"
      teleported
      hide-on-click-modal
      @close="handleClosePreviewer"
  />
</template>

<script setup lang="ts">
import {ref} from 'vue'
import {deleteFile, uploadFile, getFileInfo} from '@/api/FileApi'
import {UploadInstance, UploadRawFile, UploadFile, ElMessage} from 'element-plus'

const $props = defineProps({
  // 默认图片路径
  defaultImgUrl: {
    type: String,
    default: '',
    required: true
  },
  // 组件整体尺寸大小
  size: {
    type: String as () => 'small' | 'default' | 'large',
    default: 'default'
  }
})

const uploadRef = ref<UploadInstance>()
// 用户上传文件
const uploadFileData = ref<any>()
// 图片路径
const uploadImgUrl = ref<string>('')

// 上传文件
const handleUploadFile = async (file: UploadFile) => {
  if (file.raw && checkUploadFile(file.raw)) {
    // 调用接口上传文件
    let fileFormData = new FormData()
    fileFormData.append('file', file.raw)
    const res: any = await uploadFile(fileFormData)
    if (res.success) {
      uploadFileData.value = res.result
      uploadImgUrl.value = res.result.url
    }
  } else {
    uploadRef.value?.clearFiles()
    Object.keys(uploadFileData.value).map(key => delete uploadFileData.value[key])
  }
}

// 图片校验
function checkUploadFile(rawFile: UploadRawFile) {
  const isImg = rawFile.type.startsWith('image/')
  const isLt2M = rawFile.size / 1024 / 1024 < 10
  if (!isImg) {
    ElMessage.error('上传文件只能是图片格式!')
  }
  if (!isLt2M) {
    ElMessage.error('文件大小不能超过 10MB!')
  }
  return isImg && isLt2M
}

// 移除图片
const removeImage = async () => {
  // 调用接口删除文件
  const res: any = await deleteFile(uploadFileData.value.id)
  if (res.success) {
    Object.keys(uploadFileData.value).map(key => delete uploadFileData.value[key])
    uploadImgUrl.value = ''
    uploadRef.value?.clearFiles()
  }
}

const isPreviewAvatar = ref(false)
const previewUrlList = ref<string[]>([])

// 预览图片
const previewImage = () => {
  previewUrlList.value = [uploadImgUrl.value]
  isPreviewAvatar.value = true
}

// 关闭预览组件
const handleClosePreviewer = () => {
  isPreviewAvatar.value = false
  previewUrlList.value = []
}

// 设置预设图片
const setImageUrl = async (fileId: string) => {
  // 调用接口获取文件信息
  if (fileId) {
    const res: any = await getFileInfo(fileId)
    if (res.success) {
      uploadFileData.value = res.result
      uploadImgUrl.value = res.result.url
    }
  }
}

// 获取上传的图片数据
const getUploadInfo = () => {
  let result = {
    id: uploadFileData.value?.id,
    name: uploadFileData.value?.name,
    url: uploadFileData.value?.url
  }
  return result
}

// 清空上传文件
const clearUploadFile = () => {
  uploadRef.value?.clearFiles()
  uploadFileData.value = undefined
  uploadImgUrl.value = ''
}

// 暴露给父组件调用的函数
defineExpose({
  setImageUrl,
  getUploadInfo,
  clearUploadFile
})
</script>


<style lang="less" scoped>
.avatar-uploader {
  .avatar-img-box {
    position: relative;
    padding: 5px;
    border: 2px dotted var(--el-border-color);
    border-radius: 6px;

    &:hover {
      border-color: var(--el-color-primary);
    }

    .avatar-img {
      width: 100%;
      height: 100%;
    }

    &:hover {
      .mask-box {
        display: flex;
      }
    }
  }

  .mask-box {
    position: absolute;
    left: 5px;
    top: 5px;
    align-items: center;
    justify-content: space-evenly;
    opacity: 0.8;
    display: none;
    background-color: #666;
    cursor: auto;

    .mask-icon {
      color: #fff;
      font-size: 16px;
      cursor: pointer;

      &:hover {
        color: var(--el-color-primary);
      }
    }
  }

  .small {
    width: 130px;
    height: 130px;
  }

  .default {
    width: 110px;
    height: 110px;
  }

  .large {
    width: 160px;
    height: 160px;
  }
}

::v-deep(.avatar-uploader-icon) {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  font-size: 50px;
  color: #dcdfe6;
}
</style>
