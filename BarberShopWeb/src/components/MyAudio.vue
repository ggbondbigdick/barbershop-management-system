<template>
  <div>
    <el-upload
        class="avatar-uploader"
        action="#"
        limit="1"
        accept="audio/mpeg"
        :multiple="false"
        :show-file-list="false"
        :auto-upload="false"
        :on-change="handleUploadFile"
        :before-upload="beforeUpload"
        :disabled="disabled"
    >
      <el-button :style="user.role === '1' ? 'width: 90px;' : 'width: 260px;'" slot="trigger" v-if="!voiceUrl">{{user.role === '1' ? '上传录音简介' : 'Introduction to uploading recordings'}}</el-button>
    </el-upload>
    <div class="audio" v-if="voiceUrl">
      <!--<div class="audio-title">{{ voiceTitle }}</div>-->
      <el-button class="btn-del" type="danger" @click="handleRemove">{{user.role === '1' ? '删除' : 'Delete'}}</el-button>
      <audio class="audio-item" ref="audio" controls :src="voiceUrl"/>
    </div>
  </div>
</template>


<script setup lang="ts">
import {reactive, ref, toRefs, watch} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {uploadFile, getFileInfo, deleteFile} from '@/api/FileApi'
import {getUserInfo} from '@/tool/loginTool'

const props = defineProps({
  disabled: {
    type: Boolean,
    default: false
  },
  defaultVoiceUrl: {
    type: String,
    default: ''
  },
  voiceTitle: {
    type: String,
    default: ''
  }
})
const emit = defineEmits(['delAudio'])

const user = getUserInfo()
const loading = ref(false)
const audio = ref()
const voiceUrl = ref(props.defaultVoiceUrl)
// 用户上传文件
const uploadFileData = ref<any>({})

// 获取文件时长
const getAudioDuration = (file: any) => {
  let conteent = file
  // 获取音频时长
  let url = URL.createObjectURL(conteent)
  audio.value = new Audio(url)
  audio.value.addEventListener('loadedmetadata', function () {
    audio.value.duration = parseInt(audio.value.duration)
  })
}

// 上传前校验
const beforeUpload = (file: any) => {
  // 获取文件类型
  const type = file.type
  if (type !== 'audio/mpeg') {
    ElMessage.error('上传文件类型只能为 mp3!')
    return false
  }

  // 获取文件大小
  const size = file.size / 1024 / 1024
  if (size > 15) {
    ElMessage.error('上传文件大小不能超过 15MB!')
    return false
  }

  // 获取文件时长
  getAudioDuration(file)
  if (audio.value > 90) {
    ElMessage.error('音频时长不能超过 90 秒!')
    return false
  }

  return true
}

// 上传音频文件
const handleUploadFile = async (file: any) => {
  let fileFormData = new FormData()
  fileFormData.append('file', file.raw)
  const res: any = await uploadFile(fileFormData)
  if (res.success) {
    voiceUrl.value = res.result.url
    uploadFileData.value = res.result
  } else {
    voiceUrl.value = ''
    Object.keys(uploadFileData.value).map(key => delete uploadFileData.value[key])
  }
}


// 删除音频
const handleRemove = async () => {
  const res: any = await deleteFile(uploadFileData.value?.id)
  if (res.success) {
    voiceUrl.value = ''
    Object.keys(uploadFileData.value).map(key => delete uploadFileData.value[key])
    emit('delAudio')
  }
}


// 设置音频播放 url
const setAudioPayUrl = async (fileId: string) => {
  // 调用接口获取文件信息
  if (fileId) {
    const res: any = await getFileInfo(fileId)
    if (res.success) {
      uploadFileData.value = res.result
      voiceUrl.value = res.result.url
    }
  }
}

// 获取音频信息
const getAudioData = () => {
  return {
    id: uploadFileData.value?.id,
    name: uploadFileData.value?.name,
    url: uploadFileData.value?.url
  }
}

// 销毁音频信息
const destroyAudioData = () => {
  voiceUrl.value = ''
  Object.keys(uploadFileData.value).map(key => delete uploadFileData.value[key])
}

defineExpose({
  setAudioPayUrl,
  getAudioData,
  destroyAudioData
})

</script>

<style lang="less" scoped>
.audio {
  width: 500px;
  height: 60px;

  .audio-item {
    width: 280px;
    height: 40px;
  }

  //.audio-title {
  //  width: auto;
  //  height: 30px;
  //  margin: 0;
  //  padding: 0;
  //}

  .btn-del {
    position: absolute;
    left: 285px;
    width: 80px;
    height: 40px;
    background: #f56c6c;
  }
}
</style>
