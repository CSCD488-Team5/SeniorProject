<script setup>
import man from "@/assets/man.png";
import { ref } from "vue";

defineProps({
  username: {
    type: String,
    default: "User"
  }
});

const menu = ref(false);
</script>

<template>
  <v-menu v-model="menu" offset-y>
    <template #activator="{ props }">
      <v-btn text v-bind="props">
        <v-avatar size="32">
          <v-img :src="man"></v-img>
        </v-avatar>
        <span class="ml-2">{{ username }}</span>
      </v-btn>
    </template>

    <v-list>
      <v-list-item>
        <v-list-item-title>Change Password</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Change Email</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Sign Out</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<style scoped>
.v-btn {
  text-transform: none;
}
</style>
