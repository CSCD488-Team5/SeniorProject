<script setup>
import man from "@/assets/man.png"
import { ref } from "vue"
import axios from "axios"
import { useRouter } from "vue-router"

defineProps({
  username: {
    type: String,
    default: "User"
  }
})

const emit = defineEmits(["logout"])
const router = useRouter()
const menu = ref(false)
const showPasswordDialog = ref(false)
const currentPassword = ref("")
const newPassword = ref("")
const snackbar = ref(false)
const snackbarMessage = ref("")

// change password logic
const submitChangePassword = async () => {
  try {
    await axios.post("http://localhost/api/auth/change-password", {
      currentPassword: currentPassword.value,
      newPassword: newPassword.value
    })

    snackbarMessage.value = "Password changed successfully! Redirecting to login..."
    snackbar.value = true

    setTimeout(() => {
      currentPassword.value = ""
      newPassword.value = ""
      showPasswordDialog.value = false
      localStorage.removeItem("token")
      window.dispatchEvent(new Event("user-logged-out"))
      router.push("/Login")
    }, 1000)

  } catch (error) {
    console.error("Change password error:", error)
    snackbarMessage.value = error.response?.data || "Failed to change password"
    snackbar.value = true
  }
}

// Sign out logic
const signOut = () => {
  localStorage.removeItem("token")
  emit("logout")
  router.push("/Login")
}
</script>

<template>
  <v-menu v-model="menu" offset-y>
    <template #activator="{ props }">
      <v-btn text v-bind="props">
        <v-avatar size="32">
          <v-img :src="man" />
        </v-avatar>
        <span class="ml-2">{{ username }}</span>
      </v-btn>
    </template>

    <v-list>
      <v-list-item @click="showPasswordDialog = true">
        <v-list-item-title>Change Password</v-list-item-title>
      </v-list-item>

      <v-list-item @click="signOut">
        <v-list-item-title>Sign Out</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>

  <v-dialog v-model="showPasswordDialog" persistent max-width="500">
    <v-card rounded="lg" elevation="4">
      <v-card-title>Change Password</v-card-title>

      <form @submit.prevent="submitChangePassword">
        <v-card-text>
          <v-text-field
              label="Current Password"
              v-model="currentPassword"
              type="password"
              required
          />
          <v-text-field
              label="New Password"
              v-model="newPassword"
              type="password"
              required
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="primary" type="submit">Submit</v-btn>
          <v-btn text @click="showPasswordDialog = false">Cancel</v-btn>
        </v-card-actions>
      </form>
    </v-card>
  </v-dialog>


  <!-- alert message logic-->
  <v-snackbar v-model="snackbar" timeout="1000" location="top">
    {{ snackbarMessage }}
  </v-snackbar>
</template>

<style scoped>
.v-btn {
  text-transform: none;
}
</style>
