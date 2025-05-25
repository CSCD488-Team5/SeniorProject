<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
const router = useRouter()
onMounted(async () => {
  const code = new URLSearchParams(window.location.search).get('code')
  if (!code) return
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
    router.push('/Home')
  } catch (error) {
    console.error('Google OAuth failed:', error)
  }
})
</script>