<template>

  <v-sheet class="mx-auto" max-width="1000">
    <!-- Upcoming Events -->
    <h2 class="mt-8">Upcoming Events</h2>
    <v-slide-group>
      <v-slide-item v-for="event in events" :key="event.id">
        <div class="card-wrapper">
          <EventCard :id="event.id" :imageSrc="event.imageBase64" :title="event.title" :subtitle="event.subtitle"
            :content="event.content" />
        </div>
      </v-slide-item>
    </v-slide-group>

    <!-- Bottom: Followed Events Grid -->
    <h2 class="mt-8">Your Followed Events</h2>

    <v-radio-group v-model="filterOption" row class="mb-4" color="primary" inline>
      <v-radio label="All" value="all" />
      <v-radio label="Filter by Category (Sports)" value="category" />
      <v-radio label="Filter by People (ProfJane)" value="people" />
    </v-radio-group>

    <!-- Show dropdown only if category or people is selected -->
    <v-select v-if="filterOption === 'category'" v-model="selectedValue" :items="categoryOptions"
      label="Select a category" class="mb-4" dense clearable outlined />

    <v-select v-if="filterOption === 'people'" v-model="selectedValue" :items="peopleOptions" label="Select a person"
      class="mb-4" dense clearable outlined />

    <v-row>
      <v-col v-for="event in followedEvents" :key="event.id" cols="12" sm="6" md="4">
        <EventCard :id="event.id" :imageSrc="event.imageBase64" :title="event.title" :subtitle="event.subtitle"
          :content="event.content" />
      </v-col>
    </v-row>
  </v-sheet>


</template>

<script setup>
import { ref, onMounted, getCurrentInstance, computed, watch } from 'vue';
import EventCard from './EventCard.vue';

const events = ref([]); // Reactive variable for events
const allFollowedEvents = ref([ // hardcoded followed events
  {
    id: 101,
    title: "Basketball Game",
    subtitle: "March Madness",
    content: "Come cheer for your team!",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Basketball",
    category: "Sports",
    createdBy: "CoachMike"
  },
  {
    id: 102,
    title: "Math Club Meetup",
    subtitle: "Integration Party",
    content: "Discuss calculus and have fun!",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Math+Club",
    category: "Math",
    createdBy: "ProfJane"
  },
  {
    id: 103,
    title: "Coding Workshop",
    subtitle: "Intro to Vue.js",
    content: "Build a frontend app in 2 hours",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Coding",
    category: "Tech",
    createdBy: "DevJoe"
  },
  {
    id: 104,
    title: "Soccer Game",
    subtitle: "Team A vs Team B",
    content: "College sports event",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Soccer",
    category: "Sports",
    createdBy: "CoachMike"
  },
  {
    id: 105,
    title: "Physics Talk",
    subtitle: "Quantum Fun",
    content: "Learn about particles",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Physics",
    category: "Science",
    createdBy: "ProfJane"
  },
  // Add more as needed...
]);


const filterOption = ref('all'); // "category" or "people"
const selectedValue = ref(null); // dropdown selection

// These will power the dropdown options
const categoryOptions = ['Sports', 'Tech', 'Math', 'Science'];
const peopleOptions = ['ProfJane', 'CoachMike', 'DevJoe'];

// Simulated filtered results
const followedEvents = computed(() => {
  if (filterOption.value === 'category' && selectedValue.value) {
    return allFollowedEvents.value.filter(e => e.category === selectedValue.value);
  } else if (filterOption.value === 'people' && selectedValue.value) {
    return allFollowedEvents.value.filter(e => e.createdBy === selectedValue.value);
  } else {
    return allFollowedEvents.value;
  }
});

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

watch(filterOption, () => {
  selectedValue.value = null; // reset dropdown when switching filter type
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

.mt-8
{
  margin-top: 32px;
}

.mb-4
{
  margin-bottom: 16px;
}
</style>
