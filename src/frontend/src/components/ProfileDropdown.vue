<script setup>
import man from "@/assets/man.png"
import { ref, onMounted } from "vue"
import axios from "axios"
import { useRouter } from "vue-router"

const props = defineProps({
  username: {
    type: String,
    default: "User"
  }
})

const emit = defineEmits(["logout"])
const router = useRouter()
const menu = ref(false)
const showPasswordDialog = ref(false)
const showDeleteDialog = ref(false)
const showSettingsDialog = ref(false)
const currentPassword = ref("")
const newPassword = ref("")
const snackbar = ref(false)
const snackbarMessage = ref("")
const isCalendarConnected = ref(false)
const switchEnabled = ref(false)
const isLoading = ref(false)

onMounted(async () => {
  await checkCalendarStatus()
  
  // Listen for calendar connection events
  window.addEventListener('calendar-connected', async () => {
    await checkCalendarStatus()
    snackbarMessage.value = "Google Calendar connected successfully!"
    snackbar.value = true
  })
})

const checkCalendarStatus = async () => {
  try {
    const response = await axios.get("http://localhost/api/calendar/status", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    })
    isCalendarConnected.value = response.data.connected
    switchEnabled.value = response.data.connected
  } catch (error) {
    console.error("Failed to fetch calendar sync status:", error)
  }
}

const handleCalendarToggle = async (newValue) => {
  isLoading.value = true
  
  try {
    if (newValue) {
      // Connect to Google Calendar
      const clientId = import.meta.env.VITE_GOOGLE_CLIENTID
      const redirectUri = import.meta.env.VITE_GOOGLE_URL
      const scope = 'https://www.googleapis.com/auth/calendar.events'
      const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&access_type=offline&prompt=consent`
      window.location.href = authUrl
    } else {
      // Disconnect from Google Calendar
      await axios.post("http://localhost/api/calendar/disconnect", {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      })
      isCalendarConnected.value = false
      switchEnabled.value = false
      snackbarMessage.value = "Google Calendar disconnected successfully!"
      snackbar.value = true
    }
  } catch (error) {
    console.error("Calendar operation failed:", error)
    snackbarMessage.value = "Failed to perform calendar operation"
    snackbar.value = true
    
    switchEnabled.value = isCalendarConnected.value
  } finally {
    isLoading.value = false
  }
}

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
  snackbarMessage.value = "Logged out successfully!"
  snackbar.value = true

  setTimeout(() => {
    emit("logout")
    router.push("/Login")
  }, 800)
}

// Delete Account Logic
const deleteAccount = async () => {
  try {
    await axios.delete("http://localhost/api/auth/delete-account", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    })

    snackbarMessage.value = "Account deleted. Goodbye!"
    snackbar.value = true
    showDeleteDialog.value = false

    setTimeout(() => {
      localStorage.removeItem("token")
      window.dispatchEvent(new Event("user-logged-out"))
      router.push("/Login")
    }, 1000)
  } catch (error) {
    console.error("Delete account error:", error)
    snackbarMessage.value = error.response?.data || "Failed to delete account"
    snackbar.value = true
  }
}

// Profile logic
const profile = () => {
  router.push({ name: 'Profile', params: { username: props.username }})
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
      <v-list-item @click="profile">
        <v-list-item-title>Profile</v-list-item-title>
      </v-list-item>

      <v-list-item @click="showSettingsDialog = true">
        <v-list-item-title>Settings</v-list-item-title>
      </v-list-item>

      <v-list-item @click="signOut">
        <v-list-item-title>Sign Out</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>

  <!-- Settings Dialog -->
  <v-dialog v-model="showSettingsDialog" max-width="500">
    <v-card rounded="xl" class="pa-4">
      <v-card-title class="text-h5 pb-4">Settings</v-card-title>
      <v-card-text>
        <v-list class="pa-0">
          <v-list-item class="px-0">
            <template v-slot:prepend>
              <v-switch
                v-model="switchEnabled"
                @update:model-value="handleCalendarToggle"
                :loading="isLoading"
                color="primary"
                :disabled="isLoading"
              ></v-switch>
            </template>
            <v-list-item-title>
              <v-icon :color="isCalendarConnected ? 'success' : 'grey'" class="mr-2">
                {{ isCalendarConnected ? 'mdi-calendar-check' : 'mdi-calendar' }}
              </v-icon>
              Google Calendar
            </v-list-item-title>
            <v-list-item-subtitle>
              {{ isLoading ? 'Processing...' : (isCalendarConnected ? 'Connected - Click to disconnect' : 'Click to connect') }}
            </v-list-item-subtitle>
          </v-list-item>

          <v-divider class="my-2"></v-divider>

          <v-list-item class="px-0" @click="showPasswordDialog = true">
            <v-list-item-title>
              <v-icon class="mr-2">mdi-key</v-icon>
              Change Password
            </v-list-item-title>
          </v-list-item>

          <v-list-item class="px-0" @click="showDeleteDialog = true">
            <v-list-item-title class="text-red">
              <v-icon class="mr-2" color="red">mdi-delete</v-icon>
              Delete Account
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card-text>
      <v-card-actions class="pt-2">
        <v-spacer></v-spacer>
        <v-btn text @click="showSettingsDialog = false">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- Password Dialog -->
  <v-dialog v-model="showPasswordDialog" persistent max-width="500">
    <v-card rounded="xl" class="pa-4">
      <v-card-title class="text-h5 pb-4">Change Password</v-card-title>
      <form @submit.prevent="submitChangePassword">
        <v-card-text>
          <v-text-field label="Current Password" v-model="currentPassword" type="password" required />
          <v-text-field label="New Password" v-model="newPassword" type="password" required />
        </v-card-text>
        <v-card-actions class="pt-2">
          <v-spacer />
          <v-btn color="primary" type="submit">Submit</v-btn>
          <v-btn text @click="showPasswordDialog = false">Cancel</v-btn>
        </v-card-actions>
      </form>
    </v-card>
  </v-dialog>

  <!-- Delete Account Dialog -->
  <v-dialog v-model="showDeleteDialog" max-width="450">
    <v-card rounded="xl" class="pa-4">
      <v-card-title class="text-h5 pb-4">Confirm Account Deletion</v-card-title>
      <v-card-text>
        Are you sure you want to delete your account? This action cannot be undone.
      </v-card-text>
      <v-card-actions class="pt-2">
        <v-spacer />
        <v-btn text @click="showDeleteDialog = false">Cancel</v-btn>
        <v-btn color="red" @click="deleteAccount">Delete</v-btn>
      </v-card-actions>
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
