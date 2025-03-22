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
.app{
  background-color: white;
}
</style>
<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
</script>