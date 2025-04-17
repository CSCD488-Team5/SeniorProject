<script setup>
import man from "@/assets/man.png"; // Import man image for icon
import ProfileDropdown from "@/components/ProfileDropdown.vue";
import { getUsernameFromToken } from "@/utils/jwt.js";
import {onMounted, onUnmounted, ref,} from "vue";

const username = ref('');

const refreshUsername = () => {
  const token = localStorage.getItem("token")
  if (token) {
    username.value = getUsernameFromToken()
  } else {
    username.value = ""
  }
}

// refresh on page load
onMounted(() => {
  refreshUsername()
  window.addEventListener("user-logged-in", refreshUsername);
  window.addEventListener("user-logged-out", refreshUsername);
})

onUnmounted(() => {
  window.removeEventListener("user-logged-in", refreshUsername);
  window.removeEventListener("user-logged-out", refreshUsername); // 👈 AND THIS
});

const handleLogout = () => {
  username.value = ""
}
</script>

<template>
  <v-app-bar app color="primary" dark>
    <v-toolbar-title>My App</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn text to="/Home">Home</v-btn>
    <v-btn text to="/PostPage">Posts</v-btn>
    <v-btn v-if="!username" text to="/Login">Login</v-btn>
    <ProfileDropdown v-if="username" :username="username" @logout="handleLogout" />
  </v-app-bar>
</template>

<style scoped></style>
