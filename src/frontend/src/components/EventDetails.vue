<template>
  <v-container max-width="800px" class="py-6">
    <div v-if="eventData">
      <v-card class="mb-6" elevation="4" rounded="lg">
        <v-img
          v-if="eventData.imageUrl"
          :src="formattedImageSrc"
          height="400px"
          cover
          class="event-image"
        >
          <template v-slot:placeholder>
            <v-row class="fill-height ma-0" align="center" justify="center">
              <v-progress-circular indeterminate color="primary"></v-progress-circular>
            </v-row>
          </template>
        </v-img>

        <v-card-text class="pa-6">
          <!-- Event Title and Category -->
          <div class="d-flex align-center mb-4">
            <h1 class="text-h4 font-weight-bold flex-grow-1">{{ eventData.title }}</h1>
            <v-chip
              v-if="eventData.category"
              color="primary"
              size="large"
              class="ml-4"
            >
              {{ eventData.category }}
            </v-chip>
          </div>

          <!-- Event Details -->
          <v-row class="mt-4">
            <v-col cols="12" md="6">
              <div class="event-detail-item">
                <v-icon icon="mdi-account" class="mr-2" color="primary"></v-icon>
                <span class="text-subtitle-1">Created by {{ eventData.user.username }}</span>
              </div>
            </v-col>
            <v-col cols="12" md="6">
              <div class="event-detail-item">
                <v-icon icon="mdi-map-marker" class="mr-2" color="primary"></v-icon>
                <span class="text-subtitle-1">{{ eventData.location }}</span>
              </div>
            </v-col>
          </v-row>

          <!-- Event Description -->
          <v-card class="mt-6 pa-4" variant="outlined">
            <h2 class="text-h6 mb-3">Description</h2>
            <p class="event-description">{{ eventData.description }}</p>
          </v-card>
        </v-card-text>
      </v-card>

      <!-- Comments Section -->
      <v-card class="mt-6" elevation="4" rounded="lg">
        <v-card-title class="text-h6 pa-4 pb-2">
          Comments
          <v-spacer></v-spacer>
          <span class="text-caption text-medium-emphasis">{{ comments.length }} comments</span>
        </v-card-title>

        <v-card-text class="pa-4">
          <!-- Comments List -->
          <div class="comments-container">
            <div v-if="comments.length === 0" class="text-center py-4 text-medium-emphasis">
              No comments yet. Be the first to comment!
            </div>
            
            <div v-for="comment in comments" :key="comment.id" class="comment-item mb-4">
              <div class="d-flex align-center">
                <div class="flex-grow-1">
                  <div class="d-flex align-center">
                    <span class="text-subtitle-1 font-weight-medium">{{ comment.user.username }}</span>
                    <span class="text-caption text-medium-emphasis ml-2">{{ formatCommentDate(comment.timeStamp) }}</span>
                  </div>
                  <p class="comment-text mt-1 mb-0">{{ comment.comment }}</p>
                </div>
                <v-btn
                  v-if="currentUser && comment.user.username === currentUser"
                  icon
                  variant="text"
                  size="small"
                  color="error"
                  class="ml-2"
                  @click="deleteComment(comment.id)"
                >
                  <v-icon size="small">mdi-delete</v-icon>
                </v-btn>
              </div>
            </div>
          </div>

          <!-- Add Comment Form -->
          <v-divider class="my-4"></v-divider>
          <v-form @submit.prevent="submitComment">
            <v-textarea
              v-model="newComment"
              label="Add a comment"
              rows="3"
              variant="outlined"
              density="comfortable"
              hide-details="auto"
              class="mb-2"
              required
            ></v-textarea>
            <div class="d-flex justify-end">
              <v-btn
                color="primary"
                type="submit"
                :disabled="!newComment.trim()"
                class="mt-2"
              >
                Post Comment
              </v-btn>
            </div>
          </v-form>
        </v-card-text>
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
import {getUsernameFromToken} from "@/utils/jwt.js";

const route = useRoute()
const eventId = route.params.id

// Event data
const eventData = ref(null)
// Comments data
const comments = ref([])
const newComment = ref('')
//grabs the current user
const currentUser = ref(null);

const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http

// Fetch event details
async function fetchEvent() {
  const { data } = await axios.get(`/api/events/${eventId}`)
  eventData.value = data
}

async function fetchCurrentUser(){
    const username = getUsernameFromToken();
    currentUser.value = username;
}

// Fetch comments
async function fetchComments() {
  const { data } = await axios.get(`/api/comments/${eventId}`);
  console.log("RAW COMMENTS JSON:", data);
  comments.value = data;
}

// Submit a new comment
async function submitComment() {
  const now = new Date().toISOString().slice(0,19);//Formats the time stamp
  
  await axios.post(`/api/comments/${eventId}/postComment`, {
    comment: newComment.value,
    timeStamp: now
  })
  newComment.value = ''
  await fetchComments()
}

//Delete a comment
async function deleteComment(commentid) {
  await axios.delete(`/api/comments/${commentid}/deleteComment`)
  await fetchComments(); 
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
  await fetchCurrentUser()
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
.mb-4 {
  margin-bottom: 1rem;
}
.mb-6 {
  margin-bottom: 1.5rem;
}
.font-weight-bold {
  font-weight: bold;
}

.event-image {
  border-radius: 8px 8px 0 0;
}

.event-detail-item {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.event-description {
  color: rgba(0, 0, 0, 0.87);
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 1.1rem;
}

.comments-container {
  max-height: 600px;
  overflow-y: auto;
}

.comment-item {
  padding: 8px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.comment-item:hover {
  background-color: rgba(0, 0, 0, 0.03);
}

.comment-text {
  color: rgba(0, 0, 0, 0.87);
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-word;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .text-h4 {
    font-size: 1.75rem !important;
  }
  
  .event-detail-item {
    margin-bottom: 0.75rem;
  }
  
  .v-card-text {
    padding: 1rem !important;
  }
}
</style>