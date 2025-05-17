<template>
  <v-card class="pa-4">
    <h2 class="text-h6 mb-4">Comments</h2>
    <!-- Comments List -->
    <v-list two-line>
      <v-list-item v-for="comment in comments" :key="comment.id">
        <v-list-item-content>
          <v-list-item-title>{{ comment.user.username }}</v-list-item-title>
          <v-list-item-subtitle>
            {{ comment.content }}
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
      <v-btn
        class="mt-3"
        color="primary"
        :disabled="!newComment.trim()"
        type="submit"
      >
        Post Comment
      </v-btn>
    </v-form>
  </v-card>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import dayjs from 'dayjs'
import { useRoute } from 'vue-router'

const props = defineProps({
  eventId: {
    type: [String, Number],
    required: true
  }
})

const comments = ref([])
const newComment = ref('')
const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http

async function fetchComments() {
  const { data } = await axios.get(`/api/comments/${props.eventId}`)
  comments.value = data
}

async function submitComment() {
  await axios.post(`/api/events/${props.eventId}/comments`, {
    content: newComment.value
  })
  newComment.value = ''
  await fetchComments()
}

function formatCommentDate(dateStr) {
  return dayjs(dateStr).format('MMM D, YYYY h:mm A')
}

onMounted(fetchComments)
</script>
