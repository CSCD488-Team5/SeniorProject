<template>
	<v-card class="mx-auto" fixed-width-card>
		<v-img height="200px" :src="computedImageSrc" cover></v-img>

		<v-card-title class="title-wrap">{{ title }}</v-card-title>

		<v-card-subtitle class="text-caption text-grey">
			Posted by 
			<router-link
				:to="{ name: 'Profile', params: { username: username } }"
				class="clickable-username"
				>
				@{{ username }}
			</router-link>
			&mdash; {{ location }}
		</v-card-subtitle>

		<v-card-actions>
			<v-btn color="orange-darken-3" text variant="tonal" @click="goToEventDetails">Explore</v-btn>
			<v-btn
  				:color="joined ? 'red-darken-3' : 'green-darken-3'"
  				text
  				variant="tonal"
  				@click="joined ? unjoinEvent() : joinEvent()"
			>
  				{{ joined ? 'Unjoin' : 'Join' }}
			</v-btn>

			<v-spacer></v-spacer>
			<v-btn icon @click="toggleContent">
				<v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
			</v-btn>
		</v-card-actions>

		<v-expand-transition>
			<div v-show="show">
				<v-divider></v-divider>
				<v-card-text>
					<p><strong>Category: </strong> {{ category }}</p>
					<p><strong>Date:</strong> {{ formattedDate }}</p>
					<p><strong>Time: </strong> {{  formattedTime }}</p>
					<p>{{ description }}</p>
				</v-card-text>
			</div>
		</v-expand-transition>
	</v-card>
</template>

<script setup>
import { ref, computed, getCurrentInstance} from 'vue'
import { useRouter } from 'vue-router'
import { getUsernameFromToken } from '@/utils/jwt'

const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http


const props = defineProps({
	id: Number,
	title: String,
	category: String,
	description: String,
	location: String,
	time: String,
	imageSrc: String,
	username: String, // Creator of the post
})

const computedImageSrc = computed(() => {
	if (!props.imageSrc) return ''
	// If imageSrc already has http, return it as is. Else, prepend localhost base.
	return props.imageSrc.startsWith('http')
		? props.imageSrc
		: `http://localhost:80${props.imageSrc}`
})

const show = ref(false)
const joined = ref(false)


const toggleContent = () => {
	show.value = !show.value
}

const router = useRouter();

function goToEventDetails() {
	router.push({ name: 'EventDetails', params: { id: props.id } });
}


async function joinEvent() {
	const username = getUsernameFromToken()
	if (!username) {
		alert('Please log in to join the event.')
		return
	}

	try {
		await axios.post(`http://localhost/api/events/${props.id}/join`, null, {
			params: { username }
		})
		joined.value = true
	} catch (error) {
		console.error('Join event error:', error.response);
		alert(error.response?.data || 'Failed to join the event')
	}
}

async function unjoinEvent() {
  const username = getUsernameFromToken()
  if (!username) {
    alert('Please log in to unjoin the event.')
    return
  }

  try {
    await axios.delete(`http://localhost/api/events/${props.id}/unjoin`, {
      params: { username }
    })
    joined.value = false
  } catch (error) {
    console.error('Unjoin event error:', error.response)
    alert(error.response?.data || 'Failed to unjoin the event')
  }
}

const formattedTime = computed( () => {
	if (!props.time) return ''
	const date = new Date(props.time)
	return date.toLocaleTimeString([], {  hour: '2-digit', minute: '2-digit' })
})

const formattedDate = computed(() => {
	if (!props.time) return ''
	const date = new Date(props.time)
	return date.toLocaleDateString([], { year: 'numeric', month: 'long', day: 'numeric' })
})

function goToUserProfile() {
  router.push({ name: 'Profile', params: { username: props.username } });
}
</script>





<style scoped>
.fixed-width-card
{
	width: 250px;
}

.title-wrap
{
	white-space: normal;
	overflow: visible;
	word-wrap: break-word;
}

.clickable-username {
  color: #1976d2;
  cursor: pointer;
  text-decoration: underline;
  margin-left: 4px;
  margin-right: 4px;
}
</style>
