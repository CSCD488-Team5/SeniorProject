<template>
  <div id="app">
    <v-app>
      <NavBar />
      <v-main>
        <router-view/>
      </v-main>

    </v-app>

  </div>
</template>

<style>
#app {
  min-height: 100vh;
}
</style>
<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
import { useTheme } from 'vuetify'
import { onMounted } from 'vue'

const theme = useTheme()

onMounted(() => {
  const userTheme = localStorage.getItem('theme')
  if (userTheme) {
    theme.global.name.value = userTheme
  }
})
</script>
