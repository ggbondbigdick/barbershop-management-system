import { defineStore } from 'pinia'
import {ref} from 'vue'

export const userAgreeStore = defineStore('userAgree', () => {
    let userAgree = ref<boolean>(false)

    const setUserAgree = (value: boolean) => { userAgree.value = value }

    return { userAgree, setUserAgree }
}, { persist: true })
