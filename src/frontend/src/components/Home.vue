<template>

  <v-sheet class="mx-auto" max-width="1000">
    <v-slide-group>
      <v-slide-item v-for="event in events" :key="event.id">
        <div class="card-wrapper">
          <EventCard 
            :id="event.id"
            :imageSrc="event.imageBase64" 
            :title="event.title" 
            :subtitle="event.subtitle"
            :content="event.content" 
          />
        </div>
      </v-slide-item>
    </v-slide-group>
  </v-sheet>


</template>

<script setup>
import EventCard from '@/components/EventCard.vue'
import { ref, onMounted } from 'vue'

const show = ref(false)

const events = ref([]) // Reactive variable for events

onMounted(async () => {
  try {
    // Fetch events from backend endpoint
    const response = await fetch('http://localhost:80/api/events')
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    events.value = await response.json()
  } catch (error) {
    console.error('Error fetching events:', error)
  }
})
</script>

<style scoped>
.home
{
  padding: 20px;
}

/* This wrapper forces each card to a fixed width and adds spacing between them */
.card-wrapper
{
  width: 300px;
  /* Set the desired fixed width for each card */
  margin-right: 16px;
  /* Space between cards */
  padding: 10px;
}
</style>
