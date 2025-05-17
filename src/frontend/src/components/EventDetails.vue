<template>
  <v-container max-width="800px">
    <div v-if="eventData">
      <!-- Display the event image if available -->
      <v-img
        v-if="eventData.imageUrl"
        :src="formattedImageSrc"
        height="300px"
        cover
      />

      <h1 class="mt-4">{{ eventData.title }}</h1>

      <v-chip color="primary" class="ma-2" v-if="eventData.category">
        {{ eventData.category }}
      </v-chip>

      <p><strong>Created By:</strong> {{ eventData.user.username }}</p>
      <p><strong>Description:</strong> {{ eventData.description }}</p>
      <p><strong>Location:</strong> {{ eventData.location }}</p>

      <!-- Comments Section -->
      <v-card class="pa-4 mt-6">
        <h2 class="text-h6 mb-4">Comments</h2>
        <v-list two-line>
          <v-list-item v-for="comment in comments" :key="comment.id">
            <v-list-item-content>
              <v-list-item-title>{{ comment.user.username }}</v-list-item-title>
              <v-list-item-subtitle>
                {{ comment.comment }}
                <div class="text--secondary text-caption">
                  {{ formatCommentDate(comment.created) }}
                </div>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-list-item v-if="comments.length === 0">
            <v-list-item-content>
              <v-list-item-title>No comments yet.</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>

        <!-- Add Comment Form -->
        <v-form @submit.prevent="submitComment">
          <v-textarea
            v-model="newComment"
            label="Add a comment"
            rows="3"
            outlined
            required
          />
          <v-btn class="mt-3" color="primary" :disabled="!newComment.trim()" type="submit">
            Post Comment
          </v-btn>
        </v-form>
      </v-card>
    </div>

    <div v-else>
      <p>Loading event details...</p>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed, getCurrentInstance } from 'vue'
import dayjs from 'dayjs'
import { useRoute } from 'vue-router'

const route = useRoute()
const eventId = route.params.id

// Event data
const eventData = ref(null)
// Comments data
const comments = ref([])
const newComment = ref('')

// Axios instance
const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http

// Fetch event details
async function fetchEvent() {
  const { data } = await axios.get(`/api/events/${eventId}`)
  eventData.value = data
}

// Fetch comments
async function fetchComments() {
  const { data } = await axios.get(`/api/comments/${eventId}`)
  comments.value = data
}

// Submit a new comment
async function submitComment() {
  await axios.post(`/api/comments/${eventId}/postComment`, {
    comment: newComment.value
  })
  newComment.value = ''
  await fetchComments()
}

// Format comment timestamp
function formatCommentDate(dateStr) {
  return dayjs(dateStr).format('MMM D, YYYY h:mm A')
}

// Computed for image URL
const formattedImageSrc = computed(() => {
  if (eventData.value && eventData.value.imageUrl) {
    return eventData.value.imageUrl.startsWith('http')
      ? eventData.value.imageUrl
      : `http://localhost:80${eventData.value.imageUrl}`
  }
  return ''
})

// On mount, fetch both event and comments
onMounted(async () => {
  await fetchEvent()
  await fetchComments()
})
</script>

<style scoped>
.mt-4 {
  margin-top: 1rem;
}
.mt-6 {
  margin-top: 1.5rem;
}
</style>
