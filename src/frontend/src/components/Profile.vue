<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { getUsernameFromToken } from '@/utils/jwt.js'

const user = ref({
	username: '',
	email: '',
	createdAt: '',
})

const events = ref([])

const profileImageUrl = ref('https://cdn-icons-png.flaticon.com/512/149/149071.png') // placeholder avatar

const username = getUsernameFromToken()
const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http

onMounted(async () => {
	try {
		const userRes = await axios.get(`http://localhost/api/users/${username}`)
		user.value = userRes.data

		const eventRes = await axios.get(`http://localhost/api/events/user/${username}`)
		events.value = eventRes.data
	} catch (err) {
		console.error("Error loading profile:", err)
	}
})



</script>

<template>
	<v-container class="mt-10">
	  <v-card class="mx-auto" max-width="600">
		<v-card-title>
		  <v-avatar size="56" class="mr-3">
			<v-img :src="profileImageUrl" alt="Profile" />
		  </v-avatar>
		  <div>
			<h3 class="text-h6">{{ user.username }}</h3>
			<p class="text-subtitle-2">{{ user.email }}</p>
		  </div>
		</v-card-title>
  
		<v-card-text>
		  <p><strong>Joined:</strong> {{ user.createdAt }}</p>
		  <p><strong>Total Events:</strong> {{ events.length }}</p>
		</v-card-text>
  
		<v-divider class="my-4"></v-divider>
  
		<v-card-text>
		  <h4 class="text-subtitle-1 mb-2">My Events</h4>
		  <v-list>
			<v-list-item v-for="event in events" :key="event.id">
			  <v-list-item-content>
				<v-list-item-title>{{ event.title }}</v-list-item-title>
				<v-list-item-subtitle>{{ event.subtitle }}</v-list-item-subtitle>
			  </v-list-item-content>
			</v-list-item>
		  </v-list>
		</v-card-text>
	  </v-card>
	</v-container>
  </template>
