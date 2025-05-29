<template>
	<v-btn
		:color="isFollowing ? 'grey' : 'primary'"
		@click="toggleFollow"
	>
		{{ isFollowing ? 'Unfollow' : 'Follow' }}
	</v-btn>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const { profileUserId } = defineProps({
	profileUserId: {
		type: Number,
		required: true
	}
})

const isFollowing = ref(false);

// Check follow status on mount
onMounted(async () => {
	try {
		const { data } = await axios.get(`/api/users/${profileUserId}/is-following`)
		isFollowing.value = data;
	} catch (err) {
		console.error("Could not check follow status", err)
	}
})

// Toggle follow/unfollow
const toggleFollow = async () => {
	try {
		if (isFollowing.value) {
			await axios.delete(`/api/users/${profileUserId}/unfollow`)
		} else {
			await axios.post(`/api/users/${profileUserId}/follow`)
		}

		isFollowing.value = !isFollowing.value
	} catch (err) {
		console.error("Follow toggle failed", err)
	}
}
</script>
