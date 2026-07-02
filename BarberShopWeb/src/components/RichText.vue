<template>
  <div
      ref="editor"
      class="rich-text-editor"
  />
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import Quill from 'quill'
import 'quill/dist/quill.snow.css'

const props = defineProps({
  value: {
    type: String,
    default: ''
  },
  options: {
    type: Object,
    default: () => ({})
  }
})
const emit = defineEmits(['input'])
const editor = ref<any>(null)
let quill = ref<any>()

watch(() => props.value, (val) => {
  if (quill && val !== quill.value.root.innerHTML) {
    quill.value.root.innerHTML = val
  }
})

onMounted(() => {
  quill.value = new Quill(editor.value,
      {
        theme: 'snow',
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'],
            ['blockquote', 'code-block'],
            [{header: 1}, {header: 2}],
            [{list: 'ordered'}, {list: 'bullet'}],
            [{script: 'sub'}, {script: 'super'}],
            [{indent: '-1'}, {indent: '+1'}],
            [{direction: 'rtl'}],
            [{size: ['small', false, 'large', 'huge']}],
            [{header: [1, 2, 3, 4, 5, 6, false]}],
            [{color: []}, {background: []}],
            [{font: []}],
            [{align: []}],
            ['clean'],
            ['link', 'image', 'video']
          ],
          ...props.options.modules
        },
        placeholder: props.options.placeholder || '请输入内容'
      }
  )
  quill.value.root.innerHTML = props.value
  quill.value.on('text-change', () => {
    const html = quill.value.root.innerHTML
    emit('input', html || null)
  })
})
</script>

<style lang="less" scoped>
.rich-text-editor {
  height: 300px;
}
</style>
