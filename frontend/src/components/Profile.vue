<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { getUsernameFromToken } from '@/utils/jwt.js'
import EventCard from './EventCard.vue'
import { useRoute } from 'vue-router'
import FollowButton from './FollowButton.vue'


const route = useRoute()
const profileUsername = ref(route.params.username)

const user = ref({
	username: '',
	email: '',
	createdAt: '',
})

const events = ref([])
const followersCounter = ref(0)

const profileImageUrl = ref('https://cdn-icons-png.flaticon.com/512/149/149071.png') // placeholder avatar

// const username = getUsernameFromToken()
const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http

onMounted(async () => {
	try {
		const userRes = await axios.get(`http://localhost/api/users/${profileUsername.value}`)
		user.value = userRes.data

		const eventRes = await axios.get(`http://localhost/api/events/user/${profileUsername.value}`)
		events.value = eventRes.data

		const followersRes = await axios.get(`api/users/${profileUsername.value}/follow-count`)
		followersCounter.value = followersRes.data;
	} catch (err) {
		console.error("Error loading profile:", err)
	}
})

const currentUsername = getUsernameFromToken()

</script>

<template>
	<v-container class="mt-10">
		<v-card class="mx-auto" max-width="1000">
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
				<p><strong>Followers:</strong> {{ followersCounter }}</p>
			</v-card-text>

			<v-card-actions>
				<FollowButton
				v-if="user.username && user.username !== currentUsername"
				:profileUserId="user.id">
				</FollowButton>
			</v-card-actions>
			
		</v-card>
	</v-container>

	<v-container class="mt-6" fluid>
		<v-row justify="center" align="stretch">
			<v-col
				v-for="event in events"
				:key="event.id"
				cols="12"
				sm="6"
				md="4"
				lg="3"
			>
				<EventCard
				:id="event.id"
				:title="event.title"
				:category="event.category"
				:description="event.description"
				:location="event.location"
				:time="event.time"
				:imageSrc="event.imageUrl"
				:username="event.user.username"></EventCard>
			</v-col>
		</v-row>
	</v-container>
</template>

<style scoped>

/* This wrapper forces each card to a fixed width and adds spacing between them */
.card-wrapper
{
  width: 300px;
  /* Set the desired fixed width for each card */
  margin-right: 16px;
  /* Space between cards */
  padding: 10px;
}
</style>
