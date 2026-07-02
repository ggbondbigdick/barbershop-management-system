import { defineStore } from 'pinia'
import {ref} from 'vue'

export const useMenuShrink = defineStore('menuShrink', () => {
    let isMenuShrink = ref(false)

    const toggleMenuShrink = () => {
        isMenuShrink.value =!isMenuShrink.value
    }

    return {
        isMenuShrink,
        toggleMenuShrink
    }

}, { persist: true })
