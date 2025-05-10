<template>

  <v-sheet color="primary" class="pa-4 mb-6 text-white">
    <h1 class="text-h4">Welcome to Campus Events</h1>
    <p>Discover and follow events happening around your university.</p>
  </v-sheet>

  <v-sheet class="mx-auto" max-width="1000">
    <!-- Upcoming Events -->
    <h2 class="mt-8">Upcoming Events</h2>
    <v-fade-transition>
      <div v-if="upcomingLoading">
        <v-row>
          <v-col v-for="n in 3" :key="n">
            <v-skeleton-loader type="image, article" height="250" class="mb-4" />
          </v-col>
        </v-row>
      </div>
      <v-slide-group v-else show-arrows center-active class="py-4">
        <v-slide-item v-for="event in events" :key="event.id">
          <div class="card-wrapper">
            <EventCard :id="event.id" :imageSrc="event.imageBase64" :title="event.title" :subtitle="event.subtitle"
              :content="event.content" />
          </div>
        </v-slide-item>
      </v-slide-group>
    </v-fade-transition>

    <!-- Fallback if empty events -->
    <v-fade-transition>
      <v-alert v-if="events.length === 0 && !upcomingLoading" type="info" variant="tonal" class="my-6" border="start">
        No upcoming events found. Try again later or check your followed events below.
      </v-alert>
    </v-fade-transition>

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


    <!-- Pagination -->
    <!-- Followed Events Grid or Skeleton Loader -->
    <v-fade-transition>
      <v-row v-if="!followedLoading">
        <v-col v-for="event in paginatedEvents" :key="event.id" cols="12" sm="6" md="4">
          <EventCard :id="event.id" :imageSrc="event.imageBase64" :title="event.title" :subtitle="event.subtitle"
            :content="event.content" />
        </v-col>
      </v-row>

      <!-- Skeleton loader while loading -->
      <v-row v-else>
        <v-col v-for="n in itemsPerPage" :key="n" cols="12" sm="6" md="4">
          <v-skeleton-loader type="image, article" height="250" class="mb-4" />
        </v-col>
      </v-row>
    </v-fade-transition>

    <!-- Pagination Control -->
    <v-fade-transition>
      <v-pagination v-if="!followedLoading && totalPages > 1" v-model="currentPage" :length="totalPages"
        class="my-4 d-flex justify-center" rounded color="primary" />
    </v-fade-transition>

    <!-- <v-row>
      <v-col v-for="event in followedEvents" :key="event.id" cols="12" sm="6" md="4">
        <EventCard :id="event.id" :imageSrc="event.imageBase64" :title="event.title" :subtitle="event.subtitle"
          :content="event.content" />
      </v-col>
    </v-row> -->
  </v-sheet>


</template>

<script setup>
import { ref, onMounted, getCurrentInstance, computed, watch } from 'vue';
import EventCard from './EventCard.vue';

const events = ref([]); // Reactive variable for events
const allFollowedEvents = ref([
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
  {
    id: 106,
    title: "Art Showcase",
    subtitle: "Student Gallery",
    content: "A collection of student artwork.",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Art+Showcase",
    category: "Arts",
    createdBy: "ArtProf"
  },
  {
    id: 107,
    title: "Chemistry Lab",
    subtitle: "Acids & Bases",
    content: "Live demo in science building.",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Chem+Lab",
    category: "Science",
    createdBy: "ProfJane"
  },
  {
    id: 108,
    title: "Hackathon",
    subtitle: "24-hour coding event",
    content: "Teams compete to build apps.",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Hackathon",
    category: "Tech",
    createdBy: "DevJoe"
  },
  {
    id: 109,
    title: "Football Tailgate",
    subtitle: "Before the big game",
    content: "Food, games, and spirit!",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Tailgate",
    category: "Sports",
    createdBy: "CoachMike"
  },
  {
    id: 110,
    title: "Data Science Meetup",
    subtitle: "Trends in Machine Learning",
    content: "Networking and talks.",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Data+Science",
    category: "Tech",
    createdBy: "DevJoe"
  },
  {
    id: 111,
    title: "Open Mic Night",
    subtitle: "Poetry and music",
    content: "Sign up to perform!",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Open+Mic",
    category: "Arts",
    createdBy: "ClubEvents"
  },
  {
    id: 112,
    title: "Astronomy Club",
    subtitle: "Night Sky Viewing",
    content: "Telescope setup on roof",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Astronomy",
    category: "Science",
    createdBy: "ProfJane"
  },
  {
    id: 113,
    title: "D&D Game Night",
    subtitle: "Fantasy & fun",
    content: "Join a campaign!",
    imageBase64: "https://via.placeholder.com/300x200.png?text=DND",
    category: "Games",
    createdBy: "ClubEvents"
  },
  {
    id: 114,
    title: "Debate Club",
    subtitle: "Free Speech Forum",
    content: "Discuss current issues.",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Debate",
    category: "Politics",
    createdBy: "ProfJane"
  },
  {
    id: 115,
    title: "Startup Pitch Night",
    subtitle: "Student Entrepreneurs",
    content: "Pitch your business idea!",
    imageBase64: "https://via.placeholder.com/300x200.png?text=Startup+Night",
    category: "Business",
    createdBy: "DevJoe"
  }
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

// const loading = ref(false); // Loading state
const upcomingLoading = ref(false);
const followedLoading = ref(false);



onMounted(async () => {
  upcomingLoading.value = true;
  try {
    // Fetch events from backend endpoint using axios now
    const response = await axios.get("http://localhost/api/events");
    events.value = response.data;
  } catch (error) {
    console.error("Error loading events:", error);
    alert("You must be logged in to see events.");
  } finally {
    upcomingLoading.value = false; // Set loading to false after data is fetched
  }
});


// Pagination logic (if needed in the future)
const currentPage = ref(1);
const itemsPerPage = 6; // Number of items per page


const paginatedEvents = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return followedEvents.value.slice(start, start + itemsPerPage);
});

const totalPages = computed(() => {
  return Math.ceil(followedEvents.value.length / itemsPerPage);
});

watch(filterOption, async () => {
  followedLoading.value = true; // Set loading to true when filter changes
  await new Promise(resolve => setTimeout(resolve, 1000)); // Simulate loading delay
  selectedValue.value = null; // reset dropdown when switching filter type
  currentPage.value = 1; // reset pagination when switching filter type
  followedLoading.value = false; // Set loading to false after data is fetched
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

.v-fade-transition-enter-active,
.v-fade-transition-leave-active
{
  transition: opacity 0.5s ease;
}

@media (max-width: 600px)
{
  .card-wrapper
  {
    width: 90vw;
    margin-right: 12px;
  }

  .v-sheet
  {
    padding: 0 8px;
  }
}
</style>
