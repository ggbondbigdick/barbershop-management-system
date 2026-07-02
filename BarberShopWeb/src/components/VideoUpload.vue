<template>
  <div class="video-uploader">
    <el-upload
        ref="upload"
        action="#"
        drag
        :limit="1"
        accept="video/mp4"
        :show-file-list="false"
        :auto-upload="false"
        :on-change="handleFileChange"
    >
      <div v-if="!videoUrl" class="upload-area">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将视频拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" v-if="tip">{{ tip }}</div>
      </div>
      <div v-else class="video-preview">
        <video :src="videoUrl" controls class="video-element"/>
        <div class="video-actions">
          <el-button type="danger" @click.stop="handleRemove">删除</el-button>
        </div>
      </div>
    </el-upload>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import {uploadFile, getFileInfo,deleteFile} from '@/api/FileApi'

const props = defineProps({
  tip: {
    type: String,
    default: '只能上传mp4格式的视频，且不超过100MB'
  }
})
const emit = defineEmits(['uploadSuccess', 'remove'])
defineExpose({initVideoUrl, clearVideo})

const upload = ref()
const accept = ['video/mp4']
const videoUrl = ref('')
let uploadFileId = ref('')

const handleFileChange = async (file: any) => {

  if (!accept.includes(file.raw.type)) {
    ElMessage.error('上传文件只能是视频格式!')
    return
  }

  if (file.raw.size > 100 * 1024 * 1024) {
    ElMessage.error('视频大小不能超过 100MB!')
    return
  }

  videoUrl.value = URL.createObjectURL(file.raw)
  let formData = new FormData()
  formData.append('file', file.raw)

  const res: any = await uploadFile(formData)
  if (!res.success) {
    ElMessage.error('上传视频失败，请重试')
    return
  }
  uploadFileId.value = res.result.id
  emit('uploadSuccess', res.result)
}

function initVideoUrl(id: string) {
  getVideoUrl(id)
}

const getVideoUrl =async (id: string) => {
  if (!id) return
  const res: any = await getFileInfo(id)
  if (res.success) {
    uploadFileId.value = res.result.id
    videoUrl.value = res.result.url
  }
}

const handleRemove = () => {
  upload.value.clearFiles()
  videoUrl.value = ''
  clearUploadFile()
  emit('remove')
}

function clearVideo() {
  upload.value.clearFiles()
  videoUrl.value = ''
  uploadFileId.value = ''
}

const clearUploadFile = async () => {
  if (!uploadFileId.value) return
  const res: any = await deleteFile(uploadFileId.value)
  if (res.success) {
    uploadFileId.value = ''
  }
}
</script>

<style lang="less" scoped>
.video-uploader {
  position: relative;
}

.upload-area {
  padding: 20px;
}

.video-preview {
  position: relative;
  padding: 10px;
}

.video-element {
  width: 100%;
  max-height: 400px;
  border-radius: 4px;
}

.video-actions {
  margin-top: 10px;
  display: flex;
  justify-content: center;

  .el-button {
    width: 160px;
  }
}
</style>
