<template>
  <v-container>
    <h1>{{ eventData.title }}</h1>
    <p><strong>Subtitle:</strong> {{ eventData.subtitle }}</p>
    <p><strong>Content:</strong> {{ eventData.content }}</p>
    <p><strong>Time:</strong> {{ eventData.time }}</p>
    <p><strong>Location:</strong> {{ eventData.location }}</p>
    <!-- Add any other details you need -->
  </v-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

// For demo, we import or define the same events array you have in Home.vue.
// In a real app, you might fetch this from an API or a Vuex/Pinia store.
const events = [
  {
    id: 1,
    title: 'Outdoor Basketball Showdown',
    subtitle: 'Feel the energy of the court',
    content: 'Lace up your sneakers... (truncated for brevity)',
    time: 'July 15, 3:00 PM',
    location: 'Campus Basketball Court'
  },
  // ... other events
]

const route = useRoute()
const eventId = route.params.id

// Find the matching event by ID
const eventData = computed(() => {
  return events.find((e) => e.id === parseInt(eventId))
})
</script>
