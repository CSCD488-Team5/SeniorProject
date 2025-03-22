<script setup>
import man from "@/assets/man.png"; // Import man image for icon
</script>

<template>
  <v-app-bar app color="primary" dark>
    <v-toolbar-title>My App</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn text to="/Home">Home</v-btn>
    <v-btn text to="/PostPage">Posts</v-btn>
    <v-btn text to="/Login">Login</v-btn>
    <v-btn text>
      <v-avatar size="32" color="red">
        <v-img :src="man"></v-img>
      </v-avatar>
      <span class="ml-2">John Doe</span>
    </v-btn>
  </v-app-bar>
</template>

<style scoped>

</style>