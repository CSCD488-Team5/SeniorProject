<script setup>
import man from "@/assets/man.png"; // Import man image for icon
import ProfileDropdown from "@/components/ProfileDropdown.vue";
import { getUsernameFromToken } from "@/utils/jwt.js";
import { onMounted, ref } from "vue";

const username = ref('');

onMounted(() => {
  const checkToken = setInterval(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const name = getUsernameFromToken();
      console.log("Token loaded. Username:", name);
      username.value = name;
      clearInterval(checkToken); // stop checking once we got it
    }
  }, 200); // check every 200ms
})

</script>

<template>
  <v-app-bar app color="primary" dark>
    <v-toolbar-title>My App</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn text to="/Home">Home</v-btn>
    <v-btn text to="/PostPage">Posts</v-btn>
    <v-btn text to="/Login">Login</v-btn>
    <ProfileDropdown :username="username" />
  </v-app-bar>
</template>

<style scoped></style>
