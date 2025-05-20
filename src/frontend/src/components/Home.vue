<template>

  <v-sheet color="primary" class="pa-4 mb-6 text-white">
    <h1 class="text-h4">Welcome to Campus Events</h1>
    <p>Discover and follow events happening around your university.</p>
  </v-sheet>

  <v-sheet class="mx-auto" max-width="1000">


    <!-- Bottom: Events Grid -->
    <h2 class="mt-8">Browse Events</h2>
    <p class="text-subtitle-2 mb-4">
      Search and filter to discover events — or toggle to see only the ones you're following.
    </p>
    <v-row class="d-flex flex-wrap align-center mb-4 gap-4">
      <v-col cols="12" sm="6" md="4">
        <v-radio-group v-model="filterOption" row color="primary" inline>
          <v-radio label="All" value="all" />
          <v-radio label="Category" value="category" />
        </v-radio-group>
      </v-col>

      <v-col cols="auto" class="d-flex align-center pt-0">
        <v-switch v-model="showFollowedOnly" label="Show only followed" color="primary" hide-details  density="compact"/>
      </v-col>

      <v-col v-if="filterOption === 'category'" cols="12" sm="6" md="3">
        <v-select v-model="selectedValue" :items="categoryOptions" label="Category" dense clearable outlined />
      </v-col>

      
    </v-row>

    <!-- Search bar -->
    <v-text-field v-model="searchQuery" label="Search events" prepend-inner-icon="mdi-magnify" clearable
      class="mb-4" dense outlined @click:clear="onSearchClear" />


    <!-- Pagination -->
    <!-- Followed Events Grid or Skeleton Loader -->
    <v-fade-transition>
      <v-row v-if="!followedLoading">
        <v-col v-for="event in paginatedEvents" :key="event.id" cols="12" sm="6" md="4">
          <EventCard 
          :id="event.id" 
          :imageSrc="event.imageUrl" 
          :title="event.title" 
          :category="event.category"
          :description="event.description"
          :location="event.location"
          :time="event.time"
          :username="event.user?.username"
          :joined="event.joined" 
          @update:joined="updateJoinedStatus"/>
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
      <v-col v-for="event in filteredEvents" :key="event.id" cols="12" sm="6" md="4">
        <EventCard :id="event.id" :imageSrc="event.imageBase64" :title="event.title" :subtitle="event.subtitle"
          :content="event.content" />
      </v-col>
    </v-row> -->
  </v-sheet>


</template>

<script setup>
import { ref, onMounted, getCurrentInstance, computed, watch } from 'vue';
import EventCard from './EventCard.vue';
import { getUsernameFromToken } from '@/utils/jwt'

const allEvents = ref([]);

onMounted(async () => {
  followedLoading.value = true
  try {
    const username = getUsernameFromToken();
    const response = await axios.get(`api/events/getAllEventsForUser/${username}`)
    allEvents.value = response.data
  } catch (error) {
    console.error('Error fetching personalized events: ', error)
  } finally {
    followedLoading.value = false
  }
})

onMounted(async () => {
  try {
    const response = await axios.get('api/events/categories')
    categoryOptions.value = response.data;
  } catch (error) {
    console.error('Error fetching categories: ', error)
  }
})

const filterOption = ref('all'); // "category"
const selectedValue = ref(null); // dropdown selection

// These will power the dropdown options
const categoryOptions = ref([]);

const searchQuery = ref('');

// Simulated filtered results

const showFollowedOnly = ref(false);



const filteredEvents = computed(() => {
  let filtered = allEvents.value;

  if (showFollowedOnly.value) {
    filtered = filtered.filter(e => e.joined); // assumes each event has a `followed: true/false` flag
  }

  if (filterOption.value === 'category' && selectedValue.value) {
    filtered = filtered.filter(e => e.category === selectedValue.value);
  }

  if (searchQuery.value.trim() !== '') {
    const q = searchQuery.value.trim().toLowerCase();
    filtered = filtered.filter(e =>
      e.title.toLowerCase().includes(q) ||
      e.description.toLowerCase().includes(q) ||
      e.category.toLowerCase().includes(q) ||
      e.location.toLowerCase().includes(q)
    );
  }

  return filtered;
});

function onSearchClear() {
  searchQuery.value = '';
  selectedValue.value = null;
  filterOption.value = 'all';
  currentPage.value = 1;
}

const { appContext } = getCurrentInstance();
const axios = appContext.config.globalProperties.$http; // Uses token from main.js interceptor

// const loading = ref(false); // Loading state
const followedLoading = ref(false);



// Pagination logic (if needed in the future)
const currentPage = ref(1);
const itemsPerPage = 6; // Number of items per page


const paginatedEvents = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredEvents.value.slice(start, start + itemsPerPage);
});

const totalPages = computed(() => {
  return Math.ceil(filteredEvents.value.length / itemsPerPage);
});

watch(filterOption, async () => {
  followedLoading.value = true; // Set loading to true when filter changes
  selectedValue.value = null; // reset dropdown when switching filter type
  currentPage.value = 1; // reset pagination when switching filter type
  followedLoading.value = false; // Set loading to false after data is fetched
});

watch(searchQuery, (val) => {
  if (val === '') {
    selectedValue.value = null;
    filterOption.value = 'all';
    currentPage.value = 1;
  }
})

function updateJoinedStatus({ id, joined }) {
  const target = allEvents.value.find(e => e.id === id)
  if (target) target.joined = joined
}
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
