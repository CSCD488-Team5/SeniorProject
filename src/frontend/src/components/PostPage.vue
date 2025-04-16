<script setup>
import { ref, computed } from 'vue';
import PostCard from '@/components/MyPostCard.vue';
import Vuetify from "@/plugins/vuetify.js";


const showModal = ref(false);
const newPost = ref({
  title: '',
  subtitle: '',
  description: '',
  category: '',
  date: '',
  time: '',
  location: '',
  image: '',
});
const posts = ref([]);

// Compute dateAndTime as ISO string for PostCard
const dateAndTime = computed(() => {
  if (newPost.value.date && newPost.value.time) {
    return `${newPost.value.date}T${newPost.value.time}:00`;
  }
  return '';
});

const submitPost = () => {
  posts.value.push({
    id: posts.value.length + 1,
    title: newPost.value.title,
    subtitle: newPost.value.subtitle,
    description: newPost.value.description,
    category: newPost.value.category,
    dateAndTime: dateAndTime.value, // Use computed ISO string
    location: newPost.value.location,
    image: newPost.value.image,
  });
  newPost.value = {
    title: '',
    subtitle: '',
    description: '',
    category: '',
    date: '',
    time: '',
    location: '',
    image: '',
  };
  showModal.value = false;
};

// Menu refs for date and time pickers
const dateMenu = ref(false);
const timeMenu = ref(false);
</script>

<template>
  <div class="container">
    <div class="top-right">
      <button
          @click="showModal = true"
          class="create-button"
      >
        Create Post
      </button>
    </div>

    <!-- Modal for Post Creation -->
    <v-dialog
        v-model="showModal"
        max-width="500"
        persistent
    >
      <v-card>
        <v-card-title class="text-h5">
          Create a New Post
        </v-card-title>
        <v-card-text>
          <v-form @submit.prevent="submitPost">
            <v-text-field
                v-model="newPost.title"
                label="Title"
                placeholder="Enter post title"
                required
            ></v-text-field>
            <v-text-field
                v-model="newPost.subtitle"
                label="Subtitle"
                placeholder="Enter subtitle"
                required
            ></v-text-field>
            <v-textarea
                v-model="newPost.description"
                label="Description"
                placeholder="Enter post description"
                required
            ></v-textarea>
            <v-text-field
                v-model="newPost.category"
                label="Category"
                placeholder="E.g., Sports"
                required
            ></v-text-field>
            <!-- Date Picker -->
            <v-menu
                v-model="dateMenu"
                :close-on-content-click="false"
                transition="scale-transition"
                offset-y
                min-width="auto"
            >
              <template v-slot:activator="{ props }">
                <v-text-field
                    v-model="newPost.date"
                    label="Date"
                    prepend-icon="mdi-calendar"
                    readonly
                    v-bind="props"
                    required
                ></v-text-field>
              </template>
              <v-date-picker
                  v-model="newPost.date"
                  @update:model-value="dateMenu = false"
              ></v-date-picker>
            </v-menu>
            <!-- Time Picker -->
            <v-menu
                v-model="timeMenu"
                :close-on-content-click="false"
                transition="scale-transition"
                offset-y
                min-width="auto"
            >
              <template v-slot:activator="{ props }">
                <v-text-field
                    v-model="newPost.time"
                    label="Time"
                    prepend-icon="mdi-clock"
                    readonly
                    v-bind="props"
                    required
                ></v-text-field>
              </template>
              <v-time-picker
                  v-model="newPost.time"
                  format="ampm"

                  @update:model-value="timeMenu = false"
              ></v-time-picker>
            </v-menu>
            <v-text-field
                v-model="newPost.location"
                label="Location"
                placeholder="Enter location"
                required
            ></v-text-field>
            <v-text-field
                v-model="newPost.image"
                label="Image URL"
                placeholder="Enter image URL"
                required
            ></v-text-field>
            <v-row>
              <v-col>
                <v-btn
                    color="primary"
                    type="submit"
                    block
                >
                  Submit
                </v-btn>
              </v-col>
              <v-col>
                <v-btn
                    color="grey"
                    @click="showModal = false"
                    block
                >
                  Cancel
                </v-btn>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- Posts Display -->
    <div class="posts-box">
      <h2 class="text-h5 mb-4">Posts</h2>
      <v-alert
          v-if="posts.length === 0"
          type="info"
          text
          class="mb-4"
      >
        No posts yet!
      </v-alert>
      <v-row v-else>
        <v-col
            v-for="post in posts"
            :key="post.id"
            cols="12"
            sm="6"
            md="4"
        >
          <post-card
              :id="post.id"
              :title="post.title"
              :subtitle="post.subtitle"
              :description="post.description"
              :category="post.category"
              :time-date="post.dateAndTime"
              :location="post.location"
              :image="post.image"
          />
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f3f4f6;
  position: relative;
  padding: 20px;
}

.top-right {
  position: absolute;
  top: 20px;
  right: 20px;
}

.create-button {
  padding: 0.5rem 1rem;
  background-color: #10b981;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.create-button:hover {
  background-color: #059669;
}

.posts-box {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  width: 100%;
}

.text-h5 {
  text-align: center;
  margin-bottom: 1.5rem;
}
</style>