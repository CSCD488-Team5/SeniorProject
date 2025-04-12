<script setup>
import man from "@/assets/man.png"; // Import man image for icon
import { ref, watch } from 'vue'
import { useTheme } from 'vuetify'

const theme = useTheme()


// Use localStorage to persist user's choice
const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
const isDark = ref(localStorage.getItem('theme') === 'dark' || (!localStorage.getItem('theme') && prefersDark))

watch(isDark, (val) => {
  theme.global.name.value = val ? 'dark' : 'light'
  localStorage.setItem('theme', val ? 'dark' : 'light')
})

function toggleTheme() {
  isDark.value = !isDark.value
}
</script>

<template>
  <v-app-bar app :color="isDark ? 'grey-darken-4' : 'primary'" :dark="isDark">
    <v-toolbar-title>My App</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn text to="/Home">Home</v-btn>
    <v-btn text to="/PostPage">Posts</v-btn>
    <v-btn text to="/Login">Login</v-btn>

    <!-- Dark Mode Toggle Switch -->
    <v-btn icon @click="toggleTheme" class="mx-2 animated-toggle">
      <v-fade-transition mode="out-in">
        <v-icon :color="isDark ? 'amber' : 'black'">{{ isDark ? 'mdi-white-balance-sunny' : 'mdi-weather-night' }}</v-icon>
      </v-fade-transition>
    </v-btn>

    <v-btn text>
      <v-avatar size="32" color="red">
        <v-img :src="man"></v-img>
      </v-avatar>
      <span class="ml-2">John Doe</span>
    </v-btn>
  </v-app-bar>
</template>

<style scoped>
.animated-toggle .v-icon {
  transition: transform 0.5s ease;
}

.animated-toggle:active .v-icon {
  transform: rotate(360deg);
}
</style>
