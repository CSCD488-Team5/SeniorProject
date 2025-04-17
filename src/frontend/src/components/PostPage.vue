<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import EventCard from '@/components/EventCard.vue'; // adjust path if needed

// Reactive state
const posts = ref([]);
const showModal = ref(false);
const dateMenu = ref(false);
const timeMenu = ref(false);

// Form data for creating a new post
const form = ref({
  title: '',
  subtitle: '',
  description: '',
  category: '',
  location: '',
  date: '',
  time: ''
});

// Axios instance from Vue global properties
const { appContext } = getCurrentInstance();
const axios = appContext.config.globalProperties.$http;

// Fetch posts from the backend when the component is mounted
onMounted(async () => {
  try {
    const response = await axios.get("http://localhost/api/PostPageController");
    posts.value = response.data;
  } catch (err) {
    console.error('Error fetching posts:', err);
  }
});

// Submit post function
const submitPost = async () => {
  try {
    const response = await axios.post("http://localhost/api/createPost", postData);
    posts.value.push(response.data);
    showModal.value = false;
    form.value = {
      title: '',
      subtitle: '',
      description: '',
      category: '',
      location: '',
      date: '',
      time: ''
    };
  } catch (error) {

    console.error("Post did not create!");
    alert("Failed to create post");
  }
};

</script>

<template>
  <!-- Create Post Button -->
  <v-btn color="primary" @click="showModal = true">Create New Post</v-btn>

  <!-- Modal Popup -->
  <v-dialog v-model="showModal" max-width="500px">
    <v-card>
      <v-card-title>Create New Post</v-card-title>

      <v-card-text>
        <v-form @submit.prevent="submitPost">
          <v-text-field v-model="form.title" label="Title" required />
          <v-text-field v-model="form.subtitle" label="Subtitle" required />
          <v-textarea v-model="form.description" label="Description" required />
          <v-text-field v-model="form.category" label="Category" required />
          <v-text-field v-model="form.location" label="Location" required />

          <!-- Date Picker -->
          <v-menu v-model="dateMenu" transition="scale-transition" offset-y>
            <template v-slot:activator="{ props }">
              <v-text-field v-bind="props" v-model="form.date" label="Date" readonly />
            </template>
            <v-date-picker v-model="form.date" @update:model-value="dateMenu = false" />
          </v-menu>

          <!-- Time Picker -->
          <v-menu v-model="timeMenu" transition="scale-transition" offset-y>
            <template v-slot:activator="{ props }">
              <v-text-field v-bind="props" v-model="form.time" label="Time" readonly />
            </template>
            <v-time-picker v-model="form.time" format="24hr" @update:model-value="timeMenu = false" />
          </v-menu>

          <v-btn type="submit" color="success" class="mt-4">Submit</v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>

  <!-- Posts Section -->
  <div class="page-wrapper">
    <div class="posts-box">
      <h2 class="text-h5 mb-4">Posts</h2>

      <v-alert v-if="posts.length === 0" type="info" text class="mb-4">
        No posts yet!
      </v-alert>

      <v-row v-else justify="center" align="stretch" class="post-grid">
        <v-col v-for="post in posts" :key="post.id" cols="12" sm="6" md="4" lg="3">
          <EventCard
              :id="post.id"
              :title="post.title"
              :subtitle="post.subtitle"
              :category="post.category"
              :description="post.description"
              :imageSrc="post.imageBase64"
          />
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<style scoped>
.page-wrapper {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  background-color: #f3f4f6;
  padding: 40px 20px;
}

.posts-box {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 1300px;
  width: 100%;
}

.text-h5 {
  text-align: center;
  margin-bottom: 1.5rem;
}
</style>

