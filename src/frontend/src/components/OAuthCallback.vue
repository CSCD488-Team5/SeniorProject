<script setup>
import { onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import TokenService from '@/scripts/TokenService'

const router = useRouter()
// Get token from URL hash
onMounted(async () => {
  const hashParams = new URLSearchParams(window.location.hash.slice(1))
  const accessToken = hashParams.get('access_token')

  console.log('OAuthCallback mounted')
  console.log('Access Token:', accessToken)

  if (accessToken) {
    try {
      // Get Microsoft user info using the access token
      const userInfo = await axios.get('https://graph.microsoft.com/oidc/userinfo', {
        headers: { Authorization: `Bearer ${accessToken}` }
      })

      // Send user info to backend to create JWT token
      const response = await axios.post('http://localhost/api/auth/oauth/microsoft', userInfo.data)

      const { token } = response.data
      TokenService.saveToken(token)
      window.dispatchEvent(new Event('user-logged-in'))
      router.push('/Home')
    } catch (err) {
      console.error('OAuth failed:', err)
    }
  } else {
    console.warn('No access token found in URL')
  }
})
</script>

<template>
  <v-container class="fill-height d-flex align-center justify-center">
    <div class="text-center">
      <v-progress-circular indeterminate color="primary" />
      <p>Signing in with Microsoft...</p>
    </div>
  </v-container>
</template>
