<script setup>
import {ref, onMounted, getCurrentInstance} from 'vue';
import EventCard from '@/components/EventCard.vue'; // adjust path if needed

const posts = ref([]);
const { appContext } = getCurrentInstance();
const axios = appContext.config.globalProperties.$http;

// Fetch posts from the backend when the component is mounted
onMounted(async () => {
  try {
    const response = await axios.get("http://localhost/api/PostPageController/2");
    posts.value = response.data;
  } catch (err) {
    console.error('Error fetching posts:', err);
  }
});
</script>

<template>
  <div class="page-wrapper">
    <div class="posts-box">
      <h2 class="text-h5 mb-4">Posts</h2>

      <v-alert v-if="posts.length === 0" type="info" text class="mb-4">
        No posts yet!
      </v-alert>

      <v-row v-else justify="center" align="stretch" class="post-grid">
        <v-col
            v-for="post in posts"
            :key="post.id"
            cols="12"
            sm="6"
            md="4"
            lg="3"
        >
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
