<template>
  <v-container max-width="800px">
    <div v-if="eventData">
      <!-- Display the event image if available -->
      <v-img
        v-if="eventData.imageUrl"
        :src="formattedImageSrc"
        height="300px"
        cover
      ></v-img>
      <h1>{{ eventData.title }}</h1>
      <p><strong>Subtitle:</strong> {{ eventData.subtitle }}</p>
      <p><strong>Content:</strong> {{ eventData.content }}</p>
      <p><strong>Time:</strong> {{ eventData.time }}</p>
      <p><strong>Location:</strong> {{ eventData.location }}</p>
      <!-- Add any other details you need -->
    </div>
    <div v-else>
      <p>Loading event details...</p>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed, getCurrentInstance } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const eventId = route.params.id

// We'll store the fetched event in a reactive variable
const eventData = ref(null)

// Using the configured axios that is used in main.js
const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http

onMounted(async () => {
  try {
    const response = await axios.get(`http://localhost/api/events/${eventId}`)
    eventData.value = response.data // Axios parses it in json automatically
  } catch (error) {
    console.error('Error fetching event details:', error)
    alert('You must be logged in to view this event.') // We can delete this later, debugging purposes lol
  }
})

// Computed property to format the Base64 image data as a data URI
const formattedImageSrc = computed(() => {
  if (eventData.value && eventData.value.imageUrl) {
    return eventData.value.imageUrl.startsWith('http')
      ? eventData.value.imageUrl
      : `http://localhost:80${eventData.value.imageUrl}`;
  }
  return '';
});
</script>
