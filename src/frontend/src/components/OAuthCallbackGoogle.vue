<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

onMounted(async () => {
  const code = new URLSearchParams(window.location.search).get('code')

  if (!code) return

  const token = localStorage.getItem('token') // ✅ Get JWT from localStorage

  try {
    await axios.post(
      'http://localhost/api/calendar/oauth',
      { code },
      {
        headers: {
          Authorization: `Bearer ${token}`, // ✅ Attach JWT here
        },
      }
    )
    router.push('/Home') // or wherever you want to redirect after success
  } catch (error) {
    console.error('Google OAuth failed:', error)
  }
})
</script>
