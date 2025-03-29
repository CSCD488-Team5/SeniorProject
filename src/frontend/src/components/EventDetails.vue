<template>
  <v-container>
    <div v-if="eventData">
      <!-- Display the event image if available -->
      <v-img
        v-if="eventData.imageBase64"
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
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const eventId = route.params.id

// We'll store the fetched event in a reactive variable
const eventData = ref(null)

onMounted(async () => {
  try {
    // Replace 'http://localhost:8080' with the actual host/port of your Spring Boot backend
    const response = await fetch(`http://localhost:80/api/events/${eventId}`)
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    // Parse JSON and assign to eventData
    eventData.value = await response.json()
  } catch (error) {
    console.error('Error fetching event details:', error)
  }
})

// Computed property to format the Base64 image data as a data URI
const formattedImageSrc = computed(() => {
  if (eventData.value && eventData.value.imageBase64) {
    // If the image data doesn't already start with 'data:', prepend the Base64 prefix.
    return eventData.value.imageBase64.startsWith('data:')
      ? eventData.value.imageBase64
      : `data:image/jpeg;base64,${eventData.value.imageBase64}`
  }
  return ''
})
</script>
