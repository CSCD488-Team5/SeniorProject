<template>
  <v-container>
    <v-btn color="primary" @click="openCreateDialog">Create New Event</v-btn>

    <v-row>
      <v-col v-for="event in events" :key="event.id" cols="12" md="6" lg="4">
        <v-card>
          <v-img :src="getImageUrl(event.imageUrl)" height="200px" cover></v-img>
          <v-card-title>{{ event.title }}</v-card-title>
          <v-card-subtitle>
            {{ event.location }} — {{ formatDate(event.time) }}
          </v-card-subtitle>
          <v-card-text>{{ event.description }}</v-card-text>
          <v-card-actions>
            <v-btn @click="editEvent(event)">Edit</v-btn>
            <v-btn color="error" @click="deleteEvent(event.id)">Delete</v-btn>
          </v-card-actions>
        </v-card>
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
            />
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn text @click="closeDialog">Cancel</v-btn>
            <v-btn color="primary" type="submit" :disabled="!formRef?.isValid">Save</v-btn>
          </v-card-actions>
          </v-form>
        </v-card>
      </v-dialog>
  </v-container>
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

const requiredRule = (v) => !!v || 'This field is required'
const datetimeRule = (v) => !!v && !isNaN(Date.parse(v)) || 'Invalid date/time'

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

const getImageUrl = (path) => `http://localhost${path}`

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
    console.error('Error loading events:', err)
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

  // 📋 DEBUG: inspect everything
  for (let [key, v] of formData.entries()) {
    console.log(key, v)
  }

  try {
    if (isEditing.value) {
      await axios.put(`/api/events/update/${editingId.value}`, formData)
    } else {

      await axios.post('/api/events/upload', formData)
    }

    await loadEvents()
    closeDialog()
  } catch (err) {
    console.error('Error submitting form:', err)
  }
}

const deleteEvent = async (id) => {
  try {
    await axios.delete(`/api/events/delete/${id}`)
    await loadEvents()
  } catch (err) {
    console.error('Failed to delete event:', err)
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
