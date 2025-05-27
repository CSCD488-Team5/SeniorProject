<template>
  <v-container :key="remountKey">
    <v-btn color="primary" class="mb-4" @click="openCreateDialog">Create New Event</v-btn>

    <!-- If we have events, render them -->
    <v-row v-if="events.length">
      <v-col v-for="event in events" :key="event.id" cols="12" md="6" lg="4">
        <v-card>
          <v-img :src="event.imageUrl" cover></v-img>
          <v-card-title>{{ event.title }}</v-card-title>
          <v-card-subtitle>
            {{ event.location }} — {{ formatDate(event.time) }}
          </v-card-subtitle>
          <v-card-text>{{ event.description }}</v-card-text>
          <v-card-actions>
            <v-btn @click="editEvent(event)" variant="tonal">Edit</v-btn>
            <v-btn
              :loading="deletingId === event.id"
              :disabled="deletingId === event.id" 
              color="error" 
              @click="deleteEvent(event.id)" 
              variant="tonal">Delete</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <!-- if no events, show an alert -->
    <v-row v-else>
      <v-col cols="12">
        <v-alert type="info"  colored-border elevation="2">
          You have no events yet. Click "Create New Event" go get started.
        </v-alert>
      </v-col>
    </v-row>

    <!-- Dialog for Create/Update -->
      <v-dialog v-model="showDialog" max-width="500px">
        <v-card>
          <v-card-title>{{ isEditing ? 'Edit Event' : 'Create Event' }}</v-card-title>
          <v-form ref="formRef" @submit.prevent="submitForm">
          <v-card-text>
            <v-text-field 
              label="Title" 
              v-model="form.title" 
              :rules="[requiredRule]"/>
            <v-select
              label="Category"
              :items="categories"
              v-model="form.category"
              :rules="[requiredRule]"
            />
            <v-textarea 
              label="Description" 
              v-model="form.description" 
              :rules="[requiredRule]"/>
            <v-text-field 
              label="Location" 
              v-model="form.location"
              :rules="[requiredRule]"/>
            <v-text-field
              label="Time"
              v-model="form.time"
              type="datetime-local"
              :rules="[requiredRule, datetimeRule]"
            />
            <v-file-input
              v-model="form.image"
              label="Upload Image"
              accept="image/*"
              prepend-icon="mdi-image"
              :rules="[imageRule]"
            />
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn text @click="closeDialog" variant="tonal">Cancel</v-btn>
            <v-btn 
              color="primary" 
              type="submit"
              :loading="saving" 
              :disabled="!formRef?.isValid" 
              variant="tonal">Save</v-btn>
          </v-card-actions>
          </v-form>
        </v-card>
      </v-dialog>

      
  </v-container>

  <!-- Success Snackbar -->
    <v-snackbar 
      v-model="successSnackbar" 
      color="success" 
      timeout="3000"
      location="top"
      absolute
      elevation="2">
      {{ snackbarMessage }}
      <template #actions>
        <v-btn text @click="successSnackbar = false">Close</v-btn>
      </template>
    </v-snackbar>

    <!-- Error Snackbar -->
    <v-snackbar 
      v-model="errorSnackbar" 
      color="error" 
      timeout="3000"
      location="top"
      absolute
      elevation="2">
      {{ snackbarMessage }}
      <template #actions>
        <v-btn text @click="errorSnackbar = false">Close</v-btn>
      </template>
    </v-snackbar>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { getUsernameFromToken } from '@/utils/jwt'

const username = getUsernameFromToken();

const events = ref([])
const categories = ref([])

const showDialog = ref(false)
const isEditing = ref(false)
const editingId = ref(null)

const saving = ref(false)
const deletingId = ref(null)

// snackbars
const successSnackbar = ref(false)
const errorSnackbar = ref(false)
const snackbarMessage = ref('')

const remountKey = ref(0)

const requiredRule = (v) => !!v || 'This field is required'
const datetimeRule = (v) => !!v && !isNaN(Date.parse(v)) || 'Invalid date/time'
const imageRule = (file) => isEditing.value || (file instanceof File) || 'Image is required'

const form = ref({
  title: '',
  category: '',
  description: '',
  time: '',
  location: '',
  image: null
})

const handleFileChange = (files) => {
  form.value.image = files?.[0] || null
}

// Format date to readable format
const formatDate = (datetimeStr) => {
  const date = new Date(datetimeStr)
  return date.toLocaleDateString([], { year: 'numeric', month: 'short', day: 'numeric' }) +
         ' at ' +
         date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const loadEvents = async () => {
  try {
    const res = await axios.get(`/api/events/user/${username}`)
    events.value = res.data
  } catch (err) {
    if (err.response?.status === 404) {
      // No events, so just show empty list
      events.value = []
    } else {
      console.error('Error loading events:', err)
    }
    
    
  }
}

const openCreateDialog = () => {
  isEditing.value = false
  editingId.value = null
  form.value = {
    title: '',
    category: '',
    description: '',
    time: '',
    location: '',
    image: null
  }
  showDialog.value = true
}

const editEvent = (event) => {
  isEditing.value = true
  editingId.value = event.id
  form.value = {
    title: event.title,
    category: event.category,
    description: event.description,
    time: event.time,
    location: event.location,
    image: null
  }
  showDialog.value = true
}

const closeDialog = () => {
  showDialog.value = false
}

const formRef = ref(null)

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  const formData = new FormData()
  formData.append('title', form.value.title)
  formData.append('category', form.value.category)
  formData.append('description', form.value.description)
  formData.append('time', form.value.time)
  formData.append('location', form.value.location)
  formData.append('username', username)

  if (form.value.image) {
    formData.append('image', form.value.image)
  }

  // Set spinner
  saving.value = true

  try {
    if (isEditing.value) {
      const { data } = await axios.put(`/api/events/update/${editingId.value}`, formData)
      events.value = events.value.map(e => e.id === data.id ? data : e)
      snackbarMessage.value = 'Event updated successfully!'
    } else {
      const { data } = await axios.post('/api/events/upload', formData)
      
      events.value = [data, ...events.value]
      snackbarMessage.value = 'Event created successfully!'
    }

    remountKey.value++

    successSnackbar.value = true
    closeDialog()
  } catch (err) {
    console.error('Error submitting form:', err)

    // rollback on failure
    events.value = original

    // show error snackbar
    snackbarMessage.value = 'Failed to save event. Please try again.'
    errorSnackbar.value = true
  } finally {
    saving.value = false
  }
}

const deleteEvent = async (id) => {
  deletingId.value = id
  const original = [...events.value]
  events.value = events.value.filter(e => e.id !== id)
  try {
    await axios.delete(`/api/events/delete/${id}`)
    snackbarMessage.value = 'Event deleted successfully!'
    successSnackbar.value = true
  } catch (err) {
    console.error('Failed to delete event:', err)

    // rollback on error
    events.value = original
    snackbarMessage.value = 'Failed to delete event. Please try again.'
    errorSnackbar.value = true
  } finally {
    deletingId.value = null
  }
}

const fetchCategories = async () => {
  try {
    const res = await axios.get('/api/events/categories')
    categories.value = res.data
  } catch (err) {
    console.error('Failed to load categories:', err)
  }
}

onMounted(() => {
  loadEvents()
  fetchCategories()
})
</script>
