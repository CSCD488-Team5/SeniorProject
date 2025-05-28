<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

onMounted(async () => {
  const code = new URLSearchParams(window.location.search).get('code')
  if (!code) {
    router.push('/Home')
    return
  }

  const token = localStorage.getItem('token')
  try {
    await axios.post(
      'http://localhost/api/calendar/oauth',
      { code },
      {
        headers: {
          Authorization: `Bearer ${token}`, 
        },
      }
    )
    
    window.dispatchEvent(new Event('calendar-connected'))
    router.push('/Home')
  } catch (error) {
    console.error('Google OAuth failed:', error)
    router.push('/Home')
  }
})
</script>

<template>
  <div class="d-flex justify-center align-center" style="height: 100vh">
    <v-progress-circular indeterminate color="primary" />
  </div>
</template>