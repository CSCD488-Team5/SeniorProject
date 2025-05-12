<template>

  <v-sheet class="mx-auto" max-width="1000">
    <v-slide-group>
      <v-slide-item v-for="event in events" :key="event.id">
        <div class="card-wrapper">
          <EventCard 
            :id="event.id"
            :imageSrc="event.imageUrl" 
            :title="event.title"
            :category="event.category"
            :description="event.description"
            :location="event.location"
            :time="event.time"
            :username="event.user?.username"
          />
        </div>
      </v-slide-item>
    </v-slide-group>
  </v-sheet>


</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import EventCard from './EventCard.vue';

const events = ref([]); // Reactive variable for events

const { appContext } = getCurrentInstance();
const axios = appContext.config.globalProperties.$http; // Uses token from main.js interceptor

onMounted(async () => {
  try {
    // Fetch events from backend endpoint using axios now
    const response = await axios.get("http://localhost/api/events");
    events.value = response.data;
  } catch (error) {
    console.error("Error loading events:", error);
    alert("You must be logged in to see events.");
  }
});
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
