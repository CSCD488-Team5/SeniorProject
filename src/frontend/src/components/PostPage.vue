<script setup>
import { ref } from 'vue';

const showModal = ref(false);
const newPost = ref({ title: '', description: '' });
const posts = ref([]);

const submitPost = () => {
  posts.value.push({ ...newPost.value });
  newPost.value.title = '';
  newPost.value.description = '';
  showModal.value = false;
};
</script>

<template>
  <div class="container">
    <div class="top-right">
      <button @click="showModal = true" class="create-button">Create Post</button>
    </div>

    <!-- Modal for Post Creation -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-content">
        <h2 class="title">Create a New Post</h2>
        <form @submit.prevent="submitPost">
          <div class="form-group">
            <label for="title">Title</label>
            <input
                v-model="newPost.title"
                id="title"
                type="text"
                class="input-field"
                placeholder="Enter post title"
                required
            />
          </div>
          <div class="form-group">
            <label for="description">Description</label>
            <textarea
                v-model="newPost.description"
                id="description"
                class="input-field"
                placeholder="Enter post description"
                required
            ></textarea>
          </div>
          <button type="submit" class="submit-button">Submit</button>
          <button type="button" @click="showModal = false" class="cancel-button">Cancel</button>
        </form>
      </div>
    </div>

    <!-- Posts Display -->
    <div class="posts-box">
      <h2 class="title">Posts</h2>
      <div v-if="posts.length === 0" class="no-posts">No posts yet!</div>
      <div v-else v-for="(post, index) in posts" :key="index" class="post">
        <h3>{{ post.title }}</h3>
        <p>{{ post.description }}</p>
      </div>
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
}

.title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.input-field {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
}

.input-field[type="text"] {
  height: 40px;
}

.input-field[type="textarea"],
textarea.input-field {
  height: 100px;
  resize: vertical;
}

.submit-button,
.cancel-button {
  width: 48%;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button {
  background-color: #3b82f6;
  color: white;
  margin-right: 4%;
}

.submit-button:hover {
  background-color: #2563eb;
}

.cancel-button {
  background-color: #ccc;
}

.cancel-button:hover {
  background-color: #b3b3b3;
}

.posts-box {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 100%;
}

.no-posts {
  text-align: center;
  color: #666;
}

.post {
  border-bottom: 1px solid #eee;
  padding: 1rem 0;
}

.post:last-child {
  border-bottom: none;
}

.post h3 {
  margin: 0 0 0.5rem;
  font-size: 1.25rem;
}

.post p {
  margin: 0;
  color: #555;
}
</style>